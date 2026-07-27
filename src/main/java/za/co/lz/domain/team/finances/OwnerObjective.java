package za.co.lz.domain.team.finances;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import za.co.lz.domain.team.Team;

import java.io.Serializable;
import java.util.UUID;

@Entity
public class OwnerObjective implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID objectiveId;
    
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;
    
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    
    private ObjectiveType objectiveType;
    private int priority; // 1-5, where 5 is highest
    private boolean achieved;
    private int progressPercentage; // 0-100
    
    public OwnerObjective() {}
    
    private OwnerObjective(Builder builder) {
        this.objectiveId = builder.objectiveId;
        this.owner = builder.owner;
        this.team = builder.team;
        this.objectiveType = builder.objectiveType;
        this.priority = builder.priority;
        this.achieved = builder.achieved;
        this.progressPercentage = builder.progressPercentage;
    }
    
    public UUID getObjectiveId() {
        return objectiveId;
    }
    
    public Owner getOwner() {
        return owner;
    }
    
    public Team getTeam() {
        return team;
    }
    
    public ObjectiveType getObjectiveType() {
        return objectiveType;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public boolean isAchieved() {
        return achieved;
    }
    
    public int getProgressPercentage() {
        return progressPercentage;
    }
    
    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = Math.min(Math.max(progressPercentage, 0), 100);
    }
    
    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
        if (achieved) {
            this.progressPercentage = 100;
        }
    }
    
    public void setPriority(int priority) {
        this.priority = Math.min(Math.max(priority, 1), 5);
    }
    
    @Override
    public String toString() {
        return "OwnerObjective{" +
                "objectiveId=" + objectiveId +
                ", objectiveType=" + objectiveType +
                ", team=" + (team != null ? team.getTeamName() : "null") +
                ", priority=" + priority +
                ", achieved=" + achieved +
                ", progress=" + progressPercentage + "%" +
                '}';
    }
    
    public static class Builder {
        private UUID objectiveId;
        private Owner owner;
        private Team team;
        private ObjectiveType objectiveType;
        private int priority = 3;
        private boolean achieved = false;
        private int progressPercentage = 0;
        
        public Builder setObjectiveId(UUID objectiveId) {
            this.objectiveId = objectiveId;
            return this;
        }
        
        public Builder setOwner(Owner owner) {
            this.owner = owner;
            return this;
        }
        
        public Builder setTeam(Team team) {
            this.team = team;
            return this;
        }
        
        public Builder setObjectiveType(ObjectiveType objectiveType) {
            this.objectiveType = objectiveType;
            return this;
        }
        
        public Builder setPriority(int priority) {
            this.priority = Math.min(Math.max(priority, 1), 5);
            return this;
        }
        
        public Builder setAchieved(boolean achieved) {
            this.achieved = achieved;
            return this;
        }
        
        public Builder setProgressPercentage(int progressPercentage) {
            this.progressPercentage = Math.min(Math.max(progressPercentage, 0), 100);
            return this;
        }
        
        public Builder copy(OwnerObjective objective) {
            this.objectiveId = objective.objectiveId;
            this.owner = objective.owner;
            this.team = objective.team;
            this.objectiveType = objective.objectiveType;
            this.priority = objective.priority;
            this.achieved = objective.achieved;
            this.progressPercentage = objective.progressPercentage;
            return this;
        }
        
        public OwnerObjective build() {
            if (owner == null) {
                throw new IllegalStateException("Owner must be set for an objective");
            }
            if (team == null) {
                throw new IllegalStateException("Team must be set for an objective");
            }
            if (objectiveType == null) {
                throw new IllegalStateException("Objective type must be set");
            }
            return new OwnerObjective(this);
        }
    }
}
