package com.example.demo;

import com.example.demo.dto.GameIdDto;
import com.example.demo.dto.GameWinnerDto;
import com.example.demo.entity.GameStateEntity;
import com.example.demo.entity.PlayerEntity;
import com.example.demo.exception.GameDoesntExistException;
import com.example.demo.exception.IllegalMoveException;
import com.example.demo.exception.InternalErrorException;
import com.example.demo.exception.PlayerDoesntExist;
import com.example.demo.repository.GameCompletionRepository;
import com.example.demo.repository.GameStateRepository;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.service.GameService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

class GameServiceTest {
    private AutoCloseable closeable;
    @Mock
    private GameStateRepository gameStateRepositoryMock;
    @Mock
    private GameCompletionRepository gameCompletionRepositoryMock;
    @Mock
    private PlayerRepository playerRepositoryMock;

    private GameService uut;

    private PlayerEntity player1;
    private PlayerEntity player2;
    private GameStateEntity gameState;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        uut = new GameService(gameStateRepositoryMock, playerRepositoryMock, gameCompletionRepositoryMock);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testBeginNewGameReturnsNoError() throws PlayerDoesntExist, InternalErrorException {
        PlayerEntity player1 = new PlayerEntity();
        PlayerEntity player2 = new PlayerEntity();
        doReturn(Optional.of(player1)).when(playerRepositoryMock).findById(1);
        doReturn(Optional.of(player2)).when(playerRepositoryMock).findById(13);

        int gameId = 169;
        doAnswer(invocation -> {
            ((GameStateEntity) invocation.getArgument(0)).setId(gameId);
            return invocation.getArgument(0);
        }).when(gameStateRepositoryMock).save(any());
        GameIdDto gameIdDto = uut.beginNewGame(1, 13);
        assertEquals(new GameIdDto(gameId), gameIdDto);
    }

    @Test
    public void testBeginNewGameReturnsErrorWithInvalidPlayer() {
        PlayerEntity player1 = new PlayerEntity();
        doReturn(Optional.of(player1)).when(playerRepositoryMock).findById(1);
        doReturn(Optional.empty()).when(playerRepositoryMock).findById(13);
        assertThrows(PlayerDoesntExist.class, () -> uut.beginNewGame(1, 13));
    }

    @Test
    public void testMakeAMoveForGameThatDoesntExist() {
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1);
        doReturn(Optional.of(player1)).when(playerRepositoryMock).findById(player1.getId());

        final int nonexistentGameId = 13131313;
        assertThrows(GameDoesntExistException.class, () -> uut.playNextMove(nonexistentGameId, player1.getId(), 0, 0));
    }

    @Test
    public void testMakeAMoveForPlayerThatIsNotPlaying() throws PlayerDoesntExist, InternalErrorException, IOException {
        int nonExistentPlayerId = 13;

        setUpValidGame();

        uut.beginNewGame(player1.getId(), player2.getId());
        assertThrows(IllegalMoveException.class, () -> uut.playNextMove(gameState.getId(), nonExistentPlayerId, 0, 0));
    }

    @Test
    public void testMakeAMoveOnAlreadyOccupiedSpace() throws GameDoesntExistException, IllegalMoveException,
            IOException, InternalErrorException {
        setUpValidGame();
        uut.playNextMove(gameState.getId(), player1.getId(), 0, 0);
        assertThrows(IllegalMoveException.class, () -> uut.playNextMove(gameState.getId(), player1.getId(), 0, 0));
    }

    @Test
    public void checkWinnerInRow() throws GameDoesntExistException, IllegalMoveException, IOException,
            InternalErrorException {
        setUpValidGame();

        uut.playNextMove(gameState.getId(), player1.getId(), 0, 0);
        uut.playNextMove(gameState.getId(), player1.getId(), 0, 1);
        uut.playNextMove(gameState.getId(), player1.getId(), 0, 2);
        uut.playNextMove(gameState.getId(), player1.getId(), 0, 3);
        uut.playNextMove(gameState.getId(), player1.getId(), 0, 4);
        assertEquals(new GameWinnerDto(gameState.getId(), player1.getId()), uut.checkWinner(gameState.getId()));
    }

    @Test
    public void checkWinnerInColumn() throws GameDoesntExistException, IllegalMoveException, InternalErrorException,
            IOException {
        setUpValidGame();

        uut.playNextMove(gameState.getId(), player2.getId(), 2, 5);
        uut.playNextMove(gameState.getId(), player2.getId(), 3, 5);
        uut.playNextMove(gameState.getId(), player2.getId(), 4, 5);
        uut.playNextMove(gameState.getId(), player2.getId(), 5, 5);
        uut.playNextMove(gameState.getId(), player2.getId(), 6, 5);
        assertEquals(new GameWinnerDto(gameState.getId(), player2.getId()), uut.checkWinner(gameState.getId()));
    }

    @Test
    public void checkWinnerInDiagonal() throws GameDoesntExistException, IllegalMoveException, InternalErrorException,
            IOException {
        setUpValidGame();

        uut.playNextMove(gameState.getId(), player2.getId(), 5, 5);
        uut.playNextMove(gameState.getId(), player2.getId(), 6, 6);
        uut.playNextMove(gameState.getId(), player2.getId(), 7, 7);
        uut.playNextMove(gameState.getId(), player2.getId(), 8, 8);
        uut.playNextMove(gameState.getId(), player2.getId(), 9, 9);

        assertEquals(new GameWinnerDto(gameState.getId(), player2.getId()), uut.checkWinner(gameState.getId()));
    }

    @Test
    public void checkWinnerInOppositeDiagonal() throws GameDoesntExistException, IllegalMoveException,
            InternalErrorException, IOException {
        setUpValidGame();

        uut.playNextMove(gameState.getId(), player1.getId(), 9, 1);
        uut.playNextMove(gameState.getId(), player1.getId(), 8, 2);
        uut.playNextMove(gameState.getId(), player1.getId(), 7, 3);
        uut.playNextMove(gameState.getId(), player1.getId(), 6, 4);
        uut.playNextMove(gameState.getId(), player1.getId(), 5, 5);

        assertEquals(new GameWinnerDto(gameState.getId(), player1.getId()), uut.checkWinner(gameState.getId()));
    }

    @Test
    public void testNoWinnerIfNotInRow() throws GameDoesntExistException, IllegalMoveException, InternalErrorException,
            IOException {
        setUpValidGame();

        uut.playNextMove(gameState.getId(), player1.getId(), 2, 3);
        uut.playNextMove(gameState.getId(), player1.getId(), 2, 5);
        uut.playNextMove(gameState.getId(), player1.getId(), 2, 6);
        uut.playNextMove(gameState.getId(), player1.getId(), 2, 7);
        uut.playNextMove(gameState.getId(), player1.getId(), 2, 8);
        assertEquals(new GameWinnerDto(gameState.getId(), null), uut.checkWinner(gameState.getId()));
    }

    private void setUpValidGame() throws IOException {
        player1 = new PlayerEntity();
        player1.setId(1);
        doReturn(Optional.of(player1)).when(playerRepositoryMock).findById(player1.getId());
        player2 = new PlayerEntity();
        player2.setId(2);
        doReturn(Optional.of(player2)).when(playerRepositoryMock).findById(player2.getId());
        gameState = new GameStateEntity(encodeGameState(new int[10][10]), player1, player2);
        gameState.setId(13);
        doReturn(Optional.of(gameState)).when(gameStateRepositoryMock).findById(gameState.getId());
    }

    private byte[] encodeGameState(int[][] gameState) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ObjectOutputStream(out).writeObject(gameState);
        return out.toByteArray();
    }
}
