package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.lang.Long.valueOf;

@Service
public class PlayersService {

    private final PlayersRepository playersRepository;
    @Autowired
    public PlayersService(PlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }

    //Get players list
    public List<Player> getPlayers(String name, String title, Race race, Profession profession, Long after, Long before,
                                   Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel)
    {
        final Date dateAfter = after == null ? null : new Date(after);
        final Date dateBefore = before == null ? null : new Date(before);
        final List<Player> allPlayers = new ArrayList<>();

        playersRepository.findAll().forEach((player) -> {
            if(name != null && !player.getName().contains(name)) return;
            if(title != null && !player.getTitle().contains(title)) return;
            if(race != null && !player.getRace().equals(race)) return;;
            if (profession != null && !player.getProfession().equals(profession)) return;
            if(after != null && player.getBirthday().before(dateAfter)) return;
            if(before != null && player.getBirthday().after(dateBefore)) return;
            if(banned != null && player.getBanned() != banned) return;
            if(minExperience != null && player.getExperience() < minExperience) return;
            if(maxExperience != null && player.getExperience() > maxExperience) return;
            if(minLevel != null && player.getLevel() < minLevel) return;
            if(maxLevel != null && player.getLevel() > maxLevel) return;

            allPlayers.add(player);
        });
        return allPlayers;
    }

    public List<Player> sortPlayers(List<Player> players, PlayerOrder playerOrder)
    {
        players.sort((player1, player2) -> {
            switch (playerOrder) {
                case NAME:
                    return player1.getName().compareTo(player2.getName());
                case LEVEL:
                    return player1.getLevel().compareTo(player2.getLevel());
                case BIRTHDAY:
                    return player1.getBirthday().compareTo(player2.getBirthday());
                case EXPERIENCE:
                    return player1.getExperience().compareTo(player2.getExperience());
                default:
                    Long player1ID = player1.getId();
                    Long player2ID = player2.getId();
                    return player1ID.compareTo(player2ID);
                }
        });

        return players;
    }

    public List<Player> getPages(List<Player> players, Integer pageNumber, Integer pageSize)
    {
        if (pageNumber == null)
        {
            pageNumber = 0;
        }
        if (pageSize == null)
        {
            pageSize = 3;
        }
        final int fromPage = pageNumber * pageSize;
        int toPage = fromPage + pageSize;

        if(toPage > players.size())
        {
            toPage = players.size();
        }
        return players.subList(fromPage, toPage);
    }

    //Create player
    public Player createPlayer(Player player) {

        if (player.getBanned() == null) {player.setBanned(false);}

        Integer level = countPlayersLevel(player.getExperience());
        player.setLevel(level);
        player.setUntilNextLevel(countExperienceUntilNextLevel(player.getExperience(), level));

        return playersRepository.save(player);
    }

    public Boolean isPlayerValid(Player player)
    {
        if (player.getName() == null || player.getTitle() == null || player.getRace() == null || player.getProfession() == null || player.getBirthday() == null || player.getExperience() == null)
        {
            return false;
        }
        // else if (player.getBirthday().before(new Date(0))) return false;
        else if (player.getExperience() > 10000000) return false;
        else if(player.getTitle().length() > 30) return false;
        else return true;

    }

    //Get a player by id
    public Player getPlayer(long id) {
        return playersRepository.findById(id).orElse(null);
    }

    public Long scanID (String inputId) {
        Long id = null;
        try {
            id = Long.parseLong(inputId);
        } catch (Exception e)
        {
            id = null;
        }
        return id;
    }

    //Update player
    public Player updatePlayer(long id, Player updatedPlayer) {

        Player toBeUpdated = playersRepository.findById(id).orElse(null);
        Long originalId = toBeUpdated.getId();
        Integer originalLevel = toBeUpdated.getLevel();
        Integer originalUntilNextLvl = toBeUpdated.getUntilNextLevel();

        if(updatedPlayer.getName() != null)
        {
            toBeUpdated.setName(updatedPlayer.getName());
        }
        if(updatedPlayer.getTitle() != null)
        {
            toBeUpdated.setTitle(updatedPlayer.getTitle());
        }
        if(updatedPlayer.getRace() != null)
        {
            toBeUpdated.setRace(updatedPlayer.getRace());
        }
        if(updatedPlayer.getProfession() != null)
        {
            toBeUpdated.setProfession(updatedPlayer.getProfession());
        }
        if(updatedPlayer.getBirthday() != null)
        {
            toBeUpdated.setBirthday(updatedPlayer.getBirthday());
        }
        if(updatedPlayer.getBanned() != null)
        {
            toBeUpdated.setBanned(updatedPlayer.getBanned());
        }
        if(updatedPlayer.getExperience() != null && updatedPlayer.getExperience() > 0 && updatedPlayer.getExperience() <= 10000000)
        {
            toBeUpdated.setExperience(updatedPlayer.getExperience());
            toBeUpdated.setLevel(countPlayersLevel(updatedPlayer.getExperience()));
            toBeUpdated.setUntilNextLevel(countExperienceUntilNextLevel(updatedPlayer.getExperience(), countPlayersLevel(updatedPlayer.getExperience())));
        } else
        {
            Integer level = countPlayersLevel(toBeUpdated.getExperience());
            toBeUpdated.setLevel(level);
            toBeUpdated.setUntilNextLevel(countExperienceUntilNextLevel(toBeUpdated.getExperience(), level));
        }
        if((Long) updatedPlayer.getId() != null)
        {
            toBeUpdated.setId(originalId);
        }
        if(updatedPlayer.getLevel() != null || updatedPlayer.getUntilNextLevel() != null) {
            Integer level = countPlayersLevel(toBeUpdated.getExperience());
            toBeUpdated.setLevel(level);
            toBeUpdated.setUntilNextLevel(countExperienceUntilNextLevel(toBeUpdated.getExperience(), level));
        }

        return playersRepository.save(toBeUpdated);
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
