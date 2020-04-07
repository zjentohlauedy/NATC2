package org.natc.app.entity.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ScheduleStatusTest {

    @Test
    public void getByValue_ShouldReturnNullWhenGivenNull() {
        assertNull(ScheduleStatus.getByValue(null));
    }

    @Test
    public void getByValue_ShouldReturnNullWhenGivenInvalidValue() {
        assertNull(ScheduleStatus.getByValue(25));
    }

    @Test
    public void getByValue_ShouldReturnAppropriateEnumWhenGivenValidValue() {
        assertEquals(ScheduleStatus.SCHEDULED, ScheduleStatus.getByValue(0));
        assertEquals(ScheduleStatus.IN_PROGRESS, ScheduleStatus.getByValue(1));
        assertEquals(ScheduleStatus.COMPLETED, ScheduleStatus.getByValue(2));
    }
}