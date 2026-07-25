package za.co.lz.repository.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.lz.domain.team.finances.Owner;

import java.util.UUID;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, UUID> {
}
