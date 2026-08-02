package za.co.lz.factory.match;

import org.junit.jupiter.api.Test;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.Officials;
import za.co.lz.domain.team.Team;
import za.co.lz.util.TestFixtures;

import static org.junit.jupiter.api.Assertions.*;

class OfficialsFactoryTest {

    @Test
    void createOfficials_assignsIdAndFields() {
        Team home = TestFixtures.team("Home FC");
        Team away = TestFixtures.team("Away FC");
        Match match = TestFixtures.match(home, away);

        Officials officials = OfficialsFactory.createOfficials(match, "R. Referee", "A. One", "A. Two",
                "F. Official", "C. Commissioner");

        assertNotNull(officials.getOfficialsId());
        assertEquals(match, officials.getMatch());
        assertEquals("R. Referee", officials.getReferee());
        assertEquals("A. One", officials.getAssistantReferee1());
        assertEquals("A. Two", officials.getAssistantReferee2());
        assertEquals("F. Official", officials.getFourthOfficial());
        assertEquals("C. Commissioner", officials.getMatchCommissioner());
    }

    @Test
    void createOfficials_requiresReferee() {
        Team home = TestFixtures.team("Home FC");
        Team away = TestFixtures.team("Away FC");
        Match match = TestFixtures.match(home, away);

        assertThrows(IllegalStateException.class, () ->
                OfficialsFactory.createOfficials(match, null, null, null, null, null));
    }
}
