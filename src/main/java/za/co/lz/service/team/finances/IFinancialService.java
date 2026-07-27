package za.co.lz.service.team.finances;

import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.finances.ClubFinancials;
import za.co.lz.domain.team.finances.FinancialHealth;
import za.co.lz.domain.team.finances.Owner;
import za.co.lz.service.IService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IFinancialService extends IService<ClubFinancials, UUID> {
    
    // Create annual financials
    ClubFinancials createAnnualFinancials(Team team, Owner owner, LocalDate year);
    
    // Find financials
    ClubFinancials findByTeamAndYear(Team team, LocalDate year);
    List<ClubFinancials> findByTeam(Team team);
    List<ClubFinancials> findByOwner(Owner owner);
    
    // Evaluate financial health
    FinancialHealth evaluateHealth(UUID financialId);
    FinancialHealth evaluateHealthByMetrics(BigDecimal revenue, BigDecimal expenses, BigDecimal debt);
    
    // Trigger checks
    boolean requiresIntervention(UUID financialId);
    boolean isTakeoverCandidate(UUID financialId);
    
    // Financial operations
    ClubFinancials recordLoss(UUID financialId);
    ClubFinancials recordProfit(UUID financialId);
    ClubFinancials addDebt(UUID financialId, BigDecimal debtAmount);
    ClubFinancials reduceDebt(UUID financialId, BigDecimal debtAmount);
    
    // Penalty operations
    ClubFinancials applyRelegationPenalty(UUID financialId, BigDecimal penaltyAmount);
    ClubFinancials freezeBudgetForFFP(UUID financialId);
    
    // List problematic clubs
    List<ClubFinancials> findTakeovers();
    List<ClubFinancials> findInIntervention();
}
