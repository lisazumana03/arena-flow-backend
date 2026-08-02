package za.co.lz.controller.match;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import za.co.lz.domain.match.*;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.PlayerPosition;
import za.co.lz.domain.team.Team;
import za.co.lz.factory.match.MatchEventFactory;
import za.co.lz.service.match.impl.MatchEventServiceImpl;
import za.co.lz.util.TestFixtures;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MatchEventControllerTest {

    private MockMvc mockMvc;
    private MatchEventServiceImpl matchEventService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Team home = TestFixtures.team("Home FC");
    private final Team away = TestFixtures.team("Away FC");
    private final Match match = TestFixtures.match(home, away);
    private final Player scorer = TestFixtures.player(home, "Sipho", "Nkosi", PlayerPosition.ST);

    @BeforeEach
    void setUp() {
        MatchEventController controller = new MatchEventController();
        matchEventService = mock(MatchEventServiceImpl.class);
        ReflectionTestUtils.setField(controller, "matchEventService", matchEventService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void recordGoal_returnsCreatedEvent() throws Exception {
        MatchEvent event = MatchEventFactory.createGoal(match, home, scorer, null, MatchEventType.GOAL, 23, null);
        when(matchEventService.recordGoal(eq(match.getMatchId()), eq(home.getTeamId()), eq(scorer.getPlayerId()),
                isNull(), eq(MatchEventType.GOAL), eq(23), isNull())).thenReturn(event);

        MatchEventController.GoalRequest request = new MatchEventController.GoalRequest();
        request.setScoringTeamId(home.getTeamId());
        request.setScorerId(scorer.getPlayerId());
        request.setGoalType(MatchEventType.GOAL);
        request.setMinute(23);

        mockMvc.perform(post("/api/match-event/" + match.getMatchId() + "/goal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventType").value("GOAL"))
                .andExpect(jsonPath("$.minute").value(23));
    }

    @Test
    void recordCard_returnsCreatedEvent() throws Exception {
        MatchEvent event = MatchEventFactory.createCard(match, away, scorer, MatchEventType.RED_CARD,
                DisciplinaryOffence.VIOLENT_CONDUCT, 60, null);
        when(matchEventService.recordCard(eq(match.getMatchId()), eq(away.getTeamId()), eq(scorer.getPlayerId()),
                eq(MatchEventType.RED_CARD), eq(DisciplinaryOffence.VIOLENT_CONDUCT), eq(60), isNull()))
                .thenReturn(event);

        MatchEventController.CardRequest request = new MatchEventController.CardRequest();
        request.setTeamId(away.getTeamId());
        request.setPlayerId(scorer.getPlayerId());
        request.setCardType(MatchEventType.RED_CARD);
        request.setOffence(DisciplinaryOffence.VIOLENT_CONDUCT);
        request.setMinute(60);

        mockMvc.perform(post("/api/match-event/" + match.getMatchId() + "/card")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventType").value("RED_CARD"))
                .andExpect(jsonPath("$.offence").value("VIOLENT_CONDUCT"));
    }

    @Test
    void recordCorner_delegatesToServiceWithRequestParams() throws Exception {
        MatchEvent event = MatchEventFactory.createCorner(match, home, 10);
        when(matchEventService.recordCorner(match.getMatchId(), home.getTeamId(), 10)).thenReturn(event);

        mockMvc.perform(post("/api/match-event/" + match.getMatchId() + "/corner")
                        .param("teamId", home.getTeamId().toString())
                        .param("minute", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventType").value("CORNER"));
    }

    @Test
    void recordInjury_returnsCreatedEvent() throws Exception {
        MatchEvent event = MatchEventFactory.createInjury(match, home, scorer, InjurySeverity.MODERATE, 40, "twisted ankle");
        when(matchEventService.recordInjury(eq(match.getMatchId()), eq(home.getTeamId()), eq(scorer.getPlayerId()),
                eq(InjurySeverity.MODERATE), eq(40), eq("twisted ankle"))).thenReturn(event);

        MatchEventController.InjuryRequest request = new MatchEventController.InjuryRequest();
        request.setTeamId(home.getTeamId());
        request.setPlayerId(scorer.getPlayerId());
        request.setSeverity(InjurySeverity.MODERATE);
        request.setMinute(40);
        request.setNotes("twisted ankle");

        mockMvc.perform(post("/api/match-event/" + match.getMatchId() + "/injury")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.injurySeverity").value("MODERATE"));
    }

    @Test
    void getMatchEvents_returnsFullTimeline() throws Exception {
        MatchEvent goal = MatchEventFactory.createGoal(match, home, scorer, null, MatchEventType.GOAL, 23, null);
        when(matchEventService.getMatchEvents(match.getMatchId())).thenReturn(List.of(goal));

        mockMvc.perform(get("/api/match-event/match/" + match.getMatchId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void finalizeMatch_returnsCompletedMatch() throws Exception {
        Match completed = match;
        completed.setStatus(MatchStatus.COMPLETED);
        completed.setResult(MatchResult.HOME_WIN);
        when(matchEventService.finalizeMatch(match.getMatchId())).thenReturn(completed);

        mockMvc.perform(post("/api/match-event/" + match.getMatchId() + "/finalize"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.result").value("HOME_WIN"));
    }
}
