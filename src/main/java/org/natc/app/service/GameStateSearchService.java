package org.natc.app.service;

import org.natc.app.entity.domain.GameState;
import org.natc.app.entity.request.GameStateSearchRequest;
import org.natc.app.entity.response.GameStateResponse;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class GameStateSearchService implements NATCService<GameStateResponse, GameStateSearchRequest> {

    private JpaRepository repository;

    public GameStateSearchService(final JpaRepository repository) {
        this.repository = repository;
    }

    public List<GameStateResponse> fetchAll(final GameStateSearchRequest gameStateSearchRequest) {
        final GameState gameState = GameState.builder()
                .gameId(gameStateSearchRequest.getGameId())
                .build();

        final List<GameState> gameStateList = repository.findAll(Example.of(gameState));

        return gameStateList.stream().map(GameStateResponse::new).collect(Collectors.toList());
    }
}
