package za.co.lz.service.match.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.Officials;
import za.co.lz.factory.match.OfficialsFactory;
import za.co.lz.repository.match.OfficialsRepository;
import za.co.lz.service.match.IOfficialsService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OfficialsServiceImpl implements IOfficialsService {

    @Autowired
    private OfficialsRepository officialsRepository;

    @Override
    public Officials create(Officials officials) {
        return officialsRepository.save(officials);
    }

    @Override
    public List<Officials> findAll() {
        return officialsRepository.findAll();
    }

    @Override
    public Optional<Officials> findById(UUID uuid) {
        return officialsRepository.findById(uuid);
    }

    @Override
    public Officials update(Officials officials, UUID uuid) {
        return officialsRepository.save(officials);
    }

    @Override
    public void delete(UUID uuid) {
        officialsRepository.deleteById(uuid);
    }

    @Override
    public Officials assignOfficials(Match match, String referee, String assistantReferee1, String assistantReferee2,
                                      String fourthOfficial, String matchCommissioner) {
        if (officialsRepository.findByMatch_MatchId(match.getMatchId()).isPresent()) {
            throw new IllegalStateException("Officials have already been assigned to this match");
        }
        Officials officials = OfficialsFactory.createOfficials(match, referee, assistantReferee1,
                assistantReferee2, fourthOfficial, matchCommissioner);
        return officialsRepository.save(officials);
    }

    @Override
    public Optional<Officials> getByMatch(UUID matchId) {
        return officialsRepository.findByMatch_MatchId(matchId);
    }
}
