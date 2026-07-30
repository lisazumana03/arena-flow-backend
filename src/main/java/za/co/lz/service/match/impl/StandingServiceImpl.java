package za.co.lz.service.match.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.match.Season;
import za.co.lz.domain.match.Standing;
import za.co.lz.domain.team.Team;
import za.co.lz.repository.match.SeasonRepository;
import za.co.lz.repository.match.StandingRepository;
import za.co.lz.service.match.IStandingService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StandingServiceImpl implements IStandingService {
    
    @Autowired
    private StandingRepository standingRepository;

    @Autowired
    private SeasonRepository seasonRepository;
    
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
        return initializeTeamStanding(seasonId, team, null);
    }

    @Override
    public Standing initializeTeamStanding(UUID seasonId, Team team, String groupName) {
        // Check if already exists
        Optional<Standing> existing = standingRepository.findBySeason_SeasonIdAndTeam_TeamId(seasonId, team.getTeamId());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Standing already exists for team " + team.getTeamName() + " in this season");
        }

        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new IllegalArgumentException("Season not found with ID: " + seasonId));
        
        Standing standing = new Standing.Builder()
                .setStandingId(UUID.randomUUID())
                .setSeason(season)
                .setTeam(team)
                .setGroupName(groupName)
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
    public List<String> getGroupNames(UUID seasonId) {
        return standingRepository.findDistinctGroupNamesBySeasonId(seasonId);
    }

    @Override
    public List<Standing> getGroupStandingsSorted(UUID seasonId, String groupName) {
        return standingRepository.findBySeason_SeasonIdAndGroupNameOrderByPointsDescGoalDifferenceDescGoalsForDesc(
                seasonId, groupName);
    }

    @Override
    public List<Standing> getThirdPlacedTeams(UUID seasonId) {
        List<String> groupNames = getGroupNames(seasonId);
        return groupNames.stream()
                .map(groupName -> getGroupStandingsSorted(seasonId, groupName))
                .filter(groupStandings -> groupStandings.size() >= 3)
                .map(groupStandings -> groupStandings.get(2)) // index 2 = 3rd place
                .toList();
    }

    @Override
    public List<Standing> rankThirdPlacedTeams(UUID seasonId) {
        // Same tiebreak order as within a group: points, then goal difference, then goals scored.
        // These teams never played each other, so head-to-head does not apply.
        Comparator<Standing> byTournamentTiebreak = Comparator
                .comparingInt(Standing::getPoints).reversed()
                .thenComparing(Comparator.comparingInt(Standing::getGoalDifference).reversed())
                .thenComparing(Comparator.comparingInt(Standing::getGoalsFor).reversed());

        return getThirdPlacedTeams(seasonId).stream()
                .sorted(byTournamentTiebreak)
                .toList();
    }
    
    @Override
    public void recalculateStandings(UUID seasonId) {
        List<String> groupNames = getGroupNames(seasonId);

        if (groupNames.isEmpty()) {
            // No group stage - recalculate positions across the whole season, as before
            List<Standing> standings = getSeasonStandingsSorted(seasonId);
            for (int i = 0; i < standings.size(); i++) {
                standings.get(i).setPosition(i + 1);
                standingRepository.save(standings.get(i));
            }
            return;
        }

        // Group stage - positions are relative to each team's own group
        for (String groupName : groupNames) {
            List<Standing> groupStandings = getGroupStandingsSorted(seasonId, groupName);
            for (int i = 0; i < groupStandings.size(); i++) {
                groupStandings.get(i).setPosition(i + 1);
                standingRepository.save(groupStandings.get(i));
            }
        }
    }
}
