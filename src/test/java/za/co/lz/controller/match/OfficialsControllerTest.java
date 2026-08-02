package za.co.lz.controller.match;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.Officials;
import za.co.lz.domain.team.Team;
import za.co.lz.service.match.impl.MatchServiceImpl;
import za.co.lz.service.match.impl.OfficialsServiceImpl;
import za.co.lz.util.TestFixtures;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OfficialsControllerTest {

    private MockMvc mockMvc;
    private OfficialsServiceImpl officialsService;
    private MatchServiceImpl matchService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Team home = TestFixtures.team("Home FC");
    private final Team away = TestFixtures.team("Away FC");
    private final Match match = TestFixtures.match(home, away);

    @BeforeEach
    void setUp() {
        OfficialsController controller = new OfficialsController();
        officialsService = mock(OfficialsServiceImpl.class);
        matchService = mock(MatchServiceImpl.class);
        ReflectionTestUtils.setField(controller, "officialsService", officialsService);
        ReflectionTestUtils.setField(controller, "matchService", matchService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void assignOfficials_returnsCreatedOfficials() throws Exception {
        when(matchService.findById(match.getMatchId())).thenReturn(Optional.of(match));
        Officials officials = new Officials.Builder()
                .setOfficialsId(UUID.randomUUID())
                .setMatch(match)
                .setReferee("R. Referee")
                .build();
        when(officialsService.assignOfficials(eq(match), eq("R. Referee"), any(), any(), any(), any()))
                .thenReturn(officials);

        OfficialsController.OfficialsRequest request = new OfficialsController.OfficialsRequest();
        request.setReferee("R. Referee");

        mockMvc.perform(post("/api/officials/match/" + match.getMatchId() + "/assign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.referee").value("R. Referee"));
    }

    @Test
    void getOfficialsForMatch_returnsOfficials() throws Exception {
        Officials officials = new Officials.Builder()
                .setOfficialsId(UUID.randomUUID())
                .setMatch(match)
                .setReferee("R. Referee")
                .build();
        when(officialsService.getByMatch(match.getMatchId())).thenReturn(Optional.of(officials));

        mockMvc.perform(get("/api/officials/match/" + match.getMatchId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.referee").value("R. Referee"));
    }

    @Test
    void deleteOfficials_returnsNoContent() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/officials/delete/" + id))
                .andExpect(status().isNoContent());

        verify(officialsService).delete(id);
    }
}
