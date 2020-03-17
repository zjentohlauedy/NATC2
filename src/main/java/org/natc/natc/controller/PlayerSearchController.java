package org.natc.natc.controller;

import org.natc.natc.entity.request.PlayerSearchRequest;
import org.natc.natc.entity.response.PlayerResponse;
import org.natc.natc.entity.response.ResponseEnvelope;
import org.natc.natc.service.NATCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/players")
public class PlayerSearchController {

    private final NATCService<PlayerResponse, PlayerSearchRequest> service;

    @Autowired
    public PlayerSearchController(final NATCService<PlayerResponse, PlayerSearchRequest> service) {
        this.service = service;
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseEnvelope<PlayerResponse>> search(@RequestParam(name = "player-id", required = false) final Integer playerId,
                                                                   @RequestParam(name = "team-id", required = false) final Integer teamId,
                                                                   @RequestParam(required = false) final String year) {
        final PlayerSearchRequest request = PlayerSearchRequest.builder()
                .playerId(playerId)
                .teamId(teamId)
                .year(year)
                .build();

        final List<PlayerResponse> playerList =  service.fetchAll(request);

        return ResponseEntity.ok(new ResponseEnvelope<>(playerList));
    }
}
