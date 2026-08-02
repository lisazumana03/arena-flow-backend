package za.co.lz.controller.match;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import za.co.lz.domain.match.Match;
import za.co.lz.domain.match.MatchLineup;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;
import za.co.lz.factory.match.MatchLineupFactory;
import za.co.lz.service.match.impl.MatchLineupServiceImpl;
import za.co.lz.service.match.impl.MatchServiceImpl;
import za.co.lz.service.team.impl.PlayerServiceImpl;
import za.co.lz.service.team.impl.TeamServiceImpl;
import za.co.lz.util.TestFixtures;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MatchLineupControllerTest {

    private MockMvc mockMvc;
    private MatchLineupServiceImpl matchLineupService;
    private MatchServiceImpl matchService;
    private TeamServiceImpl teamService;
    private PlayerServiceImpl playerService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Team home = TestFixtures.team("Home FC");
    private final Team away = TestFixtures.team("Away FC");
    private final Match match = TestFixtures.match(home, away);
    private final Player player = TestFixtures.player(home, "Sipho", "Nkosi", PlayerPosition.ST);

    @BeforeEach
    void setUp() {
        MatchLineupController controller = new MatchLineupController();
        matchLineupService = mock(MatchLineupServiceImpl.class);
        matchService = mock(MatchServiceImpl.class);
        teamService = mock(TeamServiceImpl.class);
        playerService = mock(PlayerServiceImpl.class);
        ReflectionTestUtils.setField(controller, "matchLineupService", matchLineupService);
        ReflectionTestUtils.setField(controller, "matchService", matchService);
        ReflectionTestUtils.setField(controller, "teamService", teamService);
        ReflectionTestUtils.setField(controller, "playerService", playerService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void namePlayer_returnsCreatedLineupEntry() throws Exception {
        when(matchService.findById(match.getMatchId())).thenReturn(Optional.of(match));
        when(teamService.findById(home.getTeamId())).thenReturn(Optional.of(home));
        when(playerService.findById(player.getPlayerId())).thenReturn(Optional.of(player));
        MatchLineup lineup = MatchLineupFactory.createLineupEntry(match, home, player, true, 9, PlayerPosition.ST);
        when(matchLineupService.namePlayer(eq(match), eq(home), eq(player), eq(true), eq(9), eq(PlayerPosition.ST)))
                .thenReturn(lineup);

        MatchLineupController.NamePlayerRequest request = new MatchLineupController.NamePlayerRequest();
        request.setPlayerId(player.getPlayerId());
        request.setStarting(true);
        request.setShirtNumber(9);
        request.setMatchPosition(PlayerPosition.ST);

        mockMvc.perform(post("/api/lineup/match/" + match.getMatchId() + "/team/" + home.getTeamId() + "/name-player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shirtNumber").value(9))
                .andExpect(jsonPath("$.starting").value(true));
    }

    @Test
    void getStartingXI_delegatesToService() throws Exception {
        MatchLineup lineup = MatchLineupFactory.createLineupEntry(match, home, player, true, 9, PlayerPosition.ST);
        when(matchLineupService.getStartingXI(match.getMatchId(), home.getTeamId())).thenReturn(List.of(lineup));

        mockMvc.perform(get("/api/lineup/match/" + match.getMatchId() + "/team/" + home.getTeamId() + "/starting-xi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void substitutePlayerOff_returnsUpdatedLineupEntry() throws Exception {
        MatchLineup lineup = MatchLineupFactory.createLineupEntry(match, home, player, true, 9, PlayerPosition.ST);
        lineup.setSubstitutedOffMinute(75);
        when(matchLineupService.substitutePlayerOff(match.getMatchId(), player.getPlayerId(), 75)).thenReturn(lineup);

        mockMvc.perform(post("/api/lineup/match/" + match.getMatchId() + "/player/" + player.getPlayerId() + "/substitute-off")
                        .param("minute", "75"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.substitutedOffMinute").value(75));
    }
}
