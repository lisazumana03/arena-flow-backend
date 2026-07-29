package za.co.lz.service.team.finances;

import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.finances.Owner;
import za.co.lz.domain.team.finances.TeamBudget;
import za.co.lz.service.IService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IBudgetService extends IService<TeamBudget, UUID> {
    
    // Create budget for a team based on owner strategy
    TeamBudget createBudgetForTeam(Team team, Owner owner, LocalDate budgetYear, BigDecimal totalBudget);
    
    // Find budget for team in specific year
    TeamBudget findBudgetByTeamAndYear(Team team, LocalDate budgetYear);
    
    // Find all budgets for a team
    List<TeamBudget> findBudgetsByTeam(Team team);
    
    // Find all budgets for an owner
    List<TeamBudget> findBudgetsByOwner(Owner owner);
    
    // Allocate budgets based on owner strategy multipliers
    TeamBudget allocateBudgetByStrategy(TeamBudget budget, Owner owner, BigDecimal totalFunds);
    
    // Spend budget (will validate available funds)
    TeamBudget spendTransferBudget(UUID budgetId, BigDecimal amount);
    TeamBudget spendWageBudget(UUID budgetId, BigDecimal amount);
    TeamBudget spendAcademyBudget(UUID budgetId, BigDecimal amount);
    
    // Check budget health
    boolean isBudgetHealthy(UUID budgetId);
    double getBudgetUtilizationPercentage(UUID budgetId);
    
    // Freeze/unfreeze budget
    TeamBudget freezeBudget(UUID budgetId);
    TeamBudget unfreezeBudget(UUID budgetId);
}
