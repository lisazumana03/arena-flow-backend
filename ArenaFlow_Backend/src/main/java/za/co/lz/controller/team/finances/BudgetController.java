package za.co.lz.controller.team.finances;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.finances.Owner;
import za.co.lz.domain.team.finances.TeamBudget;
import za.co.lz.service.team.finances.IBudgetService;
import za.co.lz.service.team.ITeamService;
import za.co.lz.service.team.finances.IOwnerService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    
    @Autowired
    private IBudgetService budgetService;
    
    @Autowired
    private ITeamService teamService;
    
    @Autowired
    private IOwnerService ownerService;
    
    @GetMapping
    public ResponseEntity<List<TeamBudget>> getAllBudgets() {
        return ResponseEntity.ok(budgetService.findAll());
    }
    
    @PostMapping("/create")
    public ResponseEntity<TeamBudget> createBudget(@RequestBody BudgetCreateRequest request) {
        // This endpoint should be called by owners to create a budget for their team
        Team team = teamService.findById(request.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        // Verify the requesting owner actually owns this team
        Owner owner = team.getOwner();
        if (owner == null || !owner.getOwnerId().equals(request.getOwnerId())) {
            throw new IllegalArgumentException("Owner not found or does not own this team");
        }
        
        TeamBudget budget = budgetService.createBudgetForTeam(
                team, 
                owner,
                request.getBudgetYear(), 
                request.getTotalBudget()
        );
        
        return ResponseEntity.ok(budget);
    }
    
    @GetMapping("/team/{teamId}/year/{year}")
    public ResponseEntity<TeamBudget> getBudgetByTeamAndYear(
            @PathVariable UUID teamId,
            @PathVariable int year) {
        Team team = teamService.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        LocalDate budgetYear = LocalDate.of(year, 1, 1);
        TeamBudget budget = budgetService.findBudgetByTeamAndYear(team, budgetYear);
        return ResponseEntity.ok(budget);
    }
    
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<TeamBudget>> getBudgetsByTeam(@PathVariable UUID teamId) {
        Team team = teamService.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        List<TeamBudget> budgets = budgetService.findBudgetsByTeam(team);
        return ResponseEntity.ok(budgets);
    }
    
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<TeamBudget>> getBudgetsByOwner(@PathVariable UUID ownerId) {
        Owner owner = ownerService.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        List<TeamBudget> budgets = budgetService.findBudgetsByOwner(owner);
        return ResponseEntity.ok(budgets);
    }
    
    @PostMapping("/{budgetId}/spend-transfer")
    public ResponseEntity<TeamBudget> spendTransferBudget(
            @PathVariable UUID budgetId,
            @RequestParam BigDecimal amount) {
        TeamBudget budget = budgetService.spendTransferBudget(budgetId, amount);
        return ResponseEntity.ok(budget);
    }
    
    @PostMapping("/{budgetId}/spend-wages")
    public ResponseEntity<TeamBudget> spendWageBudget(
            @PathVariable UUID budgetId,
            @RequestParam BigDecimal amount) {
        TeamBudget budget = budgetService.spendWageBudget(budgetId, amount);
        return ResponseEntity.ok(budget);
    }
    
    @PostMapping("/{budgetId}/spend-academy")
    public ResponseEntity<TeamBudget> spendAcademyBudget(
            @PathVariable UUID budgetId,
            @RequestParam BigDecimal amount) {
        TeamBudget budget = budgetService.spendAcademyBudget(budgetId, amount);
        return ResponseEntity.ok(budget);
    }
    
    @GetMapping("/{budgetId}/health")
    public ResponseEntity<BudgetHealthResponse> getBudgetHealth(@PathVariable UUID budgetId) {
        boolean healthy = budgetService.isBudgetHealthy(budgetId);
        double utilization = budgetService.getBudgetUtilizationPercentage(budgetId);
        BudgetHealthResponse response = new BudgetHealthResponse(healthy, utilization);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{budgetId}/freeze")
    public ResponseEntity<TeamBudget> freezeBudget(@PathVariable UUID budgetId) {
        TeamBudget budget = budgetService.freezeBudget(budgetId);
        return ResponseEntity.ok(budget);
    }
    
    @PostMapping("/{budgetId}/unfreeze")
    public ResponseEntity<TeamBudget> unfreezeBudget(@PathVariable UUID budgetId) {
        TeamBudget budget = budgetService.unfreezeBudget(budgetId);
        return ResponseEntity.ok(budget);
    }
    
    // Inner class for API requests
    public static class BudgetCreateRequest {
        private UUID teamId;
        private UUID ownerId;
        private LocalDate budgetYear;
        private BigDecimal totalBudget;
        
        public UUID getTeamId() { return teamId; }
        public void setTeamId(UUID teamId) { this.teamId = teamId; }
        
        public UUID getOwnerId() { return ownerId; }
        public void setOwnerId(UUID ownerId) { this.ownerId = ownerId; }
        
        public LocalDate getBudgetYear() { return budgetYear; }
        public void setBudgetYear(LocalDate budgetYear) { this.budgetYear = budgetYear; }
        
        public BigDecimal getTotalBudget() { return totalBudget; }
        public void setTotalBudget(BigDecimal totalBudget) { this.totalBudget = totalBudget; }
    }
    
    // Inner class for budget health response
    public static class BudgetHealthResponse {
        private boolean healthy;
        private double utilizationPercentage;
        
        public BudgetHealthResponse(boolean healthy, double utilizationPercentage) {
            this.healthy = healthy;
            this.utilizationPercentage = utilizationPercentage;
        }
        
        public boolean isHealthy() { return healthy; }
        public double getUtilizationPercentage() { return utilizationPercentage; }
    }
}