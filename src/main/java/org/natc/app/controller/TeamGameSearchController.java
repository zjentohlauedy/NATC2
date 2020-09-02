package org.natc.app.controller;

import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.request.TeamGameSearchRequest;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.entity.response.TeamGameResponse;
import org.natc.app.service.NATCSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/team-games")
public class TeamGameSearchController {

    private final NATCSearchService<TeamGameResponse, TeamGameSearchRequest> service;

    @Autowired
    public TeamGameSearchController(final NATCSearchService<TeamGameResponse, TeamGameSearchRequest> service) {
        this.service = service;
    }

    @RequestMapping("/search")
    public ResponseEntity<ResponseEnvelope<TeamGameResponse>> search(
            @RequestParam(name = "game-id", required = false) final Integer gameId,
            @RequestParam(required = false) final String year,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate datestamp,
            @RequestParam(required = false) final GameType type,
            @RequestParam(name = "team-id", required = false) final Integer teamId,
            @RequestParam(required = false) final Integer opponent
    ) {
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
