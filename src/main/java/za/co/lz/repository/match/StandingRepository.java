package za.co.lz.repository.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // One group's table (e.g. Group A), sorted using standard tiebreak order
    List<Standing> findBySeason_SeasonIdAndGroupNameOrderByPointsDescGoalDifferenceDescGoalsForDesc(
            UUID seasonId, String groupName);

    // All distinct group names used in this season's group stage, e.g. ["Group A", ... "Group F"]
    @Query("SELECT DISTINCT s.groupName FROM Standing s WHERE s.season.seasonId = :seasonId AND s.groupName IS NOT NULL ORDER BY s.groupName")
    List<String> findDistinctGroupNamesBySeasonId(@Param("seasonId") UUID seasonId);
}
