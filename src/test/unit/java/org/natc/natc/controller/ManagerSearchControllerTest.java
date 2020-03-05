package org.natc.natc.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.natc.entity.request.ManagerSearchRequest;
import org.natc.natc.entity.response.ManagerResponse;
import org.natc.natc.entity.response.ResponseEnvelope;
import org.natc.natc.entity.response.ResponseStatus;
import org.natc.natc.service.NATCService;
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
    private NATCService managerSearchService;

    @InjectMocks
    private ManagerSearchController managerSearchController;

    @Test
    public void search_ShouldReturnOKResponse() {
        final ResponseEntity<ResponseEnvelope<ManagerResponse>> response = managerSearchController.search(null, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithNotFoundStatusWhenNoRecordsAreFound() {
        final ResponseEntity<ResponseEnvelope<ManagerResponse>> response = managerSearchController.search(null, null, null, null);

        assertEquals(ResponseStatus.NOT_FOUND, response.getBody().getStatus());
    }

    @Test
    public void search_ShouldCallManagerSearchService() {
        managerSearchController.search(null, null, null, null);

        verify(managerSearchService).fetchAll(any());
    }
    
    @Test
    public void search_ShouldConstructRequestObjectForManagerSearchServiceUsingRequestParameters() {
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
    public void search_ShouldRespondWithEnvelopContainingManagersReturnedBySearchService() {
        final List<ManagerResponse> managerList = Collections.singletonList(new ManagerResponse());

        when(managerSearchService.fetchAll(any(ManagerSearchRequest.class))).thenReturn(managerList);

        final ResponseEntity<ResponseEnvelope<ManagerResponse>> response = managerSearchController.search(null, null, null, null);

        assertEquals(managerList, response.getBody().getResources());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithSuccessStatusWhenRecordsAreFound() {
        final List<ManagerResponse> managerList = Collections.singletonList(new ManagerResponse());

        when(managerSearchService.fetchAll(any(ManagerSearchRequest.class))).thenReturn(managerList);

        final ResponseEntity<ResponseEnvelope<ManagerResponse>> response = managerSearchController.search(null, null, null, null);

        assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
    }
}