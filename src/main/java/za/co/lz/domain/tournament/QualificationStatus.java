package za.co.lz.domain.tournament;

/**
 * Tracks a team's progress or final outcome within a single tournament edition
 * (a specific Season). Meaning is format-dependent:
 * - LEAGUE: mostly REGISTERED -> ELIMINATED/QUALIFIED (for promotion/continental spots)
 * - KNOCKOUT / HYBRID: progresses through the round-based statuses below
 */
public enum QualificationStatus {
    REGISTERED("Registered", "Entered but the tournament has not started"),
    GROUP_STAGE("Group Stage", "Currently competing in the group stage"),
    ROUND_OF_16("Round of 16", "Reached the round of 16"),
    QUARTERFINALIST("Quarter-finalist", "Reached the quarter-finals"),
    SEMIFINALIST("Semi-finalist", "Reached the semi-finals"),
    RUNNER_UP("Runner-up", "Lost the final"),
    CHAMPION("Champion", "Won the tournament"),
    QUALIFIED("Qualified", "Qualified for the next stage/competition (e.g. continental spot)"),
    ELIMINATED("Eliminated", "Knocked out or finished the season without qualifying"),
    WITHDRAWN("Withdrawn", "Withdrew or was disqualified from the tournament");

    private final String displayName;
    private final String description;

    QualificationStatus(String displayName, String description) {
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
