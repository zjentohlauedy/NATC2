package org.natc.natc

import org.natc.natc.entity.domain.GameType
import org.natc.natc.entity.domain.TeamDefenseSummary
import org.natc.natc.repository.TeamDefenseSummaryRepository
import org.springframework.beans.factory.annotation.Autowired

class TeamDefenseSummarySearchTest extends NATCFunctionalTest {

    @Autowired
    def TeamDefenseSummaryRepository repository

    def setup() {
        repository.deleteAll()
    }

    def 'team defense summary search endpoint returns team data'() {
        given: 'a team defense summary exists in the database'
        def teamDefenseSummary = TeamDefenseSummary.builder()
                .year("2014")
                .type(GameType.REGULAR_SEASON.getValue())
                .teamId(23)
                .build()

        repository.save(teamDefenseSummary)

        when: 'a request is sent to the team defense summary search endpoint'
        def response = restClient.get(path: '/api/team-defense-summaries/search', contentType: 'application/json')

        then: 'the response should contain the team defense summary'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resource == null
            data.errors == null
            data.resources.size == 1
            data.resources[0].year == '2014'
            data.resources[0].type == 'REGULAR_SEASON'
            data.resources[0].teamId == 23
        }
    }

    def 'team defense summary search endpoint provides all fields'() {
        given: 'a team defense summary exists in the database'
        def teamDefenseSummary = TeamDefenseSummary.builder()
                .year("1997")
                .type(GameType.REGULAR_SEASON.getValue())
                .teamId(123)
                .games(83)
                .possessions(321)
                .possessionTime(999)
                .attempts(444)
                .goals(333)
                .turnovers(55)
                .steals(111)
                .penalties(99)
                .offensivePenalties(66)
                .penaltyShotsAttempted(77)
                .penaltyShotsMade(44)
                .overtimePenaltyShotsAttempted(17)
                .overtimePenaltyShotsMade(12)
                .score(666)
                .build()

        repository.save(teamDefenseSummary)

        when: 'a request is sent to the team defense summary search endpoint'
        def response = restClient.get(path: '/api/team-defense-summaries/search', contentType: 'application/json')

        then: 'the response should contain all of the team defense summary fields'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            with(data.resources[0]) {
                year == "1997"
                type == 'REGULAR_SEASON'
                teamId == 123
                games == 83
                possessions == 321
                possessionTime == 999
                attempts == 444
                goals == 333
                turnovers == 55
                steals == 111
                penalties == 99
                offensivePenalties == 66
                penaltyShotsAttempted == 77
                penaltyShotsMade == 44
                overtimePenaltyShotsAttempted == 17
                overtimePenaltyShotsMade == 12
                score == 666
            }
        }
    }

    def 'team defense summary search endpoint returns all matching records'() {
        given: 'three records exist in the database'
        def teamDefenseSummaries = [
                TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamDefenseSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(2).build(),
                TeamDefenseSummary.builder().year("2002").type(GameType.ALLSTAR.getValue()).teamId(3).build()
        ]

        repository.saveAll(teamDefenseSummaries)

        when: 'a request is sent to the team defense summary search endpoint'
        def response = restClient.get(path: '/api/team-defense-summaries/search', contentType: 'application/json')

        then: 'the response should contain all three records'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 3

            data.resources.collect { it.teamId }.containsAll([1, 2, 3])
        }
    }

    def 'team defense summary search endpoint returns empty list when no matching records found'() {
        given: 'three records exist in the database'
        def teamDefenseSummaries = [
                TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamDefenseSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(2).build(),
                TeamDefenseSummary.builder().year("2002").type(GameType.ALLSTAR.getValue()).teamId(3).build()
        ]

        repository.saveAll(teamDefenseSummaries)

        when: 'a request is sent to the team defense summary search endpoint for year 2003'
        def response = restClient.get(path: '/api/team-defense-summaries/search', contentType: 'application/json', query: ['year': '2003'])

        then: 'the response should contain an empty resources list'
        with(response) {
            status == 200
            data.status == 'NOT_FOUND'
            data.resources.size == 0
        }
    }

    def 'team defense summary search endpoint accepts year query parameter'() {
        given: 'three records exist in the database'
        def teamDefenseSummaries = [
                TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamDefenseSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(2).build(),
                TeamDefenseSummary.builder().year("2002").type(GameType.ALLSTAR.getValue()).teamId(3).build()
        ]

        repository.saveAll(teamDefenseSummaries)

        when: 'a request is sent to the team defense summary search endpoint for year 2000'
        def response = restClient.get(path: '/api/team-defense-summaries/search', contentType: 'application/json', query: ['year': '2000'])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].year == '2000'
        }
    }

    def 'team defense summary search endpoint accepts type query parameter'() {
        given: 'three records exist in the database'
        def teamDefenseSummaries = [
                TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamDefenseSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(2).build(),
                TeamDefenseSummary.builder().year("2002").type(GameType.ALLSTAR.getValue()).teamId(3).build()
        ]

        repository.saveAll(teamDefenseSummaries)

        when: 'a request is sent to the team defense summary search endpoint for type REGULAR_SEASON'
        def response = restClient.get(path: '/api/team-defense-summaries/search', contentType: 'application/json', query: ['type': 'REGULAR_SEASON'])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].type == 'REGULAR_SEASON'
        }
    }

    def 'team defense summary search endpoint accepts team-id query parameter'() {
        given: 'three records exist in the database'
        def teamDefenseSummaries = [
                TeamDefenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamDefenseSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(2).build(),
                TeamDefenseSummary.builder().year("2002").type(GameType.ALLSTAR.getValue()).teamId(3).build()
        ]

        repository.saveAll(teamDefenseSummaries)

        when: 'a request is sent to the team defense summary search endpoint for team id 3'
        def response = restClient.get(path: '/api/team-defense-summaries/search', contentType: 'application/json', query: ['team-id': 3])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].teamId == 3
        }
    }
}