package org.natc.app.entity.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PossessionTypeTest {

    @Test
    public void getByValue_ShouldReturnNullWhenGivenNull() {
        assertNull(PossessionType.getByValue(null));
    }

    @Test
    public void getByValue_ShouldReturnNullWhenGivenInvalidValue() {
        assertNull(PossessionType.getByValue(7));
    }

    @Test
    public void getByValue_ShouldReturnAppropriateEnumWhenGivenValidValue() {
        assertEquals(PossessionType.NONE, PossessionType.getByValue(0));
        assertEquals(PossessionType.HOME, PossessionType.getByValue(1));
        assertEquals(PossessionType.ROAD, PossessionType.getByValue(2));
    }

    @Test
    public void getValueFor_ShouldReturnNullWhenGivenNull() {
        assertNull(PossessionType.getValueFor(null));
    }

    @Test
    public void getValueFor_ShouldReturnAppropriateValueGivenValidPossessionType() {
        assertEquals(0, PossessionType.getValueFor(PossessionType.NONE));
        assertEquals(1, PossessionType.getValueFor(PossessionType.HOME));
        assertEquals(2, PossessionType.getValueFor(PossessionType.ROAD));
    }
}