package org.natc.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.FullName;
import org.natc.app.entity.domain.Player;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.PlayerRepository;
import org.springframework.data.domain.Example;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Captor
    private ArgumentCaptor<Example<Player>> captor;

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
        void setup() throws NATCException {
            when(nameService.generateName()).thenReturn(FullName.builder().build());
        }

        @Test
        void shouldReturnAListOfPlayersGenerated() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers(null, 1);

            assertFalse(playerList.isEmpty());
        }

        @Test
        void shouldCreateTheGivenNumberOfPlayers() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers(null, 10);

            assertEquals(10, playerList.size());
        }

        @Test
        void shouldCreatePlayersForTheGivenYear() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 10);

            assertEquals(1, playerList.stream().map(Player::getYear).distinct().count());
            assertEquals("1995", playerList.get(0).getYear());
        }

        @Test
        void shouldCallPlayerRepositoryToGetTheLastPlayerId() throws NATCException {
            playerService.generatePlayers("2001", 10);

            verify(playerRepository).findMaxPlayerId();
        }

        @Test
        void shouldSetThePlayerIdToTheNumberAfterTheMaxValueReturnedByRepository() throws NATCException {
            when(playerRepository.findMaxPlayerId()).thenReturn(Optional.of(123));

            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            assertEquals(1, playerList.size());
            assertEquals(124, playerList.get(0).getPlayerId());
        }

        @Test
        void shouldSetThePlayerIdToOneIfNoValueIsFoundInTheRepository() throws NATCException {
            when(playerRepository.findMaxPlayerId()).thenReturn(Optional.empty());

            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            assertEquals(1, playerList.size());
            assertEquals(1, playerList.get(0).getPlayerId());
        }

        @Test
        void shouldOnlyGetTheMaxPlayerIdOnceEvenWhenGeneratingMultiplePlayers() throws NATCException {
            playerService.generatePlayers("2001", 10);

            verify(playerRepository, atMostOnce()).findMaxPlayerId();
        }

        @Test
        void shouldGenerateEveryPlayerWithAPlayerId() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 10);

            assertEquals(10, playerList.size());
            assertEquals(0, playerList.stream().filter(player -> Objects.isNull(player.getPlayerId())).count());
        }

        @Test
        void shouldGeneratePlayersWithUniquePlayerIdsIncrementingFromMaxValueInRepository() throws NATCException {
            when(playerRepository.findMaxPlayerId()).thenReturn(Optional.of(123));

            final List<Player> playerList = playerService.generatePlayers("1995", 5);

            final List<Integer> expectedPlayerIds = Arrays.asList(124, 125, 126, 127, 128);

            assertEquals(5, playerList.size());
            assertEquals(expectedPlayerIds, playerList.stream().map(Player::getPlayerId).collect(Collectors.toList()));
        }

        @Test
        void shouldCallNameServiceToGenerateANameForThePlayer() throws NATCException {
            playerService.generatePlayers("1995", 1);

            verify(nameService).generateName();
        }

        @Test
        void shouldGenerateANameForEveryPlayer() throws NATCException {
            playerService.generatePlayers("1995", 25);

            verify(nameService, times(25)).generateName();
        }

        @Test
        void shouldSetTheNamesFromNameServiceOnTheGeneratedPlayers() throws NATCException {
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
        void shouldGeneratePlayersBetweenEighteenAndThirtyYearsOld() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 100);

            assertEquals(100, playerList.size());
            assertEquals(100, playerList.stream().filter(player -> player.getAge() >= 18 && player.getAge() <= 30).count());
        }

        @Test
        void shouldGeneratePlayersWithARandomAge() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 100);

            assertEquals(100, playerList.size());

            assertEquals(12, playerList.stream().map(Player::getAge).distinct().count());
        }

        @Test
        void shouldSetPlayerOffenseRatingsToRandomValueBetweenZeroAndOne() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            assertEquals(1, playerList.size());

            final Player player = playerList.get(0);

            assertTrue(player.getScoring() >= 0.0 && player.getScoring() <= 1.0);
            assertTrue(player.getPassing() >= 0.0 && player.getPassing() <= 1.0);
            assertTrue(player.getBlocking() >= 0.0 && player.getBlocking() <= 1.0);
        }

        @Test
        void shouldSetPlayerDefenseRatingsToRandomValueBetweenZeroAndOne() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            assertEquals(1, playerList.size());

            final Player player = playerList.get(0);

            assertTrue(player.getTackling() >= 0.0 && player.getTackling() <= 1.0);
            assertTrue(player.getStealing() >= 0.0 && player.getStealing() <= 1.0);
            assertTrue(player.getPresence() >= 0.0 && player.getPresence() <= 1.0);
        }

        @Test
        void shouldSetPlayerPenaltyRatingsToRandomValueBetweenZeroAndOne() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            assertEquals(1, playerList.size());

            final Player player = playerList.get(0);

            assertTrue(player.getDiscipline() >= 0.0 && player.getDiscipline() <= 1.0);
            assertTrue(player.getPenaltyShot() >= 0.0 && player.getPenaltyShot() <= 1.0);
            assertTrue(player.getPenaltyOffense() >= 0.0 && player.getPenaltyOffense() <= 1.0);
            assertTrue(player.getPenaltyDefense() >= 0.0 && player.getPenaltyDefense() <= 1.0);
        }

        @Test
        void shouldSetPlayerMiscellaneousRatingsToRandomValueBetweenZeroAndOne() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            assertEquals(1, playerList.size());

            final Player player = playerList.get(0);

            assertTrue(player.getEndurance() >= 0.0 && player.getEndurance() <= 1.0);
            assertTrue(player.getConfidence() >= 0.0 && player.getConfidence() <= 1.0);
            assertTrue(player.getVitality() >= 0.0 && player.getVitality() <= 1.0);
            assertTrue(player.getDurability() >= 0.0 && player.getDurability() <= 1.0);
        }

        @Test
        void shouldInitializeAllPlayerFlagsToFalse() throws NATCException {
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
        void shouldInitializeSeasonsPlayedCounterToZero() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            assertEquals(1, playerList.size());

            final Player player = playerList.get(0);

            assertEquals(0, player.getSeasonsPlayed());
        }

        @Test
        void shouldCallPlayerRepositoryToPersistGeneratedPlayer() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            verify(playerRepository).save(any(Player.class));
        }

        @Test
        void shouldCallPlayerRepositoryWithGeneratedPlayer() throws NATCException {
            final ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);

            final List<Player> playerList = playerService.generatePlayers("1995", 1);

            verify(playerRepository).save(captor.capture());

            assertEquals(1, playerList.size());

            final Player expectedPlayer = playerList.get(0);
            final Player actualPlayer = captor.getValue();

            assertEquals(expectedPlayer, actualPlayer);
        }

        @Test
        void shouldCallPlayerRepositoryForEveryPlayerGenerated() throws NATCException {
            final List<Player> playerList = playerService.generatePlayers("1995", 10);

            verify(playerRepository, times(10)).save(any(Player.class));
        }
    }

    @Nested
    class UpdatePlayer {

        @Test
        void shouldCallThePlayerRepositoryToSaveTheGivenPlayer() {
            final Player player = Player.builder().playerId(555).build();

            playerService.updatePlayer(player);

            verify(playerRepository).save(player);
        }
    }

    @Nested
    class UpdatePlayersForNewSeason {

        @Test
        void shouldCallTheRepositoryToCopyPlayersForNewYear() {
            playerService.updatePlayersForNewSeason(null, null);

            verify(playerRepository).copyPlayersForNewYear(any(), any());
        }

        @Test
        void shouldCallPassThePreviousYearAndNewYearToTheRepository() {
            playerService.updatePlayersForNewSeason("2004", "2005");

            verify(playerRepository).copyPlayersForNewYear("2004", "2005");
        }
    }

    @Nested
    class GetActivePlayersForYear {

        @Test
        void shouldCallRepositoryToRetrievePlayersForGivenYearThatAreNotRetired() {
            playerService.getActivePlayersForYear("1999");

            verify(playerRepository).findAll(captor.capture());

            final Player player = captor.getValue().getProbe();

            assertEquals("1999", player.getYear());
            assertEquals(0, player.getRetired());
        }

        @Test
        void shouldReturnThePlayersReturnedByTheRepository() {
            final List<Player> expectedPlayers = Collections.singletonList(Player.builder().playerId(1).build());

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(expectedPlayers);

            final List<Player> actualPlayers = playerService.getActivePlayersForYear("1999");

            assertEquals(expectedPlayers, actualPlayers);
        }
    }

    @Nested
    class GetManagerialCandidates {

        @Test
        void shouldReturnAListOfPlayers() {
            final List<Player> playerList = playerService.getManagerialCandidates("2020");

            assertNotNull(playerList);
        }

        @Test
        void shouldCallThePlayerRepositoryWithTheGivenYearToFindTheCandidates() {
            playerService.getManagerialCandidates("2007");

            verify(playerRepository).findTopNumRetiredPlayersForYear(eq("2007"), any());
        }

        @Test
        void shouldCallTheLeagueConfigurationToGetTheMaxNumberOfPlayerManagersPerSeason() {
            playerService.getManagerialCandidates("2007");

            verify(leagueConfiguration).getMaxPlayerManagersPerSeason();
        }

        @Test
        void shouldCallThePlayerRepositoryWithTheConfiguredMaxNumberOfPlayerManagersPerSeason() {
            when(leagueConfiguration.getMaxPlayerManagersPerSeason()).thenReturn(5);

            playerService.getManagerialCandidates("2007");

            verify(playerRepository).findTopNumRetiredPlayersForYear(eq("2007"), any());
        }

        @Test
        void shouldReturnTheResponseFromTheRepository() {
            final List<Player> playerList = Arrays.asList(
                    Player.builder().build(),
                    Player.builder().build(),
                    Player.builder().build()
            );

            when(playerRepository.findTopNumRetiredPlayersForYear(anyString(), any())).thenReturn(playerList);

            assertEquals(playerList, playerService.getManagerialCandidates("2012"));
        }
    }

    @Nested
    class UpdatePlayers {
        @Test
        void shouldCallThePlayerRepositoryToSaveTheGivenPlayerList() {
            final List<Player> playerList = Collections.singletonList(Player.builder().playerId(1).year("2000").build());

            playerService.updatePlayers(playerList);

            verify(playerRepository).saveAll(playerList);
        }
    }

    @Nested
    class GetUndraftedRookiesForYear {

        @Test
        void shouldCallRepositoryToRetrieveRookiePlayersForGivenYearThatDoNotHaveATeam() {
            playerService.getUndraftedRookiesForYear("1999");

            verify(playerRepository).findPlayersByTeamIdAndYearAndRookie(null, "1999", 1);
        }

        @Test
        void shouldReturnThePlayersReturnedByTheRepository() {
            final List<Player> expectedPlayers = Collections.singletonList(Player.builder().playerId(1).build());

            when(playerRepository.findPlayersByTeamIdAndYearAndRookie(any(), any(), any())).thenReturn(expectedPlayers);

            final List<Player> actualPlayers = playerService.getUndraftedRookiesForYear("1999");

            assertEquals(expectedPlayers, actualPlayers);
        }
    }
}