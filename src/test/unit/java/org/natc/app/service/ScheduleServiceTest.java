package org.natc.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.repository.ScheduleRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

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
}