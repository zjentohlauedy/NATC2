package org.natc.natc.service;

import org.junit.jupiter.api.Test;
import org.natc.natc.entity.domain.Player;
import org.natc.natc.entity.request.PlayerSearchRequest;
import org.natc.natc.entity.response.PlayerResponse;
import org.natc.natc.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

class PlayerSearchServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerSearchService playerSearchService;

    @Test
    public void shouldReturnAPlayerFromTheDatabaseMappedToAResponse() {
        final Player player = Player.builder()
                .playerId(1)
                .year("2020")
                .firstName("John")
                .lastName("Doe")
                .age(23)
                .build();

        playerRepository.save(player);

        final List<PlayerResponse> result = playerSearchService.fetchAll(new PlayerSearchRequest());

        assertEquals(1, result.size());

        final PlayerResponse response = result.get(0);

        assertEquals(player.getPlayerId(), response.getPlayerId());
        assertEquals(player.getYear(), response.getYear());
        assertEquals(player.getFirstName(), response.getFirstName());
        assertEquals(player.getLastName(), response.getLastName());
        assertEquals(player.getAge(), response.getAge());
    }
}