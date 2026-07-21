package za.co.lz.domain.team;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Player {
    @Id
    private UUID playerId;
    private String playerName;
    private LocalDate playerDateOfBirth;
    private PlayerPosition playerPosition;
    private String playerNationality;

    public Player(){}

    private Player(Builder builder){
        this.playerId = builder.playerId;
        this.playerName = builder.playerName;
        this.playerDateOfBirth = builder.playerDateOfBirth;
        this.playerPosition = builder.playerPosition;
        this.playerNationality = builder.playerNationality;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public LocalDate getPlayerDateOfBirth() {
        return playerDateOfBirth;
    }

    public PlayerPosition getPlayerPosition() {
        return playerPosition;
    }

    public String getPlayerNationality() {
        return playerNationality;
    }

    public static class Builder{
        private UUID playerId;
        private String playerName;
        private LocalDate playerDateOfBirth;
        private PlayerPosition playerPosition;
        private String playerNationality;

        public Builder setPlayerId(UUID playerId){
            this.playerId = playerId;
            return this;
        }

        public Builder setPlayerName(String playerName){
            this.playerName = playerName;
            return this;
        }

        public Builder setPlayerDateOfBirth(LocalDate playerDateOfBirth){
            this.playerDateOfBirth = playerDateOfBirth;
            return this;
        }

        public Builder setPlayerPosition(PlayerPosition playerPosition){
            this.playerPosition = playerPosition;
            return this;
        }

        public Builder setPlayerNationality(String playerNationality){
            this.playerNationality = playerNationality;
            return this;
        }

        public Builder copy(Player player){
            this.playerId = player.playerId;
            this.playerName = player.playerName;
            this.playerDateOfBirth = player.playerDateOfBirth;
            this.playerPosition = player.playerPosition;
            this.playerNationality = player.playerNationality;
            return this;
        }

        public Player build(){
            return new Player(this);
        }

    }
}
