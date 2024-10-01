package org.natc.app.service.search;

import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleId;
import org.natc.app.entity.request.ScheduleSearchRequest;
import org.natc.app.entity.response.ScheduleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleSearchService implements NATCSearchService<ScheduleResponse, ScheduleSearchRequest> {

    private final JpaRepository<Schedule, ScheduleId> repository;

    @Autowired
    public ScheduleSearchService(final JpaRepository<Schedule, ScheduleId> repository) {
        this.repository = repository;
    }

    public List<ScheduleResponse> fetchAll(final ScheduleSearchRequest request) {
        final Schedule schedule = Schedule.builder()
                .year(request.getYear())
                .sequence(request.getSequence())
                .build();

        final List<Schedule> scheduleList = repository.findAll(Example.of(schedule));

        return scheduleList.stream().map(ScheduleResponse::new).toList();
    }
}
