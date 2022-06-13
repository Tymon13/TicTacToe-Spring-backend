package com.example.demo.unit;

import com.example.demo.dto.PlayerDto;
import com.example.demo.dto.PlayerStatsDto;
import com.example.demo.entity.GameCompletionEntity;
import com.example.demo.entity.GameStateEntity;
import com.example.demo.entity.PlayerEntity;
import com.example.demo.exception.PlayerDoesntExist;
import com.example.demo.repository.GameCompletionRepository;
import com.example.demo.repository.GameStateRepository;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.service.PlayerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

public class PlayerServiceTest {
    private AutoCloseable closeable;
    @Mock
    private GameStateRepository gameStateRepositoryMock;
    @Mock
    private GameCompletionRepository gameCompletionRepositoryMock;
    @Mock
    private PlayerRepository playerRepositoryMock;

    private PlayerService uut;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        uut = new PlayerService(playerRepositoryMock, gameStateRepositoryMock, gameCompletionRepositoryMock);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void addNewPlayer() {
        String playerName = "Somebody I used to know";
        int id = 13;

        doAnswer(invocation -> {
            ((PlayerEntity) invocation.getArgument(0)).setId(id);
            return invocation.getArgument(0);
        }).when(playerRepositoryMock).saveAndFlush(argThat(argument -> Objects.equals(argument.getName(), playerName)));

        assertEquals(new PlayerDto(id, playerName), uut.add(playerName));
    }

    @Test
    public void getExistingPlayer() throws PlayerDoesntExist {
        PlayerEntity player = new PlayerEntity();
        player.setId(13);
        player.setName("qwerty");

        doReturn(Optional.of(player)).when(playerRepositoryMock).findById(player.getId());

        assertEquals(new PlayerDto(player.getId(), player.getName()), uut.get(player.getId()));
    }

    @Test
    public void getNonExistingPlayerThrows() throws PlayerDoesntExist {
        int id = 13;

        doReturn(Optional.empty()).when(playerRepositoryMock).findById(id);

        assertThrows(PlayerDoesntExist.class, () -> uut.get(id));
    }

    @Test
    public void getStatsWithNoMatches() throws PlayerDoesntExist {
        PlayerEntity player = new PlayerEntity();
        player.setId(13);
        player.setName("qwerty");

        doReturn(Optional.of(player)).when(playerRepositoryMock).findById(player.getId());
        doReturn(new ArrayList<GameStateEntity>()).when(gameStateRepositoryMock).findAllByPlayer1OrPlayer2(player);
        doReturn(new ArrayList<GameCompletionEntity>())
                .when(gameCompletionRepositoryMock)
                .getAllByGameIn(new ArrayList<>());

        assertEquals(new PlayerStatsDto(player.getId(), 0, 0, 0), uut.getStats(player.getId()));
    }

    @Test
    public void getStatsWithSomeMatches() throws PlayerDoesntExist {
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(13);
        player1.setName("qwerty");
        PlayerEntity player2 = new PlayerEntity();
        player2.setId(169);
        player2.setName("azerty");

        List<GameStateEntity> gameStates = new ArrayList<>();
        List<GameCompletionEntity> gameCompletions = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            GameCompletionEntity gameCompletion = new GameCompletionEntity();
            gameCompletion.setWinner(player1);
            gameCompletions.add(gameCompletion);
        }
        for (int i = 4; i < 7; ++i) {
            GameCompletionEntity gameCompletion = new GameCompletionEntity();
            gameCompletion.setWinner(player2);
            gameCompletions.add(gameCompletion);
        }
        for (int i = 7; i < 10; ++i) {
            gameCompletions.add(new GameCompletionEntity());
        }
        Collections.shuffle(gameCompletions, new Random(13));

        doReturn(Optional.of(player1)).when(playerRepositoryMock).findById(player1.getId());
        doReturn(gameStates).when(gameStateRepositoryMock).findAllByPlayer1OrPlayer2(player1);
        doReturn(gameCompletions).when(gameCompletionRepositoryMock).getAllByGameIn(refEq(gameStates));

        assertEquals(new PlayerStatsDto(player1.getId(), 4, 3, 3), uut.getStats(player1.getId()));
    }
}
