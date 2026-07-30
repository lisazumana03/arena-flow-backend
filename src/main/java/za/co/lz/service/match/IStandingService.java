package za.co.lz.service.match;

import za.co.lz.domain.match.Standing;
import za.co.lz.domain.team.Team;
import za.co.lz.service.IService;

import java.util.List;
import java.util.UUID;

public interface IStandingService extends IService<Standing, UUID> {
    
    // Standing queries
    List<Standing> getSeasonStandings(UUID seasonId);
    List<Standing> getSeasonStandingsSorted(UUID seasonId);
    Standing getTeamStanding(UUID seasonId, UUID teamId);
    
    // Standing updates
    Standing initializeTeamStanding(UUID seasonId, Team team);
    Standing initializeTeamStanding(UUID seasonId, Team team, String groupName);
    void recalculateStandings(UUID seasonId);

    // Group-stage support (e.g. 6 groups of 4 in a HYBRID tournament)
    List<String> getGroupNames(UUID seasonId);
    List<Standing> getGroupStandingsSorted(UUID seasonId, String groupName);

    // Third-placed team ranking: the 3rd-place finisher from every group, compared
    // against each other (points, then goal difference, then goals scored) to decide
    // which of them advance to the knockout stage alongside the group winners/runners-up.
    List<Standing> getThirdPlacedTeams(UUID seasonId);
    List<Standing> rankThirdPlacedTeams(UUID seasonId);
}
