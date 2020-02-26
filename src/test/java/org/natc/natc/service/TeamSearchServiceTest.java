package org.natc.natc.service;

import org.junit.jupiter.api.Test;
import org.natc.natc.entity.domain.Team;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamSearchServiceTest {

    @Test
    public void execute_shouldReturnAListOfTeams() {
        final TeamSearchService teamSearchService = new TeamSearchService();

        final List<Team> result = teamSearchService.execute();

        assertEquals(0, result.size());
    }
}