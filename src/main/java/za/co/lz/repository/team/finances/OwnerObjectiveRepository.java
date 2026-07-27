package za.co.lz.repository.team.finances;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.lz.domain.team.finances.OwnerObjective;

import java.util.List;
import java.util.UUID;

@Repository
public interface OwnerObjectiveRepository extends JpaRepository<OwnerObjective, UUID> {
    List<OwnerObjective> findByOwnerId(UUID ownerId);
    List<OwnerObjective> findByTeamId(UUID teamId);
    List<OwnerObjective> findByOwnerIdAndTeamId(UUID ownerId, UUID teamId);
}
