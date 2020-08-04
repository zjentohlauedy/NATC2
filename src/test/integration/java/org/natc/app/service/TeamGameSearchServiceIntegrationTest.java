package org.natc.app.service;

import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.TeamGame;
import org.natc.app.entity.request.TeamGameSearchRequest;
import org.natc.app.entity.response.TeamGameResponse;
import org.natc.app.repository.TeamGameRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TeamGameSearchServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private TeamGameRepository repository;

    @Autowired
    private TeamGameSearchService searchService;

    @Test
    public void shouldReturnATeamGameFromTheDatabaseMappedToAResponse() {
        final TeamGame teamGame = TeamGame.builder()
                .gameId(12345)
                .year("2015")
                .datestamp(LocalDate.now())
                .type(GameType.POSTSEASON.getValue())
                .teamId(23)
                .opponent(6)
                .playoffRound(2)
                .attempts(45)
                .goals(22)
                .totalScore(71)
                .build();

        repository.save(teamGame);

        final List<TeamGameResponse> result = searchService.fetchAll(new TeamGameSearchRequest());

        assertEquals(1, result.size());

        final TeamGameResponse response = result.get(0);

        assertEquals(teamGame.getGameId(), response.getGameId());
        assertEquals(teamGame.getYear(), response.getYear());
        assertEquals(teamGame.getDatestamp(), response.getDatestamp());
        assertEquals(GameType.POSTSEASON, response.getType());
        assertEquals(teamGame.getTeamId(), response.getTeamId());
        assertEquals(teamGame.getOpponent(), response.getOpponent());
        assertEquals(teamGame.getPlayoffRound(), response.getPlayoffRound());
        assertEquals(teamGame.getAttempts(), response.getAttempts());
        assertEquals(teamGame.getGoals(), response.getGoals());
        assertEquals(teamGame.getTotalScore(), response.getTotalScore());
    }

    @Test
    public void shouldMapAllTeamGameFieldsToTheResponse() {
        final TeamGame teamGame = TeamGame.builder()
                .gameId(1234)
                .year("1984")
                .datestamp(LocalDate.now())
                .type(GameType.POSTSEASON.getValue())
                .playoffRound(3)
                .teamId(22)
                .opponent(33)
                .road(0)
                .overtime(1)
                .win(1)
                .possessions(4321)
                .possessionTime(9999)
                .attempts(555)
                .goals(111)
                .turnovers(66)
                .steals(55)
                .penalties(44)
                .offensivePenalties(11)
                .penaltyShotsAttempted(9)
                .penaltyShotsMade(5)
                .overtimePenaltyShotsAttempted(6)
                .overtimePenaltyShotsMade(2)
                .period1Score(19)
                .period2Score(18)
                .period3Score(17)
                .period4Score(16)
                .period5Score(15)
                .overtimeScore(12)
                .totalScore(99)
                .build();

        repository.save(teamGame);

        final List<TeamGameResponse> result = searchService.fetchAll(new TeamGameSearchRequest());

        assertEquals(1, result.size());

        final TeamGameResponse response = result.get(0);

        assertNotNull(response.getGameId());
        assertNotNull(response.getYear());
        assertNotNull(response.getDatestamp());
        assertNotNull(response.getType());
        assertNotNull(response.getPlayoffRound());
        assertNotNull(response.getTeamId());
        assertNotNull(response.getOpponent());
        assertNotNull(response.getRoad());
        assertNotNull(response.getOvertime());
        assertNotNull(response.getWin());
        assertNotNull(response.getPossessions());
        assertNotNull(response.getPossessionTime());
        assertNotNull(response.getAttempts());
        assertNotNull(response.getGoals());
        assertNotNull(response.getTurnovers());
        assertNotNull(response.getSteals());
        assertNotNull(response.getPenalties());
        assertNotNull(response.getOffensivePenalties());
        assertNotNull(response.getPenaltyShotsAttempted());
        assertNotNull(response.getPenaltyShotsMade());
        assertNotNull(response.getOvertimePenaltyShotsAttempted());
        assertNotNull(response.getOvertimePenaltyShotsMade());
        assertNotNull(response.getPeriod1Score());
        assertNotNull(response.getPeriod2Score());
        assertNotNull(response.getPeriod3Score());
        assertNotNull(response.getPeriod4Score());
        assertNotNull(response.getPeriod5Score());
        assertNotNull(response.getOvertimeScore());
        assertNotNull(response.getTotalScore());
    }

    @Test
    public void shouldReturnAllEntriesWhenSearchingWithoutValues() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final List<TeamGameResponse> result = searchService.fetchAll(new TeamGameSearchRequest());

        assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnNoEntriesWhenSearchingGivenNoDataInTheDatabase() {
        final List<TeamGameResponse> result = searchService.fetchAll(new TeamGameSearchRequest());

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForGameWhenSearchingByGameId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameWhenSearchingByGameId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getGameId().equals(1)).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearWhenSearchingByYear() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearWhenSearchingByYear() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(4).year("2002").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getYear().equals("2000")).count());
    }

    @Test
    public void shouldReturnAllEntriesForDatestampWhenSearchingByDatestamp() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForDatestampWhenSearchingByDatestamp() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2000-04-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getDatestamp().equals(LocalDate.parse("2000-03-16"))).count());
    }

    @Test
    public void shouldReturnAllEntriesForTypeWhenSearchingByType() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .type(GameType.PRESEASON)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForTypeWhenSearchingByType() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.POSTSEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .type(GameType.PRESEASON)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getType().equals(GameType.PRESEASON)).count());
    }

    @Test
    public void shouldReturnAllEntriesForTeamWhenSearchingByTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForTeamWhenSearchingByTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(3).opponent(2).build(),
                TeamGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getTeamId().equals(1)).count());
    }

    @Test
    public void shouldReturnAllEntriesForOpponentWhenSearchingByOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForOpponentWhenSearchingByOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(3).build(),
                TeamGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getOpponent().equals(1)).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameAndYearWhenSearchingByGameIdAndYear() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameAndYearWhenSearchingByGameIdAndYear() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(4).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(5).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(6).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(7).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getYear().equals("2000")
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameAndDatestampWhenSearchingByGameIdAndDatestamp() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameAndDatestampWhenSearchingByGameIdAndDatestamp() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(4).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(5).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(6).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(7).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getDatestamp().equals(LocalDate.parse("2000-03-16"))
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameAndTypeWhenSearchingByGameIdAndType() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameAndTypeWhenSearchingByGameIdAndType() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).teamId(4).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).teamId(5).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.POSTSEASON.getValue()).teamId(6).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).teamId(7).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getType().equals(GameType.PRESEASON)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameAndTeamWhenSearchingByGameIdAndTeamId() {
        final List<TeamGame> teamGameList = Collections.singletonList(
                // Game ID & Team ID are the key fields, so only one record is possible
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameAndTeamWhenSearchingByGameIdAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameAndOpponentWhenSearchingByGameIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameAndOpponentWhenSearchingByGameIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(4).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(5).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(6).opponent(3).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(7).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearAndDatestampWhenSearchingByYearAndDatestamp() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearAndDatestampWhenSearchingByYearAndDatestamp() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2002-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getDatestamp().equals(LocalDate.parse("2000-03-16"))
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearAndTypeWhenSearchingByYearAndType() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearAndTypeWhenSearchingByYearAndType() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.POSTSEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getType().equals(GameType.PRESEASON)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(3).opponent(2).build(),
                TeamGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearAndOpponentWhenSearchingByYearAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearAndOpponentWhenSearchingByYearAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(5).year("2002").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(3).build(),
                TeamGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForDatestampAndTypeWhenSearchingByDatestampAndType() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForDatestampAndTypeWhenSearchingByDatestampAndType() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(5).year("2001").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getType().equals(GameType.PRESEASON)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForDatestampAndTeamWhenSearchingByDatestampAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForDatestampAndTeamWhenSearchingByDatestampAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(5).year("2001").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(3).opponent(2).build(),
                TeamGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForDatestampAndOpponentWhenSearchingByDatestampAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForDatestampAndOpponentWhenSearchingByDatestampAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(5).year("2001").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(3).build(),
                TeamGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForTypeAndTeamWhenSearchingByTypeAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForTypeAndTeamWhenSearchingByTypeAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(5).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(2).build(),
                TeamGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getType().equals(GameType.PRESEASON) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForTypeAndOpponentWhenSearchingByTypeAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .type(GameType.PRESEASON)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForTypeAndOpponentWhenSearchingByTypeAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(5).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.POSTSEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(3).build(),
                TeamGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .type(GameType.PRESEASON)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getType().equals(GameType.PRESEASON) && t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForTeamAndOpponentWhenSearchingByTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForTeamAndOpponentWhenSearchingByTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(4).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(5).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(3).build(),
                TeamGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getTeamId().equals(1) && t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearAndDatestampWhenSearchingByGameIdYearAndDatestamp() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearAndDatestampWhenSearchingByGameIdYearAndDatestamp() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(4).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(5).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(6).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(7).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(8).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(9).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getYear().equals("2000") && t.getDatestamp().equals(LocalDate.parse("2000-03-16"))
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearAndTypeWhenSearchingByGameIdYearAndType() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearAndTypeWhenSearchingByGameIdYearAndType() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(4).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(5).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(6).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(7).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.POSTSEASON.getValue()).teamId(8).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(9).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getYear().equals("2000") && t.getType().equals(GameType.PRESEASON)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearAndTeamWhenSearchingByGameIdYearAndTeamId() {
        final List<TeamGame> teamGameList = Collections.singletonList(
                // Game ID & Team ID are the key fields, so only one record is possible
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearAndTeamWhenSearchingByGameIdYearAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getYear().equals("2000") && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearAndTeamWhenSearchingByGameIdYearAndTeamIdAndYearIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearAndOpponentWhenSearchingByGameIdYearAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearAndOpponentWhenSearchingByGameIdYearAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(4).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(5).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(6).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(7).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(8).opponent(3).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.POSTSEASON.getValue()).teamId(9).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getYear().equals("2000") && t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameDatestampAndTypeWhenSearchingByGameIdDatestampAndType() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameDatestampAndTypeWhenSearchingByGameIdDatestampAndType() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(4).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(5).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(6).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(7).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(8).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(9).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getType().equals(GameType.PRESEASON)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameDatestampAndTeamWhenSearchingByGameIdDatestampAndTeamId() {
        final List<TeamGame> teamGameList = Collections.singletonList(
                // Game ID & Team ID are the key fields, so only one record is possible
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameDatestampAndTeamWhenSearchingByGameIdDatestampAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameDatestampAndTeamWhenSearchingByGameIdDatestampAndTeamIdAndDatestampIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForGameDatestampAndOpponentWhenSearchingByGameIdDatestampAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameDatestampAndOpponentWhenSearchingByGameIdDatestampAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(4).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(5).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(6).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-18")).type(GameType.REGULAR_SEASON.getValue()).teamId(7).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(8).opponent(3).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(9).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameTypeAndTeamWhenSearchingByGameIdTypeAndTeamId() {
        final List<TeamGame> teamGameList = Collections.singletonList(
                // Game ID & Team ID are the key fields, so only one record is possible
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameTypeAndTeamWhenSearchingByGameIdTypeAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getType().equals(GameType.PRESEASON) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameTypeAndTeamWhenSearchingByGameIdTypeAndTeamIdAndTypeIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForGameTypeAndOpponentWhenSearchingByGameIdTypeAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameTypeAndOpponentWhenSearchingByGameIdTypeAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(4).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(5).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(6).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-17")).type(GameType.POSTSEASON.getValue()).teamId(7).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(8).opponent(3).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(9).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getType().equals(GameType.PRESEASON) && t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameTeamAndOpponentWhenSearchingByGameIdTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Collections.singletonList(
                // Game ID & Team ID are the key fields, so only one record is possible
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameTeamAndOpponentWhenSearchingByGameIdTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) && t.getTeamId().equals(1) && t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameTeamAndOpponentWhenSearchingByGameIdTeamIdAndOpponentAndOpponentIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForYearDatestampAndTypeWhenSearchingByYearDatestampAndType() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearDatestampAndTypeWhenSearchingByYearDatestampAndType() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(6).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getType().equals(GameType.PRESEASON)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearDatestampAndTeamWhenSearchingByYearDatestampAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearDatestampAndTeamWhenSearchingByYearDatestampAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(6).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(3).opponent(2).build(),
                TeamGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearDatestampAndOpponentWhenSearchingByYearDatestampAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearDatestampAndOpponentWhenSearchingByYearDatestampAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(6).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(3).build(),
                TeamGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearTypeAndTeamWhenSearchingByYearTypeAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearTypeAndTeamWhenSearchingByYearTypeAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(6).year("2002").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(2).build(),
                TeamGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getType().equals(GameType.PRESEASON) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearTypeAndOpponentWhenSearchingByYearTypeAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearTypeAndTeamWhenSearchingByYearTypeAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(6).year("2002").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.POSTSEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(3).build(),
                TeamGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getType().equals(GameType.PRESEASON) && t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearTeamAndOpponentWhenSearchingByYearTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearTeamAndOpponentWhenSearchingByYearTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(6).year("2002").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(3).build(),
                TeamGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getTeamId().equals(1) && t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForDatestampTypeAndTeamWhenSearchingByDatestampTypeAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForDatestampTypeAndTeamWhenSearchingByDatestampTypeAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(5).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(7).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(8).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(2).build(),
                TeamGame.builder().gameId(9).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getType().equals(GameType.PRESEASON) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForDatestampTypeAndOpponentWhenSearchingByDatestampTypeAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForDatestampTypeAndOpponentWhenSearchingByDatestampTypeAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(5).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(7).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(8).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(3).build(),
                TeamGame.builder().gameId(9).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getType().equals(GameType.PRESEASON) && t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForDatestampTeamAndOpponentWhenSearchingByDatestampTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForDatestampTeamAndOpponentWhenSearchingByDatestampTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(5).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2000-03-18")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(7).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(8).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(3).build(),
                TeamGame.builder().gameId(9).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getDatestamp().equals(LocalDate.parse("2000-03-16")) && t.getTeamId().equals(1) && t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForTypeTeamAndOpponentWhenSearchingByTypeTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForTypeTeamAndOpponentWhenSearchingByTypeTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(5).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(7).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(8).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(3).build(),
                TeamGame.builder().gameId(9).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getType().equals(GameType.PRESEASON) && t.getTeamId().equals(1) && t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearDatestampAndTypeWhenSearchingByGameYearDatestampAndType() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearDatestampAndTypeWhenSearchingByGameYearDatestampAndType() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(4).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(5).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(6).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(7).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(8).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(9).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(10).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(11).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearDatestampAndTeamWhenSearchingByGameIdYearDatestampAndTeamId() {
        final List<TeamGame> teamGameList = Collections.singletonList(
                // Game ID & Team ID are the key fields, so only one record is possible
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearDatestampAndTeamWhenSearchingByGameIdYearDatestampAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearDatestampAndTeamWhenSearchingByGameIdYearDatestampAndTeamIdAndYearIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearDatestampAndTeamWhenSearchingByGameIdYearDatestampAndTeamIdAndDatestampIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearDatestampAndOpponentWhenSearchingByGameYearDatestampAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearDatestampAndOpponentWhenSearchingByGameYearDatestampAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(4).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(5).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(6).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(7).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(8).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(9).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(10).opponent(3).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(11).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearTypeAndTeamWhenSearchingByGameIdYearTypeAndTeamId() {
        final List<TeamGame> teamGameList = Collections.singletonList(
                // Game ID & Team ID are the key fields, so only one record is possible
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearTypeAndTeamWhenSearchingByGameIdYearTypeAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearTypeAndTeamWhenSearchingByGameIdYearTypeAndTeamIdAndYearIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearTypeAndTeamWhenSearchingByGameIdYearTypeAndTeamIdAndTypeIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearTypeAndOpponentWhenSearchingByGameYearTypeAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearTypeAndOpponentWhenSearchingByGameYearTypeAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(4).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(5).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(6).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(7).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(8).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(9).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(10).opponent(3).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(11).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearTeamAndOpponentWhenSearchingByGameIdYearTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Collections.singletonList(
                // Game ID & Team ID are the key fields, so only one record is possible
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearTeamAndOpponentWhenSearchingByGameIdYearTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getTeamId().equals(1) &&
                        t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearTeamAndOpponentWhenSearchingByGameIdYearTeamIdAndOpponentAndYearIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearTeamAndOpponentWhenSearchingByGameIdYearTeamIdAndOpponentAndOpponentIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForGameDatestampTypeAndTeamWhenSearchingByGameIdDatestampTypeAndTeamId() {
        final List<TeamGame> teamGameList = Collections.singletonList(
                // Game ID & Team ID are the key fields, so only one record is possible
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameDatestampTypeAndTeamWhenSearchingByGameIdDatestampTypeAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameDatestampTypeAndTeamWhenSearchingByGameIdDatestampTypeAndTeamIdAndDatestampIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameDatestampTypeAndTeamWhenSearchingByGameIdDatestampTypeAndTeamIdAndTypeIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForGameDatestampTypeAndOpponentWhenSearchingByGameDatestampTypeAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameDatestampTypeAndOpponentWhenSearchingByGameDatestampTypeAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(4).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(5).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(6).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(7).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(8).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(9).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(10).opponent(3).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(11).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameDatestampTeamAndOpponentWhenSearchingByGameIdDatestampTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Collections.singletonList(
                // Game ID & Team ID are the key fields, so only one record is possible
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameDatestampTeamAndOpponentWhenSearchingByGameIdDatestampTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getTeamId().equals(1) &&
                        t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameDatestampTeamAndOpponentWhenSearchingByGameIdDatestampTeamIdAndOpponentAndDatestampIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameDatestampTeamAndOpponentWhenSearchingByGameIdDatestampTeamIdAndOpponentAndOpponentIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForGameTypeTeamAndOpponentWhenSearchingByGameIdTypeTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Collections.singletonList(
                // Game ID & Team ID are the key fields, so only one record is possible
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameTypeTeamAndOpponentWhenSearchingByGameIdTypeTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getTeamId().equals(1) &&
                        t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameTypeTeamAndOpponentWhenSearchingByGameIdTypeTeamIdAndOpponentAndTypeIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameTypeTeamAndOpponentWhenSearchingByGameIdTypeTeamIdAndOpponentAndOpponentIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForYearDatestampTypeAndTeamWhenSearchingByYearDatestampTypeAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearDatestampTypeAndTeamWhenSearchingByYearDatestampTypeAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(10).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(2).build(),
                TeamGame.builder().gameId(11).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(3).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearDatestampTypeAndOpponentWhenSearchingByYearDatestampTypeAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearDatestampTypeAndOpponentWhenSearchingByYearDatestampTypeAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(10).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(3).build(),
                TeamGame.builder().gameId(11).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearDatestampTeamAndOpponentWhenSearchingByYearDatestampTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearDatestampTeamAndOpponentWhenSearchingByYearDatestampTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(10).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(3).build(),
                TeamGame.builder().gameId(11).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getTeamId().equals(1) &&
                        t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearTypeTeamAndOpponentWhenSearchingByYearTypeTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearTypeTeamAndOpponentWhenSearchingByYearTypeTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(7).year("2002").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(8).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(10).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(3).build(),
                TeamGame.builder().gameId(11).year("2000").datestamp(LocalDate.parse("2000-03-18")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getTeamId().equals(1) &&
                        t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForDatestampTypeTeamAndOpponentWhenSearchingByDatestampTypeTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForDatestampTypeTeamAndOpponentWhenSearchingByDatestampTypeTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(6).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(7).year("2001").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(8).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(9).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(10).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(3).build(),
                TeamGame.builder().gameId(11).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getTeamId().equals(1) &&
                        t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearDatestampTypeAndTeamWhenSearchingByGameIdYearDatestampTypeAndTeamId() {
        final List<TeamGame> teamGameList = Collections.singletonList(
                // Game ID & Team ID are the key fields, so only one record is possible
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearDatestampTypeAndTeamWhenSearchingByGameIdYearDatestampTypeAndTeamId() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearDatestampTypeAndTeamWhenSearchingByGameIdYearDatestampTypeAndTeamIdAndYearIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearDatestampTypeAndTeamWhenSearchingByGameIdYearDatestampTypeAndTeamIdAndDatestampIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearDatestampTypeAndTeamWhenSearchingByGameIdYearDatestampTypeAndTeamIdAndTypeIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearDatestampTypeAndOpponentWhenSearchingByGameIdYearDatestampTypeAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearDatestampTypeAndOpponentWhenSearchingByGameIdYearDatestampTypeAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(4).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(5).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(6).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(7).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(8).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(9).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).teamId(10).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(11).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(12).opponent(3).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(13).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearDatestampTeamAndOpponentWhenSearchingByGameIdYearDatestampTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Collections.singletonList(
                // Game ID & Team ID are the key fields, so only one record is possible
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearDatestampTeamAndOpponentWhenSearchingByGameIdYearDatestampTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getTeamId().equals(1) &&
                        t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearDatestampTeamAndOpponentWhenSearchingByGameIdYearDatestampTeamIdAndOpponentAndYearIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearDatestampTeamAndOpponentWhenSearchingByGameIdYearDatestampTeamIdAndOpponentAndDatestampIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearDatestampTeamAndOpponentWhenSearchingByGameIdYearDatestampTeamIdAndOpponentAndOpponentIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForGameYearTypeTeamAndOpponentWhenSearchingByGameIdYearTypeTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Collections.singletonList(
                // Game ID & Team ID are the key fields, so only one record is possible
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameYearTypeTeamAndOpponentWhenSearchingByGameIdYearTypeTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getTeamId().equals(1) &&
                        t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearTypeTeamAndOpponentWhenSearchingByGameIdYearTypeTeamIdAndOpponentAndYearIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearTypeTeamAndOpponentWhenSearchingByGameIdYearTypeTeamIdAndOpponentAndTypeIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameYearTypeTeamAndOpponentWhenSearchingByGameIdYearTypeTeamIdAndOpponentAndOpponentIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForGameDatestampTypeTeamAndOpponentWhenSearchingByGameIdDatestampTypeTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Collections.singletonList(
                // Game ID & Team ID are the key fields, so only one record is possible
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForGameDatestampTypeTeamAndOpponentWhenSearchingByGameIdDatestampTypeTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getTeamId().equals(1) &&
                        t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForGameDatestampTypeTeamAndOpponentWhenSearchingByGameIdDatestampTypeTeamIdAndOpponentAndDatestampIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameDatestampTypeTeamAndOpponentWhenSearchingByGameIdDatestampTypeTeamIdAndOpponentAndTypeIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnNoEntriesForGameDatestampTypeTeamAndOpponentWhenSearchingByGameIdDatestampTypeTeamIdAndOpponentAndOpponentIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForYearDatestampTypeTeamAndOpponentWhenSearchingByYearDatestampTypeTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearDatestampTypeTeamAndOpponentWhenSearchingByYearDatestampTypeTeamIdAndOpponent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(3).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(4).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(5).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(6).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(7).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(8).year("2002").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(9).year("2000").datestamp(LocalDate.parse("2000-04-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(10).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.POSTSEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(11).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(3).opponent(1).build(),
                TeamGame.builder().gameId(12).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(3).build(),
                TeamGame.builder().gameId(13).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getTeamId().equals(1) &&
                        t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnMatchingEntryWhenSearchingByAllParameters() {
        final List<TeamGame> teamGameList = Collections.singletonList(
                // Game ID & Team ID are the key fields, so only one record is possible
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnMatchingEntryWhenSearchingByAllParameters() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getGameId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getDatestamp().equals(LocalDate.parse("2000-03-16")) &&
                        t.getType().equals(GameType.PRESEASON) &&
                        t.getTeamId().equals(1) &&
                        t.getOpponent().equals(1)
        ).count());
    }

    @Test
    public void shouldNotReturnMatchingEntriyWhenSearchingByAllParametersAndYearIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldNotReturnMatchingEntriyWhenSearchingByAllParametersAndDatestampIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-17")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldNotReturnMatchingEntriyWhenSearchingByAllParametersAndTypeIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(1).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldNotReturnMatchingEntriyWhenSearchingByAllParametersAndOpponentIsDifferent() {
        final List<TeamGame> teamGameList = Arrays.asList(
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(2).build(),
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(2).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(2).opponent(2).build()
        );

        repository.saveAll(teamGameList);

        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(1)
                .year("2000")
                .datestamp(LocalDate.parse("2000-03-16"))
                .type(GameType.PRESEASON)
                .teamId(1)
                .opponent(1)
                .build();

        final List<TeamGameResponse> result = searchService.fetchAll(request);

        assertEquals(0, result.size());
    }
}