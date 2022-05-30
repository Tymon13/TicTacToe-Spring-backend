package com.example.demo;

import com.example.demo.exception.GameDoesntExistException;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Repository
public class FakeGameProvider implements GameProvider {
    private static final Map<Integer, GameDao> existingGames = new HashMap<>();

    @Override
    public int makeNewGame() {
        int nextId;
        if (existingGames.isEmpty()) {
            nextId = 0;
        } else {
            nextId = Collections.max(existingGames.keySet()) + 1;
        }
        GameDao newGame = new FakeGameDataAccess();
        newGame.beginNewGame();
        existingGames.put(nextId, newGame);
        return nextId;
    }

    @Override
    public GameDao getExistingGame(int id) throws GameDoesntExistException {
        GameDao game = existingGames.get(id);
        if (game == null) {
            throw new GameDoesntExistException();
        }
        return game;
    }
}
