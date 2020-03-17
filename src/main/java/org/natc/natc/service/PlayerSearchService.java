package org.natc.natc.service;

import org.natc.natc.entity.domain.Player;
import org.natc.natc.entity.domain.PlayerId;
import org.natc.natc.entity.request.PlayerSearchRequest;
import org.natc.natc.entity.response.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerSearchService implements NATCService<PlayerResponse, PlayerSearchRequest> {

    private final JpaRepository<Player, PlayerId> repository;

    @Autowired
    public PlayerSearchService(final JpaRepository<Player, PlayerId> repository) {
        this.repository = repository;
    }

    @Override
    public List<PlayerResponse> fetchAll(final PlayerSearchRequest request) {
        final Player player = Player.builder()
                .playerId(request.getPlayerId())
                .teamId(request.getTeamId())
                .year(request.getYear())
                .build();

        final List<Player> playerList = repository.findAll(Example.of(player));

        return playerList.stream().map(PlayerResponse::new).collect(Collectors.toList());
    }
}
