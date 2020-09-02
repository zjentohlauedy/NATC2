package org.natc.app.controller;

import org.natc.app.entity.request.InjurySearchRequest;
import org.natc.app.entity.response.InjuryResponse;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.service.NATCSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/injuries")
public class InjurySearchController {

    private final NATCSearchService<InjuryResponse, InjurySearchRequest> service;

    @Autowired
    public InjurySearchController(final NATCSearchService<InjuryResponse, InjurySearchRequest> service) {
        this.service = service;
    }

    @RequestMapping("/search")
    public ResponseEntity<ResponseEnvelope<InjuryResponse>> search(
            @RequestParam(name = "game-id", required = false) final Integer gameId,
            @RequestParam(name = "player-id", required = false) final Integer playerId,
            @RequestParam(name = "team-id", required = false) final Integer teamId) {
        final InjurySearchRequest request = InjurySearchRequest.builder()
                .gameId(gameId)
                .playerId(playerId)
                .teamId(teamId)
                .build();

        final List<InjuryResponse> injuryList = service.fetchAll(request);

        return ResponseEntity.ok(new ResponseEnvelope<>(injuryList));
    }
}
