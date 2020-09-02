package org.natc.app.controller;

import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.request.PlayerStatsSummarySearchRequest;
import org.natc.app.entity.response.PlayerStatsSummaryResponse;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.service.NATCSearchService;
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

    private final NATCSearchService<PlayerStatsSummaryResponse, PlayerStatsSummarySearchRequest> service;

    @Autowired
    public PlayerStatsSummarySearchController(final NATCSearchService<PlayerStatsSummaryResponse, PlayerStatsSummarySearchRequest> service) {
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
