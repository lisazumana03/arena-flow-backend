package za.co.lz.service.team.finances;

import za.co.lz.domain.team.finances.OwnerObjective;
import za.co.lz.domain.team.finances.ObjectiveType;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.finances.Owner;
import za.co.lz.service.IService;

import java.util.List;
import java.util.UUID;

public interface IOwnerObjectiveService extends IService<OwnerObjective, UUID> {
    
    // Find all objectives for an owner
    List<OwnerObjective> findObjectivesByOwner(Owner owner);
    
    // Find all objectives for a specific team
    List<OwnerObjective> findObjectivesByTeam(Team team);
    
    // Find all objectives for an owner's specific team
    List<OwnerObjective> findObjectivesByOwnerAndTeam(Owner owner, Team team);
    
    // Create an objective for a team
    OwnerObjective createObjective(Owner owner, Team team, ObjectiveType objectiveType, int priority);
    
    // Update objective progress
    OwnerObjective updateProgress(UUID objectiveId, int progressPercentage);
    
    // Mark objective as achieved
    OwnerObjective markAsAchieved(UUID objectiveId);
    
    // Delete an objective
    void deleteObjective(UUID objectiveId);
}
