package org.natc.natc.controller;

import org.natc.natc.entity.request.TeamSearchRequest;
import org.natc.natc.entity.response.ResponseEnvelope;
import org.natc.natc.entity.response.ResponseStatus;
import org.natc.natc.entity.response.TeamResponse;
import org.natc.natc.service.NATCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/teams")
public class TeamSearchController {

    private final NATCService<TeamResponse, TeamSearchRequest> service;

    @Autowired
    public TeamSearchController(final NATCService<TeamResponse, TeamSearchRequest> service) {
        this.service = service;
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseEnvelope<TeamResponse>> search(@RequestParam(name = "team-id", required = false) final Integer teamId,
                                                         @RequestParam(required = false) final String year,
                                                         @RequestParam(name = "conference-id", required = false) final Integer conferenceId,
                                                         @RequestParam(name = "division-id", required = false) final Integer divisionId,
                                                         @RequestParam(name = "allstar-team", required = false) final Boolean allstarTeam) {
        final TeamSearchRequest request = TeamSearchRequest.builder()
                .teamId(teamId)
                .year(year)
                .conferenceId(conferenceId)
                .divisionId(divisionId)
                .allstarTeam(allstarTeam)
                .build();

        final List<TeamResponse> teamList = service.fetchAll(request);

        final ResponseStatus status = CollectionUtils.isEmpty(teamList) ? ResponseStatus.NOT_FOUND : ResponseStatus.SUCCESS;

        return ResponseEntity.ok(new ResponseEnvelope<>(status, teamList));
    }
}