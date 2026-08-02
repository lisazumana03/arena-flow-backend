package za.co.lz.service.match;

import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.MatchLineup;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;
import za.co.lz.service.IService;

import java.util.List;
import java.util.UUID;

public interface IMatchLineupService extends IService<MatchLineup, UUID> {

    /**
     * Names a player in the squad for a match, either starting or on the bench.
     * Rejects the player if they are currently serving a suspension or carrying
     * an active injury - that's how a ban/injury actually stops a player playing.
     */
    MatchLineup namePlayer(Match match, Team team, Player player, boolean starting, int shirtNumber, PlayerPosition matchPosition);

    List<MatchLineup> getLineupForMatch(UUID matchId);

    List<MatchLineup> getLineupForMatchAndTeam(UUID matchId, UUID teamId);

    List<MatchLineup> getStartingXI(UUID matchId, UUID teamId);

    /** Marks a starting/bench player as having come off (used alongside a SUBSTITUTION MatchEvent). */
    MatchLineup substitutePlayerOff(UUID matchId, UUID playerId, int minute);
}
