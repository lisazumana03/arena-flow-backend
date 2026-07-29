package za.co.lz.service.match.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.match.Season;
import za.co.lz.domain.match.SeasonStatus;
import za.co.lz.repository.match.SeasonRepository;
import za.co.lz.service.match.ISeasonService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SeasonServiceImpl implements ISeasonService {
    
    @Autowired
    private SeasonRepository seasonRepository;
    
    @Override
    public Season create(Season season) {
        return seasonRepository.save(season);
    }
    
    @Override
    public List<Season> findAll() {
        return seasonRepository.findAll();
    }
    
    @Override
    public Optional<Season> findById(UUID uuid) {
        return seasonRepository.findById(uuid);
    }
    
    @Override
    public Season update(Season season, UUID uuid) {
        return seasonRepository.save(season);
    }
    
    @Override
    public void delete(UUID uuid) {
        seasonRepository.deleteById(uuid);
    }
    
    @Override
    public Season createSeason(int year, String seasonName, LocalDate startDate, LocalDate endDate) {
        // Check if season already exists
        Optional<Season> existing = seasonRepository.findByYear(year);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Season for year " + year + " already exists");
        }
        
        Season season = new Season.Builder()
                .setSeasonId(UUID.randomUUID())
                .setYear(year)
                .setSeasonName(seasonName)
                .setStartDate(startDate)
                .setEndDate(endDate)
                .setStatus(SeasonStatus.PLANNING)
                .build();
        
        return seasonRepository.save(season);
    }
    
    @Override
    public Season getCurrentSeason() {
        List<Season> activeSeason = seasonRepository.findByStatus(SeasonStatus.ACTIVE);
        if (activeSeason.isEmpty()) {
            throw new IllegalStateException("No active season found");
        }
        return activeSeason.get(0);
    }
    
    @Override
    public Season getSeason(int year) {
        return seasonRepository.findByYear(year)
                .orElseThrow(() -> new IllegalArgumentException("Season for year " + year + " not found"));
    }
    
    @Override
    public List<Season> findAllSeasons() {
        return seasonRepository.findAll();
    }
    
    @Override
    public Season startSeason(UUID seasonId) {
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new IllegalArgumentException("Season not found with ID: " + seasonId));
        
        season.setStatus(SeasonStatus.ACTIVE);
        return seasonRepository.save(season);
    }
    
    @Override
    public Season completeSeason(UUID seasonId) {
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new IllegalArgumentException("Season not found with ID: " + seasonId));
        
        season.setStatus(SeasonStatus.COMPLETED);
        
        // Evaluate financials at season end
        evaluateSeasonFinancials(seasonId);
        
        return seasonRepository.save(season);
    }
    
    @Override
    public void evaluateSeasonFinancials(UUID seasonId) {
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new IllegalArgumentException("Season not found with ID: " + seasonId));
        
        // At season end, financial health is evaluated
        // This triggers takeover mechanics if needed
        // Integration point with FinancialService
    }
    
    @Override
    public void generateMatchSchedule(UUID seasonId, List<String> teamIds) {
        // TODO: Implement round-robin schedule generation
        // This will create matches between all teams
    }
}
