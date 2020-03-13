package org.natc.natc.controller;

import org.natc.natc.entity.request.PlayerSearchRequest;
import org.natc.natc.entity.response.PlayerResponse;
import org.natc.natc.entity.response.ResponseEnvelope;
import org.natc.natc.entity.response.ResponseStatus;
import org.natc.natc.service.NATCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/players")
public class PlayerSearchController {

    private final NATCService<PlayerResponse, PlayerSearchRequest> playerSearchService;

    @Autowired
    public PlayerSearchController(final NATCService<PlayerResponse, PlayerSearchRequest> playerSearchService) {
        this.playerSearchService = playerSearchService;
    }

    @GetMapping("/search")
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
