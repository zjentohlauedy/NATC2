package org.natc.app.service;

import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.PlayerGame;
import org.natc.app.entity.request.PlayerGameSearchRequest;
import org.natc.app.entity.response.PlayerGameResponse;
import org.natc.app.repository.PlayerGameRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
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
}