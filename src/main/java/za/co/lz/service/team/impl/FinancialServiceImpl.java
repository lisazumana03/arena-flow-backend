package za.co.lz.service.team.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.finances.ClubFinancials;
import za.co.lz.domain.team.finances.FinancialHealth;
import za.co.lz.domain.team.finances.Owner;
import za.co.lz.repository.team.finances.ClubFinancialsRepository;
import za.co.lz.service.team.finances.IFinancialService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FinancialServiceImpl implements IFinancialService {
    
    @Autowired
    private ClubFinancialsRepository financialsRepository;
    
    @Override
    public ClubFinancials create(ClubFinancials clubFinancials) {
        return financialsRepository.save(clubFinancials);
    }
    
    @Override
    public List<ClubFinancials> findAll() {
        return financialsRepository.findAll();
    }
    
    @Override
    public Optional<ClubFinancials> findById(UUID uuid) {
        return financialsRepository.findById(uuid);
    }
    
    @Override
    public ClubFinancials update(ClubFinancials clubFinancials, UUID uuid) {
        return financialsRepository.save(clubFinancials);
    }
    
    @Override
    public void delete(UUID uuid) {
        financialsRepository.deleteById(uuid);
    }
    
    @Override
    public ClubFinancials createAnnualFinancials(Team team, Owner owner, LocalDate year) {
        Optional<ClubFinancials> existing = financialsRepository.findByTeamIdAndFinancialYear(team.getTeamId(), year);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Financials already exist for " + team.getTeamName() + " in " + year.getYear());
        }
        
        ClubFinancials financials = new ClubFinancials.Builder()
                .setFinancialId(UUID.randomUUID())
                .setTeam(team)
                .setOwner(owner)
                .setFinancialYear(year)
                .setHealthStatus(FinancialHealth.HEALTHY)
                .build();
        
        return financialsRepository.save(financials);
    }
    
    @Override
    public ClubFinancials findByTeamAndYear(Team team, LocalDate year) {
        return financialsRepository.findByTeamIdAndFinancialYear(team.getTeamId(), year)
                .orElseThrow(() -> new IllegalArgumentException("Financials not found for team " + team.getTeamName() + " in " + year.getYear()));
    }
    
    @Override
    public List<ClubFinancials> findByTeam(Team team) {
        return financialsRepository.findByTeamId(team.getTeamId());
    }
    
    @Override
    public List<ClubFinancials> findByOwner(Owner owner) {
        return financialsRepository.findByOwnerId(owner.getOwnerId());
    }
    
    @Override
    public FinancialHealth evaluateHealth(UUID financialId) {
        Optional<ClubFinancials> optional = financialsRepository.findById(financialId);
        if (optional.isPresent()) {
            ClubFinancials financials = optional.get();
            return evaluateHealthByMetrics(
                    financials.getTotalRevenue(),
                    financials.getTotalExpenses(),
                    financials.getDebt()
            );
        }
        throw new IllegalArgumentException("Financials not found with ID: " + financialId);
    }
    
    @Override
    public FinancialHealth evaluateHealthByMetrics(BigDecimal revenue, BigDecimal expenses, BigDecimal debt) {
        BigDecimal profitOrLoss = revenue.subtract(expenses);
        double debtToRevenue = revenue.compareTo(BigDecimal.ZERO) == 0 ? 
                (debt.compareTo(BigDecimal.ZERO) == 0 ? 0 : Double.MAX_VALUE) :
                debt.doubleValue() / revenue.doubleValue();
        double operatingMargin = revenue.compareTo(BigDecimal.ZERO) == 0 ? 0 :
                (profitOrLoss.doubleValue() / revenue.doubleValue()) * 100;
        
        // Financial health scoring
        if (operatingMargin > 20 && debtToRevenue < 1.0) {
            return FinancialHealth.EXCELLENT;
        } else if (operatingMargin > 10 && debtToRevenue < 1.5) {
            return FinancialHealth.HEALTHY;
        } else if (operatingMargin > 0 && debtToRevenue < 2.5) {
            return FinancialHealth.STABLE;
        } else if (operatingMargin > -10 && debtToRevenue < 4.0) {
            return FinancialHealth.CAUTION;
        } else if (operatingMargin > -20 && debtToRevenue < 5.0) {
            return FinancialHealth.AT_RISK;
        } else if (operatingMargin > -30 && debtToRevenue < 6.0) {
            return FinancialHealth.CRITICAL;
        } else {
            return FinancialHealth.INSOLVENT;
        }
    }
    
    @Override
    public boolean requiresIntervention(UUID financialId) {
        FinancialHealth health = evaluateHealth(financialId);
        return health.isInterventionNeeded();
    }
    
    @Override
    public boolean isTakeoverCandidate(UUID financialId) {
        FinancialHealth health = evaluateHealth(financialId);
        return health.isTakeoverRisk();
    }
    
    @Override
    public ClubFinancials recordLoss(UUID financialId) {
        Optional<ClubFinancials> optional = financialsRepository.findById(financialId);
        if (optional.isPresent()) {
            ClubFinancials financials = optional.get();
            int losses = financials.getConsecutiveLosses() + 1;
            financials.setConsecutiveLosses(losses);
            
            // Add debt for loss
            BigDecimal addedDebt = financials.getTotalExpenses().subtract(financials.getTotalRevenue());
            if (addedDebt.compareTo(BigDecimal.ZERO) > 0) {
                financials.setDebt(financials.getDebt().add(addedDebt));
            }
            
            updateHealthStatus(financials);
            return financialsRepository.save(financials);
        }
        throw new IllegalArgumentException("Financials not found with ID: " + financialId);
    }
    
    @Override
    public ClubFinancials recordProfit(UUID financialId) {
        Optional<ClubFinancials> optional = financialsRepository.findById(financialId);
        if (optional.isPresent()) {
            ClubFinancials financials = optional.get();
            financials.setConsecutiveLosses(0);
            
            // Reduce debt with profit
            BigDecimal profit = financials.getTotalRevenue().subtract(financials.getTotalExpenses());
            if (profit.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal newDebt = financials.getDebt().subtract(profit);
                financials.setDebt(newDebt.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : newDebt);
            }
            
            updateHealthStatus(financials);
            return financialsRepository.save(financials);
        }
        throw new IllegalArgumentException("Financials not found with ID: " + financialId);
    }
    
    @Override
    public ClubFinancials addDebt(UUID financialId, BigDecimal debtAmount) {
        Optional<ClubFinancials> optional = financialsRepository.findById(financialId);
        if (optional.isPresent()) {
            ClubFinancials financials = optional.get();
            financials.setDebt(financials.getDebt().add(debtAmount));
            updateHealthStatus(financials);
            return financialsRepository.save(financials);
        }
        throw new IllegalArgumentException("Financials not found with ID: " + financialId);
    }
    
    @Override
    public ClubFinancials reduceDebt(UUID financialId, BigDecimal debtAmount) {
        Optional<ClubFinancials> optional = financialsRepository.findById(financialId);
        if (optional.isPresent()) {
            ClubFinancials financials = optional.get();
            BigDecimal newDebt = financials.getDebt().subtract(debtAmount);
            financials.setDebt(newDebt.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : newDebt);
            updateHealthStatus(financials);
            return financialsRepository.save(financials);
        }
        throw new IllegalArgumentException("Financials not found with ID: " + financialId);
    }
    
    @Override
    public ClubFinancials applyRelegationPenalty(UUID financialId, BigDecimal penaltyAmount) {
        Optional<ClubFinancials> optional = financialsRepository.findById(financialId);
        if (optional.isPresent()) {
            ClubFinancials financials = optional.get();
            // Add penalty as debt
            financials.setDebt(financials.getDebt().add(penaltyAmount));
            // Mark as requiring intervention
            financials.setRequiresIntervention(true);
            updateHealthStatus(financials);
            return financialsRepository.save(financials);
        }
        throw new IllegalArgumentException("Financials not found with ID: " + financialId);
    }
    
    @Override
    public ClubFinancials freezeBudgetForFFP(UUID financialId) {
        Optional<ClubFinancials> optional = financialsRepository.findById(financialId);
        if (optional.isPresent()) {
            ClubFinancials financials = optional.get();
            financials.setHealthStatus(FinancialHealth.CRITICAL);
            financials.setRequiresIntervention(true);
            return financialsRepository.save(financials);
        }
        throw new IllegalArgumentException("Financials not found with ID: " + financialId);
    }
    
    @Override
    public List<ClubFinancials> findTakeovers() {
        return financialsRepository.findByIsTakeoverCandidateTrue();
    }
    
    @Override
    public List<ClubFinancials> findInIntervention() {
        return financialsRepository.findByRequiresInterventionTrue();
    }
    
    private void updateHealthStatus(ClubFinancials financials) {
        FinancialHealth newHealth = evaluateHealthByMetrics(
                financials.getTotalRevenue(),
                financials.getTotalExpenses(),
                financials.getDebt()
        );
        financials.setHealthStatus(newHealth);
        financials.setRequiresIntervention(newHealth.isInterventionNeeded());
        financials.setTakeoverCandidate(newHealth.isTakeoverRisk());
    }
}
