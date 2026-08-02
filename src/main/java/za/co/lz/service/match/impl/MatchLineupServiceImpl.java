package za.co.lz.service.match.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.MatchLineup;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;
import za.co.lz.factory.match.MatchLineupFactory;
import za.co.lz.repository.match.MatchLineupRepository;
import za.co.lz.service.match.IMatchLineupService;
import za.co.lz.service.team.discipline.IPlayerInjuryService;
import za.co.lz.service.team.discipline.IPlayerSuspensionService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MatchLineupServiceImpl implements IMatchLineupService {

    @Autowired
    private MatchLineupRepository matchLineupRepository;

    @Autowired
    private IPlayerSuspensionService playerSuspensionService;

    @Autowired
    private IPlayerInjuryService playerInjuryService;

    @Override
    public MatchLineup create(MatchLineup matchLineup) {
        return matchLineupRepository.save(matchLineup);
    }

    @Override
    public List<MatchLineup> findAll() {
        return matchLineupRepository.findAll();
    }

    @Override
    public Optional<MatchLineup> findById(UUID uuid) {
        return matchLineupRepository.findById(uuid);
    }

    @Override
    public MatchLineup update(MatchLineup matchLineup, UUID uuid) {
        return matchLineupRepository.save(matchLineup);
    }

    @Override
    public void delete(UUID uuid) {
        matchLineupRepository.deleteById(uuid);
    }

    @Override
    public MatchLineup namePlayer(Match match, Team team, Player player, boolean starting, int shirtNumber, PlayerPosition matchPosition) {
        if (playerSuspensionService.isPlayerSuspended(player.getPlayerId())) {
            throw new IllegalStateException("Player " + player.getPlayerName() + " is currently suspended and cannot be named");
        }
        if (playerInjuryService.isPlayerInjured(player.getPlayerId())) {
            throw new IllegalStateException("Player " + player.getPlayerName() + " is currently injured and cannot be named");
        }
        MatchLineup lineup = MatchLineupFactory.createLineupEntry(match, team, player, starting, shirtNumber, matchPosition);
        return matchLineupRepository.save(lineup);
    }

    @Override
    public List<MatchLineup> getLineupForMatch(UUID matchId) {
        return matchLineupRepository.findByMatch_MatchId(matchId);
    }

    @Override
    public List<MatchLineup> getLineupForMatchAndTeam(UUID matchId, UUID teamId) {
        return matchLineupRepository.findByMatch_MatchIdAndTeam_TeamId(matchId, teamId);
    }

    @Override
    public List<MatchLineup> getStartingXI(UUID matchId, UUID teamId) {
        return matchLineupRepository.findByMatch_MatchIdAndTeam_TeamId(matchId, teamId).stream()
                .filter(MatchLineup::isStarting)
                .toList();
    }

    @Override
    public MatchLineup substitutePlayerOff(UUID matchId, UUID playerId, int minute) {
        MatchLineup lineup = matchLineupRepository.findByMatch_MatchId(matchId).stream()
                .filter(l -> l.getPlayer().getPlayerId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player is not named in this match's lineup"));
        lineup.setSubstitutedOffMinute(minute);
        return matchLineupRepository.save(lineup);
    }
}
