package org.natc.app.controller;

import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.request.PlayerGameSearchRequest;
import org.natc.app.entity.response.PlayerGameResponse;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.service.NATCService;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public class PlayerGameSearchController {

    private final NATCService<PlayerGameResponse, PlayerGameSearchRequest> service;

    public PlayerGameSearchController(final NATCService<PlayerGameResponse, PlayerGameSearchRequest> service) {
        this.service = service;
    }

    public ResponseEntity<ResponseEnvelope<PlayerGameResponse>> search(final Integer gameId,
                                                                       final String year,
                                                                       final LocalDate datestamp,
                                                                       final GameType type,
                                                                       final Integer playerId,
                                                                       final Integer teamId) {
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
