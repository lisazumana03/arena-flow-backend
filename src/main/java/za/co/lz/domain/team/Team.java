package za.co.lz.domain.team;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import za.co.lz.domain.team.finances.Owner;

import java.io.Serializable;
import java.util.UUID;

@Entity
public class Team implements Serializable {
    @Id
    private UUID teamId;
    private String teamName;
    private int teamFormationYear;
    private TeamType teamType;
    // Male/female side the team competes in
    private PlayerGender teamGender;
    // Whether the team runs a youth academy
    private boolean hasYouthAcademy;
    // Country the team represents/is based in (e.g. "England")
    private String teamNationality;
    // Inserting a team logo from device
    private byte[] teamLogo;
    // Ownership attributes
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    public Team(){}

    private Team(Builder builder) {
        this.teamId = builder.teamId;
        this.teamName = builder.teamName;
        this.teamFormationYear = builder.teamFormationYear;
        this.teamType = builder.teamType;
        this.teamGender = builder.teamGender;
        this.hasYouthAcademy = builder.hasYouthAcademy;
        this.teamNationality = builder.teamNationality;
        this.teamLogo = builder.teamLogo;
        this.owner = builder.owner;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getTeamFormationYear() {
        return teamFormationYear;
    }

    public TeamType getTeamType() {
        return teamType;
    }

    public PlayerGender getTeamGender() {
        return teamGender;
    }

    public boolean isHasYouthAcademy() {
        return hasYouthAcademy;
    }

    public String getTeamNationality() {
        return teamNationality;
    }

    public void setTeamGender(PlayerGender teamGender) {
        this.teamGender = teamGender;
    }

    public void setHasYouthAcademy(boolean hasYouthAcademy) {
        this.hasYouthAcademy = hasYouthAcademy;
    }

    public void setTeamNationality(String teamNationality) {
        this.teamNationality = teamNationality;
    }

    public byte[] getTeamLogo() {
        return teamLogo;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                ", teamFormationYear=" + teamFormationYear +
                ", teamType=" + teamType +
                ", teamGender=" + teamGender +
                ", hasYouthAcademy=" + hasYouthAcademy +
                ", teamNationality='" + teamNationality + '\'' +
                ", owner=" + (owner != null ? owner.getOwnerId() : "null") +
                '}';
    }

    public static class Builder{
        private UUID teamId;
        private String teamName;
        private int teamFormationYear;
        private TeamType teamType;
        private PlayerGender teamGender;
        private boolean hasYouthAcademy;
        private String teamNationality;
        private byte[] teamLogo;
        private Owner owner;
        
        public Builder setTeamId(UUID teamId) {
            this.teamId = teamId;
            return this;
        }

        public Builder setTeamName(String teamName) {
            this.teamName = teamName;
            return this;
        }

        public Builder setTeamFormationYear(int teamFormationYear) {
            this.teamFormationYear = teamFormationYear;
            return this;
        }

        public Builder setTeamType(TeamType teamType) {
            this.teamType = teamType;
            return this;
        }

        public Builder setTeamGender(PlayerGender teamGender) {
            this.teamGender = teamGender;
            return this;
        }

        public Builder setHasYouthAcademy(boolean hasYouthAcademy) {
            this.hasYouthAcademy = hasYouthAcademy;
            return this;
        }

        public Builder setTeamNationality(String teamNationality) {
            this.teamNationality = teamNationality;
            return this;
        }

        public Builder setTeamLogo(byte[] teamLogo) {
            this.teamLogo = teamLogo;
            return this;
        }

        public Builder setOwner(Owner owner) {
            this.owner = owner;
            return this;
        }

        public Builder copy(Team team){
            this.teamId = team.teamId;
            this.teamName = team.teamName;
            this.teamFormationYear = team.teamFormationYear;
            this.teamType = team.teamType;
            this.teamGender = team.teamGender;
            this.hasYouthAcademy = team.hasYouthAcademy;
            this.teamNationality = team.teamNationality;
            this.teamLogo = team.teamLogo;
            this.owner = team.owner;
            return this;
        }

        public Team build(){
            if (owner == null) {
                throw new IllegalStateException("Team must have an owner. Use setOwner() to assign an owner.");
            }
            return new Team(this);
        }
    }
}
