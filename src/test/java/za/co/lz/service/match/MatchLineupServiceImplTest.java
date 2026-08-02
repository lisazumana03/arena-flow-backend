package za.co.lz.service.match;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.MatchLineup;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;
import za.co.lz.factory.match.MatchLineupFactory;
import za.co.lz.repository.match.MatchLineupRepository;
import za.co.lz.service.match.impl.MatchLineupServiceImpl;
import za.co.lz.service.team.discipline.IPlayerInjuryService;
import za.co.lz.service.team.discipline.IPlayerSuspensionService;
import za.co.lz.util.TestFixtures;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchLineupServiceImplTest {

    @Mock
    private MatchLineupRepository matchLineupRepository;
    @Mock
    private IPlayerSuspensionService playerSuspensionService;
    @Mock
    private IPlayerInjuryService playerInjuryService;

    @InjectMocks
    private MatchLineupServiceImpl matchLineupService;

    private final Team team = TestFixtures.team("Home FC");
    private final Player player = TestFixtures.player(team, "Sipho", "Nkosi", PlayerPosition.ST);
    private final Match match = TestFixtures.match(team, TestFixtures.team("Away FC"));

    @Test
    void namePlayer_succeedsWhenEligible() {
        when(playerSuspensionService.isPlayerSuspended(player.getPlayerId())).thenReturn(false);
        when(playerInjuryService.isPlayerInjured(player.getPlayerId())).thenReturn(false);
        when(matchLineupRepository.save(any(MatchLineup.class))).thenAnswer(inv -> inv.getArgument(0));

        MatchLineup lineup = matchLineupService.namePlayer(match, team, player, true, 9, PlayerPosition.ST);

        assertTrue(lineup.isStarting());
        assertEquals(9, lineup.getShirtNumber());
    }

    @Test
    void namePlayer_rejectsSuspendedPlayer() {
        when(playerSuspensionService.isPlayerSuspended(player.getPlayerId())).thenReturn(true);

        assertThrows(IllegalStateException.class, () ->
                matchLineupService.namePlayer(match, team, player, true, 9, PlayerPosition.ST));

        verify(matchLineupRepository, never()).save(any());
    }

    @Test
    void namePlayer_rejectsInjuredPlayer() {
        when(playerSuspensionService.isPlayerSuspended(player.getPlayerId())).thenReturn(false);
        when(playerInjuryService.isPlayerInjured(player.getPlayerId())).thenReturn(true);

        assertThrows(IllegalStateException.class, () ->
                matchLineupService.namePlayer(match, team, player, true, 9, PlayerPosition.ST));

        verify(matchLineupRepository, never()).save(any());
    }

    @Test
    void substitutePlayerOff_setsMinuteOnExistingLineupEntry() {
        MatchLineup lineup = MatchLineupFactory.createLineupEntry(match, team, player, true, 9, PlayerPosition.ST);
        when(matchLineupRepository.findByMatch_MatchId(match.getMatchId())).thenReturn(List.of(lineup));
        when(matchLineupRepository.save(any(MatchLineup.class))).thenAnswer(inv -> inv.getArgument(0));

        MatchLineup result = matchLineupService.substitutePlayerOff(match.getMatchId(), player.getPlayerId(), 75);

        assertEquals(75, result.getSubstitutedOffMinute());
    }

    @Test
    void substitutePlayerOff_throwsWhenPlayerNotInLineup() {
        when(matchLineupRepository.findByMatch_MatchId(match.getMatchId())).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () ->
                matchLineupService.substitutePlayerOff(match.getMatchId(), player.getPlayerId(), 75));
    }
}
