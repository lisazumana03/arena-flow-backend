package za.co.lz.domain.tournament;

import jakarta.persistence.*;
import za.co.lz.domain.match.Season;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Represents a competition's identity, independent of any given year
 * (e.g. "Premier League", "FA Cup", "UEFA Champions League").
 *
 * A Tournament is a template: it does not hold matches, standings or team
 * entries directly. Each yearly run of a Tournament is a {@link Season}
 * (e.g. "Premier League 2027" = Season{year=2027, tournament=this}),
 * and team-specific data for that run (points, group, qualification status,
 * registration date) lives on {@link TournamentTeam}, scoped to that Season.
 */
@Entity
@Table(name = "tournaments")
public class Tournament implements Serializable {

    @Id
    private UUID tournamentId;
    private String tournamentName;
    @Enumerated(EnumType.STRING)
    private TournamentFormat format;
    private String description;
    @OneToMany(mappedBy = "tournament")
    private List<Season> seasons;
    private byte[] tournamentLogo;

    public Tournament() {}

    private Tournament(Builder builder) {
        this.tournamentId = builder.tournamentId;
        this.tournamentName = builder.tournamentName;
        this.format = builder.format;
        this.description = builder.description;
        this.seasons = builder.seasons;
        this.tournamentLogo = builder.tournamentLogo;
    }

    public UUID getTournamentId() { return tournamentId; }
    public String getTournamentName() { return tournamentName; }
    public TournamentFormat getFormat() { return format; }
    public String getDescription() { return description; }
    public List<Season> getSeasons() { return seasons; }
    public byte[] getLogo() {return tournamentLogo; }

    public void setDescription(String description) { this.description = description; }
    public void setSeasons(List<Season> seasons) { this.seasons = seasons; }

    @Override
    public String toString() {
        return "Tournament{" +
                "tournamentId=" + tournamentId +
                ", tournamentName='" + tournamentName + '\'' +
                ", format=" + format +
                '}';
    }

    public static class Builder {
        private UUID tournamentId;
        private String tournamentName;
        private TournamentFormat format;
        private String description;
        private List<Season> seasons;
        private byte[] tournamentLogo;

        public Builder setTournamentId(UUID tournamentId) { this.tournamentId = tournamentId; return this; }
        public Builder setTournamentName(String tournamentName) { this.tournamentName = tournamentName; return this; }
        public Builder setFormat(TournamentFormat format) { this.format = format; return this; }
        public Builder setDescription(String description) { this.description = description; return this; }
        public Builder setSeasons(List<Season> seasons) { this.seasons = seasons; return this; }
        public Builder setTournamentLogo(byte[] tournamentLogo){
            this.tournamentLogo = tournamentLogo;
            return this;
        }

        public Builder copy(Tournament tournament) {
            this.tournamentId = tournament.tournamentId;
            this.tournamentName = tournament.tournamentName;
            this.format = tournament.format;
            this.description = tournament.description;
            this.seasons = tournament.seasons;
            this.tournamentLogo = tournament.tournamentLogo;
            return this;
        }

        public Tournament build() {
            if (tournamentId == null) throw new IllegalStateException("Tournament ID is required");
            if (tournamentName == null || tournamentName.isBlank()) throw new IllegalStateException("Tournament name is required");
            if (format == null) throw new IllegalStateException("Tournament format is required");
            return new Tournament(this);
        }
    }
}
