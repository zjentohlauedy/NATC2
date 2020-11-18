package org.natc.app.service;

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

    @Test
    public void getCurrentScheduleEntry_ShouldReturnNullIfNoSchedulesExist() {
        final Schedule schedule = scheduleService.getCurrentScheduleEntry();

        assertNull(schedule);
    }

    @Test
    public void getCurrentScheduleEntry_ShouldReturnNullIfNoInProgressSchedulesExist() {
        final List<Schedule> scheduleList = Arrays.asList(
                Schedule.builder().year("2020").sequence(1).scheduled(LocalDate.parse("2020-01-01")).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2020").sequence(2).scheduled(LocalDate.parse("2020-01-02")).status(ScheduleStatus.SCHEDULED.getValue()).build()
        );

        repository.saveAll(scheduleList);

        final Schedule schedule = scheduleService.getCurrentScheduleEntry();

        assertNull(schedule);
    }

    @Test
    public void getCurrentScheduleEntry_ShouldReturnInProgressSchedule() {
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
    public void getCurrentScheduleEntry_ShouldReturnMostRecentlyScheduledInProgressSchedule() {
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

    @Test
    public void getLastScheduleEntry_ShouldReturnNullIfNoSchedulesExist() {
        final Schedule schedule = scheduleService.getLastScheduleEntry();

        assertNull(schedule);
    }

    @Test
    public void getLastScheduleEntry_ShouldReturnNullIfNoCompletedSchedulesExist() {
        final List<Schedule> scheduleList = Arrays.asList(
                Schedule.builder().year("2020").sequence(1).scheduled(LocalDate.parse("2020-01-01")).status(ScheduleStatus.IN_PROGRESS.getValue()).build(),
                Schedule.builder().year("2020").sequence(2).scheduled(LocalDate.parse("2020-01-02")).status(ScheduleStatus.SCHEDULED.getValue()).build()
        );

        repository.saveAll(scheduleList);

        final Schedule schedule = scheduleService.getLastScheduleEntry();

        assertNull(schedule);
    }

    @Test
    public void getLastScheduleEntry_ShouldReturnCompletedSchedule() {
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
    public void getLastScheduleEntry_ShouldReturnMostRecentlyScheduledCompletedSchedule() {
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

    @Test
    public void getNextScheduleEntry_ShouldReturnNullIfNoSchedulesExist() {
        final Schedule schedule = scheduleService.getNextScheduleEntry(null);

        assertNull(schedule);
    }

    @Test
    public void getNextScheduleEntry_ShouldReturnNullIfNoSchedulesExistGivenAPreviousSchedule() {
        final Schedule currentSchedule = Schedule.builder()
                .year("2000")
                .sequence(1)
                .type(ScheduleType.REGULAR_SEASON.getValue())
                .build();

        final Schedule schedule = scheduleService.getNextScheduleEntry(currentSchedule);

        assertNull(schedule);
    }

    @Test
    public void getNextScheduleEntry_ShouldReturnNullIfNoSchedulesExistGivenAnEndOfSeasonSchedule() {
        final Schedule currentSchedule = Schedule.builder()
                .year("2000")
                .sequence(1)
                .type(ScheduleType.END_OF_SEASON.getValue())
                .build();

        final Schedule schedule = scheduleService.getNextScheduleEntry(currentSchedule);

        assertNull(schedule);
    }

    @Test
    public void getNextScheduleEntry_ShouldReturnFirstDefinedScheduleWhenGivenNull() {
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
    public void getNextScheduleEntry_ShouldReturnNullIfFirstDefinedScheduleDoesNotExistWhenGivenNull() {
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
    public void getNextScheduleEntry_ShouldReturnFirstSequenceOfTheFollowingYearGivenEndOfSeasonSchedule() {
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
    public void getNextScheduleEntry_ShouldReturnNullIfFirstSequenceOfTheFollowingYearDoesNotExistGivenEndOfSeasonSchedule() {
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
    public void getNextScheduleEntry_ShouldReturnNextSequenceScheduleOfSameYearGivenASchedule() {
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
    public void getNextScheduleEntry_ShouldReturnNullIfNextSequenceScheduleOfSameYearDoesNotExistGivenASchedule() {
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

    @Test
    public void updateScheduleEntry_ShouldUpdateScheduleRecord() {
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

    @Test
    public void generateSchedule_ShouldCreateScheduleRecordsForGivenYear() {
        scheduleService.generateSchedule("2001");

        final List<Schedule> scheduleList = repository.findAll();

        final Set<String> years = scheduleList.stream().map(Schedule::getYear).collect(Collectors.toSet());

        assertEquals(1, years.size());
        assertEquals("2001", years.stream().findFirst().orElse(null));
    }

    @Test
    public void generateSchedule_ShouldCreateScheduleRecordsForEveryTypeOfSchedule() {
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
    public void generateSchedule_ShouldCreateScheduleRecordsThatAreAllScheduledForSomeDate() {
        scheduleService.generateSchedule("2001");

        final List<Schedule> scheduleList = repository.findAll();

        final Set<Integer> statuses = scheduleList.stream().map(Schedule::getStatus).collect(Collectors.toSet());

        assertEquals(1, statuses.size());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), statuses.stream().findFirst().orElse(null));
        assertEquals(0, scheduleList.stream().filter(schedule -> schedule.getScheduled() == null).count());
    }
}