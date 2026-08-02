package za.co.lz.controller.team.discipline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import za.co.lz.domain.match.InjurySeverity;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.discipline.PlayerInjury;
import za.co.lz.factory.team.discipline.PlayerInjuryFactory;
import za.co.lz.service.match.impl.MatchServiceImpl;
import za.co.lz.service.team.discipline.impl.PlayerInjuryServiceImpl;
import za.co.lz.service.team.impl.PlayerServiceImpl;
import za.co.lz.util.TestFixtures;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PlayerInjuryControllerTest {

    private MockMvc mockMvc;
    private PlayerInjuryServiceImpl playerInjuryService;
    private PlayerServiceImpl playerService;
    private MatchServiceImpl matchService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private final Team team = TestFixtures.team("Home FC");
    private final Player player = TestFixtures.player(team, "Sipho", "Nkosi", PlayerPosition.CB);
    private final Match match = TestFixtures.match(team, TestFixtures.team("Away FC"));

    @BeforeEach
    void setUp() {
        PlayerInjuryController controller = new PlayerInjuryController();
        playerInjuryService = mock(PlayerInjuryServiceImpl.class);
        playerService = mock(PlayerServiceImpl.class);
        matchService = mock(MatchServiceImpl.class);
        ReflectionTestUtils.setField(controller, "playerInjuryService", playerInjuryService);
        ReflectionTestUtils.setField(controller, "playerService", playerService);
        ReflectionTestUtils.setField(controller, "matchService", matchService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void reportInjury_estimatesReturnDateFromSeverity() throws Exception {
        when(playerService.findById(player.getPlayerId())).thenReturn(Optional.of(player));
        when(matchService.findById(match.getMatchId())).thenReturn(Optional.of(match));
        LocalDate injuryDate = LocalDate.of(2026, 7, 1);
        PlayerInjury injury = PlayerInjuryFactory.createInjury(player, match, InjurySeverity.MODERATE, injuryDate);
        when(playerInjuryService.reportInjury(player, match, InjurySeverity.MODERATE, injuryDate)).thenReturn(injury);

        PlayerInjuryController.ReportInjuryRequest request = new PlayerInjuryController.ReportInjuryRequest();
        request.setPlayerId(player.getPlayerId());
        request.setTriggeringMatchId(match.getMatchId());
        request.setSeverity(InjurySeverity.MODERATE);
        request.setInjuryDate(injuryDate);

        mockMvc.perform(post("/api/injuries/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expectedReturnDate").value(injuryDate.plusDays(28).toString()));
    }

    @Test
    void markRecovered_delegatesToService() throws Exception {
        UUID injuryId = UUID.randomUUID();
        PlayerInjury injury = PlayerInjuryFactory.createInjury(player, match, InjurySeverity.MINOR, LocalDate.now());
        injury.markRecovered();
        when(playerInjuryService.markRecovered(injuryId)).thenReturn(injury);

        mockMvc.perform(post("/api/injuries/" + injuryId + "/recover"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void isPlayerInjured_returnsServiceResult() throws Exception {
        when(playerInjuryService.isPlayerInjured(player.getPlayerId())).thenReturn(false);

        mockMvc.perform(get("/api/injuries/player/" + player.getPlayerId() + "/is-injured"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
