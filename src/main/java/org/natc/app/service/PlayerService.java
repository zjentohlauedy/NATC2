package org.natc.app.service;

import org.natc.app.entity.domain.FullName;
import org.natc.app.entity.domain.Player;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final NameService nameService;

    @Autowired
    public PlayerService(final PlayerRepository playerRepository, final NameService nameService) {
        this.playerRepository = playerRepository;
        this.nameService = nameService;
    }

    public List<Player> generatePlayers(final String year, final Integer count) throws NATCException {
        final List<Player> players = new ArrayList<>();

        int lastPlayerId = playerRepository.findMaxPlayerId().orElse(0);

        for (int i = 0; i < count; i++) {
            final FullName fullName = nameService.generateName();
            final Player player = Player.generate(++lastPlayerId, year, fullName.getFirstName(), fullName.getLastName());

            playerRepository.save(player);

            players.add(player);
        }

        return players;
    }

    public void updatePlayer(final Player player) {
        playerRepository.save(player);
    }
}
