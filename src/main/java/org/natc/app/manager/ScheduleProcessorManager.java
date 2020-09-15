package org.natc.app.manager;

import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.processor.ScheduleProcessor;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public class ScheduleProcessorManager {

    private final ApplicationContext context;
    private final Map<ScheduleType, String> scheduleTypeProcessorMap;

    public ScheduleProcessorManager(final ApplicationContext context, final Map<ScheduleType, String> scheduleTypeProcessorMap) {
        this.context = context;
        this.scheduleTypeProcessorMap = scheduleTypeProcessorMap;
    }

    public ScheduleProcessor getProcessorFor(final ScheduleType scheduleType) {
        return context.getBean(scheduleTypeProcessorMap.get(scheduleType), ScheduleProcessor.class);
    }
}
