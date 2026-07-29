package za.co.lz.service.match.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.match.Standing;
import za.co.lz.domain.team.Team;
import za.co.lz.repository.match.StandingRepository;
import za.co.lz.service.match.IStandingService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StandingServiceImpl implements IStandingService {
    
    @Autowired
    private StandingRepository standingRepository;
    
    @Override
    public Standing create(Standing standing) {
        return standingRepository.save(standing);
    }
    
    @Override
    public List<Standing> findAll() {
        return standingRepository.findAll();
    }
    
    @Override
    public Optional<Standing> findById(UUID uuid) {
        return standingRepository.findById(uuid);
    }
    
    @Override
    public Standing update(Standing standing, UUID uuid) {
        return standingRepository.save(standing);
    }
    
    @Override
    public void delete(UUID uuid) {
        standingRepository.deleteById(uuid);
    }
    
    @Override
    public List<Standing> getSeasonStandings(UUID seasonId) {
        return standingRepository.findBySeason_SeasonId(seasonId);
    }
    
    @Override
    public List<Standing> getSeasonStandingsSorted(UUID seasonId) {
        return standingRepository.findBySeason_SeasonIdOrderByPointsDescGoalDifferenceDesc(seasonId);
    }
    
    @Override
    public Standing getTeamStanding(UUID seasonId, UUID teamId) {
        return standingRepository.findBySeason_SeasonIdAndTeam_TeamId(seasonId, teamId)
                .orElseThrow(() -> new IllegalArgumentException("Standing not found for team " + teamId + " in season " + seasonId));
    }
    
    @Override
    public Standing initializeTeamStanding(UUID seasonId, Team team) {
        // Check if already exists
        Optional<Standing> existing = standingRepository.findBySeason_SeasonIdAndTeam_TeamId(seasonId, team.getTeamId());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Standing already exists for team " + team.getTeamName() + " in this season");
        }
        
        Standing standing = new Standing.Builder()
                .setStandingId(UUID.randomUUID())
                .setTeam(team)
                .setPosition(0)  // Will be updated as matches are played
                .setGamesPlayed(0)
                .setWins(0)
                .setDraws(0)
                .setLosses(0)
                .setGoalsFor(0)
                .setGoalsAgainst(0)
                .setGoalDifference(0)
                .setPoints(0)
                .build();
        
        return standingRepository.save(standing);
    }
    
    @Override
    public void recalculateStandings(UUID seasonId) {
        // Recalculate positions based on points
        List<Standing> standings = getSeasonStandingsSorted(seasonId);
        
        for (int i = 0; i < standings.size(); i++) {
            standings.get(i).setPosition(i + 1);
            standingRepository.save(standings.get(i));
        }
    }
}
