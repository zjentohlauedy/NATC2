package org.natc.app.controller;

import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.request.PlayerGameSearchRequest;
import org.natc.app.entity.response.PlayerGameResponse;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.service.search.NATCSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/player-games")
public class PlayerGameSearchController {

    private final NATCSearchService<PlayerGameResponse, PlayerGameSearchRequest> service;

    @Autowired
    public PlayerGameSearchController(final NATCSearchService<PlayerGameResponse, PlayerGameSearchRequest> service) {
        this.service = service;
    }

    @RequestMapping("/search")
    public ResponseEntity<ResponseEnvelope<PlayerGameResponse>> search(
            @RequestParam(name = "game-id", required = false) final Integer gameId,
            @RequestParam(required = false) final String year,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate datestamp,
            @RequestParam(required = false) final GameType type,
            @RequestParam(name = "player-id", required = false) final Integer playerId,
            @RequestParam(name = "team-id", required = false) final Integer teamId) {
        final PlayerGameSearchRequest request = PlayerGameSearchRequest.builder()
                .gameId(gameId)
                .year(year)
                .datestamp(datestamp)
                .type(type)
                .playerId(playerId)
                .teamId(teamId)
                .build();

        final List<PlayerGameResponse> playerGameList = service.fetchAll(request);

        return ResponseEntity.ok(new ResponseEnvelope<>(playerGameList));
    }
}
