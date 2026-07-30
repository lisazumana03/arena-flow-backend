package za.co.lz.repository.tournament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.lz.domain.tournament.Tournament;
import za.co.lz.domain.tournament.TournamentFormat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, UUID> {
    Optional<Tournament> findByTournamentName(String tournamentName);
    List<Tournament> findByFormat(TournamentFormat format);
}
