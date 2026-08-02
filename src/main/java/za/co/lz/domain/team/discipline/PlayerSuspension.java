package za.co.lz.domain.team.discipline;

import jakarta.persistence.*;
import za.co.lz.domain.match.DisciplinaryOffence;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.team.Player;

import java.io.Serializable;
import java.util.UUID;

/**
 * A ban served by a player after a sending-off. gamesServed increments each
 * time the player's team plays another match while the suspension is active
 * (see IPlayerSuspensionService#serveMatchForTeam); once gamesServed reaches
 * gamesBanned, the suspension is no longer active and the player is eligible
 * again.
 */
@Entity
@Table(name = "player_suspensions")
public class PlayerSuspension implements Serializable {

    @Id
    private UUID suspensionId;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match triggeringMatch; // the match the red card occurred in

    @Enumerated(EnumType.STRING)
    private DisciplinaryOffence offence;

    private int gamesBanned;
    private int gamesServed;
    private boolean active;

    public PlayerSuspension() {}

    private PlayerSuspension(Builder builder) {
        this.suspensionId = builder.suspensionId;
        this.player = builder.player;
        this.triggeringMatch = builder.triggeringMatch;
        this.offence = builder.offence;
        this.gamesBanned = builder.gamesBanned;
        this.gamesServed = builder.gamesServed;
        this.active = builder.active;
    }

    public UUID getSuspensionId() { return suspensionId; }
    public Player getPlayer() { return player; }
    public Match getTriggeringMatch() { return triggeringMatch; }
    public DisciplinaryOffence getOffence() { return offence; }
    public int getGamesBanned() { return gamesBanned; }
    public int getGamesServed() { return gamesServed; }
    public boolean isActive() { return active; }

    public int getGamesRemaining() { return Math.max(0, gamesBanned - gamesServed); }

    /** Records that the player's team has played one more match under this suspension. */
    public void serveGame() {
        if (!active) return;
        gamesServed++;
        if (gamesServed >= gamesBanned) {
            active = false;
        }
    }

    @Override
    public String toString() {
        return "PlayerSuspension{" +
                "player=" + (player != null ? player.getPlayerName() : "null") +
                ", offence=" + offence +
                ", gamesBanned=" + gamesBanned +
                ", gamesServed=" + gamesServed +
                ", active=" + active +
                '}';
    }

    public static class Builder {
        private UUID suspensionId;
        private Player player;
        private Match triggeringMatch;
        private DisciplinaryOffence offence;
        private int gamesBanned;
        private int gamesServed;
        private boolean active;

        public Builder setSuspensionId(UUID suspensionId) { this.suspensionId = suspensionId; return this; }
        public Builder setPlayer(Player player) { this.player = player; return this; }
        public Builder setTriggeringMatch(Match triggeringMatch) { this.triggeringMatch = triggeringMatch; return this; }
        public Builder setOffence(DisciplinaryOffence offence) { this.offence = offence; return this; }
        public Builder setGamesBanned(int gamesBanned) { this.gamesBanned = gamesBanned; return this; }
        public Builder setGamesServed(int gamesServed) { this.gamesServed = gamesServed; return this; }
        public Builder setActive(boolean active) { this.active = active; return this; }

        public PlayerSuspension build() {
            if (suspensionId == null) throw new IllegalStateException("Suspension ID is required");
            if (player == null) throw new IllegalStateException("Player is required");
            if (triggeringMatch == null) throw new IllegalStateException("Triggering match is required");
            if (offence == null) throw new IllegalStateException("Offence is required");
            if (gamesBanned <= 0) gamesBanned = offence.getDefaultBanGames();
            active = gamesServed < gamesBanned;
            return new PlayerSuspension(this);
        }
    }
}
