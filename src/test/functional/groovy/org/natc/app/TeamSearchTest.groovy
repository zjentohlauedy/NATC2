package org.natc.app


import org.natc.app.entity.domain.Team
import org.natc.app.repository.TeamRepository
import org.springframework.beans.factory.annotation.Autowired

class TeamSearchTest extends NATCFunctionalTest {

    @Autowired
    TeamRepository teamRepository

    def setup() {
        teamRepository.deleteAll()
    }

    def 'team search endpoint returns team data'() {
        given: 'a team exists in the database'
        def team = Team.builder().teamId(1).year('2000').build()
        teamRepository.save(team)

        when: 'a request is sent to the team search endpoint'
        def response = restClient.get(path: '/api/teams/search', contentType: 'application/json')

        then: 'the response should contain the team'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resource == null
            data.errors == null
            data.resources.size == 1
            data.resources[0].teamId == 1
            data.resources[0].year == '2000'
        }
    }

    def 'team search endpoint provides all team fields'() {
        given: 'a team exists in the database'
        def team = Team.builder()
                .teamId(123)
                .year('1999')
                .location('Here')
                .name('Them')
                .abbreviation('ABC.')
                .timeZone('Americas/Los_Angeles')
                .gameTime(999)
                .conference(111)
                .division(222)
                .allstarTeam(1)
                .preseasonGames(12)
                .preseasonWins(7)
                .preseasonLosses(5)
                .games(99)
                .wins(55)
                .losses(44)
                .divisionWins(33)
                .divisionLosses(22)
                .outOfConferenceWins(66)
                .outOfConferenceLosses(11)
                .overtimeWins(9)
                .overtimeLosses(3)
                .roadWins(88)
                .roadLosses(77)
                .homeWins(35)
                .homeLosses(46)
                .divisionRank(6)
                .playoffRank(15)
                .playoffGames(14)
                .round1Wins(13)
                .round2Wins(6)
                .round3Wins(4)
                .expectation(0.432)
                .drought(17)
                .build()

        teamRepository.save(team)

        when: 'a request is sent to the team search endpoint'
        def response = restClient.get(path: '/api/teams/search', contentType: 'application/json')

        then: 'the response should contain all of the team fields'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            with(data.resources[0]) {
                teamId == 123
                year == '1999'
                location == 'Here'
                name == 'Them'
                abbreviation == 'ABC.'
                timeZone == 'Americas/Los_Angeles'
                gameTime == 999
                conferenceId == 111
                divisionId == 222
                allstarTeam == true
                preseasonGames == 12
                preseasonWins == 7
                preseasonLosses == 5
                games == 99
                wins == 55
                losses == 44
                divisionWins == 33
                divisionLosses == 22
                outOfConferenceWins == 66
                outOfConferenceLosses == 11
                overtimeWins == 9
                overtimeLosses == 3
                roadWins == 88
                roadLosses == 77
                homeWins == 35
                homeLosses == 46
                divisionRank == 6
                playoffRank == 15
                playoffGames == 14
                round1Wins == 13
                round2Wins == 6
                round3Wins == 4
                expectation == 0.432
                drought == 17
            }
        }
    }

    def 'team search endpoint returns all matching teams'() {
        given: 'three teams exist in the database'
        def teams = [
                Team.builder().teamId(1).year("2000").build(),
                Team.builder().teamId(2).year("2000").build(),
                Team.builder().teamId(3).year("2000").build()
        ]

        teamRepository.saveAll(teams)

        when: 'a request is sent to the team search endpoint'
        def response = restClient.get(path: '/api/teams/search', contentType: 'application/json')

        then: 'the response should contain all three teams'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 3

            data.resources.collect { it.teamId }.containsAll([1, 2, 3])
        }
    }

    def 'team search endpoint returns empty list when no matching teams found'() {
        given: 'no teams exists in the database'
        when: 'a request is sent to the team search endpoint'
        def response = restClient.get(path: '/api/teams/search', contentType: 'application/json')

        then: 'the response should contain an empty resources list'
        with(response) {
            status == 200
            data.status == 'NOT_FOUND'
            data.resources.size == 0
        }
    }

    def 'team search endpoint accepts team-id query parameter'() {
        given: 'teams with different ids exist in the database'
        def teams = [
                Team.builder().teamId(1).year("2000").conference(1).division(1).allstarTeam(0).build(),
                Team.builder().teamId(2).year("2000").conference(1).division(1).allstarTeam(0).build(),
                Team.builder().teamId(3).year("2000").conference(1).division(1).allstarTeam(0).build()
        ]

        teamRepository.saveAll(teams)

        when: 'a request is sent to the team search endpoint for team id 1'
        def response = restClient.get(path: '/api/teams/search', contentType: 'application/json', query: ['team-id': 1])

        then: 'only the matching team should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].teamId == 1
        }
    }

    def 'team search endpoint accepts year query parameter'() {
        given: 'teams with different years exist in the database'
        def teams = [
                Team.builder().teamId(1).year("2000").conference(1).division(1).allstarTeam(0).build(),
                Team.builder().teamId(1).year("2001").conference(1).division(1).allstarTeam(0).build(),
                Team.builder().teamId(1).year("2002").conference(1).division(1).allstarTeam(0).build()
        ]

        teamRepository.saveAll(teams)

        when: 'a request is sent to the team search endpoint for year 2000'
        def response = restClient.get(path: '/api/teams/search', contentType: 'application/json', query: ['year': '2000'])

        then: 'only the matching team should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].year == '2000'
        }
    }

    def 'team search endpoint accepts conference-id query parameter'() {
        given: 'teams with different conferences exist in the database'
        def teams = [
                Team.builder().teamId(1).year("2000").conference(1).division(1).allstarTeam(0).build(),
                Team.builder().teamId(1).year("2001").conference(2).division(1).allstarTeam(0).build(),
                Team.builder().teamId(1).year("2002").conference(3).division(1).allstarTeam(0).build()
        ]

        teamRepository.saveAll(teams)

        when: 'a request is sent to the team search endpoint for conference id 1'
        def response = restClient.get(path: '/api/teams/search', contentType: 'application/json', query: ['conference-id': 1])

        then: 'only the matching team should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].conferenceId == 1
        }
    }

    def 'team search endpoint accepts division-id query parameter'() {
        given: 'teams with different divisions exist in the database'
        def teams = [
                Team.builder().teamId(1).year("2000").conference(1).division(1).allstarTeam(0).build(),
                Team.builder().teamId(1).year("2001").conference(1).division(2).allstarTeam(0).build(),
                Team.builder().teamId(1).year("2002").conference(1).division(3).allstarTeam(0).build()
        ]

        teamRepository.saveAll(teams)

        when: 'a request is sent to the team search endpoint for division id 1'
        def response = restClient.get(path: '/api/teams/search', contentType: 'application/json', query: ['division-id': 1])

        then: 'only the matching team should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].divisionId == 1
        }
    }
}
