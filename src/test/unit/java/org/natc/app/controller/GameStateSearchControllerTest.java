package org.natc.app.controller;

import org.junit.jupiter.api.Nested;
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
import org.natc.app.service.search.NATCSearchService;
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
    private NATCSearchService gameStateSearchService;

    @InjectMocks
    private GameStateSearchController gameStateSearchController;

    @Nested
    class Search {

        @Test
        void shouldReturnOKResponse() {
            final ResponseEntity<ResponseEnvelope<GameStateResponse>> response = gameStateSearchController.search(null);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturnEnvelopeWithNotFoundStatusWhenNoRecordsAreFound() {
            final ResponseEntity<ResponseEnvelope<GameStateResponse>> response = gameStateSearchController.search(null);

            assertEquals(ResponseStatus.NOT_FOUND, response.getBody().getStatus());
        }

        @Test
        void shouldCallGameStateSearchService() {
            gameStateSearchController.search(null);

            verify(gameStateSearchService).fetchAll(any(GameStateSearchRequest.class));
        }

        @Test
        void shouldConstructRequestObjectForGameStateSearchServiceUsingRequestParameters() {
            final Integer gameId = 123;
            final ArgumentCaptor<GameStateSearchRequest> captor = ArgumentCaptor.forClass(GameStateSearchRequest.class);

            gameStateSearchController.search(gameId);

            verify(gameStateSearchService).fetchAll(captor.capture());

            final GameStateSearchRequest request = captor.getValue();

            assertEquals(gameId, request.getGameId());
        }

        @Test
        void shouldRespondWithEnvelopContainingGameStatesReturnedBySearchService() {
            final List<GameStateResponse> gameStateList = Collections.singletonList(new GameStateResponse());

            when(gameStateSearchService.fetchAll(any(GameStateSearchRequest.class))).thenReturn(gameStateList);

            final ResponseEntity<ResponseEnvelope<GameStateResponse>> response = gameStateSearchController.search(null);

            assertEquals(gameStateList, response.getBody().getResources());
        }

        @Test
        void shouldReturnEnvelopeWithSuccessStatusWhenRecordsAreFound() {
            final List<GameStateResponse> gameStateList = Collections.singletonList(new GameStateResponse());

            when(gameStateSearchService.fetchAll(any(GameStateSearchRequest.class))).thenReturn(gameStateList);

            final ResponseEntity<ResponseEnvelope<GameStateResponse>> response = gameStateSearchController.search(null);

            assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
        }
    }
}