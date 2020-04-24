package org.natc.app.controller;

import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.request.TeamGameSearchRequest;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.entity.response.TeamGameResponse;
import org.natc.app.service.NATCService;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public class TeamGameSearchController {

    private NATCService<TeamGameResponse, TeamGameSearchRequest> service;

    public TeamGameSearchController(final NATCService<TeamGameResponse, TeamGameSearchRequest> service) {
        this.service = service;
    }

    public ResponseEntity<ResponseEnvelope<TeamGameResponse>> search(final Integer gameId, final String year, final LocalDate datestamp, final GameType type, final Integer teamId, final Integer opponent) {
        final TeamGameSearchRequest request = TeamGameSearchRequest.builder()
                .gameId(gameId)
                .year(year)
                .datestamp(datestamp)
                .type(type)
                .teamId(teamId)
                .opponent(opponent)
                .build();

        final List<TeamGameResponse> teamGameList = service.fetchAll(request);

        return ResponseEntity.ok(new ResponseEnvelope<>(teamGameList));
    }
}
