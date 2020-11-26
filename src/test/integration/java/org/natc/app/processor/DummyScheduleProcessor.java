package org.natc.app.processor;

import org.natc.app.entity.domain.Schedule;
import org.springframework.stereotype.Component;

@Component("dummy-schedule-processor")
public class DummyScheduleProcessor implements ScheduleProcessor {
    @Override
    public void process(final Schedule schedule) {
    }
}
