package org.natc.app.service;

import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.request.ScheduleSearchRequest;
import org.natc.app.entity.response.ScheduleResponse;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ScheduleSearchService implements NATCService<ScheduleResponse, ScheduleSearchRequest> {

    private final JpaRepository repository;

    public ScheduleSearchService(final JpaRepository repository) {
        this.repository = repository;
    }

    public List<ScheduleResponse> fetchAll(final ScheduleSearchRequest request) {
        final Schedule schedule = Schedule.builder()
                .year(request.getYear())
                .sequence(request.getSequence())
                .build();

        final List<Schedule> scheduleList = repository.findAll(Example.of(schedule));

        return scheduleList.stream().map(ScheduleResponse::new).collect(Collectors.toList());
    }
}
