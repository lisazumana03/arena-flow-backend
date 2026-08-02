package za.co.lz.repository.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.lz.domain.match.MatchEvent;
import za.co.lz.domain.match.MatchEventType;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchEventRepository extends JpaRepository<MatchEvent, UUID> {
    List<MatchEvent> findByMatch_MatchIdOrderByMinuteAsc(UUID matchId);
    List<MatchEvent> findByMatch_MatchIdAndEventType(UUID matchId, MatchEventType eventType);
    List<MatchEvent> findByPlayer_PlayerId(UUID playerId);
}
