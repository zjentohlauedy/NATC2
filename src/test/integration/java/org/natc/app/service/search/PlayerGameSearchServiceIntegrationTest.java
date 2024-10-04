package org.natc.app.service.search;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.PlayerGame;
import org.natc.app.entity.request.PlayerGameSearchRequest;
import org.natc.app.entity.response.PlayerGameResponse;
import org.natc.app.repository.PlayerGameRepository;
import org.natc.app.service.NATCServiceIntegrationTest;
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

    @Nested
    class FetchAll {

        @Test
        void shouldReturnAPlayerGameFromTheDatabaseMappedToAResponse() {
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

            final PlayerGameResponse response = result.getFirst();

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
        void shouldMapAllPlayerGameFieldsToTheResponse() {
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

            final PlayerGameResponse response = result.getFirst();

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
        void shouldReturnAllEntriesWhenSearchingWithoutValues() {
            final List<PlayerGame> playerGameList = List.of(
                    PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                    PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                    PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
            );

            repository.saveAll(playerGameList);

            final List<PlayerGameResponse> result = searchService.fetchAll(new PlayerGameSearchRequest());

            assertEquals(3, result.size());
        }

        @Test
        void shouldReturnNoEntriesWhenSearchingGivenNoDataInTheDatabase() {
            final List<PlayerGameResponse> result = searchService.fetchAll(new PlayerGameSearchRequest());

            assertEquals(0, result.size());
        }

        @Nested
        class WithOneSearchParameter {

            @Test
            void shouldReturnAllEntriesForGameWhenSearchingByGameId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameWhenSearchingByGameId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForYearWhenSearchingByYear() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearWhenSearchingByYear() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForDatestampWhenSearchingByDatestamp() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForDatestampWhenSearchingByDatestamp() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForTypeWhenSearchingByType() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForTypeWhenSearchingByType() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForPlayerWhenSearchingByPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForPlayerWhenSearchingByPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForTeamWhenSearchingByTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForTeamWhenSearchingByTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
        }

        @Nested
        class WithTwoSearchParameters {

            @Test
            void shouldReturnAllEntriesForGameAndYearWhenSearchingByGameIdAndYear() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameAndYearWhenSearchingByGameIdAndYear() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameAndDatestampWhenSearchingByGameIdAndDatestamp() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameAndDatestampWhenSearchingByGameIdAndDatestamp() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameAndTypeWhenSearchingByGameIdAndType() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameAndTypeWhenSearchingByGameIdAndType() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameAndPlayerWhenSearchingByGameIdAndPlayerId() {
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
            void shouldOnlyReturnEntriesForGameAndPlayerWhenSearchingByGameIdAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameAndTeamWhenSearchingByGameIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameAndTeamWhenSearchingByGameIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForYearAndDatestampWhenSearchingByYearAndDatestamp() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearAndDatestampWhenSearchingByYearAndDatestamp() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForYearAndTypeWhenSearchingByYearAndType() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearAndTypeWhenSearchingByYearAndType() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForYearAndPlayerWhenSearchingByYearAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearAndPlayerWhenSearchingByYearAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForDatestampAndTypeWhenSearchingByDatestampAndType() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForDatestampAndTypeWhenSearchingByDatestampAndType() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForDatestampAndPlayerWhenSearchingByDatestampAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForDatestampAndPlayerWhenSearchingByDatestampAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForDatestampAndTeamWhenSearchingByDatestampAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForDatestampAndTeamWhenSearchingByDatestampAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForTypeAndPlayerWhenSearchingByTypeAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForTypeAndPlayerWhenSearchingByTypeAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForTypeAndTeamWhenSearchingByTypeAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForTypeAndTeamWhenSearchingByTypeAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForPlayerAndTeamWhenSearchingByPlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForPlayerAndTeamWhenSearchingByPlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
        }

        @Nested
        class WithThreeSearchParameters {

            @Test
            void shouldReturnAllEntriesForGameYearAndDatestampWhenSearchingByGameIdYearAndDatestamp() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameYearAndDatestampWhenSearchingByGameIdYearAndDatestamp() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearAndTypeWhenSearchingByGameIdYearAndType() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameYearAndTypeWhenSearchingByGameIdYearAndType() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearAndPlayerWhenSearchingByGameIdYearAndPlayerId() {
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
            void shouldOnlyReturnEntriesForGameYearAndPlayerWhenSearchingByGameIdYearAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearAndTeamWhenSearchingByGameIdYearAndTeamIdAndYearIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearAndTeamWhenSearchingByGameIdYearAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameYearAndTeamWhenSearchingByGameIdYearAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameDatestampAndTypeWhenSearchingByGameIdDatestampAndType() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameDatestampAndTypeWhenSearchingByGameIdDatestampAndType() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameDatestampAndPlayerWhenSearchingByGameIdDatestampAndPlayerId() {
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
            void shouldOnlyReturnEntriesForGameDatestampAndPlayerWhenSearchingByGameIdDatestampAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameDatestampAndPlayerWhenSearchingByGameIdDatestampAndPlayerIdAndDatestampIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameDatestampAndTeamWhenSearchingByGameIdDatestampAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameDatestampAndTeamWhenSearchingByGameIdDatestampAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameTypeAndPlayerWhenSearchingByGameIdTypeAndPlayerId() {
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
            void shouldOnlyReturnEntriesForGameTypeAndPlayerWhenSearchingByGameIdTypeAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameTypeAndPlayerWhenSearchingByGameIdTypeAndPlayerIdAndTypeIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameTypeAndTeamWhenSearchingByGameIdTypeAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameTypeAndTeamWhenSearchingByGameIdTypeAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGamePlayerAndTeamWhenSearchingByGameIdPlayerIdAndTeamId() {
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
            void shouldOnlyReturnEntriesForGamePlayerAndTeamWhenSearchingByGameIdPlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGamePlayerAndTeamWhenSearchingByGameIdPlayerIdAndTeamAndTeamIdIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForYearDatestampAndTypeWhenSearchingByYearDatestampAndType() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearDatestampAndTypeWhenSearchingByYearDatestampAndType() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForYearDatestampAndPlayerWhenSearchingByYearDatestampAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearDatestampAndPlayerWhenSearchingByYearDatestampAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForYearDatestampAndTeamWhenSearchingByYearDatestampAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearDatestampAndTeamWhenSearchingByYearDatestampAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForYearTypeAndPlayerWhenSearchingByYearTypeAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearTypeAndPlayerWhenSearchingByYearTypeAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForYearTypeAndTeamWhenSearchingByYearTypeAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearTypeAndTeamWhenSearchingByYearTypeAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForYearPlayerAndTeamWhenSearchingByYearPlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearPlayerAndTeamWhenSearchingByYearPlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForDatestampTypeAndPlayerWhenSearchingByDatestampTypeAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForDatestampTypeAndPlayerWhenSearchingByDatestampTypeAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForDatestampTypeAndTeamWhenSearchingByDatestampTypeAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForDatestampTypeAndTeamWhenSearchingByDatestampTypeAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForDatestampPlayerAndTeamWhenSearchingByDatestampPlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForDatestampPlayerAndTeamWhenSearchingByDatestampPlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForTypePlayerAndTeamWhenSearchingByTypePlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForTypePlayerAndTeamWhenSearchingByTypePlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
        }

        @Nested
        class WithFourSearchParameters {

            @Test
            void shouldReturnAllEntriesForGameYearDatestampAndTypeWhenSearchingByGameYearDatestampAndType() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameYearDatestampAndTypeWhenSearchingByGameYearDatestampAndType() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearDatestampAndPlayerWhenSearchingByGameIdYearDatestampAndPlayerId() {
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
            void shouldOnlyReturnEntriesForGameYearDatestampAndPlayerWhenSearchingByGameIdYearDatestampAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearDatestampAndPlayerWhenSearchingByGameIdYearDatestampAndPlayerIdAndYearIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearDatestampAndPlayerWhenSearchingByGameIdYearDatestampAndPlayerIdAndDatestampIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearDatestampAndTeamWhenSearchingByGameIdYearDatestampAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameYearDatestampAndTeamWhenSearchingByGameIdYearDatestampAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearTypeAndPlayerWhenSearchingByGameIdYearTypeAndPlayerId() {
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
            void shouldOnlyReturnEntriesForGameYearTypeAndPlayerWhenSearchingByGameIdYearTypeAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearTypeAndPlayerWhenSearchingByGameIdYearTypeAndPlayerIdAndYearIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearTypeAndPlayerWhenSearchingByGameIdYearTypeAndPlayerIdAndTypeIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearTypeAndTeamWhenSearchingByGameIdYearTypeAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameYearTypeAndTeamWhenSearchingByGameIdYearTypeAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearPlayerAndTeamWhenSearchingByGameIdYearPlayerIdAndTeamId() {
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
            void shouldOnlyReturnEntriesForGameYearPlayerAndTeamWhenSearchingByGameIdYearPlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearPlayerAndTeamWhenSearchingByGameIdYearPlayerIdAndTeamAndYearIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearPlayerAndTeamWhenSearchingByGameIdYearPlayerIdAndTeamAndTeamIdIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameDatestampTypeAndPlayerWhenSearchingByGameIdDatestampTypeAndPlayerId() {
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
            void shouldOnlyReturnEntriesForGameDatestampTypeAndPlayerWhenSearchingByGameIdDatestampTypeAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameDatestampTypeAndPlayerWhenSearchingByGameIdDatestampTypeAndPlayerIdAndDatestampIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameDatestampTypeAndPlayerWhenSearchingByGameIdDatestampTypeAndPlayerIdAndTypeIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameDatestampTypeAndTeamWhenSearchingByGameIdDatestampTypeAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameDatestampTypeAndTeamWhenSearchingByGameIdDatestampTypeAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameDatestampPlayerAndTeamWhenSearchingByGameIdDatestampPlayerIdAndTeamId() {
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
            void shouldOnlyReturnEntriesForGameDatestampPlayerAndTeamWhenSearchingByGameIdDatestampPlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameDatestampPlayerAndTeamWhenSearchingByGameIdDatestampPlayerIdAndTeamAndDatestampIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameDatestampPlayerAndTeamWhenSearchingByGameIdDatestampPlayerIdAndTeamAndTeamIdIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameTypePlayerAndTeamWhenSearchingByGameIdTypePlayerIdAndTeamId() {
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
            void shouldOnlyReturnEntriesForGameTypePlayerAndTeamWhenSearchingByGameIdTypePlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameTypePlayerAndTeamWhenSearchingByGameIdTypePlayerIdAndTeamAndTypeIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameTypePlayerAndTeamWhenSearchingByGameIdTypePlayerIdAndTeamAndTeamIdIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForYearDatestampTypeAndPlayerWhenSearchingByYearDatestampTypeAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearDatestampTypeAndPlayerWhenSearchingByYearDatestampTypeAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForYearDatestampTypeAndTeamWhenSearchingByYearDatestampTypeAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearDatestampTypeAndTeamWhenSearchingByYearDatestampTypeAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForYearDatestampPlayerAndTeamWhenSearchingByYearDatestampPlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearDatestampPlayerAndTeamWhenSearchingByYearDatestampPlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForYearTypePlayerAndTeamWhenSearchingByYearTypePlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearTypePlayerAndTeamWhenSearchingByYearTypePlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForDatestampTypePlayerAndTeamWhenSearchingByDatestampTypePlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForDatestampTypePlayerAndTeamWhenSearchingByDatestampTypePlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
        }

        @Nested
        class WithFiveSearchParameters {

            @Test
            void shouldReturnAllEntriesForGameYearDatestampTypeAndPlayerWhenSearchingByGameIdYearDatestampTypeAndPlayerId() {
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
            void shouldOnlyReturnEntriesForGameYearDatestampTypeAndPlayerWhenSearchingByGameIdYearDatestampTypeAndPlayerId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearDatestampTypeAndPlayerWhenSearchingByGameIdYearDatestampTypeAndPlayerIdAndYearIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearDatestampTypeAndPlayerWhenSearchingByGameIdYearDatestampTypeAndPlayerIdAndDatestampIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearDatestampTypeAndPlayerWhenSearchingByGameIdYearDatestampTypeAndPlayerIdAndTypeIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearDatestampTypeAndTeamWhenSearchingByGameIdYearDatestampTypeAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForGameYearDatestampTypeAndTeamWhenSearchingByGameIdYearDatestampTypeAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearDatestampPlayerAndTeamWhenSearchingByGameIdYearDatestampPlayerIdAndTeamId() {
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
            void shouldOnlyReturnEntriesForGameYearDatestampPlayerAndTeamWhenSearchingByGameIdYearDatestampPlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearDatestampPlayerAndTeamWhenSearchingByGameIdYearDatestampPlayerIdAndTeamAndYearIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearDatestampPlayerAndTeamWhenSearchingByGameIdYearDatestampPlayerIdAndTeamAndDatestampIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearDatestampPlayerAndTeamWhenSearchingByGameIdYearDatestampPlayerIdAndTeamIdAndTeamIdIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameYearTypePlayerAndTeamWhenSearchingByGameIdYearTypePlayerIdAndTeamId() {
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
            void shouldOnlyReturnEntriesForGameYearTypePlayerAndTeamWhenSearchingByGameIdYearTypePlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearTypePlayerAndTeamWhenSearchingByGameIdYearTypePlayerIdAndTeamIdAndYearIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearTypePlayerAndTeamWhenSearchingByGameIdYearTypePlayerIdAndTeamIdAndTypeIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameYearTypePlayerAndTeamWhenSearchingByGameIdYearTypePlayerIdAndTeamIdAndTeamIdIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForGameDatestampTypePlayerAndTeamWhenSearchingByGameIdDatestampTypePlayerIdAndTeamId() {
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
            void shouldOnlyReturnEntriesForGameDatestampTypePlayerAndTeamWhenSearchingByGameIdDatestampTypePlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameDatestampTypePlayerAndTeamWhenSearchingByGameIdDatestampTypePlayerIdAndTeamIdAndDatestampIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameDatestampTypePlayerAndTeamWhenSearchingByGameIdDatestampTypePlayerIdAndTeamIdAndTypeIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnNoEntriesForGameDatestampTypePlayerAndTeamWhenSearchingByGameIdDatestampTypePlayerIdAndTeamIdAndTeamIdIsDifferent() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldReturnAllEntriesForYearDatestampTypePlayerAndTeamWhenSearchingByYearDatestampTypePlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
            void shouldOnlyReturnEntriesForYearDatestampTypePlayerAndTeamWhenSearchingByYearDatestampTypePlayerIdAndTeamId() {
                final List<PlayerGame> playerGameList = List.of(
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
        }

        @Test
        void shouldReturnMatchingEntryWhenSearchingByAllParameters() {
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
        void shouldOnlyReturnMatchingEntryWhenSearchingByAllParameters() {
            final List<PlayerGame> playerGameList = List.of(
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
        void shouldNotReturnMatchingEntriyWhenSearchingByAllParametersAndYearIsDifferent() {
            final List<PlayerGame> playerGameList = List.of(
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
        void shouldNotReturnMatchingEntriyWhenSearchingByAllParametersAndDatestampIsDifferent() {
            final List<PlayerGame> playerGameList = List.of(
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
        void shouldNotReturnMatchingEntriyWhenSearchingByAllParametersAndTypeIsDifferent() {
            final List<PlayerGame> playerGameList = List.of(
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
        void shouldNotReturnMatchingEntriyWhenSearchingByAllParametersAndTeamIdIsDifferent() {
            final List<PlayerGame> playerGameList = List.of(
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
}