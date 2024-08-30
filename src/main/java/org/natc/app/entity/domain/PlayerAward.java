package org.natc.app.entity.domain;

import java.util.Objects;

public enum PlayerAward {
    NONE(0), SILVER(1), GOLD(2), PLATINUM(3);

    private final Integer award;

    PlayerAward(final Integer award) {
        this.award = award;
    }

    public Integer getValue() {
        return award;
    }

    public static PlayerAward getByValue(final Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        for (final PlayerAward award : PlayerAward.values()) {
            if (award.getValue().equals(value)) {
                return award;
            }
        }

        return null;
    }
}
