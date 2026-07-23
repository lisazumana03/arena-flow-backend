package za.co.lz.domain.team;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import za.co.lz.domain.Name;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Player implements Serializable {
    @Id
    protected UUID playerId;
    @Embedded
    protected Name playerName;
    protected PlayerGender playerGender;
    protected LocalDate playerDateOfBirth;
    protected PlayerPosition playerPosition;
    protected String playerNationality;

    public Player(){}

    private Player(Builder builder){
        this.playerId = builder.playerId;
        this.playerName = builder.playerName;
        this.playerGender = builder.playerGender;
        this.playerDateOfBirth = builder.playerDateOfBirth;
        this.playerPosition = builder.playerPosition;
        this.playerNationality = builder.playerNationality;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Name getPlayerName() {
        return playerName;
    }

    public PlayerGender getPlayerGender() {return playerGender;}

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
        private Name playerName;
        private PlayerGender playerGender;
        private LocalDate playerDateOfBirth;
        private PlayerPosition playerPosition;
        private String playerNationality;

        public Builder setPlayerId(UUID playerId){
            this.playerId = playerId;
            return this;
        }

        public Builder setPlayerName(Name playerName){
            this.playerName = playerName;
            return this;
        }

        public Builder setPlayerGender(PlayerGender playerGender){
            this.playerGender = playerGender;
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
            this.playerGender = player.playerGender;
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
