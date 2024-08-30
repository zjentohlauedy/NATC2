package org.natc.app.entity.domain;

import java.util.Objects;

public enum ManagerAward {
    NONE(0), MANAGER_OF_THE_YEAR(1);

    private final Integer award;

    ManagerAward(final Integer award) {
        this.award = award;
    }

    public Integer getValue() {
        return award;
    }

    public static ManagerAward getByValue(final Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        for (final ManagerAward award : ManagerAward.values()) {
            if (award.getValue().equals(value)) {
                return award;
            }
        }

        return null;
    }
}
