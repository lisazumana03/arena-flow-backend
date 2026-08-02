package za.co.lz.domain.match;

import jakarta.persistence.*;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.Team;

import java.io.Serializable;
import java.util.UUID;

/**
 * A single timestamped occurrence within a match: a goal, a booking, a corner,
 * a free kick, a substitution or an injury. This is the append-only log that
 * {@link Match#getHomeScore()}/{@link Match#getAwayScore()}, {@link Standing}
 * and player discipline/fitness records are all derived from.
 */
@Entity
@Table(name = "match_events")
public class MatchEvent implements Serializable {

    @Id
    private UUID eventId;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team; // team the event is attributed to

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player; // primary player involved (scorer, booked player, injured player, player subbed off)

    @ManyToOne
    @JoinColumn(name = "related_player_id")
    private Player relatedPlayer; // e.g. assist provider, or player subbed on

    @Enumerated(EnumType.STRING)
    private MatchEventType eventType;

    private int minute;
    private Integer stoppageMinute; // e.g. 3 for "90+3"

    @Enumerated(EnumType.STRING)
    private DisciplinaryOffence offence; // set when eventType is a card

    @Enumerated(EnumType.STRING)
    private InjurySeverity injurySeverity; // set when eventType is INJURY

    private String notes;

    public MatchEvent() {}

    private MatchEvent(Builder builder) {
        this.eventId = builder.eventId;
        this.match = builder.match;
        this.team = builder.team;
        this.player = builder.player;
        this.relatedPlayer = builder.relatedPlayer;
        this.eventType = builder.eventType;
        this.minute = builder.minute;
        this.stoppageMinute = builder.stoppageMinute;
        this.offence = builder.offence;
        this.injurySeverity = builder.injurySeverity;
        this.notes = builder.notes;
    }

    public UUID getEventId() { return eventId; }
    public Match getMatch() { return match; }
    public Team getTeam() { return team; }
    public Player getPlayer() { return player; }
    public Player getRelatedPlayer() { return relatedPlayer; }
    public MatchEventType getEventType() { return eventType; }
    public int getMinute() { return minute; }
    public Integer getStoppageMinute() { return stoppageMinute; }
    public DisciplinaryOffence getOffence() { return offence; }
    public InjurySeverity getInjurySeverity() { return injurySeverity; }
    public String getNotes() { return notes; }

    @Override
    public String toString() {
        return "MatchEvent{" +
                "eventType=" + eventType +
                ", minute=" + minute +
                (stoppageMinute != null ? ("+" + stoppageMinute) : "") +
                ", team=" + (team != null ? team.getTeamName() : "null") +
                ", player=" + (player != null ? player.getPlayerName() : "null") +
                '}';
    }

    public static class Builder {
        private UUID eventId;
        private Match match;
        private Team team;
        private Player player;
        private Player relatedPlayer;
        private MatchEventType eventType;
        private int minute;
        private Integer stoppageMinute;
        private DisciplinaryOffence offence;
        private InjurySeverity injurySeverity;
        private String notes;

        public Builder setEventId(UUID eventId) { this.eventId = eventId; return this; }
        public Builder setMatch(Match match) { this.match = match; return this; }
        public Builder setTeam(Team team) { this.team = team; return this; }
        public Builder setPlayer(Player player) { this.player = player; return this; }
        public Builder setRelatedPlayer(Player relatedPlayer) { this.relatedPlayer = relatedPlayer; return this; }
        public Builder setEventType(MatchEventType eventType) { this.eventType = eventType; return this; }
        public Builder setMinute(int minute) { this.minute = minute; return this; }
        public Builder setStoppageMinute(Integer stoppageMinute) { this.stoppageMinute = stoppageMinute; return this; }
        public Builder setOffence(DisciplinaryOffence offence) { this.offence = offence; return this; }
        public Builder setInjurySeverity(InjurySeverity injurySeverity) { this.injurySeverity = injurySeverity; return this; }
        public Builder setNotes(String notes) { this.notes = notes; return this; }

        public MatchEvent build() {
            if (eventId == null) throw new IllegalStateException("Event ID is required");
            if (match == null) throw new IllegalStateException("Match is required");
            if (team == null) throw new IllegalStateException("Team is required");
            if (eventType == null) throw new IllegalStateException("Event type is required");
            if (minute < 0) throw new IllegalStateException("Minute cannot be negative");
            if (eventType == MatchEventType.RED_CARD || eventType == MatchEventType.SECOND_YELLOW_CARD) {
                if (offence == null) throw new IllegalStateException("A dismissal requires a DisciplinaryOffence");
                if (player == null) throw new IllegalStateException("A card requires the player booked");
            }
            if (eventType == MatchEventType.INJURY && injurySeverity == null) {
                throw new IllegalStateException("An injury event requires an InjurySeverity");
            }
            return new MatchEvent(this);
        }
    }
}
