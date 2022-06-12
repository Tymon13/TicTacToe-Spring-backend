package com.example.demo.controller;

import com.example.demo.dto.GameIdDto;
import com.example.demo.dto.GameWinnerDto;
import com.example.demo.exception.GameDoesntExistException;
import com.example.demo.exception.IllegalMoveException;
import com.example.demo.exception.InternalErrorException;
import com.example.demo.exception.PlayerDoesntExist;
import com.example.demo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {
    private final GameService gameService;

    @Autowired
    GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping(path = "/game/new")
    public GameIdDto startNewGame(@RequestParam int player1, @RequestParam int player2)
            throws PlayerDoesntExist, InternalErrorException {
        return gameService.beginNewGame(player1, player2);
    }

    @PutMapping(path = "/game/{id}/play")
    public void playMove(@PathVariable("id") int id, @RequestParam int player, @RequestParam int x, @RequestParam int y)
            throws GameDoesntExistException, IllegalMoveException, InternalErrorException {
        gameService.playNextMove(id, player, x, y);
    }

    @GetMapping(path = "/game/{id}/winner")
    public GameWinnerDto getWinner(@PathVariable("id") int id) throws GameDoesntExistException, InternalErrorException {
        return gameService.checkWinner(id);
    }

    @ExceptionHandler({GameDoesntExistException.class, PlayerDoesntExist.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void gameDoesntExistHandler() {
    }

    @ExceptionHandler(IllegalMoveException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void illegalMoveExceptionHandler() {
    }

    @ExceptionHandler(InternalErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void internalErrorExceptionHandler() {
    }
}
