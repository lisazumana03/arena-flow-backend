package za.co.lz.controller.team;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.lz.domain.team.Player;
import za.co.lz.service.team.impl.PlayerServiceImpl;

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

    @GetMapping("/all")
    public ResponseEntity<List<Player>> getAllPlayers() {
        List<Player> players = playerService.findAll();
        return ResponseEntity.ok(players);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable UUID id, @RequestBody Player player) {
        Player updatedPlayer = playerService.update(player, id);
        return ResponseEntity.ok(updatedPlayer);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable UUID id) {   
        playerService.delete(id);
            return ResponseEntity.noContent().build();
    }
}
