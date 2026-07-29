package za.co.lz.domain.team.finances;

/**
 * Represents the strategic approach an owner takes with their club(s).
 * Each strategy affects how the owner allocates budgets and makes decisions.
 */
public enum OwnerStrategy {
    AGGRESSIVE_SPENDING(
            "Aggressive Spending",
            "Maximize transfer and wage budgets for immediate success",
            1.5,    // Transfer budget multiplier
            1.5,    // Wage budget multiplier
            0.8     // Youth academy priority (lower = less investment)
    ),
    
    YOUTH_DEVELOPMENT(
            "Youth Development",
            "Focus on developing young talent through academy and careful spending",
            0.6,    // Transfer budget multiplier
            0.7,    // Wage budget multiplier
            2.0     // Youth academy priority (higher = more investment)
    ),
    
    INFRASTRUCTURE_INVESTMENT(
            "Infrastructure Investment",
            "Focus on building stadiums, training facilities, and club infrastructure",
            0.7,
            0.8,
            1.2
    ),
    
    CONSERVATIVE_SPENDING(
            "Conservative Spending",
            "Controlled spending with emphasis on stability and profitability",
            0.7,
            0.8,
            0.8
    ),
    
    COMMUNITY_ENGAGEMENT(
            "Community Engagement",
            "Focus on grassroots development and fan engagement",
            0.6,
            0.7,
            1.5
    ),
    
    SUSTAINABILITY_FOCUS(
            "Sustainability Focus",
            "Balance revenue, spending, and long-term club health",
            1.0,
            1.0,
            1.0
    ),
    
    BRAND_EXPANSION(
            "Brand Expansion",
            "Invest in marketing, sponsorships, and global brand development",
            1.2,
            1.0,
            0.9
    ),
    
    MERGERS_AND_ACQUISITIONS(
            "Mergers & Acquisitions",
            "Acquire additional clubs and consolidate multi-club operations",
            0.8,
            0.9,
            1.1
    ),
    
    PROFIT_FOCUS(
            "Profit Focus",
            "Maximize revenue and minimize spending for shareholder returns",
            0.5,    // Transfer budget multiplier
            0.6,    // Wage budget multiplier
            0.5     // Youth academy priority
    ),
    
    BALANCED_APPROACH(
            "Balanced Approach",
            "Mix of spending, revenue, and development for sustainable growth",
            1.0,    // Transfer budget multiplier
            1.0,    // Wage budget multiplier
            1.0     // Youth academy priority
    ),
    
    SURVIVAL_MODE(
            "Survival Mode",
            "Minimize spending and focus on financial stability",
            0.4,    // Transfer budget multiplier
            0.5,    // Wage budget multiplier
            0.3     // Youth academy priority
    );

    private final String displayName;
    private final String description;
    private final double transferBudgetMultiplier;
    private final double wageBudgetMultiplier;
    private final double youthAcademyPriority;

    OwnerStrategy(String displayName, String description, 
                  double transferBudgetMultiplier, double wageBudgetMultiplier, 
                  double youthAcademyPriority) {
        this.displayName = displayName;
        this.description = description;
        this.transferBudgetMultiplier = transferBudgetMultiplier;
        this.wageBudgetMultiplier = wageBudgetMultiplier;
        this.youthAcademyPriority = youthAcademyPriority;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public double getTransferBudgetMultiplier() {
        return transferBudgetMultiplier;
    }

    public double getWageBudgetMultiplier() {
        return wageBudgetMultiplier;
    }

    public double getYouthAcademyPriority() {
        return youthAcademyPriority;
    }
}
