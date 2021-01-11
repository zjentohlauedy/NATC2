package org.natc.app.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScheduleServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private ScheduleRepository repository;
    
    @Autowired
    private LeagueConfiguration leagueConfiguration;

    @Autowired
    private ScheduleService scheduleService;

    @Nested
    class GetCurrentScheduleEntry {

        @Test
        void shouldReturnNullIfNoSchedulesExist() {
            final Schedule schedule = scheduleService.getCurrentScheduleEntry();

            assertNull(schedule);
        }

        @Test
        void shouldReturnNullIfNoInProgressSchedulesExist() {
            final List<Schedule> scheduleList = Arrays.asList(
                    Schedule.builder().year("2020").sequence(1).scheduled(LocalDate.parse("2020-01-01")).status(ScheduleStatus.COMPLETED.getValue()).build(),
                    Schedule.builder().year("2020").sequence(2).scheduled(LocalDate.parse("2020-01-02")).status(ScheduleStatus.SCHEDULED.getValue()).build()
            );

            repository.saveAll(scheduleList);

            final Schedule schedule = scheduleService.getCurrentScheduleEntry();

            assertNull(schedule);
        }

        @Test
        void shouldReturnInProgressSchedule() {
            final List<Schedule> scheduleList = Arrays.asList(
                    Schedule.builder().year("2020").sequence(1).scheduled(LocalDate.parse("2020-01-01")).status(ScheduleStatus.COMPLETED.getValue()).build(),
                    Schedule.builder().year("2020").sequence(2).scheduled(LocalDate.parse("2020-01-02")).status(ScheduleStatus.IN_PROGRESS.getValue()).build(),
                    Schedule.builder().year("2020").sequence(3).scheduled(LocalDate.parse("2020-01-03")).status(ScheduleStatus.SCHEDULED.getValue()).build()
            );

            repository.saveAll(scheduleList);

            final Schedule schedule = scheduleService.getCurrentScheduleEntry();

            assertEquals(schedule.getSequence(), 2);
            assertEquals(schedule.getStatus(), ScheduleStatus.IN_PROGRESS.getValue());
        }

        @Test
        void shouldReturnMostRecentlyScheduledInProgressSchedule() {
            final List<Schedule> scheduleList = Arrays.asList(
                    Schedule.builder().year("2020").sequence(1).scheduled(LocalDate.parse("2020-01-01")).status(ScheduleStatus.IN_PROGRESS.getValue()).build(),
                    Schedule.builder().year("2020").sequence(2).scheduled(LocalDate.parse("2020-01-02")).status(ScheduleStatus.IN_PROGRESS.getValue()).build(),
                    Schedule.builder().year("2020").sequence(3).scheduled(LocalDate.parse("2020-01-03")).status(ScheduleStatus.IN_PROGRESS.getValue()).build()
            );

            repository.saveAll(scheduleList);

            final Schedule schedule = scheduleService.getCurrentScheduleEntry();

            assertEquals(schedule.getSequence(), 3);
            assertEquals(schedule.getScheduled(), LocalDate.parse("2020-01-03"));
        }
    }

    @Nested
    class GetLastScheduleEntry {

        @Test
        void shouldReturnNullIfNoSchedulesExist() {
            final Schedule schedule = scheduleService.getLastScheduleEntry();

            assertNull(schedule);
        }

        @Test
        void shouldReturnNullIfNoCompletedSchedulesExist() {
            final List<Schedule> scheduleList = Arrays.asList(
                    Schedule.builder().year("2020").sequence(1).scheduled(LocalDate.parse("2020-01-01")).status(ScheduleStatus.IN_PROGRESS.getValue()).build(),
                    Schedule.builder().year("2020").sequence(2).scheduled(LocalDate.parse("2020-01-02")).status(ScheduleStatus.SCHEDULED.getValue()).build()
            );

            repository.saveAll(scheduleList);

            final Schedule schedule = scheduleService.getLastScheduleEntry();

            assertNull(schedule);
        }

        @Test
        void shouldReturnCompletedSchedule() {
            final List<Schedule> scheduleList = Arrays.asList(
                    Schedule.builder().year("2020").sequence(1).scheduled(LocalDate.parse("2020-01-01")).status(ScheduleStatus.COMPLETED.getValue()).build(),
                    Schedule.builder().year("2020").sequence(2).scheduled(LocalDate.parse("2020-01-02")).status(ScheduleStatus.IN_PROGRESS.getValue()).build(),
                    Schedule.builder().year("2020").sequence(3).scheduled(LocalDate.parse("2020-01-03")).status(ScheduleStatus.SCHEDULED.getValue()).build()
            );

            repository.saveAll(scheduleList);

            final Schedule schedule = scheduleService.getLastScheduleEntry();

            assertEquals(schedule.getSequence(), 1);
            assertEquals(schedule.getStatus(), ScheduleStatus.COMPLETED.getValue());
        }

        @Test
        void shouldReturnMostRecentlyScheduledCompletedSchedule() {
            final List<Schedule> scheduleList = Arrays.asList(
                    Schedule.builder().year("2020").sequence(1).scheduled(LocalDate.parse("2020-01-01")).status(ScheduleStatus.COMPLETED.getValue()).build(),
                    Schedule.builder().year("2020").sequence(2).scheduled(LocalDate.parse("2020-01-02")).status(ScheduleStatus.COMPLETED.getValue()).build(),
                    Schedule.builder().year("2020").sequence(3).scheduled(LocalDate.parse("2020-01-03")).status(ScheduleStatus.COMPLETED.getValue()).build()
            );

            repository.saveAll(scheduleList);

            final Schedule schedule = scheduleService.getLastScheduleEntry();

            assertEquals(schedule.getSequence(), 3);
            assertEquals(schedule.getScheduled(), LocalDate.parse("2020-01-03"));
        }
    }

    @Nested
    class GetNextScheduleEntry {

        @Test
        void shouldReturnNullIfNoSchedulesExist() {
            final Schedule schedule = scheduleService.getNextScheduleEntry(null);

            assertNull(schedule);
        }

        @Test
        void shouldReturnNullIfNoSchedulesExistGivenAPreviousSchedule() {
            final Schedule currentSchedule = Schedule.builder()
                    .year("2000")
                    .sequence(1)
                    .type(ScheduleType.REGULAR_SEASON.getValue())
                    .build();

            final Schedule schedule = scheduleService.getNextScheduleEntry(currentSchedule);

            assertNull(schedule);
        }

        @Test
        void shouldReturnNullIfNoSchedulesExistGivenAnEndOfSeasonSchedule() {
            final Schedule currentSchedule = Schedule.builder()
                    .year("2000")
                    .sequence(1)
                    .type(ScheduleType.END_OF_SEASON.getValue())
                    .build();

            final Schedule schedule = scheduleService.getNextScheduleEntry(currentSchedule);

            assertNull(schedule);
        }

        @Test
        void shouldReturnFirstDefinedScheduleWhenGivenNull() {
            final List<Schedule> scheduleList = Arrays.asList(
                    Schedule.builder().year(leagueConfiguration.getFirstSeason()).sequence(Schedule.FIRST_SEQUENCE).status(ScheduleStatus.SCHEDULED.getValue()).build(),
                    Schedule.builder().year(leagueConfiguration.getFirstSeason()).sequence(Schedule.FIRST_SEQUENCE + 1).status(ScheduleStatus.SCHEDULED.getValue()).build(),
                    Schedule.builder().year(leagueConfiguration.getFirstSeason()).sequence(Schedule.FIRST_SEQUENCE + 2).status(ScheduleStatus.SCHEDULED.getValue()).build()
            );

            repository.saveAll(scheduleList);

            final Schedule schedule = scheduleService.getNextScheduleEntry(null);

            assertEquals(schedule.getYear(), leagueConfiguration.getFirstSeason());
            assertEquals(schedule.getSequence(), Schedule.FIRST_SEQUENCE);
        }

        @Test
        void shouldReturnNullIfFirstDefinedScheduleDoesNotExistWhenGivenNull() {
            final String differentYear = String.valueOf(Integer.parseInt(leagueConfiguration.getFirstSeason()) - 1);

            final List<Schedule> scheduleList = Arrays.asList(
                    Schedule.builder().year(differentYear).sequence(Schedule.FIRST_SEQUENCE).status(ScheduleStatus.SCHEDULED.getValue()).build(),
                    Schedule.builder().year(leagueConfiguration.getFirstSeason()).sequence(Schedule.FIRST_SEQUENCE + 1).status(ScheduleStatus.SCHEDULED.getValue()).build(),
                    Schedule.builder().year(leagueConfiguration.getFirstSeason()).sequence(Schedule.FIRST_SEQUENCE + 2).status(ScheduleStatus.SCHEDULED.getValue()).build(),
                    Schedule.builder().year(leagueConfiguration.getFirstSeason()).sequence(Schedule.FIRST_SEQUENCE + 3).status(ScheduleStatus.SCHEDULED.getValue()).build()
            );

            repository.saveAll(scheduleList);

            final Schedule schedule = scheduleService.getNextScheduleEntry(null);

            assertNull(schedule);
        }

        @Test
        void shouldReturnFirstSequenceOfTheFollowingYearGivenEndOfSeasonSchedule() {
            final List<Schedule> scheduleList = Arrays.asList(
                    Schedule.builder().year("2000").sequence(123).type(ScheduleType.END_OF_SEASON.getValue()).status(ScheduleStatus.COMPLETED.getValue()).build(),
                    Schedule.builder().year("2001").sequence(Schedule.FIRST_SEQUENCE).type(ScheduleType.PRESEASON.getValue()).status(ScheduleStatus.SCHEDULED.getValue()).build(),
                    Schedule.builder().year("2002").sequence(Schedule.FIRST_SEQUENCE + 1).type(ScheduleType.PRESEASON.getValue()).status(ScheduleStatus.SCHEDULED.getValue()).build()
            );

            repository.saveAll(scheduleList);

            final Schedule currentSchedule = Schedule.builder()
                    .year("2000")
                    .sequence(123)
                    .type(ScheduleType.END_OF_SEASON.getValue())
                    .build();

            final Schedule schedule = scheduleService.getNextScheduleEntry(currentSchedule);

            assertEquals(schedule.getYear(), "2001");
            assertEquals(schedule.getSequence(), Schedule.FIRST_SEQUENCE);
        }

        @Test
        void shouldReturnNullIfFirstSequenceOfTheFollowingYearDoesNotExistGivenEndOfSeasonSchedule() {
            final List<Schedule> scheduleList = Arrays.asList(
                    Schedule.builder().year("2000").sequence(121).type(ScheduleType.REGULAR_SEASON.getValue()).status(ScheduleStatus.COMPLETED.getValue()).build(),
                    Schedule.builder().year("2000").sequence(122).type(ScheduleType.REGULAR_SEASON.getValue()).status(ScheduleStatus.COMPLETED.getValue()).build(),
                    Schedule.builder().year("2000").sequence(123).type(ScheduleType.END_OF_SEASON.getValue()).status(ScheduleStatus.COMPLETED.getValue()).build()
            );

            repository.saveAll(scheduleList);

            final Schedule currentSchedule = Schedule.builder()
                    .year("2000")
                    .sequence(123)
                    .type(ScheduleType.END_OF_SEASON.getValue())
                    .build();

            final Schedule schedule = scheduleService.getNextScheduleEntry(currentSchedule);

            assertNull(schedule);
        }

        @Test
        void shouldReturnNextSequenceScheduleOfSameYearGivenASchedule() {
            final List<Schedule> scheduleList = Arrays.asList(
                    Schedule.builder().year("2000").sequence(4).type(ScheduleType.PRESEASON.getValue()).status(ScheduleStatus.COMPLETED.getValue()).build(),
                    Schedule.builder().year("2000").sequence(5).type(ScheduleType.PRESEASON.getValue()).status(ScheduleStatus.COMPLETED.getValue()).build(),
                    Schedule.builder().year("2000").sequence(6).type(ScheduleType.PRESEASON.getValue()).status(ScheduleStatus.SCHEDULED.getValue()).build()
            );

            repository.saveAll(scheduleList);

            final Schedule currentSchedule = Schedule.builder()
                    .year("2000")
                    .sequence(5)
                    .type(ScheduleType.PRESEASON.getValue())
                    .build();

            final Schedule schedule = scheduleService.getNextScheduleEntry(currentSchedule);

            assertEquals(schedule.getYear(), "2000");
            assertEquals(schedule.getSequence(), 6);
        }

        @Test
        void shouldReturnNullIfNextSequenceScheduleOfSameYearDoesNotExistGivenASchedule() {
            final List<Schedule> scheduleList = Arrays.asList(
                    Schedule.builder().year("2000").sequence(4).type(ScheduleType.PRESEASON.getValue()).status(ScheduleStatus.COMPLETED.getValue()).build(),
                    Schedule.builder().year("2000").sequence(5).type(ScheduleType.PRESEASON.getValue()).status(ScheduleStatus.COMPLETED.getValue()).build(),
                    Schedule.builder().year("2000").sequence(8).type(ScheduleType.PRESEASON.getValue()).status(ScheduleStatus.SCHEDULED.getValue()).build()
            );

            repository.saveAll(scheduleList);

            final Schedule currentSchedule = Schedule.builder()
                    .year("2000")
                    .sequence(5)
                    .type(ScheduleType.PRESEASON.getValue())
                    .build();

            final Schedule schedule = scheduleService.getNextScheduleEntry(currentSchedule);

            assertNull(schedule);
        }
    }

    @Nested
    class UpdateScheduleEntry {

        @Test
        void shouldUpdateScheduleRecord() {
            final Schedule schedule = Schedule.builder()
                    .year("2000")
                    .sequence(123)
                    .type(ScheduleType.PRESEASON.getValue())
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .build();

            repository.save(schedule);

            final Example<Schedule> exampleSchedule = Example.of(Schedule.builder().year("2000").sequence(123).build());

            final Schedule scheduleBefore = repository.findOne(exampleSchedule).orElse(null);

            assertNotNull(scheduleBefore);
            assertEquals(ScheduleStatus.SCHEDULED.getValue(), scheduleBefore.getStatus());

            schedule.setStatus(ScheduleStatus.IN_PROGRESS.getValue());

            scheduleService.updateScheduleEntry(schedule);

            final Schedule scheduleAfter = repository.findOne(exampleSchedule).orElse(null);

            assertNotNull(scheduleAfter);
            assertEquals(ScheduleStatus.IN_PROGRESS.getValue(), scheduleAfter.getStatus());
        }
    }

    @Nested
    class GenerateSchedule {

        @Test
        void shouldCreateScheduleRecordsForGivenYear() {
            scheduleService.generateSchedule("2001");

            final List<Schedule> scheduleList = repository.findAll();

            assertEquals(1, scheduleList.stream().map(Schedule::getYear).distinct().count());
            assertEquals("2001", scheduleList.get(0).getYear());
        }

        @Test
        void shouldCreateScheduleRecordsForEveryTypeOfSchedule() {
            scheduleService.generateSchedule("2001");

            final List<Schedule> scheduleList = repository.findAll();

            final Set<Integer> scheduleTypes = scheduleList.stream().map(Schedule::getType).collect(Collectors.toSet());

            assertTrue(scheduleTypes.contains(ScheduleType.BEGINNING_OF_SEASON.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.MANAGER_CHANGES.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.PLAYER_CHANGES.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.ROOKIE_DRAFT_ROUND_2.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.TRAINING_CAMP.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.PRESEASON.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.END_OF_PRESEASON.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.ROSTER_CUT.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.REGULAR_SEASON.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.END_OF_REGULAR_SEASON.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.AWARDS.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.POSTSEASON.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.DIVISION_PLAYOFF.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.DIVISION_CHAMPIONSHIP.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.CONFERENCE_CHAMPIONSHIP.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.END_OF_POSTSEASON.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.ALL_STARS.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.ALL_STAR_DAY_1.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.ALL_STAR_DAY_2.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.END_OF_ALLSTAR_GAMES.getValue()));
            assertTrue(scheduleTypes.contains(ScheduleType.END_OF_SEASON.getValue()));
        }

        @Test
        void shouldCreateScheduleRecordsThatAreAllScheduledForSomeDate() {
            scheduleService.generateSchedule("2001");

            final List<Schedule> scheduleList = repository.findAll();

            assertEquals(1, scheduleList.stream().map(Schedule::getStatus).distinct().count());
            assertEquals(ScheduleStatus.SCHEDULED.getValue(), scheduleList.get(0).getStatus());
            assertEquals(0, scheduleList.stream().filter(schedule -> schedule.getScheduled() == null).count());
        }
    }
}