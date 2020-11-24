package org.natc.app.generator;

import org.natc.app.entity.domain.ScheduleData;

import java.util.List;

public interface ScheduleDataGenerator {
    List<ScheduleData> generate();
}
