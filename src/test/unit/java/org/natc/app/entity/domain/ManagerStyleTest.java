package org.natc.app.entity.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ManagerStyleTest {

    @Nested
    class GetByValue {

        @Test
        void shouldReturnNullWhenGivenNull() {
            assertNull(ManagerStyle.getByValue(null));
        }

        @Test
        void shouldReturnNullWhenGivenInvalidValue() {
            assertNull(ManagerStyle.getByValue(7));
        }

        @Test
        public void shouldReturnAppropriateEnumWhenGivenValidValue() {
            assertEquals(ManagerStyle.OFFENSIVE, ManagerStyle.getByValue(1));
            assertEquals(ManagerStyle.DEFENSIVE, ManagerStyle.getByValue(2));
            assertEquals(ManagerStyle.INTANGIBLE, ManagerStyle.getByValue(3));
            assertEquals(ManagerStyle.PENALTIES, ManagerStyle.getByValue(4));
            assertEquals(ManagerStyle.BALANCED, ManagerStyle.getByValue(5));
        }
    }
}