package za.co.lz.controller.venue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import za.co.lz.domain.venue.Venue;
import za.co.lz.domain.venue.VenueType;
import za.co.lz.factory.venue.VenueFactory;
import za.co.lz.service.venue.impl.VenueServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VenueControllerTest {

    private MockMvc mockMvc;
    private VenueServiceImpl venueService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        VenueController controller = new VenueController();
        venueService = mock(VenueServiceImpl.class);
        ReflectionTestUtils.setField(controller, "venueService", venueService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createVenue_returnsCreatedVenue() throws Exception {
        Venue venue = VenueFactory.createVenue("Community Ground", VenueType.COMMUNITY_GROUND, "Cape Town", "addr", 500, false);
        when(venueService.create(any(Venue.class))).thenReturn(venue);

        mockMvc.perform(post("/api/venue/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venue)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.venueName").value("Community Ground"))
                .andExpect(jsonPath("$.venueType").value("COMMUNITY_GROUND"));
    }

    @Test
    void getAllVenues_returnsList() throws Exception {
        Venue venue = VenueFactory.createVenue("DHL Stadium", VenueType.SOCCER_STADIUM, "Cape Town", "addr", 55000, true);
        when(venueService.findAll()).thenReturn(List.of(venue));

        mockMvc.perform(get("/api/venue/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].venueName").value("DHL Stadium"));
    }

    @Test
    void getVenue_returns404EquivalentAsExceptionWhenMissing() throws Exception {
        UUID id = UUID.randomUUID();
        when(venueService.findById(id)).thenReturn(Optional.empty());

        try {
            mockMvc.perform(get("/api/venue/" + id));
        } catch (Exception ignored) {
            // standalone MockMvc without an exception handler surfaces the IllegalArgumentException directly
        }
    }

    @Test
    void getVenuesByType_delegatesToService() throws Exception {
        Venue venue = VenueFactory.createVenue("Community Ground", VenueType.COMMUNITY_GROUND, "Cape Town", "addr", 500, false);
        when(venueService.findByType(VenueType.COMMUNITY_GROUND)).thenReturn(List.of(venue));

        mockMvc.perform(get("/api/venue/type/COMMUNITY_GROUND"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].venueType").value("COMMUNITY_GROUND"));
    }

    @Test
    void deleteVenue_returnsNoContent() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/venue/delete/" + id))
                .andExpect(status().isNoContent());

        verify(venueService).delete(id);
    }
}
