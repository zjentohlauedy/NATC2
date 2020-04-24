package org.natc.app.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.request.ScheduleSearchRequest;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.entity.response.ResponseStatus;
import org.natc.app.entity.response.ScheduleResponse;
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
class ScheduleSearchControllerTest {

    @Mock
    private NATCService scheduleSearchService;

    @InjectMocks
    private ScheduleSearchController scheduleSearchController;

    @Test
    public void search_ShouldReturnOKResponse() {
        final ResponseEntity<ResponseEnvelope<ScheduleResponse>> response = scheduleSearchController.search(null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithNotFoundStatusWhenNoRecordsAreFound() {
        final ResponseEntity<ResponseEnvelope<ScheduleResponse>> response = scheduleSearchController.search(null, null);

        assertEquals(ResponseStatus.NOT_FOUND, response.getBody().getStatus());
    }

    @Test
    public void search_ShouldCallScheduleSearchService() {
        scheduleSearchController.search(null, null);

        verify(scheduleSearchService).fetchAll(any(ScheduleSearchRequest.class));
    }

    @Test
    public void search_ShouldConstructRequestObjectForScheduleSearchServiceUsingRequestParameters() {
        final String year = "1992";
        final Integer sequence = 35;
        final ArgumentCaptor<ScheduleSearchRequest> captor = ArgumentCaptor.forClass(ScheduleSearchRequest.class);

        scheduleSearchController.search(year, sequence);

        verify(scheduleSearchService).fetchAll(captor.capture());

        final ScheduleSearchRequest request = captor.getValue();

        assertEquals(year, request.getYear());
        assertEquals(sequence, request.getSequence());
    }

    @Test
    public void search_ShouldRespondWithEnvelopContainingSchedulesReturnedBySearchService() {
        final List<ScheduleResponse> scheduleList = Collections.singletonList(new ScheduleResponse());

        when(scheduleSearchService.fetchAll(any(ScheduleSearchRequest.class))).thenReturn(scheduleList);

        final ResponseEntity<ResponseEnvelope<ScheduleResponse>> response = scheduleSearchController.search(null, null);

        assertEquals(scheduleList, response.getBody().getResources());
    }

    @Test
    public void search_ShouldReturnEnvelopeWithSuccessStatusWhenRecordsAreFound() {
        final List<ScheduleResponse> scheduleList = Collections.singletonList(new ScheduleResponse());

        when(scheduleSearchService.fetchAll(any(ScheduleSearchRequest.class))).thenReturn(scheduleList);

        final ResponseEntity<ResponseEnvelope<ScheduleResponse>> response = scheduleSearchController.search(null, null);

        assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
    }
}