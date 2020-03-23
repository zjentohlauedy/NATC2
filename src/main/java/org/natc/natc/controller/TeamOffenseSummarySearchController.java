package org.natc.natc.controller;

import org.natc.natc.entity.domain.GameType;
import org.natc.natc.entity.request.TeamOffenseSummaryRequest;
import org.natc.natc.entity.response.ResponseEnvelope;
import org.natc.natc.entity.response.TeamOffenseSummaryResponse;
import org.natc.natc.service.NATCService;
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

    private final NATCService<TeamOffenseSummaryResponse, TeamOffenseSummaryRequest> service;

    @Autowired
    public TeamOffenseSummarySearchController(final NATCService<TeamOffenseSummaryResponse, TeamOffenseSummaryRequest> service) {
        this.service = service;
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseEnvelope<TeamOffenseSummaryResponse>> search(
            @RequestParam(required = false) final String year,
            @RequestParam(required = false) final GameType type,
            @RequestParam(name = "team-id", required = false) final Integer teamId) {
        final TeamOffenseSummaryRequest request = TeamOffenseSummaryRequest.builder()
                .year(year)
                .type(type)
                .teamId(teamId)
                .build();

        final List<TeamOffenseSummaryResponse> teamOffenseSummaryList = service.fetchAll(request);

        return ResponseEntity.ok(new ResponseEnvelope<>(teamOffenseSummaryList));
    }
}
