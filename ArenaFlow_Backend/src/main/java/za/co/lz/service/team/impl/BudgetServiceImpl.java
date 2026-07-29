package za.co.lz.service.team.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.finances.BudgetStatus;
import za.co.lz.domain.team.finances.Owner;
import za.co.lz.domain.team.finances.TeamBudget;
import za.co.lz.repository.team.finances.TeamBudgetRepository;
import za.co.lz.service.team.finances.IBudgetService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BudgetServiceImpl implements IBudgetService {
    
    @Autowired
    private TeamBudgetRepository budgetRepository;
    
    @Override
    public TeamBudget create(TeamBudget budget) {
        return budgetRepository.save(budget);
    }
    
    @Override
    public List<TeamBudget> findAll() {
        return budgetRepository.findAll();
    }
    
    @Override
    public Optional<TeamBudget> findById(UUID uuid) {
        return budgetRepository.findById(uuid);
    }
    
    @Override
    public TeamBudget update(TeamBudget budget, UUID uuid) {
        return budgetRepository.save(budget);
    }
    
    @Override
    public void delete(UUID uuid) {
        budgetRepository.deleteById(uuid);
    }
    
    @Override
    public TeamBudget createBudgetForTeam(Team team, Owner owner, LocalDate budgetYear, BigDecimal totalBudget) {
        // Check if budget already exists for this team and year
        Optional<TeamBudget> existing = budgetRepository.findByTeam_TeamIdAndBudgetYear(team.getTeamId(), budgetYear);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Budget already exists for " + team.getTeamName() + " in " + budgetYear.getYear());
        }
        
        TeamBudget budget = new TeamBudget.Builder()
                .setBudgetId(UUID.randomUUID())
                .setTeam(team)
                .setOwner(owner)
                .setBudgetYear(budgetYear)
                .setStatus(BudgetStatus.ACTIVE)
                .build();
        
        return allocateBudgetByStrategy(budget, owner, totalBudget);
    }
    
    @Override
    public TeamBudget findBudgetByTeamAndYear(Team team, LocalDate budgetYear) {
        return budgetRepository.findByTeam_TeamIdAndBudgetYear(team.getTeamId(), budgetYear)
                .orElseThrow(() -> new IllegalArgumentException("Budget not found for team " + team.getTeamName() + " in year " + budgetYear.getYear()));
    }
    
    @Override
    public List<TeamBudget> findBudgetsByTeam(Team team) {
        return budgetRepository.findByTeam_TeamId(team.getTeamId());
    }
    
    @Override
    public List<TeamBudget> findBudgetsByOwner(Owner owner) {
        return budgetRepository.findByOwner_OwnerId(owner.getOwnerId());
    }
    
    @Override
    public TeamBudget allocateBudgetByStrategy(TeamBudget budget, Owner owner, BigDecimal totalBudget) {
        if (owner.getStrategy() == null) {
            throw new IllegalStateException("Owner must have a strategy set");
        }
        
        double transferMultiplier = owner.getStrategy().getTransferBudgetMultiplier();
        double wageMultiplier = owner.getStrategy().getWageBudgetMultiplier();
        double academyMultiplier = owner.getStrategy().getYouthAcademyPriority();
        
        // Calculate total multiplier sum (for proportional allocation)
        double totalMultiplier = transferMultiplier + wageMultiplier + academyMultiplier + 2.0; // +2.0 for operating and infrastructure
        
        // Allocate budgets proportionally
        BigDecimal transferBudget = totalBudget.multiply(BigDecimal.valueOf(transferMultiplier / totalMultiplier));
        BigDecimal wageBudget = totalBudget.multiply(BigDecimal.valueOf(wageMultiplier / totalMultiplier));
        BigDecimal academyBudget = totalBudget.multiply(BigDecimal.valueOf(academyMultiplier / totalMultiplier));
        BigDecimal operatingBudget = totalBudget.multiply(BigDecimal.valueOf(1.0 / totalMultiplier));
        BigDecimal infrastructureBudget = totalBudget.multiply(BigDecimal.valueOf(1.0 / totalMultiplier));
        
        budget = new TeamBudget.Builder()
                .copy(budget)
                .setTransferBudget(transferBudget)
                .setWageBudget(wageBudget)
                .setYouthAcademyBudget(academyBudget)
                .setOperatingBudget(operatingBudget)
                .setInfrastructureBudget(infrastructureBudget)
                .build();
        
        return budgetRepository.save(budget);
    }
    
    @Override
    public TeamBudget spendTransferBudget(UUID budgetId, BigDecimal amount) {
        Optional<TeamBudget> optional = budgetRepository.findById(budgetId);
        if (optional.isPresent()) {
            TeamBudget budget = optional.get();
            budget.spendOnTransfers(amount);
            return budgetRepository.save(budget);
        }
        throw new IllegalArgumentException("Budget not found with ID: " + budgetId);
    }
    
    @Override
    public TeamBudget spendWageBudget(UUID budgetId, BigDecimal amount) {
        Optional<TeamBudget> optional = budgetRepository.findById(budgetId);
        if (optional.isPresent()) {
            TeamBudget budget = optional.get();
            budget.spendOnWages(amount);
            return budgetRepository.save(budget);
        }
        throw new IllegalArgumentException("Budget not found with ID: " + budgetId);
    }
    
    @Override
    public TeamBudget spendAcademyBudget(UUID budgetId, BigDecimal amount) {
        Optional<TeamBudget> optional = budgetRepository.findById(budgetId);
        if (optional.isPresent()) {
            TeamBudget budget = optional.get();
            budget.spendOnAcademy(amount);
            return budgetRepository.save(budget);
        }
        throw new IllegalArgumentException("Budget not found with ID: " + budgetId);
    }
    
    @Override
    public boolean isBudgetHealthy(UUID budgetId) {
        Optional<TeamBudget> optional = budgetRepository.findById(budgetId);
        if (optional.isPresent()) {
            TeamBudget budget = optional.get();
            // Budget is healthy if remaining budget is positive
            return budget.getRemainingBudget().compareTo(BigDecimal.ZERO) >= 0 
                    && budget.getStatus() != BudgetStatus.SUSPENDED
                    && budget.getStatus() != BudgetStatus.EXCEEDED;
        }
        return false;
    }
    
    @Override
    public double getBudgetUtilizationPercentage(UUID budgetId) {
        Optional<TeamBudget> optional = budgetRepository.findById(budgetId);
        if (optional.isPresent()) {
            TeamBudget budget = optional.get();
            BigDecimal total = budget.getTotalBudget();
            if (total.compareTo(BigDecimal.ZERO) == 0) {
                return 0.0;
            }
            BigDecimal spent = budget.getTotalSpent();
            return (spent.doubleValue() / total.doubleValue()) * 100;
        }
        return 0.0;
    }
    
    @Override
    public TeamBudget freezeBudget(UUID budgetId) {
        Optional<TeamBudget> optional = budgetRepository.findById(budgetId);
        if (optional.isPresent()) {
            TeamBudget budget = optional.get();
            budget.setStatus(BudgetStatus.FROZEN);
            return budgetRepository.save(budget);
        }
        throw new IllegalArgumentException("Budget not found with ID: " + budgetId);
    }
    
    @Override
    public TeamBudget unfreezeBudget(UUID budgetId) {
        Optional<TeamBudget> optional = budgetRepository.findById(budgetId);
        if (optional.isPresent()) {
            TeamBudget budget = optional.get();
            budget.setStatus(BudgetStatus.ACTIVE);
            return budgetRepository.save(budget);
        }
        throw new IllegalArgumentException("Budget not found with ID: " + budgetId);
    }
}
