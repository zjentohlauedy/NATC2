package org.natc.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.Player;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.PlayerRepository;
import org.natc.app.util.TestHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlayerServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private TestHelpers testHelpers;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    @BeforeEach
    public void setup() {
        testHelpers.seedFirstAndLastNames();
    }

    @Test
    public void generatePlayers_ShouldCreateTheGivenNumberOfPlayersInTheDatabase() throws NATCException {
        playerService.generatePlayers("2222", 100);

        final List<Player> players = playerRepository.findAll();

        assertEquals(100, players.size());
    }
    
    @Test
    public void generatePlayers_ShouldCreateAllPlayersForTheGivenYear() throws NATCException {
        playerService.generatePlayers("2222", 100);

        final List<Player> players = playerRepository.findAll();

        assertEquals(100, players.stream().filter(player -> player.getYear().equals("2222")).count());
    }
    
    @Test
    public void generatePlayers_ShouldCreateEveryPlayerWithAUniqueName() throws NATCException {
        playerService.generatePlayers("2222", 1000);

        final List<Player> players = playerRepository.findAll();

        final Set<String> names = players.stream().map(player -> player.getFirstName() + player.getLastName()).collect(Collectors.toSet());

        assertEquals(1000, names.size());
    }
    
    @Test
    public void generatePlayers_ShouldCreateEveryPlayerWithAUniquePlayerId() throws NATCException {
        playerService.generatePlayers("2222", 100);

        final List<Player> players = playerRepository.findAll();

        final Set<Integer> playerIds = players.stream().map(Player::getPlayerId).collect(Collectors.toSet());

        assertEquals(100, playerIds.size());
    }
    
    @Test
    public void generatePlayers_ShouldCreateEveryPlayerWithAnAgeBetweenEighteenAndThirty() throws NATCException {
        playerService.generatePlayers("2222", 100);

        final List<Player> players = playerRepository.findAll();

        assertEquals(100, players.stream().filter(player -> player.getAge() >= 18 && player.getAge() <= 30).count());
    }
    
    @Test
    public void generatePlayers_ShouldCreateEveryPlayerWithRatingsBetweenZeroAndOne() throws NATCException {
        playerService.generatePlayers("2222", 100);

        final List<Player> players = playerRepository.findAll();

        assertEquals(100, players.stream().filter(player -> player.getScoring() >= 0.0 && player.getScoring() <= 1.0).count());
        assertEquals(100, players.stream().filter(player -> player.getPassing() >= 0.0 && player.getPassing() <= 1.0).count());
        assertEquals(100, players.stream().filter(player -> player.getBlocking() >= 0.0 && player.getBlocking() <= 1.0).count());
        assertEquals(100, players.stream().filter(player -> player.getTackling() >= 0.0 && player.getTackling() <= 1.0).count());
        assertEquals(100, players.stream().filter(player -> player.getStealing() >= 0.0 && player.getStealing() <= 1.0).count());
        assertEquals(100, players.stream().filter(player -> player.getPresence() >= 0.0 && player.getPresence() <= 1.0).count());
        assertEquals(100, players.stream().filter(player -> player.getDiscipline() >= 0.0 && player.getDiscipline() <= 1.0).count());
        assertEquals(100, players.stream().filter(player -> player.getPenaltyShot() >= 0.0 && player.getPenaltyShot() <= 1.0).count());
        assertEquals(100, players.stream().filter(player -> player.getPenaltyOffense() >= 0.0 && player.getPenaltyOffense() <= 1.0).count());
        assertEquals(100, players.stream().filter(player -> player.getPenaltyDefense() >= 0.0 && player.getPenaltyDefense() <= 1.0).count());
        assertEquals(100, players.stream().filter(player -> player.getEndurance() >= 0.0 && player.getEndurance() <= 1.0).count());
        assertEquals(100, players.stream().filter(player -> player.getConfidence() >= 0.0 && player.getConfidence() <= 1.0).count());
        assertEquals(100, players.stream().filter(player -> player.getVitality() >= 0.0 && player.getVitality() <= 1.0).count());
        assertEquals(100, players.stream().filter(player -> player.getDurability() >= 0.0 && player.getDurability() <= 1.0).count());
    }
    
    @Test
    public void generatePlayers_ShouldCreateEveryPlayerWithAllPlayerLifecycleFlagsAsZero() throws NATCException {
        playerService.generatePlayers("2222", 100);

        final List<Player> players = playerRepository.findAll();

        assertEquals(100, players.stream().filter(player -> player.getRookie() == 0).count());
        assertEquals(100, players.stream().filter(player -> player.getInjured() == 0).count());
        assertEquals(100, players.stream().filter(player -> player.getFreeAgent() == 0).count());
        assertEquals(100, players.stream().filter(player -> player.getSigned() == 0).count());
        assertEquals(100, players.stream().filter(player -> player.getReleased() == 0).count());
        assertEquals(100, players.stream().filter(player -> player.getRetired() == 0).count());
        assertEquals(100, players.stream().filter(player -> player.getAllstarAlternate() == 0).count());
    }
    
    @Test
    public void generatePlayers_ShouldCreateEveryPlayerWithSeasonsPlayedAsZero() throws NATCException {
        playerService.generatePlayers("2222", 100);

        final List<Player> players = playerRepository.findAll();

        assertEquals(100, players.stream().filter(player -> player.getSeasonsPlayed() == 0).count());
    }
    
    @Test
    public void generatePlayers_ShouldContinueToIncrementPlayerIdsOnSubsequentCalls() throws NATCException {
        playerService.generatePlayers("2222", 5);
        playerService.generatePlayers("2222", 5);
        playerService.generatePlayers("2222", 5);
        playerService.generatePlayers("2222", 5);
        playerService.generatePlayers("2222", 5);

        final List<Player> players = playerRepository.findAll();

        assertEquals(25, players.size());
        assertEquals(25, players.stream().filter(player -> player.getPlayerId() >= 1 && player.getPlayerId() <= 25).count());
    }
    
    @Test
    public void updatePlayer_ShouldModifyAnExistingPlayerRecordInTheDatabase() {
        final Player originalPlayer = Player.builder().playerId(333).year("1999").build();

        playerRepository.save(originalPlayer);

        final Player updatedPlayer = Player.builder().playerId(333).year("1999").firstName("Todd").lastName("Stevens").build();

        playerService.updatePlayer(updatedPlayer);

        final Player persistedPlayer = playerRepository.findOne(Example.of(originalPlayer)).orElse(null);

        assertNotNull(persistedPlayer);

        assertEquals(updatedPlayer.getPlayerId(), persistedPlayer.getPlayerId());
        assertEquals(updatedPlayer.getYear(), persistedPlayer.getYear());
        assertEquals(updatedPlayer.getFirstName(), persistedPlayer.getFirstName());
        assertEquals(updatedPlayer.getLastName(), persistedPlayer.getLastName());
    }
}