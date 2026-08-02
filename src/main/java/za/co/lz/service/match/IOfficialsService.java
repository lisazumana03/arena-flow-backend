package za.co.lz.service.match;

import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.Officials;
import za.co.lz.service.IService;

import java.util.Optional;
import java.util.UUID;

public interface IOfficialsService extends IService<Officials, UUID> {
    Officials assignOfficials(Match match, String referee, String assistantReferee1, String assistantReferee2,
                               String fourthOfficial, String matchCommissioner);
    Optional<Officials> getByMatch(UUID matchId);
}
