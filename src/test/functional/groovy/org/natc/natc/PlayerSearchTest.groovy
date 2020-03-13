package org.natc.natc

import org.natc.natc.entity.domain.Player
import org.natc.natc.repository.PlayerRepository
import org.springframework.beans.factory.annotation.Autowired

class PlayerSearchTest extends NATCFunctionalTest {

    @Autowired
    private PlayerRepository playerRepository;

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
}
