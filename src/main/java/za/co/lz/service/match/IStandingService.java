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
    void recalculateStandings(UUID seasonId);
}
