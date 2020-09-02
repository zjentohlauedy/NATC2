package org.natc.app.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.request.TeamOffenseSummarySearchRequest;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.entity.response.ResponseStatus;
import org.natc.app.entity.response.TeamOffenseSummaryResponse;
import org.natc.app.service.NATCSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamOffenseSummarySearchControllerTest {

    @Mock
    private NATCSearchService searchService;

    @InjectMocks
    private TeamOffenseSummarySearchController controller;

    @Test
    public void search_ShouldReturnOKResponse() {
        final ResponseEntity<ResponseEnvelope<TeamOffenseSummaryResponse>> response = controller.search(null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithNotFoundStatusWhenNoRecordsAreFound() {
        final ResponseEntity<ResponseEnvelope<TeamOffenseSummaryResponse>> response = controller.search(null, null, null);

        assertEquals(ResponseStatus.NOT_FOUND, response.getBody().getStatus());
    }

    @Test
    public void search_ShouldCallTeamOffenseSummarySearchService() {
        controller.search(null, null, null);

        verify(searchService).fetchAll(any(TeamOffenseSummarySearchRequest.class));
    }

    @Test
    public void search_ShouldConstructRequestObjectForSearchServiceUsingRequestParameters() {
        final String year = "2004";
        final GameType type = GameType.POSTSEASON;
        final Integer teamId = 20;
        final ArgumentCaptor<TeamOffenseSummarySearchRequest> captor = ArgumentCaptor.forClass(TeamOffenseSummarySearchRequest.class);

        controller.search(year, type, teamId);

        verify(searchService).fetchAll(captor.capture());

        final TeamOffenseSummarySearchRequest request = captor.getValue();

        assertEquals(year, request.getYear());
        assertEquals(type, request.getType());
        assertEquals(teamId, request.getTeamId());
    }

    @Test
    public void search_ShouldRespondWithEnvelopContainingResponsesReturnedBySearchService() {
        final List<TeamOffenseSummaryResponse> teamOffenseSummaryList = Collections.singletonList(new TeamOffenseSummaryResponse());

        when(searchService.fetchAll(any(TeamOffenseSummarySearchRequest.class))).thenReturn(teamOffenseSummaryList);

        final ResponseEntity<ResponseEnvelope<TeamOffenseSummaryResponse>> response = controller.search(null, null, null);

        assertEquals(teamOffenseSummaryList, response.getBody().getResources());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithSuccessStatusWhenRecordsAreFound() {
        final List<TeamOffenseSummaryResponse> teamOffenseSummaryList = Collections.singletonList(new TeamOffenseSummaryResponse());

        when(searchService.fetchAll(any(TeamOffenseSummarySearchRequest.class))).thenReturn(teamOffenseSummaryList);

        final ResponseEntity<ResponseEnvelope<TeamOffenseSummaryResponse>> response = controller.search(null, null, null);

        assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
    }
}