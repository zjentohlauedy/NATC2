package org.natc.app.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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
    
    @InjectMocks
    private SeasonManager seasonManager;

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
    public void processScheduledEvent_ShouldCallScheduleServiceToGetCurrentScheduleEntry() throws NATCException {
        seasonManager.processScheduledEvent();

        verify(scheduleService).getCurrentScheduleEntry();
    }

    @Test
    public void processScheduledEvent_ShouldCallScheduleServiceToGetLastScheduleEntry() throws NATCException {
        seasonManager.processScheduledEvent();

        verify(scheduleService).getLastScheduleEntry();
    }

    @Test
    public void processScheduledEvent_ShouldNotGetLastScheduleEntryIfCurrentScheduleEntryFound() throws NATCException {
        final Schedule currentSchedule = Schedule.builder().year("2000").sequence(1).status(ScheduleStatus.IN_PROGRESS.getValue()).build();

        when(scheduleService.getCurrentScheduleEntry()).thenReturn(currentSchedule);

        seasonManager.processScheduledEvent();

        verify(scheduleService, never()).getLastScheduleEntry();
    }

    @Test
    public void processScheduledEvent_ShouldCallLeagueServiceToGenerateLeagueIfLastScheduleEntryIsNotFound() throws NATCException {
        when(scheduleService.getLastScheduleEntry()).thenReturn(null);

        seasonManager.processScheduledEvent();

        verify(leagueService).generateNewLeague();
    }

    @Test
    public void processScheduledEvent_ShouldCallScheduleServiceToGenerateScheduleForFirstYearIfLastScheduleEntryIsNotFound() throws NATCException {
        when(scheduleService.getLastScheduleEntry()).thenReturn(null);

        seasonManager.processScheduledEvent();

        verify(scheduleService).generateSchedule(Schedule.FIRST_YEAR);
    }

    @Test
    public void processScheduledEvent_ShouldGenerateNewLeagueBeforeGeneratingNewSchedule() throws NATCException {
        when(scheduleService.getLastScheduleEntry()).thenReturn(null);

        final InOrder inOrder = inOrder(leagueService, scheduleService);

        seasonManager.processScheduledEvent();

        inOrder.verify(leagueService).generateNewLeague();
        inOrder.verify(scheduleService).generateSchedule(any());
    }

    @Test
    public void processScheduledEvent_ShouldNotGenerateNewLeagueIfCurrentScheduleEntryIsFound() throws NATCException {
        when(scheduleService.getCurrentScheduleEntry()).thenReturn(new Schedule());

        seasonManager.processScheduledEvent();

        verify(leagueService, never()).generateNewLeague();
    }

    @Test
    public void processScheduledEvent_ShouldNotGenerateNewLeagueIfLastScheduleEntryIsFound() throws NATCException {
        final Schedule lastSchedule = Schedule.builder().year("2000").type(ScheduleType.REGULAR_SEASON.getValue()).build();

        when(scheduleService.getLastScheduleEntry()).thenReturn(lastSchedule);

        seasonManager.processScheduledEvent();

        verify(leagueService, never()).generateNewLeague();
    }

    @Test
    public void processScheduledEvent_ShouldNotGenerateNewLeagueIfLastScheduleEntryIsEndOfSeason() throws NATCException {
        final Schedule lastSchedule = Schedule.builder().year("2000").type(ScheduleType.END_OF_SEASON.getValue()).build();

        when(scheduleService.getLastScheduleEntry()).thenReturn(lastSchedule);

        seasonManager.processScheduledEvent();

        verify(leagueService, never()).generateNewLeague();
    }

    @Test
    public void processScheduledEvent_ShouldUpdateLeagueForNewSeasonWhenLastScheduleEntryIsEndOfSeason() throws NATCException {
        final Schedule lastSchedule = Schedule.builder().year("2000").type(ScheduleType.END_OF_SEASON.getValue()).build();

        when(scheduleService.getLastScheduleEntry()).thenReturn(lastSchedule);

        seasonManager.processScheduledEvent();

        verify(leagueService).updateLeagueForNewSeason();
    }

    @Test
    public void processScheduledEvent_ShouldCallScheduleServiceToGenerateScheduleForNextYearWhenLastScheduleEntryIsEndOfSeason() throws NATCException {
        final Schedule lastSchedule = Schedule.builder().year("2000").type(ScheduleType.END_OF_SEASON.getValue()).build();

        when(scheduleService.getLastScheduleEntry()).thenReturn(lastSchedule);

        seasonManager.processScheduledEvent();

        verify(scheduleService).generateSchedule("2001");
    }

    @Test
    public void processScheduledEvent_ShouldUpdateLeagueBeforeGeneratingNewSchedule() throws NATCException {
        final Schedule lastSchedule = Schedule.builder().year("2000").type(ScheduleType.END_OF_SEASON.getValue()).build();

        when(scheduleService.getLastScheduleEntry()).thenReturn(lastSchedule);

        final InOrder inOrder = inOrder(leagueService, scheduleService);

        seasonManager.processScheduledEvent();

        inOrder.verify(leagueService).updateLeagueForNewSeason();
        inOrder.verify(scheduleService).generateSchedule(any());
    }

    @Test
    public void processScheduledEvent_ShouldNotUpdateLeagueIfCurrentScheduleEntryIsFound() throws NATCException {
        when(scheduleService.getCurrentScheduleEntry()).thenReturn(new Schedule());

        seasonManager.processScheduledEvent();

        verify(leagueService, never()).updateLeagueForNewSeason();
    }

    @Test
    public void processScheduledEvent_ShouldNotUpdateLeagueIfLastScheduleEntryIsNotFound() throws NATCException {
        when(scheduleService.getLastScheduleEntry()).thenReturn(null);

        seasonManager.processScheduledEvent();

        verify(leagueService, never()).updateLeagueForNewSeason();
    }

    @Test
    public void processScheduledEvent_ShouldNotUpdateLeagueIfLastScheduleEntryIsNotEndOfSeason() throws NATCException {
        final Schedule lastSchedule = Schedule.builder().year("2000").type(ScheduleType.REGULAR_SEASON.getValue()).build();

        when(scheduleService.getLastScheduleEntry()).thenReturn(lastSchedule);

        seasonManager.processScheduledEvent();

        verify(leagueService, never()).updateLeagueForNewSeason();
    }

    @Test
    public void processScheduledEvent_ShouldNotGenerateScheduleIfCurrentScheduleEntryIsFound() throws NATCException {
        when(scheduleService.getCurrentScheduleEntry()).thenReturn(new Schedule());

        seasonManager.processScheduledEvent();

        verify(scheduleService, never()).generateSchedule(any());
    }

    @Test
    public void processScheduledEvent_ShouldNotGenerateScheduleIfLastScheduleEntryIsFoundAndIsNotEndOfSeason() throws NATCException {
        final Schedule lastSchedule = Schedule.builder().year("2000").type(ScheduleType.REGULAR_SEASON.getValue()).build();

        when(scheduleService.getLastScheduleEntry()).thenReturn(lastSchedule);

        seasonManager.processScheduledEvent();

        verify(scheduleService, never()).generateSchedule(any());
    }

    @Test
    public void processScheduledEvent_ShouldCallScheduleServiceToGetNextScheduleEntry() throws NATCException {
        seasonManager.processScheduledEvent();

        verify(scheduleService).getNextScheduleEntry(any());
    }

    @Test
    public void processScheduledEvent_ShouldUseLastScheduleEntryToGetNextScheduleEntry() throws NATCException {
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
    public void processScheduledEvent_ShouldUseLastScheduleEntryToGetNextScheduleEntryEvenIfNull() throws NATCException {
        when(scheduleService.getLastScheduleEntry()).thenReturn(null);

        seasonManager.processScheduledEvent();

        verify(scheduleService).getNextScheduleEntry(null);
    }

    @Test
    public void processScheduledEvent_ShouldNotGetNextScheduleEntryIfCurrentScheduleEntryFound() throws NATCException {
        final Schedule currentSchedule = Schedule.builder().year("2000").sequence(1).status(ScheduleStatus.IN_PROGRESS.getValue()).build();

        when(scheduleService.getCurrentScheduleEntry()).thenReturn(currentSchedule);

        seasonManager.processScheduledEvent();

        verify(scheduleService, never()).getNextScheduleEntry(any());
    }

    @Test
    public void processScheduledEvent_ShouldCallScheduleServiceToUpdateScheduleStatusToInProgress() throws NATCException {
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
    public void processScheduledEvent_ShouldUpdateScheduleStatusToInProgressWhenScheduledDateIsInThePast() throws NATCException {
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
    public void processScheduledEvent_ShouldNotUpdateScheduleStatusToInProgressWhenScheduledDateIsInTheFuture() throws NATCException {
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
    public void processScheduledEvent_ShouldThrowScheduleProcessingExceptionIfNextScheduleEntryNotFound() {
        reset(scheduleService);
        when(scheduleService.getNextScheduleEntry(any())).thenReturn(null);

        assertThrows(ScheduleProcessingException.class, () -> seasonManager.processScheduledEvent());
    }

    @Test
    public void processScheduledEvent_ShouldGetScheduleProcessorFromScheduleProcessorManager() throws NATCException {
        seasonManager.processScheduledEvent();

        verify(scheduleProcessorManager).getProcessorFor(any(ScheduleType.class));
    }

    @Test
    public void processScheduledEvent_ShouldNotGetScheduleProcessorIfCurrentScheduleEntryFound() throws NATCException {
        final Schedule currentSchedule = Schedule.builder().year("2000").sequence(1).status(ScheduleStatus.IN_PROGRESS.getValue()).build();

        when(scheduleService.getCurrentScheduleEntry()).thenReturn(currentSchedule);

        seasonManager.processScheduledEvent();

        verify(scheduleProcessorManager, never()).getProcessorFor(any());
    }

    @Test
    public void processScheduledEvent_ShouldNotgetScheduleProcessorWhenNextEntryScheduledDateIsInTheFuture() throws NATCException {
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
    public void processScheduledEvent_ShouldUseScheduleTypeFromNextScheduleEntryToGetScheduleProcessor() throws NATCException {
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
    public void processScheduledEvent_ShouldUseScheduleProcessorToProcessNextScheduleEntry() throws NATCException {
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