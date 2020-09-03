package org.natc.app.service;

import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    private final ScheduleRepository repository;

    @Autowired
    public ScheduleService(final ScheduleRepository repository) {
        this.repository = repository;
    }

    public Schedule getCurrentScheduleEntry() {

        return repository.findFirstByStatusOrderByScheduledDesc(ScheduleStatus.IN_PROGRESS.getValue()).orElse(null);
    }
}
