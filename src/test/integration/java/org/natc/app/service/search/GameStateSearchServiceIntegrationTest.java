package org.natc.app.service.search;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.GameState;
import org.natc.app.entity.domain.Period;
import org.natc.app.entity.domain.PossessionType;
import org.natc.app.entity.request.GameStateSearchRequest;
import org.natc.app.entity.response.GameStateResponse;
import org.natc.app.repository.GameStateRepository;
import org.natc.app.service.NATCServiceIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameStateSearchServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private GameStateRepository repository;

    @Autowired
    private GameStateSearchService searchService;

    @Nested
    class FetchAll {

        @Test
        void shouldReturnAPlayerFromTheDatabaseMappedToAResponse() {
            final GameState gameState = GameState.builder()
                    .gameId(123)
                    .started(0)
                    .startTime(54321)
                    .build();

            repository.save(gameState);

            final List<GameStateResponse> result = searchService.fetchAll(new GameStateSearchRequest());

            assertEquals(1, result.size());

            final GameStateResponse response = result.getFirst();

            assertEquals(gameState.getGameId(), response.getGameId());
            assertEquals(false, response.getStarted());
            assertEquals(gameState.getStartTime(), response.getStartTime());
        }

        @Test
        void shouldMapAllGameStateFieldsToTheGameStateResponse() {
            final GameState gameState = GameState.builder()
                    .gameId(123)
                    .started(1)
                    .startTime(55555)
                    .sequence(111)
                    .period(Period.FOURTH.getValue())
                    .overtime(0)
                    .timeRemaining(222)
                    .clockStopped(1)
                    .possession(PossessionType.ROAD.getValue())
                    .lastEvent("This is the last event description.")
                    .build();

            repository.save(gameState);

            final List<GameStateResponse> result = searchService.fetchAll(new GameStateSearchRequest());

            assertEquals(1, result.size());

            final GameStateResponse response = result.getFirst();

            assertNotNull(response.getGameId());
            assertNotNull(response.getStarted());
            assertNotNull(response.getStartTime());
            assertNotNull(response.getSequence());
            assertNotNull(response.getPeriod());
            assertNotNull(response.getOvertime());
            assertNotNull(response.getTimeRemaining());
            assertNotNull(response.getClockStopped());
            assertNotNull(response.getPossession());
            assertNotNull(response.getLastEvent());
        }

        @Test
        void shouldReturnAllEntriesWhenSearchingWithoutValues() {
            final List<GameState> gameStateList = List.of(
                    GameState.builder().gameId(1).build(),
                    GameState.builder().gameId(2).build(),
                    GameState.builder().gameId(3).build()
            );

            repository.saveAll(gameStateList);

            final List<GameStateResponse> result = searchService.fetchAll(new GameStateSearchRequest());

            assertEquals(3, result.size());
        }

        @Test
        void shouldReturnNoEntriesWhenSearchingGivenNoDataInTheDatabase() {
            final List<GameStateResponse> result = searchService.fetchAll(new GameStateSearchRequest());

            assertEquals(0, result.size());
        }

        @Test
        void shouldReturnAllEntriesForGameWhenSearchingByGameId() {
            final List<GameState> gameStateList = Collections.singletonList(
                    // Game ID is the key field, so only one record is possible
                    GameState.builder().gameId(1).build()
            );

            repository.saveAll(gameStateList);

            final GameStateSearchRequest request = GameStateSearchRequest.builder().gameId(1).build();

            final List<GameStateResponse> result = searchService.fetchAll(request);

            assertEquals(1, result.size());
        }

        @Test
        void shouldReturnOnlyEntriesForGameWhenSearchingByGameId() {
            final List<GameState> gameStateList = List.of(
                    GameState.builder().gameId(1).build(),
                    GameState.builder().gameId(2).build(),
                    GameState.builder().gameId(3).build()
            );

            repository.saveAll(gameStateList);

            final GameStateSearchRequest request = GameStateSearchRequest.builder().gameId(1).build();

            final List<GameStateResponse> result = searchService.fetchAll(request);

            assertEquals(1, result.size());
            assertEquals(1, result.stream().filter(t -> t.getGameId().equals(1)).count());
        }
    }
}