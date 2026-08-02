package za.co.lz.controller.team.discipline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.lz.domain.match.DisciplinaryOffence;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.discipline.PlayerSuspension;
import za.co.lz.service.match.impl.MatchServiceImpl;
import za.co.lz.service.team.discipline.impl.PlayerSuspensionServiceImpl;
import za.co.lz.service.team.impl.PlayerServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/suspensions")
public class PlayerSuspensionController {

    @Autowired
    private PlayerSuspensionServiceImpl playerSuspensionService;
    @Autowired
    private PlayerServiceImpl playerService;
    @Autowired
    private MatchServiceImpl matchService;

    @PostMapping("/issue")
    public ResponseEntity<PlayerSuspension> issueSuspension(@RequestBody IssueSuspensionRequest request) {
        Player player = playerService.findById(request.getPlayerId())
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + request.getPlayerId()));
        Match match = matchService.findById(request.getTriggeringMatchId())
                .orElseThrow(() -> new IllegalArgumentException("Match not found with ID: " + request.getTriggeringMatchId()));

        PlayerSuspension suspension = request.getGamesBanned() != null
                ? playerSuspensionService.issueSuspension(player, match, request.getOffence(), request.getGamesBanned())
                : playerSuspensionService.issueSuspension(player, match, request.getOffence());
        return ResponseEntity.ok(suspension);
    }

    @GetMapping("/player/{playerId}/active")
    public ResponseEntity<List<PlayerSuspension>> getActiveSuspensions(@PathVariable UUID playerId) {
        return ResponseEntity.ok(playerSuspensionService.getActiveSuspensions(playerId));
    }

    @GetMapping("/player/{playerId}/history")
    public ResponseEntity<List<PlayerSuspension>> getSuspensionHistory(@PathVariable UUID playerId) {
        return ResponseEntity.ok(playerSuspensionService.getSuspensionHistory(playerId));
    }

    @GetMapping("/player/{playerId}/is-suspended")
    public ResponseEntity<Boolean> isPlayerSuspended(@PathVariable UUID playerId) {
        return ResponseEntity.ok(playerSuspensionService.isPlayerSuspended(playerId));
    }

    public static class IssueSuspensionRequest {
        private UUID playerId;
        private UUID triggeringMatchId;
        private DisciplinaryOffence offence;
        private Integer gamesBanned; // optional committee override

        public UUID getPlayerId() { return playerId; }
        public void setPlayerId(UUID playerId) { this.playerId = playerId; }
        public UUID getTriggeringMatchId() { return triggeringMatchId; }
        public void setTriggeringMatchId(UUID triggeringMatchId) { this.triggeringMatchId = triggeringMatchId; }
        public DisciplinaryOffence getOffence() { return offence; }
        public void setOffence(DisciplinaryOffence offence) { this.offence = offence; }
        public Integer getGamesBanned() { return gamesBanned; }
        public void setGamesBanned(Integer gamesBanned) { this.gamesBanned = gamesBanned; }
    }
}
