package org.natc.natc.controller;

import org.natc.natc.entity.domain.Team;
import org.natc.natc.entity.response.ResponseEnvelope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/teams")
public class TeamSearchController {

    @GetMapping("/search")
    public ResponseEntity<ResponseEnvelope<Team>> search() {
        return ResponseEntity.ok(new ResponseEnvelope<>());
    }
}
