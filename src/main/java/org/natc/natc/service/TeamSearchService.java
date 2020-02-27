package org.natc.natc.service;

import org.natc.natc.entity.domain.Team;
import org.natc.natc.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Boolean.TRUE;

@Service
public class TeamSearchService {

    private final TeamRepository teamRepository;

    @Autowired
    public TeamSearchService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> execute(Integer teamId, String year, Integer conferenceId, Integer divisionId, Boolean allstarTeam) {
        final Team team = Team.builder()
                .teamId(teamId)
                .year(year)
                .conference(conferenceId)
                .division(divisionId)
                .allstarTeam(mapAllstarTeamValue(allstarTeam))
                .build();

        return teamRepository.findAll(Example.of(team));
    }

    private Integer mapAllstarTeamValue(Boolean allstarTeam) {
        if (allstarTeam != null) {
            return TRUE.equals(allstarTeam) ? 1 : 0;
        }

        return null;
    }
}
