package com.example.demo.controller;

import com.example.demo.*;
import com.example.demo.dto.GameIdDto;
import com.example.demo.exception.GameDoesntExistException;
import com.example.demo.exception.IllegalMoveException;
import com.example.demo.service.GameService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping(path = "/game/new")
    public GameIdDto startNewGame() {
        return new GameIdDto(gameService.beginNewGame());
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

    @ExceptionHandler(GameDoesntExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void gameDoesntExistHandler() {
    }

    @ExceptionHandler(IllegalMoveException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void illegalMoveExceptionHandler() {
    }
}
