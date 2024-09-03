package org.natc.app

import org.natc.app.entity.domain.GameState
import org.natc.app.entity.domain.Period
import org.natc.app.entity.domain.PossessionType
import org.natc.app.repository.GameStateRepository
import org.springframework.beans.factory.annotation.Autowired

class GameStateSearchTest extends NATCFunctionalTest {

    @Autowired
    GameStateRepository gameStateRepository

    def setup() {
        gameStateRepository.deleteAll()
    }

    def 'game state search endpoint returns game state data'() {
        given: 'a game state exists in the database'
        def gameState = GameState.builder().gameId(123).build()
        gameStateRepository.save(gameState)

        when: 'a request is sent to the game state search endpoint'
        def response = restClient.get(path: '/api/game-states/search', contentType: 'application/json')

        then: 'the response should contain the game state'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resource == null
            data.errors == null
            data.resources.size() == 1
            data.resources[0].gameId == 123
        }
    }

    def 'injury search endpoint provides all injury fields'() {
        given: 'a game state exists in the database'
        def gameState = GameState.builder()
                .gameId(123)
                .started(1)
                .startTime(55555)
                .sequence(111)
                .period(Period.FIRST.getValue())
                .overtime(0)
                .timeRemaining(222)
                .clockStopped(1)
                .possession(PossessionType.HOME.getValue())
                .lastEvent("This is the last event description.")
                .build()
        gameStateRepository.save(gameState)

        when: 'a request is sent to the game state search endpoint'
        def response = restClient.get(path: '/api/game-states/search', contentType: 'application/json')

        then: 'the response should contain all of the game state fields'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size() == 1
            with(data.resources[0]) {
                gameId == 123
                started == true
                startTime == 55555
                sequence == 111
                period == "FIRST"
                overtime == false
                timeRemaining == 222
                clockStopped == true
                possession == "HOME"
                lastEvent == "This is the last event description."
            }
        }
    }
    
    def 'game state search endpoint returns all matching game state records'() {
        given: 'three game state records exist in the database'
        def gameStates = [
                GameState.builder().gameId(1).build(),
                GameState.builder().gameId(2).build(),
                GameState.builder().gameId(3).build()
        ]
        gameStateRepository.saveAll(gameStates)

        when: 'a request is sent to the game state search endpoint'
        def response = restClient.get(path: '/api/game-states/search', contentType: 'application/json')
        
        then: 'the response should contain all three game state records'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size() == 3
        }
    }
    
    def 'game state search endpoint returns an empty list when no matching game states are found'() {
        given: 'no game state records exist in the database'
        when: 'a request is sent to the game state search endpoint'
        def response = restClient.get(path: '/api/game-states/search', contentType: 'application/json')
        
        then: 'the response should contain an empty resources list'
        with(response) {
            status == 200
            data.status == 'NOT_FOUND'
            data.resources.size() == 0
        }
    }
    
    def 'game state search endpoint accepts the year game-id parameter'() {
        given: 'three game state records exist in the database'
        def gameStates = [
                GameState.builder().gameId(1).build(),
                GameState.builder().gameId(2).build(),
                GameState.builder().gameId(3).build()
        ]
        gameStateRepository.saveAll(gameStates)

        when: 'a request is sent to the game state search endpoint for game id 1'
        def response = restClient.get(path: '/api/game-states/search', contentType: 'application/json', query: ['game-id': 1])

        then: 'only the matching game state should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size() == 1
            data.resources[0].gameId == 1
        }
    }
}
