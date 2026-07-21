package za.co.lz.domain.tournament;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import za.co.lz.domain.tournament.phase.Stage;
import za.co.lz.domain.tournament.phase.StageType;

import java.util.List;
import java.util.UUID;

@Entity
public class Tournament {
    @Id
    private UUID tournamentId;
    private String tournamentName;
    private TournamentSport tournamentSport;
    private String season;
    private TournamentFormat format;
    private List<Stage> stages;
    private TournamentStatus status;

    public Tournament(){}

    private Tournament(Builder builder) {
        this.tournamentId = builder.tournamentId;
        this.tournamentName = builder.tournamentName;
        this.tournamentSport = builder.tournamentSport;
        this.season = builder.season;
        this.format = builder.format;
        this.stages = builder.stages;
        this.status = builder.status;
    }

    /**
     * Attaches a stage to this tournament. Only legal while the tournament
     * is still in DRAFT — once simulation has started the shape of the
     * competition is frozen.
     */
    public void addStage(Stage stage) {
        if (status != TournamentStatus.DRAFT) {
            throw new IllegalStateException(
                    "Cannot add a stage once the tournament has left DRAFT status: " + status);
        }
        stage.assignTournament(this);
        stage.assignOrder(stages.size() + 1);
        stages.add(stage);
    }
 
    public void start() {
        if (stages.isEmpty()) {
            throw new IllegalStateException("Cannot start a tournament with no stages");
        }
        if (status != TournamentStatus.DRAFT) {
            throw new IllegalStateException("Only a DRAFT tournament can be started, was " + status);
        }
        this.status = TournamentStatus.IN_PROGRESS;
        stages.get(0).start();
    }
 
    private void validateStagesMatchFormat() {
        boolean hasLeague = stages.stream().anyMatch(s -> s.getStageType() == StageType.LEAGUE);
        boolean hasKnockout = stages.stream().anyMatch(s -> s.getStageType() == StageType.KNOCKOUT);
 
        switch (format) {
            case LEAGUE -> {
                if (!hasLeague || hasKnockout) {
                    throw new IllegalStateException("LEAGUE format requires only league stage(s)");
                }
            }
            case KNOCKOUT -> {
                if (!hasKnockout || hasLeague) {
                    throw new IllegalStateException("KNOCKOUT format requires only knockout stage(s)");
                }
            }
            case HYBRID -> {
                if (!hasLeague || !hasKnockout) {
                    throw new IllegalStateException(
                            "HYBRID format requires at least one league stage and one knockout stage");
                }
            }
        }
    }

    public UUID getTournamentId() {
        return tournamentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public TournamentSport getTournamentSport() {
        return tournamentSport;
    }

    public String getSeason() {
        return season;
    }

    public TournamentFormat getFormat() {
        return format;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public TournamentStatus getStatus() {
        return status;
    }

    public String toString() {
        return "Tournament{" +
                "tournamentId=" + tournamentId +
                ", tournamentName='" + tournamentName + '\'' +
                ", tournamentSport=" + tournamentSport +
                ", season='" + season + '\'' +
                ", format=" + format +
                ", stages=" + stages +
                ", status=" + status +
                '}';
    }

    public static class Builder{
        private UUID tournamentId;
        private String tournamentName;
        private TournamentSport tournamentSport;
        private String season;
        private TournamentFormat format;
        private List<Stage> stages;
        private TournamentStatus status;

        public Builder setTournamentId(UUID tournamentId) {
            this.tournamentId = tournamentId;
            return this;
        }

        public Builder setTournamentName(String tournamentName) {
            this.tournamentName = tournamentName;
            return this;
        }

        public Builder setTournamentSport(TournamentSport tournamentSport) {
            this.tournamentSport = tournamentSport;
            return this;
        }

        public Builder setSeason(String season) {
            this.season = season;
            return this;
        }

        public Builder setFormat(TournamentFormat format) {
            this.format = format;
            return this;
        }

        public Builder setStages(List<Stage> stages) {
            this.stages = stages;
            return this;
        }

        public Builder setStatus(TournamentStatus status) {
            this.status = status;
            return this;
        }

        public Builder copy(Tournament tournament) {
            this.tournamentId = tournament.tournamentId;
            this.tournamentName = tournament.tournamentName;
            this.tournamentSport = tournament.tournamentSport;
            this.season = tournament.season;
            this.format = tournament.format;
            this.stages = tournament.stages;
            this.status = tournament.status;
            return this;
        }

        public Tournament build() {
            return new Tournament(this);
        }
    }
}
