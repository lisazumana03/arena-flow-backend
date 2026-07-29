package za.co.lz.repository.team.finances;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.lz.domain.team.finances.TeamBudget;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamBudgetRepository extends JpaRepository<TeamBudget, UUID> {
    List<TeamBudget> findByTeam_TeamId(UUID teamId);
    List<TeamBudget> findByOwner_OwnerId(UUID ownerId);
    Optional<TeamBudget> findByTeam_TeamIdAndBudgetYear(UUID teamId, LocalDate budgetYear);
    List<TeamBudget> findByOwner_OwnerIdAndBudgetYear(UUID ownerId, LocalDate budgetYear);
}
