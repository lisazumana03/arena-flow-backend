package za.co.lz.service.team.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.finances.Owner;
import za.co.lz.repository.team.finances.OwnerRepository;
import za.co.lz.service.team.finances.ITakeoverService;
import za.co.lz.repository.team.TeamRepository;

import java.math.BigDecimal;

@Service
public class TakeoverServiceImpl implements ITakeoverService {
    
    @Autowired
    private OwnerRepository ownerRepository;
    
    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private FinancialServiceImpl financialService;
    
    @Autowired
    private BudgetServiceImpl budgetService;
        
    @Override
    public Owner buyClub(Owner buyer, Team team, BigDecimal purchasePrice) {
        // Validate buyer has funds
        if (buyer.getAvailableFunds().compareTo(purchasePrice) < 0) {
            throw new IllegalArgumentException("Buyer has insufficient funds. Available: " 
                    + buyer.getAvailableFunds() + ", Required: " + purchasePrice);
        }
        
        Owner previousOwner = team.getOwner();
        
        // Transfer ownership
        team.setOwner(buyer);
        buyer.getOwnedTeams().add(team);
        
        // Deduct purchase price from buyer
        buyer.setAvailableFunds(buyer.getAvailableFunds().subtract(purchasePrice));
        
        // Add proceeds to seller (if exists)
        if (previousOwner != null) {
            previousOwner.getOwnedTeams().remove(team);
            previousOwner.setAvailableFunds(previousOwner.getAvailableFunds().add(purchasePrice));
            ownerRepository.save(previousOwner);
        }
        
        // Save changes
        teamRepository.save(team);
        return ownerRepository.save(buyer);
    }
    
    @Override
    public boolean evaluateForcedSale(Team team) {
        Owner owner = team.getOwner();
        if (owner == null) {
            return false;
        }
        
        // Check if owner is bankrupt or team is in severe distress
        // Criteria:
        // 1. Owner's available funds < 0
        // 2. Debt > 3x annual revenue
        // 3. 3+ consecutive losses
        
        if (owner.getAvailableFunds().compareTo(BigDecimal.ZERO) < 0) {
            return true;
        }
        
        // Check financial health
        try {
            var financials = financialService.findByTeam(team);
            if (!financials.isEmpty()) {
                var latest = financials.get(financials.size() - 1);
                if (latest.getDebtToRevenueRatio() > 3.0) {
                    return true;
                }
                if (latest.getConsecutiveLosses() >= 3) {
                    return true;
                }
            }
        } catch (Exception e) {
            // If no financials exist, can't evaluate
        }
        
        return false;
    }
    
    @Override
    public Owner forcedSale(Team team, Owner newOwner, BigDecimal pricepoint) {
        Owner currentOwner = team.getOwner();
        
        if (currentOwner == null) {
            throw new IllegalArgumentException("Team has no current owner");
        }
        
        if (!evaluateForcedSale(team)) {
            throw new IllegalArgumentException("Team does not meet forced sale criteria");
        }
        
        // Execute forced sale at reduced price
        return buyClub(newOwner, team, pricepoint);
    }
    
    @Override
    public boolean requiresOwnerIntervention(Team team) {
        try {
            var financials = financialService.findByTeam(team);
            if (!financials.isEmpty()) {
                var latest = financials.get(financials.size() - 1);
                return latest.isRequiresIntervention();
            }
        } catch (Exception e) {
            // If no financials, no intervention needed
        }
        return false;
    }
    
    @Override
    public Owner injectionOfFunds(Team team, BigDecimal injectionAmount) {
        Owner owner = team.getOwner();
        
        if (owner == null) {
            throw new IllegalArgumentException("Team has no owner");
        }
        
        // Add injection to available funds (simulating owner investment)
        owner.setAvailableFunds(owner.getAvailableFunds().add(injectionAmount));
        
        // Reduce debt
        var financials = financialService.findByTeam(team);
        if (!financials.isEmpty()) {
            var latest = financials.get(financials.size() - 1);
            financialService.reduceDebt(latest.getFinancialId(), injectionAmount);
        }
        
        return ownerRepository.save(owner);
    }
    
    @Override
    public void applyBudgetReduction(Team team, double reductionPercentage) {
        // This would reduce next year's budget by the specified percentage
        // Implementation would integrate with BudgetService
        // For now, we mark the need for reduction
    }
    
    @Override
    public boolean isOwnerBankrupt(Owner owner) {
        if (owner == null) {
            return false;
        }
        
        // Owner is bankrupt if:
        // 1. Available funds negative and no team income
        // 2. Debt exceeds net worth significantly
        // 3. All teams in financial distress
        
        if (owner.getAvailableFunds().compareTo(BigDecimal.ZERO) < 0) {
            // Check if can recover through team profits
            long healthyTeams = owner.getOwnedTeams().stream()
                    .filter(team -> {
                        try {
                            return !requiresOwnerIntervention(team);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .count();
            
            return healthyTeams == 0;
        }
        
        return false;
    }
    
    @Override
    public void handleOwnerBankruptcy(Owner owner) {
        if (isOwnerBankrupt(owner)) {
            // Transfer all teams to default owner (league/authority)
            // Or mark for automatic sale
            owner.getOwnedTeams().forEach(team -> {
                try {
                    team.setOwner(null);
                    teamRepository.save(team);
                } catch (Exception e) {
                    // Handle error
                }
            });
            
            owner.getOwnedTeams().clear();
            owner.setAvailableFunds(BigDecimal.ZERO);
            ownerRepository.save(owner);
        }
    }
}
