package org.natc.app.processor;

import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.exception.ScheduleProcessingException;

import java.util.List;
import java.util.Objects;

public class ScheduleValidator {

    public static void validateScheduleEntry(final Schedule schedule, final List<ScheduleType> validScheduleTypes) throws ScheduleProcessingException {
        final ScheduleType scheduleType = ScheduleType.getByValue(schedule.getType());

        if (Objects.isNull(scheduleType)) {
            throw new ScheduleProcessingException("Schedule type is required");
        }

        if (!validScheduleTypes.contains(scheduleType)) {
            throw new ScheduleProcessingException(
                    String.format("Invalid Schedule Type [%s] for this processor, valid types: %s",
                            scheduleType.name(),
                            validScheduleTypes
                    )
            );
        }
    }
}
