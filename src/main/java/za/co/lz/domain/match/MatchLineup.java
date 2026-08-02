package za.co.lz.domain.match;

import jakarta.persistence.*;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;

import java.io.Serializable;
import java.util.UUID;

/**
 * One row = one player named in one team's squad for one match - either in
 * the starting XI or on the bench. Substitutions during the match are tracked
 * via {@link MatchEvent} (type SUBSTITUTION) and reflected back onto this row
 * (substitutedOffMinute) so "who was on the pitch when" can be reconstructed.
 */
@Entity
@Table(
    name = "match_lineups",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"match_id", "player_id"})
    }
)
public class MatchLineup implements Serializable {

    @Id
    private UUID lineupId;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    private boolean starting;      // true = starting XI, false = substitute/bench
    private int shirtNumber;
    @Enumerated(EnumType.STRING)
    private PlayerPosition matchPosition;

    private Integer substitutedOffMinute; // null while still on the pitch / never came on

    public MatchLineup() {}

    private MatchLineup(Builder builder) {
        this.lineupId = builder.lineupId;
        this.match = builder.match;
        this.team = builder.team;
        this.player = builder.player;
        this.starting = builder.starting;
        this.shirtNumber = builder.shirtNumber;
        this.matchPosition = builder.matchPosition;
        this.substitutedOffMinute = builder.substitutedOffMinute;
    }

    public UUID getLineupId() { return lineupId; }
    public Match getMatch() { return match; }
    public Team getTeam() { return team; }
    public Player getPlayer() { return player; }
    public boolean isStarting() { return starting; }
    public int getShirtNumber() { return shirtNumber; }
    public PlayerPosition getMatchPosition() { return matchPosition; }
    public Integer getSubstitutedOffMinute() { return substitutedOffMinute; }

    public void setSubstitutedOffMinute(Integer substitutedOffMinute) { this.substitutedOffMinute = substitutedOffMinute; }

    @Override
    public String toString() {
        return "MatchLineup{" +
                "player=" + (player != null ? player.getPlayerName() : "null") +
                ", team=" + (team != null ? team.getTeamName() : "null") +
                ", starting=" + starting +
                ", shirtNumber=" + shirtNumber +
                '}';
    }

    public static class Builder {
        private UUID lineupId;
        private Match match;
        private Team team;
        private Player player;
        private boolean starting;
        private int shirtNumber;
        private PlayerPosition matchPosition;
        private Integer substitutedOffMinute;

        public Builder setLineupId(UUID lineupId) { this.lineupId = lineupId; return this; }
        public Builder setMatch(Match match) { this.match = match; return this; }
        public Builder setTeam(Team team) { this.team = team; return this; }
        public Builder setPlayer(Player player) { this.player = player; return this; }
        public Builder setStarting(boolean starting) { this.starting = starting; return this; }
        public Builder setShirtNumber(int shirtNumber) { this.shirtNumber = shirtNumber; return this; }
        public Builder setMatchPosition(PlayerPosition matchPosition) { this.matchPosition = matchPosition; return this; }
        public Builder setSubstitutedOffMinute(Integer substitutedOffMinute) { this.substitutedOffMinute = substitutedOffMinute; return this; }

        public MatchLineup build() {
            if (lineupId == null) throw new IllegalStateException("Lineup ID is required");
            if (match == null) throw new IllegalStateException("Match is required");
            if (team == null) throw new IllegalStateException("Team is required");
            if (player == null) throw new IllegalStateException("Player is required");
            if (shirtNumber <= 0) throw new IllegalStateException("Shirt number must be positive");
            return new MatchLineup(this);
        }
    }
}
