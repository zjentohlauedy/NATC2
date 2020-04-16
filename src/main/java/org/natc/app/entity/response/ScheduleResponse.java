package org.natc.app.entity.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ScheduleResponse {
    private String year;
    private Integer sequence;
    private ScheduleType type;
    private String data;
    private LocalDate scheduled;
    private ScheduleStatus status;

    public ScheduleResponse(final Schedule schedule) {
        year = schedule.getYear();
        sequence = schedule.getSequence();
        type = ScheduleType.getByValue(schedule.getType());
        data = schedule.getData();
        scheduled = schedule.getScheduled();
        status = ScheduleStatus.getByValue(schedule.getStatus());
    }
}
