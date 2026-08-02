package za.co.lz.service.team.discipline.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.match.InjurySeverity;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.discipline.PlayerInjury;
import za.co.lz.factory.team.discipline.PlayerInjuryFactory;
import za.co.lz.repository.team.discipline.PlayerInjuryRepository;
import za.co.lz.service.team.discipline.IPlayerInjuryService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerInjuryServiceImpl implements IPlayerInjuryService {

    @Autowired
    private PlayerInjuryRepository playerInjuryRepository;

    @Override
    public PlayerInjury create(PlayerInjury playerInjury) {
        return playerInjuryRepository.save(playerInjury);
    }

    @Override
    public List<PlayerInjury> findAll() {
        return playerInjuryRepository.findAll();
    }

    @Override
    public Optional<PlayerInjury> findById(UUID uuid) {
        return playerInjuryRepository.findById(uuid);
    }

    @Override
    public PlayerInjury update(PlayerInjury playerInjury, UUID uuid) {
        return playerInjuryRepository.save(playerInjury);
    }

    @Override
    public void delete(UUID uuid) {
        playerInjuryRepository.deleteById(uuid);
    }

    @Override
    public PlayerInjury reportInjury(Player player, Match triggeringMatch, InjurySeverity severity, LocalDate injuryDate) {
        return playerInjuryRepository.save(PlayerInjuryFactory.createInjury(player, triggeringMatch, severity, injuryDate));
    }

    @Override
    public PlayerInjury updateExpectedReturn(UUID injuryId, LocalDate newExpectedReturnDate) {
        PlayerInjury injury = playerInjuryRepository.findById(injuryId)
                .orElseThrow(() -> new IllegalArgumentException("Injury not found with ID: " + injuryId));
        injury.setExpectedReturnDate(newExpectedReturnDate);
        return playerInjuryRepository.save(injury);
    }

    @Override
    public PlayerInjury markRecovered(UUID injuryId) {
        PlayerInjury injury = playerInjuryRepository.findById(injuryId)
                .orElseThrow(() -> new IllegalArgumentException("Injury not found with ID: " + injuryId));
        injury.markRecovered();
        return playerInjuryRepository.save(injury);
    }

    @Override
    public boolean isPlayerInjured(UUID playerId) {
        return !playerInjuryRepository.findByPlayer_PlayerIdAndActiveTrue(playerId).isEmpty();
    }

    @Override
    public List<PlayerInjury> getActiveInjuries(UUID playerId) {
        return playerInjuryRepository.findByPlayer_PlayerIdAndActiveTrue(playerId);
    }

    @Override
    public List<PlayerInjury> getInjuryHistory(UUID playerId) {
        return playerInjuryRepository.findByPlayer_PlayerId(playerId);
    }
}
