package org.natc.app.configuration;

import org.natc.app.entity.domain.ScheduleType;
import org.springframework.context.annotation.Bean;

import java.util.EnumMap;
import java.util.Map;

public class ScheduleDataGeneratorConfiguration {

    @Bean
    public Map<ScheduleType, String> scheduleTypeDataGeneratorMap() {
        final EnumMap<ScheduleType, String> map = new EnumMap<>(ScheduleType.class);
        map.put(ScheduleType.PRESEASON, "preseason-schedule-data-generator");
        map.put(ScheduleType.REGULAR_SEASON, "regular-season-schedule-data-generator");
        return map;
    }
}
