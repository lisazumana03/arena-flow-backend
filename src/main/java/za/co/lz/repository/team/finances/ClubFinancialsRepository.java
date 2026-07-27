package za.co.lz.repository.team.finances;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.lz.domain.team.finances.ClubFinancials;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClubFinancialsRepository extends JpaRepository<ClubFinancials, UUID> {
    List<ClubFinancials> findByTeamId(UUID teamId);
    List<ClubFinancials> findByOwnerId(UUID ownerId);
    Optional<ClubFinancials> findByTeamIdAndFinancialYear(UUID teamId, LocalDate financialYear);
    List<ClubFinancials> findByIsTakeoverCandidateTrue();
    List<ClubFinancials> findByRequiresInterventionTrue();
}
