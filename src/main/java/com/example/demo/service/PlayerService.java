package com.example.demo.service;

import com.example.demo.dto.PlayerStatsDto;
import com.example.demo.entity.GameCompletionEntity;
import com.example.demo.entity.GameStateEntity;
import com.example.demo.entity.PlayerEntity;
import com.example.demo.exception.PlayerDoesntExist;
import com.example.demo.repository.GameCompletionRepository;
import com.example.demo.repository.GameStateRepository;
import com.example.demo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final GameStateRepository gameStateRepository;
    private final GameCompletionRepository gameCompletionRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, GameStateRepository gameStateRepository,
                         GameCompletionRepository gameCompletionRepository) {
        this.playerRepository = playerRepository;
        this.gameStateRepository = gameStateRepository;
        this.gameCompletionRepository = gameCompletionRepository;
    }

    public int add(String name) {
        PlayerEntity player = new PlayerEntity();
        player.setName(name);
        player = playerRepository.saveAndFlush(player);
        return player.getId();
    }

    public PlayerEntity get(int id) throws PlayerDoesntExist {
        Optional<PlayerEntity> optional = playerRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            throw new PlayerDoesntExist();
    }

    public PlayerStatsDto getStats(int id) throws PlayerDoesntExist {
        Optional<PlayerEntity> optional = playerRepository.findById(id);
        if (optional.isEmpty())
            throw new PlayerDoesntExist();
        PlayerEntity player = optional.get();
        List<GameStateEntity> gamesWithPlayer = gameStateRepository.findAllByPlayer1OrPlayer2(player);
        List<GameCompletionEntity> completions = gameCompletionRepository.getAllByGameIn(gamesWithPlayer);

        int wins = 0;
        int losses = 0;
        int draws = 0;
        for(GameCompletionEntity completion: completions) {
            if (completion.getWinner() == player) {
                wins++;
            } else if (completion.getWinner() == null) {
                draws++;
            } else {
                losses++;
            }
        }

        return new PlayerStatsDto(player.getId(), wins, losses, draws);
    }
}
