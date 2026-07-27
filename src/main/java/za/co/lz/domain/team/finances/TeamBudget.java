package za.co.lz.domain.team.finances;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import za.co.lz.domain.team.Team;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents the budget allocation for a team for a specific season/year.
 * Budget is controlled by the Owner and influenced by their strategy.
 */
@Entity
public class TeamBudget implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID budgetId;
    
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;
    
    private LocalDate budgetYear;
    
    // Budget allocations
    private BigDecimal transferBudget;
    private BigDecimal wageBudget;
    private BigDecimal operatingBudget;
    private BigDecimal youthAcademyBudget;
    private BigDecimal infrastructureBudget;
    
    // Spending tracking
    private BigDecimal transferSpent = BigDecimal.ZERO;
    private BigDecimal wageSpent = BigDecimal.ZERO;
    private BigDecimal operatingSpent = BigDecimal.ZERO;
    private BigDecimal youthAcademySpent = BigDecimal.ZERO;
    private BigDecimal infrastructureSpent = BigDecimal.ZERO;
    
    // Status
    private BudgetStatus status;
    
    public TeamBudget() {}
    
    private TeamBudget(Builder builder) {
        this.budgetId = builder.budgetId;
        this.team = builder.team;
        this.owner = builder.owner;
        this.budgetYear = builder.budgetYear;
        this.transferBudget = builder.transferBudget;
        this.wageBudget = builder.wageBudget;
        this.operatingBudget = builder.operatingBudget;
        this.youthAcademyBudget = builder.youthAcademyBudget;
        this.infrastructureBudget = builder.infrastructureBudget;
        this.transferSpent = builder.transferSpent;
        this.wageSpent = builder.wageSpent;
        this.operatingSpent = builder.operatingSpent;
        this.youthAcademySpent = builder.youthAcademySpent;
        this.infrastructureSpent = builder.infrastructureSpent;
        this.status = builder.status;
    }
    
    public UUID getBudgetId() {
        return budgetId;
    }
    
    public Team getTeam() {
        return team;
    }
    
    public Owner getOwner() {
        return owner;
    }
    
    public LocalDate getBudgetYear() {
        return budgetYear;
    }
    
    public BigDecimal getTransferBudget() {
        return transferBudget;
    }
    
    public BigDecimal getWageBudget() {
        return wageBudget;
    }
    
    public BigDecimal getOperatingBudget() {
        return operatingBudget;
    }
    
    public BigDecimal getYouthAcademyBudget() {
        return youthAcademyBudget;
    }
    
    public BigDecimal getInfrastructureBudget() {
        return infrastructureBudget;
    }
    
    public BigDecimal getTransferSpent() {
        return transferSpent;
    }
    
    public BigDecimal getWageSpent() {
        return wageSpent;
    }
    
    public BigDecimal getOperatingSpent() {
        return operatingSpent;
    }
    
    public BigDecimal getYouthAcademySpent() {
        return youthAcademySpent;
    }
    
    public BigDecimal getInfrastructureSpent() {
        return infrastructureSpent;
    }
    
    public BudgetStatus getStatus() {
        return status;
    }
    
    public BigDecimal getTotalBudget() {
        return transferBudget.add(wageBudget).add(operatingBudget)
                .add(youthAcademyBudget).add(infrastructureBudget);
    }
    
    public BigDecimal getTotalSpent() {
        return transferSpent.add(wageSpent).add(operatingSpent)
                .add(youthAcademySpent).add(infrastructureSpent);
    }
    
    public BigDecimal getRemainingBudget() {
        return getTotalBudget().subtract(getTotalSpent());
    }
    
    // Transfer budget operations
    public boolean canSpendOnTransfers(BigDecimal amount) {
        return transferBudget.subtract(transferSpent).compareTo(amount) >= 0;
    }
    
    public void spendOnTransfers(BigDecimal amount) {
        if (!canSpendOnTransfers(amount)) {
            throw new IllegalArgumentException("Insufficient transfer budget. Available: " 
                    + getAvailableTransferBudget() + ", Requested: " + amount);
        }
        this.transferSpent = transferSpent.add(amount);
    }
    
    public BigDecimal getAvailableTransferBudget() {
        return transferBudget.subtract(transferSpent);
    }
    
    // Wage budget operations
    public boolean canSpendOnWages(BigDecimal amount) {
        return wageBudget.subtract(wageSpent).compareTo(amount) >= 0;
    }
    
    public void spendOnWages(BigDecimal amount) {
        if (!canSpendOnWages(amount)) {
            throw new IllegalArgumentException("Insufficient wage budget. Available: " 
                    + getAvailableWageBudget() + ", Requested: " + amount);
        }
        this.wageSpent = wageSpent.add(amount);
    }
    
    public BigDecimal getAvailableWageBudget() {
        return wageBudget.subtract(wageSpent);
    }
    
    // Academy budget operations
    public boolean canSpendOnAcademy(BigDecimal amount) {
        return youthAcademyBudget.subtract(youthAcademySpent).compareTo(amount) >= 0;
    }
    
    public void spendOnAcademy(BigDecimal amount) {
        if (!canSpendOnAcademy(amount)) {
            throw new IllegalArgumentException("Insufficient academy budget. Available: " 
                    + getAvailableAcademyBudget() + ", Requested: " + amount);
        }
        this.youthAcademySpent = youthAcademySpent.add(amount);
    }
    
    public BigDecimal getAvailableAcademyBudget() {
        return youthAcademyBudget.subtract(youthAcademySpent);
    }
    
    public void setStatus(BudgetStatus status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "TeamBudget{" +
                "budgetId=" + budgetId +
                ", team=" + (team != null ? team.getTeamName() : "null") +
                ", budgetYear=" + budgetYear +
                ", totalBudget=" + getTotalBudget() +
                ", totalSpent=" + getTotalSpent() +
                ", remaining=" + getRemainingBudget() +
                ", status=" + status +
                '}';
    }
    
    public static class Builder {
        private UUID budgetId;
        private Team team;
        private Owner owner;
        private LocalDate budgetYear;
        private BigDecimal transferBudget = BigDecimal.ZERO;
        private BigDecimal wageBudget = BigDecimal.ZERO;
        private BigDecimal operatingBudget = BigDecimal.ZERO;
        private BigDecimal youthAcademyBudget = BigDecimal.ZERO;
        private BigDecimal infrastructureBudget = BigDecimal.ZERO;
        private BigDecimal transferSpent = BigDecimal.ZERO;
        private BigDecimal wageSpent = BigDecimal.ZERO;
        private BigDecimal operatingSpent = BigDecimal.ZERO;
        private BigDecimal youthAcademySpent = BigDecimal.ZERO;
        private BigDecimal infrastructureSpent = BigDecimal.ZERO;
        private BudgetStatus status = BudgetStatus.ACTIVE;
        
        public Builder setBudgetId(UUID budgetId) {
            this.budgetId = budgetId;
            return this;
        }
        
        public Builder setTeam(Team team) {
            this.team = team;
            return this;
        }
        
        public Builder setOwner(Owner owner) {
            this.owner = owner;
            return this;
        }
        
        public Builder setBudgetYear(LocalDate budgetYear) {
            this.budgetYear = budgetYear;
            return this;
        }
        
        public Builder setTransferBudget(BigDecimal transferBudget) {
            this.transferBudget = transferBudget;
            return this;
        }
        
        public Builder setWageBudget(BigDecimal wageBudget) {
            this.wageBudget = wageBudget;
            return this;
        }
        
        public Builder setOperatingBudget(BigDecimal operatingBudget) {
            this.operatingBudget = operatingBudget;
            return this;
        }
        
        public Builder setYouthAcademyBudget(BigDecimal youthAcademyBudget) {
            this.youthAcademyBudget = youthAcademyBudget;
            return this;
        }
        
        public Builder setInfrastructureBudget(BigDecimal infrastructureBudget) {
            this.infrastructureBudget = infrastructureBudget;
            return this;
        }
        
        public Builder setStatus(BudgetStatus status) {
            this.status = status;
            return this;
        }
        
        public Builder copy(TeamBudget budget) {
            this.budgetId = budget.budgetId;
            this.team = budget.team;
            this.owner = budget.owner;
            this.budgetYear = budget.budgetYear;
            this.transferBudget = budget.transferBudget;
            this.wageBudget = budget.wageBudget;
            this.operatingBudget = budget.operatingBudget;
            this.youthAcademyBudget = budget.youthAcademyBudget;
            this.infrastructureBudget = budget.infrastructureBudget;
            this.transferSpent = budget.transferSpent;
            this.wageSpent = budget.wageSpent;
            this.operatingSpent = budget.operatingSpent;
            this.youthAcademySpent = budget.youthAcademySpent;
            this.infrastructureSpent = budget.infrastructureSpent;
            this.status = budget.status;
            return this;
        }
        
        public TeamBudget build() {
            if (team == null) {
                throw new IllegalStateException("Team must be set for budget");
            }
            if (owner == null) {
                throw new IllegalStateException("Owner must be set for budget");
            }
            if (budgetYear == null) {
                throw new IllegalStateException("Budget year must be set");
            }
            return new TeamBudget(this);
        }
    }
}
