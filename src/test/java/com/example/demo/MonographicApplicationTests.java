package com.example.demo;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MonographicApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    private int createNewPlayer() throws Exception {
        MvcResult result = mockMvc.perform(post("/player/new?name=test_username"))
                .andExpect(status().isOk()).andReturn();
        String body = result.getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    private int startNewGame(int player1Id, int player2Id) throws Exception {
        MvcResult result = mockMvc.perform(post(String.format("/game/new?player1=%d&player2=%d", player1Id, player2Id)))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    private void playMove(int gameId, int p, int x, int y) throws Exception {
        mockMvc.perform(put(String.format("/game/%d/play?player=%s&x=%d&y=%d", gameId, p, x, y)))
                .andExpect(status().isOk());
    }

    private void checkWinner(int gameId, int expectedWinner) throws Exception {
        mockMvc.perform(get(String.format("/game/%d/winner", gameId)))
                .andExpect(status().isOk())
                .andExpect(content().json(String.format("{\"gameId\":%d, \"playerId\":%d}", gameId, expectedWinner)));
    }

    @Test
    void runSimpleGame() throws Exception {
        int player1Id = createNewPlayer();
        int player2Id = createNewPlayer();
        int gameId = startNewGame(player1Id, player2Id);
        playMove(gameId, player2Id, 9, 9);
        playMove(gameId, player2Id, 8, 8);
        playMove(gameId, player2Id, 7, 7);
        playMove(gameId, player2Id, 6, 6);
        playMove(gameId, player2Id, 5, 5);
        checkWinner(gameId, player2Id);
    }

    @Test
    void playTwoGamesAtTheSameTime() throws Exception {
        int player1Id = createNewPlayer();
        int player2Id = createNewPlayer();
        int player3Id = createNewPlayer();

        int gameId1 = startNewGame(player1Id, player2Id);
        playMove(gameId1, player2Id, 9, 1);
        playMove(gameId1, player2Id, 8, 2);
        playMove(gameId1, player2Id, 7, 3);

        int gameId2 = startNewGame(player1Id, player3Id);
        playMove(gameId2, player1Id, 1, 1);
        playMove(gameId2, player1Id, 2, 2);

        playMove(gameId1, player2Id, 6, 4);
        playMove(gameId1, player2Id, 5, 5);

        playMove(gameId2, player1Id, 3, 3);
        playMove(gameId2, player1Id, 4, 4);
        playMove(gameId2, player1Id, 5, 5);

        checkWinner(gameId1, player2Id);
        checkWinner(gameId2, player1Id);
    }
}
