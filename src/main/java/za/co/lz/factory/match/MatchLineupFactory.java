package za.co.lz.factory.match;

import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.MatchLineup;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;

import java.util.UUID;

public class MatchLineupFactory {
    public static MatchLineup createLineupEntry(Match match, Team team, Player player, boolean starting,
                                                  int shirtNumber, PlayerPosition matchPosition) {
        return new MatchLineup.Builder()
                .setLineupId(UUID.randomUUID())
                .setMatch(match)
                .setTeam(team)
                .setPlayer(player)
                .setStarting(starting)
                .setShirtNumber(shirtNumber)
                .setMatchPosition(matchPosition)
                .build();
    }
}
