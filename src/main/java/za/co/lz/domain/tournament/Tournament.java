package za.co.lz.domain.tournament;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    // JsonIgnore breaks the Tournament -> seasons -> Season -> tournament -> seasons -> ...
    // cycle. Frontend never needs seasons nested inside a Tournament response anyway —
    // editions are always fetched via the dedicated /tournaments/{id}/editions endpoint,
    // which still returns Season.tournament (now cycle-free, since this side is ignored).
    @JsonIgnore
    @OneToMany(mappedBy = "tournament")
    private List<Season> seasons;
    private byte[] tournamentLogo;
    // Only meaningful for LEAGUE/HYBRID formats that produce a standings table.
    // How many of the top places earn promotion (e.g. 3 for a second-tier league).
    // 0 = promotion/relegation colouring is not applicable to this tournament.
    private int promotionSpots;
    // How many of the bottom places are relegated (e.g. 3 for a top-flight league).
    private int relegationSpots;

    public Tournament() {}

    private Tournament(Builder builder) {
        this.tournamentId = builder.tournamentId;
        this.tournamentName = builder.tournamentName;
        this.format = builder.format;
        this.description = builder.description;
        this.seasons = builder.seasons;
        this.tournamentLogo = builder.tournamentLogo;
        this.promotionSpots = builder.promotionSpots;
        this.relegationSpots = builder.relegationSpots;
    }

    public UUID getTournamentId() { return tournamentId; }
    public String getTournamentName() { return tournamentName; }
    public TournamentFormat getFormat() { return format; }
    public String getDescription() { return description; }
    public List<Season> getSeasons() { return seasons; }
    public byte[] getLogo() {return tournamentLogo; }
    public int getPromotionSpots() { return promotionSpots; }
    public int getRelegationSpots() { return relegationSpots; }

    public void setDescription(String description) { this.description = description; }
    public void setSeasons(List<Season> seasons) { this.seasons = seasons; }
    public void setPromotionSpots(int promotionSpots) { this.promotionSpots = promotionSpots; }
    public void setRelegationSpots(int relegationSpots) { this.relegationSpots = relegationSpots; }

    @Override
    public String toString() {
        return "Tournament{" +
                "tournamentId=" + tournamentId +
                ", tournamentName='" + tournamentName + '\'' +
                ", format=" + format +
                ", promotionSpots=" + promotionSpots +
                ", relegationSpots=" + relegationSpots +
                '}';
    }

    public static class Builder {
        private UUID tournamentId;
        private String tournamentName;
        private TournamentFormat format;
        private String description;
        private List<Season> seasons;
        private byte[] tournamentLogo;
        private int promotionSpots;
        private int relegationSpots;

        public Builder setTournamentId(UUID tournamentId) { this.tournamentId = tournamentId; return this; }
        public Builder setTournamentName(String tournamentName) { this.tournamentName = tournamentName; return this; }
        public Builder setFormat(TournamentFormat format) { this.format = format; return this; }
        public Builder setDescription(String description) { this.description = description; return this; }
        public Builder setSeasons(List<Season> seasons) { this.seasons = seasons; return this; }
        public Builder setTournamentLogo(byte[] tournamentLogo){
            this.tournamentLogo = tournamentLogo;
            return this;
        }
        public Builder setPromotionSpots(int promotionSpots) { this.promotionSpots = promotionSpots; return this; }
        public Builder setRelegationSpots(int relegationSpots) { this.relegationSpots = relegationSpots; return this; }

        public Builder copy(Tournament tournament) {
            this.tournamentId = tournament.tournamentId;
            this.tournamentName = tournament.tournamentName;
            this.format = tournament.format;
            this.description = tournament.description;
            this.seasons = tournament.seasons;
            this.tournamentLogo = tournament.tournamentLogo;
            this.promotionSpots = tournament.promotionSpots;
            this.relegationSpots = tournament.relegationSpots;
            return this;
        }

        public Tournament build() {
            if (tournamentId == null) throw new IllegalStateException("Tournament ID is required");
            if (tournamentName == null || tournamentName.isBlank()) throw new IllegalStateException("Tournament name is required");
            if (format == null) throw new IllegalStateException("Tournament format is required");
            if (promotionSpots < 0) throw new IllegalStateException("Promotion spots cannot be negative");
            if (relegationSpots < 0) throw new IllegalStateException("Relegation spots cannot be negative");
            return new Tournament(this);
        }
    }
}
