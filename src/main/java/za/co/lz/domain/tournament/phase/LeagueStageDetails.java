package za.co.lz.domain.tournament.phase;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class LeagueStageDetails{
    @Id
    private UUID leagueStageId;
    private Stage stage;
    private int pointsForWin;
    private int pointsForDraw;
    private int pointsForLoss;
    private boolean allowDraws;
}
