package za.co.lz.controller.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.MatchLineup;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;
import za.co.lz.service.match.impl.MatchLineupServiceImpl;
import za.co.lz.service.match.impl.MatchServiceImpl;
import za.co.lz.service.team.impl.PlayerServiceImpl;
import za.co.lz.service.team.impl.TeamServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lineup")
public class MatchLineupController {

    @Autowired
    private MatchLineupServiceImpl matchLineupService;
    @Autowired
    private MatchServiceImpl matchService;
    @Autowired
    private TeamServiceImpl teamService;
    @Autowired
    private PlayerServiceImpl playerService;

    @PostMapping("/match/{matchId}/team/{teamId}/name-player")
    public ResponseEntity<MatchLineup> namePlayer(@PathVariable UUID matchId, @PathVariable UUID teamId,
                                                   @RequestBody NamePlayerRequest request) {
        Match match = matchService.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with ID: " + matchId));
        Team team = teamService.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));
        Player player = playerService.findById(request.getPlayerId())
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + request.getPlayerId()));

        MatchLineup lineup = matchLineupService.namePlayer(match, team, player, request.isStarting(),
                request.getShirtNumber(), request.getMatchPosition());
        return ResponseEntity.ok(lineup);
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<MatchLineup>> getLineupForMatch(@PathVariable UUID matchId) {
        return ResponseEntity.ok(matchLineupService.getLineupForMatch(matchId));
    }

    @GetMapping("/match/{matchId}/team/{teamId}")
    public ResponseEntity<List<MatchLineup>> getLineupForMatchAndTeam(@PathVariable UUID matchId, @PathVariable UUID teamId) {
        return ResponseEntity.ok(matchLineupService.getLineupForMatchAndTeam(matchId, teamId));
    }

    @GetMapping("/match/{matchId}/team/{teamId}/starting-xi")
    public ResponseEntity<List<MatchLineup>> getStartingXI(@PathVariable UUID matchId, @PathVariable UUID teamId) {
        return ResponseEntity.ok(matchLineupService.getStartingXI(matchId, teamId));
    }

    @PostMapping("/match/{matchId}/player/{playerId}/substitute-off")
    public ResponseEntity<MatchLineup> substitutePlayerOff(@PathVariable UUID matchId, @PathVariable UUID playerId,
                                                             @RequestParam int minute) {
        return ResponseEntity.ok(matchLineupService.substitutePlayerOff(matchId, playerId, minute));
    }

    public static class NamePlayerRequest {
        private UUID playerId;
        private boolean starting;
        private int shirtNumber;
        private PlayerPosition matchPosition;

        public UUID getPlayerId() { return playerId; }
        public void setPlayerId(UUID playerId) { this.playerId = playerId; }
        public boolean isStarting() { return starting; }
        public void setStarting(boolean starting) { this.starting = starting; }
        public int getShirtNumber() { return shirtNumber; }
        public void setShirtNumber(int shirtNumber) { this.shirtNumber = shirtNumber; }
        public PlayerPosition getMatchPosition() { return matchPosition; }
        public void setMatchPosition(PlayerPosition matchPosition) { this.matchPosition = matchPosition; }
    }
}
