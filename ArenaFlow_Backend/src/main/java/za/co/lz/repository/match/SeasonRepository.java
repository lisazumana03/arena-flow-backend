package za.co.lz.repository.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.lz.domain.match.Season;
import za.co.lz.domain.match.SeasonStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeasonRepository extends JpaRepository<Season, UUID> {
    Optional<Season> findByYear(int year);
    Optional<Season> findBySeasonName(String seasonName);
    List<Season> findByStatus(SeasonStatus status);
}
