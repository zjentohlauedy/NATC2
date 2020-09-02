package org.natc.app.service;

import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.PlayerGame;
import org.natc.app.entity.request.PlayerGameSearchRequest;
import org.natc.app.entity.response.PlayerGameResponse;
import org.natc.app.repository.PlayerGameRepository;
import org.natc.app.service.search.PlayerGameSearchService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlayerGameSearchServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private PlayerGameRepository repository;

    @Autowired
    private PlayerGameSearchService searchService;

    @Test
    public void shouldReturnAPlayerGameFromTheDatabaseMappedToAResponse() {
        final PlayerGame playerGame = PlayerGame.builder()
                .gameId(12345)
                .year("2015")
                .datestamp(LocalDate.now())
                .type(GameType.POSTSEASON.getValue())
                .playerId(4567)
                .teamId(28)
                .playingTime(3333)
                .goals(12)
                .stops(15)
                .points(45)
                .build();

        repository.save(playerGame);

        final List<PlayerGameResponse> result = searchService.fetchAll(new PlayerGameSearchRequest());

        assertEquals(1, result.size());

        final PlayerGameResponse response = result.get(0);

        assertEquals(playerGame.getGameId(), response.getGameId());
        assertEquals(playerGame.getYear(), response.getYear());
        assertEquals(playerGame.getDatestamp(), response.getDatestamp());
        assertEquals(GameType.POSTSEASON, response.getType());
        assertEquals(playerGame.getPlayerId(), response.getPlayerId());
        assertEquals(playerGame.getTeamId(), response.getTeamId());
        assertEquals(playerGame.getPlayingTime(), response.getPlayingTime());
        assertEquals(playerGame.getGoals(), response.getGoals());
        assertEquals(playerGame.getStops(), response.getStops());
        assertEquals(playerGame.getPoints(), response.getPoints());
    }

    @Test
    public void shouldMapAllPlayerGameFieldsToTheResponse() {
        final PlayerGame playerGame = PlayerGame.builder()
                .gameId(12345)
                .year("2002")
                .datestamp(LocalDate.now())
                .type(GameType.PRESEASON.getValue())
                .playerId(1234)
                .teamId(123)
                .injured(1)
                .started(0)
                .playingTime(321)
                .attempts(99)
                .goals(55)
                .assists(44)
                .turnovers(33)
                .stops(88)
                .steals(22)
                .penalties(11)
                .offensivePenalties(6)
                .penaltyShotsAttempted(9)
                .penaltyShotsMade(8)
                .overtimePenaltyShotsAttempted(3)
                .overtimePenaltyShotsMade(2)
                .offense(77)
                .points(66)
                .build();

        repository.save(playerGame);

        final List<PlayerGameResponse> result = searchService.fetchAll(new PlayerGameSearchRequest());

        assertEquals(1, result.size());

        final PlayerGameResponse response = result.get(0);

        assertNotNull(response.getGameId());
        assertNotNull(response.getYear());
        assertNotNull(response.getDatestamp());
        assertNotNull(response.getType());
        assertNotNull(response.getPlayerId());
        assertNotNull(response.getTeamId());
        assertNotNull(response.getInjured());
        assertNotNull(response.getStarted());
        assertNotNull(response.getPlayingTime());
        assertNotNull(response.getAttempts());
        assertNotNull(response.getGoals());
        assertNotNull(response.getAssists());
        assertNotNull(response.getTurnovers());
        assertNotNull(response.getStops());
        assertNotNull(response.getSteals());
        assertNotNull(response.getPenalties());
        assertNotNull(response.getOffensivePenalties());
        assertNotNull(response.getPenaltyShotsAttempted());
        assertNotNull(response.getPenaltyShotsMade());
        assertNotNull(response.getOvertimePenaltyShotsAttempted());
        assertNotNull(response.getOvertimePenaltyShotsMade());
        assertNotNull(response.getOffense());
        assertNotNull(response.getPoints());
    }

    @Test
    public void shouldReturnAllEntriesWhenSearchingWithoutValues() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final List<PlayerGameResponse> result = searchService.fetchAll(new PlayerGameSearchRequest());

        assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnNoEntriesWhenSearchingGivenNoDataInTheDatabase() {
        final List<PlayerGameResponse> result = searchService.fetchAll(new PlayerGameSearchRequest());

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForGameWhenSearchingByGameId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameWhenSearchingByGameId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getGameId().equals(1)).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearWhenSearchingByYear() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearWhenSearchingByYear() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(4).year("2002").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getYear().equals("2000")).count());
    }

    @Test
    public void shouldReturnAllEntriesForDatestampWhenSearchingByDatestamp() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForDatestampWhenSearchingByDatestamp() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getDatestamp().equals(LocalDate.parse("2000-03-16"))).count());
    }

    @Test
    public void shouldReturnAllEntriesForTypeWhenSearchingByType() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForTypeWhenSearchingByType() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.POSTSEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getType().equals(GameType.PRESEASON)).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForPlayerWhenSearchingByPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForPlayerWhenSearchingByPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(3).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getPlayerId().equals(1)).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForTeamWhenSearchingByTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForTeamWhenSearchingByTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(3).build(),
                PlayerGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getTeamId().equals(1)).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameAndYearWhenSearchingByGameIdAndYear() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameAndYearWhenSearchingByGameIdAndYear() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(4).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(5).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(6).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(7).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getYear().equals("2000")
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameAndDatestampWhenSearchingByGameIdAndDatestamp() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameAndDatestampWhenSearchingByGameIdAndDatestamp() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(4).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(5).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2002-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(6).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(7).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getDatestamp().equals(LocalDate.parse("2000-03-16"))
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameAndTypeWhenSearchingByGameIdAndType() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameAndTypeWhenSearchingByGameIdAndType() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(4).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(5).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.POSTSEASON.getValue()).playerId(6).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(7).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getType().equals(GameType.PRESEASON)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameAndPlayerWhenSearchingByGameIdAndPlayerId() {
        final List<PlayerGame> playerGameList = Collections.singletonList(
                // Game ID & Player ID are the key fields, so only one record is possible
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameAndPlayerWhenSearchingByGameIdAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getPlayerId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameAndTeamWhenSearchingByGameIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameAndTeamWhenSearchingByGameIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(4).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(5).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(6).teamId(3).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(7).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearAndDatestampWhenSearchingByYearAndDatestamp() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearAndDatestampWhenSearchingByYearAndDatestamp() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getDatestamp().equals(LocalDate.parse("2000-03-16"))
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearAndTypeWhenSearchingByYearAndType() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearAndTypeWhenSearchingByYearAndType() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.POSTSEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getType().equals(GameType.PRESEASON)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForYearAndPlayerWhenSearchingByYearAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForYearAndPlayerWhenSearchingByYearAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(3).teamId(2).build(),
                PlayerGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getPlayerId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(3).build(),
                PlayerGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForDatestampAndTypeWhenSearchingByDatestampAndType() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForDatestampAndTypeWhenSearchingByDatestampAndType() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(5).year("2001").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getType().equals(GameType.PRESEASON)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForDatestampAndPlayerWhenSearchingByDatestampAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForDatestampAndPlayerWhenSearchingByDatestampAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(5).year("2001").datestamp(LocalDate.parse("2002-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(3).teamId(2).build(),
                PlayerGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getPlayerId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForDatestampAndTeamWhenSearchingByDatestampAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForDatestampAndTeamWhenSearchingByDatestampAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(5).year("2001").datestamp(LocalDate.parse("2002-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(3).build(),
                PlayerGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForTypeAndPlayerWhenSearchingByTypeAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForTypeAndPlayerWhenSearchingByTypeAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(5).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(2).build(),
                PlayerGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getType().equals(GameType.PRESEASON) && t.getPlayerId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForTypeAndTeamWhenSearchingByTypeAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForTypeAndTeamWhenSearchingByTypeAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(5).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.POSTSEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(3).build(),
                PlayerGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getType().equals(GameType.PRESEASON) && t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForPlayerAndTeamWhenSearchingByPlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForPlayerAndTeamWhenSearchingByPlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(5).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(3).build(),
                PlayerGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getPlayerId().equals(1) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearAndDatestampWhenSearchingByGameIdYearAndDatestamp() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearAndDatestampWhenSearchingByGameIdYearAndDatestamp() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(4).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(5).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(6).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(7).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(8).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(9).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getYear().equals("2000") && t.getDatestamp().equals(LocalDate.parse("2000-03-16"))
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearAndTypeWhenSearchingByGameIdYearAndType() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearAndTypeWhenSearchingByGameIdYearAndType() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(4).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(5).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(6).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(7).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.POSTSEASON.getValue()).playerId(8).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(9).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getYear().equals("2000") && t.getType().equals(GameType.PRESEASON)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForGameYearAndPlayerWhenSearchingByGameIdYearAndPlayerId() {
        final List<PlayerGame> playerGameList = Collections.singletonList(
                // Game ID & Player ID are the key fields, so only one record is possible
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForGameYearAndPlayerWhenSearchingByGameIdYearAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getYear().equals("2000") && t.getPlayerId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearAndTeamWhenSearchingByGameIdYearAndTeamIdAndYearIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2003").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
    
    @Test
    public void shouldReturnAllEntriesForGameYearAndTeamWhenSearchingByGameIdYearAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForGameYearAndTeamWhenSearchingByGameIdYearAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(4).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(5).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(6).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(7).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(8).teamId(3).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(9).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getYear().equals("2000") && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameDatestampAndTypeWhenSearchingByGameIdDatestampAndType() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameDatestampAndTypeWhenSearchingByGameIdDatestampAndType() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(4).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(5).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(6).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(7).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(8).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(9).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getType().equals(GameType.PRESEASON)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForGameDatestampAndPlayerWhenSearchingByGameIdDatestampAndPlayerId() {
        final List<PlayerGame> playerGameList = Collections.singletonList(
                // Game ID & Player ID are the key fields, so only one record is possible
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForGameDatestampAndPlayerWhenSearchingByGameIdDatestampAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getPlayerId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnNoEntriesForGameDatestampAndPlayerWhenSearchingByGameIdDatestampAndPlayerIdAndDatestampIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
    
    @Test
    public void shouldReturnAllEntriesForGameDatestampAndTeamWhenSearchingByGameIdDatestampAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForGameDatestampAndTeamWhenSearchingByGameIdDatestampAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(4).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(5).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(6).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2002-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(7).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(8).teamId(3).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(9).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForGameTypeAndPlayerWhenSearchingByGameIdTypeAndPlayerId() {
        final List<PlayerGame> playerGameList = Collections.singletonList(
                // Game ID & Player ID are the key fields, so only one record is possible
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForGameTypeAndPlayerWhenSearchingByGameIdTypeAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getType().equals(GameType.PRESEASON) && t.getPlayerId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnNoEntriesForGameTypeAndPlayerWhenSearchingByGameIdTypeAndPlayerIdAndTypeIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
    
    @Test
    public void shouldReturnAllEntriesForGameTypeAndTeamWhenSearchingByGameIdTypeAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForGameTypeAndTeamWhenSearchingByGameIdTypeAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(4).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(5).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(6).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.POSTSEASON.getValue()).playerId(7).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(8).teamId(3).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(9).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getType().equals(GameType.PRESEASON) && t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForGamePlayerAndTeamWhenSearchingByGameIdPlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Collections.singletonList(
                // Game ID & Player ID are the key fields, so only one record is possible
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForGamePlayerAndTeamWhenSearchingByGameIdPlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getPlayerId().equals(1) && t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnNoEntriesForGamePlayerAndTeamWhenSearchingByGameIdPlayerIdAndTeamAndTeamIdIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForYearDatestampAndTypeWhenSearchingByYearDatestampAndType() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearDatestampAndTypeWhenSearchingByYearDatestampAndType() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(6).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getType().equals(GameType.PRESEASON)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForYearDatestampAndPlayerWhenSearchingByYearDatestampAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForYearDatestampAndPlayerWhenSearchingByYearDatestampAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(6).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(3).teamId(2).build(),
                PlayerGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getPlayerId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForYearDatestampAndTeamWhenSearchingByYearDatestampAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForYearDatestampAndTeamWhenSearchingByYearDatestampAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(6).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(3).build(),
                PlayerGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForYearTypeAndPlayerWhenSearchingByYearTypeAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-05-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForYearTypeAndPlayerWhenSearchingByYearTypeAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(6).year("2002").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(2).build(),
                PlayerGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-05-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getType().equals(GameType.PRESEASON) && t.getPlayerId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForYearTypeAndTeamWhenSearchingByYearTypeAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-05-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForYearTypeAndTeamWhenSearchingByYearTypeAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(6).year("2002").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.POSTSEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(3).build(),
                PlayerGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-05-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getType().equals(GameType.PRESEASON) && t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForYearPlayerAndTeamWhenSearchingByYearPlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-05-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForYearPlayerAndTeamWhenSearchingByYearPlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(6).year("2002").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(3).build(),
                PlayerGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-05-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getPlayerId().equals(1) && t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForDatestampTypeAndPlayerWhenSearchingByDatestampTypeAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2003").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForDatestampTypeAndPlayerWhenSearchingByDatestampTypeAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(6).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(8).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(2).build(),
                PlayerGame.builder().gameId(9).year("2003").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getType().equals(GameType.PRESEASON) && t.getPlayerId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForDatestampTypeAndTeamWhenSearchingByDatestampTypeAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2003").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForDatestampTypeAndTeamWhenSearchingByDatestampTypeAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(6).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(8).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(3).build(),
                PlayerGame.builder().gameId(9).year("2003").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getType().equals(GameType.PRESEASON) && t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForDatestampPlayerAndTeamWhenSearchingByDatestampPlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2003").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForDatestampPlayerAndTeamWhenSearchingByDatestampPlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(6).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(8).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(3).build(),
                PlayerGame.builder().gameId(9).year("2003").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getPlayerId().equals(1) && t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForTypePlayerAndTeamWhenSearchingByTypePlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2002").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2003").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForTypePlayerAndTeamWhenSearchingByTypePlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(6).year("2002").datestamp(LocalDate.parse("2001-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(8).year("2002").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(3).build(),
                PlayerGame.builder().gameId(9).year("2003").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getType().equals(GameType.PRESEASON) && t.getPlayerId().equals(1) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearDatestampAndTypeWhenSearchingByGameYearDatestampAndType() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearDatestampAndTypeWhenSearchingByGameYearDatestampAndType() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(4).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(5).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(6).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(7).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(8).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(9).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(10).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(11).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearDatestampAndPlayerWhenSearchingByGameIdYearDatestampAndPlayerId() {
        final List<PlayerGame> playerGameList = Collections.singletonList(
                // Game ID & Player ID are the key fields, so only one record is possible
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearDatestampAndPlayerWhenSearchingByGameIdYearDatestampAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getPlayerId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearDatestampAndPlayerWhenSearchingByGameIdYearDatestampAndPlayerIdAndYearIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearDatestampAndPlayerWhenSearchingByGameIdYearDatestampAndPlayerIdAndDatestampIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearDatestampAndTeamWhenSearchingByGameIdYearDatestampAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearDatestampAndTeamWhenSearchingByGameIdYearDatestampAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(4).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(5).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(6).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(7).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(8).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(9).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(10).teamId(3).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(11).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForGameYearTypeAndPlayerWhenSearchingByGameIdYearTypeAndPlayerId() {
        final List<PlayerGame> playerGameList = Collections.singletonList(
                // Game ID & Player ID are the key fields, so only one record is possible
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForGameYearTypeAndPlayerWhenSearchingByGameIdYearTypeAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getPlayerId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnNoEntriesForGameYearTypeAndPlayerWhenSearchingByGameIdYearTypeAndPlayerIdAndYearIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
    
    @Test
    public void shouldReturnNoEntriesForGameYearTypeAndPlayerWhenSearchingByGameIdYearTypeAndPlayerIdAndTypeIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
    
    @Test
    public void shouldReturnAllEntriesForGameYearTypeAndTeamWhenSearchingByGameIdYearTypeAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForGameYearTypeAndTeamWhenSearchingByGameIdYearTypeAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(4).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(5).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(6).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(7).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(8).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.POSTSEASON.getValue()).playerId(9).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(10).teamId(3).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(11).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearPlayerAndTeamWhenSearchingByGameIdYearPlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Collections.singletonList(
                // Game ID & Player ID are the key fields, so only one record is possible
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearPlayerAndTeamWhenSearchingByGameIdYearPlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getPlayerId().equals(1) &&
                        t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearPlayerAndTeamWhenSearchingByGameIdYearPlayerIdAndTeamAndYearIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearPlayerAndTeamWhenSearchingByGameIdYearPlayerIdAndTeamAndTeamIdIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForGameDatestampTypeAndPlayerWhenSearchingByGameIdDatestampTypeAndPlayerId() {
        final List<PlayerGame> playerGameList = Collections.singletonList(
                // Game ID & Player ID are the key fields, so only one record is possible
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameDatestampTypeAndPlayerWhenSearchingByGameIdDatestampTypeAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getPlayerId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameDatestampTypeAndPlayerWhenSearchingByGameIdDatestampTypeAndPlayerIdAndDatestampIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameDatestampTypeAndPlayerWhenSearchingByGameIdDatestampTypeAndPlayerIdAndTypeIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForGameDatestampTypeAndTeamWhenSearchingByGameIdDatestampTypeAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameDatestampTypeAndTeamWhenSearchingByGameIdDatestampTypeAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(4).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(5).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(6).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(7).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(8).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(9).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(10).teamId(3).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(11).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForGameDatestampPlayerAndTeamWhenSearchingByGameIdDatestampPlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Collections.singletonList(
                // Game ID & Player ID are the key fields, so only one record is possible
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForGameDatestampPlayerAndTeamWhenSearchingByGameIdDatestampPlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getPlayerId().equals(1) &&
                        t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnNoEntriesForGameDatestampPlayerAndTeamWhenSearchingByGameIdDatestampPlayerIdAndTeamAndDatestampIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
    
    @Test
    public void shouldReturnNoEntriesForGameDatestampPlayerAndTeamWhenSearchingByGameIdDatestampPlayerIdAndTeamAndTeamIdIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
    
    @Test
    public void shouldReturnAllEntriesForGameTypePlayerAndTeamWhenSearchingByGameIdTypePlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Collections.singletonList(
                // Game ID & Player ID are the key fields, so only one record is possible
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForGameTypePlayerAndTeamWhenSearchingByGameIdTypePlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getPlayerId().equals(1) &&
                        t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnNoEntriesForGameTypePlayerAndTeamWhenSearchingByGameIdTypePlayerIdAndTeamAndTypeIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
    
    @Test
    public void shouldReturnNoEntriesForGameTypePlayerAndTeamWhenSearchingByGameIdTypePlayerIdAndTeamAndTeamIdIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForYearDatestampTypeAndPlayerWhenSearchingByYearDatestampTypeAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearDatestampTypeAndPlayerWhenSearchingByYearDatestampTypeAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(10).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(2).build(),
                PlayerGame.builder().gameId(11).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getPlayerId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearDatestampTypeAndTeamWhenSearchingByYearDatestampTypeAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearDatestampTypeAndTeamWhenSearchingByYearDatestampTypeAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(10).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(3).build(),
                PlayerGame.builder().gameId(11).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForYearDatestampPlayerAndTeamWhenSearchingByYearDatestampPlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForYearDatestampPlayerAndTeamWhenSearchingByYearDatestampPlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(10).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(3).build(),
                PlayerGame.builder().gameId(11).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getPlayerId().equals(1) &&
                        t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForYearTypePlayerAndTeamWhenSearchingByYearTypePlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-05-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForYearTypePlayerAndTeamWhenSearchingByYearTypePlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(10).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(3).build(),
                PlayerGame.builder().gameId(11).year("2000").datestamp(LocalDate.parse("2000-05-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getPlayerId().equals(1) &&
                        t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForDatestampTypePlayerAndTeamWhenSearchingByDatestampTypePlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForDatestampTypePlayerAndTeamWhenSearchingByDatestampTypePlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(7).year("2001").datestamp(LocalDate.parse("2000-05-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(8).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(9).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(10).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(3).build(),
                PlayerGame.builder().gameId(11).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getPlayerId().equals(1) &&
                        t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearDatestampTypeAndPlayerWhenSearchingByGameIdYearDatestampTypeAndPlayerId() {
        final List<PlayerGame> playerGameList = Collections.singletonList(
                // Game ID & Player ID are the key fields, so only one record is possible
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearDatestampTypeAndPlayerWhenSearchingByGameIdYearDatestampTypeAndPlayerId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getPlayerId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearDatestampTypeAndPlayerWhenSearchingByGameIdYearDatestampTypeAndPlayerIdAndYearIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearDatestampTypeAndPlayerWhenSearchingByGameIdYearDatestampTypeAndPlayerIdAndDatestampIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearDatestampTypeAndPlayerWhenSearchingByGameIdYearDatestampTypeAndPlayerIdAndTypeIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearDatestampTypeAndTeamWhenSearchingByGameIdYearDatestampTypeAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearDatestampTypeAndTeamWhenSearchingByGameIdYearDatestampTypeAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).playerId(4).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(5).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(6).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(7).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(8).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(9).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).playerId(10).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(11).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(12).teamId(3).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(13).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnAllEntriesForGameYearDatestampPlayerAndTeamWhenSearchingByGameIdYearDatestampPlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Collections.singletonList(
                // Game ID & Player ID are the key fields, so only one record is possible
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForGameYearDatestampPlayerAndTeamWhenSearchingByGameIdYearDatestampPlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getPlayerId().equals(1) &&
                        t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnNoEntriesForGameYearDatestampPlayerAndTeamWhenSearchingByGameIdYearDatestampPlayerIdAndTeamAndYearIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
    
    @Test
    public void shouldReturnNoEntriesForGameYearDatestampPlayerAndTeamWhenSearchingByGameIdYearDatestampPlayerIdAndTeamAndDatestampIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
    
    @Test
    public void shouldReturnNoEntriesForGameYearDatestampPlayerAndTeamWhenSearchingByGameIdYearDatestampPlayerIdAndTeamIdAndTeamIdIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
    
    @Test
    public void shouldReturnAllEntriesForGameYearTypePlayerAndTeamWhenSearchingByGameIdYearTypePlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Collections.singletonList(
                // Game ID & Player ID are the key fields, so only one record is possible
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForGameYearTypePlayerAndTeamWhenSearchingByGameIdYearTypePlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getPlayerId().equals(1) &&
                        t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnNoEntriesForGameYearTypePlayerAndTeamWhenSearchingByGameIdYearTypePlayerIdAndTeamIdAndYearIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
    
    @Test
    public void shouldReturnNoEntriesForGameYearTypePlayerAndTeamWhenSearchingByGameIdYearTypePlayerIdAndTeamIdAndTypeIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
    
    @Test
    public void shouldReturnNoEntriesForGameYearTypePlayerAndTeamWhenSearchingByGameIdYearTypePlayerIdAndTeamIdAndTeamIdIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
    
    @Test
    public void shouldReturnAllEntriesForGameDatestampTypePlayerAndTeamWhenSearchingByGameIdDatestampTypePlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Collections.singletonList(
                // Game ID & Player ID are the key fields, so only one record is possible
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForGameDatestampTypePlayerAndTeamWhenSearchingByGameIdDatestampTypePlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getPlayerId().equals(1) &&
                        t.getTeamId().equals(1)
        ).count());
    }
    
    @Test
    public void shouldReturnNoEntriesForGameDatestampTypePlayerAndTeamWhenSearchingByGameIdDatestampTypePlayerIdAndTeamIdAndDatestampIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
    
    @Test
    public void shouldReturnNoEntriesForGameDatestampTypePlayerAndTeamWhenSearchingByGameIdDatestampTypePlayerIdAndTeamIdAndTypeIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
    
    @Test
    public void shouldReturnNoEntriesForGameDatestampTypePlayerAndTeamWhenSearchingByGameIdDatestampTypePlayerIdAndTeamIdAndTeamIdIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
    
    @Test
    public void shouldReturnAllEntriesForYearDatestampTypePlayerAndTeamWhenSearchingByYearDatestampTypePlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }
    
    @Test
    public void shouldOnlyReturnEntriesForYearDatestampTypePlayerAndTeamWhenSearchingByYearDatestampTypePlayerIdAndTeamId() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(8).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-05-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(10).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(11).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerGame.builder().gameId(12).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(3).build(),
                PlayerGame.builder().gameId(13).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getPlayerId().equals(1) &&
                        t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnMatchingEntryWhenSearchingByAllParameters() {
        final List<PlayerGame> playerGameList = Collections.singletonList(
                // Game ID & Player ID are the key fields, so only one record is possible
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnMatchingEntryWhenSearchingByAllParameters() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getPlayerId().equals(1) &&
                        t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldNotReturnMatchingEntriyWhenSearchingByAllParametersAndYearIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldNotReturnMatchingEntriyWhenSearchingByAllParametersAndDatestampIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldNotReturnMatchingEntriyWhenSearchingByAllParametersAndTypeIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldNotReturnMatchingEntriyWhenSearchingByAllParametersAndTeamIdIsDifferent() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build()
        );

        repository.saveAll(playerGameList);

        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
}