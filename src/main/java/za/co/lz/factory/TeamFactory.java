package za.co.lz.factory;

import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.TeamType;

public class TeamFactory {
    public static Team createClub(int teamFormationYear, TeamType teamType) {
        return new Team.Builder()
                .setTeamFormationYear(teamFormationYear)
                .setTeamType(TeamType.CLUB)
                .build();
    }

    public static Team createNationalTeam(int teamFormationYear, TeamType teamType) {
        return new Team.Builder()
                .setTeamFormationYear(teamFormationYear)
                .setTeamType(TeamType.NATIONAL)
                .build();
    }

}
