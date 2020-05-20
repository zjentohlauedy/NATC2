package org.natc.app.service;

import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.PlayerGame;
import org.natc.app.entity.request.PlayerGameSearchRequest;
import org.natc.app.entity.response.PlayerGameResponse;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerGameSearchService {

    private final JpaRepository repository;

    public PlayerGameSearchService(final JpaRepository repository) {
        this.repository = repository;
    }

    public List<PlayerGameResponse> fetchAll(final PlayerGameSearchRequest request) {
        final PlayerGame playerGame = PlayerGame.builder()
                .gameId(request.getGameId())
                .year(request.getYear())
                .datestamp(request.getDatestamp())
                .type(GameType.getValueFor(request.getType()))
                .playerId(request.getPlayerId())
                .teamId(request.getTeamId())
                .build();

        final List<PlayerGame> playerGameList = repository.findAll(Example.of(playerGame));

        return playerGameList.stream().map(PlayerGameResponse::new).collect(Collectors.toList());
    }
}
