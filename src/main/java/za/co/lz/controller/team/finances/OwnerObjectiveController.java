package za.co.lz.controller.team.finances;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.lz.domain.team.finances.ObjectiveType;
import za.co.lz.domain.team.finances.Owner;
import za.co.lz.domain.team.finances.OwnerObjective;
import za.co.lz.service.team.impl.OwnerObjectiveServiceImpl;
import za.co.lz.service.team.impl.OwnerServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/objectives")
public class OwnerObjectiveController {
    
    @Autowired
    private OwnerObjectiveServiceImpl objectiveService;
    
    @Autowired
    private OwnerServiceImpl ownerService;
    
    @GetMapping
    public ResponseEntity<List<OwnerObjective>> getAllObjectives() {
        return ResponseEntity.ok(objectiveService.findAll());
    }
    
    @GetMapping("/{objectiveId}")
    public ResponseEntity<OwnerObjective> getObjective(@PathVariable UUID objectiveId) {
        // Note: This requires adding a findById method to the service interface
        return ResponseEntity.ok(null); // Placeholder
    }
    
    @PostMapping("/create")
    public ResponseEntity<OwnerObjective> createObjective(@RequestBody OwnerObjective objective) {
        OwnerObjective created = objectiveService.create(objective);
        return ResponseEntity.ok(created);
    }
    
    @PutMapping("/{objectiveId}/progress")
    public ResponseEntity<OwnerObjective> updateProgress(
            @PathVariable UUID objectiveId,
            @RequestParam int progressPercentage) {
        OwnerObjective updated = objectiveService.updateProgress(objectiveId, progressPercentage);
        return ResponseEntity.ok(updated);
    }
    
    @PutMapping("/{objectiveId}/achieve")
    public ResponseEntity<OwnerObjective> markAsAchieved(@PathVariable UUID objectiveId) {
        OwnerObjective achieved = objectiveService.markAsAchieved(objectiveId);
        return ResponseEntity.ok(achieved);
    }
    
    @DeleteMapping("/{objectiveId}")
    public ResponseEntity<Void> deleteObjective(@PathVariable UUID objectiveId) {
        objectiveService.deleteObjective(objectiveId);
        return ResponseEntity.noContent().build();
    }
}
