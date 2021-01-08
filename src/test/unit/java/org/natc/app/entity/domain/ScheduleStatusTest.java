package org.natc.app.entity.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ScheduleStatusTest {

    @Nested
    class GetByValue {

        @Test
        void shouldReturnNullWhenGivenNull() {
            assertNull(ScheduleStatus.getByValue(null));
        }

        @Test
        void shouldReturnNullWhenGivenInvalidValue() {
            assertNull(ScheduleStatus.getByValue(25));
        }

        @Test
        void shouldReturnAppropriateEnumWhenGivenValidValue() {
            assertEquals(ScheduleStatus.SCHEDULED, ScheduleStatus.getByValue(0));
            assertEquals(ScheduleStatus.IN_PROGRESS, ScheduleStatus.getByValue(1));
            assertEquals(ScheduleStatus.COMPLETED, ScheduleStatus.getByValue(2));
        }
    }
}