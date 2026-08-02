package za.co.lz.controller.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.lz.domain.match.*;
import za.co.lz.service.match.impl.MatchEventServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/match-event")
public class MatchEventController {

    @Autowired
    private MatchEventServiceImpl matchEventService;

    @PostMapping("/{matchId}/goal")
    public ResponseEntity<MatchEvent> recordGoal(@PathVariable UUID matchId, @RequestBody GoalRequest request) {
        MatchEvent event = matchEventService.recordGoal(matchId, request.getScoringTeamId(), request.getScorerId(),
                request.getAssistedById(), request.getGoalType(), request.getMinute(), request.getStoppageMinute());
        return ResponseEntity.ok(event);
    }

    @PostMapping("/{matchId}/card")
    public ResponseEntity<MatchEvent> recordCard(@PathVariable UUID matchId, @RequestBody CardRequest request) {
        MatchEvent event = matchEventService.recordCard(matchId, request.getTeamId(), request.getPlayerId(),
                request.getCardType(), request.getOffence(), request.getMinute(), request.getStoppageMinute());
        return ResponseEntity.ok(event);
    }

    @PostMapping("/{matchId}/substitution")
    public ResponseEntity<MatchEvent> recordSubstitution(@PathVariable UUID matchId, @RequestBody SubstitutionRequest request) {
        MatchEvent event = matchEventService.recordSubstitution(matchId, request.getTeamId(),
                request.getPlayerOffId(), request.getPlayerOnId(), request.getMinute());
        return ResponseEntity.ok(event);
    }

    @PostMapping("/{matchId}/corner")
    public ResponseEntity<MatchEvent> recordCorner(@PathVariable UUID matchId, @RequestParam UUID teamId, @RequestParam int minute) {
        return ResponseEntity.ok(matchEventService.recordCorner(matchId, teamId, minute));
    }

    @PostMapping("/{matchId}/free-kick")
    public ResponseEntity<MatchEvent> recordFreeKick(@PathVariable UUID matchId, @RequestBody FreeKickRequest request) {
        MatchEvent event = matchEventService.recordFreeKick(matchId, request.getTeamId(), request.getPlayerId(),
                request.getMinute(), request.getNotes());
        return ResponseEntity.ok(event);
    }

    @PostMapping("/{matchId}/injury")
    public ResponseEntity<MatchEvent> recordInjury(@PathVariable UUID matchId, @RequestBody InjuryRequest request) {
        MatchEvent event = matchEventService.recordInjury(matchId, request.getTeamId(), request.getPlayerId(),
                request.getSeverity(), request.getMinute(), request.getNotes());
        return ResponseEntity.ok(event);
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<MatchEvent>> getMatchEvents(@PathVariable UUID matchId) {
        return ResponseEntity.ok(matchEventService.getMatchEvents(matchId));
    }

    @GetMapping("/match/{matchId}/type/{eventType}")
    public ResponseEntity<List<MatchEvent>> getMatchEventsByType(@PathVariable UUID matchId, @PathVariable MatchEventType eventType) {
        return ResponseEntity.ok(matchEventService.getMatchEventsByType(matchId, eventType));
    }

    @PostMapping("/{matchId}/finalize")
    public ResponseEntity<Match> finalizeMatch(@PathVariable UUID matchId) {
        return ResponseEntity.ok(matchEventService.finalizeMatch(matchId));
    }

    public static class GoalRequest {
        private UUID scoringTeamId;
        private UUID scorerId;
        private UUID assistedById;
        private MatchEventType goalType;
        private int minute;
        private Integer stoppageMinute;

        public UUID getScoringTeamId() { return scoringTeamId; }
        public void setScoringTeamId(UUID scoringTeamId) { this.scoringTeamId = scoringTeamId; }
        public UUID getScorerId() { return scorerId; }
        public void setScorerId(UUID scorerId) { this.scorerId = scorerId; }
        public UUID getAssistedById() { return assistedById; }
        public void setAssistedById(UUID assistedById) { this.assistedById = assistedById; }
        public MatchEventType getGoalType() { return goalType; }
        public void setGoalType(MatchEventType goalType) { this.goalType = goalType; }
        public int getMinute() { return minute; }
        public void setMinute(int minute) { this.minute = minute; }
        public Integer getStoppageMinute() { return stoppageMinute; }
        public void setStoppageMinute(Integer stoppageMinute) { this.stoppageMinute = stoppageMinute; }
    }

    public static class CardRequest {
        private UUID teamId;
        private UUID playerId;
        private MatchEventType cardType;
        private DisciplinaryOffence offence;
        private int minute;
        private Integer stoppageMinute;

        public UUID getTeamId() { return teamId; }
        public void setTeamId(UUID teamId) { this.teamId = teamId; }
        public UUID getPlayerId() { return playerId; }
        public void setPlayerId(UUID playerId) { this.playerId = playerId; }
        public MatchEventType getCardType() { return cardType; }
        public void setCardType(MatchEventType cardType) { this.cardType = cardType; }
        public DisciplinaryOffence getOffence() { return offence; }
        public void setOffence(DisciplinaryOffence offence) { this.offence = offence; }
        public int getMinute() { return minute; }
        public void setMinute(int minute) { this.minute = minute; }
        public Integer getStoppageMinute() { return stoppageMinute; }
        public void setStoppageMinute(Integer stoppageMinute) { this.stoppageMinute = stoppageMinute; }
    }

    public static class SubstitutionRequest {
        private UUID teamId;
        private UUID playerOffId;
        private UUID playerOnId;
        private int minute;

        public UUID getTeamId() { return teamId; }
        public void setTeamId(UUID teamId) { this.teamId = teamId; }
        public UUID getPlayerOffId() { return playerOffId; }
        public void setPlayerOffId(UUID playerOffId) { this.playerOffId = playerOffId; }
        public UUID getPlayerOnId() { return playerOnId; }
        public void setPlayerOnId(UUID playerOnId) { this.playerOnId = playerOnId; }
        public int getMinute() { return minute; }
        public void setMinute(int minute) { this.minute = minute; }
    }

    public static class FreeKickRequest {
        private UUID teamId;
        private UUID playerId;
        private int minute;
        private String notes;

        public UUID getTeamId() { return teamId; }
        public void setTeamId(UUID teamId) { this.teamId = teamId; }
        public UUID getPlayerId() { return playerId; }
        public void setPlayerId(UUID playerId) { this.playerId = playerId; }
        public int getMinute() { return minute; }
        public void setMinute(int minute) { this.minute = minute; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    public static class InjuryRequest {
        private UUID teamId;
        private UUID playerId;
        private InjurySeverity severity;
        private int minute;
        private String notes;

        public UUID getTeamId() { return teamId; }
        public void setTeamId(UUID teamId) { this.teamId = teamId; }
        public UUID getPlayerId() { return playerId; }
        public void setPlayerId(UUID playerId) { this.playerId = playerId; }
        public InjurySeverity getSeverity() { return severity; }
        public void setSeverity(InjurySeverity severity) { this.severity = severity; }
        public int getMinute() { return minute; }
        public void setMinute(int minute) { this.minute = minute; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}
