package za.co.lz.service.match.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.match.*;
import za.co.lz.domain.team.Team;
import za.co.lz.repository.match.MatchRepository;
import za.co.lz.repository.match.StandingRepository;
import za.co.lz.service.match.IMatchService;
import za.co.lz.service.team.impl.FinancialServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MatchServiceImpl implements IMatchService {
    
    @Autowired
    private MatchRepository matchRepository;
    
    @Autowired
    private StandingRepository standingRepository;
    
    @Autowired
    private FinancialServiceImpl financialService;
    
    @Autowired
    private StandingServiceImpl standingService;
    
    @Override
    public Match create(Match match) {
        return matchRepository.save(match);
    }
    
    @Override
    public List<Match> findAll() {
        return matchRepository.findAll();
    }
    
    @Override
    public Optional<Match> findById(UUID uuid) {
        return matchRepository.findById(uuid);
    }
    
    @Override
    public Match update(Match match, UUID uuid) {
        return matchRepository.save(match);
    }
    
    @Override
    public void delete(UUID uuid) {
        matchRepository.deleteById(uuid);
    }
    
    @Override
    public Match scheduleMatch(Team homeTeam, Team awayTeam, LocalDateTime matchDate, String venue) {
        if (homeTeam == null || awayTeam == null) {
            throw new IllegalArgumentException("Both teams are required");
        }
        if (homeTeam.getTeamId().equals(awayTeam.getTeamId())) {
            throw new IllegalArgumentException("Team cannot play against itself");
        }
        if (matchDate == null) {
            throw new IllegalArgumentException("Match date is required");
        }
        
        Match match = new Match.Builder()
                .setMatchId(UUID.randomUUID())
                .setHomeTeam(homeTeam)
                .setAwayTeam(awayTeam)
                .setMatchDate(matchDate)
                .setVenue(venue != null ? venue : "TBD")
                .setStatus(MatchStatus.SCHEDULED)
                .build();
        
        return matchRepository.save(match);
    }
    
    @Override
    public Match completeMatch(UUID matchId, int homeScore, int awayScore) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with ID: " + matchId));
        
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        match.setStatus(MatchStatus.COMPLETED);
        
        // Determine result
        MatchResult result;
        if (homeScore > awayScore) {
            result = MatchResult.HOME_WIN;
        } else if (awayScore > homeScore) {
            result = MatchResult.AWAY_WIN;
        } else {
            result = MatchResult.DRAW;
        }
        match.setResult(result);
        
        Match updated = matchRepository.save(match);
        
        // Update standings
        updateStandingsAfterMatch(matchId);
        
        // Record financial impact (if set)
        if (match.getHomeTeamRevenue() != null && match.getHomeTeamRevenue().compareTo(BigDecimal.ZERO) > 0) {
            recordMatchFinancials(matchId, 
                    match.getHomeTeamRevenue(), 
                    match.getAwayTeamRevenue(),
                    match.getHomeTeamExpenses(),
                    match.getAwayTeamExpenses());
        }
        
        return updated;
    }
    
    @Override
    public List<Match> findUpcomingMatches() {
        return matchRepository.findByStatus(MatchStatus.SCHEDULED);
    }
    
    @Override
    public List<Match> findCompletedMatches() {
        return matchRepository.findByStatus(MatchStatus.COMPLETED);
    }
    
    @Override
    public List<Match> findTeamMatches(Team team) {
        return matchRepository.findByHomeTeamOrAwayTeam(team, team);
    }
    
    @Override
    public List<Match> findSeasonMatches(UUID seasonId) {
        return matchRepository.findBySeason_SeasonId(seasonId);
    }
    
    @Override
    public Match recordMatchFinancials(UUID matchId, BigDecimal homeRevenue, BigDecimal awayRevenue, 
                                       BigDecimal homeExpenses, BigDecimal awayExpenses) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with ID: " + matchId));
        
        match.setHomeTeamRevenue(homeRevenue);
        match.setAwayTeamRevenue(awayRevenue);
        match.setHomeTeamExpenses(homeExpenses);
        match.setAwayTeamExpenses(awayExpenses);
        
        Match updated = matchRepository.save(match);
        
        // If match is completed, record in financials
        if (match.getStatus() == MatchStatus.COMPLETED) {
            recordFinancialEvents(match);
        }
        
        return updated;
    }
    
    @Override
    public void updateStandingsAfterMatch(UUID matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with ID: " + matchId));
        
        if (match.getStatus() != MatchStatus.COMPLETED || match.getResult() == null) {
            return;
        }
        
        // Update home team standing
        if (match.getSeason() != null) {
            Optional<Standing> homeStanding = standingRepository.findBySeason_SeasonIdAndTeam_TeamId(
                    match.getSeason().getSeasonId(), 
                    match.getHomeTeam().getTeamId()
            );
            
            if (homeStanding.isPresent()) {
                Standing hs = homeStanding.get();
                // Home team perspective: HOME_WIN means home team won
                MatchResult homeResult = match.getResult() == MatchResult.HOME_WIN ? MatchResult.HOME_WIN :
                                        (match.getResult() == MatchResult.AWAY_WIN ? MatchResult.AWAY_WIN : MatchResult.DRAW);
                hs.recordMatch(homeResult, match.getHomeScore(), match.getAwayScore());
                standingRepository.save(hs);
            }
            
            // Update away team standing
            Optional<Standing> awayStanding = standingRepository.findBySeason_SeasonIdAndTeam_TeamId(
                    match.getSeason().getSeasonId(),
                    match.getAwayTeam().getTeamId()
            );
            
            if (awayStanding.isPresent()) {
                Standing as = awayStanding.get();
                // Away team perspective: flip the result
                MatchResult awayResult = match.getResult() == MatchResult.AWAY_WIN ? MatchResult.HOME_WIN :
                                        (match.getResult() == MatchResult.HOME_WIN ? MatchResult.AWAY_WIN : MatchResult.DRAW);
                as.recordMatch(awayResult, match.getAwayScore(), match.getHomeScore());
                standingRepository.save(as);
            }
        }
    }
    
    private void recordFinancialEvents(Match match) {
        try {
            // Get latest financials for both teams
            var homeFinancials = financialService.findByTeam(match.getHomeTeam());
            var awayFinancials = financialService.findByTeam(match.getAwayTeam());
            
            // Record for home team
            if (!homeFinancials.isEmpty()) {
                var latest = homeFinancials.get(homeFinancials.size() - 1);
                BigDecimal homeNet = match.getHomeTeamRevenue().subtract(match.getHomeTeamExpenses());
                
                if (homeNet.compareTo(BigDecimal.ZERO) > 0) {
                    financialService.recordProfit(latest.getFinancialId());
                } else if (homeNet.compareTo(BigDecimal.ZERO) < 0) {
                    financialService.recordLoss(latest.getFinancialId());
                }
            }
            
            // Record for away team
            if (!awayFinancials.isEmpty()) {
                var latest = awayFinancials.get(awayFinancials.size() - 1);
                BigDecimal awayNet = match.getAwayTeamRevenue().subtract(match.getAwayTeamExpenses());
                
                if (awayNet.compareTo(BigDecimal.ZERO) > 0) {
                    financialService.recordProfit(latest.getFinancialId());
                } else if (awayNet.compareTo(BigDecimal.ZERO) < 0) {
                    financialService.recordLoss(latest.getFinancialId());
                }
            }
        } catch (Exception e) {
            // Log but don't fail match completion
            System.err.println("Failed to record financial events for match: " + e.getMessage());
        }
    }
}
