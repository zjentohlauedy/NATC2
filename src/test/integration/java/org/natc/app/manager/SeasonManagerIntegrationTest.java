package org.natc.app.manager;

import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.exception.ScheduleProcessingException;
import org.natc.app.repository.ScheduleRepository;
import org.natc.app.service.NATCServiceIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SeasonManagerIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private ScheduleRepository repository;

    @Autowired
    private SeasonManager seasonManager;

    @Test
    public void processScheduledEvent_ShouldHandleInProgressSchedule() throws ScheduleProcessingException {
        final List<Schedule> initialScheduleList = Arrays.asList(
                Schedule.builder().year("2001").sequence(24).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(2)).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2001").sequence(25).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(1)).status(ScheduleStatus.IN_PROGRESS.getValue()).build(),
                Schedule.builder().year("2001").sequence(26).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now()).status(ScheduleStatus.SCHEDULED.getValue()).build()
        );

        repository.saveAll(initialScheduleList);

        seasonManager.processScheduledEvent();

        final Example<Schedule> example = Example.of(Schedule.builder().year("2001").sequence(26).build());

        final List<Schedule> afterScheduleList = repository.findAll(example);

        assertEquals(ScheduleStatus.SCHEDULED.getValue(), afterScheduleList.get(0).getStatus());
    }

    @Test
    public void processScheduledEvent_ShouldThrowUnsupportedOperationExceptionWhenNoSchedulesExist() {
        final UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () ->
                seasonManager.processScheduledEvent());

        assertEquals("Method Not Implemented Yet", exception.getMessage());
    }

    @Test
    public void processScheduledEvent_ShouldThrowUnsupportedOperationExceptionWhenEndOfSeason() {
        final List<Schedule> initialScheduleList = Arrays.asList(
                Schedule.builder().year("2001").sequence(24).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(2)).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2001").sequence(25).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(1)).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2001").sequence(26).type(ScheduleType.END_OF_SEASON.getValue())
                        .scheduled(LocalDate.now()).status(ScheduleStatus.COMPLETED.getValue()).build()
        );

        repository.saveAll(initialScheduleList);

        final UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () ->
                seasonManager.processScheduledEvent());

        assertEquals("Method Not Implemented Yet", exception.getMessage());
    }

    @Test
    public void processScheduledEvent_ShouldThrowScheduleProcessingExceptionWhenNoScheduledEntriesExist() {
        final List<Schedule> initialScheduleList = Arrays.asList(
                Schedule.builder().year("2001").sequence(24).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(2)).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2001").sequence(25).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(1)).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2001").sequence(26).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now()).status(ScheduleStatus.COMPLETED.getValue()).build()
        );

        repository.saveAll(initialScheduleList);

        assertThrows(ScheduleProcessingException.class, () -> seasonManager.processScheduledEvent());
    }

    @Test
    public void processScheduledEvent_ShouldSetScheduleStatusToInProgress() throws ScheduleProcessingException {
        final List<Schedule> initialScheduleList = Arrays.asList(
                Schedule.builder().year("2001").sequence(24).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(2)).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2001").sequence(25).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(1)).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2001").sequence(26).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now()).status(ScheduleStatus.SCHEDULED.getValue()).build()
        );

        repository.saveAll(initialScheduleList);

        seasonManager.processScheduledEvent();

        final Example<Schedule> example = Example.of(Schedule.builder().year("2001").sequence(26).build());

        final List<Schedule> afterScheduleList = repository.findAll(example);

        assertEquals(ScheduleStatus.IN_PROGRESS.getValue(), afterScheduleList.get(0).getStatus());
    }
}