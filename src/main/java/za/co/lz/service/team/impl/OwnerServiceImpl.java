package za.co.lz.service.team.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.team.finances.Owner;
import za.co.lz.repository.team.OwnerRepository;
import za.co.lz.service.team.IOwnerService;

import java.util.List;
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
    public Owner update(Owner owner, UUID uuid) {
        return ownerRepository.save(owner);
    }

    @Override
    public void delete(UUID uuid) {
        this.ownerRepository.deleteById(uuid);
    }
}
