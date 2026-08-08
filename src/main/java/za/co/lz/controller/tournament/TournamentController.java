package za.co.lz.controller.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.lz.domain.match.Season;
import za.co.lz.domain.match.Standing;
import za.co.lz.domain.tournament.Tournament;
import za.co.lz.domain.tournament.TournamentFormat;
import za.co.lz.domain.tournament.TournamentTeam;
import za.co.lz.repository.team.TeamRepository;
import za.co.lz.service.match.impl.StandingServiceImpl;
import za.co.lz.service.tournament.impl.TournamentServiceImpl;
import za.co.lz.service.tournament.impl.TournamentTeamServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    @Autowired
    private TournamentServiceImpl tournamentService;

    @Autowired
    private TournamentTeamServiceImpl tournamentTeamService;

    @Autowired
    private StandingServiceImpl standingService;

    @Autowired
    private TeamRepository teamRepository;

    @PostMapping("/create")
    public ResponseEntity<Tournament> createTournament(@RequestBody TournamentCreateRequest request) {
        Tournament tournament = tournamentService.createTournament(
                request.getTournamentName(), request.getFormat(), request.getDescription(),
                request.getPromotionSpots(), request.getRelegationSpots());
        return ResponseEntity.ok(tournament);
    }

    // Exposes ITournamentService.update(), which previously had no route — added so fields
    // that create() doesn't accept (currently just the logo) can still be set after creation.
    @PutMapping("/{tournamentId}")
    public ResponseEntity<Tournament> updateTournament(@PathVariable UUID tournamentId, @RequestBody Tournament tournament) {
        Tournament updated = tournamentService.update(tournament, tournamentId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.findAll());
    }

    @GetMapping("/{tournamentId}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable UUID tournamentId) {
        Tournament tournament = tournamentService.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with ID: " + tournamentId));
        return ResponseEntity.ok(tournament);
    }

    // e.g. POST /api/tournaments/{premierLeagueId}/editions -> creates "Premier League 2027"
    @PostMapping("/{tournamentId}/editions")
    public ResponseEntity<Season> createEdition(@PathVariable UUID tournamentId, @RequestBody EditionCreateRequest request) {
        Season season = tournamentService.createEdition(tournamentId, request.getYear(),
                request.getSeasonName(), request.getStartDate(), request.getEndDate());
        return ResponseEntity.ok(season);
    }

    @GetMapping("/{tournamentId}/editions")
    public ResponseEntity<List<Season>> getEditions(@PathVariable UUID tournamentId) {
        return ResponseEntity.ok(tournamentService.getEditions(tournamentId));
    }

    // Register a team into a specific edition, e.g. Man Utd into Premier League 2027
    @PostMapping("/editions/{seasonId}/teams")
    public ResponseEntity<TournamentTeam> registerTeam(@PathVariable UUID seasonId, @RequestBody TeamRegistrationRequest request) {
        var team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + request.getTeamId()));

        TournamentTeam entry = tournamentTeamService.registerTeam(
                seasonId, team, request.getGroupName(), request.getRegistrationDate());
        return ResponseEntity.ok(entry);
    }

    // All teams entered in this edition, e.g. everyone in Premier League 2027
    @GetMapping("/editions/{seasonId}/teams")
    public ResponseEntity<List<TournamentTeam>> getEntriesForSeason(@PathVariable UUID seasonId) {
        return ResponseEntity.ok(tournamentTeamService.getEntriesForSeason(seasonId));
    }

    @GetMapping("/editions/{seasonId}/standings")
    public ResponseEntity<List<TournamentTeam>> getStandings(@PathVariable UUID seasonId) {
        return ResponseEntity.ok(tournamentTeamService.getStandingsSorted(seasonId));
    }

    // Every tournament edition a team has ever entered, across all competitions and years
    // e.g. GET /api/tournaments/teams/{manUtdId}/entries -> Premier League 2027, FA Cup 2027, UCL 2027...
    @GetMapping("/teams/{teamId}/entries")
    public ResponseEntity<List<TournamentTeam>> getEntriesForTeam(@PathVariable UUID teamId) {
        return ResponseEntity.ok(tournamentTeamService.getEntriesForTeam(teamId));
    }

    // All group names in this edition's group stage, e.g. ["Group A", ..., "Group F"]
    @GetMapping("/editions/{seasonId}/groups")
    public ResponseEntity<List<String>> getGroupNames(@PathVariable UUID seasonId) {
        return ResponseEntity.ok(standingService.getGroupNames(seasonId));
    }

    // One group's table, sorted by points -> goal difference -> goals for
    @GetMapping("/editions/{seasonId}/groups/{groupName}")
    public ResponseEntity<List<Standing>> getGroupStandings(@PathVariable UUID seasonId, @PathVariable String groupName) {
        return ResponseEntity.ok(standingService.getGroupStandingsSorted(seasonId, groupName));
    }

    // The 3rd-place finisher from every group, ranked against each other (best first).
    // Not a real "group" - these teams never played one another; it's a cross-group comparison.
    @GetMapping("/editions/{seasonId}/third-place-ranking")
    public ResponseEntity<List<Standing>> getThirdPlaceRanking(@PathVariable UUID seasonId) {
        return ResponseEntity.ok(standingService.rankThirdPlacedTeams(seasonId));
    }

    // Advances the best N 3rd-place teams to the Round of 16, eliminates the rest.
    // e.g. POST /editions/{seasonId}/third-place-ranking/qualify?spots=4 for 6 groups -> 16-team bracket
    @PostMapping("/editions/{seasonId}/third-place-ranking/qualify")
    public ResponseEntity<List<TournamentTeam>> qualifyBestThirdPlacedTeams(
            @PathVariable UUID seasonId, @RequestParam(defaultValue = "4") int spots) {
        return ResponseEntity.ok(tournamentTeamService.qualifyBestThirdPlacedTeams(seasonId, spots));
    }

    // Inner classes for API requests
    public static class TournamentCreateRequest {
        private String tournamentName;
        private TournamentFormat format;
        private String description;
        private int promotionSpots;
        private int relegationSpots;

        public String getTournamentName() { return tournamentName; }
        public TournamentFormat getFormat() { return format; }
        public String getDescription() { return description; }
        public int getPromotionSpots() { return promotionSpots; }
        public int getRelegationSpots() { return relegationSpots; }
    }

    public static class EditionCreateRequest {
        private int year;
        private String seasonName;
        private LocalDate startDate;
        private LocalDate endDate;

        public int getYear() { return year; }
        public String getSeasonName() { return seasonName; }
        public LocalDate getStartDate() { return startDate; }
        public LocalDate getEndDate() { return endDate; }
    }

    public static class TeamRegistrationRequest {
        private UUID teamId;
        private String groupName;
        private LocalDate registrationDate;

        public UUID getTeamId() { return teamId; }
        public String getGroupName() { return groupName; }
        public LocalDate getRegistrationDate() { return registrationDate; }
    }
}
