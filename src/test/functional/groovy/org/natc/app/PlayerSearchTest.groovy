package org.natc.app

import org.natc.app.entity.domain.Player
import org.natc.app.entity.domain.PlayerAward
import org.natc.app.repository.PlayerRepository
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDate

class PlayerSearchTest extends NATCFunctionalTest {

    @Autowired
    PlayerRepository playerRepository;

    def setup() {
        playerRepository.deleteAll()
    }

    def 'player search endpoint returns player data'() {
        given: 'a player exists in the database'
        def player = Player.builder().playerId(1).year('2020').build()
        playerRepository.save(player)

        when: 'a request is sent to the player search endpoint'
        def response = restClient.get(path: '/api/players/search', contentType: 'application/json')

        then: 'the response should contain the player'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resource == null
            data.errors == null
            data.resources.size == 1
            data.resources[0].playerId == 1
            data.resources[0].year == '2020'
        }
    }

    def 'player search endpoint provides all player fields'() {
        given: 'a player exists in the database'
        def dateOfReturn = LocalDate.now()
        def player = Player.builder()
                .playerId(123)
                .teamId(321)
                .year("1991")
                .firstName("John")
                .lastName("Doe")
                .age(26)
                .scoring(0.101)
                .passing(0.202)
                .blocking(0.303)
                .tackling(0.404)
                .stealing(0.505)
                .presence(0.606)
                .discipline(0.707)
                .penaltyShot(0.808)
                .penaltyOffense(0.909)
                .penaltyDefense(1.010)
                .endurance(1.111)
                .confidence(1.212)
                .vitality(1.313)
                .durability(1.414)
                .rookie(1)
                .injured(0)
                .returnDate(dateOfReturn)
                .freeAgent(1)
                .signed(0)
                .released(1)
                .retired(0)
                .formerTeamId(111)
                .allstarAlternate(1)
                .award(PlayerAward.GOLD.getValue())
                .draftPick(444)
                .seasonsPlayed(555)
                .allstarTeamId(1)
                .build()

        playerRepository.save(player)

        when: 'a request is sent to the player search endpoint'
        def response = restClient.get(path: '/api/players/search', contentType: 'application/json')

        then: 'the response should contain all of the player fields'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            with(data.resources[0]) {
                playerId == 123
                teamId == 321
                year == "1991"
                firstName == "John"
                lastName == "Doe"
                age == 26
                scoring == 0.101
                passing == 0.202
                blocking == 0.303
                tackling == 0.404
                stealing == 0.505
                presence == 0.606
                discipline == 0.707
                penaltyShot == 0.808
                penaltyOffense == 0.909
                penaltyDefense == 1.010
                endurance == 1.111
                confidence == 1.212
                vitality == 1.313
                durability == 1.414
                rookie == true
                injured == false
                returnDate == dateOfReturn.toString()
                freeAgent == true
                signed == false
                released == true
                retired == false
                formerTeamId == 111
                allstarAlternate == true
                award == 'GOLD'
                draftPick == 444
                seasonsPlayed == 555
                allstarTeamId == 1
            }
        }
    }

    def 'player search endpoint returns all matching players'() {
        given: 'three players exist in the database'
        def players = [
                Player.builder().playerId(1).year('2020').build(),
                Player.builder().playerId(2).year('2020').build(),
                Player.builder().playerId(3).year('2020').build()
        ]

        playerRepository.saveAll(players)

        when: 'a request is sent to the player search endpoint'
        def response = restClient.get(path: '/api/players/search', contentType: 'application/json')

        then: 'the response should contain all three players'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 3

            data.resources.collect { it.playerId }.containsAll([1,2,3])
        }
    }

    def 'player search endpoint returns an empty list when no matching players are found'() {
        given: 'three players exist in the database'
        def players = [
                Player.builder().playerId(1).year('2020').build(),
                Player.builder().playerId(2).year('2020').build(),
                Player.builder().playerId(3).year('2020').build()
        ]

        playerRepository.saveAll(players)

        when: 'a request is sent to the player search endpoint for player id 4'
        def response = restClient.get(path: '/api/players/search', contentType: 'application/json', query: ['player-id': 4])

        then: 'the response should contain an empty resources list'
        with(response) {
            status == 200
            data.status == 'NOT_FOUND'
            data.resources.size == 0
        }
    }

    def 'player search endpoint accepts the player-id query parameter'() {
        given: 'three players exist in the database'
        def players = [
                Player.builder().playerId(1).year('2020').teamId(4).build(),
                Player.builder().playerId(2).year('2021').teamId(5).build(),
                Player.builder().playerId(3).year('2022').teamId(6).build()
        ]

        playerRepository.saveAll(players)

        when: 'a request is sent to the player search endpoint for player id 1'
        def response = restClient.get(path: '/api/players/search', contentType: 'application/json', query: ['player-id': 1])

        then: 'only the matching player should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].playerId == 1
        }
    }

    def 'player search endpoint accepts the year query parameter'() {
        given: 'three players exist in the database'
        def players = [
                Player.builder().playerId(1).year('2020').teamId(4).build(),
                Player.builder().playerId(2).year('2021').teamId(5).build(),
                Player.builder().playerId(3).year('2022').teamId(6).build()
        ]

        playerRepository.saveAll(players)

        when: 'a request is sent to the player search endpoint for year 2021'
        def response = restClient.get(path: '/api/players/search', contentType: 'application/json', query: ['year': '2021'])

        then: 'only the matching player should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].year == '2021'
        }
    }

    def 'player search endpoint accepts the team-id query parameter'() {
        given: 'three players exist in the database'
        def players = [
                Player.builder().playerId(1).year('2020').teamId(4).build(),
                Player.builder().playerId(2).year('2021').teamId(5).build(),
                Player.builder().playerId(3).year('2022').teamId(6).build()
        ]

        playerRepository.saveAll(players)

        when: 'a request is sent to the player search endpoint for team id 6'
        def response = restClient.get(path: '/api/players/search', contentType: 'application/json', query: ['team-id': 6])

        then: 'only the matching player should be returned'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resources.size == 1
            data.resources[0].teamId == 6
        }
    }
}
