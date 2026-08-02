package za.co.lz.service.team.discipline.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.match.DisciplinaryOffence;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.discipline.PlayerSuspension;
import za.co.lz.factory.team.discipline.PlayerSuspensionFactory;
import za.co.lz.repository.team.discipline.PlayerSuspensionRepository;
import za.co.lz.service.team.discipline.IPlayerSuspensionService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlayerSuspensionServiceImpl implements IPlayerSuspensionService {

    @Autowired
    private PlayerSuspensionRepository playerSuspensionRepository;

    @Override
    public PlayerSuspension create(PlayerSuspension playerSuspension) {
        return playerSuspensionRepository.save(playerSuspension);
    }

    @Override
    public List<PlayerSuspension> findAll() {
        return playerSuspensionRepository.findAll();
    }

    @Override
    public Optional<PlayerSuspension> findById(UUID uuid) {
        return playerSuspensionRepository.findById(uuid);
    }

    @Override
    public PlayerSuspension update(PlayerSuspension playerSuspension, UUID uuid) {
        return playerSuspensionRepository.save(playerSuspension);
    }

    @Override
    public void delete(UUID uuid) {
        playerSuspensionRepository.deleteById(uuid);
    }

    @Override
    public PlayerSuspension issueSuspension(Player player, Match triggeringMatch, DisciplinaryOffence offence) {
        return playerSuspensionRepository.save(PlayerSuspensionFactory.createSuspension(player, triggeringMatch, offence));
    }

    @Override
    public PlayerSuspension issueSuspension(Player player, Match triggeringMatch, DisciplinaryOffence offence, int gamesBanned) {
        return playerSuspensionRepository.save(PlayerSuspensionFactory.createSuspension(player, triggeringMatch, offence, gamesBanned));
    }

    @Override
    public List<PlayerSuspension> serveGameForTeam(Team team) {
        List<PlayerSuspension> active = playerSuspensionRepository.findByActiveTrue().stream()
                .filter(s -> s.getPlayer().getTeam() != null
                        && s.getPlayer().getTeam().getTeamId().equals(team.getTeamId()))
                .collect(Collectors.toList());

        active.forEach(PlayerSuspension::serveGame);
        return playerSuspensionRepository.saveAll(active);
    }

    @Override
    public boolean isPlayerSuspended(UUID playerId) {
        return !playerSuspensionRepository.findByPlayer_PlayerIdAndActiveTrue(playerId).isEmpty();
    }

    @Override
    public List<PlayerSuspension> getActiveSuspensions(UUID playerId) {
        return playerSuspensionRepository.findByPlayer_PlayerIdAndActiveTrue(playerId);
    }

    @Override
    public List<PlayerSuspension> getSuspensionHistory(UUID playerId) {
        return playerSuspensionRepository.findByPlayer_PlayerId(playerId);
    }
}
