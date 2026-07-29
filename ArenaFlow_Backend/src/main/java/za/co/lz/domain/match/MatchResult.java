package za.co.lz.domain.match;

/**
 * Represents the result of a match (outcome).
 */
public enum MatchResult {
    HOME_WIN("Home Win", "Home team won the match"),
    AWAY_WIN("Away Win", "Away team won the match"),
    DRAW("Draw", "Match ended in a draw");

    private final String displayName;
    private final String description;

    MatchResult(String displayName, String description) {
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
