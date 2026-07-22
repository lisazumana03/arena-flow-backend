package za.co.lz.controller.team;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.lz.domain.team.Team;
import za.co.lz.service.team.TeamServiceImpl;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    @Autowired
    private TeamServiceImpl teamService;

    @PostMapping("/create")
    public ResponseEntity<Team> createTeam(@RequestBody Team team) {
        Team createdTeam = teamService.create(team);
        return ResponseEntity.ok(createdTeam);
    }

}
