package org.natc.natc.controller;

import org.natc.natc.entity.domain.Team;
import org.natc.natc.entity.response.ResponseEnvelope;
import org.natc.natc.service.TeamSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/teams")
public class TeamSearchController {

    private final TeamSearchService teamSearchService;

    @Autowired
    public TeamSearchController(TeamSearchService teamSearchService) {
        this.teamSearchService = teamSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseEnvelope<Team>> search(@RequestParam(name = "team-id", required = false) Integer teamId,
                                                         @RequestParam(required = false) String year,
                                                         @RequestParam(name = "conference-id", required = false) Integer conferenceId,
                                                         @RequestParam(name = "division-id", required = false) Integer divisionId,
                                                         @RequestParam(name = "allstar-team", required = false) Boolean allstarTeam) {
        final List<Team> teamList = teamSearchService.execute(teamId, year, conferenceId, divisionId, allstarTeam);

        return ResponseEntity.ok(new ResponseEnvelope<>(teamList));
    }
}
