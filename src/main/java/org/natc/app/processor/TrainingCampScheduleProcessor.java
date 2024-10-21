package org.natc.app.processor;

import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.exception.NATCException;
import org.natc.app.service.PlayerService;
import org.natc.app.service.ScheduleService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static org.natc.app.processor.ScheduleValidator.validateScheduleEntry;

@Component("training-camp-schedule-processor")
public class TrainingCampScheduleProcessor implements ScheduleProcessor {

    private final PlayerService playerService;
    private final ScheduleService scheduleService;

    private final List<ScheduleType> validScheduleTypes = Collections.singletonList(ScheduleType.TRAINING_CAMP);

    public TrainingCampScheduleProcessor(final PlayerService playerService, final ScheduleService scheduleService) {
        this.playerService = playerService;
        this.scheduleService = scheduleService;
    }

    @Override
    public void process(final Schedule schedule) throws NATCException {
        validateScheduleEntry(schedule, validScheduleTypes);

        playerService.agePlayers(schedule.getYear());

        schedule.setStatus(ScheduleStatus.COMPLETED.getValue());

        scheduleService.updateScheduleEntry(schedule);
    }
}
