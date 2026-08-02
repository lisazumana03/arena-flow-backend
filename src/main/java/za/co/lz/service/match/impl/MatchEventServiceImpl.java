package za.co.lz.service.match.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.match.*;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.tournament.QualificationStatus;
import za.co.lz.domain.tournament.Tournament;
import za.co.lz.domain.tournament.TournamentFormat;
import za.co.lz.domain.tournament.TournamentTeam;
import za.co.lz.factory.match.MatchEventFactory;
import za.co.lz.repository.match.MatchEventRepository;
import za.co.lz.repository.match.MatchRepository;
import za.co.lz.repository.team.PlayerRepository;
import za.co.lz.repository.team.TeamRepository;
import za.co.lz.service.match.IMatchEventService;
import za.co.lz.service.match.IMatchLineupService;
import za.co.lz.service.match.IMatchService;
import za.co.lz.service.team.discipline.IPlayerInjuryService;
import za.co.lz.service.team.discipline.IPlayerSuspensionService;
import za.co.lz.service.tournament.ITournamentTeamService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MatchEventServiceImpl implements IMatchEventService {

    // Simple knockout progression used to pick the winner's next stage. A production
    // system would carry an explicit "round" on Match/TournamentTeam so this doesn't
    // have to be inferred - this progression is a pragmatic default.
    private static final List<QualificationStatus> KNOCKOUT_PROGRESSION = List.of(
            QualificationStatus.ROUND_OF_16,
            QualificationStatus.QUARTERFINALIST,
            QualificationStatus.SEMIFINALIST
    );

    @Autowired
    private MatchEventRepository matchEventRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private IMatchService matchService;
    @Autowired
    private IMatchLineupService matchLineupService;
    @Autowired
    private IPlayerSuspensionService playerSuspensionService;
    @Autowired
    private IPlayerInjuryService playerInjuryService;
    @Autowired
    private ITournamentTeamService tournamentTeamService;

    @Override
    public MatchEvent create(MatchEvent matchEvent) {
        return matchEventRepository.save(matchEvent);
    }

    @Override
    public List<MatchEvent> findAll() {
        return matchEventRepository.findAll();
    }

    @Override
    public Optional<MatchEvent> findById(UUID uuid) {
        return matchEventRepository.findById(uuid);
    }

    @Override
    public MatchEvent update(MatchEvent matchEvent, UUID uuid) {
        return matchEventRepository.save(matchEvent);
    }

    @Override
    public void delete(UUID uuid) {
        matchEventRepository.deleteById(uuid);
    }

    @Override
    public MatchEvent recordGoal(UUID matchId, UUID scoringTeamId, UUID scorerId, UUID assistedById,
                                  MatchEventType goalType, int minute, Integer stoppageMinute) {
        Match match = getMatch(matchId);
        Team scoringTeam = getTeam(scoringTeamId);
        Player scorer = scorerId != null ? getPlayer(scorerId) : null;
        Player assist = assistedById != null ? getPlayer(assistedById) : null;

        MatchEvent event = MatchEventFactory.createGoal(match, scoringTeam, scorer, assist, goalType, minute, stoppageMinute);
        matchEventRepository.save(event);

        // An own goal is logged against the team whose player scored it, but the goal
        // is credited to the *opponent's* tally.
        boolean creditsHomeTeam = goalType == MatchEventType.OWN_GOAL
                ? !scoringTeamId.equals(match.getHomeTeam().getTeamId())
                : scoringTeamId.equals(match.getHomeTeam().getTeamId());

        if (creditsHomeTeam) {
            match.setHomeScore(match.getHomeScore() + 1);
        } else {
            match.setAwayScore(match.getAwayScore() + 1);
        }
        matchRepository.save(match);

        return event;
    }

    @Override
    public MatchEvent recordCard(UUID matchId, UUID teamId, UUID playerId, MatchEventType cardType,
                                  DisciplinaryOffence offence, int minute, Integer stoppageMinute) {
        Match match = getMatch(matchId);
        Team team = getTeam(teamId);
        Player player = getPlayer(playerId);

        MatchEvent event = MatchEventFactory.createCard(match, team, player, cardType, offence, minute, stoppageMinute);
        matchEventRepository.save(event);

        if (cardType == MatchEventType.RED_CARD || cardType == MatchEventType.SECOND_YELLOW_CARD) {
            // The number of games a player is banned for is decided by the offence
            // itself (see DisciplinaryOffence.defaultBanGames), not by a flat "red card = N games"
            // rule - a two-footed lunge (serious foul play) and violent conduct don't carry
            // the same tariff as a second yellow for persistent fouling.
            playerSuspensionService.issueSuspension(player, match, offence);
        }

        return event;
    }

    @Override
    public MatchEvent recordSubstitution(UUID matchId, UUID teamId, UUID playerOffId, UUID playerOnId, int minute) {
        Match match = getMatch(matchId);
        Team team = getTeam(teamId);
        Player playerOff = getPlayer(playerOffId);
        Player playerOn = getPlayer(playerOnId);

        MatchEvent event = MatchEventFactory.createSubstitution(match, team, playerOff, playerOn, minute);
        matchEventRepository.save(event);

        matchLineupService.substitutePlayerOff(matchId, playerOffId, minute);

        return event;
    }

    @Override
    public MatchEvent recordCorner(UUID matchId, UUID teamId, int minute) {
        MatchEvent event = MatchEventFactory.createCorner(getMatch(matchId), getTeam(teamId), minute);
        return matchEventRepository.save(event);
    }

    @Override
    public MatchEvent recordFreeKick(UUID matchId, UUID teamId, UUID playerId, int minute, String notes) {
        Player player = playerId != null ? getPlayer(playerId) : null;
        MatchEvent event = MatchEventFactory.createFreeKick(getMatch(matchId), getTeam(teamId), player, minute, notes);
        return matchEventRepository.save(event);
    }

    @Override
    public MatchEvent recordInjury(UUID matchId, UUID teamId, UUID playerId, InjurySeverity severity, int minute, String notes) {
        Match match = getMatch(matchId);
        Team team = getTeam(teamId);
        Player player = getPlayer(playerId);

        MatchEvent event = MatchEventFactory.createInjury(match, team, player, severity, minute, notes);
        matchEventRepository.save(event);

        // How long a player is out is estimated from the injury's clinical severity
        // (see InjurySeverity.typicalDaysOut) - medical staff can refine the return
        // date later via IPlayerInjuryService#updateExpectedReturn once properly assessed.
        playerInjuryService.reportInjury(player, match, severity, match.getMatchDate().toLocalDate());

        return event;
    }

    @Override
    public List<MatchEvent> getMatchEvents(UUID matchId) {
        return matchEventRepository.findByMatch_MatchIdOrderByMinuteAsc(matchId);
    }

    @Override
    public List<MatchEvent> getMatchEventsByType(UUID matchId, MatchEventType eventType) {
        return matchEventRepository.findByMatch_MatchIdAndEventType(matchId, eventType);
    }

    @Override
    public Match finalizeMatch(UUID matchId) {
        Match match = getMatch(matchId);

        // Sets status=COMPLETED, computes the MatchResult and updates Standing
        // (see MatchServiceImpl#updateStandingsAfterMatch).
        Match completed = matchService.completeMatch(matchId, match.getHomeScore(), match.getAwayScore());

        // Any active suspension held by a player at either club ticks down by one game
        // now that their team has played a fixture.
        playerSuspensionService.serveGameForTeam(completed.getHomeTeam());
        playerSuspensionService.serveGameForTeam(completed.getAwayTeam());

        if (completed.getSeason() != null && completed.getSeason().getTournament() != null) {
            applyTournamentConsequences(completed);
        }

        return completed;
    }

    private void applyTournamentConsequences(Match match) {
        UUID seasonId = match.getSeason().getSeasonId();
        Tournament tournament = match.getSeason().getTournament();
        TournamentFormat format = tournament.getFormat();

        TournamentTeam homeEntry = safeGetEntry(seasonId, match.getHomeTeam().getTeamId());
        TournamentTeam awayEntry = safeGetEntry(seasonId, match.getAwayTeam().getTeamId());
        if (homeEntry == null || awayEntry == null) {
            return; // teams not registered in this tournament edition - nothing to update
        }

        if (format == TournamentFormat.LEAGUE || format == TournamentFormat.HYBRID) {
            if (match.getResult() == MatchResult.HOME_WIN) {
                tournamentTeamService.addPoints(seasonId, homeEntry.getTeam().getTeamId(), 3);
            } else if (match.getResult() == MatchResult.AWAY_WIN) {
                tournamentTeamService.addPoints(seasonId, awayEntry.getTeam().getTeamId(), 3);
            } else if (match.getResult() == MatchResult.DRAW) {
                tournamentTeamService.addPoints(seasonId, homeEntry.getTeam().getTeamId(), 1);
                tournamentTeamService.addPoints(seasonId, awayEntry.getTeam().getTeamId(), 1);
            }
        }

        if (format == TournamentFormat.KNOCKOUT || format == TournamentFormat.HYBRID) {
            if (match.getResult() == MatchResult.HOME_WIN) {
                advanceBracket(seasonId, homeEntry, awayEntry);
            } else if (match.getResult() == MatchResult.AWAY_WIN) {
                advanceBracket(seasonId, awayEntry, homeEntry);
            }
            // A DRAW in a knockout leg needs extra time/penalties/away-goals to resolve -
            // that decision happens outside this service (e.g. a follow-up recordGoal
            // burst for a penalty shootout), so no bracket change is made on a draw.
        }
    }

    private void advanceBracket(UUID seasonId, TournamentTeam winner, TournamentTeam loser) {
        QualificationStatus loserStatus = loser.getQualificationStatus();

        if (loserStatus == QualificationStatus.SEMIFINALIST) {
            // This was the final.
            tournamentTeamService.updateQualificationStatus(seasonId, winner.getTeam().getTeamId(), QualificationStatus.CHAMPION);
            tournamentTeamService.updateQualificationStatus(seasonId, loser.getTeam().getTeamId(), QualificationStatus.RUNNER_UP);
            return;
        }

        QualificationStatus nextStage = nextKnockoutStage(winner.getQualificationStatus());
        tournamentTeamService.updateQualificationStatus(seasonId, winner.getTeam().getTeamId(), nextStage);
        tournamentTeamService.updateQualificationStatus(seasonId, loser.getTeam().getTeamId(), QualificationStatus.ELIMINATED);
    }

    private QualificationStatus nextKnockoutStage(QualificationStatus current) {
        int index = KNOCKOUT_PROGRESSION.indexOf(current);
        if (index < 0) {
            // Team hasn't entered the tracked knockout ladder yet (e.g. still REGISTERED
            // or GROUP_STAGE) - the next stage is the first tracked round.
            return KNOCKOUT_PROGRESSION.get(0);
        }
        if (index == KNOCKOUT_PROGRESSION.size() - 1) {
            // Already at the last tracked stage before the final; the SEMIFINALIST-vs-final
            // case is handled by the caller before this method is reached.
            return current;
        }
        return KNOCKOUT_PROGRESSION.get(index + 1);
    }

    private TournamentTeam safeGetEntry(UUID seasonId, UUID teamId) {
        try {
            return tournamentTeamService.getEntry(seasonId, teamId);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private Match getMatch(UUID matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with ID: " + matchId));
    }

    private Team getTeam(UUID teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));
    }

    private Player getPlayer(UUID playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
    }
}
