package za.co.lz.service.match;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.Officials;
import za.co.lz.domain.team.Team;
import za.co.lz.repository.match.OfficialsRepository;
import za.co.lz.service.match.impl.OfficialsServiceImpl;
import za.co.lz.util.TestFixtures;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfficialsServiceImplTest {

    @Mock
    private OfficialsRepository officialsRepository;

    @InjectMocks
    private OfficialsServiceImpl officialsService;

    private final Team home = TestFixtures.team("Home FC");
    private final Team away = TestFixtures.team("Away FC");
    private final Match match = TestFixtures.match(home, away);

    @Test
    void assignOfficials_savesWhenNoneExistYet() {
        when(officialsRepository.findByMatch_MatchId(match.getMatchId())).thenReturn(Optional.empty());
        when(officialsRepository.save(any(Officials.class))).thenAnswer(inv -> inv.getArgument(0));

        Officials officials = officialsService.assignOfficials(match, "R. Ref", "A1", "A2", "F1", "Commissioner");

        assertEquals("R. Ref", officials.getReferee());
        assertEquals(match, officials.getMatch());
        verify(officialsRepository).save(any(Officials.class));
    }

    @Test
    void assignOfficials_rejectsSecondAssignmentToSameMatch() {
        Officials existing = new Officials.Builder()
                .setOfficialsId(java.util.UUID.randomUUID())
                .setMatch(match)
                .setReferee("Existing Ref")
                .build();
        when(officialsRepository.findByMatch_MatchId(match.getMatchId())).thenReturn(Optional.of(existing));

        assertThrows(IllegalStateException.class, () ->
                officialsService.assignOfficials(match, "New Ref", null, null, null, null));

        verify(officialsRepository, never()).save(any(Officials.class));
    }

    @Test
    void getByMatch_delegatesToRepository() {
        Officials officials = new Officials.Builder()
                .setOfficialsId(java.util.UUID.randomUUID())
                .setMatch(match)
                .setReferee("R. Ref")
                .build();
        when(officialsRepository.findByMatch_MatchId(match.getMatchId())).thenReturn(Optional.of(officials));

        Optional<Officials> result = officialsService.getByMatch(match.getMatchId());

        assertTrue(result.isPresent());
        assertEquals("R. Ref", result.get().getReferee());
    }
}
