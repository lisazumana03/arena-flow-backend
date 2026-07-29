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
    List<Standing> findBySeason_SeasonId(UUID seasonId);
    Optional<Standing> findBySeason_SeasonIdAndTeam_TeamId(UUID seasonId, UUID teamId);
    List<Standing> findBySeason_SeasonIdOrderByPointsDescGoalDifferenceDesc(UUID seasonId);
}
