package za.co.lz.domain.tournament.phase;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import za.co.lz.domain.tournament.Tournament;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Stage implements Serializable {
    @Id
    @GeneratedValue
    private UUID stageId;
    @Column(nullable = false)
    private StageType stageType;
    @Column(name = "stage_order", nullable = false)
    private int stageOrder;
    @Column(nullable = false)
    private String stageName;
    @Column(nullable = false)
    private StageStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;
    @OneToOne(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private LeagueStageDetails leagueDetails;
    @OneToOne(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private KnockoutStageDetails knockoutDetails;
 
    protected Stage() {
        // required by JPA
    }

    private Stage(Builder builder) {
        this.stageId = builder.stageId;
        this.stageType = builder.stageType;
        this.stageName = builder.stageName;
        this.status = builder.status.PENDING;
    }

    public void assignTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public void assignOrder(int order) {
        this.stageOrder = order;
    }

    public void start() {
        if (status != StageStatus.PENDING) {
            throw new IllegalStateException("Cannot start a stage that is not in PENDING status: " + status);
        }
        this.status = StageStatus.IN_PROGRESS;
    }

    public void complete() {
        if (status != StageStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot complete a stage that is not in IN_PROGRESS status: " + status);
        }
        this.status = StageStatus.COMPLETED;
    }

    public void attachLeagueDetails(LeagueStageDetails details) {
        if (stageType != StageType.LEAGUE) {
            throw new IllegalStateException("Cannot attach league details to a " + stageType + " stage");
        }
        if (this.leagueDetails != null) {
            throw new IllegalStateException("League details are already attached to this stage");
        }
        this.leagueDetails = details;
    }

    public void attachKnockoutDetails(KnockoutStageDetails details) {
        if (stageType != StageType.KNOCKOUT) {
            throw new IllegalStateException("Cannot attach knockout details to a " + stageType + " stage");
        }
        if (this.knockoutDetails != null) {
            throw new IllegalStateException("Knockout details are already attached to this stage");
        }
        this.knockoutDetails = details;
    }

    public UUID getStageId() {
        return stageId;
    }

    public StageType getStageType() {
        return stageType;
    }

    public int getStageOrder() {
        return stageOrder;
    }

    public String getStageName() {
        return stageName;
    }

    public StageStatus getStageStatus() {
        return status;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public LeagueStageDetails getLeagueDetails() {
        return leagueDetails;
    }

    public KnockoutStageDetails getKnockoutDetails() {
        return knockoutDetails;
    }

    @Override
    public String toString() {
        return "Stage{" +
                "stageId=" + stageId +
                ", stageType=" + stageType +
                ", stageOrder=" + stageOrder +
                ", stageName='" + stageName + '\'' +
                ", status=" + status +
                '}';
    }
    
    public static class Builder {
        private UUID stageId;
        private StageType stageType;
        private String stageName;
        private StageStatus status;

        public Builder setStageId(UUID stageId) {
            this.stageId = stageId;
            return this;
        }

        public Builder setStageType(StageType stageType) {
            this.stageType = stageType;
            return this;
        }

        public Builder setName(String stageName) {
            this.stageName = stageName;
            return this;
        }

        public Builder setStatus(StageStatus status) {
            this.status = status;
            return this;
        }

        public Builder copy(Stage stage) {
            this.stageId = stage.getStageId();
            this.stageType = stage.getStageType();
            this.stageName = stage.getStageName();
            this.status = stage.getStageStatus();
            return this;
        }

        public Stage build(){
            Objects.requireNonNull(stageType, "stageType is required");
            if (stageName == null || stageName.isBlank()) {
                throw new IllegalArgumentException("stageName must not be blank");
            }
            return new Stage(this);
        }
    }

}
