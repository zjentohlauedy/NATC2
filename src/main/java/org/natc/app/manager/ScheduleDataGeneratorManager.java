package org.natc.app.manager;

import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.generator.ScheduleDataGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ScheduleDataGeneratorManager {

    private final ApplicationContext context;
    private final Map<ScheduleType, String> scheduleTypeDataGeneratorMap;

    public ScheduleDataGeneratorManager(
            final ApplicationContext context,
            final Map<ScheduleType, String> scheduleTypeDataGeneratorMap) {
        this.context = context;
        this.scheduleTypeDataGeneratorMap = scheduleTypeDataGeneratorMap;
    }

    public ScheduleDataGenerator getGeneratorFor(final ScheduleType scheduleType) {
        return context.getBean(scheduleTypeDataGeneratorMap.get(scheduleType), ScheduleDataGenerator.class);
    }
}
