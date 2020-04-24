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
import org.natc.app.entity.domain.TeamGame;
import org.natc.app.entity.request.TeamGameSearchRequest;
import org.natc.app.entity.response.TeamGameResponse;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamGameSearchServiceTest {

    @Captor
    private ArgumentCaptor<Example<TeamGame>> captor;

    @Mock
    private JpaRepository repository;

    @InjectMocks
    private TeamGameSearchService service;

    @Test
    public void fetchAll_ShouldReturnAListOfTeamGameResponses() {
        final List<TeamGameResponse> result = service.fetchAll(new TeamGameSearchRequest());

        assertEquals(0, result.size());
    }

    @Test
    public void fetchAll_ShouldCallTheTeamGameRepositoryWithAnExampleTeamGame() {
        service.fetchAll(new TeamGameSearchRequest());

        verify(repository).findAll(ArgumentMatchers.<Example<TeamGame>>any());
    }

    @Test
    public void fetchAll_ShouldCallTeamRepositoryWithExampleTeamBasedOnRequest() {
        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(25)
                .year("1994")
                .datestamp(LocalDate.now())
                .type(GameType.REGULAR_SEASON)
                .teamId(33)
                .opponent(12)
                .build();

        service.fetchAll(request);

        verify(repository).findAll(captor.capture());

        final TeamGame teamGame = captor.getValue().getProbe();

        assertEquals(request.getGameId(), teamGame.getGameId());
        assertEquals(request.getYear(), teamGame.getYear());
        assertEquals(request.getDatestamp(), teamGame.getDatestamp());
        assertEquals(request.getType().getValue(), teamGame.getType());
        assertEquals(request.getTeamId(), teamGame.getTeamId());
        assertEquals(request.getOpponent(), teamGame.getOpponent());
    }

    @Test
    public void fetchAll_ShouldReturnResponsesMappedFromTheTeamGamesReturnedByRepository() {
        final TeamGame teamGame = generateTeamGame(GameType.PRESEASON);

        when(repository.findAll(ArgumentMatchers.<Example<TeamGame>>any())).thenReturn(Collections.singletonList(teamGame));

        final List<TeamGameResponse> result = service.fetchAll(new TeamGameSearchRequest());

        assertEquals(1, result.size());

        final TeamGameResponse response = result.get(0);

        assertEquals(teamGame.getGameId(), response.getGameId());
        assertEquals(teamGame.getYear(), response.getYear());
        assertEquals(teamGame.getDatestamp(), response.getDatestamp());
        assertEquals(GameType.PRESEASON, response.getType());
        assertEquals(teamGame.getPlayoffRound(), response.getPlayoffRound());
        assertEquals(teamGame.getTeamId(), response.getTeamId());
        assertEquals(teamGame.getOpponent(), response.getOpponent());
        assertEquals(teamGame.getPossessions(), response.getPossessions());
        assertEquals(teamGame.getPossessionTime(), response.getPossessionTime());
        assertEquals(teamGame.getAttempts(), response.getAttempts());
        assertEquals(teamGame.getGoals(), response.getGoals());
        assertEquals(teamGame.getTurnovers(), response.getTurnovers());
        assertEquals(teamGame.getSteals(), response.getSteals());
        assertEquals(teamGame.getPenalties(), response.getPenalties());
        assertEquals(teamGame.getOffensivePenalties(), response.getOffensivePenalties());
        assertEquals(teamGame.getPenaltyShotsAttempted(), response.getPenaltyShotsAttempted());
        assertEquals(teamGame.getPenaltyShotsMade(), response.getPenaltyShotsMade());
        assertEquals(teamGame.getOvertimePenaltyShotsAttempted(), response.getOvertimePenaltyShotsAttempted());
        assertEquals(teamGame.getOvertimePenaltyShotsMade(), response.getOvertimePenaltyShotsMade());
        assertEquals(teamGame.getPeriod1Score(), response.getPeriod1Score());
        assertEquals(teamGame.getPeriod2Score(), response.getPeriod2Score());
        assertEquals(teamGame.getPeriod3Score(), response.getPeriod3Score());
        assertEquals(teamGame.getPeriod4Score(), response.getPeriod4Score());
        assertEquals(teamGame.getPeriod5Score(), response.getPeriod5Score());
        assertEquals(teamGame.getOvertimeScore(), response.getOvertimeScore());
        assertEquals(teamGame.getTotalScore(), response.getTotalScore());

        assertNotNull(response.getRoad());
        assertNotNull(response.getOvertime());
        assertNotNull(response.getWin());
    }

    @Test
    public void fetchAll_ShouldReturnSameNumberOfResponsesAsTeamGamesReturnedByRepository() {
        final List<TeamGame> teamGameList = Arrays.asList(
                new TeamGame(),
                new TeamGame(),
                new TeamGame(),
                new TeamGame()
        );

        when(repository.findAll(ArgumentMatchers.<Example<TeamGame>>any())).thenReturn(teamGameList);

        final List<TeamGameResponse> result = service.fetchAll(new TeamGameSearchRequest());

        assertEquals(teamGameList.size(), result.size());
    }

    private TeamGame generateTeamGame(final GameType gameType) {
        return TeamGame.builder()
                .gameId(1234)
                .year("1984")
                .datestamp(LocalDate.now())
                .type(gameType.getValue())
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
    }
}