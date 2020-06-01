package org.natc.app

import org.natc.app.entity.domain.GameType
import org.natc.app.entity.domain.PlayerGame
import org.natc.app.repository.PlayerGameRepository
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDate

class PlayerGameSearchTest extends NATCFunctionalTest {

    @Autowired
    PlayerGameRepository repository;

    def setup() {
        repository.deleteAll()
    }

    def 'player game search endpoint returns player game data'() {
        given: 'a player game exists in the database'
        def playerGame = PlayerGame.builder().gameId(1234).playerId(321).build()

        repository.save(playerGame)
        
        when: 'a request is sent to the player game search endpoint'
        def response = restClient.get(path: '/api/player-games/search', contentType: 'application/json')

        then: 'the response should contain the player game'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resource == null
            data.errors == null
            data.resources.size == 1
            data.resources[0].gameId == 1234
            data.resources[0].playerId == 321
        }
    }
    
    def 'player game search endpoint provides all fields'() {
        given: 'a player game exists in the database'
        def playerGame = PlayerGame.builder()
                .gameId(12345)
                .year('2002')
                .datestamp(LocalDate.parse('2018-06-24'))
                .type(GameType.REGULAR_SEASON.getValue())
                .playerId(1234)
                .teamId(123)
                .injured(1)
                .started(0)
                .playingTime(321)
                .attempts(99)
                .goals(55)
                .assists(44)
                .turnovers(33)
                .stops(88)
                .steals(22)
                .penalties(11)
                .offensivePenalties(6)
                .penaltyShotsAttempted(9)
                .penaltyShotsMade(8)
                .overtimePenaltyShotsAttempted(3)
                .overtimePenaltyShotsMade(2)
                .offense(77)
                .points(66)
                .build()

        repository.save(playerGame)
        
        when: 'a request is sent to the player game search endpoint'
        def response = restClient.get(path: '/api/player-games/search', contentType: 'application/json')

        then: 'the response should contain all of the player game fields'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            with(data.resources[0]) {
                gameId == 12345
                year == '2002'
                datestamp == '2018-06-24'
                type == 'REGULAR_SEASON'
                playerId == 1234
                teamId == 123
                injured == true
                started == false
                playingTime == 321
                attempts == 99
                goals == 55
                assists == 44
                turnovers == 33
                stops == 88
                steals == 22
                penalties == 11
                offensivePenalties == 6
                penaltyShotsAttempted == 9
                penaltyShotsMade == 8
                overtimePenaltyShotsAttempted == 3
                overtimePenaltyShotsMade == 2
                offense == 77
                points == 66
            }
        }
    }

    def 'player game search endpoint returns all matching records'() {
        given: 'three records exist in the database'
        def playerGames = [
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        ]

        repository.saveAll(playerGames)

        when: 'a request is sent to the player game search endpoint'
        def response = restClient.get(path: '/api/player-games/search', contentType: 'application/json')

        then: 'the response should contain all three records'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 3
        }
    }
    
    def 'player game search endpoint returns empty list when no matching data found'() {
        given: 'no records exist in the database'
        when: 'a request is sent to the player game search endpoint'
        def response = restClient.get(path: '/api/player-games/search', contentType: 'application/json')

        then: 'the response should contain an empty resources list'
        with(response) {
            status == 200
            data.status == 'NOT_FOUND'
            data.resources.size == 0
        }
    }
    
    def 'player game search endpoint accepts game-id query parameter'() {
        given: 'three records exist in the database'
        def playerGames = [
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        ]

        repository.saveAll(playerGames)

        when: 'a request is sent to the player game search endpoint for game id 1'
        def response = restClient.get(path: '/api/player-games/search', contentType: 'application/json', query: ['game-id': 1])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].gameId == 1
        }
    }

    def 'player game search endpoint accepts year query parameter'() {
        given: 'three records exist in the database'
        def playerGames = [
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        ]

        repository.saveAll(playerGames)

        when: 'a request is sent to the player game search endpoint for year 2001'
        def response = restClient.get(path: '/api/player-games/search', contentType: 'application/json', query: ['year': '2001'])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].year == '2001'
        }
    }

    def 'player game search endpoint accepts datestamp query parameter'() {
        given: 'three records exist in the database'
        def playerGames = [
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        ]

        repository.saveAll(playerGames)

        when: 'a request is sent to the player game search endpoint for datestamp 2002-03-16'
        def response = restClient.get(path: '/api/player-games/search', contentType: 'application/json', query: ['datestamp': '2002-03-16'])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].datestamp == '2002-03-16'
        }
    }

    def 'player game search endpoint accepts type query parameter'() {
        given: 'three records exist in the database'
        def playerGames = [
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        ]

        repository.saveAll(playerGames)

        when: 'a request is sent to the player game search endpoint for type PRESEASON'
        def response = restClient.get(path: '/api/player-games/search', contentType: 'application/json', query: ['type': 'PRESEASON'])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].type == 'PRESEASON'
        }
    }

    def 'player game search endpoint accepts player-id query parameter'() {
        given: 'three records exist in the database'
        def playerGames = [
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        ]

        repository.saveAll(playerGames)

        when: 'a request is sent to the player game search endpoint for player id 1'
        def response = restClient.get(path: '/api/player-games/search', contentType: 'application/json', query: ['player-id': 2])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].playerId == 2
        }
    }

    def 'player game search endpoint accepts team-id query parameter'() {
        given: 'three records exist in the database'
        def playerGames = [
                PlayerGame.builder().gameId(1).year("2000").datestamp(LocalDate.parse("2000-03-16")).type(GameType.PRESEASON.getValue()).playerId(1).teamId(1).build(),
                PlayerGame.builder().gameId(2).year("2001").datestamp(LocalDate.parse("2001-03-16")).type(GameType.REGULAR_SEASON.getValue()).playerId(2).teamId(2).build(),
                PlayerGame.builder().gameId(3).year("2002").datestamp(LocalDate.parse("2002-03-16")).type(GameType.POSTSEASON.getValue()).playerId(3).teamId(3).build()
        ]

        repository.saveAll(playerGames)

        when: 'a request is sent to the player game search endpoint for team id 1'
        def response = restClient.get(path: '/api/player-games/search', contentType: 'application/json', query: ['team-id': 3])

        then: 'only the matching record should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].teamId == 3
        }
    }
}
