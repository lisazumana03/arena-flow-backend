package za.co.lz.factory.match;

import za.co.lz.domain.match.*;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.Team;

import java.util.UUID;

public class MatchEventFactory {

    public static MatchEvent createGoal(Match match, Team team, Player scorer, Player assistedBy,
                                         MatchEventType eventType, int minute, Integer stoppageMinute) {
        if (!eventType.scoresGoal()) {
            throw new IllegalArgumentException(eventType + " is not a goal-scoring event type");
        }
        return new MatchEvent.Builder()
                .setEventId(UUID.randomUUID())
                .setMatch(match)
                .setTeam(team)
                .setPlayer(scorer)
                .setRelatedPlayer(assistedBy)
                .setEventType(eventType)
                .setMinute(minute)
                .setStoppageMinute(stoppageMinute)
                .build();
    }

    public static MatchEvent createCard(Match match, Team team, Player player, MatchEventType cardType,
                                         DisciplinaryOffence offence, int minute, Integer stoppageMinute) {
        if (!cardType.isCard()) {
            throw new IllegalArgumentException(cardType + " is not a card event type");
        }
        return new MatchEvent.Builder()
                .setEventId(UUID.randomUUID())
                .setMatch(match)
                .setTeam(team)
                .setPlayer(player)
                .setEventType(cardType)
                .setOffence(offence)
                .setMinute(minute)
                .setStoppageMinute(stoppageMinute)
                .build();
    }

    public static MatchEvent createSubstitution(Match match, Team team, Player playerOff, Player playerOn, int minute) {
        return new MatchEvent.Builder()
                .setEventId(UUID.randomUUID())
                .setMatch(match)
                .setTeam(team)
                .setPlayer(playerOff)
                .setRelatedPlayer(playerOn)
                .setEventType(MatchEventType.SUBSTITUTION)
                .setMinute(minute)
                .build();
    }

    public static MatchEvent createCorner(Match match, Team team, int minute) {
        return new MatchEvent.Builder()
                .setEventId(UUID.randomUUID())
                .setMatch(match)
                .setTeam(team)
                .setEventType(MatchEventType.CORNER)
                .setMinute(minute)
                .build();
    }

    public static MatchEvent createFreeKick(Match match, Team team, Player takenBy, int minute, String notes) {
        return new MatchEvent.Builder()
                .setEventId(UUID.randomUUID())
                .setMatch(match)
                .setTeam(team)
                .setPlayer(takenBy)
                .setEventType(MatchEventType.FREE_KICK)
                .setMinute(minute)
                .setNotes(notes)
                .build();
    }

    public static MatchEvent createInjury(Match match, Team team, Player player, InjurySeverity severity,
                                           int minute, String notes) {
        return new MatchEvent.Builder()
                .setEventId(UUID.randomUUID())
                .setMatch(match)
                .setTeam(team)
                .setPlayer(player)
                .setEventType(MatchEventType.INJURY)
                .setInjurySeverity(severity)
                .setMinute(minute)
                .setNotes(notes)
                .build();
    }
}
