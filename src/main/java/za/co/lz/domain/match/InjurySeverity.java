package za.co.lz.domain.match;

/**
 * Classifies an in-match injury and gives a typical recovery window. This is
 * deliberately a rough clinical estimate (a "knock" vs a "suspected fracture"),
 * not a diagnosis - the actual return date should be confirmed by medical staff.
 * Games missed is estimated assuming one match per week.
 */
public enum InjurySeverity {
    KNOCK("Knock/bruise", 3),
    MINOR("Minor strain/sprain", 14),
    MODERATE("Moderate injury (e.g. hamstring/ankle sprain)", 28),
    SEVERE("Severe injury (e.g. fracture, muscle tear)", 84),
    CAREER_THREATENING("Major injury (e.g. ACL rupture)", 270);

    private final String description;
    private final int typicalDaysOut;

    InjurySeverity(String description, int typicalDaysOut) {
        this.description = description;
        this.typicalDaysOut = typicalDaysOut;
    }

    public String getDescription() {
        return description;
    }

    public int getTypicalDaysOut() {
        return typicalDaysOut;
    }

    /** Rough games-missed estimate, assuming a match roughly once a week. */
    public int getTypicalGamesOut() {
        return (int) Math.ceil(typicalDaysOut / 7.0);
    }
}
