package za.co.lz.factory.match;

import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.Officials;

import java.util.UUID;

public class OfficialsFactory {
    public static Officials createOfficials(Match match, String referee, String assistantReferee1,
                                              String assistantReferee2, String fourthOfficial,
                                              String matchCommissioner) {
        return new Officials.Builder()
                .setOfficialsId(UUID.randomUUID())
                .setMatch(match)
                .setReferee(referee)
                .setAssistantReferee1(assistantReferee1)
                .setAssistantReferee2(assistantReferee2)
                .setFourthOfficial(fourthOfficial)
                .setMatchCommissioner(matchCommissioner)
                .build();
    }
}
