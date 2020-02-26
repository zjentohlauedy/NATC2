package org.natc.natc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.natc.entity.domain.Team;
import org.natc.natc.repository.TeamRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TeamSearchServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamSearchService teamSearchService;

    @Test
    public void execute_ShouldReturnAListOfTeams() {
        final List<Team> result = teamSearchService.execute();

        assertEquals(0, result.size());
    }

    @Test
    public void execute_ShouldCallTheTeamRepository() {
        teamSearchService.execute();

        verify(teamRepository).findAll();
    }
}