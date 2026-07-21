package za.co.lz.domain.tournament.phase;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class StandingsEntry {
    @Id
    @GeneratedValue
    private UUID id;
 
    @Column(name = "team_id", nullable = false)
    private UUID teamId;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_stage_details_id")
    private LeagueStageDetails leagueStageDetails;
 
    private int played;
    private int won;
    private int drawn;
    private int lost;
    private int pointsFor;
    private int pointsAgainst;
    private int points;
 
    protected StandingsEntry() {
        // required by JPA
    }
 
    public StandingsEntry(UUID teamId) {
        this.id = UUID.randomUUID();
        this.teamId = teamId;
    }
 
    public void assignLeagueStageDetails(LeagueStageDetails details) {
        this.leagueStageDetails = details;
    }
 
    public void recordResult(int scoredFor, int scoredAgainst, int pointsEarned) {
        this.played++;
        this.pointsFor += scoredFor;
        this.pointsAgainst += scoredAgainst;
        this.points += pointsEarned;
        if (scoredFor > scoredAgainst) {
            this.won++;
        } else if (scoredFor == scoredAgainst) {
            this.drawn++;
        } else {
            this.lost++;
        }
    }
 
    public int getGoalDifference() {
        return pointsFor - pointsAgainst;
    }
 
    public UUID getId() {
        return id;
    }
 
    public UUID getTeamId() {
        return teamId;
    }
 
    public int getPlayed() {
        return played;
    }
 
    public int getWon() {
        return won;
    }
 
    public int getDrawn() {
        return drawn;
    }
 
    public int getLost() {
        return lost;
    }
 
    public int getPointsFor() {
        return pointsFor;
    }
 
    public int getPointsAgainst() {
        return pointsAgainst;
    }
 
    public int getPoints() {
        return points;
    }
}
