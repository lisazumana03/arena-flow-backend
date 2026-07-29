package za.co.lz.service.team.transfer;

import za.co.lz.domain.team.transfer.Transfer;
import za.co.lz.domain.team.transfer.TransferStatus;
import za.co.lz.service.IService;

import java.util.List;
import java.util.UUID;

public interface ITransferService extends IService<Transfer, UUID> {

    // All transfers reported within a given window (the "ticker" feed)
    List<Transfer> findByWindow(UUID windowId);

    // All transfers currently sitting at a given reliability tier
    List<Transfer> findByStatus(TransferStatus status);

    // Move a transfer forward one tier, or collapse it. Rejects invalid jumps.
    Transfer advanceStatus(UUID transferId, TransferStatus next);

    /**
     * Runs the finalize saga: verifies the window is open, checks and spends
     * the buying team's transfer budget, moves the SquadRegistration from
     * the selling team to the buying team, re-points Player.team, and marks
     * the Transfer OFFICIAL. Fully rolled back on any failure.
     */
    Transfer finalizeTransfer(UUID transferId, UUID seasonId, UUID buyingTeamBudgetId, int newKitNumber);
}
