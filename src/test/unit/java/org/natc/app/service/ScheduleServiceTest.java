package org.natc.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.repository.ScheduleRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

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
}