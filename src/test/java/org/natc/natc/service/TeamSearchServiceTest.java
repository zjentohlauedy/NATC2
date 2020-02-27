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
        final List<Team> result = teamSearchService.execute(null);

        assertEquals(0, result.size());
    }

    @Test
    public void execute_ShouldCallTheTeamRepository() {
        teamSearchService.execute(null);

        verify(teamRepository).findAll(ArgumentMatchers.<Example<Team>>any());
    }

    @Test
    public void execute_ShouldCallTeamRepositorWithExampleTeamBasedOnInputArgs() {
        final Integer teamId = 123;

        teamSearchService.execute(teamId);

        verify(teamRepository).findAll(captor.capture());

        assertEquals(teamId, captor.getValue().getProbe().getTeamId());
    }
}