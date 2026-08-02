package za.co.lz.service.team.discipline;

import za.co.lz.domain.match.DisciplinaryOffence;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.discipline.PlayerSuspension;
import za.co.lz.service.IService;

import java.util.List;
import java.util.UUID;

public interface IPlayerSuspensionService extends IService<PlayerSuspension, UUID> {

    /** Issues a suspension using the offence's default ban length (see DisciplinaryOffence). */
    PlayerSuspension issueSuspension(Player player, Match triggeringMatch, DisciplinaryOffence offence);

    /** Issues a suspension with a ban length overridden by a disciplinary committee. */
    PlayerSuspension issueSuspension(Player player, Match triggeringMatch, DisciplinaryOffence offence, int gamesBanned);

    /**
     * Call once a match played by `team` has been completed: every active suspension
     * held by one of the team's players "serves" one game. This is how a ban counts
     * down - by the player's team playing fixtures, not by individual appearances.
     */
    List<PlayerSuspension> serveGameForTeam(Team team);

    boolean isPlayerSuspended(UUID playerId);

    List<PlayerSuspension> getActiveSuspensions(UUID playerId);

    List<PlayerSuspension> getSuspensionHistory(UUID playerId);
}
