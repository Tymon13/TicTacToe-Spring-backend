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

    private int startNewGame() throws Exception {
        MvcResult result = mockMvc.perform(post("/game/new"))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    private void playMove(int gameId, Player p, int x, int y) throws Exception {
        mockMvc.perform(put(String.format("/game/%d/play?player=%s&x=%d&y=%d", gameId, p.toString(), x, y)))
                .andExpect(status().isOk());
    }

    private void checkWinner(int gameId, Player expectedWinner) throws Exception {
        mockMvc.perform(get(String.format("/game/%d/winner", gameId)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("\"%s\"", expectedWinner.toString())));
    }

    @Test
    void runSimpleGame() throws Exception {
        int gameId = startNewGame();
        playMove(gameId, Player.Player2, 9, 9);
        playMove(gameId, Player.Player2, 8, 8);
        playMove(gameId, Player.Player2, 7, 7);
        playMove(gameId, Player.Player2, 6, 6);
        playMove(gameId, Player.Player2, 5, 5);
        checkWinner(gameId, Player.Player2);
    }

    @Test
    void playTwoGamesAtTheSameTime() throws Exception {
        int gameId1 = startNewGame();
        playMove(gameId1, Player.Player2, 9, 1);
        playMove(gameId1, Player.Player2, 8, 2);
        playMove(gameId1, Player.Player2, 7, 3);

        int gameId2 = startNewGame();
        playMove(gameId2, Player.Player1, 1, 1);
        playMove(gameId2, Player.Player1, 2, 2);

        playMove(gameId1, Player.Player2, 6, 4);
        playMove(gameId1, Player.Player2, 5, 5);

        playMove(gameId2, Player.Player1, 3, 3);
        playMove(gameId2, Player.Player1, 4, 4);
        playMove(gameId2, Player.Player1, 5, 5);

        checkWinner(gameId1, Player.Player2);
        checkWinner(gameId2, Player.Player1);
    }
}
