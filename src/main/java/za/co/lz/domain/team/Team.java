package za.co.lz.domain.team;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.UUID;

@Entity
public class Team implements Serializable {
    @Id
    private UUID teamId;
    private String teamName;
    private int teamFormationYear;
    private TeamType teamType;
    // Inserting a team logo from device
    private byte[] teamLogo;

    public Team(){}

    private Team(Builder builder) {
        this.teamId = builder.teamId;
        this.teamName = builder.teamName;
        this.teamFormationYear = builder.teamFormationYear;
        this.teamType = builder.teamType;
        this.teamLogo = builder.teamLogo;
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

    public byte[] getTeamLogo() {
        return teamLogo;
    }

    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                ", teamFormationYear=" + teamFormationYear +
                ", teamType=" + teamType +
                ", teamLogo='" + teamLogo + '\'' +
                '}';
    }

    public static class Builder{
        private UUID teamId;
        private String teamName;
        private int teamFormationYear;
        private TeamType teamType;
        private byte[] teamLogo;
        
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

        public Builder setTeamLogo(byte[] teamLogo) {
            this.teamLogo = teamLogo;
            return this;
        }

        public Builder copy(Team team){
            this.teamId = team.teamId;
            this.teamName = team.teamName;
            this.teamFormationYear = team.teamFormationYear;
            this.teamType = team.teamType;
            this.teamLogo = team.teamLogo;
            return this;
        }

        public Team build(){
            return new Team(this);
        }
    }
}
