package za.co.lz.service.team.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.finances.ObjectiveType;
import za.co.lz.domain.team.finances.Owner;
import za.co.lz.domain.team.finances.OwnerObjective;
import za.co.lz.repository.team.finances.OwnerObjectiveRepository;
import za.co.lz.service.team.finances.IOwnerObjectiveService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OwnerObjectiveServiceImpl implements IOwnerObjectiveService {
    
    @Autowired
    private OwnerObjectiveRepository objectiveRepository;
    
    @Override
    public OwnerObjective create(OwnerObjective objective) {
        return objectiveRepository.save(objective);
    }
    
    @Override
    public List<OwnerObjective> findAll() {
        return objectiveRepository.findAll();
    }
    
    @Override
    public Optional<OwnerObjective> findById(UUID uuid) {
        return objectiveRepository.findById(uuid);
    }
    
    @Override
    public OwnerObjective update(OwnerObjective objective, UUID uuid) {
        return objectiveRepository.save(objective);
    }
    
    @Override
    public void delete(UUID uuid) {
        objectiveRepository.deleteById(uuid);
    }
    
    @Override
    public List<OwnerObjective> findObjectivesByOwner(Owner owner) {
        return objectiveRepository.findByOwnerId(owner.getOwnerId());
    }
    
    @Override
    public List<OwnerObjective> findObjectivesByTeam(Team team) {
        return objectiveRepository.findByTeamId(team.getTeamId());
    }
    
    @Override
    public List<OwnerObjective> findObjectivesByOwnerAndTeam(Owner owner, Team team) {
        return objectiveRepository.findByOwnerIdAndTeamId(owner.getOwnerId(), team.getTeamId());
    }
    
    @Override
    public OwnerObjective createObjective(Owner owner, Team team, ObjectiveType objectiveType, int priority) {
        OwnerObjective objective = new OwnerObjective.Builder()
                .setObjectiveId(UUID.randomUUID())
                .setOwner(owner)
                .setTeam(team)
                .setObjectiveType(objectiveType)
                .setPriority(priority)
                .build();
        return objectiveRepository.save(objective);
    }
    
    @Override
    public OwnerObjective updateProgress(UUID objectiveId, int progressPercentage) {
        Optional<OwnerObjective> optional = objectiveRepository.findById(objectiveId);
        if (optional.isPresent()) {
            OwnerObjective objective = optional.get();
            objective.setProgressPercentage(progressPercentage);
            if (progressPercentage >= 100) {
                objective.setAchieved(true);
            }
            return objectiveRepository.save(objective);
        }
        throw new IllegalArgumentException("Objective with ID " + objectiveId + " not found");
    }
    
    @Override
    public OwnerObjective markAsAchieved(UUID objectiveId) {
        Optional<OwnerObjective> optional = objectiveRepository.findById(objectiveId);
        if (optional.isPresent()) {
            OwnerObjective objective = optional.get();
            objective.setAchieved(true);
            return objectiveRepository.save(objective);
        }
        throw new IllegalArgumentException("Objective with ID " + objectiveId + " not found");
    }
    
    @Override
    public void deleteObjective(UUID objectiveId) {
        objectiveRepository.deleteById(objectiveId);
    }

}
