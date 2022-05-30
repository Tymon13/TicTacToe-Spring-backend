package com.example.demo.controller;

import com.example.demo.dto.PlayerDto;
import com.example.demo.exception.PlayerDoesntExist;
import com.example.demo.service.PlayerService;
import com.example.demo.dto.PlayerStatsDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping(path = "/player/new")
    public PlayerDto addPlayer(@RequestParam String name) {
        return playerService.add(name);
    }

    @GetMapping(path = "/player/{id}")
    public PlayerDto getPlayer(@PathVariable int id) throws PlayerDoesntExist {
        return playerService.get(id);
    }

    @GetMapping(path = "/player/{id}/stats")
    public PlayerStatsDto getPlayerStats(@PathVariable int id) throws PlayerDoesntExist {
        return playerService.getStats(id);
    }

    @ExceptionHandler(PlayerDoesntExist.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void playerDoesntExistHandler() {
    }
}
