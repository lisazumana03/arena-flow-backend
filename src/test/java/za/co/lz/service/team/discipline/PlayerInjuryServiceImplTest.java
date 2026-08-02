package za.co.lz.service.team.discipline;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.lz.domain.match.InjurySeverity;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.discipline.PlayerInjury;
import za.co.lz.repository.team.discipline.PlayerInjuryRepository;
import za.co.lz.service.team.discipline.impl.PlayerInjuryServiceImpl;
import za.co.lz.util.TestFixtures;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerInjuryServiceImplTest {

    @Mock
    private PlayerInjuryRepository playerInjuryRepository;

    @InjectMocks
    private PlayerInjuryServiceImpl playerInjuryService;

    private final Team team = TestFixtures.team("Home FC");
    private final Player player = TestFixtures.player(team, "Sipho", "Nkosi", PlayerPosition.CB);
    private final Match match = TestFixtures.match(team, TestFixtures.team("Away FC"));

    @Test
    void reportInjury_moderateSeverity_estimatesFourWeekReturn() {
        when(playerInjuryRepository.save(any(PlayerInjury.class))).thenAnswer(inv -> inv.getArgument(0));
        LocalDate injuryDate = LocalDate.of(2026, 7, 1);

        PlayerInjury injury = playerInjuryService.reportInjury(player, match, InjurySeverity.MODERATE, injuryDate);

        assertEquals(injuryDate.plusDays(28), injury.getExpectedReturnDate());
        assertTrue(injury.isActive());
    }

    @Test
    void updateExpectedReturn_overridesEstimate() {
        UUID injuryId = UUID.randomUUID();
        PlayerInjury injury = new PlayerInjury.Builder()
                .setInjuryId(injuryId)
                .setPlayer(player)
                .setTriggeringMatch(match)
                .setSeverity(InjurySeverity.SEVERE)
                .setInjuryDate(LocalDate.of(2026, 7, 1))
                .build();
        when(playerInjuryRepository.findById(injuryId)).thenReturn(Optional.of(injury));
        when(playerInjuryRepository.save(any(PlayerInjury.class))).thenAnswer(inv -> inv.getArgument(0));

        LocalDate revised = LocalDate.of(2026, 9, 1);
        PlayerInjury updated = playerInjuryService.updateExpectedReturn(injuryId, revised);

        assertEquals(revised, updated.getExpectedReturnDate());
    }

    @Test
    void markRecovered_setsInactive() {
        UUID injuryId = UUID.randomUUID();
        PlayerInjury injury = new PlayerInjury.Builder()
                .setInjuryId(injuryId)
                .setPlayer(player)
                .setSeverity(InjurySeverity.MINOR)
                .build();
        when(playerInjuryRepository.findById(injuryId)).thenReturn(Optional.of(injury));
        when(playerInjuryRepository.save(any(PlayerInjury.class))).thenAnswer(inv -> inv.getArgument(0));

        PlayerInjury result = playerInjuryService.markRecovered(injuryId);

        assertFalse(result.isActive());
    }

    @Test
    void isPlayerInjured_reflectsActiveInjuries() {
        when(playerInjuryRepository.findByPlayer_PlayerIdAndActiveTrue(player.getPlayerId()))
                .thenReturn(List.of(mock(PlayerInjury.class)));

        assertTrue(playerInjuryService.isPlayerInjured(player.getPlayerId()));
    }
}
