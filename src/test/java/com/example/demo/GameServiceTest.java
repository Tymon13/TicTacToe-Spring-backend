package com.example.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private final GameService uut = new GameService(new FakeGameProvider());

    @Test
    public void testBeginNewGameReturnsNoError() {
        uut.beginNewGame();
    }

    @Test
    public void testMakeAMoveForGameThatDoesntExist() {
        final int nonexistentGameId = 13131313;
        assertThrowsExactly(GameDoesntExistException.class, () -> uut.playNextMove(nonexistentGameId, Player.Player1, 0, 0));
    }

    @Test
    public void testMakeAMoveOnAlreadyOccupiedSpace() throws GameDoesntExistException, IllegalMoveException {
        int gameId = uut.beginNewGame();
        uut.playNextMove(gameId, Player.Player1, 0, 0);
        assertThrowsExactly(IllegalMoveException.class, () -> uut.playNextMove(gameId, Player.Player1, 0, 0));
    }

    @Test
    public void checkWinnerInRow() throws GameDoesntExistException, IllegalMoveException {
        int gameId = uut.beginNewGame();
        uut.playNextMove(gameId, Player.Player1, 0, 0);
        uut.playNextMove(gameId, Player.Player1, 0, 1);
        uut.playNextMove(gameId, Player.Player1, 0, 2);
        uut.playNextMove(gameId, Player.Player1, 0, 3);
        uut.playNextMove(gameId, Player.Player1, 0, 4);
        assertEquals(Player.Player1, uut.checkWinner(gameId));
    }

    @Test
    public void checkWinnerInColumn() throws GameDoesntExistException, IllegalMoveException {
        int gameId = uut.beginNewGame();
        uut.playNextMove(gameId, Player.Player2, 2, 5);
        uut.playNextMove(gameId, Player.Player2, 3, 5);
        uut.playNextMove(gameId, Player.Player2, 4, 5);
        uut.playNextMove(gameId, Player.Player2, 5, 5);
        uut.playNextMove(gameId, Player.Player2, 6, 5);
        assertEquals(Player.Player2, uut.checkWinner(gameId));
    }

    @Test
    public void checkWinnerInDiagonal() throws GameDoesntExistException, IllegalMoveException {
        int gameId = uut.beginNewGame();
        uut.playNextMove(gameId, Player.Player2, 5, 5);
        uut.playNextMove(gameId, Player.Player2, 6, 6);
        uut.playNextMove(gameId, Player.Player2, 7, 7);
        uut.playNextMove(gameId, Player.Player2, 8, 8);
        uut.playNextMove(gameId, Player.Player2, 9, 9);
        assertEquals(Player.Player2, uut.checkWinner(gameId));
    }

    @Test
    public void checkWinnerInOppositeDiagonal() throws GameDoesntExistException, IllegalMoveException {
        int gameId = uut.beginNewGame();
        uut.playNextMove(gameId, Player.Player1, 9, 1);
        uut.playNextMove(gameId, Player.Player1, 8, 2);
        uut.playNextMove(gameId, Player.Player1, 7, 3);
        uut.playNextMove(gameId, Player.Player1, 6, 4);
        uut.playNextMove(gameId, Player.Player1, 5, 5);
        assertEquals(Player.Player1, uut.checkWinner(gameId));
    }

    @Test
    public void testNoWinnerIfNotInRow() throws GameDoesntExistException, IllegalMoveException {
        int gameId = uut.beginNewGame();
        uut.playNextMove(gameId, Player.Player1, 2, 3);
        uut.playNextMove(gameId, Player.Player1, 2, 5);
        uut.playNextMove(gameId, Player.Player1, 2, 6);
        uut.playNextMove(gameId, Player.Player1, 2, 7);
        uut.playNextMove(gameId, Player.Player1, 2, 8);
        assertNull(uut.checkWinner(gameId));
    }
}