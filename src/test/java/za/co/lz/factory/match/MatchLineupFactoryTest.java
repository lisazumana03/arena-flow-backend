package za.co.lz.factory.match;

import org.junit.jupiter.api.Test;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.MatchLineup;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;
import za.co.lz.util.TestFixtures;

import static org.junit.jupiter.api.Assertions.*;

class MatchLineupFactoryTest {

    @Test
    void createLineupEntry_startingPlayer() {
        Team home = TestFixtures.team("Home FC");
        Team away = TestFixtures.team("Away FC");
        Match match = TestFixtures.match(home, away);
        Player player = TestFixtures.player(home, "Sipho", "Nkosi", PlayerPosition.ST);

        MatchLineup lineup = MatchLineupFactory.createLineupEntry(match, home, player, true, 9, PlayerPosition.ST);

        assertNotNull(lineup.getLineupId());
        assertTrue(lineup.isStarting());
        assertEquals(9, lineup.getShirtNumber());
        assertEquals(PlayerPosition.ST, lineup.getMatchPosition());
        assertNull(lineup.getSubstitutedOffMinute());
    }

    @Test
    void createLineupEntry_requiresPositiveShirtNumber() {
        Team home = TestFixtures.team("Home FC");
        Match match = TestFixtures.match(home, TestFixtures.team("Away FC"));
        Player player = TestFixtures.player(home, "Sipho", "Nkosi", PlayerPosition.ST);

        assertThrows(IllegalStateException.class, () ->
                MatchLineupFactory.createLineupEntry(match, home, player, false, 0, PlayerPosition.ST));
    }
}
