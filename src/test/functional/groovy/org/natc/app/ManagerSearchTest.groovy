package org.natc.app

import org.natc.app.entity.domain.Manager
import org.natc.app.entity.domain.ManagerAward
import org.natc.app.entity.domain.ManagerStyle
import org.natc.app.repository.ManagerRepository
import org.springframework.beans.factory.annotation.Autowired

class ManagerSearchTest extends NATCFunctionalTest {

    @Autowired
    ManagerRepository managerRepository

    def setup() {
        managerRepository.deleteAll()
    }

    def 'manager search endpoint returns manager data'() {
        given: 'a manager exists in the database'
        def manager = Manager.builder().managerId(1).year('1999').build()
        managerRepository.save(manager)

        when: 'a request is sent to the manager search endpoint'
        def response = restClient.get(path: '/api/managers/search', contentType: 'application/json')

        then: 'the response should contain the manager'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resource == null
            data.errors == null
            data.resources.size == 1
            data.resources[0].managerId == 1
            data.resources[0].year == '1999'
        }
    }

    def 'manager search endpoint provides all manager fields'() {
        given: 'a manager exists in the database'
        def manager = Manager.builder()
                .managerId(123)
                .teamId(321)
                .playerId(555)
                .year('1991')
                .firstName('John')
                .lastName('Doe')
                .age(49)
                .offense(0.111)
                .defense(0.222)
                .intangible(0.333)
                .penalties(0.444)
                .vitality(0.555)
                .style(ManagerStyle.BALANCED.getValue())
                .newHire(1)
                .released(0)
                .retired(0)
                .formerTeamId(111)
                .allstarTeamId(222)
                .award(ManagerAward.NONE.getValue())
                .seasons(12)
                .score(33)
                .totalSeasons(18)
                .totalScore(77)
                .build();

        managerRepository.save(manager)

        when: 'a request is sent to the manager search endpoint'
        def response = restClient.get(path: '/api/managers/search', contentType: 'application/json')

        then: 'the response should contain all of the manager fields'
        with(response) {
            status == 200
            data.status == "SUCCESS"
            data.resources.size == 1
            with(data.resources[0]) {
                managerId == 123
                teamId == 321
                playerId == 555
                year == '1991'
                firstName == 'John'
                lastName == 'Doe'
                age == 49
                offense == 0.111
                defense == 0.222
                intangible == 0.333
                penalties == 0.444
                vitality == 0.555
                style == 'BALANCED'
                newHire == true
                released == false
                retired == false
                formerTeamId == 111
                allstarTeamId == 222
                award == 'NONE'
                seasons == 12
                score == 33
                totalSeasons == 18
                totalScore == 77
            }
        }
    }

    def 'manager search endpoint returns all matching managers'() {
        given: 'three managers exist in the database'
        def managers = [
                Manager.builder().managerId(1).year('1999').build(),
                Manager.builder().managerId(2).year('1999').build(),
                Manager.builder().managerId(3).year('1999').build()
        ]

        managerRepository.saveAll(managers)

        when: 'a request is sent to the manager search endpoint'
        def response = restClient.get(path: '/api/managers/search', contentType: 'application/json')

        then: 'the response should contain all three managers'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 3

            data.resources.collect { it.managerId }.containsAll([1,2,3])
        }
    }

    def 'manager search endpoint returns empty list when no matching managers found'() {
        given: 'no managers exist in the database'
        when: 'a request is sent to the manager search endpoint'
        def response = restClient.get(path: '/api/managers/search', contentType: 'application/json')

        then: 'the response should contain an empty resources list'
        with(response) {
            status == 200
            data.status == 'NOT_FOUND'
            data.resources.size == 0
        }
    }

    def 'manager search endpoint accepts manager-id query parameter'() {
        given: 'three managers with different ids exist in the database'
        def managers = [
                Manager.builder().managerId(1).year('2000').teamId(2).playerId(3).build(),
                Manager.builder().managerId(2).year('2001').teamId(3).playerId(4).build(),
                Manager.builder().managerId(3).year('2002').teamId(4).playerId(5).build()
        ]

        managerRepository.saveAll(managers)

        when: 'a request is sent to the manager search endpoint for manager id 1'
        def response = restClient.get(path: '/api/managers/search', contentType: 'application/json', query: ['manager-id': 1])

        then: 'only the matching manager should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].managerId == 1
        }
    }

    def 'manager search endpoint accepts year query parameter'() {
        given: 'three managers with different years exist in the database'
        def managers = [
                Manager.builder().managerId(1).year('2000').teamId(2).playerId(3).build(),
                Manager.builder().managerId(2).year('2001').teamId(3).playerId(4).build(),
                Manager.builder().managerId(3).year('2002').teamId(4).playerId(5).build()
        ]

        managerRepository.saveAll(managers)

        when: 'a request is sent to the manager search endpoint for year 2000'
        def response = restClient.get(path: '/api/managers/search', contentType: 'application/json', query: ['year': '2000'])

        then: 'only the matching manager should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].year == '2000'
        }
    }

    def 'manager search endpoint accepts team-id query parameter'() {
        given: 'three managers with different team ids exist in the database'
        def managers = [
                Manager.builder().managerId(1).year('2000').teamId(2).playerId(3).build(),
                Manager.builder().managerId(2).year('2001').teamId(3).playerId(4).build(),
                Manager.builder().managerId(3).year('2002').teamId(4).playerId(5).build()
        ]

        managerRepository.saveAll(managers)

        when: 'a request is sent to the manager search endpoint for team id 3'
        def response = restClient.get(path: '/api/managers/search', contentType: 'application/json', query: ['team-id': 3])

        then: 'only the matching manager should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].teamId == 3
        }
    }

    def 'manager search endpoint accepts player-id query parameter'() {
        given: 'three managers with different player ids exist in the database'
        def managers = [
                Manager.builder().managerId(1).year('2000').teamId(2).playerId(3).build(),
                Manager.builder().managerId(2).year('2001').teamId(3).playerId(4).build(),
                Manager.builder().managerId(3).year('2002').teamId(4).playerId(5).build()
        ]

        managerRepository.saveAll(managers)

        when: 'a request is sent to the manager search endpoint for player id 5'
        def response = restClient.get(path: '/api/managers/search', contentType: 'application/json', query: ['player-id': 5])

        then: 'only the matching manager should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].playerId == 5
        }
    }
}
