package org.natc.app.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.request.TeamGameSearchRequest;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.entity.response.ResponseStatus;
import org.natc.app.entity.response.TeamGameResponse;
import org.natc.app.service.TeamGameSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamGameSearchControllerTest {

    @Mock
    private TeamGameSearchService searchService;

    @InjectMocks
    private TeamGameSearchController controller;

    @Test
    public void search_ShouldReturnOKResponse() {
        final ResponseEntity<ResponseEnvelope<TeamGameResponse>> response = controller.search(null, null, null, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithNotFoundStatusWhenNoRecordsAreFound() {
        final ResponseEntity<ResponseEnvelope<TeamGameResponse>> response = controller.search(null, null, null, null, null, null);

        assertEquals(ResponseStatus.NOT_FOUND, response.getBody().getStatus());
    }

    @Test
    public void search_ShouldCallTeamGameSearchService() {
        controller.search(null, null, null, null, null, null);

        verify(searchService).fetchAll(any(TeamGameSearchRequest.class));
    }

    @Test
    public void search_ShouldConstructRequestObjectForSearchServiceUsingRequestParameters() {
        final Integer gameId = 24;
        final String year = "1998";
        final LocalDate datestamp = LocalDate.now();
        final GameType type = GameType.REGULAR_SEASON;
        final Integer teamId = 16;
        final Integer opponent = 33;
        final ArgumentCaptor<TeamGameSearchRequest> captor = ArgumentCaptor.forClass(TeamGameSearchRequest.class);

        controller.search(gameId, year, datestamp, type, teamId, opponent);

        verify(searchService).fetchAll(captor.capture());

        final TeamGameSearchRequest request = captor.getValue();

        assertEquals(gameId, request.getGameId());
        assertEquals(year, request.getYear());
        assertEquals(datestamp, request.getDatestamp());
        assertEquals(type, request.getType());
        assertEquals(teamId, request.getTeamId());
        assertEquals(opponent, request.getOpponent());
    }

    @Test
    public void search_ShouldRespondWithEnvelopContainingResponsesReturnedBySearchService() {
        final List<TeamGameResponse> teamGameList = Collections.singletonList(new TeamGameResponse());

        when(searchService.fetchAll(any(TeamGameSearchRequest.class))).thenReturn(teamGameList);

        final ResponseEntity<ResponseEnvelope<TeamGameResponse>> response = controller.search(null, null, null, null, null, null);

        assertEquals(teamGameList, response.getBody().getResources());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithSuccessStatusWhenRecordsAreFound() {
        final List<TeamGameResponse> teamGameList = Collections.singletonList(new TeamGameResponse());

        when(searchService.fetchAll(any(TeamGameSearchRequest.class))).thenReturn(teamGameList);

        final ResponseEntity<ResponseEnvelope<TeamGameResponse>> response = controller.search(null, null, null, null, null, null);

        assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
    }
}