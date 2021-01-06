package org.natc.app.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.request.ManagerSearchRequest;
import org.natc.app.entity.response.ManagerResponse;
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
class ManagerSearchControllerTest {

    @Mock
    private NATCSearchService managerSearchService;

    @InjectMocks
    private ManagerSearchController managerSearchController;

    @Nested
    class Search {

        @Test
        public void shouldReturnOKResponse() {
            final ResponseEntity<ResponseEnvelope<ManagerResponse>> response = managerSearchController.search(null, null, null, null);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        public void shouldReturnEnvelopeWithNotFoundStatusWhenNoRecordsAreFound() {
            final ResponseEntity<ResponseEnvelope<ManagerResponse>> response = managerSearchController.search(null, null, null, null);

            assertEquals(ResponseStatus.NOT_FOUND, response.getBody().getStatus());
        }

        @Test
        public void shouldCallManagerSearchService() {
            managerSearchController.search(null, null, null, null);

            verify(managerSearchService).fetchAll(any());
        }

        @Test
        public void shouldConstructRequestObjectForManagerSearchServiceUsingRequestParameters() {
            final Integer managerId = 123;
            final Integer teamId = 321;
            final Integer playerId = 555;
            final String year = "1919";
            final ArgumentCaptor<ManagerSearchRequest> captor = ArgumentCaptor.forClass(ManagerSearchRequest.class);

            managerSearchController.search(managerId, teamId, playerId, year);

            verify(managerSearchService).fetchAll(captor.capture());

            final ManagerSearchRequest request = captor.getValue();

            assertEquals(managerId, request.getManagerId());
            assertEquals(teamId, request.getTeamId());
            assertEquals(playerId, request.getPlayerId());
            assertEquals(year, request.getYear());
        }

        @Test
        public void shouldRespondWithEnvelopContainingManagersReturnedBySearchService() {
            final List<ManagerResponse> managerList = Collections.singletonList(new ManagerResponse());

            when(managerSearchService.fetchAll(any(ManagerSearchRequest.class))).thenReturn(managerList);

            final ResponseEntity<ResponseEnvelope<ManagerResponse>> response = managerSearchController.search(null, null, null, null);

            assertEquals(managerList, response.getBody().getResources());
        }

        @Test
        public void shouldReturnEnvelopeWithSuccessStatusWhenRecordsAreFound() {
            final List<ManagerResponse> managerList = Collections.singletonList(new ManagerResponse());

            when(managerSearchService.fetchAll(any(ManagerSearchRequest.class))).thenReturn(managerList);

            final ResponseEntity<ResponseEnvelope<ManagerResponse>> response = managerSearchController.search(null, null, null, null);

            assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
        }
    }
}