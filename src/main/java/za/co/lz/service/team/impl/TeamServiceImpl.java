package za.co.lz.service.team.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import za.co.lz.domain.team.Team;
import za.co.lz.repository.team.TeamRepository;
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
    public Team update(Team team, UUID teamId) {
        if (teamId == null) {
            return team;
        }
        return teamRepository.save(team);
    }

    @Override
    public void delete(UUID teamId) {
        this.teamRepository.deleteById(teamId);
    }
}
