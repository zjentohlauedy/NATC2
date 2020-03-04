package org.natc.natc.entity.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerAwardTest {

    @Test
    void getByValue_ShouldReturnNullWhenGivenNull() {
        assertNull(ManagerAward.getByValue(null));
    }

    @Test
    void getByValue_ShouldReturnNullWhenGivenInvalidValue() {
        assertNull(ManagerStyle.getByValue(7));
    }

    @Test
    public void getByValue_ShouldReturnAppropriateEnumWhenGivenValidValue() {
        assertEquals(ManagerAward.NONE, ManagerAward.getByValue(0));
        assertEquals(ManagerAward.MANAGER_OF_THE_YEAR, ManagerAward.getByValue(1));
    }
}