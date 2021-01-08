package org.natc.app.processor;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.exception.NATCException;
import org.natc.app.service.ScheduleService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BeginningOfSeasonScheduleProcessorTest {

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private BeginningOfSeasonScheduleProcessor processor;

    @Nested
    class Process {

        @Test
        public void shouldCallTheScheduleServiceToUpdateTheScheduleEntry() throws NATCException {
            processor.process(Schedule.builder().build());

            verify(scheduleService).updateScheduleEntry(any());
        }

        @Test
        public void shouldUpdateTheScheduleEntryStatusToCompleted() throws NATCException {
            final Schedule schedule = Schedule.builder().year("2001").sequence(1).status(ScheduleStatus.IN_PROGRESS.getValue()).build();
            final ArgumentCaptor<Schedule> captor = ArgumentCaptor.forClass(Schedule.class);

            processor.process(schedule);

            verify(scheduleService).updateScheduleEntry(captor.capture());

            assertSame(schedule, captor.getValue());
            assertEquals(ScheduleStatus.COMPLETED.getValue(), captor.getValue().getStatus());
        }
    }
}