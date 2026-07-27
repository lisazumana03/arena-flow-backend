package za.co.lz.repository.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.lz.domain.match.Standing;
import za.co.lz.domain.team.Team;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StandingRepository extends JpaRepository<Standing, UUID> {
    List<Standing> findBySeasonId(UUID seasonId);
    Optional<Standing> findBySeasonIdAndTeamId(UUID seasonId, UUID teamId);
    List<Standing> findBySeasonIdOrderByPointsDescGoalDifferenceDesc(UUID seasonId);
}
