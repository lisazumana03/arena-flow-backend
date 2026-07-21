package za.co.lz.factory;

import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.TeamType;

public class TeamFactory {
    public static Team createClub(int teamFormationYear, TeamType teamType) {
        return new Team.Builder()
                .setTeamFormationYear(teamFormationYear)
                .setTeamType(teamType)
                .build();
    }
}
