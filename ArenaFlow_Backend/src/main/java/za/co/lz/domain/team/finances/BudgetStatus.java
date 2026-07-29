package za.co.lz.domain.team.finances;

/**
 * Represents the status of a team's budget for a given year.
 */
public enum BudgetStatus {
    ACTIVE("Active", "Budget is currently active and spending is allowed"),
    FROZEN("Frozen", "Budget is frozen due to financial restrictions"),
    EXCEEDED("Exceeded", "Budget has been exceeded (overspending)"),
    COMPLETED("Completed", "Budget cycle has ended"),
    SUSPENDED("Suspended", "Budget is suspended due to FFP violations");

    private final String displayName;
    private final String description;

    BudgetStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
