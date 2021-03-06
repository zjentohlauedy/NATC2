package org.natc.app.entity.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameTypeTest {

    @Nested
    class GetByValue {

        @Test
        void shouldReturnNullWhenGivenNull() {
            assertNull(GameType.getByValue(null));
        }

        @Test
        void shouldReturnNullWhenGivenInvalidValue() {
            assertNull(GameType.getByValue(12));
        }

        @Test
        void shouldReturnAppropriateEnumWhenGivenValidValue() {
            assertEquals(GameType.PRESEASON, GameType.getByValue(1));
            assertEquals(GameType.REGULAR_SEASON, GameType.getByValue(2));
            assertEquals(GameType.POSTSEASON, GameType.getByValue(3));
            assertEquals(GameType.ALLSTAR, GameType.getByValue(4));
        }
    }

    @Nested
    class GetValueFor {

        @Test
        void shouldReturnNullWhenGivenNull() {
            assertNull(GameType.getValueFor(null));
        }

        @Test
        void shouldReturnAppropriateValueGivenValidGameType() {
            assertEquals(1, GameType.getValueFor(GameType.PRESEASON));
            assertEquals(2, GameType.getValueFor(GameType.REGULAR_SEASON));
            assertEquals(3, GameType.getValueFor(GameType.POSTSEASON));
            assertEquals(4, GameType.getValueFor(GameType.ALLSTAR));
        }
    }
}