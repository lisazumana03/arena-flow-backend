package za.co.lz.domain.match;

import jakarta.persistence.*;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.tournament.Tournament;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Represents one yearly edition of a {@link Tournament} (e.g. "Premier League 2027").
 * Contains all matches played in that edition and final standings.
 */
@Entity
@Table(name = "seasons")
public class Season {
    
    @Id
    private UUID seasonId;

    // The competition this is an edition of (e.g. Premier League). Nullable
    // for backward compatibility with seasons created before Tournament existed.
    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;
    
    private int year; // e.g., 2024
    private String seasonName; // e.g., "2024-2025"
    
    private LocalDate startDate;
    private LocalDate endDate;
    
    @Enumerated(EnumType.STRING)
    private SeasonStatus status;
    
    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<Match> matches;
    
    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<Standing> standings;
    
    public Season() {}
    
    private Season(Builder builder) {
        this.seasonId = builder.seasonId;
        this.tournament = builder.tournament;
        this.year = builder.year;
        this.seasonName = builder.seasonName;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.status = builder.status;
        this.matches = builder.matches;
        this.standings = builder.standings;
    }
    
    // Getters
    public UUID getSeasonId() { return seasonId; }
    public Tournament getTournament() { return tournament; }
    public int getYear() { return year; }
    public String getSeasonName() { return seasonName; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public SeasonStatus getStatus() { return status; }
    public List<Match> getMatches() { return matches; }
    public List<Standing> getStandings() { return standings; }
    
    // Setters
    public void setTournament(Tournament tournament) { this.tournament = tournament; }
    public void setStatus(SeasonStatus status) { this.status = status; }
    public void setMatches(List<Match> matches) { this.matches = matches; }
    public void setStandings(List<Standing> standings) { this.standings = standings; }
    
    public int getTotalMatches() {
        return matches != null ? matches.size() : 0;
    }
    
    public int getCompletedMatches() {
        return matches != null ? (int) matches.stream()
                .filter(m -> m.getStatus() == MatchStatus.COMPLETED)
                .count() : 0;
    }
    
    public String toString() {
        return "Season{" +
                "seasonId=" + seasonId +
                ", seasonName='" + seasonName + '\'' +
                ", year=" + year +
                ", status=" + status +
                ", matches=" + getTotalMatches() +
                ", completed=" + getCompletedMatches() +
                '}';
    }
    
    public static class Builder {
        private UUID seasonId;
        private Tournament tournament;
        private int year;
        private String seasonName;
        private LocalDate startDate;
        private LocalDate endDate;
        private SeasonStatus status;
        private List<Match> matches;
        private List<Standing> standings;
        
        public Builder setSeasonId(UUID seasonId) {
            this.seasonId = seasonId;
            return this;
        }

        public Builder setTournament(Tournament tournament) {
            this.tournament = tournament;
            return this;
        }
        
        public Builder setYear(int year) {
            this.year = year;
            return this;
        }
        
        public Builder setSeasonName(String seasonName) {
            this.seasonName = seasonName;
            return this;
        }
        
        public Builder setStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }
        
        public Builder setEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }
        
        public Builder setStatus(SeasonStatus status) {
            this.status = status;
            return this;
        }
        
        public Builder setMatches(List<Match> matches) {
            this.matches = matches;
            return this;
        }
        
        public Builder setStandings(List<Standing> standings) {
            this.standings = standings;
            return this;
        }
        
        public Season build() {
            if (seasonId == null) {
                throw new IllegalStateException("Season ID is required");
            }
            if (seasonName == null || seasonName.isBlank()) {
                throw new IllegalStateException("Season name is required");
            }
            if (startDate == null) {
                throw new IllegalStateException("Start date is required");
            }
            if (endDate == null) {
                throw new IllegalStateException("End date is required");
            }
            if (status == null) {
                status = SeasonStatus.ACTIVE;
            }
            return new Season(this);
        }
    }
}
