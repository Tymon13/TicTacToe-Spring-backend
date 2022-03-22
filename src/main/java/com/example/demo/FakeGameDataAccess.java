package com.example.demo;

import org.springframework.stereotype.Repository;

@Repository
public class FakeGameDataAccess implements GameDao {
    private Player[][] gameBoard = null;

    @Override
    public boolean gameInProgress() {
        return gameBoard != null;
    }

    @Override
    public void endGame() {
        gameBoard = null;
    }

    @Override
    public void beginNewGame() {
        gameBoard = new Player[10][10];
    }

    @Override
    public void setAtXY(Player p, int x, int y) {
        gameBoard[x][y] = p;
    }

    @Override
    public Player getAtXY(int x, int y) {
        return gameBoard[x][y];
    }

    @Override
    public Player[][] getGameState() {
        return gameBoard;
    }
}
