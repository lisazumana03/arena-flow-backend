package za.co.lz.factory.team.discipline;

import za.co.lz.domain.match.DisciplinaryOffence;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.discipline.PlayerSuspension;

import java.util.UUID;

public class PlayerSuspensionFactory {

    /** Creates a suspension using the offence's default ban length. */
    public static PlayerSuspension createSuspension(Player player, Match triggeringMatch, DisciplinaryOffence offence) {
        return createSuspension(player, triggeringMatch, offence, offence.getDefaultBanGames());
    }

    /** Creates a suspension with a ban length overridden by a disciplinary committee. */
    public static PlayerSuspension createSuspension(Player player, Match triggeringMatch,
                                                      DisciplinaryOffence offence, int gamesBanned) {
        return new PlayerSuspension.Builder()
                .setSuspensionId(UUID.randomUUID())
                .setPlayer(player)
                .setTriggeringMatch(triggeringMatch)
                .setOffence(offence)
                .setGamesBanned(gamesBanned)
                .build();
    }
}
