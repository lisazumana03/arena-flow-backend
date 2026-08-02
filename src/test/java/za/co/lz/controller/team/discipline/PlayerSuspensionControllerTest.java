package za.co.lz.controller.team.discipline;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import za.co.lz.domain.match.DisciplinaryOffence;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.discipline.PlayerSuspension;
import za.co.lz.factory.team.discipline.PlayerSuspensionFactory;
import za.co.lz.service.match.impl.MatchServiceImpl;
import za.co.lz.service.team.discipline.impl.PlayerSuspensionServiceImpl;
import za.co.lz.service.team.impl.PlayerServiceImpl;
import za.co.lz.util.TestFixtures;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PlayerSuspensionControllerTest {

    private MockMvc mockMvc;
    private PlayerSuspensionServiceImpl playerSuspensionService;
    private PlayerServiceImpl playerService;
    private MatchServiceImpl matchService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Team team = TestFixtures.team("Home FC");
    private final Player player = TestFixtures.player(team, "Sipho", "Nkosi", PlayerPosition.CB);
    private final Match match = TestFixtures.match(team, TestFixtures.team("Away FC"));

    @BeforeEach
    void setUp() {
        PlayerSuspensionController controller = new PlayerSuspensionController();
        playerSuspensionService = mock(PlayerSuspensionServiceImpl.class);
        playerService = mock(PlayerServiceImpl.class);
        matchService = mock(MatchServiceImpl.class);
        ReflectionTestUtils.setField(controller, "playerSuspensionService", playerSuspensionService);
        ReflectionTestUtils.setField(controller, "playerService", playerService);
        ReflectionTestUtils.setField(controller, "matchService", matchService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void issueSuspension_usesDefaultBanLengthWhenNoOverrideGiven() throws Exception {
        when(playerService.findById(player.getPlayerId())).thenReturn(Optional.of(player));
        when(matchService.findById(match.getMatchId())).thenReturn(Optional.of(match));
        PlayerSuspension suspension = PlayerSuspensionFactory.createSuspension(player, match, DisciplinaryOffence.VIOLENT_CONDUCT);
        when(playerSuspensionService.issueSuspension(player, match, DisciplinaryOffence.VIOLENT_CONDUCT)).thenReturn(suspension);

        PlayerSuspensionController.IssueSuspensionRequest request = new PlayerSuspensionController.IssueSuspensionRequest();
        request.setPlayerId(player.getPlayerId());
        request.setTriggeringMatchId(match.getMatchId());
        request.setOffence(DisciplinaryOffence.VIOLENT_CONDUCT);

        mockMvc.perform(post("/api/suspensions/issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gamesBanned").value(3));
    }

    @Test
    void isPlayerSuspended_returnsServiceResult() throws Exception {
        when(playerSuspensionService.isPlayerSuspended(player.getPlayerId())).thenReturn(true);

        mockMvc.perform(get("/api/suspensions/player/" + player.getPlayerId() + "/is-suspended"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getActiveSuspensions_returnsList() throws Exception {
        PlayerSuspension suspension = PlayerSuspensionFactory.createSuspension(player, match, DisciplinaryOffence.SECOND_BOOKABLE_OFFENCE);
        when(playerSuspensionService.getActiveSuspensions(player.getPlayerId())).thenReturn(List.of(suspension));

        mockMvc.perform(get("/api/suspensions/player/" + player.getPlayerId() + "/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
