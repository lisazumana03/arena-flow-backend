package za.co.lz.controller.venue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.lz.domain.venue.Venue;
import za.co.lz.domain.venue.VenueType;
import za.co.lz.service.venue.impl.VenueServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/venue")
public class VenueController {

    @Autowired
    private VenueServiceImpl venueService;

    @PostMapping("/create")
    public ResponseEntity<Venue> createVenue(@RequestBody Venue venue) {
        return ResponseEntity.ok(venueService.create(venue));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Venue>> getAllVenues() {
        return ResponseEntity.ok(venueService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venue> getVenue(@PathVariable UUID id) {
        Venue venue = venueService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venue not found with ID: " + id));
        return ResponseEntity.ok(venue);
    }

    @GetMapping("/type/{venueType}")
    public ResponseEntity<List<Venue>> getVenuesByType(@PathVariable VenueType venueType) {
        return ResponseEntity.ok(venueService.findByType(venueType));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Venue> updateVenue(@PathVariable UUID id, @RequestBody Venue venue) {
        return ResponseEntity.ok(venueService.update(venue, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable UUID id) {
        venueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
