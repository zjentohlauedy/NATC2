package org.natc.natc.controller;

import org.natc.natc.entity.domain.GameType;
import org.natc.natc.entity.request.TeamDefenseSummaryRequest;
import org.natc.natc.entity.response.ResponseEnvelope;
import org.natc.natc.entity.response.TeamDefenseSummaryResponse;
import org.natc.natc.service.NATCService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class TeamDefenseSummarySearchController {

    private final NATCService<TeamDefenseSummaryResponse, TeamDefenseSummaryRequest> searchService;

    public TeamDefenseSummarySearchController(final NATCService<TeamDefenseSummaryResponse, TeamDefenseSummaryRequest> searchService) {
        this.searchService = searchService;
    }

    public ResponseEntity<ResponseEnvelope<TeamDefenseSummaryResponse>> search(final String year, final GameType type, final Integer teamId) {
        final TeamDefenseSummaryRequest request = TeamDefenseSummaryRequest.builder()
                .year(year)
                .type(type)
                .teamId(teamId)
                .build();

        final List<TeamDefenseSummaryResponse> teamOffenseSummaryList = searchService.fetchAll(request);

        return ResponseEntity.ok(new ResponseEnvelope<>(teamOffenseSummaryList));
    }
}
