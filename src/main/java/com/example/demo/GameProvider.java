package com.example.demo;

import com.example.demo.exception.GameDoesntExistException;

public interface GameProvider {
    int makeNewGame();

    GameDao getExistingGame(int id) throws GameDoesntExistException;
}
