package com.example.demo.service;

import com.example.demo.dto.GameIdDto;
import com.example.demo.dto.GameWinnerDto;
import com.example.demo.entity.GameStateEntity;
import com.example.demo.entity.PlayerEntity;
import com.example.demo.exception.GameDoesntExistException;
import com.example.demo.exception.IllegalMoveException;
import com.example.demo.exception.InternalErrorException;
import com.example.demo.exception.PlayerDoesntExist;
import com.example.demo.repository.GameStateRepository;
import com.example.demo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

@Service
public class GameService {
    private static final int WIN_CONDITION = 5;
    private final GameStateRepository gameStateRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public GameService(GameStateRepository gameStateRepository, PlayerRepository playerRepository) {
        this.gameStateRepository = gameStateRepository;
        this.playerRepository = playerRepository;
    }

    public GameIdDto beginNewGame(int player1Id, int player2Id) throws InternalErrorException, PlayerDoesntExist {
        Optional<PlayerEntity> player1Optional = playerRepository.findById(player1Id);
        if (player1Optional.isEmpty()) {
            throw new PlayerDoesntExist();
        }
        Optional<PlayerEntity> player2Optional = playerRepository.findById(player2Id);
        if (player2Optional.isEmpty()) {
            throw new PlayerDoesntExist();
        }

        GameStateEntity gameState = new GameStateEntity(encodeGameState(new int[10][10]), player1Optional.get(),
                player2Optional.get());
        gameStateRepository.save(gameState);
        return new GameIdDto(gameState.getId());
    }

    public void playNextMove(int gameId, int playerId, int x, int y)
            throws GameDoesntExistException, IllegalMoveException, InternalErrorException {
        Optional<GameStateEntity> gameStateEntityOptional = gameStateRepository.findById(gameId);
        if (gameStateEntityOptional.isEmpty()) {
            throw new GameDoesntExistException();
        }
        GameStateEntity gameStateEntity = gameStateEntityOptional.get();
        if (playerId != gameStateEntity.getPlayer1().getId() && playerId != gameStateEntity.getPlayer2().getId()) {
            throw new IllegalMoveException();
        }

        int[][] gameState = decodeGameState(gameStateEntity.getD());
        if (gameState[x][y] != 0) {
            throw new IllegalMoveException();
        }
        gameState[x][y] = playerId;
        gameStateEntity.setD(encodeGameState(gameState));
        gameStateRepository.save(gameStateEntity);
    }

    private int[][] decodeGameState(byte[] data) throws InternalErrorException {
        ByteArrayInputStream in2 = new ByteArrayInputStream(data);
        try {
            return (int[][]) new ObjectInputStream(in2).readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new InternalErrorException();
        }
    }

    private byte[] encodeGameState(int[][] gameState) throws InternalErrorException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(out).writeObject(gameState);
        } catch (IOException e) {
            throw new InternalErrorException();
        }
        return out.toByteArray();
    }

    public GameWinnerDto checkWinner(int gameId) throws GameDoesntExistException, InternalErrorException {
        Optional<GameStateEntity> gameStateEntityOptional = gameStateRepository.findById(gameId);
        if (gameStateEntityOptional.isEmpty()) {
            throw new GameDoesntExistException();
        }
        GameStateEntity gameStateEntity = gameStateEntityOptional.get();
        int[][] gameState = decodeGameState(gameStateEntity.getD());


        int winner = checkWinner(gameState);
        if (winner != 0) {
            return new GameWinnerDto(gameId, winner);
        }
        return new GameWinnerDto(gameId, null);
    }

    private int checkWinner(int[][] gameState) {
        int winner;
        if ((winner = checkWinnerInRows(gameState)) != 0) {
            return winner;
        }
        if ((winner = checkWinnerInColumns(gameState)) != 0) {
            return winner;
        }
        if ((winner = checkWinnerInDiagonals(gameState)) != 0) {
            return winner;
        }
        winner = checkWinnerInOppositeDiagonals(gameState);
        return winner;
    }

    private int checkWinnerInRows(int[][] gameState) {
        for (int i = 0; i < gameState.length; i++) {
            int currentCheck = 0;
            int currentCount = 0;
            for (int j = 0; j < gameState[i].length; j++) {
                if (gameState[i][j] != currentCheck) {
                    currentCount = 0;
                    currentCheck = gameState[i][j];
                }
                currentCount++;
                if (currentCheck != 0 && currentCount >= WIN_CONDITION) {
                    return currentCheck;
                }
            }
        }
        return 0;
    }

    private int checkWinnerInColumns(int[][] gameState) {
        for (int j = 0; j < gameState[0].length; j++) {
            int currentCheck = 0;
            int currentCount = 0;
            for (int i = 0; i < gameState.length; i++) {
                if (gameState[i][j] != currentCheck) {
                    currentCount = 0;
                    currentCheck = gameState[i][j];
                }
                currentCount++;
                if (currentCheck != 0 && currentCount >= WIN_CONDITION) {
                    return currentCheck;
                }
            }
        }
        return 0;
    }

    private int checkWinnerInDiagonals(int[][] gameState) {
        for (int i = 0; i < gameState.length - WIN_CONDITION + 1; i++) {
            for (int j = 0; j < gameState[i].length - WIN_CONDITION + 1; j++) {
                if (gameState[i][j] != 0) {
                    int currentCheck = gameState[i][j];
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
        return 0;
    }

    private int checkWinnerInOppositeDiagonals(int[][] gameState) {
        for (int i = gameState.length - 1; i > WIN_CONDITION - 1; i--) {
            for (int j = 0; j < gameState[i].length - WIN_CONDITION + 1; j++) {
                if (gameState[i][j] != 0) {
                    int currentCheck = gameState[i][j];
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
        return 0;
    }

}
