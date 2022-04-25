package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping(path = "/player/new")
    public PlayerEntity addPlayer(@RequestParam String name) {
        int id = playerService.add(name);
        return playerService.get(id);
    }

    @GetMapping(path = "/player/{id}")
    public PlayerEntity getPlayer(@PathVariable int id) {
        return playerService.get(id);
    }

//    @GetMapping(path = "/player/{id}/stats}")
//    public PlayerStats getPlayerStats(@PathVariable int id) {
//        return playerService.getStats(id);
//    }
}
