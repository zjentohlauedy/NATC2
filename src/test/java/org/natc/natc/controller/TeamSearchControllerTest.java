package org.natc.natc.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.natc.entity.domain.Team;
import org.natc.natc.entity.response.ResponseEnvelope;
import org.natc.natc.service.TeamSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamSearchControllerTest {
    @Mock
    private TeamSearchService teamSearchService;

    @InjectMocks
    private TeamSearchController teamSearchController;

    @Test
    public void search_ShouldReturnOKResponse() {
        final ResponseEntity<ResponseEnvelope<Team>> response = teamSearchController.search();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void search_ShouldCallTeamSearchService() {
        teamSearchController.search();

        verify(teamSearchService).execute();
    }

    @Test
    public void search_ShouldResponseWithEnvelopContainingTeamsReturnedBySearchService() {
        final List<Team> teamList = Collections.emptyList();

        when(teamSearchService.execute()).thenReturn(teamList);

        final ResponseEntity<ResponseEnvelope<Team>> response = teamSearchController.search();

        assertEquals(teamList, response.getBody().getResources());
    }
}