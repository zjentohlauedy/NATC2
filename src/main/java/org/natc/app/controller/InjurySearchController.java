package org.natc.app.controller;

import org.natc.app.entity.request.InjurySearchRequest;
import org.natc.app.entity.response.InjuryResponse;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.service.NATCService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class InjurySearchController {

    private final NATCService<InjuryResponse, InjurySearchRequest> service;

    public InjurySearchController(final NATCService<InjuryResponse, InjurySearchRequest> service) {
        this.service = service;
    }

    public ResponseEntity<ResponseEnvelope<InjuryResponse>> search(final Integer gameId, final Integer playerId, final Integer teamId) {
        final InjurySearchRequest request = InjurySearchRequest.builder()
                .gameId(gameId)
                .playerId(playerId)
                .teamId(teamId)
                .build();

        final List<InjuryResponse> injuryList = service.fetchAll(request);

        return ResponseEntity.ok(new ResponseEnvelope<>(injuryList));
    }
}
