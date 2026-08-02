package za.co.lz.repository.team.discipline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.lz.domain.team.discipline.PlayerInjury;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlayerInjuryRepository extends JpaRepository<PlayerInjury, UUID> {
    List<PlayerInjury> findByPlayer_PlayerId(UUID playerId);
    List<PlayerInjury> findByPlayer_PlayerIdAndActiveTrue(UUID playerId);
    List<PlayerInjury> findByActiveTrue();
}
