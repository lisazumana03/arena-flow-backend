package za.co.lz.domain.match;

import jakarta.persistence.*;
import za.co.lz.domain.team.Team;

import java.util.UUID;

/**
 * Represents a team's position in league standings.
 * Tracks wins, losses, draws, points, and ranking.
 */
@Entity
@Table(name = "standings")
public class Standing {
    
    @Id
    private UUID standingId;
    
    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;
    
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
    
    private int position; // 1st, 2nd, etc.
    private int gamesPlayed;
    private int wins;
    private int draws;
    private int losses;
    private int goalsFor;
    private int goalsAgainst;
    private int goalDifference;
    private int points; // 3 per win, 1 per draw
    
    public Standing() {}
    
    private Standing(Builder builder) {
        this.standingId = builder.standingId;
        this.season = builder.season;
        this.team = builder.team;
        this.position = builder.position;
        this.gamesPlayed = builder.gamesPlayed;
        this.wins = builder.wins;
        this.draws = builder.draws;
        this.losses = builder.losses;
        this.goalsFor = builder.goalsFor;
        this.goalsAgainst = builder.goalsAgainst;
        this.goalDifference = builder.goalDifference;
        this.points = builder.points;
    }
    
    // Getters
    public UUID getStandingId() { return standingId; }
    public Season getSeason() { return season; }
    public Team getTeam() { return team; }
    public int getPosition() { return position; }
    public int getGamesPlayed() { return gamesPlayed; }
    public int getWins() { return wins; }
    public int getDraws() { return draws; }
    public int getLosses() { return losses; }
    public int getGoalsFor() { return goalsFor; }
    public int getGoalsAgainst() { return goalsAgainst; }
    public int getGoalDifference() { return goalDifference; }
    public int getPoints() { return points; }
    
    // Setters
    public void setPosition(int position) { this.position = position; }
    public void setGamesPlayed(int gamesPlayed) { this.gamesPlayed = gamesPlayed; }
    public void setWins(int wins) { this.wins = wins; }
    public void setDraws(int draws) { this.draws = draws; }
    public void setLosses(int losses) { this.losses = losses; }
    public void setGoalsFor(int goalsFor) { this.goalsFor = goalsFor; }
    public void setGoalsAgainst(int goalsAgainst) { this.goalsAgainst = goalsAgainst; }
    public void setGoalDifference(int goalDifference) { this.goalDifference = goalDifference; }
    public void setPoints(int points) { this.points = points; }
    
    // Helper method to update standings after a match
    public void recordMatch(MatchResult result, int goalsFor, int goalsAgainst) {
        gamesPlayed++;
        this.goalsFor += goalsFor;
        this.goalsAgainst += goalsAgainst;
        this.goalDifference = this.goalsFor - this.goalsAgainst;
        
        if (result == MatchResult.HOME_WIN || result == MatchResult.AWAY_WIN) {
            wins++;
            points += 3;
        } else if (result == MatchResult.DRAW) {
            draws++;
            points += 1;
        } else {
            losses++;
        }
    }
    
    public String toString() {
        return "Standing{" +
                "position=" + position +
                ", team=" + (team != null ? team.getTeamName() : "null") +
                ", gamesPlayed=" + gamesPlayed +
                ", wins=" + wins +
                ", draws=" + draws +
                ", losses=" + losses +
                ", points=" + points +
                ", goalDifference=" + goalDifference +
                '}';
    }
    
    public static class Builder {
        private UUID standingId;
        private Season season;
        private Team team;
        private int position;
        private int gamesPlayed;
        private int wins;
        private int draws;
        private int losses;
        private int goalsFor;
        private int goalsAgainst;
        private int goalDifference;
        private int points;
        
        public Builder setStandingId(UUID standingId) {
            this.standingId = standingId;
            return this;
        }
        
        public Builder setSeason(Season season) {
            this.season = season;
            return this;
        }
        
        public Builder setTeam(Team team) {
            this.team = team;
            return this;
        }
        
        public Builder setPosition(int position) {
            this.position = position;
            return this;
        }
        
        public Builder setGamesPlayed(int gamesPlayed) {
            this.gamesPlayed = gamesPlayed;
            return this;
        }
        
        public Builder setWins(int wins) {
            this.wins = wins;
            return this;
        }
        
        public Builder setDraws(int draws) {
            this.draws = draws;
            return this;
        }
        
        public Builder setLosses(int losses) {
            this.losses = losses;
            return this;
        }
        
        public Builder setGoalsFor(int goalsFor) {
            this.goalsFor = goalsFor;
            return this;
        }
        
        public Builder setGoalsAgainst(int goalsAgainst) {
            this.goalsAgainst = goalsAgainst;
            return this;
        }
        
        public Builder setGoalDifference(int goalDifference) {
            this.goalDifference = goalDifference;
            return this;
        }
        
        public Builder setPoints(int points) {
            this.points = points;
            return this;
        }
        
        public Standing build() {
            if (standingId == null) {
                throw new IllegalStateException("Standing ID is required");
            }
            if (season == null) {
                throw new IllegalStateException("Season is required");
            }
            if (team == null) {
                throw new IllegalStateException("Team is required");
            }
            return new Standing(this);
        }
    }
}
