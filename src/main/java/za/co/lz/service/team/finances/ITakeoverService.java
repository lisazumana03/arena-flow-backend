package za.co.lz.service.team.finances;

import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.finances.Owner;
import za.co.lz.domain.team.finances.TakeoverStatus;
import za.co.lz.service.IService;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Service for handling takeovers, ownership changes, and forced sales.
 */
public interface ITakeoverService{
    
    // Takeover mechanics
    Owner buyClub(Owner buyer, Team team, BigDecimal purchasePrice);
    
    // Forced sale due to financial distress
    boolean evaluateForcedSale(Team team);
    Owner forcedSale(Team team, Owner newOwner, BigDecimal pricepoint);
    
    // Owner intervention
    boolean requiresOwnerIntervention(Team team);
    Owner injectionOfFunds(Team team, BigDecimal injectionAmount);
    
    // Budget reduction (penalty for poor performance/debt)
    void applyBudgetReduction(Team team, double reductionPercentage);
    
    // Owner bankruptcy
    boolean isOwnerBankrupt(Owner owner);
    void handleOwnerBankruptcy(Owner owner);
}
