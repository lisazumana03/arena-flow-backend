package za.co.lz.repository.match;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.lz.domain.match.Officials;

import java.util.Optional;
import java.util.UUID;

public interface OfficialsRepository extends JpaRepository<Officials, UUID> {
    Optional<Officials> findByMatch_MatchId(UUID matchId);
}
