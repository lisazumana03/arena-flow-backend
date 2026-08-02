package za.co.lz.domain.match;

import jakarta.persistence.*;
import za.co.lz.domain.team.Team;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a match between two teams.
 * Tracks match details, score, financials, and result.
 */
@Entity
@Table(name = "matches")
public class Match {
    
    @Id
    private UUID matchId;
    
    @ManyToOne
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;
    
    @ManyToOne
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;
    
    private int homeScore;
    private int awayScore;
    
    @Enumerated(EnumType.STRING)
    private MatchStatus status;
    
    @Enumerated(EnumType.STRING)
    private MatchResult result;
    
    private LocalDateTime matchDate;
    private String venue; // legacy free-text venue name, kept for backward compatibility

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private za.co.lz.domain.venue.Venue venueDetails; // structured venue (community ground / stadium)

    private int attendance;
    
    // Financial impact
    private BigDecimal homeTeamRevenue;  // Ticket sales + broadcast cut
    private BigDecimal awayTeamRevenue;
    private BigDecimal homeTeamExpenses;  // Staff, travel costs
    private BigDecimal awayTeamExpenses;
    
    // Season reference
    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;
    
    public Match() {}
    
    private Match(Builder builder) {
        this.matchId = builder.matchId;
        this.homeTeam = builder.homeTeam;
        this.awayTeam = builder.awayTeam;
        this.homeScore = builder.homeScore;
        this.awayScore = builder.awayScore;
        this.status = builder.status;
        this.result = builder.result;
        this.matchDate = builder.matchDate;
        this.venue = builder.venue;
        this.venueDetails = builder.venueDetails;
        this.attendance = builder.attendance;
        this.homeTeamRevenue = builder.homeTeamRevenue;
        this.awayTeamRevenue = builder.awayTeamRevenue;
        this.homeTeamExpenses = builder.homeTeamExpenses;
        this.awayTeamExpenses = builder.awayTeamExpenses;
        this.season = builder.season;
    }
    
    // Getters
    public UUID getMatchId() { return matchId; }
    public Team getHomeTeam() { return homeTeam; }
    public Team getAwayTeam() { return awayTeam; }
    public int getHomeScore() { return homeScore; }
    public int getAwayScore() { return awayScore; }
    public MatchStatus getStatus() { return status; }
    public MatchResult getResult() { return result; }
    public LocalDateTime getMatchDate() { return matchDate; }
    public String getVenue() { return venue; }
    public za.co.lz.domain.venue.Venue getVenueDetails() { return venueDetails; }
    public int getAttendance() { return attendance; }
    public BigDecimal getHomeTeamRevenue() { return homeTeamRevenue; }
    public BigDecimal getAwayTeamRevenue() { return awayTeamRevenue; }
    public BigDecimal getHomeTeamExpenses() { return homeTeamExpenses; }
    public BigDecimal getAwayTeamExpenses() { return awayTeamExpenses; }
    public Season getSeason() { return season; }
    
    // Setters
    public void setStatus(MatchStatus status) { this.status = status; }
    public void setResult(MatchResult result) { this.result = result; }
    public void setHomeScore(int homeScore) { this.homeScore = homeScore; }
    public void setAwayScore(int awayScore) { this.awayScore = awayScore; }
    public void setAttendance(int attendance) { this.attendance = attendance; }
    public void setVenueDetails(za.co.lz.domain.venue.Venue venueDetails) { this.venueDetails = venueDetails; }
    public void setHomeTeamRevenue(BigDecimal revenue) { this.homeTeamRevenue = revenue; }
    public void setAwayTeamRevenue(BigDecimal revenue) { this.awayTeamRevenue = revenue; }
    public void setHomeTeamExpenses(BigDecimal expenses) { this.homeTeamExpenses = expenses; }
    public void setAwayTeamExpenses(BigDecimal expenses) { this.awayTeamExpenses = expenses; }
    
