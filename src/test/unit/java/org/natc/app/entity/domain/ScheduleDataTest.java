package org.natc.app.entity.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ScheduleDataTest {

    @Nested
    class ToString {

        @Test
        void shouldReturnAString() {
            final ScheduleData scheduleData = new ScheduleData();

            assertFalse(scheduleData.toString().isBlank());
        }

        @Test
        void shouldEncodeTheNumberOfGamesInTheFirstCharacterAsASCIIZeroPlusValue() {
            final ScheduleData scheduleData = new ScheduleData();

            assertEquals('0', scheduleData.toString().charAt(0));

            scheduleData.getMatches().add(new ScheduleData.Match(1, 2));
            assertEquals('1', scheduleData.toString().charAt(0));

            scheduleData.getMatches().add(new ScheduleData.Match(1, 2));
            assertEquals('2', scheduleData.toString().charAt(0));

            scheduleData.getMatches().add(new ScheduleData.Match(1, 2));
            assertEquals('3', scheduleData.toString().charAt(0));

            scheduleData.getMatches().add(new ScheduleData.Match(1, 2));
            assertEquals('4', scheduleData.toString().charAt(0));

            scheduleData.getMatches().add(new ScheduleData.Match(1, 2));
            assertEquals('5', scheduleData.toString().charAt(0));
        }

        @Test
        void shouldEncodeEachMatchAsRoadFollowedByHomeAsASCIIZeroPlusValue() {
            final ScheduleData scheduleData = new ScheduleData();

            scheduleData.getMatches().add(new ScheduleData.Match(11, 21));
            assertEquals('E', scheduleData.toString().charAt(1));
            assertEquals(';', scheduleData.toString().charAt(2));
        }

        @Test
        void shouldEncodeEveryMatch() {
            final ScheduleData scheduleData = new ScheduleData();

            scheduleData.getMatches().add(new ScheduleData.Match(1, 20));
            scheduleData.getMatches().add(new ScheduleData.Match(2, 19));
            scheduleData.getMatches().add(new ScheduleData.Match(3, 18));
            scheduleData.getMatches().add(new ScheduleData.Match(4, 17));
            scheduleData.getMatches().add(new ScheduleData.Match(5, 16));
            scheduleData.getMatches().add(new ScheduleData.Match(6, 15));
            scheduleData.getMatches().add(new ScheduleData.Match(7, 14));

            assertEquals("7D1C2B3A4@5?6>7", scheduleData.toString());
        }
    }
}