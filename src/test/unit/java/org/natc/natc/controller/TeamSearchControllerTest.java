package org.natc.natc.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.natc.entity.request.TeamSearchRequest;
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

        when(teamSearchService.fetchAll(any(TeamSearchRequest.class))).thenReturn(teamList);

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

        verify(teamSearchService).fetchAll(any(TeamSearchRequest.class));
    }

    @Test
    public void search_ShouldConstructRequestObjectForTeamSearchServiceUsingRequestParameters() {
        final Integer teamId = 123;
        final String year = "1999";
        final Integer conferenceId = 1;
        final Integer divisionId = 2;
        final Boolean allstarTeam = true;
        final ArgumentCaptor<TeamSearchRequest> captor = ArgumentCaptor.forClass(TeamSearchRequest.class);

        teamSearchController.search(teamId, year, conferenceId, divisionId, allstarTeam);

        verify(teamSearchService).fetchAll(captor.capture());

        final TeamSearchRequest request = captor.getValue();

        assertEquals(teamId, request.getTeamId());
        assertEquals(year, request.getYear());
        assertEquals(conferenceId, request.getConferenceId());
        assertEquals(divisionId, request.getDivisionId());
        assertEquals(allstarTeam, request.getAllstarTeam());
    }

    @Test
    public void search_ShouldRespondWithEnvelopContainingTeamsReturnedBySearchService() {
        final List<TeamResponse> teamList = Collections.emptyList();

        when(teamSearchService.fetchAll(any(TeamSearchRequest.class))).thenReturn(teamList);

        final ResponseEntity<ResponseEnvelope<TeamResponse>> response = teamSearchController.search(null, null, null, null, null);

        assertEquals(teamList, response.getBody().getResources());
    }
}