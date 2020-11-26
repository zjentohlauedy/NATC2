package org.natc.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.Player;
import org.natc.app.entity.domain.PlayerAward;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.PlayerRepository;
import org.natc.app.util.TestHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    
    @Test
    public void updatePlayersForNewSeason_ShouldCopyPlayerRecordsFromOneYearToAnother() {
        final List<Player> originalPlayers = Arrays.asList(
                Player.builder().playerId(1).year("2015").build(),
                Player.builder().playerId(2).year("2015").build(),
                Player.builder().playerId(3).year("2015").build()
        );

        playerRepository.saveAll(originalPlayers);

        playerService.updatePlayersForNewSeason("2015", "2017");

        final Example<Player> queryCriteria = Example.of(Player.builder().year("2017").build());
        final List<Player> copiedPlayers = playerRepository.findAll(queryCriteria);

        assertEquals(originalPlayers.size(), copiedPlayers.size());
    }
    
    @Test
    public void updatePlayersForNewSeason_ShouldOnlyCopyPlayerRecordsFromPreviousYear() {
        final List<Player> originalPlayers = Arrays.asList(
                Player.builder().playerId(1).year("2015").build(),
                Player.builder().playerId(2).year("2016").build(),
                Player.builder().playerId(3).year("2017").build()
        );

        playerRepository.saveAll(originalPlayers);

        playerService.updatePlayersForNewSeason("2015", "2018");

        final Example<Player> queryCriteria = Example.of(Player.builder().year("2018").build());
        final List<Player> copiedPlayers = playerRepository.findAll(queryCriteria);

        assertEquals(1, copiedPlayers.size());
        assertEquals(1, copiedPlayers.get(0).getPlayerId());
    }
    
    @Test
    public void updatePlayersForNewSeason_ShouldOnlyCopyNecessaryFieldsToNewYear() {
        final Player originalPlayer = Player.builder()
                .playerId(123)
                .teamId(321)
                .year("1991")
                .firstName("John")
                .lastName("Doe")
                .age(26)
                .scoring(0.101)
                .passing(0.202)
                .blocking(0.303)
                .tackling(0.404)
                .stealing(0.505)
                .presence(0.606)
                .discipline(0.707)
                .penaltyShot(0.808)
                .penaltyOffense(0.909)
                .penaltyDefense(1.010)
                .endurance(1.111)
                .confidence(1.212)
                .vitality(1.313)
                .durability(1.414)
                .rookie(1)
                .injured(0)
                .returnDate(LocalDate.now())
                .freeAgent(1)
                .signed(0)
                .released(1)
                .retired(0)
                .formerTeamId(111)
                .allstarAlternate(222)
                .award(PlayerAward.GOLD.getValue())
                .draftPick(444)
                .seasonsPlayed(555)
                .allstarTeamId(1)
                .build();

        playerRepository.save(originalPlayer);

        playerService.updatePlayersForNewSeason("1991", "1996");

        final Example<Player> queryCriteria = Example.of(Player.builder().year("1996").build());
        final List<Player> copiedPlayers = playerRepository.findAll(queryCriteria);

        assertEquals(1, copiedPlayers.size());

        final Player copiedPlayer = copiedPlayers.get(0);

        assertEquals(originalPlayer.getPlayerId(), copiedPlayer.getPlayerId());
        assertEquals(originalPlayer.getTeamId(), copiedPlayer.getTeamId());
        assertEquals(originalPlayer.getFirstName(), copiedPlayer.getFirstName());
        assertEquals(originalPlayer.getLastName(), copiedPlayer.getLastName());
        assertEquals(originalPlayer.getAge(), copiedPlayer.getAge());
        assertEquals(originalPlayer.getScoring(), copiedPlayer.getScoring());
        assertEquals(originalPlayer.getPassing(), copiedPlayer.getPassing());
        assertEquals(originalPlayer.getBlocking(), copiedPlayer.getBlocking());
        assertEquals(originalPlayer.getTackling(), copiedPlayer.getTackling());
        assertEquals(originalPlayer.getStealing(), copiedPlayer.getStealing());
        assertEquals(originalPlayer.getPresence(), copiedPlayer.getPresence());
        assertEquals(originalPlayer.getDiscipline(), copiedPlayer.getDiscipline());
        assertEquals(originalPlayer.getPenaltyShot(), copiedPlayer.getPenaltyShot());
        assertEquals(originalPlayer.getPenaltyOffense(), copiedPlayer.getPenaltyOffense());
        assertEquals(originalPlayer.getPenaltyDefense(), copiedPlayer.getPenaltyDefense());
        assertEquals(originalPlayer.getEndurance(), copiedPlayer.getEndurance());
        assertEquals(originalPlayer.getConfidence(), copiedPlayer.getConfidence());
        assertEquals(originalPlayer.getVitality(), copiedPlayer.getVitality());
        assertEquals(originalPlayer.getDurability(), copiedPlayer.getDurability());
        assertEquals(originalPlayer.getRookie(), copiedPlayer.getRookie());
        assertEquals(originalPlayer.getRetired(), copiedPlayer.getRetired());
        assertEquals(originalPlayer.getSeasonsPlayed(), copiedPlayer.getSeasonsPlayed());

        assertNull(copiedPlayer.getInjured());
        assertNull(copiedPlayer.getReturnDate());
        assertNull(copiedPlayer.getFreeAgent());
        assertNull(copiedPlayer.getSigned());
        assertNull(copiedPlayer.getReleased());
        assertNull(copiedPlayer.getFormerTeamId());
        assertNull(copiedPlayer.getAllstarAlternate());
        assertNull(copiedPlayer.getAllstarTeamId());
        assertNull(copiedPlayer.getAward());
        assertNull(copiedPlayer.getDraftPick());
    }
}