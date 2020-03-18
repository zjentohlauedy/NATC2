package org.natc.natc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.natc.entity.domain.GameType;
import org.natc.natc.entity.domain.TeamOffenseSummary;
import org.natc.natc.entity.request.TeamOffenseSummaryRequest;
import org.natc.natc.entity.response.TeamOffenseSummaryResponse;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamOffenseSummarySearchServiceTest {

    @Captor
    private ArgumentCaptor<Example<TeamOffenseSummary>> captor;

    @Mock
    private JpaRepository repository;

    @InjectMocks
    private TeamOffenseSummarySearchService service;

    @Test
    public void fetchAll_ShouldReturnAListOfTeamOffenseSummaryResponses() {
        final List<TeamOffenseSummaryResponse> result = service.fetchAll(new TeamOffenseSummaryRequest());

        assertEquals(0, result.size());
    }

    @Test
    public void fetchAll_ShouldCallTheRepositoryWithAnExampleTeamOffenseSummary() {
        service.fetchAll(new TeamOffenseSummaryRequest());

        verify(repository).findAll(ArgumentMatchers.<Example<TeamOffenseSummary>>any());
    }

    @Test
    public void fetchAll_ShouldCallRepositoryWithExampleBasedOnRequest() {
        final TeamOffenseSummaryRequest request = TeamOffenseSummaryRequest.builder().year("2002").teamId(123).build();

        service.fetchAll(request);

        verify(repository).findAll(captor.capture());

        final TeamOffenseSummary teamOffenseSummary = captor.getValue().getProbe();

        assertEquals(request.getYear(), teamOffenseSummary.getYear());
        assertEquals(request.getTeamId(), teamOffenseSummary.getTeamId());
    }

    @Test
    public void fetchAll_ShouldReturnResponsesMappedFromTheTeamOffenseSummariesReturnedByRepository() {
        final TeamOffenseSummary teamOffenseSummary = generateTeamOffenseSummary(GameType.REGULAR_SEASON);

        when(repository.findAll(ArgumentMatchers.<Example<TeamOffenseSummary>>any()))
                .thenReturn(Collections.singletonList(teamOffenseSummary));

        final List<TeamOffenseSummaryResponse> result = service.fetchAll(new TeamOffenseSummaryRequest());

        assertEquals(1, result.size());

        final TeamOffenseSummaryResponse response = result.get(0);

        assertEquals(teamOffenseSummary.getYear(), response.getYear());
        assertEquals(GameType.REGULAR_SEASON, response.getType());
        assertEquals(teamOffenseSummary.getTeamId(), response.getTeamId());
        assertEquals(teamOffenseSummary.getGames(), response.getGames());
        assertEquals(teamOffenseSummary.getPossessions(), response.getPossessions());
        assertEquals(teamOffenseSummary.getPossessionTime(), response.getPossessionTime());
        assertEquals(teamOffenseSummary.getAttempts(), response.getAttempts());
        assertEquals(teamOffenseSummary.getGoals(), response.getGoals());
        assertEquals(teamOffenseSummary.getTurnovers(), response.getTurnovers());
        assertEquals(teamOffenseSummary.getSteals(), response.getSteals());
        assertEquals(teamOffenseSummary.getPenalties(), response.getPenalties());
        assertEquals(teamOffenseSummary.getOffensivePenalties(), response.getOffensivePenalties());
        assertEquals(teamOffenseSummary.getPenaltyShotsAttempted(), response.getPenaltyShotsAttempted());
        assertEquals(teamOffenseSummary.getPenaltyShotsMade(), response.getPenaltyShotsMade());
        assertEquals(teamOffenseSummary.getOvertimePenaltyShotsAttempted(), response.getOvertimePenaltyShotsAttempted());
        assertEquals(teamOffenseSummary.getOvertimePenaltyShotsMade(), response.getOvertimePenaltyShotsMade());
        assertEquals(teamOffenseSummary.getScore(), response.getScore());

    }

    @Test
    public void fetchAll_ShouldReturnSameNumberOfResponsesAsTeamOffenseSummariesReturnedByRepository() {
        final List<TeamOffenseSummary> teamOffenseSummaryList = Arrays.asList(
                new TeamOffenseSummary(),
                new TeamOffenseSummary(),
                new TeamOffenseSummary(),
                new TeamOffenseSummary()
        );

        when(repository.findAll(ArgumentMatchers.<Example<TeamOffenseSummary>>any())).thenReturn(teamOffenseSummaryList);

        final List<TeamOffenseSummaryResponse> result = service.fetchAll(new TeamOffenseSummaryRequest());

        assertEquals(teamOffenseSummaryList.size(), result.size());
    }

    private TeamOffenseSummary generateTeamOffenseSummary(final GameType gameType) {
        return TeamOffenseSummary.builder()
                .year("2016")
                .type(gameType.getValue())
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
    }
}