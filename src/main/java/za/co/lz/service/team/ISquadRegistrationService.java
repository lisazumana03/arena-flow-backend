package za.co.lz.service.team;

import za.co.lz.domain.team.SquadRegistration;
import za.co.lz.service.IService;

import java.util.List;
import java.util.UUID;

public interface ISquadRegistrationService extends IService<SquadRegistration, UUID> {
    List<SquadRegistration> findByPlayer(UUID playerId);
    List<SquadRegistration> findSquad(UUID teamId, UUID seasonId);
    // ISquadRegistrationService
    SquadRegistration renew(UUID playerId, UUID teamId, UUID seasonId);
}