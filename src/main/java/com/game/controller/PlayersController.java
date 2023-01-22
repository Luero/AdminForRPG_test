package com.game.controller;

import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayersRepository;
import com.game.service.PlayersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.game.entity.Player;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest")
public class PlayersController {

    private final PlayersService playersService;
    private final PlayersRepository playersRepository;

    @Autowired
    public PlayersController(PlayersService playersService,
                             PlayersRepository playersRepository) {
        this.playersService = playersService;
        this.playersRepository = playersRepository;
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
                                                                     @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        final List<Player> players = playersService.getPlayers(name, title, race, profession, after, before, banned, minExperience,
                maxExperience, minLevel, maxLevel);
        if(order == null)
        {
            order = PlayerOrder.ID;
        }

        final List<Player> sortedPlayers = playersService.sortPlayers(players, order);

        return playersService.getPages(sortedPlayers, pageNumber,pageSize);
    }

    @RequestMapping(path = "/players/count", method = RequestMethod.GET)
    public Integer countPlayers(@RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "title", required = false) String title,
                                     @RequestParam(value = "race", required = false) Race race,
                                     @RequestParam(value = "profession", required = false) Profession profession,
                                     @RequestParam(value = "after", required = false) Long after,
                                     @RequestParam(value = "before", required = false) Long before,
                                     @RequestParam(value = "banned", required = false) Boolean banned,
                                     @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                     @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                     @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                     @RequestParam(value = "maxLevel", required = false) Integer maxLevel)
    {
        return playersService.getPlayers(name, title, race, profession, after, before, banned, minExperience,
                maxExperience, minLevel, maxLevel).size();
    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Player> createPlayer (@RequestBody Player player)
    {
        if(!playersService.isPlayerValid(player))
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else {
            final Player newPlayer = playersService.createPlayer(player);
            return new ResponseEntity<>(newPlayer, HttpStatus.OK);
        }
    }

    @GetMapping("/players/{id}")
    public @ResponseBody ResponseEntity<Player> getPlayer(@PathVariable(value = "id") String inputId) {

        Long id = playersService.scanID(inputId);

        if(id == null || id <= 0)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Player player = playersService.getPlayer(id);

        if(player == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Player>(player, HttpStatus.OK);
    }

    @PostMapping("/players/{id}")
    public @ResponseBody ResponseEntity<Player> updatePlayer(@PathVariable(value = "id") String inputId,
                                                             @RequestBody Player player)
    {
        Long id = playersService.scanID(inputId);

        if(id == null || id <= 0)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Player toBeUpdated = getPlayer(inputId).getBody();

        if(toBeUpdated == null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
        {
            return new ResponseEntity<>(playersService.updatePlayer(id, player), HttpStatus.OK);
        }
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<Player> deletePlayer(@PathVariable(value = "id") String inputId)
    {
        Long id = playersService.scanID(inputId);

        if(id == null || id <= 0)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Player toBeDeleted = getPlayer(inputId).getBody();

        if(toBeDeleted == null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
        {
            playersService.deletePlayer(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

    }



}
