package za.co.lz.service.venue.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.lz.domain.venue.Venue;
import za.co.lz.domain.venue.VenueType;
import za.co.lz.repository.venue.VenueRepository;
import za.co.lz.service.venue.IVenueService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VenueServiceImpl implements IVenueService {

    @Autowired
    private VenueRepository venueRepository;

    @Override
    public Venue create(Venue venue) {
        return venueRepository.save(venue);
    }

    @Override
    public List<Venue> findAll() {
        return venueRepository.findAll();
    }

    @Override
    public Optional<Venue> findById(UUID uuid) {
        return venueRepository.findById(uuid);
    }

    @Override
    public Venue update(Venue venue, UUID uuid) {
        return venueRepository.save(venue);
    }

    @Override
    public void delete(UUID uuid) {
        venueRepository.deleteById(uuid);
    }

    @Override
    public List<Venue> findByType(VenueType venueType) {
        return venueRepository.findByVenueType(venueType);
    }
}
