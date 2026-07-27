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
    List<TeamBudget> findByTeamId(UUID teamId);
    List<TeamBudget> findByOwnerId(UUID ownerId);
    Optional<TeamBudget> findByTeamIdAndBudgetYear(UUID teamId, LocalDate budgetYear);
    List<TeamBudget> findByOwnerIdAndBudgetYear(UUID ownerId, LocalDate budgetYear);
}
