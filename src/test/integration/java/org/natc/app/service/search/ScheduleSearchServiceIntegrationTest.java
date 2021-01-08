package org.natc.app.service.search;


import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.entity.request.ScheduleSearchRequest;
import org.natc.app.entity.response.ScheduleResponse;
import org.natc.app.repository.ScheduleRepository;
import org.natc.app.service.NATCServiceIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ScheduleSearchServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleSearchService scheduleSearchService;

    @Nested
    class FetchAll {

        @Test
        void shouldReturnAScheduleFromTheDatabaseMappedToAResponse() {
            final Schedule schedule = Schedule.builder()
                    .year("2002")
                    .sequence(12)
                    .type(ScheduleType.CONFERENCE_CHAMPIONSHIP.getValue())
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .build();

            scheduleRepository.save(schedule);

            final List<ScheduleResponse> result = scheduleSearchService.fetchAll(new ScheduleSearchRequest());

            assertEquals(1, result.size());

            final ScheduleResponse response = result.get(0);

            assertEquals(schedule.getYear(), response.getYear());
            assertEquals(schedule.getSequence(), response.getSequence());
            assertEquals(ScheduleType.CONFERENCE_CHAMPIONSHIP, response.getType());
            assertEquals(ScheduleStatus.SCHEDULED, response.getStatus());
        }

        @Test
        void shouldMapAllScheduleFieldsToTheScheduleResponse() {
            final Schedule schedule = Schedule.builder()
                    .year("2002")
                    .sequence(12)
                    .type(ScheduleType.CONFERENCE_CHAMPIONSHIP.getValue())
                    .data("schedule data string")
                    .scheduled(LocalDate.now())
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .build();

            scheduleRepository.save(schedule);

            final List<ScheduleResponse> result = scheduleSearchService.fetchAll(new ScheduleSearchRequest());

            assertEquals(1, result.size());

            final ScheduleResponse response = result.get(0);

            assertNotNull(response.getYear());
            assertNotNull(response.getSequence());
            assertNotNull(response.getType());
            assertNotNull(response.getData());
            assertNotNull(response.getScheduled());
            assertNotNull(response.getStatus());
        }

        @Test
        void shouldReturnNoEntriesWhenSearchingGivenNoDataInTheDatabase() {
            final List<ScheduleResponse> result = scheduleSearchService.fetchAll(new ScheduleSearchRequest());

            assertEquals(0, result.size());
        }

        @Nested
        class WithOneSearchParameter {

            @Test
            void shouldReturnAllEntriesForYearWhenSearchingByYear() {
                final List<Schedule> scheduleList = Arrays.asList(
                        Schedule.builder().year("2000").sequence(1).build(),
                        Schedule.builder().year("2000").sequence(2).build(),
                        Schedule.builder().year("2000").sequence(3).build()
                );

                scheduleRepository.saveAll(scheduleList);

                final ScheduleSearchRequest request = ScheduleSearchRequest.builder()
                        .year("2000")
                        .build();

                final List<ScheduleResponse> result = scheduleSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForYearWhenSearchingByYear() {
                final List<Schedule> scheduleList = Arrays.asList(
                        Schedule.builder().year("2000").sequence(1).build(),
                        Schedule.builder().year("2001").sequence(1).build(),
                        Schedule.builder().year("2000").sequence(2).build(),
                        Schedule.builder().year("2002").sequence(2).build(),
                        Schedule.builder().year("2000").sequence(3).build()
                );

                scheduleRepository.saveAll(scheduleList);

                final ScheduleSearchRequest request = ScheduleSearchRequest.builder()
                        .year("2000")
                        .build();

                final List<ScheduleResponse> result = scheduleSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t -> t.getYear().equals("2000")).count());
            }

            @Test
            void shouldReturnAllEntriesForSequenceWhenSearchingBySequence() {
                final List<Schedule> scheduleList = Arrays.asList(
                        Schedule.builder().year("2000").sequence(1).build(),
                        Schedule.builder().year("2001").sequence(1).build(),
                        Schedule.builder().year("2002").sequence(1).build()
                );

                scheduleRepository.saveAll(scheduleList);

                final ScheduleSearchRequest request = ScheduleSearchRequest.builder()
                        .sequence(1)
                        .build();

                final List<ScheduleResponse> result = scheduleSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForSequenceWhenSearchingBySequence() {
                final List<Schedule> scheduleList = Arrays.asList(
                        Schedule.builder().year("2000").sequence(1).build(),
                        Schedule.builder().year("2000").sequence(2).build(),
                        Schedule.builder().year("2001").sequence(1).build(),
                        Schedule.builder().year("2001").sequence(3).build(),
                        Schedule.builder().year("2002").sequence(1).build()
                );

                scheduleRepository.saveAll(scheduleList);

                final ScheduleSearchRequest request = ScheduleSearchRequest.builder()
                        .sequence(1)
                        .build();

                final List<ScheduleResponse> result = scheduleSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t -> t.getSequence().equals(1)).count());
            }
        }

        @Nested
        class WithTwoSearchParameters {

            @Test
            void shouldReturnAllEntriesForYearAndSequenceWhenSearchingByYearAndSequence() {
                final List<Schedule> scheduleList = Collections.singletonList(
                        // Year & Sequence are the key fields, so only one record is possible
                        Schedule.builder().year("2000").sequence(1).build()
                );

                scheduleRepository.saveAll(scheduleList);

                final ScheduleSearchRequest request = ScheduleSearchRequest.builder()
                        .year("2000")
                        .sequence(1)
                        .build();

                final List<ScheduleResponse> result = scheduleSearchService.fetchAll(request);

                assertEquals(1, result.size());
            }

            @Test
            void shouldReturnOnlyEntriesForYearAndSequenceWhenSearchingByYearAndSequence() {
                final List<Schedule> scheduleList = Arrays.asList(
                        Schedule.builder().year("2000").sequence(1).build(),
                        Schedule.builder().year("2001").sequence(1).build(),
                        Schedule.builder().year("2000").sequence(2).build()
                );

                scheduleRepository.saveAll(scheduleList);

                final ScheduleSearchRequest request = ScheduleSearchRequest.builder()
                        .year("2000")
                        .sequence(1)
                        .build();

                final List<ScheduleResponse> result = scheduleSearchService.fetchAll(request);

                assertEquals(1, result.size());
                assertEquals(1, result.stream().filter(t ->
                        t.getYear().equals("2000") && t.getSequence().equals(1)
                ).count());
            }
        }
    }
}