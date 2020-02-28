package org.natc.natc.util;

public final class BooleanHelper {
    private BooleanHelper() {
    }

    public static Boolean valueOf(Integer i) {
        if (i != null) {
            return i == 1;
        }

        return null;
    }
}
