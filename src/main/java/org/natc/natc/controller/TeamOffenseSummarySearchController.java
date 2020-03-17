package org.natc.natc.controller;

import org.natc.natc.entity.request.TeamOffenseSummaryRequest;
import org.natc.natc.entity.response.ResponseEnvelope;
import org.natc.natc.entity.response.TeamOffenseSummaryResponse;
import org.natc.natc.service.NATCService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class TeamOffenseSummarySearchController {

    private final NATCService<TeamOffenseSummaryResponse, TeamOffenseSummaryRequest> service;

    public TeamOffenseSummarySearchController(final NATCService<TeamOffenseSummaryResponse, TeamOffenseSummaryRequest> service) {
        this.service = service;
    }

    public ResponseEntity<ResponseEnvelope<TeamOffenseSummaryResponse>> search(final String year, final Integer teamId) {
        final TeamOffenseSummaryRequest request = TeamOffenseSummaryRequest.builder().year(year).teamId(teamId).build();

        final List<TeamOffenseSummaryResponse> teamOffenseSummaryList = service.fetchAll(request);

        return ResponseEntity.ok(new ResponseEnvelope<>(teamOffenseSummaryList));
    }
}
