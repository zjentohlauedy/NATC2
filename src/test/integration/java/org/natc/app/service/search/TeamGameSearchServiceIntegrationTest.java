package org.natc.app.service.search;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.TeamGame;
import org.natc.app.entity.request.TeamGameSearchRequest;
import org.natc.app.entity.response.TeamGameResponse;
import org.natc.app.repository.TeamGameRepository;
import org.natc.app.service.NATCServiceIntegrationTest;
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

    @Nested
    class FetchAll {

        @Test
        void shouldReturnATeamGameFromTheDatabaseMappedToAResponse() {
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

            final TeamGameResponse response = result.getFirst();

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
        void shouldMapAllTeamGameFieldsToTheResponse() {
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

            final TeamGameResponse response = result.getFirst();

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
        void shouldReturnAllEntriesWhenSearchingWithoutValues() {
            final List<TeamGame> teamGameList = List.of(
                    TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                    TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                    TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
            );

            repository.saveAll(teamGameList);

            final List<TeamGameResponse> result = searchService.fetchAll(new TeamGameSearchRequest());

            assertEquals(3, result.size());
        }

        @Test
        void shouldReturnNoEntriesWhenSearchingGivenNoDataInTheDatabase() {
            final List<TeamGameResponse> result = searchService.fetchAll(new TeamGameSearchRequest());

            assertEquals(0, result.size());
        }

        @Nested
        class WithOneSearchParameter {

            @Test
            void shouldReturnAllEntriesForGameWhenSearchingByGameId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameWhenSearchingByGameId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForYearWhenSearchingByYear() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearWhenSearchingByYear() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForDatestampWhenSearchingByDatestamp() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForDatestampWhenSearchingByDatestamp() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForTypeWhenSearchingByType() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForTypeWhenSearchingByType() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForTeamWhenSearchingByTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForTeamWhenSearchingByTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForOpponentWhenSearchingByOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForOpponentWhenSearchingByOpponent() {
                final List<TeamGame> teamGameList = List.of(
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

        @Nested
        class WithTwoSearchParameters {

            @Test
            void shouldReturnAllEntriesForGameAndYearWhenSearchingByGameIdAndYear() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameAndYearWhenSearchingByGameIdAndYear() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameAndDatestampWhenSearchingByGameIdAndDatestamp() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameAndDatestampWhenSearchingByGameIdAndDatestamp() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameAndTypeWhenSearchingByGameIdAndType() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameAndTypeWhenSearchingByGameIdAndType() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameAndTeamWhenSearchingByGameIdAndTeamId() {
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
            void shouldOnlyReturnEntriesForGameAndTeamWhenSearchingByGameIdAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameAndOpponentWhenSearchingByGameIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameAndOpponentWhenSearchingByGameIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForYearAndDatestampWhenSearchingByYearAndDatestamp() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearAndDatestampWhenSearchingByYearAndDatestamp() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForYearAndTypeWhenSearchingByYearAndType() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearAndTypeWhenSearchingByYearAndType() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForYearAndOpponentWhenSearchingByYearAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearAndOpponentWhenSearchingByYearAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForDatestampAndTypeWhenSearchingByDatestampAndType() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForDatestampAndTypeWhenSearchingByDatestampAndType() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForDatestampAndTeamWhenSearchingByDatestampAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForDatestampAndTeamWhenSearchingByDatestampAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForDatestampAndOpponentWhenSearchingByDatestampAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForDatestampAndOpponentWhenSearchingByDatestampAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForTypeAndTeamWhenSearchingByTypeAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForTypeAndTeamWhenSearchingByTypeAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForTypeAndOpponentWhenSearchingByTypeAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForTypeAndOpponentWhenSearchingByTypeAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForTeamAndOpponentWhenSearchingByTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForTeamAndOpponentWhenSearchingByTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
        }

        @Nested
        class WithThreeSearchParameters {

            @Test
            void shouldReturnAllEntriesForGameYearAndDatestampWhenSearchingByGameIdYearAndDatestamp() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameYearAndDatestampWhenSearchingByGameIdYearAndDatestamp() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearAndTypeWhenSearchingByGameIdYearAndType() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameYearAndTypeWhenSearchingByGameIdYearAndType() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearAndTeamWhenSearchingByGameIdYearAndTeamId() {
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
            void shouldOnlyReturnEntriesForGameYearAndTeamWhenSearchingByGameIdYearAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearAndTeamWhenSearchingByGameIdYearAndTeamIdAndYearIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearAndOpponentWhenSearchingByGameIdYearAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameYearAndOpponentWhenSearchingByGameIdYearAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameDatestampAndTypeWhenSearchingByGameIdDatestampAndType() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameDatestampAndTypeWhenSearchingByGameIdDatestampAndType() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameDatestampAndTeamWhenSearchingByGameIdDatestampAndTeamId() {
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
            void shouldOnlyReturnEntriesForGameDatestampAndTeamWhenSearchingByGameIdDatestampAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameDatestampAndTeamWhenSearchingByGameIdDatestampAndTeamIdAndDatestampIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameDatestampAndOpponentWhenSearchingByGameIdDatestampAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameDatestampAndOpponentWhenSearchingByGameIdDatestampAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameTypeAndTeamWhenSearchingByGameIdTypeAndTeamId() {
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
            void shouldOnlyReturnEntriesForGameTypeAndTeamWhenSearchingByGameIdTypeAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameTypeAndTeamWhenSearchingByGameIdTypeAndTeamIdAndTypeIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameTypeAndOpponentWhenSearchingByGameIdTypeAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameTypeAndOpponentWhenSearchingByGameIdTypeAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameTeamAndOpponentWhenSearchingByGameIdTeamIdAndOpponent() {
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
            void shouldOnlyReturnEntriesForGameTeamAndOpponentWhenSearchingByGameIdTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameTeamAndOpponentWhenSearchingByGameIdTeamIdAndOpponentAndOpponentIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForYearDatestampAndTypeWhenSearchingByYearDatestampAndType() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearDatestampAndTypeWhenSearchingByYearDatestampAndType() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForYearDatestampAndTeamWhenSearchingByYearDatestampAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearDatestampAndTeamWhenSearchingByYearDatestampAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForYearDatestampAndOpponentWhenSearchingByYearDatestampAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearDatestampAndOpponentWhenSearchingByYearDatestampAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForYearTypeAndTeamWhenSearchingByYearTypeAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearTypeAndTeamWhenSearchingByYearTypeAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForYearTypeAndOpponentWhenSearchingByYearTypeAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearTypeAndTeamWhenSearchingByYearTypeAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForYearTeamAndOpponentWhenSearchingByYearTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearTeamAndOpponentWhenSearchingByYearTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForDatestampTypeAndTeamWhenSearchingByDatestampTypeAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForDatestampTypeAndTeamWhenSearchingByDatestampTypeAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForDatestampTypeAndOpponentWhenSearchingByDatestampTypeAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForDatestampTypeAndOpponentWhenSearchingByDatestampTypeAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForDatestampTeamAndOpponentWhenSearchingByDatestampTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForDatestampTeamAndOpponentWhenSearchingByDatestampTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForTypeTeamAndOpponentWhenSearchingByTypeTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForTypeTeamAndOpponentWhenSearchingByTypeTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
        }

        @Nested
        class WithFourSearchParameters {

            @Test
            void shouldReturnAllEntriesForGameYearDatestampAndTypeWhenSearchingByGameYearDatestampAndType() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameYearDatestampAndTypeWhenSearchingByGameYearDatestampAndType() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearDatestampAndTeamWhenSearchingByGameIdYearDatestampAndTeamId() {
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
            void shouldOnlyReturnEntriesForGameYearDatestampAndTeamWhenSearchingByGameIdYearDatestampAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearDatestampAndTeamWhenSearchingByGameIdYearDatestampAndTeamIdAndYearIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearDatestampAndTeamWhenSearchingByGameIdYearDatestampAndTeamIdAndDatestampIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearDatestampAndOpponentWhenSearchingByGameYearDatestampAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameYearDatestampAndOpponentWhenSearchingByGameYearDatestampAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearTypeAndTeamWhenSearchingByGameIdYearTypeAndTeamId() {
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
            void shouldOnlyReturnEntriesForGameYearTypeAndTeamWhenSearchingByGameIdYearTypeAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearTypeAndTeamWhenSearchingByGameIdYearTypeAndTeamIdAndYearIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearTypeAndTeamWhenSearchingByGameIdYearTypeAndTeamIdAndTypeIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearTypeAndOpponentWhenSearchingByGameYearTypeAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameYearTypeAndOpponentWhenSearchingByGameYearTypeAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearTeamAndOpponentWhenSearchingByGameIdYearTeamIdAndOpponent() {
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
            void shouldOnlyReturnEntriesForGameYearTeamAndOpponentWhenSearchingByGameIdYearTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearTeamAndOpponentWhenSearchingByGameIdYearTeamIdAndOpponentAndYearIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearTeamAndOpponentWhenSearchingByGameIdYearTeamIdAndOpponentAndOpponentIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameDatestampTypeAndTeamWhenSearchingByGameIdDatestampTypeAndTeamId() {
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
            void shouldOnlyReturnEntriesForGameDatestampTypeAndTeamWhenSearchingByGameIdDatestampTypeAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameDatestampTypeAndTeamWhenSearchingByGameIdDatestampTypeAndTeamIdAndDatestampIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameDatestampTypeAndTeamWhenSearchingByGameIdDatestampTypeAndTeamIdAndTypeIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameDatestampTypeAndOpponentWhenSearchingByGameDatestampTypeAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameDatestampTypeAndOpponentWhenSearchingByGameDatestampTypeAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameDatestampTeamAndOpponentWhenSearchingByGameIdDatestampTeamIdAndOpponent() {
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
            void shouldOnlyReturnEntriesForGameDatestampTeamAndOpponentWhenSearchingByGameIdDatestampTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameDatestampTeamAndOpponentWhenSearchingByGameIdDatestampTeamIdAndOpponentAndDatestampIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameDatestampTeamAndOpponentWhenSearchingByGameIdDatestampTeamIdAndOpponentAndOpponentIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameTypeTeamAndOpponentWhenSearchingByGameIdTypeTeamIdAndOpponent() {
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
            void shouldOnlyReturnEntriesForGameTypeTeamAndOpponentWhenSearchingByGameIdTypeTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameTypeTeamAndOpponentWhenSearchingByGameIdTypeTeamIdAndOpponentAndTypeIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameTypeTeamAndOpponentWhenSearchingByGameIdTypeTeamIdAndOpponentAndOpponentIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForYearDatestampTypeAndTeamWhenSearchingByYearDatestampTypeAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearDatestampTypeAndTeamWhenSearchingByYearDatestampTypeAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForYearDatestampTypeAndOpponentWhenSearchingByYearDatestampTypeAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearDatestampTypeAndOpponentWhenSearchingByYearDatestampTypeAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForYearDatestampTeamAndOpponentWhenSearchingByYearDatestampTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearDatestampTeamAndOpponentWhenSearchingByYearDatestampTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForYearTypeTeamAndOpponentWhenSearchingByYearTypeTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearTypeTeamAndOpponentWhenSearchingByYearTypeTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForDatestampTypeTeamAndOpponentWhenSearchingByDatestampTypeTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForDatestampTypeTeamAndOpponentWhenSearchingByDatestampTypeTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
        }

        @Nested
        class WithFiveSearchParameters {

            @Test
            void shouldReturnAllEntriesForGameYearDatestampTypeAndTeamWhenSearchingByGameIdYearDatestampTypeAndTeamId() {
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
            void shouldOnlyReturnEntriesForGameYearDatestampTypeAndTeamWhenSearchingByGameIdYearDatestampTypeAndTeamId() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearDatestampTypeAndTeamWhenSearchingByGameIdYearDatestampTypeAndTeamIdAndYearIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearDatestampTypeAndTeamWhenSearchingByGameIdYearDatestampTypeAndTeamIdAndDatestampIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearDatestampTypeAndTeamWhenSearchingByGameIdYearDatestampTypeAndTeamIdAndTypeIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearDatestampTypeAndOpponentWhenSearchingByGameIdYearDatestampTypeAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameYearDatestampTypeAndOpponentWhenSearchingByGameIdYearDatestampTypeAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearDatestampTeamAndOpponentWhenSearchingByGameIdYearDatestampTeamIdAndOpponent() {
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
            void shouldOnlyReturnEntriesForGameYearDatestampTeamAndOpponentWhenSearchingByGameIdYearDatestampTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearDatestampTeamAndOpponentWhenSearchingByGameIdYearDatestampTeamIdAndOpponentAndYearIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearDatestampTeamAndOpponentWhenSearchingByGameIdYearDatestampTeamIdAndOpponentAndDatestampIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearDatestampTeamAndOpponentWhenSearchingByGameIdYearDatestampTeamIdAndOpponentAndOpponentIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearTypeTeamAndOpponentWhenSearchingByGameIdYearTypeTeamIdAndOpponent() {
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
            void shouldOnlyReturnEntriesForGameYearTypeTeamAndOpponentWhenSearchingByGameIdYearTypeTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearTypeTeamAndOpponentWhenSearchingByGameIdYearTypeTeamIdAndOpponentAndYearIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearTypeTeamAndOpponentWhenSearchingByGameIdYearTypeTeamIdAndOpponentAndTypeIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearTypeTeamAndOpponentWhenSearchingByGameIdYearTypeTeamIdAndOpponentAndOpponentIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForGameDatestampTypeTeamAndOpponentWhenSearchingByGameIdDatestampTypeTeamIdAndOpponent() {
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
            void shouldOnlyReturnEntriesForGameDatestampTypeTeamAndOpponentWhenSearchingByGameIdDatestampTypeTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameDatestampTypeTeamAndOpponentWhenSearchingByGameIdDatestampTypeTeamIdAndOpponentAndDatestampIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameDatestampTypeTeamAndOpponentWhenSearchingByGameIdDatestampTypeTeamIdAndOpponentAndTypeIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnNoEntriesForGameDatestampTypeTeamAndOpponentWhenSearchingByGameIdDatestampTypeTeamIdAndOpponentAndOpponentIsDifferent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldReturnAllEntriesForYearDatestampTypeTeamAndOpponentWhenSearchingByYearDatestampTypeTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearDatestampTypeTeamAndOpponentWhenSearchingByYearDatestampTypeTeamIdAndOpponent() {
                final List<TeamGame> teamGameList = List.of(
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
        }

        @Test
        void shouldReturnMatchingEntryWhenSearchingByAllParameters() {
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
        void shouldOnlyReturnMatchingEntryWhenSearchingByAllParameters() {
            final List<TeamGame> teamGameList = List.of(
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
        void shouldNotReturnMatchingEntriyWhenSearchingByAllParametersAndYearIsDifferent() {
            final List<TeamGame> teamGameList = List.of(
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
        void shouldNotReturnMatchingEntriyWhenSearchingByAllParametersAndDatestampIsDifferent() {
            final List<TeamGame> teamGameList = List.of(
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
        void shouldNotReturnMatchingEntriyWhenSearchingByAllParametersAndTypeIsDifferent() {
            final List<TeamGame> teamGameList = List.of(
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
        void shouldNotReturnMatchingEntriyWhenSearchingByAllParametersAndOpponentIsDifferent() {
            final List<TeamGame> teamGameList = List.of(
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
}