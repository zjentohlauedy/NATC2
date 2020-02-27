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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        final ResponseEntity<ResponseEnvelope<Team>> response = teamSearchController.search(null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void search_ShouldCallTeamSearchService() {
        teamSearchController.search(null, null);

        verify(teamSearchService).execute(any(), any());
    }

    @Test
    public void search_ShouldPassTeamIdFromRequestToSearchService() {
        final Integer teamId = 123;

        teamSearchController.search(teamId, null);

        verify(teamSearchService).execute(eq(teamId), any());
    }

    @Test
    public void search_ShouldPassYearFromRequestToSearchService() {
        final String year = "1999";

        teamSearchController.search(null, year);

        verify(teamSearchService).execute(any(), eq(year));
    }

    @Test
    public void search_ShouldRespondWithEnvelopContainingTeamsReturnedBySearchService() {
        final List<Team> teamList = Collections.emptyList();

        when(teamSearchService.execute(any(), any())).thenReturn(teamList);

        final ResponseEntity<ResponseEnvelope<Team>> response = teamSearchController.search(null, null);

        assertEquals(teamList, response.getBody().getResources());
    }
}