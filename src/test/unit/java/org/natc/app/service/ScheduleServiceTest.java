package org.natc.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.repository.ScheduleRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Captor
    private ArgumentCaptor<List<Schedule>> captor;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private LeagueConfiguration leagueConfiguration;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    public void getCurrentScheduleEntry_ShouldCallScheduleRepository() {
        scheduleService.getCurrentScheduleEntry();

        verify(scheduleRepository).findFirstByStatusOrderByScheduledDesc(ScheduleStatus.IN_PROGRESS.getValue());
    }

    @Test
    public void getCurrentScheduleEntry_ShouldReturnScheduleReturnedByRepository() {
        final Schedule expectedSchedule = new Schedule();

        when(scheduleRepository.findFirstByStatusOrderByScheduledDesc(any())).thenReturn(Optional.of(expectedSchedule));

        final Schedule actualSchedule = scheduleService.getCurrentScheduleEntry();

        assertEquals(expectedSchedule, actualSchedule);
    }

    @Test
    public void getCurrentScheduleEntry_ShouldReturnNullIfRepositoryDoesNotReturnASchedule() {
        when(scheduleRepository.findFirstByStatusOrderByScheduledDesc(any())).thenReturn(Optional.empty());

        final Schedule actualSchedule = scheduleService.getCurrentScheduleEntry();

        assertNull(actualSchedule);
    }

    @Test
    public void getLastScheduleEntry_ShouldCallScheduleRepository() {
        scheduleService.getLastScheduleEntry();

        verify(scheduleRepository).findFirstByStatusOrderByScheduledDesc(ScheduleStatus.COMPLETED.getValue());
    }

    @Test
    public void getLastScheduleEntry_ShouldReturnScheduleReturnedByRepository() {
        final Schedule expectedSchedule = new Schedule();

        when(scheduleRepository.findFirstByStatusOrderByScheduledDesc(any())).thenReturn(Optional.of(expectedSchedule));

        final Schedule actualSchedule = scheduleService.getLastScheduleEntry();

        assertEquals(expectedSchedule, actualSchedule);
    }

    @Test
    public void getLastScheduleEntry_ShouldReturnNullIfRepositoryDoesNotReturnASchedule() {
        when(scheduleRepository.findFirstByStatusOrderByScheduledDesc(any())).thenReturn(Optional.empty());

        final Schedule actualSchedule = scheduleService.getLastScheduleEntry();

        assertNull(actualSchedule);
    }

    @Test
    public void getNextScheduleEntry_ShouldCallScheduleRepositoryWithSameYearAndNextSequenceGivenASchedule() {
        final Schedule input = Schedule.builder().year("2020").sequence(20).build();

        scheduleService.getNextScheduleEntry(input);

        verify(scheduleRepository).findByYearAndSequence(input.getYear(), 21);
    }

    @Test
    public void getNextScheduleEntry_ShouldCallScheduleRepositoryWithFirstSeasonAndSequenceGivenNullInput() {
        final String expectedYear = "1984";

        when(leagueConfiguration.getFirstSeason()).thenReturn(expectedYear);

        scheduleService.getNextScheduleEntry(null);

        verify(scheduleRepository).findByYearAndSequence(expectedYear, Schedule.FIRST_SEQUENCE);
    }

    @Test
    public void getNextScheduleEntry_ShouldCallScheduleRepositoryWithNextYearAndFirstSequenceGiveEndOfSeasonSchedule() {
        final Schedule input = Schedule.builder().year("2020").sequence(45).type(ScheduleType.END_OF_SEASON.getValue()).build();

        scheduleService.getNextScheduleEntry(input);

        verify(scheduleRepository).findByYearAndSequence("2021", Schedule.FIRST_SEQUENCE);
    }

    @Test
    public void getNextScheduleEntry_ShouldReturnScheduleReturnedByRepository() {
        final Schedule input = Schedule.builder().year("2020").sequence(1).build();
        final Schedule expectedSchedule = new Schedule();

        when(scheduleRepository.findByYearAndSequence(anyString(), anyInt())).thenReturn(Optional.of(expectedSchedule));

        final Schedule actualSchedule = scheduleService.getNextScheduleEntry(input);

        assertEquals(expectedSchedule, actualSchedule);
    }

    @Test
    public void updateScheduleEntry_ShouldCallScheduleRepository() {
        scheduleService.updateScheduleEntry(null);

        verify(scheduleRepository).save(any());
    }

    @Test
    public void updateScheduleEntry_ShouldCallScheduleRepositorySaveOnGivenSchedule() {
        final ArgumentCaptor<Schedule> captor = ArgumentCaptor.forClass(Schedule.class);
        final Schedule expectedSchedule = Schedule.builder()
                .year("2000")
                .sequence(123)
                .scheduled(LocalDate.now())
                .type(ScheduleType.REGULAR_SEASON.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .build();

        scheduleService.updateScheduleEntry(expectedSchedule);

        verify(scheduleRepository).save(captor.capture());

        final Schedule actualSchedule = captor.getValue();

        assertSame(expectedSchedule, actualSchedule);
    }

    @Test
    public void generateSchedule_ShouldCallScheduleRepositoryToSaveAListOfSchedules() {
        scheduleService.generateSchedule("1999");

        verify(scheduleRepository).saveAll(ArgumentMatchers.<List<Schedule>>any());
    }

    @Test
    public void generateSchedule_ShouldSaveABeginningOfSeasonScheduleEntryForJanuaryFirstOfGivenYear() {
        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule beginningOfSeason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.BEGINNING_OF_SEASON.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(beginningOfSeason);

        assertEquals("2005", beginningOfSeason.getYear());
        assertEquals(1, beginningOfSeason.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), beginningOfSeason.getStatus());
        assertEquals(LocalDate.parse("2005-01-01"), beginningOfSeason.getScheduled());
    }

    @Test
    public void generateSchedule_ShouldSaveAManagerChangesScheduleEntryForFirstMondayInFebruaryOfGivenYear() {
        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule managerChanges = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.MANAGER_CHANGES.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(managerChanges);

        assertEquals("2005", managerChanges.getYear());
        assertEquals(2, managerChanges.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), managerChanges.getStatus());
        assertEquals(LocalDate.parse("2005-02-07"), managerChanges.getScheduled());
    }

    @Test
    public void generateSchedule_ShouldSaveAPlayerChangesScheduleEntryForFebruaryTwentyEightOfGivenYear() {
        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule playerChanges = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.PLAYER_CHANGES.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(playerChanges);

        assertEquals("2005", playerChanges.getYear());
        assertEquals(3, playerChanges.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), playerChanges.getStatus());
        assertEquals(LocalDate.parse("2005-02-28"), playerChanges.getScheduled());
    }

    @Test
    public void generateSchedule_ShouldSaveTheRookieDraftRoundOneScheduleEntryForFirstMondayInMarchOfGivenYear() {
        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule rookieDraft = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(rookieDraft);

        assertEquals("2005", rookieDraft.getYear());
        assertEquals(4, rookieDraft.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), rookieDraft.getStatus());
        assertEquals(LocalDate.parse("2005-03-07"), rookieDraft.getScheduled());
    }

    @Test
    public void generateSchedule_ShouldSaveTheRookieDraftRoundTwoScheduleEntryForTheDayAfterRoundOneOfGivenYear() {
        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule rookieDraft = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.ROOKIE_DRAFT_ROUND_2.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(rookieDraft);

        assertEquals("2005", rookieDraft.getYear());
        assertEquals(5, rookieDraft.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), rookieDraft.getStatus());
        assertEquals(LocalDate.parse("2005-03-08"), rookieDraft.getScheduled());
    }

    @Test
    public void generateSchedule_ShouldSaveTheTrainingCampScheduleEntryForThirdMondayInMarchOfGivenYear() {
        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule trainingCamp = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.TRAINING_CAMP.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(trainingCamp);

        assertEquals("2005", trainingCamp.getYear());
        assertEquals(6, trainingCamp.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), trainingCamp.getStatus());
        assertEquals(LocalDate.parse("2005-03-21"), trainingCamp.getScheduled());
    }

    @Test
    public void generateSchedule_ShouldSaveConfiguredNumberOfPreseasonScheduleEntriesStartingFirstMondayInAprilOfGivenYear() {
        when(leagueConfiguration.getDaysInPreseason()).thenReturn(10);
        when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);
        when(leagueConfiguration.getNumberOfTeams()).thenReturn(40);
        when(leagueConfiguration.getGamesPerDay()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerConference()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerDivision()).thenReturn(10);
        when(leagueConfiguration.getOutOfConferenceGames()).thenReturn(7);

        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final List<Schedule> preseason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.PRESEASON.getValue()))
                .collect(Collectors.toList());

        assertEquals(leagueConfiguration.getDaysInPreseason(), preseason.size());

        preseason.sort(Comparator.comparing(Schedule::getSequence));

        int expectedSequence = 7;

        for (final Schedule schedule : preseason) {
            assertEquals("2005", schedule.getYear());
            assertEquals(expectedSequence, schedule.getSequence());
            assertEquals(ScheduleStatus.SCHEDULED.getValue(), schedule.getStatus());
            assertEquals(2005, schedule.getScheduled().getYear());
            assertNotNull(schedule.getData());

            expectedSequence++;
        }
    }

    @Test
    public void generateSchedule_ShouldSaveConfiguredNumberOfPreseasonScheduleEntriesAllWithUniqueDataValues() {
        when(leagueConfiguration.getDaysInPreseason()).thenReturn(10);
        when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);
        when(leagueConfiguration.getNumberOfTeams()).thenReturn(40);
        when(leagueConfiguration.getGamesPerDay()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerConference()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerDivision()).thenReturn(10);
        when(leagueConfiguration.getOutOfConferenceGames()).thenReturn(7);

        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final List<Schedule> preseason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.PRESEASON.getValue()))
                .collect(Collectors.toList());

        assertEquals(leagueConfiguration.getDaysInPreseason(), preseason.size());

        final Set<String> dataValues = preseason.stream().map(Schedule::getData).collect(Collectors.toSet());

        assertEquals(leagueConfiguration.getDaysInPreseason(), dataValues.size());
    }

    @Test
    public void generateSchedule_ShouldShouldSchedulePreseasonGamesOnDifferentDays() {
        when(leagueConfiguration.getDaysInPreseason()).thenReturn(10);
        when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);
        when(leagueConfiguration.getNumberOfTeams()).thenReturn(40);
        when(leagueConfiguration.getGamesPerDay()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerConference()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerDivision()).thenReturn(10);
        when(leagueConfiguration.getOutOfConferenceGames()).thenReturn(7);

        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final List<Schedule> preseason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.PRESEASON.getValue()))
                .collect(Collectors.toList());

        assertEquals(leagueConfiguration.getDaysInPreseason(), preseason.size());

        final Set<LocalDate> dates = preseason.stream().map(Schedule::getScheduled).collect(Collectors.toSet());

        assertEquals(leagueConfiguration.getDaysInPreseason(), dates.size());
    }

    @Test
    public void generateSchedule_ShouldShouldNotSchedulePreseasonGamesOnTheWeekend() {
        when(leagueConfiguration.getDaysInPreseason()).thenReturn(10);
        when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);
        when(leagueConfiguration.getNumberOfTeams()).thenReturn(40);
        when(leagueConfiguration.getGamesPerDay()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerConference()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerDivision()).thenReturn(10);
        when(leagueConfiguration.getOutOfConferenceGames()).thenReturn(7);

        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final List<Schedule> preseason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.PRESEASON.getValue()))
                .collect(Collectors.toList());

        assertEquals(leagueConfiguration.getDaysInPreseason(), preseason.size());

        for (final Schedule schedule : preseason) {
            assertNotEquals(DayOfWeek.SATURDAY, schedule.getScheduled().getDayOfWeek());
            assertNotEquals(DayOfWeek.SUNDAY, schedule.getScheduled().getDayOfWeek());
        }
    }

    @Test
    public void generateSchedule_ShouldShouldSchedulePreseasonGamesOnConsecutiveWeekdays() {
        when(leagueConfiguration.getDaysInPreseason()).thenReturn(10);
        when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);
        when(leagueConfiguration.getNumberOfTeams()).thenReturn(40);
        when(leagueConfiguration.getGamesPerDay()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerConference()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerDivision()).thenReturn(10);
        when(leagueConfiguration.getOutOfConferenceGames()).thenReturn(7);

        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final List<Schedule> preseason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.PRESEASON.getValue()))
                .collect(Collectors.toList());

        assertEquals(leagueConfiguration.getDaysInPreseason(), preseason.size());

        preseason.sort(Comparator.comparing(Schedule::getSequence));

        LocalDate previousDate = null;

        for (final Schedule schedule : preseason) {
            if (previousDate == null) {
                previousDate = schedule.getScheduled();
                continue;
            }

            if (previousDate.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
                assertEquals(3, Period.between(previousDate, schedule.getScheduled()).getDays());
                previousDate = schedule.getScheduled();
                continue;
            }

            assertEquals(1, Period.between(previousDate, schedule.getScheduled()).getDays(),
                    "Previous: " + previousDate + ", Next: " + schedule.getScheduled());

            previousDate = schedule.getScheduled();
        }
    }

    @Test
    public void generateSchedule_ShouldSaveAEndOfPreseasonScheduleEntryForTheDayAfterPreseasonGames() {
        when(leagueConfiguration.getDaysInPreseason()).thenReturn(10);
        when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);
        when(leagueConfiguration.getNumberOfTeams()).thenReturn(40);
        when(leagueConfiguration.getGamesPerDay()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerConference()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerDivision()).thenReturn(10);
        when(leagueConfiguration.getOutOfConferenceGames()).thenReturn(7);

        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule lastPreseasonSchedule = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.PRESEASON.getValue()))
                .max(Comparator.comparing(Schedule::getSequence)).orElseThrow();

        final Schedule endOfPresason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.END_OF_PRESEASON.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(endOfPresason);

        assertEquals("2005", endOfPresason.getYear());
        assertEquals(lastPreseasonSchedule.getSequence() + 1, endOfPresason.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), endOfPresason.getStatus());
        assertEquals(lastPreseasonSchedule.getScheduled().plusDays(1), endOfPresason.getScheduled());
    }

    @Test
    public void generateSchedule_ShouldSaveARosterCutScheduleEntryForAprilThirtiethOfGivenYear() {
        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule endOfPresason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.END_OF_PRESEASON.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(endOfPresason);

        final Schedule rosterCut = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.ROSTER_CUT.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(rosterCut);

        assertEquals("2005", rosterCut.getYear());
        assertEquals(endOfPresason.getSequence() + 1, rosterCut.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), rosterCut.getStatus());
        assertEquals(LocalDate.parse("2005-04-30"), rosterCut.getScheduled());
    }

    @Test
    public void generateSchedule_ShouldSaveConfiguredNumberOfRegularSeasonScheduleEntriesStartingFirstMondayInMayOfGivenYear() {
        when(leagueConfiguration.getDaysInPreseason()).thenReturn(10);
        when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);
        when(leagueConfiguration.getNumberOfTeams()).thenReturn(40);
        when(leagueConfiguration.getGamesPerDay()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerConference()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerDivision()).thenReturn(10);
        when(leagueConfiguration.getOutOfConferenceGames()).thenReturn(7);

        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule rosterCut = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.ROSTER_CUT.getValue()))
                .findFirst()
                .orElseThrow();

        final List<Schedule> regularSeason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.REGULAR_SEASON.getValue()))
                .collect(Collectors.toList());

        assertEquals(leagueConfiguration.getDaysInRegularSeason(), regularSeason.size());

        regularSeason.sort(Comparator.comparing(Schedule::getSequence));

        int expectedSequence = rosterCut.getSequence() + 1;

        for (final Schedule schedule : regularSeason) {
            assertEquals("2005", schedule.getYear());
            assertEquals(expectedSequence, schedule.getSequence());
            assertEquals(ScheduleStatus.SCHEDULED.getValue(), schedule.getStatus());
            assertEquals(2005, schedule.getScheduled().getYear());
            assertNotNull(schedule.getData());

            expectedSequence++;
        }
    }

    @Test
    public void generateSchedule_ShouldSaveConfiguredNumberOfRegularSeasonScheduleEntriesAllWithSixtyThreeUniqueDataValues() {
        // 40 teams, 10 per division, 2 divisions per conference
        // 9 games within division times 2 (home and road)
        // 18 games within conference time 2
        // 7 games between conferences
        final Integer expectedDataCount = 63;

        when(leagueConfiguration.getDaysInPreseason()).thenReturn(10);
        when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);
        when(leagueConfiguration.getNumberOfTeams()).thenReturn(40);
        when(leagueConfiguration.getGamesPerDay()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerConference()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerDivision()).thenReturn(10);
        when(leagueConfiguration.getOutOfConferenceGames()).thenReturn(7);

        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final List<Schedule> regularSeason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.REGULAR_SEASON.getValue()))
                .collect(Collectors.toList());

        assertEquals(leagueConfiguration.getDaysInRegularSeason(), regularSeason.size());

        final Set<String> dataValues = regularSeason.stream().map(Schedule::getData).collect(Collectors.toSet());

        assertEquals(expectedDataCount, dataValues.size());
    }

    @Test
    public void generateSchedule_ShouldShouldScheduleRegularSeasonGamesOnDifferentDays() {
        when(leagueConfiguration.getDaysInPreseason()).thenReturn(10);
        when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);
        when(leagueConfiguration.getNumberOfTeams()).thenReturn(40);
        when(leagueConfiguration.getGamesPerDay()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerConference()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerDivision()).thenReturn(10);
        when(leagueConfiguration.getOutOfConferenceGames()).thenReturn(7);

        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final List<Schedule> regularSeason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.REGULAR_SEASON.getValue()))
                .collect(Collectors.toList());

        assertEquals(leagueConfiguration.getDaysInRegularSeason(), regularSeason.size());

        final Set<LocalDate> dates = regularSeason.stream().map(Schedule::getScheduled).collect(Collectors.toSet());

        assertEquals(leagueConfiguration.getDaysInRegularSeason(), dates.size());
    }

    @Test
    public void generateSchedule_ShouldShouldNotScheduleRegularSeasonGamesOnTheWeekend() {
        when(leagueConfiguration.getDaysInPreseason()).thenReturn(10);
        when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);
        when(leagueConfiguration.getNumberOfTeams()).thenReturn(40);
        when(leagueConfiguration.getGamesPerDay()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerConference()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerDivision()).thenReturn(10);
        when(leagueConfiguration.getOutOfConferenceGames()).thenReturn(7);

        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final List<Schedule> regularSeason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.REGULAR_SEASON.getValue()))
                .collect(Collectors.toList());

        assertEquals(leagueConfiguration.getDaysInRegularSeason(), regularSeason.size());

        for (final Schedule schedule : regularSeason) {
            assertNotEquals(DayOfWeek.SATURDAY, schedule.getScheduled().getDayOfWeek());
            assertNotEquals(DayOfWeek.SUNDAY, schedule.getScheduled().getDayOfWeek());
        }
    }

    @Test
    public void generateSchedule_ShouldShouldScheduleRegularSeasonGamesOnConsecutiveWeekdays() {
        when(leagueConfiguration.getDaysInPreseason()).thenReturn(10);
        when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);
        when(leagueConfiguration.getNumberOfTeams()).thenReturn(40);
        when(leagueConfiguration.getGamesPerDay()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerConference()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerDivision()).thenReturn(10);
        when(leagueConfiguration.getOutOfConferenceGames()).thenReturn(7);

        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final List<Schedule> regularSeason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.REGULAR_SEASON.getValue()))
                .collect(Collectors.toList());

        assertEquals(leagueConfiguration.getDaysInRegularSeason(), regularSeason.size());

        regularSeason.sort(Comparator.comparing(Schedule::getSequence));

        LocalDate previousDate = null;

        for (final Schedule schedule : regularSeason) {
            if (previousDate == null) {
                previousDate = schedule.getScheduled();
                continue;
            }

            if (previousDate.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
                assertEquals(3, Period.between(previousDate, schedule.getScheduled()).getDays());
                previousDate = schedule.getScheduled();
                continue;
            }

            assertEquals(1, Period.between(previousDate, schedule.getScheduled()).getDays());

            previousDate = schedule.getScheduled();
        }
    }

    @Test
    public void generateSchedule_ShouldSaveAEndOfRegularSeasonScheduleEntryForTheDayAfterRegularSeasonGames() {
        when(leagueConfiguration.getDaysInPreseason()).thenReturn(10);
        when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);
        when(leagueConfiguration.getNumberOfTeams()).thenReturn(40);
        when(leagueConfiguration.getGamesPerDay()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerConference()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerDivision()).thenReturn(10);
        when(leagueConfiguration.getOutOfConferenceGames()).thenReturn(7);

        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule lastRegularSeasonSchedule = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.REGULAR_SEASON.getValue()))
                .max(Comparator.comparing(Schedule::getSequence)).orElseThrow();

        final Schedule endOfRegularSeason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.END_OF_REGULAR_SEASON.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(endOfRegularSeason);

        assertEquals("2005", endOfRegularSeason.getYear());
        assertEquals(lastRegularSeasonSchedule.getSequence() + 1, endOfRegularSeason.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), endOfRegularSeason.getStatus());
        assertEquals(lastRegularSeasonSchedule.getScheduled().plusDays(1), endOfRegularSeason.getScheduled());
    }

    @Test
    public void generateSchedule_ShouldSaveAnAwardsScheduleEntryForTheMondayAfterEndOfRegularSeason() {
        when(leagueConfiguration.getDaysInPreseason()).thenReturn(10);
        when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);
        when(leagueConfiguration.getNumberOfTeams()).thenReturn(40);
        when(leagueConfiguration.getGamesPerDay()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerConference()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerDivision()).thenReturn(10);
        when(leagueConfiguration.getOutOfConferenceGames()).thenReturn(7);

        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule endOfRegularSeason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.END_OF_REGULAR_SEASON.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(endOfRegularSeason);

        final Schedule awards = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.AWARDS.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(awards);

        assertEquals("2005", awards.getYear());
        assertEquals(endOfRegularSeason.getSequence() + 1, awards.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), awards.getStatus());

        assertEquals(DayOfWeek.MONDAY, awards.getScheduled().getDayOfWeek());
        assertTrue(awards.getScheduled().isAfter(endOfRegularSeason.getScheduled()));
        assertTrue(Period.between(awards.getScheduled(), endOfRegularSeason.getScheduled()).getDays() <= 7);
    }

    @Test
    public void generateSchedule_ShouldSaveAPostseasonScheduleEntryForTheFridayAfterAwards() {
        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule awards = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.AWARDS.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(awards);

        final Schedule postseason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.POSTSEASON.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(postseason);

        assertEquals("2005", postseason.getYear());
        assertEquals(awards.getSequence() + 1, postseason.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), postseason.getStatus());

        assertEquals(DayOfWeek.FRIDAY, postseason.getScheduled().getDayOfWeek());
        assertTrue(postseason.getScheduled().isAfter(awards.getScheduled()));
        assertTrue(Period.between(postseason.getScheduled(), awards.getScheduled()).getDays() <= 7);
    }

    @Test
    public void generateSchedule_ShouldSaveConfiguredNumberOfDivisionPlayoffScheduleEntriesStartingSundayAfterPostseason() {
        when(leagueConfiguration.getPlayoffGamesRoundOne()).thenReturn(7);

        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule postseason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.POSTSEASON.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(postseason);

        final List<Schedule> divisionPlayoff = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.DIVISION_PLAYOFF.getValue()))
                .collect(Collectors.toList());

        assertEquals(leagueConfiguration.getPlayoffGamesRoundOne(), divisionPlayoff.size());

        divisionPlayoff.sort(Comparator.comparing(Schedule::getSequence));

        final Schedule firstDivisionPlayoff = divisionPlayoff.get(0);

        assertEquals(DayOfWeek.SUNDAY, firstDivisionPlayoff.getScheduled().getDayOfWeek());
        assertTrue(firstDivisionPlayoff.getScheduled().isAfter(postseason.getScheduled()));
        assertTrue(Period.between(firstDivisionPlayoff.getScheduled(), postseason.getScheduled()).getDays() <= 7);

        Integer expectedSequence = postseason.getSequence() + 1;
        LocalDate expectedScheduledDate = firstDivisionPlayoff.getScheduled();

        for (final Schedule schedule : divisionPlayoff) {
            assertEquals("2005", schedule.getYear());
            assertEquals(expectedSequence, schedule.getSequence());
            assertEquals(ScheduleStatus.SCHEDULED.getValue(), schedule.getStatus());
            assertEquals(expectedScheduledDate, schedule.getScheduled());

            expectedSequence++;
            expectedScheduledDate = expectedScheduledDate.plusDays(1);
        }
    }

    @Test
    public void generateSchedule_ShouldSaveConfiguredNumberOfDivisionChampionshipScheduleEntriesStartingSundayAfterDivisionPlayoffs() {
        when(leagueConfiguration.getPlayoffGamesRoundOne()).thenReturn(7);
        when(leagueConfiguration.getPlayoffGamesRoundTwo()).thenReturn(5);

        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule lastDivisionPlayoff = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.DIVISION_PLAYOFF.getValue()))
                .max(Comparator.comparing(Schedule::getSequence)).orElseThrow();

        final List<Schedule> divisionChampionship = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.DIVISION_CHAMPIONSHIP.getValue()))
                .collect(Collectors.toList());

        assertEquals(leagueConfiguration.getPlayoffGamesRoundTwo(), divisionChampionship.size());

        divisionChampionship.sort(Comparator.comparing(Schedule::getSequence));

        final Schedule firstDivisionChampionship = divisionChampionship.get(0);

        assertEquals(DayOfWeek.SUNDAY, firstDivisionChampionship.getScheduled().getDayOfWeek());
        assertTrue(firstDivisionChampionship.getScheduled().isAfter(lastDivisionPlayoff.getScheduled()));
        assertTrue(Period.between(firstDivisionChampionship.getScheduled(), lastDivisionPlayoff.getScheduled()).getDays() <= 7);

        Integer expectedSequence = lastDivisionPlayoff.getSequence() + 1;
        LocalDate expectedScheduledDate = firstDivisionChampionship.getScheduled();

        for (final Schedule schedule : divisionChampionship) {
            assertEquals("2005", schedule.getYear());
            assertEquals(expectedSequence, schedule.getSequence());
            assertEquals(ScheduleStatus.SCHEDULED.getValue(), schedule.getStatus());
            assertEquals(expectedScheduledDate, schedule.getScheduled());

            expectedSequence++;
            expectedScheduledDate = expectedScheduledDate.plusDays(1);
        }
    }

    @Test
    public void generateSchedule_ShouldSaveConfiguredNumberOfConferenceChampionshipScheduleEntriesStartingSundayAfterDivisionChampionships() {
        when(leagueConfiguration.getPlayoffGamesRoundOne()).thenReturn(7);
        when(leagueConfiguration.getPlayoffGamesRoundTwo()).thenReturn(5);
        when(leagueConfiguration.getPlayoffGamesRoundThree()).thenReturn(3);

        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule lastDivisionChampionship = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.DIVISION_CHAMPIONSHIP.getValue()))
                .max(Comparator.comparing(Schedule::getSequence)).orElseThrow();

        final List<Schedule> conferenceChampionship = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.CONFERENCE_CHAMPIONSHIP.getValue()))
                .collect(Collectors.toList());

        assertEquals(leagueConfiguration.getPlayoffGamesRoundThree(), conferenceChampionship.size());

        conferenceChampionship.sort(Comparator.comparing(Schedule::getSequence));

        final Schedule firstConferenceChampionship = conferenceChampionship.get(0);

        assertEquals(DayOfWeek.SUNDAY, firstConferenceChampionship.getScheduled().getDayOfWeek());
        assertTrue(firstConferenceChampionship.getScheduled().isAfter(lastDivisionChampionship.getScheduled()));
        assertTrue(Period.between(firstConferenceChampionship.getScheduled(), lastDivisionChampionship.getScheduled()).getDays() <= 7);

        Integer expectedSequence = lastDivisionChampionship.getSequence() + 1;
        LocalDate expectedScheduledDate = firstConferenceChampionship.getScheduled();

        for (final Schedule schedule : conferenceChampionship) {
            assertEquals("2005", schedule.getYear());
            assertEquals(expectedSequence, schedule.getSequence());
            assertEquals(ScheduleStatus.SCHEDULED.getValue(), schedule.getStatus());
            assertEquals(expectedScheduledDate, schedule.getScheduled());

            expectedSequence++;
            expectedScheduledDate = expectedScheduledDate.plusDays(1);
        }
    }

    @Test
    public void generateSchedule_ShouldSaveANATCChampionshipScheduleEntryForTheSundayAfterDivisionChampionships() {
        when(leagueConfiguration.getPlayoffGamesRoundOne()).thenReturn(7);
        when(leagueConfiguration.getPlayoffGamesRoundTwo()).thenReturn(5);
        when(leagueConfiguration.getPlayoffGamesRoundThree()).thenReturn(3);

        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule lastConferenceChampionship = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.CONFERENCE_CHAMPIONSHIP.getValue()))
                .max(Comparator.comparing(Schedule::getSequence)).orElseThrow();

        final Schedule natcChampionship = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.NATC_CHAMPIONSHIP.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(natcChampionship);

        assertEquals("2005", natcChampionship.getYear());
        assertEquals(lastConferenceChampionship.getSequence() + 1, natcChampionship.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), natcChampionship.getStatus());

        assertEquals(DayOfWeek.SUNDAY, natcChampionship.getScheduled().getDayOfWeek());
        assertTrue(natcChampionship.getScheduled().isAfter(lastConferenceChampionship.getScheduled()));
        assertTrue(Period.between(natcChampionship.getScheduled(), lastConferenceChampionship.getScheduled()).getDays() <= 7);
    }

    @Test
    public void generateSchedule_ShouldSaveAEndOfPostseasonScheduleEntryForTheDayAfterNATCChampionship() {
        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule natcChampionship = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.NATC_CHAMPIONSHIP.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(natcChampionship);

        final Schedule endOfPostseason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.END_OF_POSTSEASON.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(endOfPostseason);

        assertEquals("2005", endOfPostseason.getYear());
        assertEquals(natcChampionship.getSequence() + 1, endOfPostseason.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), endOfPostseason.getStatus());
        assertEquals(natcChampionship.getScheduled().plusDays(1), endOfPostseason.getScheduled());
    }

    @Test
    public void generateSchedule_ShouldSaveAnAllStarsScheduleEntryForTheDayAfterEndOfPostseason() {
        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule endOfPostseason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.END_OF_POSTSEASON.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(endOfPostseason);

        final Schedule allStars = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.ALL_STARS.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(allStars);

        assertEquals("2005", allStars.getYear());
        assertEquals(endOfPostseason.getSequence() + 1, allStars.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), allStars.getStatus());
        assertEquals(endOfPostseason.getScheduled().plusDays(1), allStars.getScheduled());
    }

    @Test
    public void generateSchedule_ShouldSaveAnAllStarDayOneScheduleEntryForTheSaturdayAfterAllStars() {
        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule allStars = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.ALL_STARS.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(allStars);

        final Schedule allStarDayOne = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.ALL_STAR_DAY_1.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(allStarDayOne);

        assertEquals("2005", allStarDayOne.getYear());
        assertEquals(allStars.getSequence() + 1, allStarDayOne.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), allStarDayOne.getStatus());

        assertEquals(DayOfWeek.SATURDAY, allStarDayOne.getScheduled().getDayOfWeek());
        assertTrue(allStarDayOne.getScheduled().isAfter(allStars.getScheduled()));
        assertTrue(Period.between(allStarDayOne.getScheduled(), allStars.getScheduled()).getDays() <= 7);
    }

    @Test
    public void generateSchedule_ShouldSaveAnAllStarDayTwoScheduleEntryForTheDayAfterAllStarDayOne() {
        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule allStarDayOne = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.ALL_STAR_DAY_1.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(allStarDayOne);

        final Schedule allStarDayTwo = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.ALL_STAR_DAY_2.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(allStarDayTwo);

        assertEquals("2005", allStarDayTwo.getYear());
        assertEquals(allStarDayOne.getSequence() + 1, allStarDayTwo.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), allStarDayTwo.getStatus());
        assertEquals(allStarDayOne.getScheduled().plusDays(1), allStarDayTwo.getScheduled());
    }

    @Test
    public void generateSchedule_ShouldSaveAnEndOfAllStarGamesScheduleEntryForTheDayAfterAllStarDayTwo() {
        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule allStarDayTwo = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.ALL_STAR_DAY_2.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(allStarDayTwo);

        final Schedule endOfAllStarGames = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.END_OF_ALLSTAR_GAMES.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(endOfAllStarGames);

        assertEquals("2005", endOfAllStarGames.getYear());
        assertEquals(allStarDayTwo.getSequence() + 1, endOfAllStarGames.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), endOfAllStarGames.getStatus());
        assertEquals(allStarDayTwo.getScheduled().plusDays(1), endOfAllStarGames.getScheduled());
    }

    @Test
    public void generateSchedule_ShouldSaveAnEndOfSeasonScheduleEntryForFirstMondayInNovemberFirstOfGivenYear() {
        scheduleService.generateSchedule("2005");

        verify(scheduleRepository).saveAll(captor.capture());

        final Schedule endOfAllStarGames = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.END_OF_ALLSTAR_GAMES.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(endOfAllStarGames);

        final Schedule endOfSeason = captor.getValue().stream()
                .filter(schedule -> schedule.getType().equals(ScheduleType.END_OF_SEASON.getValue()))
                .findFirst()
                .orElse(null);

        assertNotNull(endOfSeason);

        assertEquals("2005", endOfSeason.getYear());
        assertEquals(endOfAllStarGames.getSequence() + 1, endOfSeason.getSequence());
        assertEquals(ScheduleStatus.SCHEDULED.getValue(), endOfSeason.getStatus());
        assertEquals(LocalDate.parse("2005-11-07"), endOfSeason.getScheduled());
    }
}