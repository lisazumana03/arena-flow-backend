package za.co.lz.repository.venue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.lz.domain.venue.Venue;
import za.co.lz.domain.venue.VenueType;

import java.util.List;
import java.util.UUID;

@Repository
public interface VenueRepository extends JpaRepository<Venue, UUID> {
    List<Venue> findByVenueType(VenueType venueType);
}
