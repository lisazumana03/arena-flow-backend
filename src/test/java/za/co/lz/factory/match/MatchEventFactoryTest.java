package za.co.lz.factory.match;

import org.junit.jupiter.api.Test;
import za.co.lz.domain.match.*;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;
import za.co.lz.util.TestFixtures;

import static org.junit.jupiter.api.Assertions.*;

class MatchEventFactoryTest {

    private final Team home = TestFixtures.team("Home FC");
    private final Team away = TestFixtures.team("Away FC");
    private final Match match = TestFixtures.match(home, away);
    private final Player scorer = TestFixtures.player(home, "Sipho", "Nkosi", PlayerPosition.ST);
    private final Player assist = TestFixtures.player(home, "Thabo", "Mokoena", PlayerPosition.CAM);

    @Test
    void createGoal_setsScorerAndAssist() {
        MatchEvent event = MatchEventFactory.createGoal(match, home, scorer, assist, MatchEventType.GOAL, 23, null);

        assertEquals(MatchEventType.GOAL, event.getEventType());
        assertEquals(scorer, event.getPlayer());
        assertEquals(assist, event.getRelatedPlayer());
        assertEquals(23, event.getMinute());
        assertTrue(event.getEventType().scoresGoal());
    }

    @Test
    void createGoal_rejectsNonGoalEventType() {
        assertThrows(IllegalArgumentException.class, () ->
                MatchEventFactory.createGoal(match, home, scorer, null, MatchEventType.CORNER, 23, null));
    }

    @Test
    void createCard_redCardRequiresOffence() {
        MatchEvent event = MatchEventFactory.createCard(match, away, scorer, MatchEventType.RED_CARD,
                DisciplinaryOffence.VIOLENT_CONDUCT, 60, null);

        assertEquals(MatchEventType.RED_CARD, event.getEventType());
        assertEquals(DisciplinaryOffence.VIOLENT_CONDUCT, event.getOffence());
    }

    @Test
    void createCard_rejectsNonCardEventType() {
        assertThrows(IllegalArgumentException.class, () ->
                MatchEventFactory.createCard(match, away, scorer, MatchEventType.GOAL,
                        DisciplinaryOffence.VIOLENT_CONDUCT, 60, null));
    }

    @Test
    void createSubstitution_setsBothPlayers() {
        Player subOn = TestFixtures.player(home, "Lwazi", "Dube", PlayerPosition.ST);
        MatchEvent event = MatchEventFactory.createSubstitution(match, home, scorer, subOn, 75);

        assertEquals(MatchEventType.SUBSTITUTION, event.getEventType());
        assertEquals(scorer, event.getPlayer());
        assertEquals(subOn, event.getRelatedPlayer());
    }

    @Test
    void createCorner_hasNoPlayer() {
        MatchEvent event = MatchEventFactory.createCorner(match, home, 10);
        assertEquals(MatchEventType.CORNER, event.getEventType());
        assertNull(event.getPlayer());
    }

    @Test
    void createInjury_requiresSeverity() {
        MatchEvent event = MatchEventFactory.createInjury(match, home, scorer, InjurySeverity.MODERATE, 40, "twisted ankle");
        assertEquals(InjurySeverity.MODERATE, event.getInjurySeverity());
        assertEquals("twisted ankle", event.getNotes());
    }
}
