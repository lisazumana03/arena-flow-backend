package za.co.lz.controller.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.lz.domain.team.SquadRegistration;
import za.co.lz.service.team.impl.SquadRegistrationServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/squad-registration")
public class SquadRegistrationController {

    @Autowired
    private SquadRegistrationServiceImpl squadRegistrationService;

    @PostMapping("/create")
    public ResponseEntity<SquadRegistration> register(@RequestBody SquadRegistration registration) {
        return ResponseEntity.ok(squadRegistrationService.create(registration));
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<SquadRegistration>> getPlayerHistory(@PathVariable UUID playerId) {
        return ResponseEntity.ok(squadRegistrationService.findByPlayer(playerId));
    }

    @GetMapping("/squad")
    public ResponseEntity<List<SquadRegistration>> getSquad(
            @RequestParam UUID teamId, @RequestParam UUID seasonId) {
        return ResponseEntity.ok(squadRegistrationService.findSquad(teamId, seasonId));
    }

    // SquadRegistrationController
@PostMapping("/renew")
public ResponseEntity<SquadRegistration> renew(
        @RequestParam UUID playerId, @RequestParam UUID teamId, @RequestParam UUID seasonId) {
    return ResponseEntity.ok(squadRegistrationService.renew(playerId, teamId, seasonId));
}
}
