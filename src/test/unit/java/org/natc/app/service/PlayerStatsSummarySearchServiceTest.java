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
import org.natc.app.entity.domain.PlayerStatsSummary;
import org.natc.app.entity.request.PlayerStatsSummarySearchRequest;
import org.natc.app.entity.response.PlayerStatsSummaryResponse;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerStatsSummarySearchServiceTest {

    @Captor
    private ArgumentCaptor<Example<PlayerStatsSummary>> captor;

    @Mock
    private JpaRepository repository;

    @InjectMocks
    private PlayerStatsSummarySearchService service;

    @Test
    public void fetchAll_ShouldReturnAListOfPlayerStatsSummaryResponses() {
        final List<PlayerStatsSummaryResponse> result = service.fetchAll(new PlayerStatsSummarySearchRequest());

        assertEquals(0, result.size());
    }

    @Test
    public void fetchAll_ShouldCallTheRepositoryWithAnExamplePlayerStatsSummary() {
        service.fetchAll(new PlayerStatsSummarySearchRequest());

        verify(repository).findAll(ArgumentMatchers.<Example<PlayerStatsSummary>>any());
    }

    @Test
    public void fetchAll_ShouldCallRepositoryWithExampleBasedOnRequest() {
        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year("1888")
                .type(GameType.POSTSEASON)
                .playerId(444)
                .teamId(22)
                .build();

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(request);

        verify(repository).findAll(captor.capture());

        final PlayerStatsSummary playerStatsSummary = captor.getValue().getProbe();

        assertEquals(request.getYear(), playerStatsSummary.getYear());
        assertEquals(request.getType().getValue(), playerStatsSummary.getType());
        assertEquals(request.getPlayerId(), playerStatsSummary.getPlayerId());
        assertEquals(request.getTeamId(), playerStatsSummary.getTeamId());
    }

    @Test
    public void fetchAll_ShouldReturnResponsesMappedFromThePlayerStatsSummariesReturnedByRepository() {
        final PlayerStatsSummary playerStatsSummary = generatePlayerStatsSummary(GameType.PRESEASON);

        when(repository.findAll(ArgumentMatchers.<Example<PlayerStatsSummary>>any()))
                .thenReturn(Collections.singletonList(playerStatsSummary));

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(new PlayerStatsSummarySearchRequest());

        assertEquals(1, result.size());

        final PlayerStatsSummaryResponse response = result.get(0);

        assertEquals(playerStatsSummary.getYear(), response.getYear());
        assertEquals(GameType.PRESEASON, response.getType());
        assertEquals(playerStatsSummary.getPlayerId(), response.getPlayerId());
        assertEquals(playerStatsSummary.getGames(), response.getGames());
        assertEquals(playerStatsSummary.getGamesStarted(), response.getGamesStarted());
        assertEquals(playerStatsSummary.getPlayingTime(), response.getPlayingTime());
        assertEquals(playerStatsSummary.getAttempts(), response.getAttempts());
        assertEquals(playerStatsSummary.getGoals(), response.getGoals());
        assertEquals(playerStatsSummary.getAssists(), response.getAssists());
        assertEquals(playerStatsSummary.getTurnovers(), response.getTurnovers());
        assertEquals(playerStatsSummary.getStops(), response.getStops());
        assertEquals(playerStatsSummary.getSteals(), response.getSteals());
        assertEquals(playerStatsSummary.getPenalties(), response.getPenalties());
        assertEquals(playerStatsSummary.getOffensivePenalties(), response.getOffensivePenalties());
        assertEquals(playerStatsSummary.getPenaltyShotsAttempted(), response.getPenaltyShotsAttempted());
        assertEquals(playerStatsSummary.getPenaltyShotsMade(), response.getPenaltyShotsMade());
        assertEquals(playerStatsSummary.getOvertimePenaltyShotsAttempted(), response.getOvertimePenaltyShotsAttempted());
        assertEquals(playerStatsSummary.getOvertimePenaltyShotsMade(), response.getOvertimePenaltyShotsMade());
        assertEquals(playerStatsSummary.getTeamId(), response.getTeamId());
    }

    @Test
    public void fetchAll_ShouldReturnSameNumberOfResponsesAsPlayerStatsSummariesReturnedByRepository() {
        final List<PlayerStatsSummary> playerStatsSummaryList = Arrays.asList(
                new PlayerStatsSummary(),
                new PlayerStatsSummary(),
                new PlayerStatsSummary(),
                new PlayerStatsSummary()
        );

        when(repository.findAll(ArgumentMatchers.<Example<PlayerStatsSummary>>any()))
                .thenReturn(playerStatsSummaryList);

        final List<PlayerStatsSummaryResponse> result = service.fetchAll(new PlayerStatsSummarySearchRequest());

        assertEquals(playerStatsSummaryList.size(), result.size());
    }

    private PlayerStatsSummary generatePlayerStatsSummary(final GameType gameType) {
        return PlayerStatsSummary.builder()
                .year("2002")
                .type(gameType.getValue())
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
    }
}