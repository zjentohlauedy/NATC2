package org.natc.app

import org.natc.app.entity.domain.GameType
import org.natc.app.entity.domain.PlayerStatsSummary
import org.natc.app.repository.PlayerStatsSummaryRepository
import org.springframework.beans.factory.annotation.Autowired

class PlayerStatsSummarySearchTest extends NATCFunctionalTest {

    @Autowired
    PlayerStatsSummaryRepository repository

    def setup() {
        repository.deleteAll()
    }

    def 'player stats summary search endpoint returns player data'() {
        given: 'a player stats summary exists in the database'
        def PlayerStatsSummary playerStatsSummary = PlayerStatsSummary.builder()
                .year('1996')
                .type(GameType.REGULAR_SEASON.getValue())
                .playerId(5434)
                .teamId(37)
                .build()

        repository.save(playerStatsSummary)

        when: 'a request is sent to the player stats summary search endpoint'
        def response = restClient.get(path: '/api/player-stats-summaries/search', contentType: 'application/json')

        then: 'the response should contain the player stats summary'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resource == null
            data.errors == null
            data.resources.size == 1
            data.resources[0].year == '1996'
            data.resources[0].type == 'REGULAR_SEASON'
            data.resources[0].playerId == 5434
            data.resources[0].teamId == 37
        }
    }

    def 'player stats summary search endpoint provides all fields'() {
        given: 'a player stats summary exists in the database'
        def PlayerStatsSummary playerStatsSummary = PlayerStatsSummary.builder()
                .year("2002")
                .type(GameType.PRESEASON.getValue())
                .playerId(123)
                .games(99)
                .gamesStarted(88)
                .playingTime(1234)
                .attempts(77)
                .goals(66)
                .assists(55)
                .turnovers(44)
                .stops(33)
                .steals(22)
                .penalties(11)
                .offensivePenalties(9)
                .penaltyShotsAttempted(8)
                .penaltyShotsMade(7)
                .overtimePenaltyShotsAttempted(6)
                .overtimePenaltyShotsMade(5)
                .teamId(321)
                .build()

        repository.save(playerStatsSummary)

        when: 'a request is sent to the player stats summary search endpoint'
        def response = restClient.get(path: '/api/player-stats-summaries/search', contentType: 'application/json')

        then: 'the response should contain all of the player stats summary fields'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            with(data.resources[0]) {
                year == "2002"
                type == 'PRESEASON'
                playerId == 123
                games == 99
                gamesStarted == 88
                playingTime == 1234
                attempts == 77
                goals == 66
                assists == 55
                turnovers == 44
                stops == 33
                steals == 22
                penalties == 11
                offensivePenalties == 9
                penaltyShotsAttempted == 8
                penaltyShotsMade == 7
                overtimePenaltyShotsAttempted == 6
                overtimePenaltyShotsMade == 5
                teamId == 321
            }
        }
    }

    def 'player stats summary search endpoint returns all matching records'() {
        given: 'three records exist in the database'
        def playerStatsSummaries = [
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        ]

        repository.saveAll(playerStatsSummaries)

        when: 'a request is sent to the player stats summary search endpoint'
        def response = restClient.get(path: '/api/player-stats-summaries/search', contentType: 'application/json')

        then: 'the response should contain all three records'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 3

            data.resources.collect { it.playerId }.containsAll([1, 2, 3])
        }
    }

    def 'player stats summary search endpoint returns empty list when no matching records found'() {
        given: 'three records exist in the database'
        def playerStatsSummaries = [
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        ]

        repository.saveAll(playerStatsSummaries)

        when: 'a request is sent to the player stats summary search endpoint for year 2003'
        def response = restClient.get(path: '/api/player-stats-summaries/search', contentType: 'application/json', query: ['year': '2003'])

        then: 'the response should contain an empty resources list'
        with(response) {
            status == 200
            data.status == 'NOT_FOUND'
            data.resources.size == 0
        }
    }

    def 'player stats summary search endpoint accepts year query parameter'() {
        given: 'three records exist in the database'
        def playerStatsSummaries = [
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        ]

        repository.saveAll(playerStatsSummaries)

        when: 'a request is sent to the player stats summary search endpoint for year 2000'
        def response = restClient.get(path: '/api/player-stats-summaries/search', contentType: 'application/json', query: ['year': '2000'])

        then: 'the response should contain an empty resources list'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].year == '2000'
        }
    }

    def 'player stats summary search endpoint accepts type query parameter'() {
        given: 'three records exist in the database'
        def playerStatsSummaries = [
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        ]

        repository.saveAll(playerStatsSummaries)

        when: 'a request is sent to the player stats summary search endpoint for type POSTSEASON'
        def response = restClient.get(path: '/api/player-stats-summaries/search', contentType: 'application/json', query: ['type': 'POSTSEASON'])

        then: 'the response should contain an empty resources list'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].type == 'POSTSEASON'
        }
    }

    def 'player stats summary search endpoint accepts player-id query parameter'() {
        given: 'three records exist in the database'
        def playerStatsSummaries = [
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        ]

        repository.saveAll(playerStatsSummaries)

        when: 'a request is sent to the player stats summary search endpoint for player-id 3'
        def response = restClient.get(path: '/api/player-stats-summaries/search', contentType: 'application/json', query: ['player-id': 3])

        then: 'the response should contain an empty resources list'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].playerId == 3
        }
    }

    def 'player stats summary search endpoint accepts team-id query parameter'() {
        given: 'three records exist in the database'
        def playerStatsSummaries = [
                PlayerStatsSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerStatsSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerStatsSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        ]

        repository.saveAll(playerStatsSummaries)

        when: 'a request is sent to the player stats summary search endpoint for team-id 2'
        def response = restClient.get(path: '/api/player-stats-summaries/search', contentType: 'application/json', query: ['team-id': 2])

        then: 'the response should contain an empty resources list'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].teamId == 2
        }
    }
}
