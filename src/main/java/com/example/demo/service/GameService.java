package com.example.demo.service;

import com.example.demo.GameDao;
import com.example.demo.GameProvider;
import com.example.demo.Player;
import com.example.demo.exception.GameDoesntExistException;
import com.example.demo.exception.IllegalMoveException;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final GameProvider gameProvider;
    private static final int WIN_CONDITION = 5;

    public GameService(GameProvider gameProvider) {
        this.gameProvider = gameProvider;
    }

    public int beginNewGame() {
        return gameProvider.makeNewGame();
    }

    public void playNextMove(int gameId, Player p, int x, int y) throws GameDoesntExistException, IllegalMoveException {
        GameDao game = gameProvider.getExistingGame(gameId);
        if (!game.gameInProgress()) {
            throw new GameDoesntExistException();
        }
        if (game.getAtXY(x, y) != null) {
            throw new IllegalMoveException();
        }
        game.setAtXY(p, x, y);
    }

    public Player checkWinner(int gameId) throws GameDoesntExistException {
        GameDao game = gameProvider.getExistingGame(gameId);
        Player[][] gameState = game.getGameState();

        Player winner = checkWinner(gameState);
        if (winner != null) {
            game.endGame();
        }
        return winner;
    }

    private Player checkWinner(Player[][] gameState) {
        Player winner;
        if ((winner = checkWinnerInRows(gameState)) != null) {
            return winner;
        }
        if ((winner = checkWinnerInColumns(gameState)) != null) {
            return winner;
        }
        if ((winner = checkWinnerInDiagonals(gameState)) != null) {
            return winner;
        }
        winner = checkWinnerInOppositeDiagonals(gameState);
        return winner;
    }

    private Player checkWinnerInRows(Player[][] gameState) {
        for (int i = 0; i < gameState.length; i++) {
            Player currentCheck = null;
            int currentCount = 0;
            for (int j = 0; j < gameState[i].length; j++) {
                if (gameState[i][j] != currentCheck) {
                    currentCount = 0;
                    currentCheck = gameState[i][j];
                }
                currentCount++;
                if (currentCheck != null && currentCount >= WIN_CONDITION) {
                    return currentCheck;
                }
            }
        }
        return null;
    }

    private Player checkWinnerInColumns(Player[][] gameState) {
        for (int j = 0; j < gameState[0].length; j++) {
            Player currentCheck = null;
            int currentCount = 0;
            for (int i = 0; i < gameState.length; i++) {
                if (gameState[i][j] != currentCheck) {
                    currentCount = 0;
                    currentCheck = gameState[i][j];
                }
                currentCount++;
                if (currentCheck != null && currentCount >= WIN_CONDITION) {
                    return currentCheck;
                }
            }
        }
        return null;
    }

    private Player checkWinnerInDiagonals(Player[][] gameState) {
        for (int i = 0; i < gameState.length - WIN_CONDITION + 1; i++) {
            for (int j = 0; j < gameState[i].length - WIN_CONDITION + 1; j++) {
                if (gameState[i][j] != null) {
                    Player currentCheck = gameState[i][j];
                    int currentCount = 1;
                    for (; currentCount < WIN_CONDITION; currentCount++) {
                        if (gameState[i + currentCount][j + currentCount] != currentCheck) {
                            break;
                        }
                    }
                    if (currentCount >= WIN_CONDITION) {
                        return currentCheck;
                    }
                }
            }
        }
        return null;
    }

    private Player checkWinnerInOppositeDiagonals(Player[][] gameState) {
        for (int i = gameState.length - 1; i > WIN_CONDITION - 1; i--) {
            for (int j = 0; j < gameState[i].length - WIN_CONDITION + 1; j++) {
                if (gameState[i][j] != null) {
                    Player currentCheck = gameState[i][j];
                    int currentCount = 1;
                    for (; currentCount < WIN_CONDITION; currentCount++) {
                        if (gameState[i - currentCount][j + currentCount] != currentCheck) {
                            break;
                        }
                    }
                    if (currentCount >= WIN_CONDITION) {
                        return currentCheck;
                    }
                }
            }
        }
        return null;
    }
}
