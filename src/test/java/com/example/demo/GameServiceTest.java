package com.example.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private final GameService uut = new GameService(new FakeGameDataAccess());

    @Test
    public void testBeginNewGameReturnsNoError() throws GameAlreadyExistsException {
        uut.beginNewGame();
    }

    @Test
    public void testBeginNewGameTwiceThrowsError() throws GameAlreadyExistsException {
        uut.beginNewGame();
        assertThrowsExactly(GameAlreadyExistsException.class, uut::beginNewGame);
    }

    @Test
    public void testMakeAMoveIfNoGameInProgress() {
        assertThrowsExactly(GameDoesntExistException.class, () -> uut.playNextMove(Player.Player1, 0, 0));
    }

    @Test
    public void testMakeAMoveOnAlreadyOccupiedSpace() throws GameDoesntExistException, IllegalMoveException, GameAlreadyExistsException {
        uut.beginNewGame();
        uut.playNextMove(Player.Player1, 0, 0);
        assertThrowsExactly(IllegalMoveException.class, () -> uut.playNextMove(Player.Player1, 0, 0));
    }

    @Test
    public void checkWinnerInRow() throws GameAlreadyExistsException, GameDoesntExistException, IllegalMoveException {
        uut.beginNewGame();
        uut.playNextMove(Player.Player1, 0, 0);
        uut.playNextMove(Player.Player1, 0, 1);
        uut.playNextMove(Player.Player1, 0, 2);
        uut.playNextMove(Player.Player1, 0, 3);
        uut.playNextMove(Player.Player1, 0, 4);
        assertEquals(Player.Player1, uut.checkWinner());
    }

    @Test
    public void checkWinnerInColumn() throws GameAlreadyExistsException, GameDoesntExistException, IllegalMoveException {
        uut.beginNewGame();
        uut.playNextMove(Player.Player2, 2, 5);
        uut.playNextMove(Player.Player2, 3, 5);
        uut.playNextMove(Player.Player2, 4, 5);
        uut.playNextMove(Player.Player2, 5, 5);
        uut.playNextMove(Player.Player2, 6, 5);
        assertEquals(Player.Player2, uut.checkWinner());
    }

    @Test
    public void checkWinnerInDiagonal() throws GameAlreadyExistsException, GameDoesntExistException, IllegalMoveException {
        uut.beginNewGame();
        uut.playNextMove(Player.Player2, 5, 5);
        uut.playNextMove(Player.Player2, 6, 6);
        uut.playNextMove(Player.Player2, 7, 7);
        uut.playNextMove(Player.Player2, 8, 8);
        uut.playNextMove(Player.Player2, 9, 9);
        assertEquals(Player.Player2, uut.checkWinner());
    }

    @Test
    public void checkWinnerInOppositeDiagonal() throws GameAlreadyExistsException, GameDoesntExistException, IllegalMoveException {
        uut.beginNewGame();
        uut.playNextMove(Player.Player1, 9, 1);
        uut.playNextMove(Player.Player1, 8, 2);
        uut.playNextMove(Player.Player1, 7, 3);
        uut.playNextMove(Player.Player1, 6, 4);
        uut.playNextMove(Player.Player1, 5, 5);
        assertEquals(Player.Player1, uut.checkWinner());
    }

    @Test
    public void testNoWinnerIfNotInRow() throws GameAlreadyExistsException, GameDoesntExistException, IllegalMoveException {
        uut.beginNewGame();
        uut.playNextMove(Player.Player1, 2, 3);
        uut.playNextMove(Player.Player1, 2, 5);
        uut.playNextMove(Player.Player1, 2, 6);
        uut.playNextMove(Player.Player1, 2, 7);
        uut.playNextMove(Player.Player1, 2, 8);
        assertNull(uut.checkWinner());
    }

    @Test
    public void newGameCanBeStartedAfterWin() throws GameAlreadyExistsException, GameDoesntExistException, IllegalMoveException {
        uut.beginNewGame();
        uut.playNextMove(Player.Player2, 5, 9);
        uut.playNextMove(Player.Player2, 6, 9);
        uut.playNextMove(Player.Player2, 7, 9);
        uut.playNextMove(Player.Player2, 8, 9);
        uut.playNextMove(Player.Player2, 9, 9);
        assertEquals(Player.Player2, uut.checkWinner());

        uut.beginNewGame();
    }
}