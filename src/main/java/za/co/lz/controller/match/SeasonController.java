package za.co.lz.controller.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.lz.domain.match.Season;
import za.co.lz.domain.match.Standing;
import za.co.lz.service.match.impl.SeasonServiceImpl;
import za.co.lz.service.match.impl.StandingServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/seasons")
public class SeasonController {
    
    @Autowired
    private SeasonServiceImpl seasonService;
    
    @Autowired
    private StandingServiceImpl standingService;
    
    @PostMapping("/create")
    public ResponseEntity<Season> createSeason(@RequestBody SeasonCreateRequest request) {
        Season season = seasonService.createSeason(request.getYear(), request.getSeasonName(), 
                request.getStartDate(), request.getEndDate());
        return ResponseEntity.ok(season);
    }
    
    @GetMapping("/current")
    public ResponseEntity<Season> getCurrentSeason() {
        Season season = seasonService.getCurrentSeason();
        return ResponseEntity.ok(season);
    }
    
    @GetMapping("/year/{year}")
    public ResponseEntity<Season> getSeason(@PathVariable int year) {
        Season season = seasonService.getSeason(year);
        return ResponseEntity.ok(season);
    }
    
    @GetMapping("/{seasonId}")
    public ResponseEntity<Season> getSeasonById(@PathVariable UUID seasonId) {
        Season season = seasonService.findById(seasonId)
                .orElseThrow(() -> new IllegalArgumentException("Season not found with ID: " + seasonId));
        return ResponseEntity.ok(season);
    }
    
    @GetMapping
    public ResponseEntity<List<Season>> getAllSeasons() {
        List<Season> seasons = seasonService.findAllSeasons();
        return ResponseEntity.ok(seasons);
    }
    
    @PostMapping("/{seasonId}/start")
    public ResponseEntity<Season> startSeason(@PathVariable UUID seasonId) {
        Season season = seasonService.startSeason(seasonId);
        return ResponseEntity.ok(season);
    }
    
    @PostMapping("/{seasonId}/complete")
    public ResponseEntity<Season> completeSeason(@PathVariable UUID seasonId) {
        Season season = seasonService.completeSeason(seasonId);
        return ResponseEntity.ok(season);
    }
    
    @GetMapping("/{seasonId}/standings")
    public ResponseEntity<List<Standing>> getStandings(@PathVariable UUID seasonId) {
        List<Standing> standings = standingService.getSeasonStandingsSorted(seasonId);
        return ResponseEntity.ok(standings);
    }
    
    @GetMapping("/{seasonId}/standings/{teamId}")
    public ResponseEntity<Standing> getTeamStanding(
            @PathVariable UUID seasonId,
            @PathVariable UUID teamId) {
        Standing standing = standingService.getTeamStanding(seasonId, teamId);
        return ResponseEntity.ok(standing);
    }
    
    @PostMapping("/{seasonId}/recalculate-standings")
    public ResponseEntity<String> recalculateStandings(@PathVariable UUID seasonId) {
        standingService.recalculateStandings(seasonId);
        return ResponseEntity.ok("Standings recalculated for season " + seasonId);
    }
    
    // Inner class for API
    public static class SeasonCreateRequest {
        private int year;
        private String seasonName;
        private LocalDate startDate;
        private LocalDate endDate;
        
        public int getYear() { return year; }
        public String getSeasonName() { return seasonName; }
        public LocalDate getStartDate() { return startDate; }
        public LocalDate getEndDate() { return endDate; }
    }
}
