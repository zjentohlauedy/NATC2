package org.natc.app.entity.domain;

import java.util.Objects;

public enum ManagerStyle {
    OFFENSIVE(1), DEFENSIVE(2), INTANGIBLE(3), PENALTIES(4), BALANCED(5);

    private final Integer style;

    ManagerStyle(final Integer style) {
        this.style = style;
    }

    public Integer getValue() {
        return style;
    }

    public static ManagerStyle getByValue(final Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        for (final ManagerStyle style: ManagerStyle.values()) {
            if (style.getValue().equals(value)) {
                return style;
            }
        }

        return null;
    }
}