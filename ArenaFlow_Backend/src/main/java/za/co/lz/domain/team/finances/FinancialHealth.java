package za.co.lz.domain.team.finances;

/**
 * Enum representing different states of financial health.
 * Determines if/when takeover triggers activate.
 */
public enum FinancialHealth {
    EXCELLENT(
            "Excellent",
            "Strong financials, no concerns",
            1.0,    // Budget multiplier
            false,  // Takeover risk
            false   // Intervention needed
    ),
    
    HEALTHY(
            "Healthy",
            "Good financial position",
            1.0,
            false,
            false
    ),
    
    STABLE(
            "Stable",
            "Meets minimum requirements",
            0.9,
            false,
            false
    ),
    
    CAUTION(
            "Caution",
            "Needs careful management",
            0.75,
            false,
            true    // Monitoring required
    ),
    
    AT_RISK(
            "At Risk",
            "Financial concerns, possible intervention",
            0.5,
            true,   // Takeover risk
            true
    ),
    
    CRITICAL(
            "Critical",
            "Severe financial distress",
            0.3,
            true,
            true
    ),
    
    INSOLVENT(
            "Insolvent",
            "Unable to meet obligations",
            0.0,
            true,
            true    // Must sell or fold
    );

    private final String displayName;
    private final String description;
    private final double budgetMultiplier;
    private final boolean takeoverRisk;
    private final boolean interventionNeeded;

    FinancialHealth(String displayName, String description, double budgetMultiplier, 
                   boolean takeoverRisk, boolean interventionNeeded) {
        this.displayName = displayName;
        this.description = description;
        this.budgetMultiplier = budgetMultiplier;
        this.takeoverRisk = takeoverRisk;
        this.interventionNeeded = interventionNeeded;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public double getBudgetMultiplier() {
        return budgetMultiplier;
    }

    public boolean isTakeoverRisk() {
        return takeoverRisk;
    }

    public boolean isInterventionNeeded() {
        return interventionNeeded;
    }
}
