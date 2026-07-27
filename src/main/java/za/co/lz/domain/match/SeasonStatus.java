package za.co.lz.domain.match;

/**
 * Represents the status of a season.
 */
public enum SeasonStatus {
    PLANNING("Planning", "Season is being planned, matches not yet scheduled"),
    ACTIVE("Active", "Season is currently active with ongoing matches"),
    COMPLETED("Completed", "Season has ended and final standings determined"),
    CANCELLED("Cancelled", "Season has been cancelled");

    private final String displayName;
    private final String description;

    SeasonStatus(String displayName, String description) {
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
