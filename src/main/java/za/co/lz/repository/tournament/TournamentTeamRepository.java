package za.co.lz.repository.tournament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.lz.domain.tournament.TournamentTeam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TournamentTeamRepository extends JpaRepository<TournamentTeam, UUID> {
    // All entries for a given tournament edition (e.g. everyone in Premier League 2027)
    List<TournamentTeam> findBySeason_SeasonId(UUID seasonId);

    // One team's entry in a given tournament edition
    Optional<TournamentTeam> findBySeason_SeasonIdAndTeam_TeamId(UUID seasonId, UUID teamId);

    // Every tournament edition a given team has ever entered, across all competitions/years
    // e.g. Man Utd's entries: Premier League 2027, FA Cup 2027, UEFA Champions League 2027...
    List<TournamentTeam> findByTeam_TeamId(UUID teamId);

    // Standings-style ordering within one tournament edition (LEAGUE/HYBRID group stage)
    List<TournamentTeam> findBySeason_SeasonIdOrderByPointsDesc(UUID seasonId);

    List<TournamentTeam> findBySeason_SeasonIdAndGroupName(UUID seasonId, String groupName);
}
