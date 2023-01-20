package com.game.controller;

import com.game.service.PlayersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.game.entity.Player;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class PlayersController {

    private final PlayersService playersService;

    @Autowired
    public PlayersController(PlayersService playersService) {
        this.playersService = playersService;
    }

    @GetMapping("/players")
    public @ResponseBody ResponseEntity<List<Player>> getPlayersList() {
        return new ResponseEntity<List<Player>>(playersService.getPlayers(), HttpStatus.OK);
    }

    @GetMapping("/players/{id}")
    public @ResponseBody ResponseEntity<Player> getPlayer (@PathVariable long id) {
        //Test without validation
        return new ResponseEntity<Player>(playersService.getPlayer(id), HttpStatus.OK);
    }



}
