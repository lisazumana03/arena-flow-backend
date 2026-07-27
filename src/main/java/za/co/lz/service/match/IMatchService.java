package za.co.lz.service.match;

import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.MatchResult;
import za.co.lz.domain.team.Team;
import za.co.lz.service.IService;

import java.util.List;
import java.util.UUID;

public interface IMatchService extends IService<Match, UUID> {
    
    // Match operations
    Match scheduleMatch(Team homeTeam, Team awayTeam, java.time.LocalDateTime matchDate, String venue);
    Match completeMatch(UUID matchId, int homeScore, int awayScore);
    List<Match> findUpcomingMatches();
    List<Match> findCompletedMatches();
    List<Match> findTeamMatches(Team team);
    List<Match> findSeasonMatches(UUID seasonId);
    
    // Financial integration
    Match recordMatchFinancials(UUID matchId, java.math.BigDecimal homeRevenue, 
                               java.math.BigDecimal awayRevenue, 
                               java.math.BigDecimal homeExpenses, 
                               java.math.BigDecimal awayExpenses);
    
    // Standings update
    void updateStandingsAfterMatch(UUID matchId);
}
