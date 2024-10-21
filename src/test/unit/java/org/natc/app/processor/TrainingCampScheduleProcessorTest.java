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
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.exception.NATCException;
import org.natc.app.exception.ScheduleProcessingException;
import org.natc.app.service.PlayerService;
import org.natc.app.service.ScheduleService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainingCampScheduleProcessorTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private TrainingCampScheduleProcessor processor;

    @Nested
    class Process {
        @Test
        void shouldThrowExceptionWhenScheduleTypeIsMissing() {
            final Schedule schedule = Schedule.builder().year("2019").build();

            assertThrows(ScheduleProcessingException.class, () -> processor.process(schedule));
        }

        @Test
        void shouldThrowExceptionWhenGivenIncorrectScheduleEventType() throws NATCException {
            final Schedule schedule = Schedule.builder()
                    .type(ScheduleType.MANAGER_CHANGES.getValue())
                    .year("2019")
                    .build();

            assertThrows(ScheduleProcessingException.class, () -> processor.process(schedule));
        }

        @Test
        void shouldCallTheScheduleServiceToUpdateTheScheduleEntry() throws NATCException {
            processor.process(Schedule.builder().type(ScheduleType.TRAINING_CAMP.getValue()).build());

            verify(scheduleService).updateScheduleEntry(any());
        }

        @Test
        void shouldUpdateTheScheduleEntryStatusToCompleted() throws NATCException {
            final Schedule schedule = Schedule.builder()
                    .type(ScheduleType.TRAINING_CAMP.getValue())
                    .year("2001")
                    .sequence(1)
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .build();
            final ArgumentCaptor<Schedule> captor = ArgumentCaptor.forClass(Schedule.class);

            processor.process(schedule);

            verify(scheduleService).updateScheduleEntry(captor.capture());

            assertSame(schedule, captor.getValue());
            assertEquals(ScheduleStatus.COMPLETED.getValue(), captor.getValue().getStatus());
        }

        @Test
        void shouldCallPlayerServiceToUpdatePlayerAgesForScheduleYear() throws NATCException {
            final String expectedYear = "2005";

            processor.process(Schedule.builder().type(ScheduleType.TRAINING_CAMP.getValue()).year(expectedYear).build());

            verify(playerService).agePlayers(expectedYear);
        }
    }
}