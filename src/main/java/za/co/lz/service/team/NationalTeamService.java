package za.co.lz.service.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import za.co.lz.repository.PlayerRepository;
import za.co.lz.repository.TeamRepository;

@Service
public class NationalTeamService{
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;
}