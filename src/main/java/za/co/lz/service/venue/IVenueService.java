package za.co.lz.service.venue;

import za.co.lz.domain.venue.Venue;
import za.co.lz.domain.venue.VenueType;
import za.co.lz.service.IService;

import java.util.List;
import java.util.UUID;

public interface IVenueService extends IService<Venue, UUID> {
    List<Venue> findByType(VenueType venueType);
}
