package com.game.controller;

import com.game.service.PlayersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.game.entity.Player;

import java.util.List;

@Controller
@RequestMapping("/rest")
public class PlayersController {

    private final PlayersService playersService;
    @Autowired
    public PlayersController(PlayersService playersService) {
        this.playersService = playersService;
    }


    // не работает, надо переделать
    @GetMapping("/players")
    public List<Player> findAll() {
        return playersService.findAll();
    }
}
