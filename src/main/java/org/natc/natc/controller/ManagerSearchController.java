package org.natc.natc.controller;

import org.natc.natc.entity.request.ManagerSearchRequest;
import org.natc.natc.entity.response.ManagerResponse;
import org.natc.natc.entity.response.ResponseEnvelope;
import org.natc.natc.service.NATCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/managers")
public class ManagerSearchController {

    private final NATCService<ManagerResponse, ManagerSearchRequest> service;

    @Autowired
    public ManagerSearchController(final NATCService<ManagerResponse, ManagerSearchRequest> service) {
        this.service = service;
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseEnvelope<ManagerResponse>> search(@RequestParam(name = "manager-id", required = false) final Integer managerId,
                                                                    @RequestParam(name = "team-id", required = false) final Integer teamId,
                                                                    @RequestParam(name = "player-id", required = false) final Integer playerId,
                                                                    @RequestParam(required = false) final String year) {
        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(managerId)
                .teamId(teamId)
                .playerId(playerId)
                .year(year)
                .build();

        final List<ManagerResponse> managerList = service.fetchAll(request);

        return ResponseEntity.ok(new ResponseEnvelope<>(managerList));
    }
}
