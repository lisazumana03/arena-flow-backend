package za.co.lz.service.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.team.Player;
import za.co.lz.repository.team.PlayerRepository;

import java.util.List;
import java.util.UUID;

@Service
public class PlayerServiceImpl implements IPlayerService{

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Player create(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    @Override
    public Player update(Player player, UUID uuid) {
        return playerRepository.save(player);
    }

    @Override
    public void delete(UUID uuid) {
        playerRepository.deleteById(uuid);
    }
}
