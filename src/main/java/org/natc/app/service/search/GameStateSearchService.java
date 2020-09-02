package org.natc.app.service.search;

import org.natc.app.entity.domain.GameState;
import org.natc.app.entity.request.GameStateSearchRequest;
import org.natc.app.entity.response.GameStateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameStateSearchService implements NATCSearchService<GameStateResponse, GameStateSearchRequest> {

    private final JpaRepository<GameState, Integer> repository;

    @Autowired
    public GameStateSearchService(final JpaRepository<GameState, Integer> repository) {
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
