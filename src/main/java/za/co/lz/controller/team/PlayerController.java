package za.co.lz.controller.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.lz.domain.team.Player;
import za.co.lz.repository.team.PlayerRepository;
import za.co.lz.service.team.PlayerServiceImpl;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
    @Autowired
    private PlayerServiceImpl playerService;

    @PostMapping("/create")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player){
        Player createdPlayer = playerService.create(player);
        return ResponseEntity.ok(createdPlayer);
    }
}
