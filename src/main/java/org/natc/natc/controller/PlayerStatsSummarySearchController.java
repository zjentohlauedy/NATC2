package org.natc.natc.controller;

import org.natc.natc.entity.domain.GameType;
import org.natc.natc.entity.request.PlayerStatsSummarySearchRequest;
import org.natc.natc.entity.response.PlayerStatsSummaryResponse;
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
@RequestMapping("/api/player-stats-summaries")
public class PlayerStatsSummarySearchController {

    private final NATCService<PlayerStatsSummaryResponse, PlayerStatsSummarySearchRequest> service;

    @Autowired
    public PlayerStatsSummarySearchController(final NATCService<PlayerStatsSummaryResponse, PlayerStatsSummarySearchRequest> service) {
        this.service = service;
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseEnvelope<PlayerStatsSummaryResponse>> search(
            @RequestParam(required = false) final String year,
            @RequestParam(required = false) final GameType type,
            @RequestParam(name = "player-id", required = false) final Integer playerId,
            @RequestParam(name = "team-id", required = false) final Integer teamId) {
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
