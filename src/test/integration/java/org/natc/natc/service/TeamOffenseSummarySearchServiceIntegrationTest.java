package org.natc.natc.service;

import org.junit.jupiter.api.Test;
import org.natc.natc.entity.domain.GameType;
import org.natc.natc.entity.domain.TeamOffenseSummary;
import org.natc.natc.entity.request.TeamOffenseSummaryRequest;
import org.natc.natc.entity.response.TeamOffenseSummaryResponse;
import org.natc.natc.repository.TeamOffenseSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TeamOffenseSummarySearchServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private TeamOffenseSummaryRepository repository;

    @Autowired
    private TeamOffenseSummarySearchService searchService;

    @Test
    public void shouldReturnATeamOffenseSummaryFromTheDatabaseMappedToAResponse() {
        final TeamOffenseSummary teamOffenseSummary = TeamOffenseSummary.builder()
                .year("2004")
                .teamId(16)
                .type(GameType.REGULAR_SEASON.getValue())
                .games(100)
                .attempts(3000)
                .goals(1200)
                .score(4444)
                .build();

        repository.save(teamOffenseSummary);

        final List<TeamOffenseSummaryResponse> result = searchService.fetchAll(new TeamOffenseSummaryRequest());

        assertEquals(1, result.size());

        final TeamOffenseSummaryResponse response = result.get(0);

        assertEquals(teamOffenseSummary.getYear(), response.getYear());
        assertEquals(teamOffenseSummary.getTeamId(), response.getTeamId());
        assertEquals(GameType.REGULAR_SEASON, response.getType());
        assertEquals(teamOffenseSummary.getGames(), response.getGames());
        assertEquals(teamOffenseSummary.getAttempts(), response.getAttempts());
        assertEquals(teamOffenseSummary.getGoals(), response.getGoals());
        assertEquals(teamOffenseSummary.getScore(), response.getScore());
    }

    @Test
    public void shouldMapAllTeamFieldsToTheResponse() {
        final TeamOffenseSummary teamOffenseSummary = TeamOffenseSummary.builder()
                .year("2016")
                .type(GameType.REGULAR_SEASON.getValue())
                .teamId(123)
                .games(99)
                .possessions(234)
                .possessionTime(987)
                .attempts(345)
                .goals(321)
                .turnovers(111)
                .steals(222)
                .penalties(333)
                .offensivePenalties(77)
                .penaltyShotsAttempted(88)
                .penaltyShotsMade(99)
                .overtimePenaltyShotsAttempted(11)
                .overtimePenaltyShotsMade(22)
                .score(678)
                .build();

        repository.save(teamOffenseSummary);

        final List<TeamOffenseSummaryResponse> result = searchService.fetchAll(new TeamOffenseSummaryRequest());

        assertEquals(1, result.size());

        final TeamOffenseSummaryResponse response = result.get(0);

        assertNotNull(response.getYear());
        assertNotNull(response.getType());
        assertNotNull(response.getTeamId());
        assertNotNull(response.getGames());
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
        assertNotNull(response.getScore());
    }

    @Test
    public void shouldReturnAllEntriesWhenSearchingWithoutValues() {
        final List<TeamOffenseSummary> teamOffenseSummaries = Arrays.asList(
                TeamOffenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(2).build(),
                TeamOffenseSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).teamId(3).build()
        );

        repository.saveAll(teamOffenseSummaries);

        final List<TeamOffenseSummaryResponse> result = searchService.fetchAll(new TeamOffenseSummaryRequest());

        assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnNoEntriesWhenSearchingGivenNoDataInTheDatabase() {
        final List<TeamOffenseSummaryResponse> result = searchService.fetchAll(new TeamOffenseSummaryRequest());

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForYearWhenSearchingByYear() {
        final List<TeamOffenseSummary> teamOffenseSummaries = Arrays.asList(
                TeamOffenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(2).build(),
                TeamOffenseSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).teamId(3).build()
        );

        repository.saveAll(teamOffenseSummaries);

        final TeamOffenseSummaryRequest request = TeamOffenseSummaryRequest.builder()
                .year("2000")
                .build();

        final List<TeamOffenseSummaryResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearWhenSearchingByYear() {
        final List<TeamOffenseSummary> teamOffenseSummaries = Arrays.asList(
                TeamOffenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(2).build(),
                TeamOffenseSummary.builder().year("2003").type(GameType.REGULAR_SEASON.getValue()).teamId(2).build(),
                TeamOffenseSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).teamId(3).build()
        );

        repository.saveAll(teamOffenseSummaries);

        final TeamOffenseSummaryRequest request = TeamOffenseSummaryRequest.builder()
                .year("2000")
                .build();

        final List<TeamOffenseSummaryResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getYear().equals("2000")).count());
    }

    @Test
    public void shouldReturnAllEntriesForTeamWhenSearchingByTeamId() {
        final List<TeamOffenseSummary> teamOffenseSummaries = Arrays.asList(
                TeamOffenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).teamId(1).build()
        );

        repository.saveAll(teamOffenseSummaries);

        final TeamOffenseSummaryRequest request = TeamOffenseSummaryRequest.builder()
                .teamId(1)
                .build();

        final List<TeamOffenseSummaryResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForTeamWhenSearchingByTeamId() {
        final List<TeamOffenseSummary> teamOffenseSummaries = Arrays.asList(
                TeamOffenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(2).build(),
                TeamOffenseSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(3).build(),
                TeamOffenseSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).teamId(1).build()
        );

        repository.saveAll(teamOffenseSummaries);

        final TeamOffenseSummaryRequest request = TeamOffenseSummaryRequest.builder()
                .teamId(1)
                .build();

        final List<TeamOffenseSummaryResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getTeamId().equals(1)).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
        final List<TeamOffenseSummary> teamOffenseSummaries = Arrays.asList(
                TeamOffenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).teamId(1).build()
        );

        repository.saveAll(teamOffenseSummaries);

        final TeamOffenseSummaryRequest request = TeamOffenseSummaryRequest.builder()
                .year("2000")
                .teamId(1)
                .build();

        final List<TeamOffenseSummaryResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
        final List<TeamOffenseSummary> teamOffenseSummaries = Arrays.asList(
                TeamOffenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(2).build(),
                TeamOffenseSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2002").type(GameType.REGULAR_SEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(3).build(),
                TeamOffenseSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).teamId(1).build()
        );

        repository.saveAll(teamOffenseSummaries);

        final TeamOffenseSummaryRequest request = TeamOffenseSummaryRequest.builder()
                .year("2000")
                .teamId(1)
                .build();

        final List<TeamOffenseSummaryResponse> result = searchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getTeamId().equals(1)
        ).count());
    }
}