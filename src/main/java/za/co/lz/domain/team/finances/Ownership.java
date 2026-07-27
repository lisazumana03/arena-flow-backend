package za.co.lz.domain.team.finances;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import za.co.lz.domain.team.Team;

@Entity
public class Ownership {
    @Id
    private UUID ownershipId;
    @ManyToOne
    private Owner owner;
    @ManyToOne
    private Team team;
    private BigDecimal ownershipPercentage;
    private BigDecimal purchasePrice;
    private LocalDate purchaseDate;
    private boolean majorityOwner;
    private boolean minorityOwner;

    public Ownership() {
    }

    private Ownership(Builder builder) {
        this.ownershipId = builder.ownershipId;
        this.owner = builder.owner;
        this.team = builder.team;
        this.ownershipPercentage = builder.ownershipPercentage;
        this.purchasePrice = builder.purchasePrice;
        this.purchaseDate = builder.purchaseDate;
        this.majorityOwner = builder.majorityOwner;
        this.minorityOwner = builder.minorityOwner;
    }

    public UUID getOwnershipId() {
        return ownershipId;
    }

    public Owner getOwner() {
        return owner;
    }

    public Team getTeam() {
        return team;
    }

    public BigDecimal getOwnershipPercentage() {
        return ownershipPercentage;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public boolean isMajorityOwner() {
        return majorityOwner;
    }

    public boolean isMinorityOwner() {
        return minorityOwner;
    }

    public String toString() {
        return "Ownership{" +
                "ownershipId=" + ownershipId +
                ", owner=" + owner +
                ", team=" + team +
                ", ownershipPercentage=" + ownershipPercentage +
                ", purchasePrice=" + purchasePrice +
                ", purchaseDate=" + purchaseDate +
                ", majorityOwner=" + majorityOwner +
                ", minorityOwner=" + minorityOwner +
                '}';
    }

    public static class Builder{
        private UUID ownershipId;
        private Owner owner;
        private Team team;
        private BigDecimal ownershipPercentage;
        private BigDecimal purchasePrice;
        private LocalDate purchaseDate;
        private boolean majorityOwner;
        private boolean minorityOwner;

        public Builder setOwnershipId(UUID ownershipId) {
            this.ownershipId = ownershipId;
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

        public Builder setOwnershipPercentage(BigDecimal ownershipPercentage) {
            this.ownershipPercentage = ownershipPercentage;
            return this;
        }

        public Builder setPurchasePrice(BigDecimal purchasePrice) {
            this.purchasePrice = purchasePrice;
            return this;
        }

        public Builder setPurchaseDate(LocalDate purchaseDate) {
            this.purchaseDate = purchaseDate;
            return this;
        }

        public Builder setMajorityOwner(boolean majorityOwner) {
            this.majorityOwner = majorityOwner;
            return this;
        }

        public Builder setMinorityOwner(boolean minorityOwner) {
            this.minorityOwner = minorityOwner;
            return this;
        }

        public Builder copy(Ownership ownership) {
            this.ownershipId = ownership.ownershipId;
            this.owner = ownership.owner;
            this.team = ownership.team;
            this.ownershipPercentage = ownership.ownershipPercentage;
            this.purchasePrice = ownership.purchasePrice;
            this.purchaseDate = ownership.purchaseDate;
            this.majorityOwner = ownership.majorityOwner;
            this.minorityOwner = ownership.isMinorityOwner();
            return this;
        }
    }
}
