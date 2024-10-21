package org.natc.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.Player;
import org.natc.app.entity.domain.PlayerAward;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.PlayerRepository;
import org.natc.app.util.TestHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PlayerServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private TestHelpers testHelpers;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    @Nested
    class GeneratePlayers {

        @BeforeEach
        void setup() {
            testHelpers.seedFirstAndLastNames();
        }

        @Test
        void shouldCreateTheGivenNumberOfPlayersInTheDatabase() throws NATCException {
            playerService.generatePlayers("2222", 100);

            final List<Player> players = playerRepository.findAll();

            assertEquals(100, players.size());
        }

        @Test
        void shouldCreateAllPlayersForTheGivenYear() throws NATCException {
            playerService.generatePlayers("2222", 100);

            final List<Player> players = playerRepository.findAll();

            assertEquals(100, players.stream().filter(player -> player.getYear().equals("2222")).count());
        }

        @Test
        void shouldCreateEveryPlayerWithAUniqueName() throws NATCException {
            playerService.generatePlayers("2222", 1000);

            final List<Player> players = playerRepository.findAll();

            assertEquals(1000, players.stream().map(player -> player.getFirstName() + player.getLastName()).distinct().count());
        }

        @Test
        void shouldCreateEveryPlayerWithAUniquePlayerId() throws NATCException {
            playerService.generatePlayers("2222", 100);

            final List<Player> players = playerRepository.findAll();

            assertEquals(100, players.stream().map(Player::getPlayerId).distinct().count());
        }

        @Test
        void shouldCreateEveryPlayerWithAnAgeBetweenEighteenAndThirty() throws NATCException {
            playerService.generatePlayers("2222", 100);

            final List<Player> players = playerRepository.findAll();

            assertEquals(100, players.stream().filter(player -> player.getAge() >= 18 && player.getAge() <= 30).count());
        }

        @Test
        void shouldCreateEveryPlayerWithRatingsBetweenZeroAndOne() throws NATCException {
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
        void shouldCreateEveryPlayerWithAllPlayerLifecycleFlagsAsZero() throws NATCException {
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
        void shouldCreateEveryPlayerWithSeasonsPlayedAsZero() throws NATCException {
            playerService.generatePlayers("2222", 100);

            final List<Player> players = playerRepository.findAll();

            assertEquals(100, players.stream().filter(player -> player.getSeasonsPlayed() == 0).count());
        }

        @Test
        void shouldContinueToIncrementPlayerIdsOnSubsequentCalls() throws NATCException {
            playerService.generatePlayers("2222", 5);
            playerService.generatePlayers("2222", 5);
            playerService.generatePlayers("2222", 5);
            playerService.generatePlayers("2222", 5);
            playerService.generatePlayers("2222", 5);

            final List<Player> players = playerRepository.findAll();

            assertEquals(25, players.size());
            assertEquals(25, players.stream().filter(player -> player.getPlayerId() >= 1 && player.getPlayerId() <= 25).count());
        }
    }

    @Nested
    class UpdatePlayer {

        @Test
        void shouldModifyAnExistingPlayerRecordInTheDatabase() {
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

    @Nested
    class UpdatePlayersForNewSeason {

        @Test
        void shouldCopyPlayerRecordsFromOneYearToAnother() {
            final List<Player> originalPlayers = List.of(
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
        void shouldOnlyCopyPlayerRecordsFromPreviousYear() {
            final List<Player> originalPlayers = List.of(
                    Player.builder().playerId(1).year("2015").build(),
                    Player.builder().playerId(2).year("2016").build(),
                    Player.builder().playerId(3).year("2017").build()
            );

            playerRepository.saveAll(originalPlayers);

            playerService.updatePlayersForNewSeason("2015", "2018");

            final Example<Player> queryCriteria = Example.of(Player.builder().year("2018").build());
            final List<Player> copiedPlayers = playerRepository.findAll(queryCriteria);

            assertEquals(1, copiedPlayers.size());
            assertEquals(1, copiedPlayers.getFirst().getPlayerId());
        }

        @Test
        void shouldOnlyCopyNecessaryFieldsToNewYear() {
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

            final Player copiedPlayer = copiedPlayers.getFirst();

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

    @Nested
    class GetActivePlayersForYear {

        @Test
        void ShouldReturnAllActivePlayerRecordsMatchingTheGivenYear() {
            final List<Player> persistedPlayers = List.of(
                    Player.builder().playerId(1).year("2015").retired(0).build(),
                    Player.builder().playerId(2).year("2015").retired(0).build(),
                    Player.builder().playerId(3).year("2015").retired(0).build()
            );

            playerRepository.saveAll(persistedPlayers);

            final List<Player> foundPlayers = playerService.getActivePlayersForYear("2015");

            assertEquals(persistedPlayers.size(), foundPlayers.size());
        }

        @Test
        void ShouldReturnOnlyActivePlayerRecordsMatchingTheGivenYear() {
            final List<Player> persistedPlayers = List.of(
                    Player.builder().playerId(1).year("2015").retired(0).build(),
                    Player.builder().playerId(2).year("2016").retired(0).build(),
                    Player.builder().playerId(3).year("2015").retired(0).build(),
                    Player.builder().playerId(4).year("2015").retired(1).build(),
                    Player.builder().playerId(5).year("2015").retired(0).build()
            );

            playerRepository.saveAll(persistedPlayers);

            final List<Player> foundPlayers = playerService.getActivePlayersForYear("2015");

            assertEquals(3, foundPlayers.size());
            assertEquals(3, foundPlayers.stream().filter(player ->
                    player.getYear().equals("2015") && player.getRetired().equals(0)
            ).count());
        }
    }

    @Nested
    class GetManagerialCandidates {

        @Test
        void shouldReturnAnEmptyListIfThereAreNoPlayers() {
            assertEquals(0, playerService.getManagerialCandidates("2005").size());
        }

        @Test
        void shouldReturnOnePlayerIfOnlyOnePlayerExists() {
            final Player expectedPlayer = Player.builder().playerId(1).year("2005").retired(1).build();

            playerRepository.save(expectedPlayer);

            final List<Player> playerList = playerService.getManagerialCandidates("2005");

            assertEquals(1, playerList.size());

            final Player actualPlayer = playerList.getFirst();

            assertEquals(expectedPlayer.getPlayerId(), actualPlayer.getPlayerId());
        }

        @Test
        void shouldReturnAtMostTwoPlayers() {
            final List<Player> playerList = List.of(
                    Player.builder().playerId(1).year("1997").retired(1).build(),
                    Player.builder().playerId(2).year("1997").retired(1).build(),
                    Player.builder().playerId(3).year("1997").retired(1).build(),
                    Player.builder().playerId(4).year("1997").retired(1).build(),
                    Player.builder().playerId(5).year("1997").retired(1).build()
            );

            playerRepository.saveAll(playerList);

            assertEquals(2, playerService.getManagerialCandidates("1997").size());
        }

        @Test
        void shouldOnlyConsiderRetiredPlayers() {
            final List<Player> playerList = List.of(
                    Player.builder().playerId(1).year("1997").retired(0).build(),
                    Player.builder().playerId(2).year("1997").retired(0).build(),
                    Player.builder().playerId(3).year("1997").retired(1).build(),
                    Player.builder().playerId(4).year("1997").retired(0).build(),
                    Player.builder().playerId(5).year("1997").retired(0).build()
            );

            playerRepository.saveAll(playerList);

            final List<Player> actualPlayers = playerService.getManagerialCandidates("1997");

            assertEquals(1, actualPlayers.size());

            final Player actualPlayer = actualPlayers.getFirst();

            assertEquals(3, actualPlayer.getPlayerId());
        }

        @Test
        void shouldOnlyConsiderPlayersForTheGivenYear() {
            final List<Player> playerList = List.of(
                    Player.builder().playerId(1).year("1995").retired(1).build(),
                    Player.builder().playerId(2).year("1996").retired(1).build(),
                    Player.builder().playerId(3).year("1997").retired(1).build(),
                    Player.builder().playerId(4).year("1998").retired(1).build(),
                    Player.builder().playerId(5).year("1999").retired(1).build()
            );

            playerRepository.saveAll(playerList);

            final List<Player> actualPlayers = playerService.getManagerialCandidates("1998");

            assertEquals(1, actualPlayers.size());

            final Player actualPlayer = actualPlayers.getFirst();

            assertEquals(4, actualPlayer.getPlayerId());
        }
        
        @Test
        void shouldReturnTheTwoHighestRatedPlayers() {
            final List<Player> playerList = List.of(
                    Player.builder().playerId(1).year("1998").retired(1).scoring(0.2).passing(0.2).blocking(0.2).tackling(0.2).stealing(0.2).presence(0.2).discipline(0.2).endurance(0.2).penaltyShot(0.2).penaltyOffense(0.2).penaltyDefense(0.2).build(),
                    Player.builder().playerId(2).year("1998").retired(1).scoring(0.8).passing(0.8).blocking(0.8).tackling(0.8).stealing(0.8).presence(0.8).discipline(0.8).endurance(0.8).penaltyShot(0.8).penaltyOffense(0.8).penaltyDefense(0.8).build(),
                    Player.builder().playerId(3).year("1998").retired(1).scoring(0.6).passing(0.6).blocking(0.6).tackling(0.6).stealing(0.6).presence(0.6).discipline(0.6).endurance(0.6).penaltyShot(0.6).penaltyOffense(0.6).penaltyDefense(0.6).build(),
                    Player.builder().playerId(4).year("1998").retired(1).scoring(0.4).passing(0.4).blocking(0.4).tackling(0.4).stealing(0.4).presence(0.4).discipline(0.4).endurance(0.4).penaltyShot(0.4).penaltyOffense(0.4).penaltyDefense(0.4).build(),
                    Player.builder().playerId(5).year("1998").retired(1).scoring(1.0).passing(1.0).blocking(1.0).tackling(1.0).stealing(1.0).presence(1.0).discipline(1.0).endurance(1.0).penaltyShot(1.0).penaltyOffense(1.0).penaltyDefense(1.0).build()
            );

            playerRepository.saveAll(playerList);

            final List<Player> actualPlayers = playerService.getManagerialCandidates("1998");

            assertEquals(2, actualPlayers.size());

            assertEquals(5, actualPlayers.get(0).getPlayerId());
            assertEquals(2, actualPlayers.get(1).getPlayerId());
        }
    }

    @Nested
    class GetUndraftedRookiesForYear {
        @Test
        void ShouldReturnAllRookiePlayerRecordsMatchingTheGivenYear() {
            final List<Player> persistedPlayers = List.of(
                    Player.builder().playerId(1).year("2015").rookie(1).build(),
                    Player.builder().playerId(2).year("2015").rookie(1).build(),
                    Player.builder().playerId(3).year("2015").rookie(1).build()
            );

            playerRepository.saveAll(persistedPlayers);

            final List<Player> foundPlayers = playerService.getUndraftedRookiesForYear("2015");

            assertEquals(persistedPlayers.size(), foundPlayers.size());
        }

        @Test
        void ShouldOnlyReturnRookiePlayerRecordsForGivenYearNotOnATeam() {
            final List<Player> persistedPlayers = List.of(
                    Player.builder().playerId(1).year("2015").rookie(1).build(),
                    Player.builder().playerId(2).teamId(1).year("2015").rookie(1).build(),
                    Player.builder().playerId(3).year("2015").rookie(1).build(),
                    Player.builder().playerId(4).teamId(2).year("2015").rookie(1).build(),
                    Player.builder().playerId(5).year("2015").rookie(1).build()
            );

            playerRepository.saveAll(persistedPlayers);

            final List<Player> foundPlayers = playerService.getUndraftedRookiesForYear("2015");

            assertEquals(3, foundPlayers.size());
            assertEquals(3, foundPlayers.stream().filter(player ->
                    player.getYear().equals("2015") && player.getRookie().equals(1) && Objects.isNull(player.getTeamId())
            ).count());
        }

        @Test
        void ShouldOnlyReturnUndraftedRookiePlayerRecordsForGivenYear() {
            final List<Player> persistedPlayers = List.of(
                    Player.builder().playerId(1).year("2015").rookie(1).build(),
                    Player.builder().playerId(2).year("2014").rookie(1).build(),
                    Player.builder().playerId(3).year("2015").rookie(1).build(),
                    Player.builder().playerId(4).year("2016").rookie(1).build(),
                    Player.builder().playerId(5).year("2015").rookie(1).build()
            );

            playerRepository.saveAll(persistedPlayers);

            final List<Player> foundPlayers = playerService.getUndraftedRookiesForYear("2015");

            assertEquals(3, foundPlayers.size());
            assertEquals(3, foundPlayers.stream().filter(player ->
                    player.getYear().equals("2015") && player.getRookie().equals(1) && Objects.isNull(player.getTeamId())
            ).count());
        }

        @Test
        void ShouldOnlyReturnUndraftedPlayerRecordsForGivenYearThatAreRookies() {
            final List<Player> persistedPlayers = List.of(
                    Player.builder().playerId(1).year("2015").rookie(1).build(),
                    Player.builder().playerId(2).year("2015").rookie(0).build(),
                    Player.builder().playerId(3).year("2015").rookie(1).build(),
                    Player.builder().playerId(4).year("2015").build(),
                    Player.builder().playerId(5).year("2015").rookie(1).build()
            );

            playerRepository.saveAll(persistedPlayers);

            final List<Player> foundPlayers = playerService.getUndraftedRookiesForYear("2015");

            assertEquals(3, foundPlayers.size());
            assertEquals(3, foundPlayers.stream().filter(player ->
                    player.getYear().equals("2015") && player.getRookie().equals(1) && Objects.isNull(player.getTeamId())
            ).count());
        }
    }

    @Nested
    class AgePlayers {
        @Test
        void shouldIncreaseEveryPlayersAgeByOneForGivenYear() {
            final List<Player> persistedPlayers = List.of(
                    Player.builder().playerId(1).year("2015").age(21).build(),
                    Player.builder().playerId(2).year("2015").age(36).build(),
                    Player.builder().playerId(3).year("2015").age(24).build(),
                    Player.builder().playerId(4).year("2015").age(41).build(),
                    Player.builder().playerId(5).year("2015").age(30).build()
            );

            playerRepository.saveAll(persistedPlayers);

            playerService.agePlayers("2015");

            final List<Player> updatedPlayers = playerRepository.findAll();

            assertEquals(persistedPlayers.size(), updatedPlayers.size());

            for (final Player updatedPlayer : updatedPlayers) {
                final Player originalPlayer = persistedPlayers.stream()
                        .filter(p -> p.getPlayerId().equals(updatedPlayer.getPlayerId()))
                        .findFirst()
                        .orElseThrow();

                assertEquals(1, updatedPlayer.getAge() - originalPlayer.getAge());
            }
        }

        @Test
        void shouldNotChangePlayersAgeForDifferentYear() {
            final List<Player> persistedPlayers = List.of(
                    Player.builder().playerId(1).year("2012").age(21).build(),
                    Player.builder().playerId(2).year("2013").age(36).build(),
                    Player.builder().playerId(3).year("2014").age(24).build(),
                    Player.builder().playerId(4).year("2016").age(41).build(),
                    Player.builder().playerId(5).year("2017").age(30).build()
            );

            playerRepository.saveAll(persistedPlayers);

            playerService.agePlayers("2015");

            final List<Player> updatedPlayers = playerRepository.findAll();

            assertEquals(persistedPlayers.size(), updatedPlayers.size());

            for (final Player updatedPlayer : updatedPlayers) {
                final Player originalPlayer = persistedPlayers.stream()
                        .filter(p -> p.getPlayerId().equals(updatedPlayer.getPlayerId()))
                        .findFirst()
                        .orElseThrow();

                assertEquals(originalPlayer.getAge(), updatedPlayer.getAge());
            }
        }
    }
}