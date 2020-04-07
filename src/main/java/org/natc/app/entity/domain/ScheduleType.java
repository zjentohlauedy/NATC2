package org.natc.app.entity.domain;

public enum ScheduleType {
    BEGINNING_OF_SEASON(0),
    MANAGER_CHANGES(1),
    PLAYER_CHANGES(2),
    ROOKIE_DRAFT_ROUND_1(3),
    ROOKIE_DRAFT_ROUND_2(4),
    TRAINING_CAMP(5),
    PRESEASON(6),
    END_OF_PRESEASON(7),
    ROSTER_CUT(8),
    REGULAR_SEASON(9),
    END_OF_REGULAR_SEASON(10),
    AWARDS(11),
    POSTSEASON(12),
    DIVISION_PLAYOFF(13),
    DIVISION_CHAMPIONSHIP(14),
    CONFERENCE_CHAMPIONSHIP(15),
    NATC_CHAMPIONSHIP(16),
    END_OF_POSTSEASON(17),
    ALL_STARS(18),
    ALL_STAR_DAY_1(19),
    ALL_STAR_DAY_2(20),
    END_OF_ALLSTAR_GAMES(21),
    END_OF_SEASON(22);

    private Integer type;

    ScheduleType(final Integer type) {
        this.type = type;
    }

    public Integer getValue() {
        return type;
    }

    public static ScheduleType getByValue(final Integer value) {
        if (value == null) {
            return null;
        }

        for (final ScheduleType type : ScheduleType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }

        return null;
    }
}
