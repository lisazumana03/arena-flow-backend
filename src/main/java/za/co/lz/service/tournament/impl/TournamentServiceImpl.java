package za.co.lz.service.tournament.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.match.Season;
import za.co.lz.domain.match.SeasonStatus;
import za.co.lz.domain.tournament.Tournament;
import za.co.lz.domain.tournament.TournamentFormat;
import za.co.lz.repository.match.SeasonRepository;
import za.co.lz.repository.tournament.TournamentRepository;
import za.co.lz.service.tournament.ITournamentService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TournamentServiceImpl implements ITournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Override
    public Tournament create(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    @Override
    public List<Tournament> findAll() {
        return tournamentRepository.findAll();
    }

    @Override
    public Optional<Tournament> findById(UUID uuid) {
        return tournamentRepository.findById(uuid);
    }

    @Override
    public Tournament update(Tournament tournament, UUID uuid) {
        return tournamentRepository.save(tournament);
    }

    @Override
    public void delete(UUID uuid) {
        tournamentRepository.deleteById(uuid);
    }

    @Override
    public Tournament createTournament(String tournamentName, TournamentFormat format, String description,
                                        int promotionSpots, int relegationSpots) {
        Optional<Tournament> existing = tournamentRepository.findByTournamentName(tournamentName);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Tournament '" + tournamentName + "' already exists");
        }

        Tournament tournament = new Tournament.Builder()
                .setTournamentId(UUID.randomUUID())
                .setTournamentName(tournamentName)
                .setFormat(format)
                .setDescription(description)
                .setPromotionSpots(promotionSpots)
                .setRelegationSpots(relegationSpots)
                .build();

        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament getTournament(String tournamentName) {
        return tournamentRepository.findByTournamentName(tournamentName)
                .orElseThrow(() -> new IllegalArgumentException("Tournament '" + tournamentName + "' not found"));
    }

    @Override
    public List<Tournament> findByFormat(TournamentFormat format) {
        return tournamentRepository.findByFormat(format);
    }

    @Override
    public Season createEdition(UUID tournamentId, int year, String seasonName, LocalDate startDate, LocalDate endDate) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with ID: " + tournamentId));

        Optional<Season> existing = seasonRepository.findBySeasonName(seasonName);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Season '" + seasonName + "' already exists");
        }

        Season season = new Season.Builder()
                .setSeasonId(UUID.randomUUID())
                .setTournament(tournament)
                .setYear(year)
                .setSeasonName(seasonName)
                .setStartDate(startDate)
                .setEndDate(endDate)
                .setStatus(SeasonStatus.PLANNING)
                .build();

        return seasonRepository.save(season);
    }

    @Override
    public List<Season> getEditions(UUID tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with ID: " + tournamentId));
        return tournament.getSeasons();
    }
}
