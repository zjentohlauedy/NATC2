package org.natc.app

import org.natc.app.entity.domain.Schedule
import org.natc.app.entity.domain.ScheduleStatus
import org.natc.app.entity.domain.ScheduleType
import org.natc.app.repository.ScheduleRepository
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDate

class ScheduleSearchTest extends NATCFunctionalTest {

    @Autowired
    ScheduleRepository scheduleRepository

    def setup() {
        scheduleRepository.deleteAll()
    }

    def 'schedule search endpoint returns schedule data'() {
        given: 'a schedule exists in the database'
        def schedule = Schedule.builder().year('2018').sequence(4).build();
        scheduleRepository.save(schedule)

        when: 'a request is sent to the schedule search endpoint'
        def response = restClient.get(path: '/api/schedules/search', contentType: 'application/json')

        then: 'the response should contain the schedule'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resource == null
            data.errors == null
            data.resources.size == 1
            data.resources[0].year == '2018'
            data.resources[0].sequence == 4
        }
    }

    def 'schedule search endpoint provides all schedule fields'() {
        given: 'a schedule exists in the database'
        def scheduledDate = LocalDate.now()
        def schedule = Schedule.builder()
                .year('2018')
                .sequence(4)
                .type(ScheduleType.REGULAR_SEASON.getValue())
                .data('schedule data')
                .scheduled(scheduledDate)
                .status(ScheduleStatus.IN_PROGRESS.getValue())
                .build();

        scheduleRepository.save(schedule)

        when: 'a request is sent to the schedule search endpoint'
        def response = restClient.get(path: '/api/schedules/search', contentType: 'application/json')

        then: 'the response should contain all of the schedule fields'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            with(data.resources[0]) {
                year == '2018'
                sequence == 4
                type == 'REGULAR_SEASON'
                data == 'schedule data'
                scheduled == scheduledDate.toString()
                status == 'IN_PROGRESS'
            }
        }
    }

    def 'schedule search endpoint returns all matching schedule'() {
        given: 'three schedules exist in the database'
        def schedules = [
                Schedule.builder().year('2000').sequence(1).build(),
                Schedule.builder().year('2001').sequence(2).build(),
                Schedule.builder().year('2002').sequence(3).build()
        ]

        scheduleRepository.saveAll(schedules)

        when: 'a request is sent to the schedule search endpoint'
        def response = restClient.get(path: '/api/schedules/search', contentType: 'application/json')

        then: 'the response should contain all three schedules'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 3

            data.resources.collect { it.sequence }.containsAll([1,2,3])
        }
    }

    def 'schedule search endpoint returns an empty list when no matching schedules are found'() {
        given: 'no schedules exist in the database'
        when: 'a request is sent to the schedule search endpoint'
        def response = restClient.get(path: '/api/schedules/search', contentType: 'application/json')

        then: 'the response should contain an empty resources list'
        with(response) {
            status == 200
            data.status == 'NOT_FOUND'
            data.resources.size == 0
        }
    }

    def 'schedule search endpoint accepts the year query parameter'() {
        given: 'three schedules exist in the database'
        def schedules = [
                Schedule.builder().year('2000').sequence(1).build(),
                Schedule.builder().year('2001').sequence(2).build(),
                Schedule.builder().year('2002').sequence(3).build()
        ]

        scheduleRepository.saveAll(schedules)

        when: 'a request is sent to the schedule search endpoint for year 2000'
        def response = restClient.get(path: '/api/schedules/search', contentType: 'application/json', query: ['year': '2000'])

        then: 'only the matching year should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].year == '2000'
        }
    }

    def 'schedule search endpoint accepts the sequence query parameter'() {
        given: 'three schedules exist in the database'
        def schedules = [
                Schedule.builder().year('2000').sequence(1).build(),
                Schedule.builder().year('2001').sequence(2).build(),
                Schedule.builder().year('2002').sequence(3).build()
        ]

        scheduleRepository.saveAll(schedules)

        when: 'a request is sent to the schedule search endpoint for sequence 2'
        def response = restClient.get(path: '/api/schedules/search', contentType: 'application/json', query: ['sequence': 2])

        then: 'only the matching sequence should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].sequence == 2
        }
    }
}
