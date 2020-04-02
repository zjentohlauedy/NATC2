package org.natc.app.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BooleanHelperTest {

    @Test
    public void valueOf_ShouldReturnNullWhenArgumentIsNull() {
        assertNull(BooleanHelper.valueOf(null));
    }

    @Test
    public void valueOf_ShouldReturnTrueWhenArgumentValueIs1() {
        assertEquals(true, BooleanHelper.valueOf(1));
    }

    @Test
    public void valueOf_ShouldReturnFalseWhenArgumentValueIs0() {
        assertEquals(false, BooleanHelper.valueOf(0));
    }
}