package org.natc.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.PlayerGame;
import org.natc.app.entity.request.PlayerGameSearchRequest;
import org.natc.app.entity.response.PlayerGameResponse;
import org.natc.app.service.search.PlayerGameSearchService;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerGameSearchServiceTest {

    @Captor
    private ArgumentCaptor<Example<PlayerGame>> captor;

    @Mock
    private JpaRepository repository;

    @InjectMocks
    private PlayerGameSearchService service;

    @Test
    public void fetchAll_ShouldReturnAListOfPlayerGameResponses() {
        final List<PlayerGameResponse> result = service.fetchAll(new PlayerGameSearchRequest());

        assertEquals(0, result.size());
    }

    @Test
    public void fetchAll_ShouldCallThePlayerGameRepositoryWithAnExamplePlayerGame() {
        service.fetchAll(new PlayerGameSearchRequest());

        verify(repository).findAll(ArgumentMatchers.<Example<PlayerGame>>any());
    }

    @Test
    public void fetchAll_ShouldCallPlayerGameRepositoryWithExamplePlayerGameBasedOnRequest() {
        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(123)
                .year("2014")
                .datestamp(LocalDate.now())
                .type(GameType.REGULAR_SEASON)
                .playerId(321)
                .teamId(32)
                .build();

        service.fetchAll(request);

        verify(repository).findAll(captor.capture());

        final PlayerGame playerGame = captor.getValue().getProbe();

        assertEquals(request.getGameId(), playerGame.getGameId());
        assertEquals(request.getYear(), playerGame.getYear());
        assertEquals(request.getDatestamp(), playerGame.getDatestamp());
        assertEquals(request.getType().getValue(), playerGame.getType());
        assertEquals(request.getPlayerId(), playerGame.getPlayerId());
        assertEquals(request.getTeamId(), playerGame.getTeamId());
    }

    @Test
    public void fetchAll_ShouldReturnResponsesMappedFromThePlayerGamesReturnedByRepository() {
        final PlayerGame playerGame = generatePlayerGame(GameType.REGULAR_SEASON);

        when(repository.findAll(ArgumentMatchers.<Example<PlayerGame>>any())).thenReturn(Collections.singletonList(playerGame));

        final List<PlayerGameResponse> result = service.fetchAll(new PlayerGameSearchRequest());

        assertEquals(1, result.size());

        final PlayerGameResponse response = result.get(0);

        assertEquals(playerGame.getGameId(), response.getGameId());
        assertEquals(playerGame.getYear(), response.getYear());
        assertEquals(playerGame.getDatestamp(), response.getDatestamp());
        assertEquals(GameType.REGULAR_SEASON, response.getType());
        assertEquals(playerGame.getPlayerId(), response.getPlayerId());
        assertEquals(playerGame.getTeamId(), response.getTeamId());
        assertEquals(playerGame.getPlayingTime(), response.getPlayingTime());
        assertEquals(playerGame.getAttempts(), response.getAttempts());
        assertEquals(playerGame.getGoals(), response.getGoals());
        assertEquals(playerGame.getAssists(), response.getAssists());
        assertEquals(playerGame.getTurnovers(), response.getTurnovers());
        assertEquals(playerGame.getStops(), response.getStops());
        assertEquals(playerGame.getSteals(), response.getSteals());
        assertEquals(playerGame.getPenalties(), response.getPenalties());
        assertEquals(playerGame.getOffensivePenalties(), response.getOffensivePenalties());
        assertEquals(playerGame.getPenaltyShotsAttempted(), response.getPenaltyShotsAttempted());
        assertEquals(playerGame.getPenaltyShotsMade(), response.getPenaltyShotsMade());
        assertEquals(playerGame.getOvertimePenaltyShotsAttempted(), response.getOvertimePenaltyShotsAttempted());
        assertEquals(playerGame.getOvertimePenaltyShotsMade(), response.getOvertimePenaltyShotsMade());
        assertEquals(playerGame.getOffense(), response.getOffense());
        assertEquals(playerGame.getPoints(), response.getPoints());

        assertNotNull(response.getInjured());
        assertNotNull(response.getStarted());
    }

    @Test
    public void fetchAll_ShouldMapInjuredValueFromIntegerToBooleanInResponseWhenFalse() {
        final PlayerGame playerGame = PlayerGame.builder().injured(0).build();

        when(repository.findAll(ArgumentMatchers.<Example<PlayerGame>>any())).thenReturn(Collections.singletonList(playerGame));

        final List<PlayerGameResponse> result = service.fetchAll(new PlayerGameSearchRequest());

        assertEquals(1, result.size());

        final PlayerGameResponse response = result.get(0);

        assertFalse(response.getInjured());
    }

    @Test
    public void fetchAll_ShouldMapInjuredValueFromIntegerToBooleanInResponseWhenTrue() {
        final PlayerGame playerGame = PlayerGame.builder().injured(1).build();

        when(repository.findAll(ArgumentMatchers.<Example<PlayerGame>>any())).thenReturn(Collections.singletonList(playerGame));

        final List<PlayerGameResponse> result = service.fetchAll(new PlayerGameSearchRequest());

        assertEquals(1, result.size());

        final PlayerGameResponse response = result.get(0);

        assertTrue(response.getInjured());
    }

    @Test
    public void fetchAll_ShouldMapInjuredValueFromIntegerToBooleanInResponseWhenNull() {
        final PlayerGame playerGame = PlayerGame.builder().build();

        when(repository.findAll(ArgumentMatchers.<Example<PlayerGame>>any())).thenReturn(Collections.singletonList(playerGame));

        final List<PlayerGameResponse> result = service.fetchAll(new PlayerGameSearchRequest());

        assertEquals(1, result.size());

        final PlayerGameResponse response = result.get(0);

        assertNull(response.getInjured());
    }

    @Test
    public void fetchAll_ShouldMapStartedValueFromIntegerToBooleanInResponseWhenFalse() {
        final PlayerGame playerGame = PlayerGame.builder().started(0).build();

        when(repository.findAll(ArgumentMatchers.<Example<PlayerGame>>any())).thenReturn(Collections.singletonList(playerGame));

        final List<PlayerGameResponse> result = service.fetchAll(new PlayerGameSearchRequest());

        assertEquals(1, result.size());

        final PlayerGameResponse response = result.get(0);

        assertFalse(response.getStarted());
    }

    @Test
    public void fetchAll_ShouldMapStartedValueFromIntegerToBooleanInResponseWhenTrue() {
        final PlayerGame playerGame = PlayerGame.builder().started(1).build();

        when(repository.findAll(ArgumentMatchers.<Example<PlayerGame>>any())).thenReturn(Collections.singletonList(playerGame));

        final List<PlayerGameResponse> result = service.fetchAll(new PlayerGameSearchRequest());

        assertEquals(1, result.size());

        final PlayerGameResponse response = result.get(0);

        assertTrue(response.getStarted());
    }

    @Test
    public void fetchAll_ShouldMapStartedValueFromIntegerToBooleanInResponseWhenNull() {
        final PlayerGame playerGame = PlayerGame.builder().build();

        when(repository.findAll(ArgumentMatchers.<Example<PlayerGame>>any())).thenReturn(Collections.singletonList(playerGame));

        final List<PlayerGameResponse> result = service.fetchAll(new PlayerGameSearchRequest());

        assertEquals(1, result.size());

        final PlayerGameResponse response = result.get(0);

        assertNull(response.getStarted());
    }

    @Test
    public void fetchAll_ShouldReturnSameNumberOfResponsesAsPlayerGamesReturnedByRepository() {
        final List<PlayerGame> playerGameList = Arrays.asList(
                new PlayerGame(),
                new PlayerGame(),
                new PlayerGame(),
                new PlayerGame()
        );

        when(repository.findAll(ArgumentMatchers.<Example<PlayerGame>>any())).thenReturn(playerGameList);

        final List<PlayerGameResponse> result = service.fetchAll(new PlayerGameSearchRequest());

        assertEquals(playerGameList.size(), result.size());
    }

    private PlayerGame generatePlayerGame(final GameType type) {
        return PlayerGame.builder()
                .gameId(12345)
                .year("2002")
                .datestamp(LocalDate.now())
                .type(type.getValue())
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
    }
}