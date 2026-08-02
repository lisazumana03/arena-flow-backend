package za.co.lz.factory.venue;

import za.co.lz.domain.venue.Venue;
import za.co.lz.domain.venue.VenueType;

import java.util.UUID;

public class VenueFactory {
    public static Venue createVenue(String venueName, VenueType venueType, String city,
                                     String address, int capacity, boolean hasFloodlights) {
        return new Venue.Builder()
                .setVenueId(UUID.randomUUID())
                .setVenueName(venueName)
                .setVenueType(venueType)
                .setCity(city)
                .setAddress(address)
                .setCapacity(capacity)
                .setHasFloodlights(hasFloodlights)
                .build();
    }
}
