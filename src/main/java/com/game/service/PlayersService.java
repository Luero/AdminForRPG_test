package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

@Service
public class PlayersService {

    private final PlayersRepository playersRepository;
    @Autowired
    public PlayersService(PlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }

    //Get players list
    public List<Player> getPlayers() {
        List<Player> allPlayers = playersRepository.findAll();

        //TODO

        return allPlayers;
    }

    //Get players count
    public Integer playersCount() {
        List<Player> allPlayers = playersRepository.findAll();

        //TODO

        return null;
    }

    //Create player
    public void createPlayer() {
        Player player = new Player();

        //TODO
        //parametres, validation

        Integer level = countPlayersLevel(player.getExperience());
        player.setLevel(level);
        player.setUntilNextLevel(countExperienceUntilNextLevel(player.getExperience(), level));


        playersRepository.save(player);
    }

    //Get a player by id
    public Player getPlayer(long id) {
        return playersRepository.findById(id).orElse(null);
    }

    //Update player
    public void updatePlayer(long id, Player updatedPlayer) {
        updatedPlayer.setId(id);

        Integer level = countPlayersLevel(updatedPlayer.getExperience());
        updatedPlayer.setLevel(level);
        updatedPlayer.setUntilNextLevel(countExperienceUntilNextLevel(updatedPlayer.getExperience(), level));

        playersRepository.save(updatedPlayer);

    }

    //Delete player
    public void deletePlayer(long id) {
        playersRepository.deleteById(id);

    }

    private Integer countPlayersLevel(Integer experience) {
        double techValue = ((Math.sqrt(2500 + 200 * experience)) - 50)/100;
        return (int) Math.round(techValue);
    }

    private Integer countExperienceUntilNextLevel(Integer experience, Integer level)
    {
        return 50 * (level + 1) * (level + 2) - experience;
    }


}
