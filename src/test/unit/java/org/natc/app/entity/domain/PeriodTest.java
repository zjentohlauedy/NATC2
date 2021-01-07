package org.natc.app.entity.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PeriodTest {

    @Nested
    class GetByValue {

        @Test
        public void shouldReturnNullWhenGivenNull() {
            assertNull(Period.getByValue(null));
        }

        @Test
        public void shouldReturnNullWhenGivenInvalidValue() {
            assertNull(Period.getByValue(10));
        }

        @Test
        public void shouldReturnAppropriateEnumWhenGivenValidValue() {
            assertEquals(Period.NONE, Period.getByValue(0));
            assertEquals(Period.FIRST, Period.getByValue(1));
            assertEquals(Period.SECOND, Period.getByValue(2));
            assertEquals(Period.THIRD, Period.getByValue(3));
            assertEquals(Period.FOURTH, Period.getByValue(4));
            assertEquals(Period.FIFTH, Period.getByValue(5));
        }
    }

    @Nested
    class GetValueFor {

        @Test
        public void shouldReturnNullWhenGivenNull() {
            assertNull(Period.getValueFor(null));
        }

        @Test
        public void shouldReturnAppropriateValueGivenValidPeriod() {
            assertEquals(0, Period.getValueFor(Period.NONE));
            assertEquals(1, Period.getValueFor(Period.FIRST));
            assertEquals(2, Period.getValueFor(Period.SECOND));
            assertEquals(3, Period.getValueFor(Period.THIRD));
            assertEquals(4, Period.getValueFor(Period.FOURTH));
            assertEquals(5, Period.getValueFor(Period.FIFTH));
        }
    }
}