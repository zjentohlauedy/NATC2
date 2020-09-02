package org.natc.app.controller;

import org.natc.app.entity.request.ScheduleSearchRequest;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.entity.response.ScheduleResponse;
import org.natc.app.service.NATCSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/schedules")
public class ScheduleSearchController {

    private final NATCSearchService<ScheduleResponse, ScheduleSearchRequest> service;

    @Autowired
    public ScheduleSearchController(final NATCSearchService<ScheduleResponse, ScheduleSearchRequest> service) {
        this.service = service;
    }

    @RequestMapping("/search")
    public ResponseEntity<ResponseEnvelope<ScheduleResponse>> search(@RequestParam(required = false) final String year,
                                                                     @RequestParam(required = false) final Integer sequence) {
        final ScheduleSearchRequest request = ScheduleSearchRequest.builder()
                .year(year)
                .sequence(sequence)
                .build();

        final List<ScheduleResponse> scheduleList = service.fetchAll(request);

        return ResponseEntity.ok(new ResponseEnvelope<>(scheduleList));
    }
}
