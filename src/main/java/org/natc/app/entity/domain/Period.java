package org.natc.app.entity.domain;

import java.util.Objects;

public enum Period {
    NONE(0), FIRST(1), SECOND(2), THIRD(3), FOURTH(4), FIFTH(5);

    private final Integer period;

    Period(final Integer period) {
        this.period = period;
    }

    public Integer getValue() {
        return period;
    }

    public static Integer getValueFor(final Period period) {
        if (Objects.isNull(period)) return null;

        return period.getValue();
    }

    public static Period getByValue(final Integer value) {
        for (final Period period : Period.values()) {
            if (period.getValue().equals(value)) {
                return period;
            }
        }

        return null;
    }
}
