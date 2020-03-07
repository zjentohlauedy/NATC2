package org.natc.natc.service;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.natc.natc.entity.domain.Team;
import org.natc.natc.entity.request.TeamSearchRequest;
import org.natc.natc.entity.response.TeamResponse;
import org.natc.natc.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureEmbeddedDatabase
class TeamSearchServiceIntegrationTest {

    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private TeamSearchService teamSearchService;
    
    @Test
    public void shouldReturnATeamFromTheDatabaseMappedToAResponse() {
        final Team team = Team.builder()
                .teamId(1)
                .year("1994")
                .location("San Francisco")
                .name("Warriors")
                .abbreviation("S.F.")
                .build();

        teamRepository.save(team);

        final List<TeamResponse> result = teamSearchService.fetchAll(new TeamSearchRequest());

        assertEquals(1, result.size());

        final TeamResponse response = result.get(0);

        assertEquals(team.getTeamId(), response.getTeamId());
        assertEquals(team.getYear(), response.getYear());
        assertEquals(team.getLocation(), response.getLocation());
        assertEquals(team.getName(), response.getName());
        assertEquals(team.getAbbreviation(), response.getAbbreviation());
    }
}