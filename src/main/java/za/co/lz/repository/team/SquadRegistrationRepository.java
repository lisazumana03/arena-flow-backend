package za.co.lz.repository.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.lz.domain.team.SquadRegistration;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SquadRegistrationRepository extends JpaRepository<SquadRegistration, UUID> {

    // full career history for a player
    List<SquadRegistration> findByPlayer_PlayerId(UUID playerId);

    // full squad for a team in a given season
    List<SquadRegistration> findByTeam_TeamIdAndSeason_SeasonId(UUID teamId, UUID seasonId);

    // check kit number availability before registering a player
    Optional<SquadRegistration> findByTeam_TeamIdAndSeason_SeasonIdAndKitNumber(
            UUID teamId, UUID seasonId, int kitNumber);

    // SquadRegistrationRepository
    Optional<SquadRegistration> findTopByPlayer_PlayerIdOrderByRegistrationDateDesc(UUID playerId);
}