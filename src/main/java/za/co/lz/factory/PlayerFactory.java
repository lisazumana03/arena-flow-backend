package za.co.lz.factory;

import za.co.lz.domain.Name;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerGender;
import za.co.lz.domain.team.PlayerPosition;

import java.time.LocalDate;
import java.util.UUID;

public class PlayerFactory {
    public static Player createPlayer(Name playerName, PlayerGender playerGender, LocalDate playerDateOfBirth,
                                      PlayerPosition playerPosition, String playerNationality,
                                      double playerHeight, double playerWeight
    ) {
        return new Player.Builder()
                .setPlayerId(UUID.randomUUID())
                .setPlayerName(playerName)
                .setPlayerGender(playerGender)
                .setPlayerDateOfBirth(playerDateOfBirth)
                .setPlayerPosition(playerPosition)
                .setPlayerNationality(playerNationality)
                .setPlayerHeight(playerHeight)
                .setPlayerWeight(playerWeight)
                .build();
    }
}
