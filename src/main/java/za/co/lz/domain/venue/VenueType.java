package za.co.lz.domain.venue;

/**
 * Distinguishes the class of venue a match is played at. Drives things like
 * default capacity expectations and whether facilities (floodlights, seating,
 * broadcast gantries) can be assumed to exist.
 */
public enum VenueType {
    COMMUNITY_GROUND("Community Ground", "Local, low-capacity ground - typically no seating/floodlights"),
    MUNICIPAL_STADIUM("Municipal Stadium", "Mid-size public stadium with basic seating and facilities"),
    SOCCER_STADIUM("Soccer Stadium", "Purpose-built, high-capacity stadium with full facilities");

    private final String displayName;
    private final String description;

    VenueType(String displayName, String description) {
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
