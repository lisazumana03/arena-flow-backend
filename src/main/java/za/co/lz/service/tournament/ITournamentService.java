package za.co.lz.service.tournament;

import za.co.lz.domain.match.Season;
import za.co.lz.domain.tournament.Tournament;
import za.co.lz.domain.tournament.TournamentFormat;
import za.co.lz.service.IService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ITournamentService extends IService<Tournament, UUID> {

    Tournament createTournament(String tournamentName, TournamentFormat format, String description);

    Tournament getTournament(String tournamentName);

    List<Tournament> findByFormat(TournamentFormat format);

    // Starts a new yearly edition (Season) of this tournament, e.g. "Premier League 2027"
    Season createEdition(UUID tournamentId, int year, String seasonName, LocalDate startDate, LocalDate endDate);

    // All editions (Seasons) this tournament has ever run
    List<Season> getEditions(UUID tournamentId);
}
