package org.natc.app.util;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BooleanHelperTest {

    @Nested
    class ValueOf {

        @Test
        public void shouldReturnNullWhenArgumentIsNull() {
            assertNull(BooleanHelper.valueOf(null));
        }

        @Test
        public void shouldReturnTrueWhenArgumentValueIs1() {
            assertEquals(true, BooleanHelper.valueOf(1));
        }

        @Test
        public void shouldReturnFalseWhenArgumentValueIs0() {
            assertEquals(false, BooleanHelper.valueOf(0));
        }
    }
}