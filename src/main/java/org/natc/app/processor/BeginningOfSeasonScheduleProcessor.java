package org.natc.app.processor;

import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.exception.NATCException;
import org.natc.app.service.ScheduleService;
import org.springframework.stereotype.Component;

@Component("beginning-of-season-schedule-processor")
public class BeginningOfSeasonScheduleProcessor implements ScheduleProcessor {

    private final ScheduleService scheduleService;

    public BeginningOfSeasonScheduleProcessor(final ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Override
    public void process(final Schedule schedule) throws NATCException {
        schedule.setStatus(ScheduleStatus.COMPLETED.getValue());

        scheduleService.updateScheduleEntry(schedule);
    }
}
