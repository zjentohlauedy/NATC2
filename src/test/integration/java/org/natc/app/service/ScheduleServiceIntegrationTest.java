package org.natc.app.service;

import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ScheduleServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private ScheduleRepository repository;

    @Autowired
    private ScheduleService scheduleService;

    @Test
    public void getCurrentScheduleEntry_ShouldReturnNullIfNoSchedulesExist() {
        final Schedule schedule = scheduleService.getCurrentScheduleEntry();

        assertNull(schedule);
    }

    @Test
    public void getCurrentScheduleEntry_ShouldReturnNullIfNoInProgressSchedulesExist() {
        final List<Schedule> scheduleList = Arrays.asList(
                Schedule.builder().year("2020").sequence(1).scheduled(LocalDate.parse("2020-01-01")).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2020").sequence(2).scheduled(LocalDate.parse("2020-01-02")).status(ScheduleStatus.SCHEDULED.getValue()).build()
        );

        repository.saveAll(scheduleList);

        final Schedule schedule = scheduleService.getCurrentScheduleEntry();

        assertNull(schedule);
    }

    @Test
    public void getCurrentScheduleEntry_ShouldReturnInProgressSchedule() {
        final List<Schedule> scheduleList = Arrays.asList(
                Schedule.builder().year("2020").sequence(1).scheduled(LocalDate.parse("2020-01-01")).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2020").sequence(2).scheduled(LocalDate.parse("2020-01-02")).status(ScheduleStatus.IN_PROGRESS.getValue()).build(),
                Schedule.builder().year("2020").sequence(3).scheduled(LocalDate.parse("2020-01-03")).status(ScheduleStatus.SCHEDULED.getValue()).build()
        );

        repository.saveAll(scheduleList);

        final Schedule schedule = scheduleService.getCurrentScheduleEntry();

        assertEquals(schedule.getSequence(), 2);
        assertEquals(schedule.getStatus(), ScheduleStatus.IN_PROGRESS.getValue());
    }

    @Test
    public void getCurrentScheduleEntry_ShouldReturnMostRecentlyScheduledInProgressSchedule() {
        final List<Schedule> scheduleList = Arrays.asList(
                Schedule.builder().year("2020").sequence(1).scheduled(LocalDate.parse("2020-01-01")).status(ScheduleStatus.IN_PROGRESS.getValue()).build(),
                Schedule.builder().year("2020").sequence(2).scheduled(LocalDate.parse("2020-01-02")).status(ScheduleStatus.IN_PROGRESS.getValue()).build(),
                Schedule.builder().year("2020").sequence(3).scheduled(LocalDate.parse("2020-01-03")).status(ScheduleStatus.IN_PROGRESS.getValue()).build()
        );

        repository.saveAll(scheduleList);

        final Schedule schedule = scheduleService.getCurrentScheduleEntry();

        assertEquals(schedule.getSequence(), 3);
        assertEquals(schedule.getScheduled(), LocalDate.parse("2020-01-03"));
    }
}