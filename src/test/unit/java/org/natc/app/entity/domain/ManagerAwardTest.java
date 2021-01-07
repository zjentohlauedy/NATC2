package org.natc.app.entity.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ManagerAwardTest {

    @Nested
    class GetByValue {

        @Test
        void shouldReturnNullWhenGivenNull() {
            assertNull(ManagerAward.getByValue(null));
        }

        @Test
        void shouldReturnNullWhenGivenInvalidValue() {
            assertNull(ManagerAward.getByValue(7));
        }

        @Test
        public void shouldReturnAppropriateEnumWhenGivenValidValue() {
            assertEquals(ManagerAward.NONE, ManagerAward.getByValue(0));
            assertEquals(ManagerAward.MANAGER_OF_THE_YEAR, ManagerAward.getByValue(1));
        }
    }
}