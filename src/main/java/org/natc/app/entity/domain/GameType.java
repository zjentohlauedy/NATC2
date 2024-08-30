package org.natc.app.entity.domain;

import java.util.Objects;

public enum GameType {
    PRESEASON(1), REGULAR_SEASON(2), POSTSEASON(3), ALLSTAR(4);

    private final Integer type;

    GameType(final Integer type) {
        this.type = type;
    }

    public Integer getValue() {
        return type;
    }

    public static Integer getValueFor(final GameType type) {
        if (Objects.isNull(type)) return null;

        return type.getValue();
    }

    public static GameType getByValue(final Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        for (final GameType type : GameType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }

        return null;
    }
}
