package za.co.lz.factory.tournament;

import java.time.LocalDate;

import za.co.lz.domain.tournament.DateRange;
import za.co.lz.domain.tournament.Tournament;
import za.co.lz.domain.tournament.TournamentFormat;
import za.co.lz.domain.tournament.TournamentSport;
import za.co.lz.domain.tournament.phase.Stage;
import za.co.lz.domain.tournament.phase.StageType;

public class TournamentFactory {
    public static Tournament createLeagueTournament(
        String name, TournamentSport sport, String season, 
        LocalDate startDate, LocalDate endDate) {
 
        Stage leagueStage = new Stage.Builder()
                .setStageType(StageType.LEAGUE)
                .setName(name + " - League Stage")
                .build();
 
        return new Tournament.Builder()
                .setTournamentName(name)
                .setTournamentSport(sport)
                .setFormat(TournamentFormat.LEAGUE)
                .setSeason(season)
                .setDateRange(DateRange.of(startDate, endDate))
                .stage(leagueStage)
                .build();
    }
 
    public static Tournament createKnockoutTournament(
            String name, TournamentSport sport, String season, LocalDate startDate, LocalDate endDate) {
 
        Stage knockoutStage = Stage.Builder()
                .setStageType(StageType.KNOCKOUT)
                .setName(name + " - Knockout Stage")
                .build();
 
        return new Tournament.Builder()
                .setTournamentName(name)
                .setTournamentSport(sport)
                .setFormat(TournamentFormat.KNOCKOUT)
                .setSeason(season)
                .setDateRange(DateRange.of(startDate, endDate))
                .stage(knockoutStage)
                .build();
    }
 
    /**
     * A HYBRID tournament: a league/group stage feeding into a knockout
     * stage, e.g. a continental club competition. Stage order (league = 1,
     * knockout = 2) is assigned automatically by Tournament.addStage().
     */
    public static Tournament createHybridTournament(
            String name, TournamentSport sport, String season, LocalDate overallStart, LocalDate overallEnd) {
 
        Stage leagueStage = new Stage.Builder()
                .setStageType(StageType.LEAGUE)
                .setName(name + " - Group Stage")
                .build();
 
        Stage knockoutStage = new Stage.Builder()
                .setStageType(StageType.KNOCKOUT)
                .setName(name + " - Knockout Stage")
                .build();
 
        return new Tournament.Builder()
                .setTournamentName(name)
                .setTournamentSport(sport)
                .setFormat(TournamentFormat.HYBRID)
                .setSeason(season)
                .setDateRange(DateRange.of(overallStart, overallEnd))
                .stage(leagueStage)
                .stage(knockoutStage)
                .build();
    }
}
