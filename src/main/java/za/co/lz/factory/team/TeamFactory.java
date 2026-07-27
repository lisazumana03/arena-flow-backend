package za.co.lz.factory.team;

import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.TeamType;
import za.co.lz.domain.team.finances.Owner;

/**
 * Every Team must be controlled by an Owner (individual, consortium, company,
 * municipality, or supporters' trust) - see Team.Builder.build(), which
 * rejects a Team with no owner. These factory methods take the owner
 * up front so a Team can never be created "ownerless".
 */
public class TeamFactory {
    public static Team createClub(int teamFormationYear, Owner owner) {
        return new Team.Builder()
                .setTeamFormationYear(teamFormationYear)
                .setTeamType(TeamType.CLUB)
                .setOwner(owner)
                .build();
    }

    public static Team createNationalTeam(int teamFormationYear, Owner owner) {
        return new Team.Builder()
                .setTeamFormationYear(teamFormationYear)
                .setTeamType(TeamType.NATIONAL)
                .setOwner(owner)
                .build();
    }

}
