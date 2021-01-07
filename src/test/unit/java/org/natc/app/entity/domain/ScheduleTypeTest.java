package org.natc.app.entity.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ScheduleTypeTest {

    @Nested
    class GetByValue {

        @Test
        public void shouldReturnNullWhenGivenNull() {
            assertNull(ScheduleType.getByValue(null));
        }

        @Test
        public void shouldReturnNullWhenGivenInvalidValue() {
            assertNull(ScheduleType.getByValue(99));
        }

        @Test
        public void shouldReturnAppropriateEnumWhenGivenValidValue() {
            assertEquals(ScheduleType.BEGINNING_OF_SEASON, ScheduleType.getByValue(0));
            assertEquals(ScheduleType.MANAGER_CHANGES, ScheduleType.getByValue(1));
            assertEquals(ScheduleType.PLAYER_CHANGES, ScheduleType.getByValue(2));
            assertEquals(ScheduleType.ROOKIE_DRAFT_ROUND_1, ScheduleType.getByValue(3));
            assertEquals(ScheduleType.ROOKIE_DRAFT_ROUND_2, ScheduleType.getByValue(4));
            assertEquals(ScheduleType.TRAINING_CAMP, ScheduleType.getByValue(5));
            assertEquals(ScheduleType.PRESEASON, ScheduleType.getByValue(6));
            assertEquals(ScheduleType.END_OF_PRESEASON, ScheduleType.getByValue(7));
            assertEquals(ScheduleType.ROSTER_CUT, ScheduleType.getByValue(8));
            assertEquals(ScheduleType.REGULAR_SEASON, ScheduleType.getByValue(9));
            assertEquals(ScheduleType.END_OF_REGULAR_SEASON, ScheduleType.getByValue(10));
            assertEquals(ScheduleType.AWARDS, ScheduleType.getByValue(11));
            assertEquals(ScheduleType.POSTSEASON, ScheduleType.getByValue(12));
            assertEquals(ScheduleType.DIVISION_PLAYOFF, ScheduleType.getByValue(13));
            assertEquals(ScheduleType.DIVISION_CHAMPIONSHIP, ScheduleType.getByValue(14));
            assertEquals(ScheduleType.CONFERENCE_CHAMPIONSHIP, ScheduleType.getByValue(15));
            assertEquals(ScheduleType.NATC_CHAMPIONSHIP, ScheduleType.getByValue(16));
            assertEquals(ScheduleType.END_OF_POSTSEASON, ScheduleType.getByValue(17));
            assertEquals(ScheduleType.ALL_STARS, ScheduleType.getByValue(18));
            assertEquals(ScheduleType.ALL_STAR_DAY_1, ScheduleType.getByValue(19));
            assertEquals(ScheduleType.ALL_STAR_DAY_2, ScheduleType.getByValue(20));
            assertEquals(ScheduleType.END_OF_ALLSTAR_GAMES, ScheduleType.getByValue(21));
            assertEquals(ScheduleType.END_OF_SEASON, ScheduleType.getByValue(22));
        }
    }
}