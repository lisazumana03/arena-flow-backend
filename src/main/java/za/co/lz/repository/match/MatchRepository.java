package za.co.lz.repository.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.MatchStatus;
import za.co.lz.domain.team.Team;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {
    List<Match> findByHomeTeamOrAwayTeam(Team homeTeam, Team awayTeam);
    List<Match> findByStatus(MatchStatus status);
    List<Match> findBySeasonId(UUID seasonId);
    List<Match> findByHomeTeamAndSeasonId(Team homeTeam, UUID seasonId);
    List<Match> findByAwayTeamAndSeasonId(Team awayTeam, UUID seasonId);
    List<Match> findByMatchDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
