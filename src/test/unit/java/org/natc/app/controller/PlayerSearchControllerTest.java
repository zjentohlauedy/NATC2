package org.natc.app.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.request.PlayerSearchRequest;
import org.natc.app.entity.response.PlayerResponse;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.entity.response.ResponseStatus;
import org.natc.app.service.NATCSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerSearchControllerTest {

    @Mock
    private NATCSearchService playerSearchService;

    @InjectMocks
    private PlayerSearchController playerSearchController;

    @Test
    public void search_ShouldReturnOKResponse() {
        final ResponseEntity<ResponseEnvelope<PlayerResponse>> response = playerSearchController.search(null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithNotFoundStatusWhenNoRecordsAreFound() {
        final ResponseEntity<ResponseEnvelope<PlayerResponse>> response = playerSearchController.search(null, null, null);

        assertEquals(ResponseStatus.NOT_FOUND, response.getBody().getStatus());
    }

    @Test
    public void search_ShouldCallPlayerSearchService() {
        playerSearchController.search(null, null, null);

        verify(playerSearchService).fetchAll(any());
    }

    @Test
    public void search_ShouldConstructRequestObjectForPlayerSearchServiceUsingRequestParameters() {
        final Integer playerId = 123;
        final Integer teamId = 321;
        final String year = "1991";
        final ArgumentCaptor<PlayerSearchRequest> captor = ArgumentCaptor.forClass(PlayerSearchRequest.class);

        playerSearchController.search(playerId, teamId, year);

        verify(playerSearchService).fetchAll(captor.capture());

        final PlayerSearchRequest request = captor.getValue();

        assertEquals(playerId, request.getPlayerId());
        assertEquals(teamId, request.getTeamId());
        assertEquals(year, request.getYear());
    }

    @Test
    public void search_ShouldRespondWithEnvelopContainingPlayersReturnedBySearchService() {
        final List<PlayerResponse> playerList = Collections.singletonList(new PlayerResponse());

        when(playerSearchService.fetchAll(any(PlayerSearchRequest.class))).thenReturn(playerList);

        final ResponseEntity<ResponseEnvelope<PlayerResponse>> response = playerSearchController.search(null, null, null);

        assertSame(playerList, response.getBody().getResources());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithSuccessStatusWhenRecordsAreFound() {
        final List<PlayerResponse> playerList = Collections.singletonList(new PlayerResponse());

        when(playerSearchService.fetchAll(any(PlayerSearchRequest.class))).thenReturn(playerList);

        final ResponseEntity<ResponseEnvelope<PlayerResponse>> response = playerSearchController.search(null, null, null);

        assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
    }
}