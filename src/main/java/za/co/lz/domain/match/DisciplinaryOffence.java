package za.co.lz.domain.match;

/**
 * The specific offence behind a caution or dismissal. Each offence carries a
 * default number of games a player is banned for when it results in a straight
 * red card (or a second yellow, which is functionally a red).
 *
 * These defaults follow the general pattern used by most domestic football
 * associations (e.g. FA/SAFA disciplinary tables): the ban length scales with
 * how serious/deliberate the offence was, not just "red card = fixed ban".
 * A tournament can still override the computed ban on a case-by-case basis
 * (e.g. via a disciplinary committee) - these are sensible defaults, not law.
 */
public enum DisciplinaryOffence {
    // Straight red card offences
    SERIOUS_FOUL_PLAY("Serious foul play", 2),
    VIOLENT_CONDUCT("Violent conduct", 3),
    SPITTING("Spitting at another person", 3),
    DENYING_OBVIOUS_GOAL_SCORING_OPPORTUNITY_HANDBALL("Denying a goal/goal-scoring opportunity (handball)", 1),
    DENYING_OBVIOUS_GOAL_SCORING_OPPORTUNITY_FOUL("Denying a goal-scoring opportunity by a foul", 1),
    OFFENSIVE_ABUSIVE_LANGUAGE("Offensive, abusive or insulting language/gestures", 2),
    // Second-bookable-offence red (two yellows)
    SECOND_BOOKABLE_OFFENCE("Second bookable offence", 1);

    private final String description;
    private final int defaultBanGames;

    DisciplinaryOffence(String description, int defaultBanGames) {
        this.description = description;
        this.defaultBanGames = defaultBanGames;
    }

    public String getDescription() {
        return description;
    }

    /** Number of subsequent games the player is automatically banned for. */
    public int getDefaultBanGames() {
        return defaultBanGames;
    }
}
