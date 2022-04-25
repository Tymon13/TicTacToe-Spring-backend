package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping(path = "/player/new")
    public Player addPlayer(@RequestParam String name) {
        int id = playerService.add(name);
        return playerService.get(id);
    }

    @GetMapping(path = "/player/{id}}")
    public Player getPlayer(@PathVariable int id) {
        return playerService.get(id);
    }
}
