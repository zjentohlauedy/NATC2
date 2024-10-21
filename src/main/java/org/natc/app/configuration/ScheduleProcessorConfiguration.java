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
        map.put(ScheduleType.BEGINNING_OF_SEASON, "beginning-of-season-schedule-processor");
        map.put(ScheduleType.MANAGER_CHANGES, "manager-changes-schedule-processor");
        map.put(ScheduleType.PLAYER_CHANGES, "player-changes-schedule-processor");
        map.put(ScheduleType.ROOKIE_DRAFT_ROUND_1, "rookie-draft-schedule-processor");
        map.put(ScheduleType.ROOKIE_DRAFT_ROUND_2, "rookie-draft-schedule-processor");
        map.put(ScheduleType.TRAINING_CAMP, "training-camp-schedule-processor");
        return map;
    }
}
