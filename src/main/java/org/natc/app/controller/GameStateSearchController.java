package org.natc.app.controller;

import org.natc.app.entity.request.GameStateSearchRequest;
import org.natc.app.entity.response.GameStateResponse;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.service.NATCService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class GameStateSearchController {

    private final NATCService<GameStateResponse, GameStateSearchRequest> service;

    public GameStateSearchController(final NATCService<GameStateResponse, GameStateSearchRequest> service) {
        this.service = service;
    }

    public ResponseEntity<ResponseEnvelope<GameStateResponse>> search(final Integer gameId) {
        final GameStateSearchRequest request = GameStateSearchRequest.builder()
                .gameId(gameId)
                .build();

        final List<GameStateResponse> gameStateList = service.fetchAll(request);

        return ResponseEntity.ok(new ResponseEnvelope<>(gameStateList));
    }
}
