package org.natc.natc.controller;

import org.natc.natc.entity.domain.GameType;
import org.natc.natc.entity.request.PlayerStatsSummarySearchRequest;
import org.natc.natc.entity.response.PlayerStatsSummaryResponse;
import org.natc.natc.entity.response.ResponseEnvelope;
import org.natc.natc.service.NATCService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class PlayerStatsSummarySearchController {

    private final NATCService<PlayerStatsSummaryResponse, PlayerStatsSummarySearchRequest> service;

    public PlayerStatsSummarySearchController(final NATCService<PlayerStatsSummaryResponse, PlayerStatsSummarySearchRequest> service) {
        this.service = service;
    }

    public ResponseEntity<ResponseEnvelope<PlayerStatsSummaryResponse>> search(final String year, final GameType type, final Integer playerId, final Integer teamId) {
        final PlayerStatsSummarySearchRequest request = PlayerStatsSummarySearchRequest.builder()
                .year(year)
                .type(type)
                .playerId(playerId)
                .teamId(teamId)
                .build();

        final List<PlayerStatsSummaryResponse> playerStatsSummaryList = service.fetchAll(request);

        return ResponseEntity.ok(new ResponseEnvelope<>(playerStatsSummaryList));
    }
}
