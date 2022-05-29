package com.example.demo.controller;

import com.example.demo.exception.PlayerDoesntExist;
import com.example.demo.entity.PlayerEntity;
import com.example.demo.service.PlayerService;
import com.example.demo.dto.PlayerStatsDto;
import org.springframework.web.bind.annotation.*;

@RestController
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping(path = "/player/new")
    public PlayerEntity addPlayer(@RequestParam String name) {
        return playerService.add(name);
    }

    @GetMapping(path = "/player/{id}")
    public PlayerEntity getPlayer(@PathVariable int id) throws PlayerDoesntExist {
        return playerService.get(id);
    }

    @GetMapping(path = "/player/{id}/stats")
    public PlayerStatsDto getPlayerStats(@PathVariable int id) throws PlayerDoesntExist {
        return playerService.getStats(id);
    }
}
