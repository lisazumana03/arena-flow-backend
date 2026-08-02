package za.co.lz.factory.venue;

import org.junit.jupiter.api.Test;
import za.co.lz.domain.venue.Venue;
import za.co.lz.domain.venue.VenueType;

import static org.junit.jupiter.api.Assertions.*;

class VenueFactoryTest {

    @Test
    void createVenue_assignsIdAndFields() {
        Venue venue = VenueFactory.createVenue("Green Point Community Ground", VenueType.COMMUNITY_GROUND,
                "Cape Town", "1 Main Rd", 500, false);

        assertNotNull(venue.getVenueId());
        assertEquals("Green Point Community Ground", venue.getVenueName());
        assertEquals(VenueType.COMMUNITY_GROUND, venue.getVenueType());
        assertEquals("Cape Town", venue.getCity());
        assertEquals(500, venue.getCapacity());
        assertFalse(venue.isHasFloodlights());
    }

    @Test
    void createVenue_stadiumVariant() {
        Venue venue = VenueFactory.createVenue("DHL Stadium", VenueType.SOCCER_STADIUM,
                "Cape Town", "Fritz Sonnenberg Rd", 55000, true);

        assertEquals(VenueType.SOCCER_STADIUM, venue.getVenueType());
        assertTrue(venue.isHasFloodlights());
        assertEquals(55000, venue.getCapacity());
    }

    @Test
    void createVenue_distinctIdsPerCall() {
        Venue a = VenueFactory.createVenue("A", VenueType.COMMUNITY_GROUND, "CT", "addr", 100, false);
        Venue b = VenueFactory.createVenue("B", VenueType.COMMUNITY_GROUND, "CT", "addr", 100, false);
        assertNotEquals(a.getVenueId(), b.getVenueId());
    }
}
