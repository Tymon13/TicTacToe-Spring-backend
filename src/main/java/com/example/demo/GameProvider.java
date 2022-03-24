package com.example.demo;

public interface GameProvider {
    int makeNewGame();

    GameDao getExistingGame(int id) throws GameDoesntExistException;
}
