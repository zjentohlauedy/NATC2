package org.natc.app.processor;

import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.exception.NATCException;
import org.natc.app.service.ScheduleService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static org.natc.app.processor.ScheduleValidator.validateScheduleEntry;

@Component("beginning-of-season-schedule-processor")
public class BeginningOfSeasonScheduleProcessor implements ScheduleProcessor {

    private final ScheduleService scheduleService;

    private final List<ScheduleType> validScheduleTypes = Collections.singletonList(ScheduleType.BEGINNING_OF_SEASON);

    public BeginningOfSeasonScheduleProcessor(final ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Override
    public void process(final Schedule schedule) throws NATCException {
        validateScheduleEntry(schedule, validScheduleTypes);

        schedule.setStatus(ScheduleStatus.COMPLETED.getValue());

        scheduleService.updateScheduleEntry(schedule);
    }
}