    public String toString() {
        return "Match{" +
                "matchId=" + matchId +
                ", homeTeam=" + (homeTeam != null ? homeTeam.getTeamName() : "null") +
                ", awayTeam=" + (awayTeam != null ? awayTeam.getTeamName() : "null") +
                ", homeScore=" + homeScore +
                ", awayScore=" + awayScore +
                ", status=" + status +
                ", result=" + result +
                ", matchDate=" + matchDate +
                '}';
    }
    
    public static class Builder {
        private UUID matchId;
        private Team homeTeam;
        private Team awayTeam;
        private int homeScore;
        private int awayScore;
        private MatchStatus status;
        private MatchResult result;
        private LocalDateTime matchDate;
        private String venue;
        private za.co.lz.domain.venue.Venue venueDetails;
        private int attendance;
        private BigDecimal homeTeamRevenue;
        private BigDecimal awayTeamRevenue;
        private BigDecimal homeTeamExpenses;
        private BigDecimal awayTeamExpenses;
        private Season season;
        
        public Builder setMatchId(UUID matchId) {
            this.matchId = matchId;
            return this;
        }
        
        public Builder setHomeTeam(Team homeTeam) {
            this.homeTeam = homeTeam;
            return this;
        }
        
        public Builder setAwayTeam(Team awayTeam) {
            this.awayTeam = awayTeam;
            return this;
        }
        
        public Builder setHomeScore(int homeScore) {
            this.homeScore = homeScore;
            return this;
        }
        
        public Builder setAwayScore(int awayScore) {
            this.awayScore = awayScore;
            return this;
        }
        
        public Builder setStatus(MatchStatus status) {
            this.status = status;
            return this;
        }
        
        public Builder setResult(MatchResult result) {
            this.result = result;
            return this;
        }
        
        public Builder setMatchDate(LocalDateTime matchDate) {
            this.matchDate = matchDate;
            return this;
        }
        
        public Builder setVenue(String venue) {
            this.venue = venue;
            return this;
        }
        
        public Builder setVenueDetails(za.co.lz.domain.venue.Venue venueDetails) {
            this.venueDetails = venueDetails;
            return this;
        }
        
        public Builder setAttendance(int attendance) {
            this.attendance = attendance;
            return this;
        }
        
        public Builder setHomeTeamRevenue(BigDecimal homeTeamRevenue) {
            this.homeTeamRevenue = homeTeamRevenue;
            return this;
        }
        
        public Builder setAwayTeamRevenue(BigDecimal awayTeamRevenue) {
            this.awayTeamRevenue = awayTeamRevenue;
            return this;
        }
        
        public Builder setHomeTeamExpenses(BigDecimal homeTeamExpenses) {
            this.homeTeamExpenses = homeTeamExpenses;
            return this;
        }
        
        public Builder setAwayTeamExpenses(BigDecimal awayTeamExpenses) {
            this.awayTeamExpenses = awayTeamExpenses;
            return this;
        }
        
        public Builder setSeason(Season season) {
            this.season = season;
            return this;
        }
        
        public Match build() {
            if (matchId == null) {
                throw new IllegalStateException("Match ID is required");
            }
            if (homeTeam == null) {
                throw new IllegalStateException("Home team is required");
            }
            if (awayTeam == null) {
                throw new IllegalStateException("Away team is required");
            }
            if (matchDate == null) {
                throw new IllegalStateException("Match date is required");
            }
            if (status == null) {
                status = MatchStatus.SCHEDULED;
            }
            if (homeTeamRevenue == null) {
                homeTeamRevenue = BigDecimal.ZERO;
            }
            if (awayTeamRevenue == null) {
                awayTeamRevenue = BigDecimal.ZERO;
            }
            if (homeTeamExpenses == null) {
                homeTeamExpenses = BigDecimal.ZERO;
            }
            if (awayTeamExpenses == null) {
                awayTeamExpenses = BigDecimal.ZERO;
            }
            return new Match(this);
        }
    }
}
