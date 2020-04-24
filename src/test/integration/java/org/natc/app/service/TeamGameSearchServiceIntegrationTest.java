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
    public void shouldMapAllTeamFieldsToTheResponse() {
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
}