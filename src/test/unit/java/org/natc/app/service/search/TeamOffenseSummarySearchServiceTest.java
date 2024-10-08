package org.natc.app.service.search;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.TeamOffenseSummary;
import org.natc.app.entity.request.TeamOffenseSummarySearchRequest;
import org.natc.app.entity.response.TeamOffenseSummaryResponse;
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

    @Nested
    class FetchAll {

        @Test
        void shouldReturnAListOfTeamOffenseSummaryResponses() {
            final List<TeamOffenseSummaryResponse> result = service.fetchAll(new TeamOffenseSummarySearchRequest());

            assertEquals(0, result.size());
        }

        @Test
        void shouldCallTheRepositoryWithAnExampleTeamOffenseSummary() {
            service.fetchAll(new TeamOffenseSummarySearchRequest());

            verify(repository).findAll(ArgumentMatchers.<Example<TeamOffenseSummary>>any());
        }

        @Test
        void shouldCallRepositoryWithExampleBasedOnRequest() {
            final TeamOffenseSummarySearchRequest request = TeamOffenseSummarySearchRequest.builder()
                    .year("2002")
                    .type(GameType.ALLSTAR)
                    .teamId(123)
                    .build();

            service.fetchAll(request);

            verify(repository).findAll(captor.capture());

            final TeamOffenseSummary teamOffenseSummary = captor.getValue().getProbe();

            assertEquals(request.getYear(), teamOffenseSummary.getYear());
            assertEquals(request.getType().getValue(), teamOffenseSummary.getType());
            assertEquals(request.getTeamId(), teamOffenseSummary.getTeamId());
        }

        @Test
        void shouldReturnResponsesMappedFromTheTeamOffenseSummariesReturnedByRepository() {
            final TeamOffenseSummary teamOffenseSummary = generateTeamOffenseSummary(GameType.REGULAR_SEASON);

            when(repository.findAll(ArgumentMatchers.<Example<TeamOffenseSummary>>any()))
                    .thenReturn(Collections.singletonList(teamOffenseSummary));

            final List<TeamOffenseSummaryResponse> result = service.fetchAll(new TeamOffenseSummarySearchRequest());

            assertEquals(1, result.size());

            final TeamOffenseSummaryResponse response = result.getFirst();

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
        void shouldReturnSameNumberOfResponsesAsTeamOffenseSummariesReturnedByRepository() {
            final List<TeamOffenseSummary> teamOffenseSummaryList = List.of(
                    new TeamOffenseSummary(),
                    new TeamOffenseSummary(),
                    new TeamOffenseSummary(),
                    new TeamOffenseSummary()
            );

            when(repository.findAll(ArgumentMatchers.<Example<TeamOffenseSummary>>any())).thenReturn(teamOffenseSummaryList);

            final List<TeamOffenseSummaryResponse> result = service.fetchAll(new TeamOffenseSummarySearchRequest());

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
}