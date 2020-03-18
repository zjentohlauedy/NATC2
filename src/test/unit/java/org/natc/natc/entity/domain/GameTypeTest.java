package org.natc.natc.entity.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameTypeTest {

    @Test
    public void getByValue_ShouldReturnNullWhenGivenNull() {
        assertNull(GameType.getByValue(null));
    }

    @Test
    public void getByValue_ShouldReturnNullWhenGivenInvalidValue() {
        assertNull(GameType.getByValue(12));
    }

    @Test
    public void getByValue_ShouldReturnAppropriateEnumWhenGivenValidValue() {
        assertEquals(GameType.PRESEASON, GameType.getByValue(1));
        assertEquals(GameType.REGULAR_SEASON, GameType.getByValue(2));
        assertEquals(GameType.POSTSEASON, GameType.getByValue(3));
        assertEquals(GameType.ALLSTAR, GameType.getByValue(4));
    }
}