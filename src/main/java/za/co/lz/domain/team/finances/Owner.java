package za.co.lz.domain.team.finances;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import za.co.lz.domain.Name;
import za.co.lz.domain.team.Team;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
public class Owner {
    @Id
    private UUID ownerId;
    @Embedded
    private Name ownerName;
    private LocalDate birthDate;
    private String ownerNationality;
    @OneToMany
    private List<Team> ownedTeams;

    public Owner(){}

    private Owner(Builder builder) {
        this.ownerId = builder.ownerId;
        this.ownerName = builder.ownerName;
        this.birthDate = builder.birthDate;
        this.ownerNationality = builder.ownerNationality;
        this.ownedTeams = builder.ownedTeams;
    }

    public static class Builder {
        private UUID ownerId;
        private Name ownerName;
        private LocalDate birthDate;
        private String ownerNationality;
        private List<Team> ownedTeams;

        public Builder setOwnerId(UUID ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public Builder setOwnerName(Name ownerName) {
            this.ownerName = ownerName;
            return this;
        }

        public Builder setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder setOwnerNationality(String ownerNationality) {
            this.ownerNationality = ownerNationality;
            return this;
        }

        public Builder setOwnedTeams(List<Team> ownedTeams) {
            this.ownedTeams = ownedTeams;
            return this;
        }
    }
}
