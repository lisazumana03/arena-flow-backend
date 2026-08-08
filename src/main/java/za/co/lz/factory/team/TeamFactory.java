package za.co.lz.factory.team;

import za.co.lz.domain.team.PlayerGender;
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
    public static Team createClub(int teamFormationYear, Owner owner, PlayerGender teamGender,
                                   boolean hasYouthAcademy, String teamNationality) {
        return new Team.Builder()
                .setTeamFormationYear(teamFormationYear)
                .setTeamType(TeamType.CLUB)
                .setTeamGender(teamGender)
                .setHasYouthAcademy(hasYouthAcademy)
                .setTeamNationality(teamNationality)
                .setOwner(owner)
                .build();
    }

    public static Team createNationalTeam(int teamFormationYear, Owner owner, PlayerGender teamGender,
                                           String teamNationality) {
        return new Team.Builder()
                .setTeamFormationYear(teamFormationYear)
                .setTeamType(TeamType.NATIONAL)
                .setTeamGender(teamGender)
                // National teams don't run youth academies in their own right.
                .setHasYouthAcademy(false)
                .setTeamNationality(teamNationality)
                .setOwner(owner)
                .build();
    }

}
