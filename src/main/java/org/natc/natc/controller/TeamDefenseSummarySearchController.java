package org.natc.natc.controller;

import org.natc.natc.entity.domain.GameType;
import org.natc.natc.entity.request.TeamDefenseSummaryRequest;
import org.natc.natc.entity.response.ResponseEnvelope;
import org.natc.natc.entity.response.TeamDefenseSummaryResponse;
import org.natc.natc.service.NATCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/team-defense-summaries")
public class TeamDefenseSummarySearchController {

    private final NATCService<TeamDefenseSummaryResponse, TeamDefenseSummaryRequest> searchService;

    @Autowired
    public TeamDefenseSummarySearchController(final NATCService<TeamDefenseSummaryResponse, TeamDefenseSummaryRequest> searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseEnvelope<TeamDefenseSummaryResponse>> search(
            @RequestParam(required = false) final String year,
            @RequestParam(required = false) final GameType type,
            @RequestParam(name = "team-id", required = false) final Integer teamId) {
        final TeamDefenseSummaryRequest request = TeamDefenseSummaryRequest.builder()
                .year(year)
                .type(type)
                .teamId(teamId)
                .build();

        final List<TeamDefenseSummaryResponse> teamOffenseSummaryList = searchService.fetchAll(request);

        return ResponseEntity.ok(new ResponseEnvelope<>(teamOffenseSummaryList));
    }
}
