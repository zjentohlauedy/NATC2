package org.natc.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.entity.request.ScheduleSearchRequest;
import org.natc.app.entity.response.ScheduleResponse;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleSearchServiceTest {

    @Captor
    private ArgumentCaptor<Example<Schedule>> captor;

    @Mock
    private JpaRepository scheduleRepository;

    @InjectMocks
    private ScheduleSearchService scheduleSearchService;

    @Test
    public void fetchAll_ShouldReturnAListOfScheduleResponses() {
        final List<ScheduleResponse> result = scheduleSearchService.fetchAll(new ScheduleSearchRequest());

        assertEquals(0, result.size());
    }

    @Test
    public void fetchAll_ShouldCallTheScheduleRepositoryWithAnExampleSchedule() {
        scheduleSearchService.fetchAll(new ScheduleSearchRequest());

        verify(scheduleRepository).findAll(ArgumentMatchers.<Example<Schedule>>any());
    }

    @Test
    public void fetchAll_ShouldCallScheduleRepositoryWithExampleScheduleBasedOnRequest() {
        final ScheduleSearchRequest request = ScheduleSearchRequest.builder().year("1999").sequence(5).build();

        scheduleSearchService.fetchAll(request);

        verify(scheduleRepository).findAll(captor.capture());

        final Schedule schedule = captor.getValue().getProbe();

        assertEquals(request.getYear(), schedule.getYear());
        assertEquals(request.getSequence(), schedule.getSequence());
    }

    @Test
    public void fetchAll_ShouldReturnScheduleResponsesMappedFromTheSchedulesReturnedByRepository() {
        final Schedule schedule = generateSchedule(ScheduleType.REGULAR_SEASON, ScheduleStatus.IN_PROGRESS);

        when(scheduleRepository.findAll(ArgumentMatchers.<Example<Schedule>>any()))
                .thenReturn(Collections.singletonList(schedule));

        final List<ScheduleResponse> result = scheduleSearchService.fetchAll(new ScheduleSearchRequest());

        assertEquals(1, result.size());

        final ScheduleResponse response = result.get(0);

        assertEquals(schedule.getYear(), response.getYear());
        assertEquals(schedule.getSequence(), response.getSequence());
        assertEquals(ScheduleType.REGULAR_SEASON, response.getType());
        assertEquals(schedule.getData(), response.getData());
        assertEquals(schedule.getScheduled(), response.getScheduled());
        assertEquals(ScheduleStatus.IN_PROGRESS, response.getStatus());
    }

    private Schedule generateSchedule(final ScheduleType type, final ScheduleStatus status) {
        return Schedule.builder()
                .year("2004")
                .sequence(11)
                .type(type.getValue())
                .data("this is the data")
                .scheduled(LocalDate.now())
                .status(status.getValue())
                .build();
    }
}