package org.natc.app.processor;

import org.natc.app.entity.domain.Schedule;
import org.natc.app.exception.NATCException;

public interface ScheduleProcessor {
    void process(Schedule schedule) throws NATCException;
}
