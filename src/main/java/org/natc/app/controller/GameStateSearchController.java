package org.natc.app.controller;

import org.natc.app.entity.request.GameStateSearchRequest;
import org.natc.app.entity.response.GameStateResponse;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.service.search.NATCSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/game-states")
public class GameStateSearchController {

    private final NATCSearchService<GameStateResponse, GameStateSearchRequest> service;

    @Autowired
    public GameStateSearchController(final NATCSearchService<GameStateResponse, GameStateSearchRequest> service) {
        this.service = service;
    }

    @RequestMapping("/search")
    public ResponseEntity<ResponseEnvelope<GameStateResponse>> search(
            @RequestParam(name = "game-id", required = false) final Integer gameId) {
        // TODO: Add MCD request id to response
        //  final String requestId = MDC.get(REQUEST_ID_KEY);

        final GameStateSearchRequest request = GameStateSearchRequest.builder()
                .gameId(gameId)
                .build();

        final List<GameStateResponse> gameStateList = service.fetchAll(request);

        return ResponseEntity.ok(new ResponseEnvelope<>(gameStateList));
    }
}
