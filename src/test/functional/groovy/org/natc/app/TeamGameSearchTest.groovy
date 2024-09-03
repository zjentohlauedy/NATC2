package org.natc.app

import org.natc.app.entity.domain.GameType
import org.natc.app.entity.domain.TeamGame
import org.natc.app.repository.TeamGameRepository
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDate

class TeamGameSearchTest extends NATCFunctionalTest {

    @Autowired
    TeamGameRepository repository

    def setup() {
        repository.deleteAll()
    }

    def 'team game search endpoint returns team game data'() {
        given: 'a team game exists in the database'
        def teamGame = TeamGame.builder().gameId(1234).teamId(12).build()

        repository.save(teamGame)

        when: 'a request is sent to the team game search endpoint'
        def response = restClient.get(path: '/api/team-games/search', contentType: 'application/json')

        then: 'the response should contain the team game'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resource == null
            data.errors == null
            data.resources.size() == 1
            data.resources[0].gameId == 1234
            data.resources[0].teamId == 12
        }
    }

    def 'team game search endpoint provides all fields'() {
        given: 'a team game exists in the database'
        def teamGame = TeamGame.builder()
                .gameId(1234)
                .year('1984')
                .datestamp(LocalDate.parse('2012-06-14'))
                .type(GameType.REGULAR_SEASON.getValue())
                .playoffRound(3)
                .teamId(22)
                .opponent(33)
                .road(0)
                .overtime(1)
                .win(1)
                .possessions(4321)
                .possessionTime(9999)
                .attempts(555)
                .goals(111)
                .turnovers(66)
                .steals(55)
                .penalties(44)
                .offensivePenalties(11)
                .penaltyShotsAttempted(9)
                .penaltyShotsMade(5)
                .overtimePenaltyShotsAttempted(6)
                .overtimePenaltyShotsMade(2)
                .period1Score(19)
                .period2Score(18)
                .period3Score(17)
                .period4Score(16)
                .period5Score(15)
                .overtimeScore(12)
                .totalScore(99)
                .build()

        repository.save(teamGame)

        when: 'a request is sent to the team game search endpoint'
        def response = restClient.get(path: '/api/team-games/search', contentType: 'application/json')

        then: 'the response should contain all of the team game fields'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size() == 1
            with(data.resources[0]) {
                gameId == 1234
                year == '1984'
                datestamp == '2012-06-14'
                type == 'REGULAR_SEASON'
                playoffRound == 3
                teamId == 22
                opponent == 33
                road == false
                overtime == true
                win == true
                possessions == 4321
                possessionTime == 9999
                attempts == 555
                goals == 111
                turnovers == 66
                steals == 55
                penalties == 44
                offensivePenalties == 11
                penaltyShotsAttempted == 9
                penaltyShotsMade == 5
                overtimePenaltyShotsAttempted == 6
                overtimePenaltyShotsMade == 2
                period1Score == 19
                period2Score == 18
                period3Score == 17
                period4Score == 16
                period5Score == 15
                overtimeScore == 12
                totalScore == 99
            }
        }
    }

    def 'team game search endpoint returns all matching records'() {
        given: 'three records exist in the database'
        def teamGames = [
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        ]

        repository.saveAll(teamGames)

        when: 'a request is sent to the team game search endpoint'
        def response = restClient.get(path: '/api/team-games/search', contentType: 'application/json')

        then: 'the response should contain all three records'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size() == 3
        }
    }

    def 'team game search endpoint returns empty list when no matching data found'() {
        given: 'no records exist in the database'
        when: 'a request is sent to the team game search endpoint'
        def response = restClient.get(path: '/api/team-games/search', contentType: 'application/json')

        then: 'the response should contain an empty resources list'
        with(response) {
            status == 200
            data.status == 'NOT_FOUND'
            data.resources.size() == 0
        }
    }

    def 'team game search endpoint accepts game-id query parameter'() {
        given: 'three records exist in the database'
        def teamGames = [
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        ]

        repository.saveAll(teamGames)

        when: 'a request is sent to the team game search endpoint for game id 1'
        def response = restClient.get(path: '/api/team-games/search', contentType: 'application/json', query: ['game-id': 1])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size() == 1
            data.resources[0].gameId == 1
        }
    }

    def 'team game search endpoint accepts year query parameter'() {
        given: 'three records exist in the database'
        def teamGames = [
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        ]

        repository.saveAll(teamGames)

        when: 'a request is sent to the team game search endpoint for year 2001'
        def response = restClient.get(path: '/api/team-games/search', contentType: 'application/json', query: ['year': '2001'])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size() == 1
            data.resources[0].year == '2001'
        }
    }

    def 'team game search endpoint accepts datestamp query parameter'() {
        given: 'three records exist in the database'
        def teamGames = [
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        ]

        repository.saveAll(teamGames)

        when: 'a request is sent to the team game search endpoint for date stamp 2002-03-16'
        def response = restClient.get(path: '/api/team-games/search', contentType: 'application/json', query: ['datestamp': '2002-03-16'])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size() == 1
            data.resources[0].datestamp == '2002-03-16'
        }
    }

    def 'team game search endpoint accepts type query parameter'() {
        given: 'three records exist in the database'
        def teamGames = [
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        ]

        repository.saveAll(teamGames)

        when: 'a request is sent to the team game search endpoint for type PRESEASON'
        def response = restClient.get(path: '/api/team-games/search', contentType: 'application/json', query: ['type': 'PRESEASON'])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size() == 1
            data.resources[0].type == 'PRESEASON'
        }
    }

    def 'team game search endpoint accepts team-id query parameter'() {
        given: 'three records exist in the database'
        def teamGames = [
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        ]

        repository.saveAll(teamGames)

        when: 'a request is sent to the team game search endpoint for team id 2'
        def response = restClient.get(path: '/api/team-games/search', contentType: 'application/json', query: ['team-id': 2])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size() == 1
            data.resources[0].teamId == 2
        }
    }

    def 'team game search endpoint accepts opponent query parameter'() {
        given: 'three records exist in the database'
        def teamGames = [
                TeamGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).teamId(1).opponent(1).build(),
                TeamGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).teamId(2).opponent(2).build(),
                TeamGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).teamId(3).opponent(3).build()
        ]

        repository.saveAll(teamGames)

        when: 'a request is sent to the team game search endpoint for opponent 3'
        def response = restClient.get(path: '/api/team-games/search', contentType: 'application/json', query: ['opponent': 3])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size() == 1
            data.resources[0].opponent == 3
        }
    }
}
