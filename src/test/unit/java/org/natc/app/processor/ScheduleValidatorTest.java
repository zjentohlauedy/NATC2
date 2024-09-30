package org.natc.app.processor;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.exception.ScheduleProcessingException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ScheduleValidatorTest {

    @Nested
    class ValidateScheduleEntry {
        @Test
        void shouldDoNothingIfScheduleTypeIsInValidList() throws ScheduleProcessingException {
            final Schedule schedule = Schedule.builder().type(ScheduleType.BEGINNING_OF_SEASON.getValue()).build();
            final List<ScheduleType> validTypes = Arrays.asList(
                    ScheduleType.ALL_STAR_DAY_1,
                    ScheduleType.CONFERENCE_CHAMPIONSHIP,
                    ScheduleType.MANAGER_CHANGES,
                    ScheduleType.BEGINNING_OF_SEASON
            );

            ScheduleValidator.validateScheduleEntry(schedule, validTypes);
        }

        @Test
        void shouldThrowAScheduleProcessingExceptionIfTheScheduleDoesNotHaveAScheduleType() {
            final Schedule schedule = Schedule.builder().build();

            final ScheduleProcessingException ex = assertThrows(ScheduleProcessingException.class,
                    () -> ScheduleValidator.validateScheduleEntry(schedule, Collections.emptyList()));

            assertEquals("Schedule type is required", ex.getMessage());
        }

        @Test
        void shouldThrowScheduleProcessingExceptionWhenTheScheduleTypeIsNotInTheValidList() {
            final Schedule schedule = Schedule.builder().type(ScheduleType.BEGINNING_OF_SEASON.getValue()).build();
            final List<ScheduleType> validTypes = Arrays.asList(
                    ScheduleType.ALL_STAR_DAY_1,
                    ScheduleType.CONFERENCE_CHAMPIONSHIP,
                    ScheduleType.MANAGER_CHANGES
            );

            final ScheduleProcessingException ex = assertThrows(ScheduleProcessingException.class,
                    () -> ScheduleValidator.validateScheduleEntry(schedule, validTypes));

            assertTrue(ex.getMessage().contains(ScheduleType.BEGINNING_OF_SEASON.name()));
        }
    }
}