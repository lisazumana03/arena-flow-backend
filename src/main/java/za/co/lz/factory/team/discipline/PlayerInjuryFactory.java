package za.co.lz.factory.team.discipline;

import za.co.lz.domain.match.InjurySeverity;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.discipline.PlayerInjury;

import java.time.LocalDate;
import java.util.UUID;

public class PlayerInjuryFactory {

    /** Creates an injury record, estimating the return date from the severity's typical recovery window. */
    public static PlayerInjury createInjury(Player player, Match triggeringMatch, InjurySeverity severity, LocalDate injuryDate) {
        return new PlayerInjury.Builder()
                .setInjuryId(UUID.randomUUID())
                .setPlayer(player)
                .setTriggeringMatch(triggeringMatch)
                .setSeverity(severity)
                .setInjuryDate(injuryDate)
                .build();
    }
}
