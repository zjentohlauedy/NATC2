package org.natc.app.controller;

import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.request.TeamOffenseSummarySearchRequest;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.entity.response.TeamOffenseSummaryResponse;
import org.natc.app.service.NATCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/team-offense-summaries")
public class TeamOffenseSummarySearchController {

    private final NATCService<TeamOffenseSummaryResponse, TeamOffenseSummarySearchRequest> service;

    @Autowired
    public TeamOffenseSummarySearchController(final NATCService<TeamOffenseSummaryResponse, TeamOffenseSummarySearchRequest> service) {
        this.service = service;
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseEnvelope<TeamOffenseSummaryResponse>> search(
            @RequestParam(required = false) final String year,
            @RequestParam(required = false) final GameType type,
            @RequestParam(name = "team-id", required = false) final Integer teamId) {
        final TeamOffenseSummarySearchRequest request = TeamOffenseSummarySearchRequest.builder()
                .year(year)
                .type(type)
                .teamId(teamId)
                .build();

        final List<TeamOffenseSummaryResponse> teamOffenseSummaryList = service.fetchAll(request);

        return ResponseEntity.ok(new ResponseEnvelope<>(teamOffenseSummaryList));
    }
}
