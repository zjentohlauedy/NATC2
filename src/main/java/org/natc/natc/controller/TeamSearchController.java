package org.natc.natc.controller;

import org.natc.natc.entity.domain.Team;
import org.natc.natc.entity.response.ResponseEnvelope;
import org.natc.natc.service.TeamSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public ResponseEntity<ResponseEnvelope<Team>> search() {
        final List<Team> teamList = teamSearchService.execute();

        return ResponseEntity.ok(new ResponseEnvelope<>(teamList));
    }
}
