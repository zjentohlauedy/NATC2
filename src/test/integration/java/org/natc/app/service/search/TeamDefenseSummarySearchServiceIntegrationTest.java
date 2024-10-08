package org.natc.app.service.search;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.TeamDefenseSummary;
import org.natc.app.entity.request.TeamDefenseSummarySearchRequest;
import org.natc.app.entity.response.TeamDefenseSummaryResponse;
import org.natc.app.repository.TeamDefenseSummaryRepository;
import org.natc.app.service.NATCServiceIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TeamDefenseSummarySearchServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private TeamDefenseSummaryRepository repository;
    
    @Autowired
    private TeamDefenseSummarySearchService searchService;

    @Nested
    class FetchAll {

        @Test
        void shouldReturnATeamDefenseSummaryFromTheDatabaseMappedToAResponse() {
            final TeamDefenseSummary teamDefenseSummary = TeamDefenseSummary.builder()
                    .year("2020")
                    .teamId(32)
                    .type(GameType.REGULAR_SEASON.getValue())
                    .games(87)
                    .attempts(1214)
                    .goals(999)
                    .score(3333)
                    .build();

            repository.save(teamDefenseSummary);

            final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(new TeamDefenseSummarySearchRequest());

            assertEquals(1, result.size());

            final TeamDefenseSummaryResponse response = result.getFirst();

            assertEquals(teamDefenseSummary.getYear(), response.getYear());
            assertEquals(teamDefenseSummary.getTeamId(), response.getTeamId());
            assertEquals(GameType.REGULAR_SEASON, response.getType());
            assertEquals(teamDefenseSummary.getGames(), response.getGames());
            assertEquals(teamDefenseSummary.getAttempts(), response.getAttempts());
            assertEquals(teamDefenseSummary.getGoals(), response.getGoals());
            assertEquals(teamDefenseSummary.getScore(), response.getScore());
        }

        @Test
        void shouldMapAllTeamFieldsToTheResponse() {
            final TeamDefenseSummary teamDefenseSummary = TeamDefenseSummary.builder()
                    .year("1997")
                    .type(GameType.REGULAR_SEASON.getValue())
                    .teamId(123)
                    .games(83)
                    .possessions(321)
                    .possessionTime(999)
                    .attempts(444)
                    .goals(333)
                    .turnovers(55)
                    .steals(111)
                    .penalties(99)
                    .offensivePenalties(66)
                    .penaltyShotsAttempted(77)
                    .penaltyShotsMade(44)
                    .overtimePenaltyShotsAttempted(17)
                    .overtimePenaltyShotsMade(12)
                    .score(666)
                    .build();

            repository.save(teamDefenseSummary);

            final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(new TeamDefenseSummarySearchRequest());

            assertEquals(1, result.size());

            final TeamDefenseSummaryResponse response = result.getFirst();

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
        void shouldReturnAllEntriesWhenSearchingWithoutValues() {
            final List<TeamDefenseSummary> teamDefenseSummaries = List.of(
                    TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                    TeamDefenseSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(2).build(),
                    TeamDefenseSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).teamId(3).build()
            );

            repository.saveAll(teamDefenseSummaries);

            final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(new TeamDefenseSummarySearchRequest());

            assertEquals(3, result.size());
        }

        @Test
        void shouldReturnNoEntriesWhenSearchingGivenNoDataInTheDatabase() {
            final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(new TeamDefenseSummarySearchRequest());

            assertEquals(0, result.size());
        }

        @Nested
        class WithOneSearchParameter {

            @Test
            void shouldReturnAllEntriesForYearWhenSearchingByYear() {
                final List<TeamDefenseSummary> teamDefenseSummaries = List.of(
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(2).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).teamId(3).build()
                );

                repository.saveAll(teamDefenseSummaries);

                final TeamDefenseSummarySearchRequest request = TeamDefenseSummarySearchRequest.builder()
                        .year("2000")
                        .build();

                final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForYearWhenSearchingByYear() {
                final List<TeamDefenseSummary> teamDefenseSummaries = List.of(
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(2).build(),
                        TeamDefenseSummary.builder().year("2002").type(GameType.REGULAR_SEASON.getValue()).teamId(2).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).teamId(3).build()
                );

                repository.saveAll(teamDefenseSummaries);

                final TeamDefenseSummarySearchRequest request = TeamDefenseSummarySearchRequest.builder()
                        .year("2000")
                        .build();

                final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t -> t.getYear().equals("2000")).count());
            }

            @Test
            void shouldReturnAllEntriesForTypeWhenSearchingByType() {
                final List<TeamDefenseSummary> teamDefenseSummaries = List.of(
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).teamId(2).build(),
                        TeamDefenseSummary.builder().year("2002").type(GameType.PRESEASON.getValue()).teamId(3).build()
                );

                repository.saveAll(teamDefenseSummaries);

                final TeamDefenseSummarySearchRequest request = TeamDefenseSummarySearchRequest.builder()
                        .type(GameType.PRESEASON)
                        .build();

                final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTypeWhenSearchingByType() {
                final List<TeamDefenseSummary> teamDefenseSummaries = List.of(
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).teamId(2).build(),
                        TeamDefenseSummary.builder().year("2001").type(GameType.POSTSEASON.getValue()).teamId(2).build(),
                        TeamDefenseSummary.builder().year("2002").type(GameType.PRESEASON.getValue()).teamId(3).build()
                );

                repository.saveAll(teamDefenseSummaries);

                final TeamDefenseSummarySearchRequest request = TeamDefenseSummarySearchRequest.builder()
                        .type(GameType.PRESEASON)
                        .build();

                final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t -> t.getType().equals(GameType.PRESEASON)).count());
            }

            @Test
            void shouldReturnAllEntriesForTeamWhenSearchingByTeamId() {
                final List<TeamDefenseSummary> teamDefenseSummaries = List.of(
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).teamId(1).build()
                );

                repository.saveAll(teamDefenseSummaries);

                final TeamDefenseSummarySearchRequest request = TeamDefenseSummarySearchRequest.builder()
                        .teamId(1)
                        .build();

                final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTeamWhenSearchingByTeamId() {
                final List<TeamDefenseSummary> teamDefenseSummaries = List.of(
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(2).build(),
                        TeamDefenseSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(3).build(),
                        TeamDefenseSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).teamId(1).build()
                );

                repository.saveAll(teamDefenseSummaries);

                final TeamDefenseSummarySearchRequest request = TeamDefenseSummarySearchRequest.builder()
                        .teamId(1)
                        .build();

                final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t -> t.getTeamId().equals(1)).count());
            }
        }

        @Nested
        class WithTwoSearchParameters {

            @Test
            void shouldReturnAllEntriesForYearAndTypeWhenSearchingByYearAndType() {
                final List<TeamDefenseSummary> teamDefenseSummaries = List.of(
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(2).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(3).build()
                );

                repository.saveAll(teamDefenseSummaries);

                final TeamDefenseSummarySearchRequest request = TeamDefenseSummarySearchRequest.builder()
                        .year("2000")
                        .type(GameType.PRESEASON)
                        .build();

                final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForYearAndTypeWhenSearchingByYearAndType() {
                final List<TeamDefenseSummary> teamDefenseSummaries = List.of(
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(2).build(),
                        TeamDefenseSummary.builder().year("2002").type(GameType.PRESEASON.getValue()).teamId(2).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).teamId(2).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(3).build()
                );

                repository.saveAll(teamDefenseSummaries);

                final TeamDefenseSummarySearchRequest request = TeamDefenseSummarySearchRequest.builder()
                        .year("2000")
                        .type(GameType.PRESEASON)
                        .build();

                final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getYear().equals("2000") && t.getType().equals(GameType.PRESEASON)
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
                final List<TeamDefenseSummary> teamDefenseSummaries = List.of(
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).teamId(1).build()
                );

                repository.saveAll(teamDefenseSummaries);

                final TeamDefenseSummarySearchRequest request = TeamDefenseSummarySearchRequest.builder()
                        .year("2000")
                        .teamId(1)
                        .build();

                final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
                final List<TeamDefenseSummary> teamDefenseSummaries = List.of(
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(2).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2002").type(GameType.REGULAR_SEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(3).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.POSTSEASON.getValue()).teamId(1).build()
                );

                repository.saveAll(teamDefenseSummaries);

                final TeamDefenseSummarySearchRequest request = TeamDefenseSummarySearchRequest.builder()
                        .year("2000")
                        .teamId(1)
                        .build();

                final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getYear().equals("2000") && t.getTeamId().equals(1)
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForTypeAndTeamWhenSearchingByTypeAndTeamId() {
                final List<TeamDefenseSummary> teamDefenseSummaries = List.of(
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2002").type(GameType.PRESEASON.getValue()).teamId(1).build()
                );

                repository.saveAll(teamDefenseSummaries);

                final TeamDefenseSummarySearchRequest request = TeamDefenseSummarySearchRequest.builder()
                        .type(GameType.PRESEASON)
                        .teamId(1)
                        .build();

                final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTypeAndTeamWhenSearchingByTypeAndTeamId() {
                final List<TeamDefenseSummary> teamDefenseSummaries = List.of(
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(2).build(),
                        TeamDefenseSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2001").type(GameType.POSTSEASON.getValue()).teamId(1).build(),
                        TeamDefenseSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).teamId(3).build(),
                        TeamDefenseSummary.builder().year("2002").type(GameType.PRESEASON.getValue()).teamId(1).build()
                );

                repository.saveAll(teamDefenseSummaries);

                final TeamDefenseSummarySearchRequest request = TeamDefenseSummarySearchRequest.builder()
                        .type(GameType.PRESEASON)
                        .teamId(1)
                        .build();

                final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getType().equals(GameType.PRESEASON) && t.getTeamId().equals(1)
                ).count());
            }
        }

        @Test
        void shouldReturnMatchingEntryWhenSearchingByAllParameters() {
            final List<TeamDefenseSummary> teamDefenseSummaries = Collections.singletonList(
                    TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build()
            );

            repository.saveAll(teamDefenseSummaries);

            final TeamDefenseSummarySearchRequest request = TeamDefenseSummarySearchRequest.builder()
                    .year("2000")
                    .type(GameType.PRESEASON)
                    .teamId(1)
                    .build();

            final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(request);

            assertEquals(1, result.size());
        }

        @Test
        void shouldOnlyReturnMatchingEntryWhenSearchingByAllParameters() {
            final List<TeamDefenseSummary> teamDefenseSummaries = List.of(
                    TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                    TeamDefenseSummary.builder().year("2001").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                    TeamDefenseSummary.builder().year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(1).build(),
                    TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(2).build(),
                    TeamDefenseSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).teamId(3).build()
            );

            repository.saveAll(teamDefenseSummaries);

            final TeamDefenseSummarySearchRequest request = TeamDefenseSummarySearchRequest.builder()
                    .year("2000")
                    .type(GameType.PRESEASON)
                    .teamId(1)
                    .build();

            final List<TeamDefenseSummaryResponse> result = searchService.fetchAll(request);

            assertEquals(1, result.size());
            assertEquals(1, result.stream().filter(t ->
                    t.getYear().equals("2000") && t.getType().equals(GameType.PRESEASON) && t.getTeamId().equals(1)
            ).count());
        }
    }
}