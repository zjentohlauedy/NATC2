package org.natc.natc.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.natc.entity.response.ResponseEnvelope;
import org.natc.natc.entity.response.ResponseStatus;
import org.natc.natc.entity.response.TeamResponse;
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
        final ResponseEntity<ResponseEnvelope<TeamResponse>> response = teamSearchController.search(null, null, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithSuccessStatusWhenRecordsAreFound() {
        final List<TeamResponse> teamList = Collections.singletonList(new TeamResponse());

        when(teamSearchService.execute(any(), any(), any(), any(), any())).thenReturn(teamList);

        final ResponseEntity<ResponseEnvelope<TeamResponse>> response = teamSearchController.search(null, null, null, null, null);

        assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithNotFoundStatusWhenNoRecordsAreFound() {
        final ResponseEntity<ResponseEnvelope<TeamResponse>> response = teamSearchController.search(null, null, null, null, null);

        assertEquals(ResponseStatus.NOT_FOUND, response.getBody().getStatus());
    }

    @Test
    public void search_ShouldCallTeamSearchService() {
        teamSearchController.search(null, null, null, null, null);

        verify(teamSearchService).execute(any(), any(), any(), any(), any());
    }

    @Test
    public void search_ShouldPassTeamIdFromRequestToSearchService() {
        final Integer teamId = 123;

        teamSearchController.search(teamId, null, null, null, null);

        verify(teamSearchService).execute(eq(teamId), any(), any(), any(), any());
    }

    @Test
    public void search_ShouldPassYearFromRequestToSearchService() {
        final String year = "1999";

        teamSearchController.search(null, year, null, null, null);

        verify(teamSearchService).execute(any(), eq(year), any(), any(), any());
    }

    @Test
    public void search_ShouldPassConferenceIdFromRequestToSearchService() {
        final Integer conferenceId = 2;

        teamSearchController.search(null, null, conferenceId, null, null);

        verify(teamSearchService).execute(any(), any(), eq(conferenceId), any(), any());
    }

    @Test
    public void search_ShouldPassDivisionIdFromRequestToSearchService() {
        final Integer divisionId = 2;

        teamSearchController.search(null, null, null, divisionId, null);

        verify(teamSearchService).execute(any(), any(), any(), eq(divisionId), any());
    }

    @Test
    public void search_ShouldPassAllstarTeamFromRequestToSearchService() {
        final Boolean allstarTeam = true;

        teamSearchController.search(null, null, null, null, allstarTeam);

        verify(teamSearchService).execute(any(), any(), any(), any(), eq(allstarTeam));
    }

    @Test
    public void search_ShouldRespondWithEnvelopContainingTeamsReturnedBySearchService() {
        final List<TeamResponse> teamList = Collections.emptyList();

        when(teamSearchService.execute(any(), any(), any(), any(), any())).thenReturn(teamList);

        final ResponseEntity<ResponseEnvelope<TeamResponse>> response = teamSearchController.search(null, null, null, null, null);

        assertEquals(teamList, response.getBody().getResources());
    }
}