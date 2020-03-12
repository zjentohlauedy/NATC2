package org.natc.natc

import org.natc.natc.entity.domain.Manager
import org.natc.natc.entity.domain.ManagerAward
import org.natc.natc.entity.domain.ManagerStyle
import org.natc.natc.repository.ManagerRepository
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
        given: 'three managers exist in the database'
        def managers = [
                Manager.builder().managerId(1).year('1999').build(),
                Manager.builder().managerId(2).year('1999').build(),
                Manager.builder().managerId(3).year('1999').build()
        ]

        managerRepository.saveAll(managers)

        when: 'a request is sent to the manager search endpoint'
        def response = restClient.get(path: '/api/managers/search', contentType: 'application/json', query: ['manager-id': 4])

        then: 'the response should contain all three managers'
        with(response) {
            status == 200
            data.status == 'NOT_FOUND'
            data.resources.size == 0
        }
    }
}
