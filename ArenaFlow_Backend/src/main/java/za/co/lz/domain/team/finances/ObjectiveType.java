package za.co.lz.domain.team.finances;

/**
 * Represents different types of objectives an owner can set for their club(s).
 */
public enum ObjectiveType {
    WIN_LEAGUE(
            "Win League",
            "Win the domestic league championship"
    ),
    
    QUALIFY_FOR_CONTINENTAL_COMPETITION(
            "Qualify for Continental Competition",
            "Achieve a position to qualify for continental competitions (Champions League, etc.)"
    ),
    
    AVOID_RELEGATION(
            "Avoid Relegation",
            "Finish in a position to avoid relegation"
    ),
    
    WIN_DOMESTIC_CUP(
            "Win Domestic Cup",
            "Win the domestic cup competition"
    ),
    
    ACHIEVE_PLAYOFF_SPOT(
            "Achieve Playoff Spot",
            "Secure a spot in playoff competitions"
    ),
    
    DEVELOP_YOUNG_PLAYERS(
            "Develop Young Players",
            "Successfully develop youth academy players into first-team regulars"
    ),
    
    IMPROVE_FINANCIAL_STABILITY(
            "Improve Financial Stability",
            "Increase club revenue and reduce debt"
    ),
    
    ESTABLISH_WINNING_CULTURE(
            "Establish Winning Culture",
            "Build a competitive team and maintain high performance"
    ),
    
    EXPAND_STADIUM(
            "Expand Stadium",
            "Increase stadium capacity and modernize facilities"
    ),
    
    WIN_CONTINENTAL_TROPHY(
            "Win Continental Trophy",
            "Win a continental competition like Champions League"
    ),
    
    REACH_SPECIFIC_LEAGUE_POSITION(
            "Reach Specific League Position",
            "Achieve a specific position in the league table"
    ),
    
    INCREASE_CLUB_VALUE(
            "Increase Club Value",
            "Increase the overall valuation of the club"
    ),
    
    ACHIEVE_BREAK_EVEN_BUDGET(
            "Achieve Break-Even Budget",
            "Balance club finances to achieve break-even operations"
    ),
    
    ATTRACT_SPONSORSHIPS(
            "Attract Sponsorships",
            "Secure major commercial partnerships and sponsorships"
    ),
    
    BUILD_ACADEMY(
            "Build Academy",
            "Establish and develop a strong youth academy program"
    );

    private final String displayName;
    private final String description;

    ObjectiveType(String displayName, String description) {
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
