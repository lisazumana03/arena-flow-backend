package za.co.lz.repository.team.discipline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.lz.domain.team.discipline.PlayerSuspension;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlayerSuspensionRepository extends JpaRepository<PlayerSuspension, UUID> {
    List<PlayerSuspension> findByPlayer_PlayerId(UUID playerId);
    List<PlayerSuspension> findByPlayer_PlayerIdAndActiveTrue(UUID playerId);
    List<PlayerSuspension> findByActiveTrue();
}
