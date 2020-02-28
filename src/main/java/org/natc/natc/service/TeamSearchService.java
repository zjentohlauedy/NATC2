package org.natc.natc.service;

import org.natc.natc.entity.domain.Team;
import org.natc.natc.entity.request.TeamSearchRequest;
import org.natc.natc.entity.response.TeamResponse;
import org.natc.natc.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;

@Service
public class TeamSearchService {

    private final TeamRepository teamRepository;

    @Autowired
    public TeamSearchService(final TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<TeamResponse> execute(final TeamSearchRequest request) {
        final Team team = Team.builder()
                .teamId(request.getTeamId())
                .year(request.getYear())
                .conference(request.getConferenceId())
                .division(request.getDivisionId())
                .allstarTeam(mapAllstarTeamValue(request.getAllstarTeam()))
                .build();

        final List<Team> teamList = teamRepository.findAll(Example.of(team));

        return teamList.stream().map(TeamResponse::new).collect(Collectors.toList());
    }

    private Integer mapAllstarTeamValue(final Boolean allstarTeam) {
        if (allstarTeam != null) {
            return TRUE.equals(allstarTeam) ? 1 : 0;
        }

        return null;
    }
}
