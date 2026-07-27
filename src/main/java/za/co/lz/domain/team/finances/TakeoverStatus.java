package za.co.lz.domain.team.finances;

/**
 * Represents the state of a potential takeover.
 */
public enum TakeoverStatus {
    AVAILABLE("Available", "Team is available for purchase by interested buyers"),
    FOR_SALE("For Sale", "Owner is actively selling the team"),
    NEGOTIATION("In Negotiation", "Active takeover negotiation in progress"),
    FORCED_SALE("Forced Sale", "Team must be sold due to financial distress"),
    UNDER_NEW_OWNERSHIP("Under New Ownership", "Takeover completed"),
    PROTECTED("Protected", "Owner has protection measures in place");

    private final String displayName;
    private final String description;

    TakeoverStatus(String displayName, String description) {
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
