package org.natc.app.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.request.PlayerGameSearchRequest;
import org.natc.app.entity.response.PlayerGameResponse;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.entity.response.ResponseStatus;
import org.natc.app.service.search.PlayerGameSearchService;
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
class PlayerGameSearchControllerTest {

    @Mock
    private PlayerGameSearchService searchService;

    @InjectMocks
    private PlayerGameSearchController controller;

    @Nested
    class Search {

        @Test
        void shouldReturnOKResponse() {
            final ResponseEntity<ResponseEnvelope<PlayerGameResponse>> response = controller.search(null, null, null, null, null, null);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturnEnvelopeWithNotFoundStatusWhenNoRecordsAreFound() {
            final ResponseEntity<ResponseEnvelope<PlayerGameResponse>> response = controller.search(null, null, null, null, null, null);

            assertEquals(ResponseStatus.NOT_FOUND, response.getBody().getStatus());
        }

        @Test
        void shouldCallTeamGameSearchService() {
            controller.search(null, null, null, null, null, null);

            verify(searchService).fetchAll(any(PlayerGameSearchRequest.class));
        }

        @Test
        void shouldConstructRequestObjectForSearchServiceUsingRequestParameters() {
            final Integer gameId = 5338;
            final String year = "2003";
            final LocalDate datestamp = LocalDate.now();
            final GameType type = GameType.POSTSEASON;
            final Integer playerId = 362;
            final Integer teamId = 14;
            final ArgumentCaptor<PlayerGameSearchRequest> captor = ArgumentCaptor.forClass(PlayerGameSearchRequest.class);

            controller.search(gameId, year, datestamp, type, playerId, teamId);

            verify(searchService).fetchAll(captor.capture());

            final PlayerGameSearchRequest request = captor.getValue();

            assertEquals(gameId, request.getGameId());
            assertEquals(year, request.getYear());
            assertEquals(datestamp, request.getDatestamp());
            assertEquals(type, request.getType());
            assertEquals(playerId, request.getPlayerId());
            assertEquals(teamId, request.getTeamId());
        }

        @Test
        void shouldRespondWithEnvelopContainingResponsesReturnedBySearchService() {
            final List<PlayerGameResponse> playerGameList = Collections.singletonList(new PlayerGameResponse());

            when(searchService.fetchAll(any(PlayerGameSearchRequest.class))).thenReturn(playerGameList);

            final ResponseEntity<ResponseEnvelope<PlayerGameResponse>> response = controller.search(null, null, null, null, null, null);

            assertEquals(playerGameList, response.getBody().getResources());
        }

        @Test
        void shouldReturnEnvelopeWithSuccessStatusWhenRecordsAreFound() {
            final List<PlayerGameResponse> playerGameList = Collections.singletonList(new PlayerGameResponse());

            when(searchService.fetchAll(any(PlayerGameSearchRequest.class))).thenReturn(playerGameList);

            final ResponseEntity<ResponseEnvelope<PlayerGameResponse>> response = controller.search(null, null, null, null, null, null);

            assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
        }
    }
}