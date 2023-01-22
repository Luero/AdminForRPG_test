package com.game.controller;

import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
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

    @RequestMapping(path = "/players", method = RequestMethod.GET)
    public List<Player> getPlayersList(@RequestParam(value = "name", required = false) String name,
                                                                     @RequestParam(value = "title", required = false) String title,
                                                                     @RequestParam(value = "race", required = false) Race race,
                                                                     @RequestParam(value = "profession", required = false) Profession profession,
                                                                     @RequestParam(value = "after", required = false) Long after,
                                                                     @RequestParam(value = "before", required = false) Long before,
                                                                     @RequestParam(value = "banned", required = false) Boolean banned,
                                                                     @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                                                     @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                                                     @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                                                     @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                                                     @RequestParam(value = "order", required = false) PlayerOrder order,
                                                                     @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                                     @RequestParam(value = "pageSizw", required = false) Integer pageSize) {

        final List<Player> players = playersService.getPlayers(name, title, race, profession, after, before, banned, minExperience,
                maxExperience, minLevel, maxLevel);

        final List<Player> sortedPlayers = playersService.sortPlayers(players, order);

        return playersService.getPages(sortedPlayers, pageNumber,pageSize);
    }

    @GetMapping("/players/{id}")
    public @ResponseBody ResponseEntity<Player> getPlayer (@PathVariable long id) {
        //Test without validation
        return new ResponseEntity<Player>(playersService.getPlayer(id), HttpStatus.OK);
    }



}
