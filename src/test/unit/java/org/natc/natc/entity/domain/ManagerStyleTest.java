package org.natc.natc.entity.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerStyleTest {

    @Test
    void getByValue_ShouldReturnNullWhenGivenNull() {
        assertNull(ManagerStyle.getByValue(null));
    }

    @Test
    void getByValue_ShouldReturnNullWhenGivenInvalidValue() {
        assertNull(ManagerStyle.getByValue(7));
    }

    @Test
    public void getByValue_ShouldReturnAppropriateEnumWhenGivenValidValue() {
        assertEquals(ManagerStyle.OFFENSIVE, ManagerStyle.getByValue(1));
        assertEquals(ManagerStyle.DEFENSIVE, ManagerStyle.getByValue(2));
        assertEquals(ManagerStyle.INTANGIBLE, ManagerStyle.getByValue(3));
        assertEquals(ManagerStyle.PENALTIES, ManagerStyle.getByValue(4));
        assertEquals(ManagerStyle.BALANCED, ManagerStyle.getByValue(5));
    }
}