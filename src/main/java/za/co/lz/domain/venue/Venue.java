package za.co.lz.domain.venue;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * A physical location matches are played at - anything from a small
 * community ground to a full soccer stadium. Referenced by {@link za.co.lz.domain.match.Match}.
 */
@Entity
@Table(name = "venues")
public class Venue implements Serializable {

    @Id
    private UUID venueId;

    private String venueName;

    @Enumerated(EnumType.STRING)
    private VenueType venueType;

    private String city;
    private String address;
    private int capacity;
    private boolean hasFloodlights;

    public Venue() {}

    private Venue(Builder builder) {
        this.venueId = builder.venueId;
        this.venueName = builder.venueName;
        this.venueType = builder.venueType;
        this.city = builder.city;
        this.address = builder.address;
        this.capacity = builder.capacity;
        this.hasFloodlights = builder.hasFloodlights;
    }

    public UUID getVenueId() { return venueId; }
    public String getVenueName() { return venueName; }
    public VenueType getVenueType() { return venueType; }
    public String getCity() { return city; }
    public String getAddress() { return address; }
    public int getCapacity() { return capacity; }
    public boolean isHasFloodlights() { return hasFloodlights; }

    public void setVenueName(String venueName) { this.venueName = venueName; }
    public void setVenueType(VenueType venueType) { this.venueType = venueType; }
    public void setCity(String city) { this.city = city; }
    public void setAddress(String address) { this.address = address; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setHasFloodlights(boolean hasFloodlights) { this.hasFloodlights = hasFloodlights; }

    @Override
    public String toString() {
        return "Venue{" +
                "venueId=" + venueId +
                ", venueName='" + venueName + '\'' +
                ", venueType=" + venueType +
                ", city='" + city + '\'' +
                ", capacity=" + capacity +
                '}';
    }

    public static class Builder {
        private UUID venueId;
        private String venueName;
        private VenueType venueType;
        private String city;
        private String address;
        private int capacity;
        private boolean hasFloodlights;

        public Builder setVenueId(UUID venueId) { this.venueId = venueId; return this; }
        public Builder setVenueName(String venueName) { this.venueName = venueName; return this; }
        public Builder setVenueType(VenueType venueType) { this.venueType = venueType; return this; }
        public Builder setCity(String city) { this.city = city; return this; }
        public Builder setAddress(String address) { this.address = address; return this; }
        public Builder setCapacity(int capacity) { this.capacity = capacity; return this; }
        public Builder setHasFloodlights(boolean hasFloodlights) { this.hasFloodlights = hasFloodlights; return this; }

        public Builder copy(Venue venue) {
            this.venueId = venue.venueId;
            this.venueName = venue.venueName;
            this.venueType = venue.venueType;
            this.city = venue.city;
            this.address = venue.address;
            this.capacity = venue.capacity;
            this.hasFloodlights = venue.hasFloodlights;
            return this;
        }

        public Venue build() {
            if (venueId == null) throw new IllegalStateException("Venue ID is required");
            if (venueName == null || venueName.isBlank()) throw new IllegalStateException("Venue name is required");
            if (venueType == null) throw new IllegalStateException("Venue type is required");
            if (capacity < 0) throw new IllegalStateException("Capacity cannot be negative");
            return new Venue(this);
        }
    }
}
