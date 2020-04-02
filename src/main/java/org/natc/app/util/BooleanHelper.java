package org.natc.app.util;

public final class BooleanHelper {
    private BooleanHelper() {
    }

    public static Boolean valueOf(final Integer i) {
        if (i != null) {
            return i == 1;
        }

        return null;
    }
}
