package org.natc.app.entity.domain;

import java.util.Objects;

public enum PossessionType {
    NONE(0), HOME(1), ROAD(2);

    private Integer type;

    PossessionType(final Integer type) {
        this.type = type;
    }

    public Integer getValue() {
        return type;
    }

    public static Integer getValueFor(final PossessionType type) {
        if (Objects.isNull(type)) return null;

        return type.getValue();
    }

    public static PossessionType getByValue(final Integer value) {
        for (final PossessionType type : PossessionType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }

        return null;
    }
}
