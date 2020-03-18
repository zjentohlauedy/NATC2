package org.natc.natc.entity.domain;

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
        if (value == null) {
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
