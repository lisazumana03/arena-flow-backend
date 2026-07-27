package za.co.lz.service.team.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.finances.Owner;
import za.co.lz.domain.team.finances.OwnerStrategy;
import za.co.lz.repository.team.finances.OwnerRepository;
import za.co.lz.service.team.finances.IOwnerService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OwnerServiceImpl implements IOwnerService {
    @Autowired
    private OwnerRepository ownerRepository;


    @Override
    public Owner create(Owner owner) {
        return ownerRepository.save(owner);
    }

    @Override
    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    @Override
    public Optional<Owner> findById(UUID id) {
        return ownerRepository.findById(id);
    }

    @Override
    public Owner update(Owner owner, UUID uuid) {
        return ownerRepository.save(owner);
    }

    @Override
    public void delete(UUID uuid) {
        this.ownerRepository.deleteById(uuid);
    }

    @Override
    public Owner buyClub(Owner owner, Team team) {
        if (owner.getAvailableFunds().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Owner has insufficient funds to buy a club");
        }
        team.setOwner(owner);
        owner.getOwnedTeams().add(team);
        return ownerRepository.save(owner);
    }

    @Override
    public Owner sellClub(Owner owner, Team team) {
        if (!owner.getOwnedTeams().contains(team)) {
            throw new IllegalArgumentException("Owner does not own this team");
        }
        owner.getOwnedTeams().remove(team);
        team.setOwner(null);
        return ownerRepository.save(owner);
    }

    @Override
    public Owner investInClub(Owner owner, Team team, double amount) {
        if (!owner.getOwnedTeams().contains(team)) {
            throw new IllegalArgumentException("Owner does not own this team");
        }
        java.math.BigDecimal investmentAmount = java.math.BigDecimal.valueOf(amount);
        if (owner.getAvailableFunds().compareTo(investmentAmount) < 0) {
            throw new IllegalArgumentException("Insufficient funds for investment");
        }
        owner.setAvailableFunds(owner.getAvailableFunds().subtract(investmentAmount));
        owner.setInvestmentBudget(owner.getInvestmentBudget().add(investmentAmount));
        return ownerRepository.save(owner);
    }

    @Override
    public Owner withdrawFundsFromClub(Owner owner, Team team, double amount) {
        if (!owner.getOwnedTeams().contains(team)) {
            throw new IllegalArgumentException("Owner does not own this team");
        }
        java.math.BigDecimal withdrawalAmount = java.math.BigDecimal.valueOf(amount);
        if (owner.getInvestmentBudget().compareTo(withdrawalAmount) < 0) {
            throw new IllegalArgumentException("Insufficient investment budget to withdraw");
        }
        owner.setInvestmentBudget(owner.getInvestmentBudget().subtract(withdrawalAmount));
        owner.setAvailableFunds(owner.getAvailableFunds().add(withdrawalAmount));
        return ownerRepository.save(owner);
    }

    @Override
    public Owner increaseReputation(Owner owner, int reputation) {
        int newReputation = Math.min(owner.getReputation() + reputation, 100);
        owner.setReputation(newReputation);
        return ownerRepository.save(owner);
    }

    @Override
    public Owner changeStrategy(Owner owner, OwnerStrategy newStrategy) {
        owner.setStrategy(newStrategy);
        return ownerRepository.save(owner);
    }
}
