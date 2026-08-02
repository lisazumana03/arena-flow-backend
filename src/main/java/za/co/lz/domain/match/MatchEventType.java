package za.co.lz.domain.match;

/**
 * The kind of thing that happened at a given minute of a match.
 * scoresGoal() tells the service layer which events must bump Match's score.
 */
public enum MatchEventType {
    GOAL(true),
    PENALTY_GOAL(true),
    OWN_GOAL(true),
    YELLOW_CARD(false),
    SECOND_YELLOW_CARD(false),
    RED_CARD(false),
    SUBSTITUTION(false),
    CORNER(false),
    FREE_KICK(false),
    INJURY(false);

    private final boolean scoresGoal;

    MatchEventType(boolean scoresGoal) {
        this.scoresGoal = scoresGoal;
    }

    /** Whether recording this event type should increment the scoring team's tally. */
    public boolean scoresGoal() {
        return scoresGoal;
    }

    public boolean isCard() {
        return this == YELLOW_CARD || this == SECOND_YELLOW_CARD || this == RED_CARD;
    }
}
