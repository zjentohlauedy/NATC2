package org.natc.app.entity.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerAwardTest {

    @Test
    void getByValue_ShouldReturnNullWhenGivenNull() {
        assertNull(PlayerAward.getByValue(null));
    }

    @Test
    void getByValue_ShouldReturnNullWhenGivenInvalidValue() {
        assertNull(PlayerAward.getByValue(7));
    }

    @Test
    public void getByValue_ShouldReturnAppropriateEnumWhenGivenValidValue() {
        assertEquals(PlayerAward.NONE, PlayerAward.getByValue(0));
        assertEquals(PlayerAward.SILVER, PlayerAward.getByValue(1));
        assertEquals(PlayerAward.GOLD, PlayerAward.getByValue(2));
        assertEquals(PlayerAward.PLATINUM, PlayerAward.getByValue(3));
    }

}