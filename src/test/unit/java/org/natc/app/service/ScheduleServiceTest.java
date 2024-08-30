package org.natc.app.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleData;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.generator.ScheduleDataGenerator;
import org.natc.app.manager.ScheduleDataGeneratorManager;
import org.natc.app.repository.ScheduleRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private LeagueConfiguration leagueConfiguration;

    @Mock
    private ScheduleDataGeneratorManager scheduleDataGeneratorManager;

    @InjectMocks
    private ScheduleService scheduleService;

    @Nested
    class GetCurrentScheduleEntry {

        @Test
        void shouldCallScheduleRepository() {
            scheduleService.getCurrentScheduleEntry();

            verify(scheduleRepository).findFirstByStatusOrderByScheduledDesc(ScheduleStatus.IN_PROGRESS.getValue());
        }

        @Test
        void shouldReturnScheduleReturnedByRepository() {
            final Schedule expectedSchedule = new Schedule();

            when(scheduleRepository.findFirstByStatusOrderByScheduledDesc(any())).thenReturn(Optional.of(expectedSchedule));

            final Schedule actualSchedule = scheduleService.getCurrentScheduleEntry();

            assertEquals(expectedSchedule, actualSchedule);
        }

        @Test
        void shouldReturnNullIfRepositoryDoesNotReturnASchedule() {
            when(scheduleRepository.findFirstByStatusOrderByScheduledDesc(any())).thenReturn(Optional.empty());

            final Schedule actualSchedule = scheduleService.getCurrentScheduleEntry();

            assertNull(actualSchedule);
        }
    }

    @Nested
    class GetLastScheduleEntry {

        @Test
        void shouldCallScheduleRepository() {
            scheduleService.getLastScheduleEntry();

            verify(scheduleRepository).findFirstByStatusOrderByScheduledDesc(ScheduleStatus.COMPLETED.getValue());
        }

        @Test
        void shouldReturnScheduleReturnedByRepository() {
            final Schedule expectedSchedule = new Schedule();

            when(scheduleRepository.findFirstByStatusOrderByScheduledDesc(any())).thenReturn(Optional.of(expectedSchedule));

            final Schedule actualSchedule = scheduleService.getLastScheduleEntry();

            assertEquals(expectedSchedule, actualSchedule);
        }

        @Test
        void shouldReturnNullIfRepositoryDoesNotReturnASchedule() {
            when(scheduleRepository.findFirstByStatusOrderByScheduledDesc(any())).thenReturn(Optional.empty());

            final Schedule actualSchedule = scheduleService.getLastScheduleEntry();

            assertNull(actualSchedule);
        }
    }

    @Nested
    class GetNextScheduleEntry {

        @Test
        void shouldCallScheduleRepositoryWithSameYearAndNextSequenceGivenASchedule() {
            final Schedule input = Schedule.builder().year("2020").sequence(20).build();

            scheduleService.getNextScheduleEntry(input);

            verify(scheduleRepository).findByYearAndSequence(input.getYear(), 21);
        }

        @Test
        void shouldCallScheduleRepositoryWithFirstSeasonAndSequenceGivenNullInput() {
            final String expectedYear = "1984";

            when(leagueConfiguration.getFirstSeason()).thenReturn(expectedYear);

            scheduleService.getNextScheduleEntry(null);

            verify(scheduleRepository).findByYearAndSequence(expectedYear, Schedule.FIRST_SEQUENCE);
        }

        @Test
        void shouldCallScheduleRepositoryWithNextYearAndFirstSequenceGiveEndOfSeasonSchedule() {
            final Schedule input = Schedule.builder().year("2020").sequence(45).type(ScheduleType.END_OF_SEASON.getValue()).build();

            scheduleService.getNextScheduleEntry(input);

            verify(scheduleRepository).findByYearAndSequence("2021", Schedule.FIRST_SEQUENCE);
        }

        @Test
        void shouldReturnScheduleReturnedByRepository() {
            final Schedule input = Schedule.builder().year("2020").sequence(1).build();
            final Schedule expectedSchedule = new Schedule();

            when(scheduleRepository.findByYearAndSequence(anyString(), anyInt())).thenReturn(Optional.of(expectedSchedule));

            final Schedule actualSchedule = scheduleService.getNextScheduleEntry(input);

            assertEquals(expectedSchedule, actualSchedule);
        }
    }

    @Nested
    class UpdateScheduleEntry {

        @Test
        void shouldCallScheduleRepository() {
            scheduleService.updateScheduleEntry(null);

            verify(scheduleRepository).save(any());
        }

        @Test
        void shouldCallScheduleRepositorySaveOnGivenSchedule() {
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
    }

    @Nested
    class GenerateSchedule {

        @Captor
        private ArgumentCaptor<List<Schedule>> captor;

        @Test
        void shouldCallScheduleRepositoryToSaveAListOfSchedules() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

            scheduleService.generateSchedule("1999");

            verify(scheduleRepository).saveAll(ArgumentMatchers.<List<Schedule>>any());
        }

        @Test
        void shouldSaveABeginningOfSeasonScheduleEntryForJanuaryFirstOfGivenYear() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldSaveAManagerChangesScheduleEntryForFirstMondayInFebruaryOfGivenYear() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldSaveAPlayerChangesScheduleEntryForFebruaryTwentyEightOfGivenYear() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldSaveTheRookieDraftRoundOneScheduleEntryForFirstMondayInMarchOfGivenYear() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldSaveTheRookieDraftRoundTwoScheduleEntryForTheDayAfterRoundOneOfGivenYear() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldSaveTheTrainingCampScheduleEntryForThirdMondayInMarchOfGivenYear() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldCallScheduleDataGeneratorManagerToGetPreseasonScheduleDataGenerator() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

            scheduleService.generateSchedule("2005");

            verify(scheduleDataGeneratorManager).getGeneratorFor(ScheduleType.PRESEASON);
        }

        @Test
        void shouldUseThePreseasonScheduleDataGeneratorToGenerateThePreseasonSchedule() {
            final ScheduleDataGenerator generator = mock(ScheduleDataGenerator.class);

            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.PRESEASON)).thenReturn(generator);
            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.REGULAR_SEASON)).thenReturn(mock(ScheduleDataGenerator.class));

            scheduleService.generateSchedule("2005");

            verify(generator).generate();
        }

        @Test
        void shouldSavePreseasonScheduleEntriesFromScheduleDataGeneratorStartingFirstMondayInAprilOfGivenYear() {
            final List<ScheduleData> preseasonScheduleData = generateFakePreseasonData();
            final ScheduleDataGenerator generator = mock(ScheduleDataGenerator.class);

            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.PRESEASON)).thenReturn(generator);
            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.REGULAR_SEASON)).thenReturn(mock(ScheduleDataGenerator.class));
            when(generator.generate()).thenReturn(preseasonScheduleData);

            scheduleService.generateSchedule("2005");

            verify(scheduleRepository).saveAll(captor.capture());

            final List<Schedule> preseason = captor.getValue().stream()
                    .filter(schedule -> schedule.getType().equals(ScheduleType.PRESEASON.getValue()))
                    .collect(Collectors.toList());

            assertEquals(preseasonScheduleData.size(), preseason.size());

            preseason.sort(Comparator.comparing(Schedule::getSequence));

            assertEquals(LocalDate.parse("2005-04-04"), preseason.get(0).getScheduled());

            int expectedSequence = 7;

            for (int i = 0; i < preseason.size(); ++i) {
                final Schedule schedule = preseason.get(i);

                assertEquals("2005", schedule.getYear());
                assertEquals(expectedSequence, schedule.getSequence());
                assertEquals(ScheduleStatus.SCHEDULED.getValue(), schedule.getStatus());
                assertEquals(2005, schedule.getScheduled().getYear());
                assertEquals(preseasonScheduleData.get(i).toString(), schedule.getData());

                expectedSequence++;
            }
        }

        @Test
        void shouldSchedulePreseasonGamesOnDifferentDays() {
            final List<ScheduleData> preseasonScheduleData = generateFakePreseasonData();
            final ScheduleDataGenerator generator = mock(ScheduleDataGenerator.class);

            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.PRESEASON)).thenReturn(generator);
            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.REGULAR_SEASON)).thenReturn(mock(ScheduleDataGenerator.class));
            when(generator.generate()).thenReturn(preseasonScheduleData);

            scheduleService.generateSchedule("2005");

            verify(scheduleRepository).saveAll(captor.capture());

            final List<Schedule> preseason = captor.getValue().stream()
                    .filter(schedule -> schedule.getType().equals(ScheduleType.PRESEASON.getValue()))
                    .collect(Collectors.toList());

            assertEquals(preseasonScheduleData.size(), preseason.size());

            assertEquals(preseasonScheduleData.size(), preseason.stream().map(Schedule::getScheduled).distinct().count());
        }

        @Test
        void shouldNotSchedulePreseasonGamesOnTheWeekend() {
            final List<ScheduleData> preseasonScheduleData = generateFakePreseasonData();
            final ScheduleDataGenerator generator = mock(ScheduleDataGenerator.class);

            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.PRESEASON)).thenReturn(generator);
            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.REGULAR_SEASON)).thenReturn(mock(ScheduleDataGenerator.class));
            when(generator.generate()).thenReturn(preseasonScheduleData);

            scheduleService.generateSchedule("2005");

            verify(scheduleRepository).saveAll(captor.capture());

            final List<Schedule> preseason = captor.getValue().stream()
                    .filter(schedule -> schedule.getType().equals(ScheduleType.PRESEASON.getValue()))
                    .collect(Collectors.toList());

            assertEquals(preseasonScheduleData.size(), preseason.size());

            for (final Schedule schedule : preseason) {
                assertNotEquals(DayOfWeek.SATURDAY, schedule.getScheduled().getDayOfWeek());
                assertNotEquals(DayOfWeek.SUNDAY, schedule.getScheduled().getDayOfWeek());
            }
        }

        @Test
        void shouldSchedulePreseasonGamesOnConsecutiveWeekdays() {
            final List<ScheduleData> preseasonScheduleData = generateFakePreseasonData();
            final ScheduleDataGenerator generator = mock(ScheduleDataGenerator.class);

            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.PRESEASON)).thenReturn(generator);
            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.REGULAR_SEASON)).thenReturn(mock(ScheduleDataGenerator.class));
            when(generator.generate()).thenReturn(preseasonScheduleData);

            scheduleService.generateSchedule("2005");

            verify(scheduleRepository).saveAll(captor.capture());

            final List<Schedule> preseason = captor.getValue().stream()
                    .filter(schedule -> schedule.getType().equals(ScheduleType.PRESEASON.getValue()))
                    .collect(Collectors.toList());

            assertEquals(preseasonScheduleData.size(), preseason.size());

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
        void shouldSaveAEndOfPreseasonScheduleEntryForTheDayAfterPreseasonGames() {
            final ScheduleDataGenerator generator = mock(ScheduleDataGenerator.class);

            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.PRESEASON)).thenReturn(generator);
            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.REGULAR_SEASON)).thenReturn(mock(ScheduleDataGenerator.class));
            when(generator.generate()).thenReturn(generateFakePreseasonData());

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
        void shouldSaveARosterCutScheduleEntryForAprilThirtiethOfGivenYear() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldCallScheduleDataGeneratorManagerToGetRegularSeasonScheduleDataGenerator() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

            scheduleService.generateSchedule("2005");

            verify(scheduleDataGeneratorManager).getGeneratorFor(ScheduleType.REGULAR_SEASON);
        }

        @Test
        void shouldUseTheRegularSeasonScheduleDataGeneratorToGenerateTheRegularSeasonSchedule() {
            final ScheduleDataGenerator generator = mock(ScheduleDataGenerator.class);

            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.PRESEASON)).thenReturn(mock(ScheduleDataGenerator.class));
            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.REGULAR_SEASON)).thenReturn(generator);

            scheduleService.generateSchedule("2005");

            verify(generator).generate();
        }

        @Test
        void shouldSaveRegularSeasonScheduleEntriesFromScheduleDataGeneratorStartingFirstMondayInMayOfGivenYear() {
            final List<ScheduleData> regularSeasonScheduleData = generateFakeRegularSeasonData();
            final ScheduleDataGenerator generator = mock(ScheduleDataGenerator.class);

            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.PRESEASON)).thenReturn(mock(ScheduleDataGenerator.class));
            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.REGULAR_SEASON)).thenReturn(generator);
            when(generator.generate()).thenReturn(regularSeasonScheduleData);

            scheduleService.generateSchedule("2005");

            verify(scheduleRepository).saveAll(captor.capture());

            final Schedule rosterCut = captor.getValue().stream()
                    .filter(schedule -> schedule.getType().equals(ScheduleType.ROSTER_CUT.getValue()))
                    .findFirst()
                    .orElseThrow();

            final List<Schedule> regularSeason = captor.getValue().stream()
                    .filter(schedule -> schedule.getType().equals(ScheduleType.REGULAR_SEASON.getValue()))
                    .collect(Collectors.toList());

            assertEquals(regularSeasonScheduleData.size(), regularSeason.size());

            regularSeason.sort(Comparator.comparing(Schedule::getSequence));

            assertEquals(LocalDate.parse("2005-05-02"), regularSeason.get(0).getScheduled());

            int expectedSequence = rosterCut.getSequence() + 1;

            for (int i = 0; i < regularSeason.size(); ++i) {
                final Schedule schedule = regularSeason.get(i);

                assertEquals("2005", schedule.getYear());
                assertEquals(expectedSequence, schedule.getSequence());
                assertEquals(ScheduleStatus.SCHEDULED.getValue(), schedule.getStatus());
                assertEquals(2005, schedule.getScheduled().getYear());
                assertEquals(regularSeasonScheduleData.get(i).toString(), schedule.getData());

                expectedSequence++;
            }
        }

        @Test
        void shouldScheduleRegularSeasonGamesOnDifferentDays() {
            final List<ScheduleData> regularSeasonScheduleData = generateFakeRegularSeasonData();
            final ScheduleDataGenerator generator = mock(ScheduleDataGenerator.class);

            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.PRESEASON)).thenReturn(mock(ScheduleDataGenerator.class));
            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.REGULAR_SEASON)).thenReturn(generator);
            when(generator.generate()).thenReturn(regularSeasonScheduleData);

            scheduleService.generateSchedule("2005");

            verify(scheduleRepository).saveAll(captor.capture());

            final List<Schedule> regularSeason = captor.getValue().stream()
                    .filter(schedule -> schedule.getType().equals(ScheduleType.REGULAR_SEASON.getValue()))
                    .collect(Collectors.toList());

            assertEquals(regularSeasonScheduleData.size(), regularSeason.size());

            assertEquals(regularSeasonScheduleData.size(), regularSeason.stream().map(Schedule::getScheduled).distinct().count());
        }

        @Test
        void shouldNotScheduleRegularSeasonGamesOnTheWeekend() {
            final List<ScheduleData> regularSeasonScheduleData = generateFakeRegularSeasonData();
            final ScheduleDataGenerator generator = mock(ScheduleDataGenerator.class);

            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.PRESEASON)).thenReturn(mock(ScheduleDataGenerator.class));
            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.REGULAR_SEASON)).thenReturn(generator);
            when(generator.generate()).thenReturn(regularSeasonScheduleData);

            scheduleService.generateSchedule("2005");

            verify(scheduleRepository).saveAll(captor.capture());

            final List<Schedule> regularSeason = captor.getValue().stream()
                    .filter(schedule -> schedule.getType().equals(ScheduleType.REGULAR_SEASON.getValue()))
                    .collect(Collectors.toList());

            assertEquals(regularSeasonScheduleData.size(), regularSeason.size());

            for (final Schedule schedule : regularSeason) {
                assertNotEquals(DayOfWeek.SATURDAY, schedule.getScheduled().getDayOfWeek());
                assertNotEquals(DayOfWeek.SUNDAY, schedule.getScheduled().getDayOfWeek());
            }
        }

        @Test
        void shouldScheduleRegularSeasonGamesOnConsecutiveWeekdays() {
            final List<ScheduleData> regularSeasonScheduleData = generateFakeRegularSeasonData();
            final ScheduleDataGenerator generator = mock(ScheduleDataGenerator.class);

            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.PRESEASON)).thenReturn(mock(ScheduleDataGenerator.class));
            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.REGULAR_SEASON)).thenReturn(generator);
            when(generator.generate()).thenReturn(regularSeasonScheduleData);

            scheduleService.generateSchedule("2005");

            verify(scheduleRepository).saveAll(captor.capture());

            final List<Schedule> regularSeason = captor.getValue().stream()
                    .filter(schedule -> schedule.getType().equals(ScheduleType.REGULAR_SEASON.getValue()))
                    .collect(Collectors.toList());

            assertEquals(regularSeasonScheduleData.size(), regularSeason.size());

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
        void shouldSaveAEndOfRegularSeasonScheduleEntryForTheDayAfterRegularSeasonGames() {
            final ScheduleDataGenerator generator = mock(ScheduleDataGenerator.class);

            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.PRESEASON)).thenReturn(mock(ScheduleDataGenerator.class));
            when(scheduleDataGeneratorManager.getGeneratorFor(ScheduleType.REGULAR_SEASON)).thenReturn(generator);
            when(generator.generate()).thenReturn(generateFakeRegularSeasonData());

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
        void shouldSaveAnAwardsScheduleEntryForTheMondayAfterEndOfRegularSeason() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldSaveAPostseasonScheduleEntryForTheFridayAfterAwards() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldSaveConfiguredNumberOfDivisionPlayoffScheduleEntriesStartingSundayAfterPostseason() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldSaveConfiguredNumberOfDivisionChampionshipScheduleEntriesStartingSundayAfterDivisionPlayoffs() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldSaveConfiguredNumberOfConferenceChampionshipScheduleEntriesStartingSundayAfterDivisionChampionships() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldSaveANATCChampionshipScheduleEntryForTheSundayAfterDivisionChampionships() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldSaveAEndOfPostseasonScheduleEntryForTheDayAfterNATCChampionship() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldSaveAnAllStarsScheduleEntryForTheDayAfterEndOfPostseason() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldSaveAnAllStarDayOneScheduleEntryForTheSaturdayAfterAllStars() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldSaveAnAllStarDayTwoScheduleEntryForTheDayAfterAllStarDayOne() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldSaveAnEndOfAllStarGamesScheduleEntryForTheDayAfterAllStarDayTwo() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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
        void shouldSaveAnEndOfSeasonScheduleEntryForFirstMondayInNovemberFirstOfGivenYear() {
            when(scheduleDataGeneratorManager.getGeneratorFor(any())).thenReturn(mock(ScheduleDataGenerator.class));

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

        private List<ScheduleData> generateFakePreseasonData() {
            final List<ScheduleData> scheduleDataList = new ArrayList<>();

            for (int i = 0; i < 10; ++i) {
                final ScheduleData scheduleData = new ScheduleData();

                for (int j = 0; j < 20; ++j) {
                    final ScheduleData.Match match = new ScheduleData.Match(j + 1, j + 21);

                    scheduleData.getMatches().add(match);
                }

                scheduleDataList.add(scheduleData);
            }

            return scheduleDataList;
        }

        private List<ScheduleData> generateFakeRegularSeasonData() {
            final List<ScheduleData> scheduleDataList = new ArrayList<>();

            for (int i = 0; i < 100; ++i) {
                final ScheduleData scheduleData = new ScheduleData();

                for (int j = 0; j < 20; ++j) {
                    final ScheduleData.Match match = new ScheduleData.Match(j + 1, j + 21);

                    scheduleData.getMatches().add(match);
                }

                scheduleDataList.add(scheduleData);
            }

            return scheduleDataList;
        }
    }
}