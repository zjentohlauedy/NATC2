package org.natc.app.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.request.PlayerStatsSummarySearchRequest;
import org.natc.app.entity.response.PlayerStatsSummaryResponse;
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
class PlayerStatsSummarySearchControllerTest {

    @Mock
    private NATCSearchService searchService;

    @InjectMocks
    private PlayerStatsSummarySearchController controller;

    @Nested
    class Search {

        @Test
        public void shouldReturnOKResponse() {
            final ResponseEntity<ResponseEnvelope<PlayerStatsSummaryResponse>> response = controller.search(null, null, null, null);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        public void shouldReturnEnvelopeWithNotFoundStatusWhenNoRecordsAreFound() {
            final ResponseEntity<ResponseEnvelope<PlayerStatsSummaryResponse>> response = controller.search(null, null, null, null);

            assertEquals(ResponseStatus.NOT_FOUND, response.getBody().getStatus());
        }

        @Test
        public void shouldCallPlayerStatsSummarySearchService() {
            controller.search(null, null, null, null);

            verify(searchService).fetchAll(any());
        }

        @Test
        public void shouldConstructRequestObjectForSearchServiceUsingRequestParameters() {
            final String year = "1997";
            final GameType type = GameType.ALLSTAR;
            final Integer playerId = 123;
            final Integer teamId = 45;
            final ArgumentCaptor<PlayerStatsSummarySearchRequest> captor = ArgumentCaptor.forClass(PlayerStatsSummarySearchRequest.class);

            controller.search(year, type, playerId, teamId);

            verify(searchService).fetchAll(captor.capture());

            final PlayerStatsSummarySearchRequest request = captor.getValue();

            assertEquals(year, request.getYear());
            assertEquals(type, request.getType());
            assertEquals(playerId, request.getPlayerId());
            assertEquals(teamId, request.getTeamId());
        }

        @Test
        public void shouldRespondWithEnvelopContainingResponsesReturnedBySearchService() {
            final List<PlayerStatsSummaryResponse> playerStatsSummaryList = Collections.singletonList(new PlayerStatsSummaryResponse());

            when(searchService.fetchAll(any(PlayerStatsSummarySearchRequest.class))).thenReturn(playerStatsSummaryList);

            final ResponseEntity<ResponseEnvelope<PlayerStatsSummaryResponse>> response = controller.search(null, null, null, null);

            assertEquals(playerStatsSummaryList, response.getBody().getResources());
        }

        @Test
        public void shouldReturnEnvelopeWithSuccessStatusWhenRecordsAreFound() {
            final List<PlayerStatsSummaryResponse> playerStatsSummaryList = Collections.singletonList(new PlayerStatsSummaryResponse());

            when(searchService.fetchAll(any(PlayerStatsSummarySearchRequest.class))).thenReturn(playerStatsSummaryList);

            final ResponseEntity<ResponseEnvelope<PlayerStatsSummaryResponse>> response = controller.search(null, null, null, null);

            assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
        }
    }
}