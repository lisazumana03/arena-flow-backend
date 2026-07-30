package za.co.lz.service.tournament;

import za.co.lz.domain.team.Team;
import za.co.lz.domain.tournament.QualificationStatus;
import za.co.lz.domain.tournament.TournamentTeam;
import za.co.lz.service.IService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ITournamentTeamService extends IService<TournamentTeam, UUID> {

    // Register a team into a specific tournament edition (Season)
    TournamentTeam registerTeam(UUID seasonId, Team team, String groupName, LocalDate registrationDate);

    // All entries for one tournament edition, e.g. everyone in Premier League 2027
    List<TournamentTeam> getEntriesForSeason(UUID seasonId);

    // Every tournament edition entry a team has ever had, across all competitions/years
    // e.g. Man Utd: Premier League 2027, FA Cup 2027, UEFA Champions League 2027
    List<TournamentTeam> getEntriesForTeam(UUID teamId);

    TournamentTeam getEntry(UUID seasonId, UUID teamId);

    List<TournamentTeam> getStandingsSorted(UUID seasonId);

    List<TournamentTeam> getGroup(UUID seasonId, String groupName);

    TournamentTeam addPoints(UUID seasonId, UUID teamId, int pointsToAdd);

    TournamentTeam updateQualificationStatus(UUID seasonId, UUID teamId, QualificationStatus status);

    // Ranks the six 3rd-place group finishers and advances the best `spotsAvailable` of them
    // (their TournamentTeam.qualificationStatus -> ROUND_OF_16), eliminating the rest.
    // Returns the full ranking, best-to-worst, for display.
    List<TournamentTeam> qualifyBestThirdPlacedTeams(UUID seasonId, int spotsAvailable);
}
