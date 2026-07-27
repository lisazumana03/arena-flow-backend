package za.co.lz.service.team.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.team.SquadRegistration;
import za.co.lz.repository.team.SquadRegistrationRepository;
import za.co.lz.service.team.ISquadRegistrationService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SquadRegistrationServiceImpl implements ISquadRegistrationService {

    @Autowired
    private SquadRegistrationRepository squadRegistrationRepository;

    @Override
    public SquadRegistration create(SquadRegistration registration) {
        squadRegistrationRepository
                .findByTeam_TeamIdAndSeason_SeasonIdAndKitNumber(
                        registration.getTeam().getTeamId(),
                        registration.getSeason().getSeasonId(),
                        registration.getKitNumber())
                .ifPresent(existing -> {
                    throw new IllegalStateException(
                            "Kit number " + registration.getKitNumber() +
                            " is already taken for this team and season.");
                });
        return squadRegistrationRepository.save(registration);
    }

    @Override
    public List<SquadRegistration> findAll() {
        return squadRegistrationRepository.findAll();
    }

    @Override
    public Optional<SquadRegistration> findById(UUID id) {
        return squadRegistrationRepository.findById(id);
    }

    @Override
    public SquadRegistration update(SquadRegistration registration, UUID id) {
        return squadRegistrationRepository.save(registration);
    }

    @Override
    public void delete(UUID id) {
        squadRegistrationRepository.deleteById(id);
    }

    @Override
    public List<SquadRegistration> findByPlayer(UUID playerId) {
        return squadRegistrationRepository.findByPlayer_PlayerId(playerId);
    }

    @Override
    public List<SquadRegistration> findSquad(UUID teamId, UUID seasonId) {
        return squadRegistrationRepository.findByTeam_TeamIdAndSeason_SeasonId(teamId, seasonId);
    }

    // SquadRegistrationServiceImpl
@Override
public SquadRegistration renew(UUID playerId, UUID teamId, UUID seasonId) {
    SquadRegistration previous = squadRegistrationRepository
            .findTopByPlayer_PlayerIdOrderByRegistrationDateDesc(playerId)
            .orElseThrow(() -> new IllegalStateException("No prior registration found for this player."));

    SquadRegistration next = new SquadRegistration.Builder()
            .setSquadRegistrationId(UUID.randomUUID())
            .setPlayer(previous.getPlayer())
            .setTeam(/* fetch team by teamId */ previous.getTeam())
            .setSeason(/* fetch season by seasonId */ null)
            .setKitNumber(previous.getKitNumber())
            .setRegistrationDate(LocalDate.now())
            .build();

    return create(next); // reuses the existing "kit number taken" check
}
}
