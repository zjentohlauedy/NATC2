package org.natc.natc.controller;

import org.natc.natc.entity.request.PlayerSearchRequest;
import org.natc.natc.entity.response.PlayerResponse;
import org.natc.natc.entity.response.ResponseEnvelope;
import org.natc.natc.entity.response.ResponseStatus;
import org.natc.natc.service.NATCService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class PlayerSearchController {

    private final NATCService<PlayerResponse, PlayerSearchRequest> playerSearchService;

    public PlayerSearchController(final NATCService<PlayerResponse, PlayerSearchRequest> playerSearchService) {
        this.playerSearchService = playerSearchService;
    }

    public ResponseEntity<ResponseEnvelope<PlayerResponse>> search(final Integer playerId, final Integer teamId, final String year) {
        final PlayerSearchRequest request = PlayerSearchRequest.builder()
                .playerId(playerId)
                .teamId(teamId)
                .year(year)
                .build();

        final List<PlayerResponse> playerList =  playerSearchService.fetchAll(request);

        final ResponseStatus status = CollectionUtils.isEmpty(playerList) ? ResponseStatus.NOT_FOUND : ResponseStatus.SUCCESS;

        return ResponseEntity.ok(new ResponseEnvelope<>(status, playerList));
    }
}
