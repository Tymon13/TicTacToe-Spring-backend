package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }
    public int add(String name) {
        PlayerEntity player = new PlayerEntity();
        player.setName(name);
        player = playerRepository.saveAndFlush(player);
        return player.getId();
    }

    public PlayerEntity get(int id) {
        return playerRepository.findById(id).orElse(null);
    }
}
