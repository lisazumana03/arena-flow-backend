package za.co.lz.util;

import za.co.lz.domain.Name;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.MatchStatus;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerGender;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.TeamType;
import za.co.lz.domain.team.finances.Owner;
import za.co.lz.domain.team.finances.OwnerStrategy;
import za.co.lz.domain.team.finances.OwnershipType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Shared, minimal test fixtures for the venue / match-event / discipline test suites.
 * Keeps factory, service and controller tests from re-deriving boilerplate object graphs.
 */
public final class TestFixtures {

    private TestFixtures() {}

    public static Owner owner() {
        return new Owner.Builder()
                .setOwnerId(UUID.randomUUID())
                .setOwnerName(new Name.Builder().setFirstName("Jane").setLastName("Doe").build())
                .setOwnershipType(OwnershipType.PRIVATE_OWNER)
                .setStrategy(OwnerStrategy.BALANCED_APPROACH)
                .setNetWorth(BigDecimal.valueOf(1_000_000))
                .setAvailableFunds(BigDecimal.valueOf(1_000_000))
                .setInvestmentBudget(BigDecimal.ZERO)
                .setReputation(50)
                .build();
    }

    public static Team team(String name) {
        return new Team.Builder()
                .setTeamId(UUID.randomUUID())
                .setTeamName(name)
                .setTeamFormationYear(2000)
                .setTeamType(TeamType.CLUB)
                .setOwner(owner())
                .build();
    }

    public static Player player(Team team, String firstName, String lastName, PlayerPosition position) {
        return new Player.Builder()
                .setPlayerId(UUID.randomUUID())
                .setPlayerName(new Name.Builder().setFirstName(firstName).setLastName(lastName).build())
                .setPlayerGender(PlayerGender.MALE)
                .setPlayerDateOfBirth(LocalDate.of(1998, 1, 1))
                .setPlayerPosition(position)
                .setPlayerNationality("RSA")
                .setPlayerHeight(1.80)
                .setPlayerWeight(75)
                .setTeam(team)
                .build();
    }

    public static Match match(Team home, Team away) {
        return new Match.Builder()
                .setMatchId(UUID.randomUUID())
                .setHomeTeam(home)
                .setAwayTeam(away)
                .setMatchDate(LocalDateTime.now())
                .setVenue("TBD")
                .setStatus(MatchStatus.IN_PROGRESS)
                .build();
    }
}
