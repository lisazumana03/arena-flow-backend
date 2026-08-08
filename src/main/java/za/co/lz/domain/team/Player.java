package za.co.lz.domain.team;

import jakarta.persistence.*;
import za.co.lz.domain.Name;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Player implements Serializable {
    @Id
    private UUID playerId;
    @Embedded
    private Name playerName;
    private PlayerGender playerGender;
    private LocalDate playerDateOfBirth;
    private PlayerPosition playerPosition;
    private String playerNationality;
    private double playerHeight;
    private double playerWeight;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public Player(){}

    private Player(Builder builder){
        this.playerId = builder.playerId;
        this.playerName = builder.playerName;
        this.playerGender = builder.playerGender;
        this.playerDateOfBirth = builder.playerDateOfBirth;
        this.playerPosition = builder.playerPosition;
        this.playerNationality = builder.playerNationality;
        this.playerHeight = builder.playerHeight;
        this.playerWeight = builder.playerWeight;
        this.team = builder.team;
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

    public double getPlayerHeight() {
        return playerHeight;
    }

    public double getPlayerWeight() {
        return playerWeight;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", playerName=" + playerName +
                ", playerGender=" + playerGender +
                ", playerDateOfBirth=" + playerDateOfBirth +
                ", playerPosition=" + playerPosition +
                ", playerNationality='" + playerNationality + '\'' +
                ", playerHeight=" + playerHeight +
                ", playerWeight=" + playerWeight +
                ", team=" + team +
                '}';
    }

    public static class Builder{
        private UUID playerId;
        private Name playerName;
        private PlayerGender playerGender;
        private LocalDate playerDateOfBirth;
        private PlayerPosition playerPosition;
        private String playerNationality;
        private double playerHeight;
        private double playerWeight;
        private Team team;

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

        public Builder setPlayerHeight(double playerHeight){
            this.playerHeight = playerHeight;
            return this;
        }

        public Builder setPlayerWeight(double playerWeight){
            this.playerWeight = playerWeight;
            return this;
        }

        public Builder setTeam(Team team){
            this.team = team;
            return this;
        }

        public Builder copy(Player player){
            this.playerId = player.playerId;
            this.playerName = player.playerName;
            this.playerGender = player.playerGender;
            this.playerDateOfBirth = player.playerDateOfBirth;
            this.playerPosition = player.playerPosition;
            this.playerNationality = player.playerNationality;
            this.playerHeight = player.playerHeight;
            this.playerWeight = player.playerWeight;
            this.team = player.team;
            return this;
        }

        public Player build(){
            return new Player(this);
        }

    }
}
