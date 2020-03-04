package org.natc.natc.entity.domain;

public enum ManagerAward {
    NONE(0), MANAGER_OF_THE_YEAR(1);

    private final Integer award;

    ManagerAward(final Integer award) {
        this.award = award;
    }

    public int getValue() {
        return award;
    }

    public static ManagerAward getByValue(final Integer value) {
        if (value == null) {
            return null;
        }

        for (final ManagerAward award : ManagerAward.values()) {
            if (award.getValue() == value) {
                return award;
            }
        }

        return null;
    }
}
