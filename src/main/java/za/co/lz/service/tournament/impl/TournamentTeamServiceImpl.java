package za.co.lz.service.tournament.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.match.Season;
import za.co.lz.domain.match.Standing;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.tournament.QualificationStatus;
import za.co.lz.domain.tournament.TournamentTeam;
import za.co.lz.repository.match.SeasonRepository;
import za.co.lz.repository.tournament.TournamentTeamRepository;
import za.co.lz.service.match.impl.StandingServiceImpl;
import za.co.lz.service.tournament.ITournamentTeamService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TournamentTeamServiceImpl implements ITournamentTeamService {

    @Autowired
    private TournamentTeamRepository tournamentTeamRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private StandingServiceImpl standingService;

    @Override
    public TournamentTeam create(TournamentTeam tournamentTeam) {
        return tournamentTeamRepository.save(tournamentTeam);
    }

    @Override
    public List<TournamentTeam> findAll() {
        return tournamentTeamRepository.findAll();
    }

    @Override
    public Optional<TournamentTeam> findById(UUID uuid) {
        return tournamentTeamRepository.findById(uuid);
    }

    @Override
    public TournamentTeam update(TournamentTeam tournamentTeam, UUID uuid) {
        return tournamentTeamRepository.save(tournamentTeam);
    }

    @Override
    public void delete(UUID uuid) {
        tournamentTeamRepository.deleteById(uuid);
    }

    @Override
    public TournamentTeam registerTeam(UUID seasonId, Team team, String groupName, LocalDate registrationDate) {
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new IllegalArgumentException("Season not found with ID: " + seasonId));

        Optional<TournamentTeam> existing = tournamentTeamRepository.findBySeason_SeasonIdAndTeam_TeamId(seasonId, team.getTeamId());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Team " + team.getTeamName() + " is already registered for this tournament edition");
        }

        TournamentTeam entry = new TournamentTeam.Builder()
                .setTournamentTeamId(UUID.randomUUID())
                .setSeason(season)
                .setTeam(team)
                .setGroupName(groupName)
                .setPoints(0)
                .setQualificationStatus(QualificationStatus.REGISTERED)
                .setRegistrationDate(registrationDate)
                .build();

        TournamentTeam saved = tournamentTeamRepository.save(entry);

        // Also seed the group-scoped Standing row so completed matches (via
        // MatchServiceImpl.updateStandingsAfterMatch) accumulate wins/goals/points
        // within this team's group rather than the whole season.
        standingService.initializeTeamStanding(seasonId, team, groupName);

        return saved;
    }

    @Override
    public List<TournamentTeam> getEntriesForSeason(UUID seasonId) {
        return tournamentTeamRepository.findBySeason_SeasonId(seasonId);
    }

    @Override
    public List<TournamentTeam> getEntriesForTeam(UUID teamId) {
        return tournamentTeamRepository.findByTeam_TeamId(teamId);
    }

    @Override
    public TournamentTeam getEntry(UUID seasonId, UUID teamId) {
        return tournamentTeamRepository.findBySeason_SeasonIdAndTeam_TeamId(seasonId, teamId)
                .orElseThrow(() -> new IllegalArgumentException("No entry found for team " + teamId + " in season " + seasonId));
    }

    @Override
    public List<TournamentTeam> getStandingsSorted(UUID seasonId) {
        return tournamentTeamRepository.findBySeason_SeasonIdOrderByPointsDesc(seasonId);
    }

    @Override
    public List<TournamentTeam> getGroup(UUID seasonId, String groupName) {
        return tournamentTeamRepository.findBySeason_SeasonIdAndGroupName(seasonId, groupName);
    }

    @Override
    public TournamentTeam addPoints(UUID seasonId, UUID teamId, int pointsToAdd) {
        TournamentTeam entry = getEntry(seasonId, teamId);
        entry.setPoints(entry.getPoints() + pointsToAdd);
        return tournamentTeamRepository.save(entry);
    }

    @Override
    public TournamentTeam updateQualificationStatus(UUID seasonId, UUID teamId, QualificationStatus status) {
        TournamentTeam entry = getEntry(seasonId, teamId);
        entry.setQualificationStatus(status);
        return tournamentTeamRepository.save(entry);
    }

    @Override
    public List<TournamentTeam> qualifyBestThirdPlacedTeams(UUID seasonId, int spotsAvailable) {
        // Best-to-worst ranking of the 3rd-place finisher from every group
        List<Standing> ranking = standingService.rankThirdPlacedTeams(seasonId);

        List<TournamentTeam> result = new ArrayList<>();
        for (int i = 0; i < ranking.size(); i++) {
            Standing standing = ranking.get(i);
            TournamentTeam entry = getEntry(seasonId, standing.getTeam().getTeamId());

            entry.setQualificationStatus(i < spotsAvailable
                    ? QualificationStatus.ROUND_OF_16
                    : QualificationStatus.ELIMINATED);

            result.add(tournamentTeamRepository.save(entry));
        }

        return result;
    }
}
