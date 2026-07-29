package za.co.lz.domain.team;

import jakarta.persistence.*;
import za.co.lz.domain.match.Season;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
    name = "squad_registrations",
    uniqueConstraints = {
        // no two players on the same team/season can share a kit number
        @UniqueConstraint(columnNames = {"team_id", "season_id", "kitNumber"})
    }
)
public class SquadRegistration implements Serializable {
    @Id
    private UUID squadRegistrationId;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;

    private int kitNumber;
    private LocalDate registrationDate;

    @Enumerated(EnumType.STRING)
    private SquadRegistrationStatus status; // ACTIVE, TRANSFERRED, RELEASED

    public SquadRegistration(){}

    private SquadRegistration(Builder builder){
        this.squadRegistrationId = builder.squadRegistrationId;
        this.player = builder.player;
        this.team = builder.team;
        this.season = builder.season;
        this.kitNumber = builder.kitNumber;
        this.registrationDate = builder.registrationDate;
        this.status = builder.status;
    }

    public UUID getSquadRegistrationId() { return squadRegistrationId; }
    public Player getPlayer() { return player; }
    public Team getTeam() { return team; }
    public Season getSeason() { return season; }
    public int getKitNumber() { return kitNumber; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public SquadRegistrationStatus getStatus() { return status; }

    public void setStatus(SquadRegistrationStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "SquadRegistration{" +
                "squadRegistrationId=" + squadRegistrationId +
                ", player=" + (player != null ? player.getPlayerId() : "null") +
                ", team=" + (team != null ? team.getTeamId() : "null") +
                ", season=" + (season != null ? season.getSeasonId() : "null") +
                ", kitNumber=" + kitNumber +
                ", status=" + status +
                '}';
    }

    public static class Builder {
        private UUID squadRegistrationId;
        private Player player;
        private Team team;
        private Season season;
        private int kitNumber;
        private LocalDate registrationDate;
        private SquadRegistrationStatus status;

        public Builder setSquadRegistrationId(UUID id) { this.squadRegistrationId = id; return this; }
        public Builder setPlayer(Player player) { this.player = player; return this; }
        public Builder setTeam(Team team) { this.team = team; return this; }
        public Builder setSeason(Season season) { this.season = season; return this; }
        public Builder setKitNumber(int kitNumber) { this.kitNumber = kitNumber; return this; }
        public Builder setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; return this; }
        public Builder setStatus(SquadRegistrationStatus status) { this.status = status; return this; }

        public Builder copy(SquadRegistration r) {
            this.squadRegistrationId = r.squadRegistrationId;
            this.player = r.player;
            this.team = r.team;
            this.season = r.season;
            this.kitNumber = r.kitNumber;
            this.registrationDate = r.registrationDate;
            this.status = r.status;
            return this;
        }

        public SquadRegistration build() {
            if (player == null) throw new IllegalStateException("SquadRegistration requires a player.");
            if (team == null) throw new IllegalStateException("SquadRegistration requires a team.");
            if (season == null) throw new IllegalStateException("SquadRegistration requires a season.");
            if (status == null) status = SquadRegistrationStatus.ACTIVE;
            return new SquadRegistration(this);
        }
    }
}