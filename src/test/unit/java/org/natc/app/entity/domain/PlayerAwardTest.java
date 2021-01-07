package org.natc.app.entity.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PlayerAwardTest {

    @Nested
    class GetByValue {

        @Test
        void shouldReturnNullWhenGivenNull() {
            assertNull(PlayerAward.getByValue(null));
        }

        @Test
        void shouldReturnNullWhenGivenInvalidValue() {
            assertNull(PlayerAward.getByValue(7));
        }

        @Test
        public void shouldReturnAppropriateEnumWhenGivenValidValue() {
            assertEquals(PlayerAward.NONE, PlayerAward.getByValue(0));
            assertEquals(PlayerAward.SILVER, PlayerAward.getByValue(1));
            assertEquals(PlayerAward.GOLD, PlayerAward.getByValue(2));
            assertEquals(PlayerAward.PLATINUM, PlayerAward.getByValue(3));
        }
    }
}