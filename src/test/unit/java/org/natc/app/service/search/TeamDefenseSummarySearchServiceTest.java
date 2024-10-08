package org.natc.app.service.search;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.TeamDefenseSummary;
import org.natc.app.entity.request.TeamDefenseSummarySearchRequest;
import org.natc.app.entity.response.TeamDefenseSummaryResponse;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamDefenseSummarySearchServiceTest {

    @Captor
    private ArgumentCaptor<Example<TeamDefenseSummary>> captor;

    @Mock
    private JpaRepository repository;

    @InjectMocks
    private TeamDefenseSummarySearchService service;

    @Nested
    class FetchAll {

        @Test
        void shouldReturnAListOfTeamDefenseSummaryResponses() {
            final List<TeamDefenseSummaryResponse> result = service.fetchAll(new TeamDefenseSummarySearchRequest());

            assertEquals(0, result.size());
        }

        @Test
        void shouldCallTheRepositoryWithAnExampleTeamDefenseSummary() {
            service.fetchAll(new TeamDefenseSummarySearchRequest());

            verify(repository).findAll(ArgumentMatchers.<Example<TeamDefenseSummary>>any());
        }

        @Test
        void shouldCallRepositoryWithExampleBasedOnRequest() {
            final TeamDefenseSummarySearchRequest request = TeamDefenseSummarySearchRequest.builder()
                    .year("1992")
                    .type(GameType.POSTSEASON)
                    .teamId(16)
                    .build();

            service.fetchAll(request);

            verify(repository).findAll(captor.capture());

            final TeamDefenseSummary teamDefenseSummary = captor.getValue().getProbe();

            assertEquals(request.getYear(), teamDefenseSummary.getYear());
            assertEquals(request.getType().getValue(), teamDefenseSummary.getType());
            assertEquals(request.getTeamId(), teamDefenseSummary.getTeamId());
        }

        @Test
        void shouldReturnResponsesMappedFromTheTeamDefenseSummariesReturnedByRepository() {
            final TeamDefenseSummary teamDefenseSummary = generateTeamDefenseSummary(GameType.ALLSTAR);

            when(repository.findAll(ArgumentMatchers.<Example<TeamDefenseSummary>>any()))
                    .thenReturn(Collections.singletonList(teamDefenseSummary));

            final List<TeamDefenseSummaryResponse> result = service.fetchAll(new TeamDefenseSummarySearchRequest());

            assertEquals(1, result.size());

            final TeamDefenseSummaryResponse response = result.getFirst();

            assertEquals(teamDefenseSummary.getYear(), response.getYear());
            assertEquals(GameType.ALLSTAR, response.getType());
            assertEquals(teamDefenseSummary.getTeamId(), response.getTeamId());
            assertEquals(teamDefenseSummary.getGames(), response.getGames());
            assertEquals(teamDefenseSummary.getPossessions(), response.getPossessions());
            assertEquals(teamDefenseSummary.getPossessionTime(), response.getPossessionTime());
            assertEquals(teamDefenseSummary.getAttempts(), response.getAttempts());
            assertEquals(teamDefenseSummary.getGoals(), response.getGoals());
            assertEquals(teamDefenseSummary.getTurnovers(), response.getTurnovers());
            assertEquals(teamDefenseSummary.getSteals(), response.getSteals());
            assertEquals(teamDefenseSummary.getPenalties(), response.getPenalties());
            assertEquals(teamDefenseSummary.getOffensivePenalties(), response.getOffensivePenalties());
            assertEquals(teamDefenseSummary.getPenaltyShotsAttempted(), response.getPenaltyShotsAttempted());
            assertEquals(teamDefenseSummary.getPenaltyShotsMade(), response.getPenaltyShotsMade());
            assertEquals(teamDefenseSummary.getOvertimePenaltyShotsAttempted(), response.getOvertimePenaltyShotsAttempted());
            assertEquals(teamDefenseSummary.getOvertimePenaltyShotsMade(), response.getOvertimePenaltyShotsMade());
            assertEquals(teamDefenseSummary.getScore(), response.getScore());
        }

        @Test
        void shouldReturnSameNumberOfResponsesAsTeamDefenseSummariesReturnedByRepository() {
            final List<TeamDefenseSummary> teamDefenseSummaryList = List.of(
                    new TeamDefenseSummary(),
                    new TeamDefenseSummary(),
                    new TeamDefenseSummary(),
                    new TeamDefenseSummary()
            );

            when(repository.findAll(ArgumentMatchers.<Example<TeamDefenseSummary>>any()))
                    .thenReturn(teamDefenseSummaryList);

            final List<TeamDefenseSummaryResponse> result = service.fetchAll(new TeamDefenseSummarySearchRequest());

            assertEquals(teamDefenseSummaryList.size(), result.size());
        }

        private TeamDefenseSummary generateTeamDefenseSummary(final GameType gameType) {
            return TeamDefenseSummary.builder()
                    .year("1997")
                    .type(gameType.getValue())
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
        }
    }
}