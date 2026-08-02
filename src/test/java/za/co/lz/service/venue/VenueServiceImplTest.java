package za.co.lz.service.venue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.lz.domain.venue.Venue;
import za.co.lz.domain.venue.VenueType;
import za.co.lz.factory.venue.VenueFactory;
import za.co.lz.repository.venue.VenueRepository;
import za.co.lz.service.venue.impl.VenueServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueServiceImplTest {

    @Mock
    private VenueRepository venueRepository;

    @InjectMocks
    private VenueServiceImpl venueService;

    @Test
    void create_savesVenue() {
        Venue venue = VenueFactory.createVenue("Community Ground", VenueType.COMMUNITY_GROUND, "Cape Town", "addr", 500, false);
        when(venueRepository.save(venue)).thenReturn(venue);

        Venue result = venueService.create(venue);

        assertEquals(venue, result);
        verify(venueRepository).save(venue);
    }

    @Test
    void findById_delegatesToRepository() {
        UUID id = UUID.randomUUID();
        Venue venue = VenueFactory.createVenue("Stadium", VenueType.SOCCER_STADIUM, "Cape Town", "addr", 40000, true);
        when(venueRepository.findById(id)).thenReturn(Optional.of(venue));

        Optional<Venue> result = venueService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(venue, result.get());
    }

    @Test
    void findByType_delegatesToRepository() {
        Venue venue = VenueFactory.createVenue("Community Ground", VenueType.COMMUNITY_GROUND, "Cape Town", "addr", 500, false);
        when(venueRepository.findByVenueType(VenueType.COMMUNITY_GROUND)).thenReturn(List.of(venue));

        List<Venue> result = venueService.findByType(VenueType.COMMUNITY_GROUND);

        assertEquals(1, result.size());
        assertEquals(VenueType.COMMUNITY_GROUND, result.get(0).getVenueType());
    }

    @Test
    void delete_delegatesToRepository() {
        UUID id = UUID.randomUUID();
        venueService.delete(id);
        verify(venueRepository).deleteById(id);
    }
}
