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
import org.natc.natc.repository.TeamRepository;
import org.springframework.data.domain.Example;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TeamSearchServiceTest {
    @Captor
    private ArgumentCaptor<Example<Team>> captor;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamSearchService teamSearchService;

    @Test
    public void execute_ShouldReturnAListOfTeams() {
        final List<Team> result = teamSearchService.execute(null, null, null, null, null);

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
}