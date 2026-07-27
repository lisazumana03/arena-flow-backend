package za.co.lz.service.team.finances;

import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.finances.Owner;
import za.co.lz.domain.team.finances.OwnerStrategy;
import za.co.lz.service.IService;

import java.util.UUID;

public interface IOwnerService extends IService<Owner, UUID> {
    //If the owner wants to buy a club
    Owner buyClub(Owner owner, Team team);
    // If the owner wants to sell a club
    Owner sellClub(Owner owner, Team team);
    // If the owner wants to invest in a club
    Owner investInClub(Owner owner, Team team, double amount);
    // If the owner wants to withdraw funds from a club
    Owner withdrawFundsFromClub(Owner owner, Team team, double amount);
    // If the owner wants to increase their reputation
    Owner increaseReputation(Owner owner, int reputation);
    // Change owner's strategy
    Owner changeStrategy(Owner owner, OwnerStrategy newStrategy);
}
