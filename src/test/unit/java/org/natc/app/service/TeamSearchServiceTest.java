package org.natc.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.Team;
import org.natc.app.entity.request.TeamSearchRequest;
import org.natc.app.entity.response.TeamResponse;
import org.natc.app.service.search.TeamSearchService;
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
class TeamSearchServiceTest {
    @Captor
    private ArgumentCaptor<Example<Team>> captor;

    @Mock
    private JpaRepository teamRepository;

    @InjectMocks
    private TeamSearchService teamSearchService;

    @Test
    public void fetchAll_ShouldReturnAListOfTeamResponses() {
        final List<TeamResponse> result = teamSearchService.fetchAll(new TeamSearchRequest());

        assertEquals(0, result.size());
    }

    @Test
    public void fetchAll_ShouldCallTheTeamRepositoryWithAnExampleTeam() {
        teamSearchService.fetchAll(new TeamSearchRequest());

        verify(teamRepository).findAll(ArgumentMatchers.<Example<Team>>any());
    }

    @Test
    public void fetchAll_ShouldCallTeamRepositoryWithExampleTeamBasedOnRequest() {
        final TeamSearchRequest request = TeamSearchRequest.builder()
                .teamId(123)
                .year("1999")
                .conferenceId(1)
                .divisionId(2)
                .allstarTeam(false)
                .build();

        teamSearchService.fetchAll(request);

        verify(teamRepository).findAll(captor.capture());
        
        final Team team = captor.getValue().getProbe();

        assertEquals(request.getTeamId(), team.getTeamId());
        assertEquals(request.getYear(), team.getYear());
        assertEquals(request.getConferenceId(), team.getConference());
        assertEquals(request.getDivisionId(), team.getDivision());

        assertNotNull(team.getAllstarTeam());
    }
    
    @Test
    public void fetchAll_ShouldSetAllstarTeamTo0WhenArgIsFalse() {
        final TeamSearchRequest request = TeamSearchRequest.builder().allstarTeam(false).build();

        teamSearchService.fetchAll(request);

        verify(teamRepository).findAll(captor.capture());

        assertEquals(0, captor.getValue().getProbe().getAllstarTeam());
    }

    @Test
    public void fetchAll_ShouldSetAllstarTeamTo1WhenArgIsTrue() {
        final TeamSearchRequest request = TeamSearchRequest.builder().allstarTeam(true).build();

        teamSearchService.fetchAll(request);

        verify(teamRepository).findAll(captor.capture());

        assertEquals(1, captor.getValue().getProbe().getAllstarTeam());
    }

    @Test
    public void fetchAll_ShouldSetAllstarTeamToNullWhenArgIsNull() {
        teamSearchService.fetchAll(new TeamSearchRequest());

        verify(teamRepository).findAll(captor.capture());

        assertNull(captor.getValue().getProbe().getAllstarTeam());
    }

    @Test
    public void fetchAll_ShouldReturnTeamResponsesMappedFromTheTeamsReturnedByRepository() {
        final Team team = generateTeam();

        when(teamRepository.findAll(ArgumentMatchers.<Example<Team>>any())).thenReturn(Collections.singletonList(team));

        final List<TeamResponse> result = teamSearchService.fetchAll(new TeamSearchRequest());

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

        assertNotNull(response.getAllstarTeam());
    }

    @Test
    public void fetchAll_ShouldMapAllstarTeamValueFromIntegerToBooleanInResponseWhenFalse() {
        final Team team = Team.builder().allstarTeam(0).build();

        when(teamRepository.findAll(ArgumentMatchers.<Example<Team>>any())).thenReturn(Collections.singletonList(team));

        final List<TeamResponse> result = teamSearchService.fetchAll(new TeamSearchRequest());

        assertEquals(1, result.size());

        final TeamResponse response = result.get(0);

        assertFalse(response.getAllstarTeam());
    }

    @Test
    public void fetchAll_ShouldMapAllstarTeamValueFromIntegerToBooleanInResponseWhenTrue() {
        final Team team = Team.builder().allstarTeam(1).build();

        when(teamRepository.findAll(ArgumentMatchers.<Example<Team>>any())).thenReturn(Collections.singletonList(team));

        final List<TeamResponse> result = teamSearchService.fetchAll(new TeamSearchRequest());

        assertEquals(1, result.size());

        final TeamResponse response = result.get(0);

        assertTrue(response.getAllstarTeam());
    }

    @Test
    public void fetchAll_ShouldMapAllstarTeamValueFromIntegerToBooleanInResponseWhenNull() {
        final Team team = new Team();

        when(teamRepository.findAll(ArgumentMatchers.<Example<Team>>any())).thenReturn(Collections.singletonList(team));

        final List<TeamResponse> result = teamSearchService.fetchAll(new TeamSearchRequest());

        assertEquals(1, result.size());

        final TeamResponse response = result.get(0);

        assertNull(response.getAllstarTeam());
    }

    @Test
    public void fetchAll_ShouldReturnSameNumberOfResponsesAsTeamsReturnedByRepository() {
        final List<Team> teamList = Arrays.asList(new Team(), new Team(), new Team());

        when(teamRepository.findAll(ArgumentMatchers.<Example<Team>>any())).thenReturn(teamList);

        final List<TeamResponse> result = teamSearchService.fetchAll(new TeamSearchRequest());

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
                .allstarTeam(1)
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