package za.co.lz.controller.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.MatchStatus;
import za.co.lz.domain.team.Team;
import za.co.lz.service.match.impl.MatchServiceImpl;
import za.co.lz.service.team.impl.TeamServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/matches")
public class MatchController {
    
    @Autowired
    private MatchServiceImpl matchService;
    
    @Autowired
    private TeamServiceImpl teamService;
    
    @PostMapping("/schedule")
    public ResponseEntity<Match> scheduleMatch(@RequestBody MatchScheduleRequest request) {
        Team homeTeam = teamService.findById(request.getHomeTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Home team not found"));
        Team awayTeam = teamService.findById(request.getAwayTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Away team not found"));
        
        Match match = matchService.scheduleMatch(homeTeam, awayTeam, request.getMatchDate(), request.getVenue());
        return ResponseEntity.ok(match);
    }
    
    @PostMapping("/{matchId}/complete")
    public ResponseEntity<Match> completeMatch(
            @PathVariable UUID matchId,
            @RequestParam int homeScore,
            @RequestParam int awayScore) {
        Match match = matchService.completeMatch(matchId, homeScore, awayScore);
        return ResponseEntity.ok(match);
    }
    
    @GetMapping("/{matchId}")
    public ResponseEntity<Match> getMatch(@PathVariable UUID matchId) {
        Match match = matchService.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with ID: " + matchId));
        return ResponseEntity.ok(match);
    }
    
    @GetMapping("/upcoming")
    public ResponseEntity<List<Match>> getUpcomingMatches() {
        List<Match> matches = matchService.findUpcomingMatches();
        return ResponseEntity.ok(matches);
    }
    
    @GetMapping("/completed")
    public ResponseEntity<List<Match>> getCompletedMatches() {
        List<Match> matches = matchService.findCompletedMatches();
        return ResponseEntity.ok(matches);
    }
    
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<Match>> getTeamMatches(@PathVariable UUID teamId) {
        Team team = teamService.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));
        List<Match> matches = matchService.findTeamMatches(team);
        return ResponseEntity.ok(matches);
    }
    
    @GetMapping("/season/{seasonId}")
    public ResponseEntity<List<Match>> getSeasonMatches(@PathVariable UUID seasonId) {
        List<Match> matches = matchService.findSeasonMatches(seasonId);
        return ResponseEntity.ok(matches);
    }
    
    @PostMapping("/{matchId}/record-financials")
    public ResponseEntity<Match> recordFinancials(
            @PathVariable UUID matchId,
            @RequestBody MatchFinancialsRequest request) {
        Match match = matchService.recordMatchFinancials(matchId, 
                request.getHomeTeamRevenue(), 
                request.getAwayTeamRevenue(),
                request.getHomeTeamExpenses(),
                request.getAwayTeamExpenses());
        return ResponseEntity.ok(match);
    }
    
    @PostMapping("/{matchId}/update-standings")
    public ResponseEntity<Map<String, String>> updateStandings(@PathVariable UUID matchId) {
        matchService.updateStandingsAfterMatch(matchId);
        return ResponseEntity.ok(Map.of("message", "Standings updated for match " + matchId));
    }
    
    // Inner classes for API
    public static class MatchScheduleRequest {
        private UUID homeTeamId;
        private UUID awayTeamId;
        private LocalDateTime matchDate;
        private String venue;
        
        public UUID getHomeTeamId() { return homeTeamId; }
        public UUID getAwayTeamId() { return awayTeamId; }
        public LocalDateTime getMatchDate() { return matchDate; }
        public String getVenue() { return venue; }
    }
    
    public static class MatchFinancialsRequest {
        private BigDecimal homeTeamRevenue;
        private BigDecimal awayTeamRevenue;
        private BigDecimal homeTeamExpenses;
        private BigDecimal awayTeamExpenses;
        
        public BigDecimal getHomeTeamRevenue() { return homeTeamRevenue; }
        public BigDecimal getAwayTeamRevenue() { return awayTeamRevenue; }
        public BigDecimal getHomeTeamExpenses() { return homeTeamExpenses; }
        public BigDecimal getAwayTeamExpenses() { return awayTeamExpenses; }
    }
}
