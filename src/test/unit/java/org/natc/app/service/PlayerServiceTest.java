package org.natc.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.FullName;
import org.natc.app.entity.domain.Player;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.PlayerRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private LeagueConfiguration leagueConfiguration;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private NameService nameService;

    @InjectMocks
    private PlayerService playerService;

    @Nested
    class GeneratePlayers {

        @BeforeEach
        public void setup() throws NATCException {
            when(nameService.generateName()).thenReturn(FullName.builder().build());
        }

        @Test
        public void shouldReturnAListOfPlayersGenerated() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers(null, 1);

            assertFalse(playerList.isEmpty());
        }

        @Test
        public void shouldCreateTheGivenNumberOfPlayers() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers(null, 10);

            assertEquals(10, playerList.size());
        }

        @Test
        public void shouldCreatePlayersForTheGivenYear() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 10);

            final Set<String> years = playerList.stream().map(Player::getYear).collect(Collectors.toSet());

            assertEquals(1, years.size());
            assertEquals("1995", years.stream().findFirst().orElse(null));
        }

        @Test
        public void shouldCallPlayerRepositoryToGetTheLastPlayerId() throws NATCException {
            playerService.generatePlayers("2001", 10);

            verify(playerRepository).findMaxPlayerId();
        }

        @Test
        public void shouldSetThePlayerIdToTheNumberAfterTheMaxValueReturnedByRepository() throws NATCException {
            when(playerRepository.findMaxPlayerId()).thenReturn(Optional.of(123));

            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            assertEquals(1, playerList.size());
            assertEquals(124, playerList.get(0).getPlayerId());
        }

        @Test
        public void shouldSetThePlayerIdToOneIfNoValueIsFoundInTheRepository() throws NATCException {
            when(playerRepository.findMaxPlayerId()).thenReturn(Optional.empty());

            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            assertEquals(1, playerList.size());
            assertEquals(1, playerList.get(0).getPlayerId());
        }

        @Test
        public void shouldOnlyGetTheMaxPlayerIdOnceEvenWhenGeneratingMultiplePlayers() throws NATCException {
            playerService.generatePlayers("2001", 10);

            verify(playerRepository, atMostOnce()).findMaxPlayerId();
        }

        @Test
        public void shouldGenerateEveryPlayerWithAPlayerId() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 10);

            assertEquals(10, playerList.size());
            assertEquals(0, playerList.stream().filter(player -> player.getPlayerId() == null).count());
        }

        @Test
        public void shouldGeneratePlayersWithUniquePlayerIdsIncrementingFromMaxValueInRepository() throws NATCException {
            when(playerRepository.findMaxPlayerId()).thenReturn(Optional.of(123));

            final List<Player> playerList = playerService.generatePlayers("1995", 5);

            final List<Integer> expectedPlayerIds = Arrays.asList(124, 125, 126, 127, 128);

            assertEquals(5, playerList.size());
            assertEquals(expectedPlayerIds, playerList.stream().map(Player::getPlayerId).collect(Collectors.toList()));
        }

        @Test
        public void shouldCallNameServiceToGenerateANameForThePlayer() throws NATCException {
            playerService.generatePlayers("1995", 1);

            verify(nameService).generateName();
        }

        @Test
        public void shouldGenerateANameForEveryPlayer() throws NATCException {
            playerService.generatePlayers("1995", 25);

            verify(nameService, times(25)).generateName();
        }

        @Test
        public void shouldSetTheNamesFromNameServiceOnTheGeneratedPlayers() throws NATCException {
            reset(nameService);

            when(nameService.generateName())
                    .thenReturn(FullName.builder().firstName("James").lastName("Smith").build())
                    .thenReturn(FullName.builder().firstName("John").lastName("Johnson").build())
                    .thenReturn(FullName.builder().firstName("Robert").lastName("Williams").build());

            final List<Player> playerList = playerService.generatePlayers("1995", 3);

            assertEquals(3, playerList.size());

            assertEquals("James", playerList.get(0).getFirstName());
            assertEquals("Smith", playerList.get(0).getLastName());

            assertEquals("John", playerList.get(1).getFirstName());
            assertEquals("Johnson", playerList.get(1).getLastName());

            assertEquals("Robert", playerList.get(2).getFirstName());
            assertEquals("Williams", playerList.get(2).getLastName());
        }

        @Test
        public void shouldGeneratePlayersBetweenEighteenAndThirtyYearsOld() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 100);

            assertEquals(100, playerList.size());
            assertEquals(100, playerList.stream().filter(player -> player.getAge() >= 18 && player.getAge() <= 30).count());
        }

        @Test
        public void shouldGeneratePlayersWithARandomAge() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 100);

            assertEquals(100, playerList.size());

            final Set<Integer> ages = playerList.stream().map(Player::getAge).collect(Collectors.toSet());

            assertEquals(12, ages.size());
        }

        @Test
        public void shouldSetPlayerOffenseRatingsToRandomValueBetweenZeroAndOne() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            assertEquals(1, playerList.size());

            final Player player = playerList.get(0);

            assertTrue(player.getScoring() >= 0.0 && player.getScoring() <= 1.0);
            assertTrue(player.getPassing() >= 0.0 && player.getPassing() <= 1.0);
            assertTrue(player.getBlocking() >= 0.0 && player.getBlocking() <= 1.0);
        }

        @Test
        public void shouldSetPlayerDefenseRatingsToRandomValueBetweenZeroAndOne() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            assertEquals(1, playerList.size());

            final Player player = playerList.get(0);

            assertTrue(player.getTackling() >= 0.0 && player.getTackling() <= 1.0);
            assertTrue(player.getStealing() >= 0.0 && player.getStealing() <= 1.0);
            assertTrue(player.getPresence() >= 0.0 && player.getPresence() <= 1.0);
        }

        @Test
        public void shouldSetPlayerPenaltyRatingsToRandomValueBetweenZeroAndOne() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            assertEquals(1, playerList.size());

            final Player player = playerList.get(0);

            assertTrue(player.getDiscipline() >= 0.0 && player.getDiscipline() <= 1.0);
            assertTrue(player.getPenaltyShot() >= 0.0 && player.getPenaltyShot() <= 1.0);
            assertTrue(player.getPenaltyOffense() >= 0.0 && player.getPenaltyOffense() <= 1.0);
            assertTrue(player.getPenaltyDefense() >= 0.0 && player.getPenaltyDefense() <= 1.0);
        }

        @Test
        public void shouldSetPlayerMiscellaneousRatingsToRandomValueBetweenZeroAndOne() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            assertEquals(1, playerList.size());

            final Player player = playerList.get(0);

            assertTrue(player.getEndurance() >= 0.0 && player.getEndurance() <= 1.0);
            assertTrue(player.getConfidence() >= 0.0 && player.getConfidence() <= 1.0);
            assertTrue(player.getVitality() >= 0.0 && player.getVitality() <= 1.0);
            assertTrue(player.getDurability() >= 0.0 && player.getDurability() <= 1.0);
        }

        @Test
        public void shouldInitializeAllPlayerFlagsToFalse() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            assertEquals(1, playerList.size());

            final Player player = playerList.get(0);

            assertEquals(0, player.getRookie());
            assertEquals(0, player.getInjured());
            assertEquals(0, player.getFreeAgent());
            assertEquals(0, player.getSigned());
            assertEquals(0, player.getReleased());
            assertEquals(0, player.getRetired());
            assertEquals(0, player.getAllstarAlternate());
        }

        @Test
        public void shouldInitializeSeasonsPlayedCounterToZero() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            assertEquals(1, playerList.size());

            final Player player = playerList.get(0);

            assertEquals(0, player.getSeasonsPlayed());
        }

        @Test
        public void shouldCallPlayerRepositoryToPersistGeneratedPlayer() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            verify(playerRepository).save(any(Player.class));
        }

        @Test
        public void shouldCallPlayerRepositoryWithGeneratedPlayer() throws NATCException {
            final ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);

            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            verify(playerRepository).save(captor.capture());

            assertEquals(1, playerList.size());

            final Player expectedPlayer = playerList.get(0);
            final Player actualPlayer = captor.getValue();

            assertEquals(expectedPlayer, actualPlayer);
        }

        @Test
        public void shouldCallPlayerRepositoryForEveryPlayerGenerated() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 10);

            verify(playerRepository, times(10)).save(any(Player.class));
        }
    }

    @Nested
    class UpdatePlayer {

        @Test
        public void shouldCallThePlayerRepositoryToSaveTheGivenPlayer() {
            final Player player = Player.builder().playerId(555).build();

            playerService.updatePlayer(player);

            verify(playerRepository).save(player);
        }
    }

    @Nested
    class UpdatePlayersForNewSeason {

        @Test
        public void shouldCallTheRepositoryToCopyPlayersForNewYear() {
            playerService.updatePlayersForNewSeason(null, null);

            verify(playerRepository).copyPlayersForNewYear(any(), any());
        }

        @Test
        public void shouldCallPassThePreviousYearAndNewYearToTheRepository() {
            playerService.updatePlayersForNewSeason("2004", "2005");

            verify(playerRepository).copyPlayersForNewYear("2004", "2005");
        }
    }

    @Nested
    class GetManagerialCandidates {

        @Test
        public void shouldReturnAListOfPlayers() {
            final List<Player> playerList = playerService.getManagerialCandidates("2020");

            assertNotNull(playerList);
        }

        @Test
        public void shouldCallThePlayerRepositoryWithTheGivenYearToFindTheCandidates() {
            playerService.getManagerialCandidates("2007");

            verify(playerRepository).findTopNumRetiredPlayersForYear(eq("2007"), any());
        }

        @Test
        public void shouldCallTheLeagueConfigurationToGetTheMaxNumberOfPlayerManagersPerSeason() {
            playerService.getManagerialCandidates("2007");

            verify(leagueConfiguration).getMaxPlayerManagersPerSeason();
        }

        @Test
        public void shouldCallThePlayerRepositoryWithTheConfiguredMaxNumberOfPlayerManagersPerSeason() {
            when(leagueConfiguration.getMaxPlayerManagersPerSeason()).thenReturn(5);

            playerService.getManagerialCandidates("2007");

            verify(playerRepository).findTopNumRetiredPlayersForYear(eq("2007"), any());
        }

        @Test
        public void shouldReturnTheResponseFromTheRepository() {
            final List<Player> playerList = Arrays.asList(
                    Player.builder().build(),
                    Player.builder().build(),
                    Player.builder().build()
            );

            when(playerRepository.findTopNumRetiredPlayersForYear(anyString(), any())).thenReturn(playerList);

            assertEquals(playerList, playerService.getManagerialCandidates("2012"));
        }
    }
}