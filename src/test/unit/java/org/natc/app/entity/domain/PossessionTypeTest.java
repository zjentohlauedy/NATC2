package org.natc.app.entity.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PossessionTypeTest {

    @Nested
    class GetByValue {

        @Test
        public void shouldReturnNullWhenGivenNull() {
            assertNull(PossessionType.getByValue(null));
        }

        @Test
        public void shouldReturnNullWhenGivenInvalidValue() {
            assertNull(PossessionType.getByValue(7));
        }

        @Test
        public void shouldReturnAppropriateEnumWhenGivenValidValue() {
            assertEquals(PossessionType.NONE, PossessionType.getByValue(0));
            assertEquals(PossessionType.HOME, PossessionType.getByValue(1));
            assertEquals(PossessionType.ROAD, PossessionType.getByValue(2));
        }
    }

    @Nested
    class GetValueFor {

        @Test
        public void shouldReturnNullWhenGivenNull() {
            assertNull(PossessionType.getValueFor(null));
        }

        @Test
        public void shouldReturnAppropriateValueGivenValidPossessionType() {
            assertEquals(0, PossessionType.getValueFor(PossessionType.NONE));
            assertEquals(1, PossessionType.getValueFor(PossessionType.HOME));
            assertEquals(2, PossessionType.getValueFor(PossessionType.ROAD));
        }
    }
}