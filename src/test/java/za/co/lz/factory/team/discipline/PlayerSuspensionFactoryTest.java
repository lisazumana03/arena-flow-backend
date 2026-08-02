package za.co.lz.factory.team.discipline;

import org.junit.jupiter.api.Test;
import za.co.lz.domain.match.DisciplinaryOffence;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.discipline.PlayerSuspension;
import za.co.lz.util.TestFixtures;

import static org.junit.jupiter.api.Assertions.*;

class PlayerSuspensionFactoryTest {

    @Test
    void createSuspension_usesOffenceDefaultBanLength() {
        Team team = TestFixtures.team("Home FC");
        Player player = TestFixtures.player(team, "Sipho", "Nkosi", PlayerPosition.CB);
        Match match = TestFixtures.match(team, TestFixtures.team("Away FC"));

        PlayerSuspension suspension = PlayerSuspensionFactory.createSuspension(player, match, DisciplinaryOffence.VIOLENT_CONDUCT);

        assertEquals(3, suspension.getGamesBanned());
        assertEquals(0, suspension.getGamesServed());
        assertTrue(suspension.isActive());
    }

    @Test
    void createSuspension_committeeOverrideLength() {
        Team team = TestFixtures.team("Home FC");
        Player player = TestFixtures.player(team, "Sipho", "Nkosi", PlayerPosition.CB);
        Match match = TestFixtures.match(team, TestFixtures.team("Away FC"));

        PlayerSuspension suspension = PlayerSuspensionFactory.createSuspension(player, match,
                DisciplinaryOffence.SERIOUS_FOUL_PLAY, 5);

        assertEquals(5, suspension.getGamesBanned());
    }
}
