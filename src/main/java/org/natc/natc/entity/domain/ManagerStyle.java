package org.natc.natc.entity.domain;

public enum ManagerStyle {
    OFFENSIVE(1), DEFENSIVE(2), INTANGIBLE(3), PENALTIES(4), BALANCED(5);

    private final Integer style;

    ManagerStyle(final Integer style) {
        this.style = style;
    }

    public int getValue() {
        return style;
    }

    public static ManagerStyle getByValue(final Integer value) {
        if (value == null) {
            return null;
        }

        for (final ManagerStyle style: ManagerStyle.values()) {
            if (style.getValue() == value) {
                return style;
            }
        }

        return null;
    }
}