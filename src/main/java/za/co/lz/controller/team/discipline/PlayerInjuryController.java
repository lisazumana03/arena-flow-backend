package za.co.lz.controller.team.discipline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.lz.domain.match.InjurySeverity;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.discipline.PlayerInjury;
import za.co.lz.service.match.impl.MatchServiceImpl;
import za.co.lz.service.team.discipline.impl.PlayerInjuryServiceImpl;
import za.co.lz.service.team.impl.PlayerServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/injuries")
public class PlayerInjuryController {

    @Autowired
    private PlayerInjuryServiceImpl playerInjuryService;
    @Autowired
    private PlayerServiceImpl playerService;
    @Autowired
    private MatchServiceImpl matchService;

    @PostMapping("/report")
    public ResponseEntity<PlayerInjury> reportInjury(@RequestBody ReportInjuryRequest request) {
        Player player = playerService.findById(request.getPlayerId())
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + request.getPlayerId()));
        Match match = request.getTriggeringMatchId() != null
                ? matchService.findById(request.getTriggeringMatchId())
                    .orElseThrow(() -> new IllegalArgumentException("Match not found with ID: " + request.getTriggeringMatchId()))
                : null;

        PlayerInjury injury = playerInjuryService.reportInjury(player, match, request.getSeverity(), request.getInjuryDate());
        return ResponseEntity.ok(injury);
    }

    @PutMapping("/{injuryId}/expected-return")
    public ResponseEntity<PlayerInjury> updateExpectedReturn(@PathVariable UUID injuryId, @RequestParam LocalDate date) {
        return ResponseEntity.ok(playerInjuryService.updateExpectedReturn(injuryId, date));
    }

    @PostMapping("/{injuryId}/recover")
    public ResponseEntity<PlayerInjury> markRecovered(@PathVariable UUID injuryId) {
        return ResponseEntity.ok(playerInjuryService.markRecovered(injuryId));
    }

    @GetMapping("/player/{playerId}/active")
    public ResponseEntity<List<PlayerInjury>> getActiveInjuries(@PathVariable UUID playerId) {
        return ResponseEntity.ok(playerInjuryService.getActiveInjuries(playerId));
    }

    @GetMapping("/player/{playerId}/history")
    public ResponseEntity<List<PlayerInjury>> getInjuryHistory(@PathVariable UUID playerId) {
        return ResponseEntity.ok(playerInjuryService.getInjuryHistory(playerId));
    }

    @GetMapping("/player/{playerId}/is-injured")
    public ResponseEntity<Boolean> isPlayerInjured(@PathVariable UUID playerId) {
        return ResponseEntity.ok(playerInjuryService.isPlayerInjured(playerId));
    }

    public static class ReportInjuryRequest {
        private UUID playerId;
        private UUID triggeringMatchId; // optional - null if not sustained in a match
        private InjurySeverity severity;
        private LocalDate injuryDate;

        public UUID getPlayerId() { return playerId; }
        public void setPlayerId(UUID playerId) { this.playerId = playerId; }
        public UUID getTriggeringMatchId() { return triggeringMatchId; }
        public void setTriggeringMatchId(UUID triggeringMatchId) { this.triggeringMatchId = triggeringMatchId; }
        public InjurySeverity getSeverity() { return severity; }
        public void setSeverity(InjurySeverity severity) { this.severity = severity; }
        public LocalDate getInjuryDate() { return injuryDate; }
        public void setInjuryDate(LocalDate injuryDate) { this.injuryDate = injuryDate; }
    }
}
