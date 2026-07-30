package za.co.lz.domain.tournament;

/**
 * Defines how a Tournament is structured. Drives how TournamentTeam.group,
 * TournamentTeam.points and TournamentTeam.qualificationStatus are interpreted.
 */
public enum TournamentFormat {
    LEAGUE,     // round-robin, decided on points (e.g. Premier League)
    KNOCKOUT,   // single/two-legged elimination (e.g. FA Cup)
    HYBRID      // group stage + knockout (e.g. UEFA Champions League)
}
