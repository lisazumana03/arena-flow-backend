package za.co.lz.repository.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.lz.domain.match.MatchLineup;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchLineupRepository extends JpaRepository<MatchLineup, UUID> {
    List<MatchLineup> findByMatch_MatchId(UUID matchId);
    List<MatchLineup> findByMatch_MatchIdAndTeam_TeamId(UUID matchId, UUID teamId);
}
