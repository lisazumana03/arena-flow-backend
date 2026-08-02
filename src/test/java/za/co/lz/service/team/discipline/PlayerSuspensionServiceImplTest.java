package za.co.lz.service.team.discipline;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.lz.domain.match.DisciplinaryOffence;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.discipline.PlayerSuspension;
import za.co.lz.repository.team.discipline.PlayerSuspensionRepository;
import za.co.lz.service.team.discipline.impl.PlayerSuspensionServiceImpl;
import za.co.lz.util.TestFixtures;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerSuspensionServiceImplTest {

    @Mock
    private PlayerSuspensionRepository playerSuspensionRepository;

    @InjectMocks
    private PlayerSuspensionServiceImpl playerSuspensionService;

    private final Team teamA = TestFixtures.team("Home FC");
    private final Team teamB = TestFixtures.team("Away FC");
    private final Player player = TestFixtures.player(teamA, "Sipho", "Nkosi", PlayerPosition.CB);
    private final Match match = TestFixtures.match(teamA, teamB);

    @Test
    void issueSuspension_violentConduct_bansThreeGames() {
        when(playerSuspensionRepository.save(any(PlayerSuspension.class))).thenAnswer(inv -> inv.getArgument(0));

        PlayerSuspension suspension = playerSuspensionService.issueSuspension(player, match, DisciplinaryOffence.VIOLENT_CONDUCT);

        assertEquals(3, suspension.getGamesBanned());
        assertTrue(suspension.isActive());
    }

    @Test
    void issueSuspension_secondBookableOffence_bansOneGame() {
        when(playerSuspensionRepository.save(any(PlayerSuspension.class))).thenAnswer(inv -> inv.getArgument(0));

        PlayerSuspension suspension = playerSuspensionService.issueSuspension(player, match, DisciplinaryOffence.SECOND_BOOKABLE_OFFENCE);

        assertEquals(1, suspension.getGamesBanned());
    }

    @Test
    void issueSuspension_committeeOverrideRespected() {
        when(playerSuspensionRepository.save(any(PlayerSuspension.class))).thenAnswer(inv -> inv.getArgument(0));

        PlayerSuspension suspension = playerSuspensionService.issueSuspension(player, match, DisciplinaryOffence.SERIOUS_FOUL_PLAY, 6);

        assertEquals(6, suspension.getGamesBanned());
    }

    @Test
    void serveGameForTeam_decrementsOnlyThatTeamsActiveSuspensions() {
        PlayerSuspension teamASuspension = new PlayerSuspension.Builder()
                .setSuspensionId(UUID.randomUUID())
                .setPlayer(player)
                .setTriggeringMatch(match)
                .setOffence(DisciplinaryOffence.VIOLENT_CONDUCT)
                .build(); // 3 games banned, 0 served

        Player otherTeamPlayer = TestFixtures.player(teamB, "Thabo", "Mokoena", PlayerPosition.ST);
        PlayerSuspension teamBSuspension = new PlayerSuspension.Builder()
                .setSuspensionId(UUID.randomUUID())
                .setPlayer(otherTeamPlayer)
                .setTriggeringMatch(match)
                .setOffence(DisciplinaryOffence.SECOND_BOOKABLE_OFFENCE)
                .build();

        when(playerSuspensionRepository.findByActiveTrue()).thenReturn(List.of(teamASuspension, teamBSuspension));
        when(playerSuspensionRepository.saveAll(any())).thenAnswer(inv -> inv.getArgument(0));

        List<PlayerSuspension> updated = playerSuspensionService.serveGameForTeam(teamA);

        assertEquals(1, updated.size());
        assertEquals(1, teamASuspension.getGamesServed());
        assertEquals(0, teamBSuspension.getGamesServed()); // untouched - different team played
    }

    @Test
    void isPlayerSuspended_reflectsActiveSuspensions() {
        when(playerSuspensionRepository.findByPlayer_PlayerIdAndActiveTrue(player.getPlayerId()))
                .thenReturn(List.of(mock(PlayerSuspension.class)));

        assertTrue(playerSuspensionService.isPlayerSuspended(player.getPlayerId()));
    }
}
