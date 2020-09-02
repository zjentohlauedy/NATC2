package org.natc.app.service.search;

import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.PlayerStatsSummary;
import org.natc.app.entity.request.PlayerStatsSummarySearchRequest;
import org.natc.app.entity.response.PlayerStatsSummaryResponse;
import org.natc.app.repository.PlayerStatsSummaryRepository;
import org.natc.app.service.NATCServiceIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlayerStatsSummarySearchServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private PlayerStatsSummaryRepository repository;

    @Autowired
    private PlayerStatsSummarySearchService service;

    @Test
    public void shouldReturnAPlayerStatsSummaryFromTheDatabaseMappedToAResponse() {
        final PlayerStatsSummary playerStatsSummary = PlayerStatsSummary.builder()
                .year("2014")
                .type(GameType.REGULAR_SEASON.getValue())
                .playerId(123)
                .teamId(36)
                .games(99)
                .playingTime(12345)
                .attempts(425)
                .goals(321)
                .assists(234)
                .build();

        repository.save(playerStatsSummary);

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(new PlayerStatsSummarySearchRequest());

        assertEquals(1, result.size());

        final PlayerStatsSummaryResponse response = result.get(0);

        assertEquals(playerStatsSummary.getYear(), response.getYear());
        assertEquals(GameType.REGULAR_SEASON, response.getType());
        assertEquals(playerStatsSummary.getPlayerId(), response.getPlayerId());
        assertEquals(playerStatsSummary.getTeamId(), response.getTeamId());
        assertEquals(playerStatsSummary.getGames(), response.getGames());
        assertEquals(playerStatsSummary.getPlayingTime(), response.getPlayingTime());
        assertEquals(playerStatsSummary.getAttempts(), response.getAttempts());
        assertEquals(playerStatsSummary.getGoals(), response.getGoals());
        assertEquals(playerStatsSummary.getAssists(), response.getAssists());
    }

    @Test
    public void shouldMapAllPlayerFieldsToTheResponse() {
        final PlayerStatsSummary playerStatsSummary = PlayerStatsSummary.builder()
                .year("2002")
                .type(GameType.PRESEASON.getValue())
                .playerId(123)
                .games(99)
                .gamesStarted(88)
                .playingTime(1234)
                .attempts(77)
                .goals(66)
                .assists(55)
                .turnovers(44)
                .stops(33)
                .steals(22)
                .penalties(11)
                .offensivePenalties(9)
                .penaltyShotsAttempted(8)
                .penaltyShotsMade(7)
                .overtimePenaltyShotsAttempted(6)
                .overtimePenaltyShotsMade(5)
                .teamId(321)
                .build();

        repository.save(playerStatsSummary);

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(new PlayerStatsSummarySearchRequest());

        assertEquals(1, result.size());

        final PlayerStatsSummaryResponse response = result.get(0);

        assertNotNull(response.getYear());
        assertNotNull(response.getType());
        assertNotNull(response.getPlayerId());
        assertNotNull(response.getGames());
        assertNotNull(response.getGamesStarted());
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
        assertNotNull(response.getTeamId());
    }

    @Test
    public void shouldReturnAllEntriesWhenSearchingWithoutValues() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerStatsSummaries);

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(new PlayerStatsSummarySearchRequest());

        assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnNoEntriesWhenSearchingGivenNoDataInTheDatabase() {
        final List<PlayerStatsSummaryResponse> result = service.fetchAll(new PlayerStatsSummarySearchRequest());

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForYearWhenSearchingByYear() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("2000")
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearWhenSearchingByYear() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("2000")
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getYear().equals("2000")).count());
    }

    @Test
    public void shouldReturnAllEntriesForTypeWhenSearchingByType() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.PRESEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForTypeWhenSearchingByType() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.POSTSEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.PRESEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getType().equals(GameType.PRESEASON)).count());
    }

    @Test
    public void shouldReturnAllEntriesForPlayerWhenSearchingByPlayerId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .playerId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForPlayerWhenSearchingByPlayerId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).playerId(3).teamId(2).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .playerId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getPlayerId().equals(1)).count());
    }

    @Test
    public void shouldReturnAllEntriesForTeamWhenSearchingByTeamId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .teamId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForTeamWhenSearchingByTeamId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerStatsSummary.builder().year("2003").type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(3).build(),
                PlayerStatsSummary.builder().year("2004").type(GameType.POSTSEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .teamId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getTeamId().equals(1)).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearAndTypeWhenSearchingByYearAndType() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearAndTypeWhenSearchingByYearAndType() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.PRESEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(3).teamId(3).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getType().equals(GameType.PRESEASON)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearAndPlayerWhenSearchingByYearAndPlayerId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("2000")
                .playerId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearAndPlayerWhenSearchingByYearAndPlayerId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(3).teamId(2).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("2000")
                .playerId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getPlayerId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("2000")
                .teamId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(3).teamId(2).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(4).teamId(1).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.REGULAR_SEASON.getValue()).playerId(5).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(6).teamId(3).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).playerId(7).teamId(1).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("2000")
                .teamId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForTypeAndPlayerWhenSearchingByTypeAndPlayerId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.PRESEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForTypeAndPlayerWhenSearchingByTypeAndPlayerId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.POSTSEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(3).teamId(2).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.PRESEASON.getValue()).playerId(1).teamId(3).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getType().equals(GameType.PRESEASON) && t.getPlayerId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForTypeAndTeamWhenSearchingByTypeAndTeamId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForTypeAndTeamWhenSearchingByTypeAndTeamId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(3).teamId(2).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(4).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.POSTSEASON.getValue()).playerId(5).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(6).teamId(3).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.PRESEASON.getValue()).playerId(7).teamId(1).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getType().equals(GameType.PRESEASON) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForPlayerAndTeamWhenSearchingByPlayerIdAndTeamId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForPlayerAndTeamWhenSearchingByPlayerIdAndTeamId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerStatsSummary.builder().year("2003").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2004").type(GameType.REGULAR_SEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerStatsSummary.builder().year("2005").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(3).build(),
                PlayerStatsSummary.builder().year("2006").type(GameType.POSTSEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getPlayerId().equals(1) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearTypeAndPlayerWhenSearchingByYearTypeAndPlayerId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Collections.singletonList(
                // Year, Type and Player Id are the key fields, so only one record is possible
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearTypeAndPlayerWhenSearchingByYearTypeAndPlayerId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(3).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(2).teamId(4).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getType().equals(GameType.PRESEASON) && t.getPlayerId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearTypeAndTeamWhenSearchingByYearTypeAndTeamId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearTypeAndTeamWhenSearchingByYearTypeAndTeamId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(4).teamId(2).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(5).teamId(1).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.PRESEASON.getValue()).playerId(6).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).playerId(7).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(8).teamId(3).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(9).teamId(1).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getType().equals(GameType.PRESEASON) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearPlayerAndTeamWhenSearchingByYearPlayerIdAndTeamId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("2000")
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearPlayerAndTeamWhenSearchingByYearPlayerIdAndTeamId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.POSTSEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.ALLSTAR.getValue()).playerId(1).teamId(3).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("2000")
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getPlayerId().equals(1) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForTypePlayerAndTeamWhenSearchingByTypePlayerIdAndTeamId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForTypePlayerAndTeamWhenSearchingByTypePlayerIdAndTeamId() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerStatsSummary.builder().year("2003").type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build(),
                PlayerStatsSummary.builder().year("2004").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2005").type(GameType.POSTSEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2006").type(GameType.PRESEASON.getValue()).playerId(3).teamId(1).build(),
                PlayerStatsSummary.builder().year("2007").type(GameType.PRESEASON.getValue()).playerId(1).teamId(3).build(),
                PlayerStatsSummary.builder().year("2008").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getType().equals(GameType.PRESEASON) && t.getPlayerId().equals(1) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnMatchingEntryWhenSearchingByAllParameters() {
        final List<PlayerStatsSummary> playerStatsSummaries = Collections.singletonList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnMatchingEntryWhenSearchingByAllParameters() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build()
        );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getYear().equals("2000") &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getPlayerId().equals(1) &&
                        t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldNotReturnAnyRecordsWhenSearchingByAllParametersAndTeamIdDoesNotMatch() {
        final List<PlayerStatsSummary> playerStatsSummaries = Arrays.asList(
                PlayerStatsSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(2).teamId(1).build(),
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(2).build()
                );

        repository.saveAll(playerStatsSummaries);

        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        assertEquals(0, result.size());
    }
}