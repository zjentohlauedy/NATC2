package org.natc.natc.service;

import org.junit.jupiter.api.Test;
import org.natc.natc.entity.domain.GameType;
import org.natc.natc.entity.domain.TeamOffenseSummary;
import org.natc.natc.entity.request.TeamOffenseSummaryRequest;
import org.natc.natc.entity.response.TeamOffenseSummaryResponse;
import org.natc.natc.repository.TeamOffenseSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamOffenseSummarySearchServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private TeamOffenseSummaryRepository repository;

    @Autowired
    private TeamOffenseSummarySearchService searchService;

    @Test
    public void shouldReturnATeamOffenseSummaryFromTheDatabaseMappedToAResponse() {
        final TeamOffenseSummary teamOffenseSummary = TeamOffenseSummary.builder()
                .year("2004")
                .teamId(16)
                .type(GameType.REGULAR_SEASON.getValue())
                .games(100)
                .attempts(3000)
                .goals(1200)
                .score(4444)
                .build();

        repository.save(teamOffenseSummary);

        final List<TeamOffenseSummaryResponse> result = searchService.fetchAll(new TeamOffenseSummaryRequest());

        assertEquals(1, result.size());

        final TeamOffenseSummaryResponse response = result.get(0);

        assertEquals(teamOffenseSummary.getYear(), response.getYear());
        assertEquals(teamOffenseSummary.getTeamId(), response.getTeamId());
        assertEquals(GameType.REGULAR_SEASON, response.getType());
        assertEquals(teamOffenseSummary.getGames(), response.getGames());
        assertEquals(teamOffenseSummary.getAttempts(), response.getAttempts());
        assertEquals(teamOffenseSummary.getGoals(), response.getGoals());
        assertEquals(teamOffenseSummary.getScore(), response.getScore());
    }
}