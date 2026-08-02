package za.co.lz.factory.team.discipline;

import org.junit.jupiter.api.Test;
import za.co.lz.domain.match.InjurySeverity;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.discipline.PlayerInjury;
import za.co.lz.util.TestFixtures;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PlayerInjuryFactoryTest {

    @Test
    void createInjury_estimatesReturnDateFromSeverity() {
        Team team = TestFixtures.team("Home FC");
        Player player = TestFixtures.player(team, "Sipho", "Nkosi", PlayerPosition.CB);
        Match match = TestFixtures.match(team, TestFixtures.team("Away FC"));
        LocalDate today = LocalDate.of(2026, 7, 30);

        PlayerInjury injury = PlayerInjuryFactory.createInjury(player, match, InjurySeverity.MODERATE, today);

        assertEquals(today, injury.getInjuryDate());
        assertEquals(today.plusDays(InjurySeverity.MODERATE.getTypicalDaysOut()), injury.getExpectedReturnDate());
        assertTrue(injury.isActive());
    }

    @Test
    void createInjury_severeInjuryLongerThanMinor() {
        Team team = TestFixtures.team("Home FC");
        Player player = TestFixtures.player(team, "Sipho", "Nkosi", PlayerPosition.CB);
        Match match = TestFixtures.match(team, TestFixtures.team("Away FC"));
        LocalDate today = LocalDate.now();

        PlayerInjury minor = PlayerInjuryFactory.createInjury(player, match, InjurySeverity.MINOR, today);
        PlayerInjury severe = PlayerInjuryFactory.createInjury(player, match, InjurySeverity.SEVERE, today);

        assertTrue(severe.getExpectedReturnDate().isAfter(minor.getExpectedReturnDate()));
    }
}
