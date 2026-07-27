package za.co.lz.service.match;

import za.co.lz.domain.match.Season;
import za.co.lz.service.IService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ISeasonService extends IService<Season, UUID> {
    
    // Season operations
    Season createSeason(int year, String seasonName, LocalDate startDate, LocalDate endDate);
    Season getCurrentSeason();
    Season getSeason(int year);
    List<Season> findAllSeasons();
    
    // Season management
    Season startSeason(UUID seasonId);
    Season completeSeason(UUID seasonId);
    void evaluateSeasonFinancials(UUID seasonId);
    
    // Match scheduling
    void generateMatchSchedule(UUID seasonId, List<String> teamIds);
}
