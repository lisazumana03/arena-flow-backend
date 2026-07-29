package za.co.lz.domain.team.finances;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * Tracks annual financial performance and health of a team.
 * Records revenue, expenses, profit/loss, and debt.
 */
@Entity
public class ClubFinancials implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID financialId;
    
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;
    
    private LocalDate financialYear;
    
    // Revenue
    private BigDecimal ticketRevenue = BigDecimal.ZERO;
    private BigDecimal sponsorshipRevenue = BigDecimal.ZERO;
    private BigDecimal merchandiseRevenue = BigDecimal.ZERO;
    private BigDecimal mediaRights = BigDecimal.ZERO;
    private BigDecimal otherRevenue = BigDecimal.ZERO;
    
    // Expenses
    private BigDecimal playerWages = BigDecimal.ZERO;
    private BigDecimal staffCosts = BigDecimal.ZERO;
    private BigDecimal operatingCosts = BigDecimal.ZERO;
    private BigDecimal depreciationCosts = BigDecimal.ZERO;
    private BigDecimal otherExpenses = BigDecimal.ZERO;
    
    // Financial metrics
    private BigDecimal debt = BigDecimal.ZERO;
    private BigDecimal netWorth = BigDecimal.ZERO;
    private int consecutiveLosses = 0;
    
    @Enumerated(EnumType.STRING)
    private FinancialHealth healthStatus;
    
    private boolean requiresIntervention = false;
    private boolean isTakeoverCandidate = false;
    
    public ClubFinancials() {}
    
    private ClubFinancials(Builder builder) {
        this.financialId = builder.financialId;
        this.team = builder.team;
        this.owner = builder.owner;
        this.financialYear = builder.financialYear;
        this.ticketRevenue = builder.ticketRevenue;
        this.sponsorshipRevenue = builder.sponsorshipRevenue;
        this.merchandiseRevenue = builder.merchandiseRevenue;
        this.mediaRights = builder.mediaRights;
        this.otherRevenue = builder.otherRevenue;
        this.playerWages = builder.playerWages;
        this.staffCosts = builder.staffCosts;
        this.operatingCosts = builder.operatingCosts;
        this.depreciationCosts = builder.depreciationCosts;
        this.otherExpenses = builder.otherExpenses;
        this.debt = builder.debt;
        this.netWorth = builder.netWorth;
        this.consecutiveLosses = builder.consecutiveLosses;
        this.healthStatus = builder.healthStatus;
        this.requiresIntervention = builder.requiresIntervention;
        this.isTakeoverCandidate = builder.isTakeoverCandidate;
    }
    
    // Getters
    public UUID getFinancialId() { return financialId; }
    public Team getTeam() { return team; }
    public Owner getOwner() { return owner; }
    public LocalDate getFinancialYear() { return financialYear; }
    
    public BigDecimal getTicketRevenue() { return ticketRevenue; }
    public BigDecimal getSponsorshipRevenue() { return sponsorshipRevenue; }
    public BigDecimal getMerchandiseRevenue() { return merchandiseRevenue; }
    public BigDecimal getMediaRights() { return mediaRights; }
    public BigDecimal getOtherRevenue() { return otherRevenue; }
    
    public BigDecimal getPlayerWages() { return playerWages; }
    public BigDecimal getStaffCosts() { return staffCosts; }
    public BigDecimal getOperatingCosts() { return operatingCosts; }
    public BigDecimal getDepreciationCosts() { return depreciationCosts; }
    public BigDecimal getOtherExpenses() { return otherExpenses; }
    
    public BigDecimal getDebt() { return debt; }
    public BigDecimal getNetWorth() { return netWorth; }
    public int getConsecutiveLosses() { return consecutiveLosses; }
    
    public FinancialHealth getHealthStatus() { return healthStatus; }
    public boolean isRequiresIntervention() { return requiresIntervention; }
    public boolean isTakeoverCandidate() { return isTakeoverCandidate; }
    
    // Revenue calculations
    public BigDecimal getTotalRevenue() {
        return ticketRevenue.add(sponsorshipRevenue).add(merchandiseRevenue)
                .add(mediaRights).add(otherRevenue);
    }
    
    // Expense calculations
    public BigDecimal getTotalExpenses() {
        return playerWages.add(staffCosts).add(operatingCosts)
                .add(depreciationCosts).add(otherExpenses);
    }
    
    // Profit/Loss
    public BigDecimal getProfitOrLoss() {
        return getTotalRevenue().subtract(getTotalExpenses());
    }
    
    // Operating Margin
    public double getOperatingMargin() {
        BigDecimal revenue = getTotalRevenue();
        if (revenue.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return (getProfitOrLoss().doubleValue() / revenue.doubleValue()) * 100;
    }
    
    // Debt to Revenue ratio
    public double getDebtToRevenueRatio() {
        BigDecimal revenue = getTotalRevenue();
        if (revenue.compareTo(BigDecimal.ZERO) == 0) {
            return debt.compareTo(BigDecimal.ZERO) == 0 ? 0 : Double.MAX_VALUE;
        }
        return debt.doubleValue() / revenue.doubleValue();
    }
    
    // Setters
    public void setTicketRevenue(BigDecimal ticketRevenue) { 
        this.ticketRevenue = ticketRevenue; 
    }
    public void setSponsorshipRevenue(BigDecimal sponsorshipRevenue) { 
        this.sponsorshipRevenue = sponsorshipRevenue; 
    }
    public void setMerchandiseRevenue(BigDecimal merchandiseRevenue) { 
        this.merchandiseRevenue = merchandiseRevenue; 
    }
    public void setMediaRights(BigDecimal mediaRights) { 
        this.mediaRights = mediaRights; 
    }
    public void setOtherRevenue(BigDecimal otherRevenue) { 
        this.otherRevenue = otherRevenue; 
    }
    
    public void setPlayerWages(BigDecimal playerWages) { 
        this.playerWages = playerWages; 
    }
    public void setStaffCosts(BigDecimal staffCosts) { 
        this.staffCosts = staffCosts; 
    }
    public void setOperatingCosts(BigDecimal operatingCosts) { 
        this.operatingCosts = operatingCosts; 
    }
    public void setDepreciationCosts(BigDecimal depreciationCosts) { 
        this.depreciationCosts = depreciationCosts; 
    }
    public void setOtherExpenses(BigDecimal otherExpenses) { 
        this.otherExpenses = otherExpenses; 
    }
    
    public void setDebt(BigDecimal debt) { 
        this.debt = debt; 
    }
    public void setNetWorth(BigDecimal netWorth) { 
        this.netWorth = netWorth; 
    }
    public void setConsecutiveLosses(int consecutiveLosses) { 
        this.consecutiveLosses = consecutiveLosses; 
    }
    public void setHealthStatus(FinancialHealth healthStatus) { 
        this.healthStatus = healthStatus; 
    }
    public void setRequiresIntervention(boolean requiresIntervention) { 
        this.requiresIntervention = requiresIntervention; 
    }
    public void setTakeoverCandidate(boolean takeoverCandidate) { 
        this.isTakeoverCandidate = takeoverCandidate; 
    }
    
    @Override
    public String toString() {
        return "ClubFinancials{" +
                "team=" + (team != null ? team.getTeamName() : "null") +
                ", year=" + financialYear.getYear() +
                ", revenue=" + getTotalRevenue() +
                ", expenses=" + getTotalExpenses() +
                ", P&L=" + getProfitOrLoss() +
                ", debt=" + debt +
                ", health=" + healthStatus +
                '}';
    }
    
    public static class Builder {
        private UUID financialId;
        private Team team;
        private Owner owner;
        private LocalDate financialYear;
        private BigDecimal ticketRevenue = BigDecimal.ZERO;
        private BigDecimal sponsorshipRevenue = BigDecimal.ZERO;
        private BigDecimal merchandiseRevenue = BigDecimal.ZERO;
        private BigDecimal mediaRights = BigDecimal.ZERO;
        private BigDecimal otherRevenue = BigDecimal.ZERO;
        private BigDecimal playerWages = BigDecimal.ZERO;
        private BigDecimal staffCosts = BigDecimal.ZERO;
        private BigDecimal operatingCosts = BigDecimal.ZERO;
        private BigDecimal depreciationCosts = BigDecimal.ZERO;
        private BigDecimal otherExpenses = BigDecimal.ZERO;
        private BigDecimal debt = BigDecimal.ZERO;
        private BigDecimal netWorth = BigDecimal.ZERO;
        private int consecutiveLosses = 0;
        private FinancialHealth healthStatus = FinancialHealth.HEALTHY;
        private boolean requiresIntervention = false;
        private boolean isTakeoverCandidate = false;
        
        public Builder setFinancialId(UUID financialId) { this.financialId = financialId; return this; }
        public Builder setTeam(Team team) { this.team = team; return this; }
        public Builder setOwner(Owner owner) { this.owner = owner; return this; }
        public Builder setFinancialYear(LocalDate financialYear) { this.financialYear = financialYear; return this; }
        public Builder setTicketRevenue(BigDecimal ticketRevenue) { this.ticketRevenue = ticketRevenue; return this; }
        public Builder setSponsorshipRevenue(BigDecimal sponsorshipRevenue) { this.sponsorshipRevenue = sponsorshipRevenue; return this; }
        public Builder setMerchandiseRevenue(BigDecimal merchandiseRevenue) { this.merchandiseRevenue = merchandiseRevenue; return this; }
        public Builder setMediaRights(BigDecimal mediaRights) { this.mediaRights = mediaRights; return this; }
        public Builder setOtherRevenue(BigDecimal otherRevenue) { this.otherRevenue = otherRevenue; return this; }
        public Builder setPlayerWages(BigDecimal playerWages) { this.playerWages = playerWages; return this; }
        public Builder setStaffCosts(BigDecimal staffCosts) { this.staffCosts = staffCosts; return this; }
        public Builder setOperatingCosts(BigDecimal operatingCosts) { this.operatingCosts = operatingCosts; return this; }
        public Builder setDepreciationCosts(BigDecimal depreciationCosts) { this.depreciationCosts = depreciationCosts; return this; }
        public Builder setOtherExpenses(BigDecimal otherExpenses) { this.otherExpenses = otherExpenses; return this; }
        public Builder setDebt(BigDecimal debt) { this.debt = debt; return this; }
        public Builder setNetWorth(BigDecimal netWorth) { this.netWorth = netWorth; return this; }
        public Builder setConsecutiveLosses(int consecutiveLosses) { this.consecutiveLosses = consecutiveLosses; return this; }
        public Builder setHealthStatus(FinancialHealth healthStatus) { this.healthStatus = healthStatus; return this; }
        public Builder setRequiresIntervention(boolean requiresIntervention) { this.requiresIntervention = requiresIntervention; return this; }
        public Builder setTakeoverCandidate(boolean isTakeoverCandidate) { this.isTakeoverCandidate = isTakeoverCandidate; return this; }
        
        public Builder copy(ClubFinancials financials) {
            this.financialId = financials.financialId;
            this.team = financials.team;
            this.owner = financials.owner;
            this.financialYear = financials.financialYear;
            this.ticketRevenue = financials.ticketRevenue;
            this.sponsorshipRevenue = financials.sponsorshipRevenue;
            this.merchandiseRevenue = financials.merchandiseRevenue;
            this.mediaRights = financials.mediaRights;
            this.otherRevenue = financials.otherRevenue;
            this.playerWages = financials.playerWages;
            this.staffCosts = financials.staffCosts;
            this.operatingCosts = financials.operatingCosts;
            this.depreciationCosts = financials.depreciationCosts;
            this.otherExpenses = financials.otherExpenses;
            this.debt = financials.debt;
            this.netWorth = financials.netWorth;
            this.consecutiveLosses = financials.consecutiveLosses;
            this.healthStatus = financials.healthStatus;
            this.requiresIntervention = financials.requiresIntervention;
            this.isTakeoverCandidate = financials.isTakeoverCandidate;
            return this;
        }
        
        public ClubFinancials build() {
            if (team == null) throw new IllegalStateException("Team must be set");
            if (owner == null) throw new IllegalStateException("Owner must be set");
            if (financialYear == null) throw new IllegalStateException("Financial year must be set");
            return new ClubFinancials(this);
        }
    }
}
