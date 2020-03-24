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

    @Test
    public void getValueFor_ShouldReturnNullWhenGivenNull() {
        assertNull(GameType.getValueFor(null));
    }

    @Test
    public void getValueFor_ShouldReturnAppropriateValueGivenValidGameType() {
        assertEquals(1, GameType.getValueFor(GameType.PRESEASON));
        assertEquals(2, GameType.getValueFor(GameType.REGULAR_SEASON));
        assertEquals(3, GameType.getValueFor(GameType.POSTSEASON));
        assertEquals(4, GameType.getValueFor(GameType.ALLSTAR));
    }
}