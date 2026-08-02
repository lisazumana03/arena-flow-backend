package za.co.lz.service.team.discipline;

import za.co.lz.domain.match.InjurySeverity;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.discipline.PlayerInjury;
import za.co.lz.service.IService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IPlayerInjuryService extends IService<PlayerInjury, UUID> {

    /** Reports a new injury; expected return date is estimated from the severity's typical recovery window. */
    PlayerInjury reportInjury(Player player, Match triggeringMatch, InjurySeverity severity, LocalDate injuryDate);

    /** Medical staff override once a proper diagnosis/scan is done. */
    PlayerInjury updateExpectedReturn(UUID injuryId, LocalDate newExpectedReturnDate);

    PlayerInjury markRecovered(UUID injuryId);

    boolean isPlayerInjured(UUID playerId);

    List<PlayerInjury> getActiveInjuries(UUID playerId);

    List<PlayerInjury> getInjuryHistory(UUID playerId);
}
