package org.natc.app.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.exception.NATCException;
import org.natc.app.exception.ScheduleProcessingException;
import org.natc.app.processor.ScheduleProcessor;
import org.natc.app.service.LeagueService;
import org.natc.app.service.ScheduleService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SeasonManagerTest {

    @Mock
    private LeagueService leagueService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleProcessorManager scheduleProcessorManager;

    @Mock
    private LeagueConfiguration leagueConfiguration;
    
    @InjectMocks
    private SeasonManager seasonManager;

    @Nested
    class ProcessScheduledEvent {

        @BeforeEach
        public void setup() {
            final Schedule validTodaySchedule = Schedule.builder()
                    .year("2000")
                    .sequence(1)
                    .type(ScheduleType.REGULAR_SEASON.getValue())
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .scheduled(LocalDate.now())
                    .build();
            final ScheduleProcessor scheduleProcessor = mock(ScheduleProcessor.class);

            when(scheduleProcessorManager.getProcessorFor(any())).thenReturn(scheduleProcessor);
            when(scheduleService.getNextScheduleEntry(any())).thenReturn(validTodaySchedule);
        }

        @Test
        public void shouldCallScheduleServiceToGetCurrentScheduleEntry() throws NATCException {
            seasonManager.processScheduledEvent();

            verify(scheduleService).getCurrentScheduleEntry();
        }

        @Test
        public void shouldCallScheduleServiceToGetLastScheduleEntry() throws NATCException {
            seasonManager.processScheduledEvent();

            verify(scheduleService).getLastScheduleEntry();
        }

        @Test
        public void shouldNotGetLastScheduleEntryIfCurrentScheduleEntryFound() throws NATCException {
            final Schedule currentSchedule = Schedule.builder().year("2000").sequence(1).status(ScheduleStatus.IN_PROGRESS.getValue()).build();

            when(scheduleService.getCurrentScheduleEntry()).thenReturn(currentSchedule);

            seasonManager.processScheduledEvent();

            verify(scheduleService, never()).getLastScheduleEntry();
        }

        @Test
        public void shouldCallLeagueServiceToGenerateLeagueIfLastScheduleEntryIsNotFound() throws NATCException {
            when(scheduleService.getLastScheduleEntry()).thenReturn(null);

            seasonManager.processScheduledEvent();

            verify(leagueService).generateNewLeague();
        }

        @Test
        public void shouldCallScheduleServiceToGenerateScheduleForFirstSeasonIfLastScheduleEntryIsNotFound() throws NATCException {
            final String expectedYear = "1999";

            when(scheduleService.getLastScheduleEntry()).thenReturn(null);
            when(leagueConfiguration.getFirstSeason()).thenReturn(expectedYear);

            seasonManager.processScheduledEvent();

            verify(scheduleService).generateSchedule(expectedYear);
        }

        @Test
        public void shouldGenerateNewLeagueBeforeGeneratingNewSchedule() throws NATCException {
            when(scheduleService.getLastScheduleEntry()).thenReturn(null);

            final InOrder inOrder = inOrder(leagueService, scheduleService);

            seasonManager.processScheduledEvent();

            inOrder.verify(leagueService).generateNewLeague();
            inOrder.verify(scheduleService).generateSchedule(any());
        }

        @Test
        public void shouldNotGenerateNewLeagueIfCurrentScheduleEntryIsFound() throws NATCException {
            when(scheduleService.getCurrentScheduleEntry()).thenReturn(new Schedule());

            seasonManager.processScheduledEvent();

            verify(leagueService, never()).generateNewLeague();
        }

        @Test
        public void shouldNotGenerateNewLeagueIfLastScheduleEntryIsFound() throws NATCException {
            final Schedule lastSchedule = Schedule.builder().year("2000").type(ScheduleType.REGULAR_SEASON.getValue()).build();

            when(scheduleService.getLastScheduleEntry()).thenReturn(lastSchedule);

            seasonManager.processScheduledEvent();

            verify(leagueService, never()).generateNewLeague();
        }

        @Test
        public void shouldNotGenerateNewLeagueIfLastScheduleEntryIsEndOfSeason() throws NATCException {
            final Schedule lastSchedule = Schedule.builder().year("2000").type(ScheduleType.END_OF_SEASON.getValue()).build();

            when(scheduleService.getLastScheduleEntry()).thenReturn(lastSchedule);

            seasonManager.processScheduledEvent();

            verify(leagueService, never()).generateNewLeague();
        }

        @Test
        public void shouldUpdateLeagueForNewSeasonWhenLastScheduleEntryIsEndOfSeason() throws NATCException {
            final Schedule lastSchedule = Schedule.builder().year("2000").type(ScheduleType.END_OF_SEASON.getValue()).build();

            when(scheduleService.getLastScheduleEntry()).thenReturn(lastSchedule);

            seasonManager.processScheduledEvent();

            verify(leagueService).updateLeagueForNewSeason("2000", "2001");
        }

        @Test
        public void shouldCallScheduleServiceToGenerateScheduleForNextYearWhenLastScheduleEntryIsEndOfSeason() throws NATCException {
            final Schedule lastSchedule = Schedule.builder().year("2000").type(ScheduleType.END_OF_SEASON.getValue()).build();

            when(scheduleService.getLastScheduleEntry()).thenReturn(lastSchedule);

            seasonManager.processScheduledEvent();

            verify(scheduleService).generateSchedule("2001");
        }

        @Test
        public void shouldUpdateLeagueBeforeGeneratingNewSchedule() throws NATCException {
            final Schedule lastSchedule = Schedule.builder().year("2000").type(ScheduleType.END_OF_SEASON.getValue()).build();

            when(scheduleService.getLastScheduleEntry()).thenReturn(lastSchedule);

            final InOrder inOrder = inOrder(leagueService, scheduleService);

            seasonManager.processScheduledEvent();

            inOrder.verify(leagueService).updateLeagueForNewSeason(any(), any());
            inOrder.verify(scheduleService).generateSchedule(any());
        }

        @Test
        public void shouldNotUpdateLeagueIfCurrentScheduleEntryIsFound() throws NATCException {
            when(scheduleService.getCurrentScheduleEntry()).thenReturn(new Schedule());

            seasonManager.processScheduledEvent();

            verify(leagueService, never()).updateLeagueForNewSeason(any(), any());
        }

        @Test
        public void shouldNotUpdateLeagueIfLastScheduleEntryIsNotFound() throws NATCException {
            when(scheduleService.getLastScheduleEntry()).thenReturn(null);

            seasonManager.processScheduledEvent();

            verify(leagueService, never()).updateLeagueForNewSeason(any(), any());
        }

        @Test
        public void shouldNotUpdateLeagueIfLastScheduleEntryIsNotEndOfSeason() throws NATCException {
            final Schedule lastSchedule = Schedule.builder().year("2000").type(ScheduleType.REGULAR_SEASON.getValue()).build();

            when(scheduleService.getLastScheduleEntry()).thenReturn(lastSchedule);

            seasonManager.processScheduledEvent();

            verify(leagueService, never()).updateLeagueForNewSeason(any(), any());
        }

        @Test
        public void shouldNotGenerateScheduleIfCurrentScheduleEntryIsFound() throws NATCException {
            when(scheduleService.getCurrentScheduleEntry()).thenReturn(new Schedule());

            seasonManager.processScheduledEvent();

            verify(scheduleService, never()).generateSchedule(any());
        }

        @Test
        public void shouldNotGenerateScheduleIfLastScheduleEntryIsFoundAndIsNotEndOfSeason() throws NATCException {
            final Schedule lastSchedule = Schedule.builder().year("2000").type(ScheduleType.REGULAR_SEASON.getValue()).build();

            when(scheduleService.getLastScheduleEntry()).thenReturn(lastSchedule);

            seasonManager.processScheduledEvent();

            verify(scheduleService, never()).generateSchedule(any());
        }

        @Test
        public void shouldCallScheduleServiceToGetNextScheduleEntry() throws NATCException {
            seasonManager.processScheduledEvent();

            verify(scheduleService).getNextScheduleEntry(any());
        }

        @Test
        public void shouldUseLastScheduleEntryToGetNextScheduleEntry() throws NATCException {
            final Schedule lastSchedule = Schedule.builder()
                    .year("2000")
                    .sequence(1)
                    .status(ScheduleStatus.COMPLETED.getValue())
                    .type(ScheduleType.REGULAR_SEASON.getValue())
                    .build();

            when(scheduleService.getLastScheduleEntry()).thenReturn(lastSchedule);

            seasonManager.processScheduledEvent();

            verify(scheduleService).getNextScheduleEntry(lastSchedule);
        }

        @Test
        public void shouldUseLastScheduleEntryToGetNextScheduleEntryEvenIfNull() throws NATCException {
            when(scheduleService.getLastScheduleEntry()).thenReturn(null);

            seasonManager.processScheduledEvent();

            verify(scheduleService).getNextScheduleEntry(null);
        }

        @Test
        public void shouldNotGetNextScheduleEntryIfCurrentScheduleEntryFound() throws NATCException {
            final Schedule currentSchedule = Schedule.builder().year("2000").sequence(1).status(ScheduleStatus.IN_PROGRESS.getValue()).build();

            when(scheduleService.getCurrentScheduleEntry()).thenReturn(currentSchedule);

            seasonManager.processScheduledEvent();

            verify(scheduleService, never()).getNextScheduleEntry(any());
        }

        @Test
        public void shouldCallScheduleServiceToUpdateScheduleStatusToInProgress() throws NATCException {
            final Schedule nextSchedule = Schedule.builder()
                    .year("2000")
                    .sequence(1)
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .scheduled(LocalDate.now())
                    .build();
            final ArgumentCaptor<Schedule> captor = ArgumentCaptor.forClass(Schedule.class);

            reset(scheduleService);
            when(scheduleService.getNextScheduleEntry(any())).thenReturn(nextSchedule);

            seasonManager.processScheduledEvent();

            verify(scheduleService).updateScheduleEntry(captor.capture());

            final Schedule updatedSchedule = captor.getValue();

            assertEquals(nextSchedule.getYear(), updatedSchedule.getYear());
            assertEquals(nextSchedule.getSequence(), updatedSchedule.getSequence());
            assertEquals(ScheduleStatus.IN_PROGRESS.getValue(), updatedSchedule.getStatus());
        }

        @Test
        public void shouldUpdateScheduleStatusToInProgressWhenScheduledDateIsInThePast() throws NATCException {
            final Schedule nextSchedule = Schedule.builder()
                    .year("2000")
                    .sequence(1)
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .scheduled(LocalDate.now().minusDays(1))
                    .build();
            final ArgumentCaptor<Schedule> captor = ArgumentCaptor.forClass(Schedule.class);

            reset(scheduleService);
            when(scheduleService.getNextScheduleEntry(any())).thenReturn(nextSchedule);

            seasonManager.processScheduledEvent();

            verify(scheduleService).updateScheduleEntry(captor.capture());

            final Schedule updatedSchedule = captor.getValue();

            assertEquals(nextSchedule.getYear(), updatedSchedule.getYear());
            assertEquals(nextSchedule.getSequence(), updatedSchedule.getSequence());
            assertEquals(ScheduleStatus.IN_PROGRESS.getValue(), updatedSchedule.getStatus());
        }

        @Test
        public void shouldNotUpdateScheduleStatusToInProgressWhenScheduledDateIsInTheFuture() throws NATCException {
            final Schedule nextSchedule = Schedule.builder()
                    .year("2000")
                    .sequence(1)
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .scheduled(LocalDate.now().plusDays(1))
                    .build();

            reset(scheduleService);
            when(scheduleService.getNextScheduleEntry(any())).thenReturn(nextSchedule);

            seasonManager.processScheduledEvent();

            verify(scheduleService, never()).updateScheduleEntry(any());
        }

        @Test
        public void shouldThrowScheduleProcessingExceptionIfNextScheduleEntryNotFound() {
            reset(scheduleService);
            when(scheduleService.getNextScheduleEntry(any())).thenReturn(null);

            assertThrows(ScheduleProcessingException.class, () -> seasonManager.processScheduledEvent());
        }

        @Test
        public void shouldGetScheduleProcessorFromScheduleProcessorManager() throws NATCException {
            seasonManager.processScheduledEvent();

            verify(scheduleProcessorManager).getProcessorFor(any(ScheduleType.class));
        }

        @Test
        public void shouldNotGetScheduleProcessorIfCurrentScheduleEntryFound() throws NATCException {
            final Schedule currentSchedule = Schedule.builder().year("2000").sequence(1).status(ScheduleStatus.IN_PROGRESS.getValue()).build();

            when(scheduleService.getCurrentScheduleEntry()).thenReturn(currentSchedule);

            seasonManager.processScheduledEvent();

            verify(scheduleProcessorManager, never()).getProcessorFor(any());
        }

        @Test
        public void shouldNotgetScheduleProcessorWhenNextEntryScheduledDateIsInTheFuture() throws NATCException {
            final Schedule nextSchedule = Schedule.builder()
                    .year("2000")
                    .sequence(1)
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .scheduled(LocalDate.now().plusDays(1))
                    .build();

            reset(scheduleService);
            when(scheduleService.getNextScheduleEntry(any())).thenReturn(nextSchedule);

            seasonManager.processScheduledEvent();

            verify(scheduleProcessorManager, never()).getProcessorFor(any());
        }

        @Test
        public void shouldUseScheduleTypeFromNextScheduleEntryToGetScheduleProcessor() throws NATCException {
            final Schedule nextSchedule = Schedule.builder()
                    .year("2000")
                    .sequence(1)
                    .type(ScheduleType.REGULAR_SEASON.getValue())
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            reset(scheduleService);
            when(scheduleService.getNextScheduleEntry(any())).thenReturn(nextSchedule);

            seasonManager.processScheduledEvent();

            verify(scheduleProcessorManager).getProcessorFor(ScheduleType.REGULAR_SEASON);
        }

        @Test
        public void shouldUseScheduleProcessorToProcessNextScheduleEntry() throws NATCException {
            final Schedule nextSchedule = Schedule.builder()
                    .year("2000")
                    .sequence(1)
                    .type(ScheduleType.REGULAR_SEASON.getValue())
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .scheduled(LocalDate.now())
                    .build();
            final ScheduleProcessor scheduleProcessor = mock(ScheduleProcessor.class);

            reset(scheduleService);
            reset(scheduleProcessorManager);
            when(scheduleService.getNextScheduleEntry(any())).thenReturn(nextSchedule);
            when(scheduleProcessorManager.getProcessorFor(any())).thenReturn(scheduleProcessor);

            seasonManager.processScheduledEvent();

            verify(scheduleProcessor).process(nextSchedule);
        }
    }
}