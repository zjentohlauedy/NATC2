package org.natc.natc.entity.domain;

public enum GameType {
    PRESEASON(1), REGULAR_SEASON(2), POSTSEASON(3), ALLSTAR(4);

    private final Integer type;

    GameType(final Integer type) {
        this.type = type;
    }

    public Integer getValue() {
        return type;
    }

    public static GameType getByValue(final Integer value) {
        if (value == null) {
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
