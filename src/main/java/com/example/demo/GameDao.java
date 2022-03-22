package com.example.demo;

public interface GameDao {
    void beginNewGame();

    boolean gameInProgress();

    void endGame();

    void setAtXY(Player p, int x, int y);

    Player getAtXY(int x, int y);

    Player[][] getGameState();
}
