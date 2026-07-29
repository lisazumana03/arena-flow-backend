package za.co.lz.controller.team.finances;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.finances.ClubFinancials;
import za.co.lz.domain.team.finances.FinancialHealth;
import za.co.lz.domain.team.finances.Owner;
import za.co.lz.service.team.impl.FinancialServiceImpl;
import za.co.lz.service.team.impl.TakeoverServiceImpl;
import za.co.lz.service.team.impl.OwnerServiceImpl;
import za.co.lz.service.team.impl.TeamServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/financials")
public class FinancialController {
    
    @Autowired
    private FinancialServiceImpl financialService;
    
    @Autowired
    private TakeoverServiceImpl takeoverService;
    
    @Autowired
    private OwnerServiceImpl ownerService;
    
    @Autowired
    private TeamServiceImpl teamService;
    
    @PostMapping("/create")
    public ResponseEntity<ClubFinancials> createAnnualFinancials(@RequestBody FinancialCreateRequest request) {
        Team team = teamService.findById(request.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + request.getTeamId()));
        Owner owner = ownerService.findById(request.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found with ID: " + request.getOwnerId()));
        
        ClubFinancials financials = financialService.createAnnualFinancials(team, owner, request.getYear());
        return ResponseEntity.ok(financials);
    }
    
