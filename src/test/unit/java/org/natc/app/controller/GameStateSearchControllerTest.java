package org.natc.app.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.request.GameStateSearchRequest;
import org.natc.app.entity.response.GameStateResponse;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.entity.response.ResponseStatus;
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
class GameStateSearchControllerTest {

    @Mock
    private NATCService gameStateSearchService;

    @InjectMocks
    private GameStateSearchController gameStateSearchController;

    @Test
    public void search_ShouldReturnOKResponse() {
        final ResponseEntity<ResponseEnvelope<GameStateResponse>> response = gameStateSearchController.search(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithNotFoundStatusWhenNoRecordsAreFound() {
        final ResponseEntity<ResponseEnvelope<GameStateResponse>> response = gameStateSearchController.search(null);

        assertEquals(ResponseStatus.NOT_FOUND, response.getBody().getStatus());
    }

    @Test
    public void search_ShouldCallGameStateSearchService() {
        gameStateSearchController.search(null);

        verify(gameStateSearchService).fetchAll(any(GameStateSearchRequest.class));
    }

    @Test
    public void search_ShouldConstructRequestObjectForGameStateSearchServiceUsingRequestParameters() {
        final Integer gameId = 123;
        final ArgumentCaptor<GameStateSearchRequest> captor = ArgumentCaptor.forClass(GameStateSearchRequest.class);

        gameStateSearchController.search(gameId);

        verify(gameStateSearchService).fetchAll(captor.capture());

        final GameStateSearchRequest request = captor.getValue();

        assertEquals(gameId, request.getGameId());
    }

    @Test
    public void search_ShouldRespondWithEnvelopContainingGameStatesReturnedBySearchService() {
        final List<GameStateResponse> gameStateList = Collections.singletonList(new GameStateResponse());

        when(gameStateSearchService.fetchAll(any(GameStateSearchRequest.class))).thenReturn(gameStateList);

        final ResponseEntity<ResponseEnvelope<GameStateResponse>> response = gameStateSearchController.search(null);

        assertEquals(gameStateList, response.getBody().getResources());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithSuccessStatusWhenRecordsAreFound() {
        final List<GameStateResponse> gameStateList = Collections.singletonList(new GameStateResponse());

        when(gameStateSearchService.fetchAll(any(GameStateSearchRequest.class))).thenReturn(gameStateList);

        final ResponseEntity<ResponseEnvelope<GameStateResponse>> response = gameStateSearchController.search(null);

        assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
    }
}