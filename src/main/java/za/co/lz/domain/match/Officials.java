package za.co.lz.domain.match;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * The officiating team assigned to a single {@link Match}: referee, assistants,
 * fourth official and (optionally) a match commissioner. One-to-one with Match.
 */
@Entity
@Table(name = "officials")
public class Officials implements Serializable {

    @Id
    private UUID officialsId;

    @OneToOne
    @JoinColumn(name = "match_id", nullable = false, unique = true)
    private Match match;

    private String referee;
    private String assistantReferee1;
    private String assistantReferee2;
    private String fourthOfficial;
    private String matchCommissioner;

    public Officials() {}

    private Officials(Builder builder) {
        this.officialsId = builder.officialsId;
        this.match = builder.match;
        this.referee = builder.referee;
        this.assistantReferee1 = builder.assistantReferee1;
        this.assistantReferee2 = builder.assistantReferee2;
        this.fourthOfficial = builder.fourthOfficial;
        this.matchCommissioner = builder.matchCommissioner;
    }

    public UUID getOfficialsId() { return officialsId; }
    public Match getMatch() { return match; }
    public String getReferee() { return referee; }
    public String getAssistantReferee1() { return assistantReferee1; }
    public String getAssistantReferee2() { return assistantReferee2; }
    public String getFourthOfficial() { return fourthOfficial; }
    public String getMatchCommissioner() { return matchCommissioner; }

    public void setReferee(String referee) { this.referee = referee; }
    public void setAssistantReferee1(String assistantReferee1) { this.assistantReferee1 = assistantReferee1; }
    public void setAssistantReferee2(String assistantReferee2) { this.assistantReferee2 = assistantReferee2; }
    public void setFourthOfficial(String fourthOfficial) { this.fourthOfficial = fourthOfficial; }
    public void setMatchCommissioner(String matchCommissioner) { this.matchCommissioner = matchCommissioner; }

    @Override
    public String toString() {
        return "Officials{" +
                "match=" + (match != null ? match.getMatchId() : "null") +
                ", referee='" + referee + '\'' +
                '}';
    }

    public static class Builder {
        private UUID officialsId;
        private Match match;
        private String referee;
        private String assistantReferee1;
        private String assistantReferee2;
        private String fourthOfficial;
        private String matchCommissioner;

        public Builder setOfficialsId(UUID officialsId) { this.officialsId = officialsId; return this; }
        public Builder setMatch(Match match) { this.match = match; return this; }
        public Builder setReferee(String referee) { this.referee = referee; return this; }
        public Builder setAssistantReferee1(String assistantReferee1) { this.assistantReferee1 = assistantReferee1; return this; }
        public Builder setAssistantReferee2(String assistantReferee2) { this.assistantReferee2 = assistantReferee2; return this; }
        public Builder setFourthOfficial(String fourthOfficial) { this.fourthOfficial = fourthOfficial; return this; }
        public Builder setMatchCommissioner(String matchCommissioner) { this.matchCommissioner = matchCommissioner; return this; }

        public Officials build() {
            if (officialsId == null) throw new IllegalStateException("Officials ID is required");
            if (match == null) throw new IllegalStateException("Officials must be linked to a match");
            if (referee == null || referee.isBlank()) throw new IllegalStateException("A referee is required");
            return new Officials(this);
        }
    }
}
