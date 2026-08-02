package za.co.lz.domain.team.discipline;

import jakarta.persistence.*;
import za.co.lz.domain.match.InjurySeverity;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.team.Player;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * An injury a player sustained during a match, with an estimated return date
 * derived from {@link InjurySeverity}. Medical staff can override
 * expectedReturnDate once a proper diagnosis is made (see
 * IPlayerInjuryService#updateExpectedReturn) - the value computed at creation
 * time is only a first estimate.
 */
@Entity
@Table(name = "player_injuries")
public class PlayerInjury implements Serializable {

    @Id
    private UUID injuryId;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match triggeringMatch; // nullable - injury may be reported outside a match (training)

    @Enumerated(EnumType.STRING)
    private InjurySeverity severity;

    private LocalDate injuryDate;
    private LocalDate expectedReturnDate;
    private boolean active;

    public PlayerInjury() {}

    private PlayerInjury(Builder builder) {
        this.injuryId = builder.injuryId;
        this.player = builder.player;
        this.triggeringMatch = builder.triggeringMatch;
        this.severity = builder.severity;
        this.injuryDate = builder.injuryDate;
        this.expectedReturnDate = builder.expectedReturnDate;
        this.active = builder.active;
    }

    public UUID getInjuryId() { return injuryId; }
    public Player getPlayer() { return player; }
    public Match getTriggeringMatch() { return triggeringMatch; }
    public InjurySeverity getSeverity() { return severity; }
    public LocalDate getInjuryDate() { return injuryDate; }
    public LocalDate getExpectedReturnDate() { return expectedReturnDate; }
    public boolean isActive() { return active; }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }

    /** Estimated games missed, assuming ~1 match per week between injury and return. */
    public long getEstimatedGamesOut() {
        if (injuryDate == null || expectedReturnDate == null) return severity.getTypicalGamesOut();
        long days = java.time.temporal.ChronoUnit.DAYS.between(injuryDate, expectedReturnDate);
        return (long) Math.ceil(days / 7.0);
    }

    public void markRecovered() { this.active = false; }

    @Override
    public String toString() {
        return "PlayerInjury{" +
                "player=" + (player != null ? player.getPlayerName() : "null") +
                ", severity=" + severity +
                ", expectedReturnDate=" + expectedReturnDate +
                ", active=" + active +
                '}';
    }

    public static class Builder {
        private UUID injuryId;
        private Player player;
        private Match triggeringMatch;
        private InjurySeverity severity;
        private LocalDate injuryDate;
        private LocalDate expectedReturnDate;
        private boolean active = true;

        public Builder setInjuryId(UUID injuryId) { this.injuryId = injuryId; return this; }
        public Builder setPlayer(Player player) { this.player = player; return this; }
        public Builder setTriggeringMatch(Match triggeringMatch) { this.triggeringMatch = triggeringMatch; return this; }
        public Builder setSeverity(InjurySeverity severity) { this.severity = severity; return this; }
        public Builder setInjuryDate(LocalDate injuryDate) { this.injuryDate = injuryDate; return this; }
        public Builder setExpectedReturnDate(LocalDate expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; return this; }
        public Builder setActive(boolean active) { this.active = active; return this; }

        public PlayerInjury build() {
            if (injuryId == null) throw new IllegalStateException("Injury ID is required");
            if (player == null) throw new IllegalStateException("Player is required");
            if (severity == null) throw new IllegalStateException("Injury severity is required");
            if (injuryDate == null) injuryDate = LocalDate.now();
            if (expectedReturnDate == null) expectedReturnDate = injuryDate.plusDays(severity.getTypicalDaysOut());
            return new PlayerInjury(this);
        }
    }
}
