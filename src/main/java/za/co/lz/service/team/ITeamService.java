package za.co.lz.service.team;

import java.util.List;
import java.util.UUID;

import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.finances.Owner;
import za.co.lz.service.IService;

public interface ITeamService extends IService<Team, UUID> {
    // Query teams owned by a specific owner
    List<Team> findTeamsByOwner(Owner owner);
}
