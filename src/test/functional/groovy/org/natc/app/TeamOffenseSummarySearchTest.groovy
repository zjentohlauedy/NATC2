package org.natc.app

import org.natc.app.entity.domain.GameType
import org.natc.app.entity.domain.TeamOffenseSummary
import org.natc.app.repository.TeamOffenseSummaryRepository
import org.springframework.beans.factory.annotation.Autowired

class TeamOffenseSummarySearchTest extends NATCFunctionalTest {

    @Autowired
    TeamOffenseSummaryRepository repository

    def setup() {
        repository.deleteAll()
    }

    def 'team offense summary search endpoint returns team data'() {
        given: 'a team offense summary exists in the database'
        def teamOffenseSummary = TeamOffenseSummary.builder()
                .year("2016")
                .type(GameType.POSTSEASON.getValue())
                .teamId(12)
                .build()

        repository.save(teamOffenseSummary)

        when: 'a request is sent to the team offense summary search endpoint'
        def response = restClient.get(path: '/api/team-offense-summaries/search', contentType: 'application/json')

        then: 'the response should contain the team offense summary'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resource == null
            data.errors == null
            data.resources.size == 1
            data.resources[0].year == '2016'
            data.resources[0].type == 'POSTSEASON'
            data.resources[0].teamId == 12
        }
    }

    def 'team offense summary search endpoint provides all fields'() {
        given: 'a team offense summary exists in the database'
        def teamOffenseSummary = TeamOffenseSummary.builder()
                .year("2016")
                .type(GameType.REGULAR_SEASON.getValue())
                .teamId(123)
                .games(99)
                .possessions(234)
                .possessionTime(987)
                .attempts(345)
                .goals(321)
                .turnovers(111)
                .steals(222)
                .penalties(333)
                .offensivePenalties(77)
                .penaltyShotsAttempted(88)
                .penaltyShotsMade(99)
                .overtimePenaltyShotsAttempted(11)
                .overtimePenaltyShotsMade(22)
                .score(678)
                .build()

        repository.save(teamOffenseSummary)

        when: 'a request is sent to the team offense summary search endpoint'
        def response = restClient.get(path: '/api/team-offense-summaries/search', contentType: 'application/json')

        then: 'the response should contain all of the team offense summary fields'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            with(data.resources[0]) {
                year == "2016"
                type == 'REGULAR_SEASON'
                teamId == 123
                games == 99
                possessions == 234
                possessionTime == 987
                attempts == 345
                goals == 321
                turnovers == 111
                steals == 222
                penalties == 333
                offensivePenalties == 77
                penaltyShotsAttempted == 88
                penaltyShotsMade == 99
                overtimePenaltyShotsAttempted == 11
                overtimePenaltyShotsMade == 22
                score == 678
            }
        }
    }

    def 'team offense summary search endpoint returns all matching records'() {
        given: 'three records exist in the database'
        def teamOffenseSummaries = [
                TeamOffenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(2).build(),
                TeamOffenseSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).teamId(3).build()
        ]

        repository.saveAll(teamOffenseSummaries)

        when: 'a request is sent to the team offense summary search endpoint'
        def response = restClient.get(path: '/api/team-offense-summaries/search', contentType: 'application/json')

        then: 'the response should contain all three records'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 3

            data.resources.collect { it.teamId }.containsAll([1, 2, 3])
        }
    }

    def 'team offense summary search endpoint returns empty list when no matching teams found'() {
        given: 'no records exist in the database'
        when: 'a request is sent to the team offense summary search endpoint'
        def response = restClient.get(path: '/api/team-offense-summaries/search', contentType: 'application/json')

        then: 'the response should contain an empty resources list'
        with(response) {
            status == 200
            data.status == 'NOT_FOUND'
            data.resources.size == 0
        }
    }

    def 'team offense summary search endpoint accepts year query parameter'() {
        given: 'three records exist in the database'
        def teamOffenseSummaries = [
                TeamOffenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(2).build(),
                TeamOffenseSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).teamId(3).build()
        ]

        repository.saveAll(teamOffenseSummaries)

        when: 'a request is sent to the team offense summary search endpoint for year 2000'
        def response = restClient.get(path: '/api/team-offense-summaries/search', contentType: 'application/json', query: ['year': '2000'])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].year == '2000'
        }
    }

    def 'team offense summary search endpoint accepts type query parameter'() {
        given: 'three records exist in the database'
        def teamOffenseSummaries = [
                TeamOffenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(2).build(),
                TeamOffenseSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).teamId(3).build()
        ]

        repository.saveAll(teamOffenseSummaries)

        when: 'a request is sent to the team offense summary search endpoint for type POSTSEASON'
        def response = restClient.get(path: '/api/team-offense-summaries/search', contentType: 'application/json', query: ['type': 'POSTSEASON'])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].type == 'POSTSEASON'
        }
    }

    def 'team offense summary search endpoint accepts team-id query parameter'() {
        given: 'three records exist in the database'
        def teamOffenseSummaries = [
                TeamOffenseSummary.builder().year("2000").type(GameType.PRESEASON.getValue()).teamId(1).build(),
                TeamOffenseSummary.builder().year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(2).build(),
                TeamOffenseSummary.builder().year("2002").type(GameType.POSTSEASON.getValue()).teamId(3).build()
        ]

        repository.saveAll(teamOffenseSummaries)

        when: 'a request is sent to the team offense summary search endpoint for team-id 2'
        def response = restClient.get(path: '/api/team-offense-summaries/search', contentType: 'application/json', query: ['team-id': 2])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].teamId == 2
        }
    }
}
