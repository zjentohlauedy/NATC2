package org.natc.app.processor;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.ScheduleRepository;
import org.natc.app.service.NATCServiceIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeginningOfSeasonScheduleProcessorIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private BeginningOfSeasonScheduleProcessor processor;

    @Nested
    class Process {

        @Test
        void shouldUpdateTheGivenScheduleStatusToCompleted() throws NATCException {
            final Schedule schedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.BEGINNING_OF_SEASON.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            scheduleRepository.save(schedule);

            processor.process(schedule);

            final List<Schedule> scheduleList = scheduleRepository.findAll();

            assertEquals(1, scheduleList.size());
            assertEquals(ScheduleStatus.COMPLETED.getValue(), scheduleList.getFirst().getStatus());
        }
    }
}