package za.co.lz.service.match;

import za.co.lz.domain.match.*;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.Team;
import za.co.lz.service.IService;

import java.util.List;
import java.util.UUID;

/**
 * Records everything that happens during a match and cascades the
 * consequences: goals move the scoreline immediately, red cards create a
 * suspension, injuries create an injury record, and finalising a completed
 * match updates the league table (Standing/TournamentTeam points) or, for a
 * knockout fixture, advances the winner and eliminates the loser.
 */
public interface IMatchEventService extends IService<MatchEvent, UUID> {

    /** Records a goal and immediately updates Match.homeScore/awayScore. */
    MatchEvent recordGoal(UUID matchId, UUID scoringTeamId, UUID scorerId, UUID assistedById,
                          MatchEventType goalType, int minute, Integer stoppageMinute);

    /**
     * Records a caution/dismissal. For RED_CARD and SECOND_YELLOW_CARD this also
     * automatically issues a PlayerSuspension, using the offence's default ban length.
     */
    MatchEvent recordCard(UUID matchId, UUID teamId, UUID playerId, MatchEventType cardType,
                          DisciplinaryOffence offence, int minute, Integer stoppageMinute);

    /** Records a substitution and marks the outgoing player's lineup entry accordingly. */
    MatchEvent recordSubstitution(UUID matchId, UUID teamId, UUID playerOffId, UUID playerOnId, int minute);

    MatchEvent recordCorner(UUID matchId, UUID teamId, int minute);

    MatchEvent recordFreeKick(UUID matchId, UUID teamId, UUID playerId, int minute, String notes);

    /** Records an in-match injury and automatically opens a PlayerInjury with an estimated return date. */
    MatchEvent recordInjury(UUID matchId, UUID teamId, UUID playerId, InjurySeverity severity, int minute, String notes);

    List<MatchEvent> getMatchEvents(UUID matchId);

    List<MatchEvent> getMatchEventsByType(UUID matchId, MatchEventType eventType);

    /**
     * Ends the match: sets its final status/result via IMatchService (which also
     * updates Standing), ticks down both teams' active suspensions by one game,
     * and - if the match is part of a tournament - updates TournamentTeam points
     * (LEAGUE/HYBRID) and/or advances the knockout bracket (KNOCKOUT/HYBRID).
     */
    Match finalizeMatch(UUID matchId);
}
