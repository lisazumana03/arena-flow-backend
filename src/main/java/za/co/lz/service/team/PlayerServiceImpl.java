package za.co.lz.service.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.repository.team.PlayerRepository;
import za.co.lz.repository.team.TeamRepository;

@Service
public class PlayerServiceImpl implements IPlayerService{
    @Autowired
    private PlayerRepository playerRepository;
}
