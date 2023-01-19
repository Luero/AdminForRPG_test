package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlayersService {

    private final PlayersRepository playersRepository;
    @Autowired
    public PlayersService(PlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }

    public List<Player> findAll() {
        return playersRepository.findAll();
    }


}
