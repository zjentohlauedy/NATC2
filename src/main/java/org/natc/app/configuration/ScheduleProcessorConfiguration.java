package org.natc.app.configuration;

import org.natc.app.entity.domain.ScheduleType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class ScheduleProcessorConfiguration {

    @Bean
    public Map<ScheduleType, String> scheduleTypeProcessorMap() {
        final EnumMap<ScheduleType, String> map = new EnumMap<>(ScheduleType.class);
        map.put(ScheduleType.REGULAR_SEASON, "dummy-schedule-processor");
        return map;
    }
}
