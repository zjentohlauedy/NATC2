package org.natc.app.service.search;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.GameState;
import org.natc.app.entity.domain.Period;
import org.natc.app.entity.domain.PossessionType;
import org.natc.app.entity.request.GameStateSearchRequest;
import org.natc.app.entity.response.GameStateResponse;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameStateSearchServiceTest {

    @Captor
    private ArgumentCaptor<Example<GameState>> captor;

    @Mock
    private JpaRepository repository;

    @InjectMocks
    private GameStateSearchService service;

    @Test
    public void fetchAll_ShouldReturnAListOfGameStateResponses() {
        final List<GameStateResponse> result = service.fetchAll(new GameStateSearchRequest());

        assertEquals(0, result.size());
    }

    @Test
    public void fetchAll_ShouldCallTheGameStateRepositoryWithAnExampleGameState() {
        service.fetchAll(new GameStateSearchRequest());

        verify(repository).findAll(ArgumentMatchers.<Example<GameState>>any());
    }

    @Test
    public void fetchAll_ShouldCallGameStateRepositoryWithExampleGameStateBasedOnRequest() {
        final GameStateSearchRequest request = GameStateSearchRequest.builder()
                .gameId(111)
                .build();

        service.fetchAll(request);

        verify(repository).findAll(captor.capture());

        final GameState gameState = captor.getValue().getProbe();

        assertEquals(request.getGameId(), gameState.getGameId());
    }

    @Test
    public void fetchAll_ShouldReturnGameStateResponsesMappedFromTheGameStatesReturnedByRepository() {
        final GameState gameState = generateGameState(Period.FOURTH, PossessionType.ROAD);

        when(repository.findAll(ArgumentMatchers.<Example<GameState>>any())).thenReturn(Collections.singletonList(gameState));

        final List<GameStateResponse> result = service.fetchAll(new GameStateSearchRequest());

        assertEquals(1, result.size());

        final GameStateResponse response = result.get(0);

        assertEquals(gameState.getGameId(), response.getGameId());
        assertEquals(gameState.getStartTime(), response.getStartTime());
        assertEquals(gameState.getSequence(), response.getSequence());
        assertEquals(Period.FOURTH, response.getPeriod());
        assertEquals(gameState.getTimeRemaining(), response.getTimeRemaining());
        assertEquals(PossessionType.ROAD, response.getPossession());
        assertEquals(gameState.getLastEvent(), response.getLastEvent());

        assertNotNull(response.getStarted());
        assertNotNull(response.getOvertime());
        assertNotNull(response.getClockStopped());
    }

    @Test
    public void fetchAll_ShouldMapStartedValueFromIntegerToBooleanInResponseWhenFalse() {
        final GameState gameState = GameState.builder().started(0).build();

        when(repository.findAll(ArgumentMatchers.<Example<GameState>>any())).thenReturn(Collections.singletonList(gameState));

        final List<GameStateResponse> result = service.fetchAll(new GameStateSearchRequest());

        assertEquals(1, result.size());

        final GameStateResponse response = result.get(0);

        assertFalse(response.getStarted());
    }

    @Test
    public void fetchAll_ShouldMapStartedValueFromIntegerToBooleanInResponseWhenTrue() {
        final GameState gameState = GameState.builder().started(1).build();

        when(repository.findAll(ArgumentMatchers.<Example<GameState>>any())).thenReturn(Collections.singletonList(gameState));

        final List<GameStateResponse> result = service.fetchAll(new GameStateSearchRequest());

        assertEquals(1, result.size());

        final GameStateResponse response = result.get(0);

        assertTrue(response.getStarted());
    }

    @Test
    public void fetchAll_ShouldMapStartedValueFromIntegerToBooleanInResponseWhenNull() {
        final GameState gameState = GameState.builder().build();

        when(repository.findAll(ArgumentMatchers.<Example<GameState>>any())).thenReturn(Collections.singletonList(gameState));

        final List<GameStateResponse> result = service.fetchAll(new GameStateSearchRequest());

        assertEquals(1, result.size());

        final GameStateResponse response = result.get(0);

        assertNull(response.getStarted());
    }

    @Test
    public void fetchAll_ShouldMapOvertimeValueFromIntegerToBooleanInResponseWhenFalse() {
        final GameState gameState = GameState.builder().overtime(0).build();

        when(repository.findAll(ArgumentMatchers.<Example<GameState>>any())).thenReturn(Collections.singletonList(gameState));

        final List<GameStateResponse> result = service.fetchAll(new GameStateSearchRequest());

        assertEquals(1, result.size());

        final GameStateResponse response = result.get(0);

        assertFalse(response.getOvertime());
    }

    @Test
    public void fetchAll_ShouldMapOvertimeValueFromIntegerToBooleanInResponseWhenTrue() {
        final GameState gameState = GameState.builder().overtime(1).build();

        when(repository.findAll(ArgumentMatchers.<Example<GameState>>any())).thenReturn(Collections.singletonList(gameState));

        final List<GameStateResponse> result = service.fetchAll(new GameStateSearchRequest());

        assertEquals(1, result.size());

        final GameStateResponse response = result.get(0);

        assertTrue(response.getOvertime());
    }

    @Test
    public void fetchAll_ShouldMapOvertimeValueFromIntegerToBooleanInResponseWhenNull() {
        final GameState gameState = GameState.builder().build();

        when(repository.findAll(ArgumentMatchers.<Example<GameState>>any())).thenReturn(Collections.singletonList(gameState));

        final List<GameStateResponse> result = service.fetchAll(new GameStateSearchRequest());

        assertEquals(1, result.size());

        final GameStateResponse response = result.get(0);

        assertNull(response.getOvertime());
    }

    @Test
    public void fetchAll_ShouldMapClockStoppedValueFromIntegerToBooleanInResponseWhenFalse() {
        final GameState gameState = GameState.builder().clockStopped(0).build();

        when(repository.findAll(ArgumentMatchers.<Example<GameState>>any())).thenReturn(Collections.singletonList(gameState));

        final List<GameStateResponse> result = service.fetchAll(new GameStateSearchRequest());

        assertEquals(1, result.size());

        final GameStateResponse response = result.get(0);

        assertFalse(response.getClockStopped());
    }

    @Test
    public void fetchAll_ShouldMapClockStoppedValueFromIntegerToBooleanInResponseWhenTrue() {
        final GameState gameState = GameState.builder().clockStopped(1).build();

        when(repository.findAll(ArgumentMatchers.<Example<GameState>>any())).thenReturn(Collections.singletonList(gameState));

        final List<GameStateResponse> result = service.fetchAll(new GameStateSearchRequest());

        assertEquals(1, result.size());

        final GameStateResponse response = result.get(0);

        assertTrue(response.getClockStopped());
    }

    @Test
    public void fetchAll_ShouldMapClockStoppedValueFromIntegerToBooleanInResponseWhenNull() {
        final GameState gameState = GameState.builder().build();

        when(repository.findAll(ArgumentMatchers.<Example<GameState>>any())).thenReturn(Collections.singletonList(gameState));

        final List<GameStateResponse> result = service.fetchAll(new GameStateSearchRequest());

        assertEquals(1, result.size());

        final GameStateResponse response = result.get(0);

        assertNull(response.getClockStopped());
    }

    @Test
    public void fetchAll_ShouldReturnSameNumberOfResponsesAsGameStatesReturnedByRepository() {
        final List<GameState> gameStateList = Arrays.asList(
                new GameState(),
                new GameState(),
                new GameState(),
                new GameState()
        );

        when(repository.findAll(ArgumentMatchers.<Example<GameState>>any())).thenReturn(gameStateList);

        final List<GameStateResponse> result = service.fetchAll(new GameStateSearchRequest());

        assertEquals(gameStateList.size(), result.size());
    }

    private GameState generateGameState(final Period period, final PossessionType possessionType) {
        return GameState.builder()
                .gameId(123)
                .started(1)
                .startTime(55555)
                .sequence(111)
                .period(period.getValue())
                .overtime(0)
                .timeRemaining(222)
                .clockStopped(1)
                .possession(possessionType.getValue())
                .lastEvent("This is the last event description.")
                .build();
    }
}