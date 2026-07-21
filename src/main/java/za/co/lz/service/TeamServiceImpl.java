package za.co.lz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import za.co.lz.domain.team.Team;
import za.co.lz.repository.TeamRepository;
import za.co.lz.service.team.ITeamService;

import java.util.List;
import java.util.UUID;

@Service
public class TeamServiceImpl implements ITeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Override
    public Team create(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    @Override
    public Team update(Team team, UUID uuid) {
        return teamRepository.save(team);
    }

    @Override
    public void delete(UUID uuid) {
        this.teamRepository.deleteById(uuid);
    }
}
