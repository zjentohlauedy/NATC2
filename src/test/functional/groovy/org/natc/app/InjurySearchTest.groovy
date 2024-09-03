package org.natc.app

import org.natc.app.entity.domain.Injury
import org.natc.app.repository.InjuryRepository
import org.springframework.beans.factory.annotation.Autowired

class InjurySearchTest extends NATCFunctionalTest {

    @Autowired
    InjuryRepository injuryRepository

    def setup() {
        injuryRepository.deleteAll()
    }

    def 'injury search endpoint returns injury data'() {
        given: 'an injury record exists in the database'
        def injury = Injury.builder().gameId(123).playerId(4321).build()
        injuryRepository.save(injury)

        when: 'a request is sent to the injury search endpoint'
        def response = restClient.get(path: '/api/injuries/search', contentType: 'application/json')

        then: 'the response should contain the injury'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resource == null
            data.errors == null
            data.resources.size() == 1
            data.resources[0].gameId == 123
            data.resources[0].playerId == 4321
        }
    }

    def 'injury search endpoint provides all injury fields'() {
        given: 'an injury record exists in the database'
        def injury = Injury.builder()
                .gameId(123)
                .playerId(4321)
                .teamId(56)
                .duration(17)
                .build()
        injuryRepository.save(injury)

        when: 'a request is sent to the injury search endpoint'
        def response = restClient.get(path: '/api/injuries/search', contentType: 'application/json')

        then: 'the response should contain all of the injury fields'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size() == 1
            with(data.resources[0]) {
                gameId == 123
                playerId == 4321
                teamId == 56
                duration == 17
            }
        }
    }

    def 'injury search endpoint returns all matching injury records'() {
        given: 'three injury records exist in the database'
        def injuries = [
                Injury.builder().gameId(1).playerId(1).build(),
                Injury.builder().gameId(2).playerId(2).build(),
                Injury.builder().gameId(3).playerId(3).build()
        ]
        injuryRepository.saveAll(injuries)

        when: 'a request is sent to the injury search endpoint'
        def response = restClient.get(path: '/api/injuries/search', contentType: 'application/json')

        then: 'the response should contain all three injury records'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size() == 3

            data.resources.collect { it.gameId }.containsAll([1,2,3])
        }
    }

    def 'injury search endpoint returns an empty list when no matching injuries are found'() {
        given: 'no injury record exist in the database'
        when: 'a request is sent to the injury search endpoint'
        def response = restClient.get(path: '/api/injuries/search', contentType: 'application/json')

        then: 'the response should contain an empty resources list'
        with(response) {
            status == 200
            data.status == 'NOT_FOUND'
            data.resources.size() == 0
        }
    }

    def 'injury search endpoint accepts the year game-id parameter'() {
        given: 'three injury records exist in the database'
        def injuries = [
                Injury.builder().gameId(1).playerId(4).teamId(7).build(),
                Injury.builder().gameId(2).playerId(5).teamId(8).build(),
                Injury.builder().gameId(3).playerId(6).teamId(9).build()
        ]
        injuryRepository.saveAll(injuries)

        when: 'a request is sent to the injury search endpoint for game id 1'
        def response = restClient.get(path: '/api/injuries/search', contentType: 'application/json', query: ['game-id': 1])

        then: 'only the matching injury should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size() == 1
            data.resources[0].gameId == 1
        }
    }

    def 'injury search endpoint accepts the year player-id parameter'() {
        given: 'three injury records exist in the database'
        def injuries = [
                Injury.builder().gameId(1).playerId(4).teamId(7).build(),
                Injury.builder().gameId(2).playerId(5).teamId(8).build(),
                Injury.builder().gameId(3).playerId(6).teamId(9).build()
        ]
        injuryRepository.saveAll(injuries)

        when: 'a request is sent to the injury search endpoint for player id 5'
        def response = restClient.get(path: '/api/injuries/search', contentType: 'application/json', query: ['player-id': 5])

        then: 'only the matching injury should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size() == 1
            data.resources[0].playerId == 5
        }
    }

    def 'injury search endpoint accepts the year team-id parameter'() {
        given: 'three injury records exist in the database'
        def injuries = [
                Injury.builder().gameId(1).playerId(4).teamId(7).build(),
                Injury.builder().gameId(2).playerId(5).teamId(8).build(),
                Injury.builder().gameId(3).playerId(6).teamId(9).build()
        ]
        injuryRepository.saveAll(injuries)

        when: 'a request is sent to the injury search endpoint for team id 9'
        def response = restClient.get(path: '/api/injuries/search', contentType: 'application/json', query: ['team-id': 9])

        then: 'only the matching injury should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size() == 1
            data.resources[0].teamId == 9
        }
    }
}
