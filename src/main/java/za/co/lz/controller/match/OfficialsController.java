package za.co.lz.controller.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.Officials;
import za.co.lz.service.match.impl.MatchServiceImpl;
import za.co.lz.service.match.impl.OfficialsServiceImpl;

import java.util.UUID;

@RestController
@RequestMapping("/api/officials")
public class OfficialsController {

    @Autowired
    private OfficialsServiceImpl officialsService;

    @Autowired
    private MatchServiceImpl matchService;

    @PostMapping("/match/{matchId}/assign")
    public ResponseEntity<Officials> assignOfficials(@PathVariable UUID matchId, @RequestBody OfficialsRequest request) {
        Match match = matchService.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with ID: " + matchId));
        Officials officials = officialsService.assignOfficials(match, request.getReferee(),
                request.getAssistantReferee1(), request.getAssistantReferee2(),
                request.getFourthOfficial(), request.getMatchCommissioner());
        return ResponseEntity.ok(officials);
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<Officials> getOfficialsForMatch(@PathVariable UUID matchId) {
        Officials officials = officialsService.getByMatch(matchId)
                .orElseThrow(() -> new IllegalArgumentException("No officials assigned to match: " + matchId));
        return ResponseEntity.ok(officials);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Officials> updateOfficials(@PathVariable UUID id, @RequestBody Officials officials) {
        return ResponseEntity.ok(officialsService.update(officials, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOfficials(@PathVariable UUID id) {
        officialsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    public static class OfficialsRequest {
        private String referee;
        private String assistantReferee1;
        private String assistantReferee2;
        private String fourthOfficial;
        private String matchCommissioner;

        public String getReferee() { return referee; }
        public void setReferee(String referee) { this.referee = referee; }
        public String getAssistantReferee1() { return assistantReferee1; }
        public void setAssistantReferee1(String assistantReferee1) { this.assistantReferee1 = assistantReferee1; }
        public String getAssistantReferee2() { return assistantReferee2; }
        public void setAssistantReferee2(String assistantReferee2) { this.assistantReferee2 = assistantReferee2; }
        public String getFourthOfficial() { return fourthOfficial; }
        public void setFourthOfficial(String fourthOfficial) { this.fourthOfficial = fourthOfficial; }
        public String getMatchCommissioner() { return matchCommissioner; }
        public void setMatchCommissioner(String matchCommissioner) { this.matchCommissioner = matchCommissioner; }
    }
}
