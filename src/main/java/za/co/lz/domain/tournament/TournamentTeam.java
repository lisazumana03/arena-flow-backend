package za.co.lz.domain.tournament;

import jakarta.persistence.*;
import za.co.lz.domain.match.Season;
import za.co.lz.domain.team.Team;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Links a {@link Team} to a specific tournament edition ({@link Season}) and
 * carries all data that is unique to that entry: points, group, qualification
 * status, registration date.
 *
 * This deliberately replaces a bare @ManyToMany(Team, Tournament). A team's
 * involvement in "Premier League 2027" is independent of its involvement in
 * "FA Cup 2027" or "UEFA Champions League 2027" even though all three are the
 * same team in the same calendar year - each is its own TournamentTeam row
 * against its own Season.
 *
 * One row = one team's registration in one tournament edition.
 */
@Entity
@Table(
    name = "tournament_teams",
    uniqueConstraints = {
        // a team can only be entered once per tournament edition
        @UniqueConstraint(columnNames = {"season_id", "team_id"})
    }
)
public class TournamentTeam implements Serializable {

    @Id
    private UUID tournamentTeamId;

    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    // Relevant for LEAGUE/HYBRID formats; ignored for pure KNOCKOUT
    private int points;

    // e.g. "Group A" - null for pure LEAGUE or KNOCKOUT formats without groups
    private String groupName;

    @Enumerated(EnumType.STRING)
    private QualificationStatus qualificationStatus;

    private LocalDate registrationDate;

    public TournamentTeam() {}

    private TournamentTeam(Builder builder) {
        this.tournamentTeamId = builder.tournamentTeamId;
        this.season = builder.season;
        this.team = builder.team;
        this.points = builder.points;
        this.groupName = builder.groupName;
        this.qualificationStatus = builder.qualificationStatus;
        this.registrationDate = builder.registrationDate;
    }

    public UUID getTournamentTeamId() { return tournamentTeamId; }
    public Season getSeason() { return season; }
    public Team getTeam() { return team; }
    public int getPoints() { return points; }
    public String getGroupName() { return groupName; }
    public QualificationStatus getQualificationStatus() { return qualificationStatus; }
    public LocalDate getRegistrationDate() { return registrationDate; }

    public void setPoints(int points) { this.points = points; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    public void setQualificationStatus(QualificationStatus qualificationStatus) { this.qualificationStatus = qualificationStatus; }

    @Override
    public String toString() {
        return "TournamentTeam{" +
                "team=" + (team != null ? team.getTeamName() : "null") +
                ", season=" + (season != null ? season.getSeasonName() : "null") +
                ", group='" + groupName + '\'' +
                ", points=" + points +
                ", status=" + qualificationStatus +
                '}';
    }

    public static class Builder {
        private UUID tournamentTeamId;
        private Season season;
        private Team team;
        private int points;
        private String groupName;
        private QualificationStatus qualificationStatus;
        private LocalDate registrationDate;

        public Builder setTournamentTeamId(UUID tournamentTeamId) { this.tournamentTeamId = tournamentTeamId; return this; }
        public Builder setSeason(Season season) { this.season = season; return this; }
        public Builder setTeam(Team team) { this.team = team; return this; }
        public Builder setPoints(int points) { this.points = points; return this; }
        public Builder setGroupName(String groupName) { this.groupName = groupName; return this; }
        public Builder setQualificationStatus(QualificationStatus qualificationStatus) { this.qualificationStatus = qualificationStatus; return this; }
        public Builder setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; return this; }

        public Builder copy(TournamentTeam tournamentTeam) {
            this.tournamentTeamId = tournamentTeam.tournamentTeamId;
            this.season = tournamentTeam.season;
            this.team = tournamentTeam.team;
            this.points = tournamentTeam.points;
            this.groupName = tournamentTeam.groupName;
            this.qualificationStatus = tournamentTeam.qualificationStatus;
            this.registrationDate = tournamentTeam.registrationDate;
            return this;
        }

        public TournamentTeam build() {
            if (season == null) throw new IllegalStateException("TournamentTeam requires a season (tournament edition).");
            if (team == null) throw new IllegalStateException("TournamentTeam requires a team.");
            if (registrationDate == null) throw new IllegalStateException("TournamentTeam requires a registration date.");
            if (qualificationStatus == null) qualificationStatus = QualificationStatus.REGISTERED;
            return new TournamentTeam(this);
        }
    }
}
