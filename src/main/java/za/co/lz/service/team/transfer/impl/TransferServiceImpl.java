package za.co.lz.service.team.transfer.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.lz.domain.match.Season;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.SquadRegistration;
import za.co.lz.domain.team.SquadRegistrationStatus;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.finances.TeamBudget;
import za.co.lz.domain.team.transfer.Transfer;
import za.co.lz.domain.team.transfer.TransferStatus;
import za.co.lz.repository.match.SeasonRepository;
import za.co.lz.repository.team.PlayerRepository;
import za.co.lz.repository.team.SquadRegistrationRepository;
import za.co.lz.repository.team.finances.TeamBudgetRepository;
import za.co.lz.repository.team.transfer.TransferRepository;
import za.co.lz.service.team.transfer.ITransferService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransferServiceImpl implements ITransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private TeamBudgetRepository teamBudgetRepository;

    @Autowired
    private SquadRegistrationRepository squadRegistrationRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Override
    public Transfer create(Transfer transfer) {
        return transferRepository.save(transfer);
    }

    @Override
    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    @Override
    public Optional<Transfer> findById(UUID uuid) {
        return transferRepository.findById(uuid);
    }

    @Override
    public Transfer update(Transfer transfer, UUID uuid) {
        return transferRepository.save(transfer);
    }

    @Override
    public void delete(UUID uuid) {
        transferRepository.deleteById(uuid);
    }

    @Override
    public List<Transfer> findByWindow(UUID windowId) {
        return transferRepository.findByWindow_WindowId(windowId);
    }

    @Override
    public List<Transfer> findByStatus(TransferStatus status) {
        return transferRepository.findByStatus(status);
    }

    @Override
    public Transfer advanceStatus(UUID transferId, TransferStatus next) {
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new IllegalArgumentException("Transfer not found with ID: " + transferId));

        if (!transfer.getStatus().canAdvanceTo(next)) {
            throw new IllegalStateException(
                    "Cannot move transfer from " + transfer.getStatus() + " to " + next
                            + ". A transfer can only advance one tier at a time, or collapse.");
        }

        // OFFICIAL is only reachable through finalizeTransfer(), never a bare status flip,
        // since it has to carry out the budget spend and squad changes atomically.
        if (next == TransferStatus.OFFICIAL) {
            throw new IllegalStateException("Use finalizeTransfer() to move a transfer to OFFICIAL.");
        }

        transfer.setStatus(next);
        return transferRepository.save(transfer);
    }

    @Override
    @Transactional
    public Transfer finalizeTransfer(UUID transferId, UUID seasonId, UUID buyingTeamBudgetId, int newKitNumber) {
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new IllegalArgumentException("Transfer not found with ID: " + transferId));

        if (transfer.getStatus().isTerminal()) {
            throw new IllegalStateException("Transfer is already " + transfer.getStatus() + " and cannot be finalized.");
        }
        if (transfer.getStatus() != TransferStatus.MEDICAL_SCHEDULED) {
            throw new IllegalStateException(
                    "Transfer must reach MEDICAL_SCHEDULED before it can be finalized. Currently: " + transfer.getStatus());
        }

        // 1. Window must be open today - no deadline-day exceptions.
        LocalDate today = LocalDate.now();
        if (!transfer.getWindow().isOpen(today)) {
            throw new IllegalStateException("Transfer window is closed. The deal cannot be completed.");
        }

        // 2. Buying team must be able to afford the agreed fee.
        TeamBudget budget = teamBudgetRepository.findById(buyingTeamBudgetId)
                .orElseThrow(() -> new IllegalArgumentException("Budget not found with ID: " + buyingTeamBudgetId));
        if (!budget.getTeam().getTeamId().equals(transfer.getBuyingTeam().getTeamId())) {
            throw new IllegalStateException("Supplied budget does not belong to the buying team.");
        }
        budget.spendOnTransfers(transfer.getAgreedFee());
        teamBudgetRepository.save(budget);

        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new IllegalArgumentException("Season not found with ID: " + seasonId));
        Team buyingTeam = transfer.getBuyingTeam();
        Player player = transfer.getPlayer();

        // 3. Close out the old registration at the selling team (if any - free agents have none active).
        squadRegistrationRepository.findByPlayer_PlayerId(player.getPlayerId()).stream()
                .filter(reg -> reg.getStatus() == SquadRegistrationStatus.ACTIVE)
                .forEach(reg -> {
                    reg.setStatus(SquadRegistrationStatus.TRANSFERRED);
                    squadRegistrationRepository.save(reg);
                });

        // 4. Register the player at the buying team for the current season.
        squadRegistrationRepository.findByTeam_TeamIdAndSeason_SeasonIdAndKitNumber(
                buyingTeam.getTeamId(), season.getSeasonId(), newKitNumber)
                .ifPresent(existing -> {
                    throw new IllegalStateException(
                            "Kit number " + newKitNumber + " is already taken at the buying team for this season.");
                });

        SquadRegistration newRegistration = new SquadRegistration.Builder()
                .setSquadRegistrationId(UUID.randomUUID())
                .setPlayer(player)
                .setTeam(buyingTeam)
                .setSeason(season)
                .setKitNumber(newKitNumber)
                .setRegistrationDate(today)
                .setStatus(SquadRegistrationStatus.ACTIVE)
                .build();
        squadRegistrationRepository.save(newRegistration);

        // 5. Re-point the player's team of record.
        Player updatedPlayer = new Player.Builder()
                .copy(player)
                .setTeam(buyingTeam)
                .build();
        playerRepository.save(updatedPlayer);

        // 6. Mark the transfer OFFICIAL last, once everything above has succeeded.
        transfer.setStatus(TransferStatus.OFFICIAL);
        transfer.setConfirmedDate(today);
        return transferRepository.save(transfer);
    }
}
