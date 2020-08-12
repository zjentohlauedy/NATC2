package org.natc.app.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.request.InjurySearchRequest;
import org.natc.app.entity.response.InjuryResponse;
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
class InjurySearchControllerTest {

    @Mock
    private NATCService injurySearchService;

    @InjectMocks
    private InjurySearchController injurySearchController;

    @Test
    public void search_ShouldReturnOKResponse() {
        final ResponseEntity<ResponseEnvelope<InjuryResponse>> response = injurySearchController.search(null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithNotFoundStatusWhenNoRecordsAreFound() {
        final ResponseEntity<ResponseEnvelope<InjuryResponse>> response = injurySearchController.search(null, null, null);

        assertEquals(ResponseStatus.NOT_FOUND, response.getBody().getStatus());
    }

    @Test
    public void search_ShouldCallInjurySearchService() {
        injurySearchController.search(null, null, null);

        verify(injurySearchService).fetchAll(any(InjurySearchRequest.class));
    }

    @Test
    public void search_ShouldConstructRequestObjectForInjurySearchServiceUsingRequestParameters() {
        final Integer gameId = 123;
        final Integer playerId = 4321;
        final Integer teamId = 56;
        final ArgumentCaptor<InjurySearchRequest> captor = ArgumentCaptor.forClass(InjurySearchRequest.class);

        injurySearchController.search(gameId, playerId, teamId);

        verify(injurySearchService).fetchAll(captor.capture());

        final InjurySearchRequest request = captor.getValue();

        assertEquals(gameId, request.getGameId());
        assertEquals(playerId, request.getPlayerId());
        assertEquals(teamId, request.getTeamId());
    }

    @Test
    public void search_ShouldRespondWithEnvelopContainingInjuriesReturnedBySearchService() {
        final List<InjuryResponse> injuryList = Collections.singletonList(new InjuryResponse());

        when(injurySearchService.fetchAll(any(InjurySearchRequest.class))).thenReturn(injuryList);

        final ResponseEntity<ResponseEnvelope<InjuryResponse>> response = injurySearchController.search(null, null, null);

        assertEquals(injuryList, response.getBody().getResources());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithSuccessStatusWhenRecordsAreFound() {
        final List<InjuryResponse> injuryList = Collections.singletonList(new InjuryResponse());

        when(injurySearchService.fetchAll(any(InjurySearchRequest.class))).thenReturn(injuryList);

        final ResponseEntity<ResponseEnvelope<InjuryResponse>> response = injurySearchController.search(null, null, null);

        assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
    }
}