package za.co.lz.domain.tournament.phase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import za.co.lz.domain.tournament.round.Round;

@Entity
public class KnockoutStageDetails {
    private UUID knockoutStageId;
    private Stage stage;
    private UUID championId;
    @OneToMany(mappedBy = "knockoutStageDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("roundNumber ASC")
    private List<Round> rounds = new ArrayList<>();

    protected KnockoutStageDetails() {
        // required by JPA
    }
 
    private KnockoutStageDetails(Stage stage, List<UUID> seededTeamIds) {
        if (stage.getStageType() != StageType.KNOCKOUT) {
            throw new IllegalArgumentException("KnockoutStageDetails can only attach to a KNOCKOUT stage");
        }
        if (seededTeamIds == null || seededTeamIds.size() < 2) {
            throw new IllegalArgumentException("A knockout stage needs at least two seeded teams");
        }
        this.knockoutStageId = UUID.randomUUID();
        this.stage = stage;
        this.rounds = buildBracket(seededTeamIds);
        stage.attachKnockoutDetails(this);
    }
 
    /** seededTeamIds must be ordered strongest seed first (seed 1 = index 0). */
    public static KnockoutStageDetails singleElimination(Stage stage, List<UUID> seededTeamIds) {
        return new KnockoutStageDetails(stage, seededTeamIds);
    }
 
    private List<Round> buildBracket(List<UUID> seededTeamIds) {
        int teamCount = seededTeamIds.size();
        int bracketSize = Integer.highestOneBit(teamCount - 1) * 2; // smallest power of two >= teamCount
        int totalRounds = Integer.numberOfTrailingZeros(bracketSize);
        List<Integer> seedOrder = generateBracketOrder(bracketSize);
 
        List<Round> builtRounds = new ArrayList<>();
 
        int firstRoundFixtures = bracketSize / 2;
        Round firstRound = new Round(1, roundName(1, totalRounds));
        for (int i = 0; i < firstRoundFixtures; i++) {
            int seedA = seedOrder.get(2 * i);
            int seedB = seedOrder.get(2 * i + 1);
            UUID teamA = seedA <= teamCount ? seededTeamIds.get(seedA - 1) : null;
            UUID teamB = seedB <= teamCount ? seededTeamIds.get(seedB - 1) : null;
 
            Fixture fixture = new Fixture(i, teamA, teamB);
            if ((teamA == null) != (teamB == null)) {
                fixture.assignBye(teamA != null ? teamA : teamB);
            }
            firstRound.addFixture(fixture);
        }
        firstRound.assignKnockoutStageDetails(this);
        builtRounds.add(firstRound);
 
        int previousFixtureCount = firstRoundFixtures;
        for (int roundNumber = 2; roundNumber <= totalRounds; roundNumber++) {
            int fixturesThisRound = previousFixtureCount / 2;
            Round round = new Round(roundNumber, roundName(roundNumber, totalRounds));
            for (int i = 0; i < fixturesThisRound; i++) {
                round.addFixture(new Fixture(i, null, null));
            }
            round.assignKnockoutStageDetails(this);
            builtRounds.add(round);
            previousFixtureCount = fixturesThisRound;
        }
 
        // Byes resolve immediately at construction time — push those winners
        // into round 2 straight away rather than waiting on a result.
        for (Fixture fixture : firstRound.getFixtures()) {
            if (fixture.getStatus() == FixtureStatus.BYE) {
                propagateWinner(builtRounds, 1, fixture.getOrder(), fixture.getWinnerTeamId());
            }
        }
 
        return builtRounds;
    }
 
    /**
     * Standard single-elimination seeding: recursively pairs seed 1 against
     * the weakest remaining seed at each depth (1 vs N, then within each
     * half 2 vs N-1, and so on), which is what keeps top seeds apart for as
     * long as possible. Returns a list of seed numbers in bracket slot
     * order; adjacent pairs (0,1), (2,3), ... are the round-1 matchups.
     */
    private static List<Integer> generateBracketOrder(int size) {
        if (size == 1) {
            return new ArrayList<>(List.of(1));
        }
        List<Integer> previous = generateBracketOrder(size / 2);
        List<Integer> result = new ArrayList<>(size);
        for (int seed : previous) {
            result.add(seed);
            result.add(size + 1 - seed);
        }
        return result;
    }
 
    private static String roundName(int roundNumber, int totalRounds) {
        int fixturesRemainingAtThisRound = 1 << (totalRounds - roundNumber);
        return switch (fixturesRemainingAtThisRound) {
            case 1 -> "Final";
            case 2 -> "Semi-final";
            case 4 -> "Quarter-final";
            default -> "Round of " + (fixturesRemainingAtThisRound * 2);
        };
    }
 
    /** Records who won a fixture and, if applicable, feeds them into the next round. */
    public void recordResult(UUID fixtureId, UUID winnerTeamId) {
        for (Round round : rounds) {
            for (Fixture fixture : round.getFixtures()) {
                if (!fixture.getId().equals(fixtureId)) {
                    continue;
                }
                fixture.recordWinner(winnerTeamId);
                if (round.getRoundNumber() == rounds.size()) {
                    this.championTeamId = winnerTeamId;
                    stage.complete();
                } else {
                    propagateWinner(rounds, round.getRoundNumber(), fixture.getOrder(), winnerTeamId);
                }
                return;
            }
        }
        throw new IllegalArgumentException("No fixture found with id " + fixtureId + " in this knockout stage");
    }
 
    private void propagateWinner(List<Round> allRounds, int fromRoundNumber, int fixtureOrder, UUID winnerTeamId) {
        if (fromRoundNumber >= allRounds.size()) {
            return; // fromRoundNumber was already the final
        }
        Round nextRound = allRounds.get(fromRoundNumber); // rounds is 0-indexed, roundNumber is 1-indexed
        int nextFixtureOrder = fixtureOrder / 2;
        boolean homeSlot = fixtureOrder % 2 == 0;
        nextRound.getFixtures().get(nextFixtureOrder).fillSlot(winnerTeamId, homeSlot);
    }
 
    public UUID getId() {
        return id;
    }
 
    public Stage getStage() {
        return stage;
    }
 
    public UUID getChampionTeamId() {
        return championTeamId;
    }
 
    public List<Round> getRounds() {
        return Collections.unmodifiableList(rounds);
    }
    
}
