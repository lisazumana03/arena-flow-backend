package za.co.lz.domain.match;

/**
 * Represents the current status of a match.
 */
public enum MatchStatus {
    SCHEDULED("Scheduled", "Match is scheduled but not yet played"),
    IN_PROGRESS("In Progress", "Match is currently being played"),
    COMPLETED("Completed", "Match has been completed"),
    POSTPONED("Postponed", "Match has been postponed"),
    CANCELLED("Cancelled", "Match has been cancelled");

    private final String displayName;
    private final String description;

    MatchStatus(String displayName, String description) {
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
