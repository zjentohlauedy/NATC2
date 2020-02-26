package org.natc.natc.controller;

import org.junit.jupiter.api.Test;
import org.natc.natc.entity.domain.Team;
import org.natc.natc.entity.response.ResponseEnvelope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class TeamSearchControllerTest {
    @Test
    public void search_ShouldReturnOKResponse() {
        final TeamSearchController teamSearchController = new TeamSearchController();

        final ResponseEntity<ResponseEnvelope<Team>> response = teamSearchController.search();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}