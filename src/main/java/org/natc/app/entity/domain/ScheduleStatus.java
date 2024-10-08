package org.natc.app.entity.domain;

import java.util.Objects;

public enum ScheduleStatus {
    SCHEDULED(0), IN_PROGRESS(1), COMPLETED(2);

    private final Integer status;

    ScheduleStatus(final Integer status) {
        this.status = status;
    }

    public Integer getValue() {
        return status;
    }

    public static ScheduleStatus getByValue(final Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        for (final ScheduleStatus status : ScheduleStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }

        return null;
    }
}
