package org.natc.app.service;

import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.FullName;
import org.natc.app.entity.domain.Player;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerService {

    private final LeagueConfiguration leagueConfiguration;
    private final PlayerRepository playerRepository;
    private final NameService nameService;

    @Autowired
    public PlayerService(final LeagueConfiguration leagueConfiguration, final PlayerRepository playerRepository, final NameService nameService) {
        this.leagueConfiguration = leagueConfiguration;
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

    public void updatePlayersForNewSeason(final String previousYear, final String newYear) {
        playerRepository.copyPlayersForNewYear(previousYear, newYear);
    }

    public List<Player> getManagerialCandidates(final String year) {
        return playerRepository.findTopNumRetiredPlayersForYear(year, leagueConfiguration.getMaxPlayerManagersPerSeason());
    }

    public List<Player> getActivePlayersForYear(String year) {
        return playerRepository.findAll(Example.of(Player.builder().year(year).retired(0).build()));
    }
}
