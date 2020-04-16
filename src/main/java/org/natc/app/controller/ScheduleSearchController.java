package org.natc.app.controller;

import org.natc.app.entity.request.ScheduleSearchRequest;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.entity.response.ScheduleResponse;
import org.natc.app.service.NATCService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ScheduleSearchController {

    private final NATCService<ScheduleResponse, ScheduleSearchRequest> service;

    public ScheduleSearchController(final NATCService<ScheduleResponse, ScheduleSearchRequest> service) {
        this.service = service;
    }

    public ResponseEntity<ResponseEnvelope<ScheduleResponse>> search(final String year, final Integer sequence) {
        final ScheduleSearchRequest request = ScheduleSearchRequest.builder()
                .year(year)
                .sequence(sequence)
                .build();

        final List<ScheduleResponse> scheduleList = service.fetchAll(request);

        return ResponseEntity.ok(new ResponseEnvelope<>(scheduleList));
    }
}
