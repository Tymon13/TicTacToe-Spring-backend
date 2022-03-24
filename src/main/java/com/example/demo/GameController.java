package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping(path = "/game/new")
    public GameId startNewGame() {
        return new GameId(gameService.beginNewGame());
    }

    @PutMapping(path = "/game/{id}/play")
    public void playMove(@PathVariable("id") int id, @RequestParam Player player, @RequestParam int x,
                         @RequestParam int y) throws GameDoesntExistException, IllegalMoveException {
        gameService.playNextMove(id, player, x, y);
    }

    @GetMapping(path = "/game/{id}/winner")
    public Player getWinner(@PathVariable("id") int id) throws GameDoesntExistException {
        return gameService.checkWinner(id);
    }
}
