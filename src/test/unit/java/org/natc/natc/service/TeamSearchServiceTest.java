package org.natc.natc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.natc.entity.domain.Team;
import org.natc.natc.entity.response.TeamResponse;
import org.natc.natc.repository.TeamRepository;
import org.springframework.data.domain.Example;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamSearchServiceTest {
    @Captor
    private ArgumentCaptor<Example<Team>> captor;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamSearchService teamSearchService;

    @Test
    public void execute_ShouldReturnAListOfTeamResponses() {
        final List<TeamResponse> result = teamSearchService.execute(null, null, null, null, null);

        assertEquals(0, result.size());
    }

    @Test
    public void execute_ShouldCallTheTeamRepositoryWithAnExampleTeam() {
        teamSearchService.execute(null, null, null, null, null);

        verify(teamRepository).findAll(ArgumentMatchers.<Example<Team>>any());
    }

    @Test
    public void execute_ShouldCallTeamRepositorWithExampleTeamBasedOnInputArgs() {
        final Integer teamId = 123;
        final String year = "1999";
        final Integer conferenceId = 1;
        final Integer divisionId = 2;
        final Boolean allstarTeam = false;

        teamSearchService.execute(teamId, year, conferenceId, divisionId, allstarTeam);

        verify(teamRepository).findAll(captor.capture());
        
        Team team = captor.getValue().getProbe();

        assertEquals(teamId, team.getTeamId());
        assertEquals(year, team.getYear());
        assertEquals(conferenceId, team.getConference());
        assertEquals(divisionId, team.getDivision());
        assertNotNull(team.getAllstarTeam());
    }
    
    @Test
    public void execute_ShouldSetAllstarTeamTo0WhenArgIsFalse() {
        final Boolean allstarTeam = false;

        teamSearchService.execute(null, null, null, null, allstarTeam);

        verify(teamRepository).findAll(captor.capture());

        assertEquals(0, captor.getValue().getProbe().getAllstarTeam());
    }

    @Test
    public void execute_ShouldSetAllstarTeamTo1WhenArgIsTrue() {
        final Boolean allstarTeam = true;

        teamSearchService.execute(null, null, null, null, allstarTeam);

        verify(teamRepository).findAll(captor.capture());

        assertEquals(1, captor.getValue().getProbe().getAllstarTeam());
    }

    @Test
    public void execute_ShouldSetAllstarTeamToNullWhenArgIsNull() {
        teamSearchService.execute(null, null, null, null, null);

        verify(teamRepository).findAll(captor.capture());

        assertNull(captor.getValue().getProbe().getAllstarTeam());
    }

    @Test
    public void execute_ShouldReturnTeamResponsesMappedFromTheTeamsReturnedByRepository() {
        final Team team = generateTeam();

        when(teamRepository.findAll(ArgumentMatchers.<Example<Team>>any())).thenReturn(Collections.singletonList(team));

        final List<TeamResponse> result = teamSearchService.execute(null, null, null, null, null);

        assertEquals(1, result.size());

        final TeamResponse response = result.get(0);

        assertEquals(team.getTeamId(), response.getTeamId());
        assertEquals(team.getYear(), response.getYear());
        assertEquals(team.getLocation(), response.getLocation());
        assertEquals(team.getName(), response.getName());
        assertEquals(team.getAbbreviation(), response.getAbbreviation());
        assertEquals(team.getTimeZone(), response.getTimeZone());
        assertEquals(team.getGameTime(), response.getGameTime());
        assertEquals(team.getConference(), response.getConferenceId());
        assertEquals(team.getDivision(), response.getDivisionId());
        assertEquals(team.getPreseasonGames(), response.getPreseasonGames());
        assertEquals(team.getPreseasonWins(), response.getPreseasonWins());
        assertEquals(team.getPreseasonLosses(), response.getPreseasonLosses());
        assertEquals(team.getGames(), response.getGames());
        assertEquals(team.getWins(), response.getWins());
        assertEquals(team.getLosses(), response.getLosses());
        assertEquals(team.getDivisionWins(), response.getDivisionWins());
        assertEquals(team.getDivisionLosses(), response.getDivisionLosses());
        assertEquals(team.getOutOfConferenceWins(), response.getOutOfConferenceWins());
        assertEquals(team.getOutOfConferenceLosses(), response.getOutOfConferenceLosses());
        assertEquals(team.getOvertimeWins(), response.getOvertimeWins());
        assertEquals(team.getOvertimeLosses(), response.getOvertimeLosses());
        assertEquals(team.getRoadWins(), response.getRoadWins());
        assertEquals(team.getRoadLosses(), response.getRoadLosses());
        assertEquals(team.getHomeWins(), response.getHomeWins());
        assertEquals(team.getHomeLosses(), response.getHomeLosses());
        assertEquals(team.getDivisionRank(), response.getDivisionRank());
        assertEquals(team.getPlayoffRank(), response.getPlayoffRank());
        assertEquals(team.getPlayoffGames(), response.getPlayoffGames());
        assertEquals(team.getRound1Wins(), response.getRound1Wins());
        assertEquals(team.getRound2Wins(), response.getRound2Wins());
        assertEquals(team.getRound3Wins(), response.getRound3Wins());
        assertEquals(team.getExpectation(), response.getExpectation());
        assertEquals(team.getDrought(), response.getDrought());
    }

    @Test
    public void execute_ShouldMapAllstarTeamValueFromIntegerToBooleanInReponseWhenFalse() {
        final Team team = Team.builder().allstarTeam(0).build();

        when(teamRepository.findAll(ArgumentMatchers.<Example<Team>>any())).thenReturn(Collections.singletonList(team));

        final List<TeamResponse> result = teamSearchService.execute(null, null, null, null, null);

        assertEquals(1, result.size());

        final TeamResponse response = result.get(0);

        assertEquals(false, response.getAllstarTeam());
    }

    @Test
    public void execute_ShouldMapAllstarTeamValueFromIntegerToBooleanInReponseWhenTrue() {
        final Team team = Team.builder().allstarTeam(1).build();

        when(teamRepository.findAll(ArgumentMatchers.<Example<Team>>any())).thenReturn(Collections.singletonList(team));

        final List<TeamResponse> result = teamSearchService.execute(null, null, null, null, null);

        assertEquals(1, result.size());

        final TeamResponse response = result.get(0);

        assertEquals(true, response.getAllstarTeam());
    }

    @Test
    public void execute_ShouldMapAllstarTeamValueFromIntegerToBooleanInReponseWhenNull() {
        final Team team = new Team();

        when(teamRepository.findAll(ArgumentMatchers.<Example<Team>>any())).thenReturn(Collections.singletonList(team));

        final List<TeamResponse> result = teamSearchService.execute(null, null, null, null, null);

        assertEquals(1, result.size());

        final TeamResponse response = result.get(0);

        assertNull(response.getAllstarTeam());
    }

    @Test
    public void execute_ShouldReturnSameNumberOfResponsesAsTeamsReturnedByRepository() {
        final List<Team> teamList = Arrays.asList(new Team(), new Team(), new Team());

        when(teamRepository.findAll(ArgumentMatchers.<Example<Team>>any())).thenReturn(teamList);

        final List<TeamResponse> result = teamSearchService.execute(null, null, null, null, null);

        assertEquals(teamList.size(), result.size());
    }

    private Team generateTeam() {
        return Team.builder()
                .teamId(123)
                .year("1999")
                .location("Here")
                .name("Them")
                .abbreviation("ABC.")
                .timeZone("Americas/Los_Angeles")
                .gameTime(999)
                .conference(111)
                .division(222)
                .preseasonGames(12)
                .preseasonWins(7)
                .preseasonLosses(5)
                .games(99)
                .wins(55)
                .losses(44)
                .divisionWins(33)
                .divisionLosses(22)
                .outOfConferenceWins(66)
                .outOfConferenceLosses(11)
                .overtimeWins(9)
                .overtimeLosses(3)
                .roadWins(88)
                .roadLosses(77)
                .homeWins(35)
                .homeLosses(46)
                .divisionRank(6)
                .playoffRank(15)
                .playoffGames(14)
                .round1Wins(13)
                .round2Wins(6)
                .round3Wins(4)
                .expectation(0.432)
                .drought(17)
                .build();
    }
}