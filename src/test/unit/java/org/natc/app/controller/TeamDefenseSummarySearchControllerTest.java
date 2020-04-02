package org.natc.app.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.request.TeamDefenseSummarySearchRequest;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.entity.response.ResponseStatus;
import org.natc.app.entity.response.TeamDefenseSummaryResponse;
import org.natc.app.service.NATCService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamDefenseSummarySearchControllerTest {

    @Mock
    private NATCService searchService;

    @InjectMocks
    private TeamDefenseSummarySearchController controller;

    @Test
    public void search_ShouldReturnOKResponse() {
        final ResponseEntity<ResponseEnvelope<TeamDefenseSummaryResponse>> response = controller.search(null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithNotFoundStatusWhenNoRecordsAreFound() {
        final ResponseEntity<ResponseEnvelope<TeamDefenseSummaryResponse>> response = controller.search(null, null, null);

        assertEquals(ResponseStatus.NOT_FOUND, response.getBody().getStatus());
    }

    @Test
    public void search_ShouldCallTeamDefenseSummarySearchService() {
        controller.search(null, null, null);

        verify(searchService).fetchAll(any());
    }

    @Test
    public void search_ShouldConstructRequestObjectForSearchServiceUsingRequestParameters() {
        final String year = "1989";
        final GameType type = GameType.REGULAR_SEASON;
        final Integer teamId = 33;
        final ArgumentCaptor<TeamDefenseSummarySearchRequest> captor = ArgumentCaptor.forClass(TeamDefenseSummarySearchRequest.class);

        controller.search(year, type, teamId);

        verify(searchService).fetchAll(captor.capture());

        final TeamDefenseSummarySearchRequest request = captor.getValue();

        assertEquals(year, request.getYear());
        assertEquals(type, request.getType());
        assertEquals(teamId, request.getTeamId());
    }

    @Test
    public void search_ShouldRespondWithEnvelopContainingResponsesReturnedBySearchService() {
        final List<TeamDefenseSummaryResponse> teamDefenseSummaryList = Collections.singletonList(new TeamDefenseSummaryResponse());

        when(searchService.fetchAll(any(TeamDefenseSummarySearchRequest.class))).thenReturn(teamDefenseSummaryList);

        final ResponseEntity<ResponseEnvelope<TeamDefenseSummaryResponse>> response = controller.search(null, null, null);

        assertEquals(teamDefenseSummaryList, response.getBody().getResources());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithSuccessStatusWhenRecordsAreFound() {
        final List<TeamDefenseSummaryResponse> teamDefenseSummaryList = Collections.singletonList(new TeamDefenseSummaryResponse());

        when(searchService.fetchAll(any(TeamDefenseSummarySearchRequest.class))).thenReturn(teamDefenseSummaryList);

        final ResponseEntity<ResponseEnvelope<TeamDefenseSummaryResponse>> response = controller.search(null, null, null);

        assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
    }
}