    @GetMapping("/team/{teamId}/year/{year}")
    public ResponseEntity<ClubFinancials> getFinancialsByTeamAndYear(
            @PathVariable UUID teamId,
            @PathVariable int year) {
        Team team = teamService.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));
        ClubFinancials financials = financialService.findByTeamAndYear(team, LocalDate.of(year, 1, 1));
        return ResponseEntity.ok(financials);
    }
    
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<ClubFinancials>> getFinancialsByTeam(@PathVariable UUID teamId) {
        Team team = teamService.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));
        List<ClubFinancials> financials = financialService.findByTeam(team);
        return ResponseEntity.ok(financials);
    }
    
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ClubFinancials>> getFinancialsByOwner(@PathVariable UUID ownerId) {
        Owner owner = ownerService.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found with ID: " + ownerId));
        List<ClubFinancials> financials = financialService.findByOwner(owner);
        return ResponseEntity.ok(financials);
    }
    
    @GetMapping("/{financialId}/health")
    public ResponseEntity<FinancialHealthResponse> getFinancialHealth(@PathVariable UUID financialId) {
        FinancialHealth health = financialService.evaluateHealth(financialId);
        boolean intervention = financialService.requiresIntervention(financialId);
        boolean takeover = financialService.isTakeoverCandidate(financialId);
        
        FinancialHealthResponse response = new FinancialHealthResponse(
                health.getDisplayName(),
                health.getDescription(),
                intervention,
                takeover,
                health.getBudgetMultiplier()
        );
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{financialId}/record-loss")
    public ResponseEntity<ClubFinancials> recordLoss(@PathVariable UUID financialId) {
        ClubFinancials financials = financialService.recordLoss(financialId);
        return ResponseEntity.ok(financials);
    }
    
    @PostMapping("/{financialId}/record-profit")
    public ResponseEntity<ClubFinancials> recordProfit(@PathVariable UUID financialId) {
        ClubFinancials financials = financialService.recordProfit(financialId);
        return ResponseEntity.ok(financials);
    }
    
    @PostMapping("/{financialId}/add-debt")
    public ResponseEntity<ClubFinancials> addDebt(
            @PathVariable UUID financialId,
            @RequestParam BigDecimal amount) {
        ClubFinancials financials = financialService.addDebt(financialId, amount);
        return ResponseEntity.ok(financials);
    }
    
    @PostMapping("/{financialId}/apply-relegation-penalty")
    public ResponseEntity<ClubFinancials> applyRelegationPenalty(
            @PathVariable UUID financialId,
            @RequestParam BigDecimal penaltyAmount) {
        ClubFinancials financials = financialService.applyRelegationPenalty(financialId, penaltyAmount);
        return ResponseEntity.ok(financials);
    }
    
    @GetMapping("/takeovers")
    public ResponseEntity<List<ClubFinancials>> getTakeovers() {
        List<ClubFinancials> takeovers = financialService.findTakeovers();
        return ResponseEntity.ok(takeovers);
    }
    
    @GetMapping("/intervention")
    public ResponseEntity<List<ClubFinancials>> getInterventionRequired() {
        List<ClubFinancials> intervention = financialService.findInIntervention();
        return ResponseEntity.ok(intervention);
    }
    
    // Takeover endpoints
    @PostMapping("/takeover/evaluate/{teamId}")
    public ResponseEntity<EvaluationResponse> evaluateForcedSale(@PathVariable UUID teamId) {
        Team team = teamService.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));
        
        boolean forcedSaleNeeded = takeoverService.evaluateForcedSale(team);
        boolean interventionNeeded = takeoverService.requiresOwnerIntervention(team);
        
        EvaluationResponse response = new EvaluationResponse(forcedSaleNeeded, interventionNeeded);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/takeover/execute")
    public ResponseEntity<Owner> executeTakeover(@RequestBody TakeoverRequest request) {
        Team team = teamService.findById(request.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + request.getTeamId()));
        Owner buyer = ownerService.findById(request.getBuyerId())
                .orElseThrow(() -> new IllegalArgumentException("Buyer not found with ID: " + request.getBuyerId()));
        
        Owner result = takeoverService.forcedSale(team, buyer, request.getPrice());
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/owner/{ownerId}/inject-funds")
    public ResponseEntity<Owner> injectFunds(
            @PathVariable UUID ownerId,
            @RequestParam UUID teamId,
            @RequestParam BigDecimal amount) {
        Team team = teamService.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));
        Owner result = takeoverService.injectionOfFunds(team, amount);
        return ResponseEntity.ok(result);
    }
    
    // Inner classes for API
    public static class FinancialCreateRequest {
        private UUID teamId;
        private UUID ownerId;
        private LocalDate year;
        
        public UUID getTeamId() { return teamId; }
        public UUID getOwnerId() { return ownerId; }
        public LocalDate getYear() { return year; }
    }
    
    public static class FinancialHealthResponse {
        private String status;
        private String description;
        private boolean interventionNeeded;
        private boolean takeoverRisk;
        private double budgetMultiplier;
        
        public FinancialHealthResponse(String status, String description, 
                                      boolean interventionNeeded, boolean takeoverRisk, 
                                      double budgetMultiplier) {
            this.status = status;
            this.description = description;
            this.interventionNeeded = interventionNeeded;
            this.takeoverRisk = takeoverRisk;
            this.budgetMultiplier = budgetMultiplier;
        }
        
        public String getStatus() { return status; }
        public String getDescription() { return description; }
        public boolean isInterventionNeeded() { return interventionNeeded; }
        public boolean isTakeoverRisk() { return takeoverRisk; }
        public double getBudgetMultiplier() { return budgetMultiplier; }
    }
    
    public static class EvaluationResponse {
        private boolean forcedSaleNeeded;
        private boolean interventionNeeded;
        
        public EvaluationResponse(boolean forcedSaleNeeded, boolean interventionNeeded) {
            this.forcedSaleNeeded = forcedSaleNeeded;
            this.interventionNeeded = interventionNeeded;
        }
        
        public boolean isForcedSaleNeeded() { return forcedSaleNeeded; }
        public boolean isInterventionNeeded() { return interventionNeeded; }
    }
    
    public static class TakeoverRequest {
        private UUID teamId;
        private UUID buyerId;
        private BigDecimal price;
        
        public UUID getTeamId() { return teamId; }
        public UUID getBuyerId() { return buyerId; }
        public BigDecimal getPrice() { return price; }
    }
}
