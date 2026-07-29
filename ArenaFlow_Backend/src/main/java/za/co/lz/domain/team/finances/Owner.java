package za.co.lz.domain.team.finances;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import za.co.lz.domain.Name;
import za.co.lz.domain.team.Team;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
    @Enumerated(EnumType.STRING)
    private OwnershipType ownershipType;
    @Enumerated(EnumType.STRING)
    private OwnerStrategy strategy;
    // Back-references are JsonIgnore'd so Team -> Owner -> ownedTeams/ownerships -> Team
    // does not recurse infinitely when an entity is serialized to JSON.
    @JsonIgnore
    @OneToMany(mappedBy="owner")
    private List<Team> ownedTeams = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy="owner")
    private List<Ownership> ownerships = new ArrayList<>();
    private BigDecimal netWorth;
    private BigDecimal availableFunds;
    private BigDecimal investmentBudget;
    private int reputation; //up to 100

    public Owner(){}

    private Owner(Builder builder) {
        this.ownerId = builder.ownerId;
        this.ownerName = builder.ownerName;
        this.birthDate = builder.birthDate;
        this.ownerNationality = builder.ownerNationality;
        this.ownershipType = builder.ownershipType;
        this.strategy = builder.strategy;
        this.ownedTeams = builder.ownedTeams != null ? builder.ownedTeams : new ArrayList<>();
        this.ownerships = builder.ownerships != null ? builder.ownerships : new ArrayList<>();
        this.netWorth = builder.netWorth;
        this.availableFunds = builder.availableFunds;
        this.investmentBudget = builder.investmentBudget;
        this.reputation = builder.reputation;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public Name getOwnerName() {
        return ownerName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getOwnerNationality() {
        return ownerNationality;
    }

    public OwnershipType getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(OwnershipType ownershipType) {
        this.ownershipType = ownershipType;
    }

    public OwnerStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(OwnerStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Team> getOwnedTeams() {
        return ownedTeams;
    }

    public List<Ownership> getOwnerships() {
        return ownerships;
    }

    public BigDecimal getNetWorth() {
        return netWorth;
    }

    public BigDecimal getAvailableFunds() {
        return availableFunds;
    }

    public BigDecimal getInvestmentBudget() {
        return investmentBudget;
    }

    public int getReputation() {
        return reputation;
}

    public void setAvailableFunds(BigDecimal availableFunds) {
        this.availableFunds = availableFunds;
    }

    public void setInvestmentBudget(BigDecimal investmentBudget) {
        this.investmentBudget = investmentBudget;
    }

    public void setReputation(int reputation) {
        this.reputation = Math.min(reputation, 100);
    }

    @Override
    public String toString() {
        return "Owner{" +
                "ownerId=" + ownerId +
                ", ownerName=" + ownerName +
                ", birthDate=" + birthDate +
                ", ownerNationality='" + ownerNationality + '\'' +
                ", ownershipType=" + ownershipType +
                ", strategy=" + strategy +
                ", ownedTeams=" + ownedTeams.size() +
                ", ownerships=" + ownerships +
                ", netWorth=" + netWorth +
                ", availableFunds=" + availableFunds +
                ", investmentBudget=" + investmentBudget +
                ", reputation=" + reputation +
                '}';
    }

    public static class Builder {
        private UUID ownerId;
        private Name ownerName;
        private LocalDate birthDate;
        private String ownerNationality;
        private OwnershipType ownershipType;
        private OwnerStrategy strategy;
        private List<Team> ownedTeams;
        private List<Ownership> ownerships;
        private BigDecimal netWorth;
        private BigDecimal availableFunds;
        private BigDecimal investmentBudget;
        private int reputation;

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

        public Builder setOwnershipType(OwnershipType ownershipType) {
            this.ownershipType = ownershipType;
            return this;
        }

        public Builder setStrategy(OwnerStrategy strategy) {
            this.strategy = strategy;
            return this;
        }

        public Builder setOwnedTeams(List<Team> ownedTeams) {
            this.ownedTeams = ownedTeams;
            return this;
        }

        public Builder setOwnerships(List<Ownership> ownerships) {
            this.ownerships = ownerships;
            return this;
        }

        public Builder setNetWorth(BigDecimal netWorth) {
            this.netWorth = netWorth;
            return this;
        }

        public Builder setAvailableFunds(BigDecimal availableFunds) {
            this.availableFunds = availableFunds;
            return this;
        }

        public Builder setInvestmentBudget(BigDecimal investmentBudget) {
            this.investmentBudget = investmentBudget;
            return this;
        }

        public Builder setReputation(int reputation) {
            this.reputation = reputation;
            return this;
        }

        public Builder copy(Owner owner) {
            this.ownerId = owner.ownerId;
            this.ownerName = owner.ownerName;
            this.birthDate = owner.birthDate;
            this.ownerNationality = owner.ownerNationality;
            this.ownershipType = owner.ownershipType;
            this.strategy = owner.strategy;
            this.ownedTeams = owner.ownedTeams;
            this.ownerships = owner.ownerships;
            this.netWorth = owner.netWorth;
            this.availableFunds = owner.availableFunds;
            this.investmentBudget = owner.investmentBudget;
            this.reputation = owner.reputation;
            return this;
        }

        public Owner build() {
            return new Owner(this);
        }
    }
}
