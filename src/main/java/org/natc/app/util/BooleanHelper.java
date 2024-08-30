package org.natc.app.util;

import java.util.Objects;

public final class BooleanHelper {
    private BooleanHelper() {
    }

    public static Boolean valueOf(final Integer i) {
        if (Objects.nonNull(i)) {
            return i == 1;
        }

        return null;
    }
}
