package za.co.lz.service.team.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.TeamType;
import za.co.lz.repository.team.PlayerRepository;
import za.co.lz.repository.team.TeamRepository;
import za.co.lz.service.team.IPlayerService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerServiceImpl implements IPlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Override
    public Player create(Player player) {
        assignNationalTeamIfMatched(player);
        return playerRepository.save(player);
    }

    /**
     * If the player wasn't already assigned to a team, and their nationality matches
     * the teamNationality of an existing NATIONAL team (e.g. "England"), automatically
     * assign that national team to the player. An explicit team on the incoming
     * request is always respected and never overridden.
     */
    private void assignNationalTeamIfMatched(Player player) {
        if (player.getTeam() != null) {
            return;
        }
        String nationality = player.getPlayerNationality();
        if (nationality == null || nationality.isBlank()) {
            return;
        }
        Optional<Team> matchingNationalTeam = teamRepository.findAll().stream()
                .filter(team -> team.getTeamType() == TeamType.NATIONAL)
                .filter(team -> nationality.equalsIgnoreCase(team.getTeamNationality()))
                .findFirst();
        matchingNationalTeam.ifPresent(player::setTeam);
    }

    @Override
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    @Override
    public Optional<Player> findById(UUID uuid) {
        return playerRepository.findById(uuid);
    }

    @Override
    public Player update(Player player, UUID uuid) {
        return playerRepository.save(player);
    }

    @Override
    public void delete(UUID uuid) {
        playerRepository.deleteById(uuid);
    }
}
