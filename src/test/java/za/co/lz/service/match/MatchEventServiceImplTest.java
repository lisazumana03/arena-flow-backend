package za.co.lz.service.match;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.lz.domain.match.*;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.tournament.QualificationStatus;
import za.co.lz.domain.tournament.Tournament;
import za.co.lz.domain.tournament.TournamentFormat;
import za.co.lz.domain.tournament.TournamentTeam;
import za.co.lz.repository.match.MatchEventRepository;
import za.co.lz.repository.match.MatchRepository;
import za.co.lz.repository.team.PlayerRepository;
import za.co.lz.repository.team.TeamRepository;
import za.co.lz.service.match.impl.MatchEventServiceImpl;
import za.co.lz.service.team.discipline.IPlayerInjuryService;
import za.co.lz.service.team.discipline.IPlayerSuspensionService;
import za.co.lz.service.tournament.ITournamentTeamService;
import za.co.lz.util.TestFixtures;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchEventServiceImplTest {

    @Mock private MatchEventRepository matchEventRepository;
    @Mock private MatchRepository matchRepository;
    @Mock private PlayerRepository playerRepository;
    @Mock private TeamRepository teamRepository;
    @Mock private IMatchService matchService;
    @Mock private IMatchLineupService matchLineupService;
    @Mock private IPlayerSuspensionService playerSuspensionService;
    @Mock private IPlayerInjuryService playerInjuryService;
    @Mock private ITournamentTeamService tournamentTeamService;

    @InjectMocks
    private MatchEventServiceImpl matchEventService;

    private final Team home = TestFixtures.team("Home FC");
    private final Team away = TestFixtures.team("Away FC");
    private final Player homeScorer = TestFixtures.player(home, "Sipho", "Nkosi", PlayerPosition.ST);
    private final Match match = TestFixtures.match(home, away);

    private void stubLookups() {
        when(matchRepository.findById(match.getMatchId())).thenReturn(Optional.of(match));
        when(teamRepository.findById(home.getTeamId())).thenReturn(Optional.of(home));
        when(teamRepository.findById(away.getTeamId())).thenReturn(Optional.of(away));
        when(playerRepository.findById(homeScorer.getPlayerId())).thenReturn(Optional.of(homeScorer));
        when(matchRepository.save(any(Match.class))).thenAnswer(inv -> inv.getArgument(0));
        when(matchEventRepository.save(any(MatchEvent.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void recordGoal_homeTeamScoring_incrementsHomeScore() {
        stubLookups();

        matchEventService.recordGoal(match.getMatchId(), home.getTeamId(), homeScorer.getPlayerId(), null,
                MatchEventType.GOAL, 23, null);

        assertEquals(1, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    @Test
    void recordGoal_awayTeamScoring_incrementsAwayScore() {
        stubLookups();
        Player awayScorer = TestFixtures.player(away, "Lwazi", "Dube", PlayerPosition.ST);
        when(playerRepository.findById(awayScorer.getPlayerId())).thenReturn(Optional.of(awayScorer));

        matchEventService.recordGoal(match.getMatchId(), away.getTeamId(), awayScorer.getPlayerId(), null,
                MatchEventType.GOAL, 40, null);

        assertEquals(0, match.getHomeScore());
        assertEquals(1, match.getAwayScore());
    }

    @Test
    void recordGoal_ownGoalByHomePlayer_creditsAwayTeam() {
        stubLookups();

        matchEventService.recordGoal(match.getMatchId(), home.getTeamId(), homeScorer.getPlayerId(), null,
                MatchEventType.OWN_GOAL, 55, null);

        assertEquals(0, match.getHomeScore());
        assertEquals(1, match.getAwayScore());
    }

    @Test
    void recordCard_redCard_issuesSuspensionUsingOffenceDefault() {
        stubLookups();

        matchEventService.recordCard(match.getMatchId(), home.getTeamId(), homeScorer.getPlayerId(),
                MatchEventType.RED_CARD, DisciplinaryOffence.VIOLENT_CONDUCT, 60, null);

        verify(playerSuspensionService).issueSuspension(homeScorer, match, DisciplinaryOffence.VIOLENT_CONDUCT);
    }

    @Test
    void recordCard_yellowCard_doesNotIssueSuspension() {
        stubLookups();

        matchEventService.recordCard(match.getMatchId(), home.getTeamId(), homeScorer.getPlayerId(),
                MatchEventType.YELLOW_CARD, null, 30, null);

        verify(playerSuspensionService, never()).issueSuspension(any(), any(), any());
    }

    @Test
    void recordInjury_opensPlayerInjuryRecord() {
        stubLookups();

        matchEventService.recordInjury(match.getMatchId(), home.getTeamId(), homeScorer.getPlayerId(),
                InjurySeverity.SEVERE, 12, "suspected ankle fracture");

        verify(playerInjuryService).reportInjury(homeScorer, match, InjurySeverity.SEVERE, match.getMatchDate().toLocalDate());
    }

    @Test
    void finalizeMatch_alwaysServesSuspensionsForBothTeams() {
        when(matchRepository.findById(match.getMatchId())).thenReturn(Optional.of(match));
        when(matchService.completeMatch(match.getMatchId(), match.getHomeScore(), match.getAwayScore())).thenReturn(match);

        matchEventService.finalizeMatch(match.getMatchId());

        verify(playerSuspensionService).serveGameForTeam(home);
        verify(playerSuspensionService).serveGameForTeam(away);
    }

    @Test
    void finalizeMatch_leagueFormat_addsThreePointsToWinner() {
        Match leagueMatch = leagueMatchWithResult(MatchResult.HOME_WIN);
        when(matchRepository.findById(leagueMatch.getMatchId())).thenReturn(Optional.of(leagueMatch));
        when(matchService.completeMatch(eq(leagueMatch.getMatchId()), anyInt(), anyInt())).thenReturn(leagueMatch);

        TournamentTeam homeEntry = tournamentTeamEntry(leagueMatch, home, QualificationStatus.REGISTERED);
        TournamentTeam awayEntry = tournamentTeamEntry(leagueMatch, away, QualificationStatus.REGISTERED);
        when(tournamentTeamService.getEntry(leagueMatch.getSeason().getSeasonId(), home.getTeamId())).thenReturn(homeEntry);
        when(tournamentTeamService.getEntry(leagueMatch.getSeason().getSeasonId(), away.getTeamId())).thenReturn(awayEntry);

        matchEventService.finalizeMatch(leagueMatch.getMatchId());

        verify(tournamentTeamService).addPoints(leagueMatch.getSeason().getSeasonId(), home.getTeamId(), 3);
        verify(tournamentTeamService, never()).addPoints(eq(leagueMatch.getSeason().getSeasonId()), eq(away.getTeamId()), anyInt());
    }

    @Test
    void finalizeMatch_knockoutFormat_advancesWinnerAndEliminatesLoser() {
        Match knockoutMatch = knockoutMatchWithResult(MatchResult.HOME_WIN);
        when(matchRepository.findById(knockoutMatch.getMatchId())).thenReturn(Optional.of(knockoutMatch));
        when(matchService.completeMatch(eq(knockoutMatch.getMatchId()), anyInt(), anyInt())).thenReturn(knockoutMatch);

        TournamentTeam homeEntry = tournamentTeamEntry(knockoutMatch, home, QualificationStatus.REGISTERED);
        TournamentTeam awayEntry = tournamentTeamEntry(knockoutMatch, away, QualificationStatus.REGISTERED);
        when(tournamentTeamService.getEntry(knockoutMatch.getSeason().getSeasonId(), home.getTeamId())).thenReturn(homeEntry);
        when(tournamentTeamService.getEntry(knockoutMatch.getSeason().getSeasonId(), away.getTeamId())).thenReturn(awayEntry);

        matchEventService.finalizeMatch(knockoutMatch.getMatchId());

        verify(tournamentTeamService).updateQualificationStatus(knockoutMatch.getSeason().getSeasonId(), home.getTeamId(), QualificationStatus.ROUND_OF_16);
        verify(tournamentTeamService).updateQualificationStatus(knockoutMatch.getSeason().getSeasonId(), away.getTeamId(), QualificationStatus.ELIMINATED);
    }

    @Test
    void finalizeMatch_knockoutFinal_crownsChampionAndRunnerUp() {
        Match finalMatch = knockoutMatchWithResult(MatchResult.HOME_WIN);
        when(matchRepository.findById(finalMatch.getMatchId())).thenReturn(Optional.of(finalMatch));
        when(matchService.completeMatch(eq(finalMatch.getMatchId()), anyInt(), anyInt())).thenReturn(finalMatch);

        TournamentTeam homeEntry = tournamentTeamEntry(finalMatch, home, QualificationStatus.SEMIFINALIST);
        TournamentTeam awayEntry = tournamentTeamEntry(finalMatch, away, QualificationStatus.SEMIFINALIST);
        when(tournamentTeamService.getEntry(finalMatch.getSeason().getSeasonId(), home.getTeamId())).thenReturn(homeEntry);
        when(tournamentTeamService.getEntry(finalMatch.getSeason().getSeasonId(), away.getTeamId())).thenReturn(awayEntry);

        matchEventService.finalizeMatch(finalMatch.getMatchId());

        verify(tournamentTeamService).updateQualificationStatus(finalMatch.getSeason().getSeasonId(), home.getTeamId(), QualificationStatus.CHAMPION);
        verify(tournamentTeamService).updateQualificationStatus(finalMatch.getSeason().getSeasonId(), away.getTeamId(), QualificationStatus.RUNNER_UP);
    }

    // --- fixture builders -------------------------------------------------

    private Match leagueMatchWithResult(MatchResult result) {
        return matchWithSeasonAndResult(TournamentFormat.LEAGUE, result);
    }

    private Match knockoutMatchWithResult(MatchResult result) {
        return matchWithSeasonAndResult(TournamentFormat.KNOCKOUT, result);
    }

    private Match matchWithSeasonAndResult(TournamentFormat format, MatchResult result) {
        Tournament tournament = new Tournament.Builder()
                .setTournamentId(UUID.randomUUID())
                .setTournamentName("Test Cup")
                .setFormat(format)
                .build();
        Season season = new Season.Builder()
                .setSeasonId(UUID.randomUUID())
                .setTournament(tournament)
                .setSeasonName("Test Cup 2026")
                .setStartDate(LocalDate.of(2026, 1, 1))
                .setEndDate(LocalDate.of(2026, 12, 1))
                .build();
        Match m = new Match.Builder()
                .setMatchId(UUID.randomUUID())
                .setHomeTeam(home)
                .setAwayTeam(away)
                .setMatchDate(LocalDateTime.now())
                .setVenue("TBD")
                .setStatus(MatchStatus.COMPLETED)
                .setSeason(season)
                .setHomeScore(result == MatchResult.HOME_WIN ? 2 : 0)
                .setAwayScore(result == MatchResult.AWAY_WIN ? 2 : 0)
                .build();
        m.setResult(result);
        return m;
    }

    private TournamentTeam tournamentTeamEntry(Match m, Team team, QualificationStatus status) {
        return new TournamentTeam.Builder()
                .setTournamentTeamId(UUID.randomUUID())
                .setSeason(m.getSeason())
                .setTeam(team)
                .setRegistrationDate(LocalDate.of(2026, 1, 1))
                .setQualificationStatus(status)
                .build();
    }
}
