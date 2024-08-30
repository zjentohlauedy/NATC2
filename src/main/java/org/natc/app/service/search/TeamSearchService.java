package org.natc.app.service.search;

import org.natc.app.entity.domain.Team;
import org.natc.app.entity.domain.TeamId;
import org.natc.app.entity.request.TeamSearchRequest;
import org.natc.app.entity.response.TeamResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;

@Service
public class TeamSearchService implements NATCSearchService<TeamResponse, TeamSearchRequest> {

    private final JpaRepository<Team, TeamId> repository;

    @Autowired
    public TeamSearchService(final JpaRepository<Team, TeamId> repository) {
        this.repository = repository;
    }

    @Override
    public List<TeamResponse> fetchAll(final TeamSearchRequest request) {
        final Team team = Team.builder()
                .teamId(request.getTeamId())
                .year(request.getYear())
                .conference(request.getConferenceId())
                .division(request.getDivisionId())
                .allstarTeam(mapAllstarTeamValue(request.getAllstarTeam()))
                .build();

        final List<Team> teamList = repository.findAll(Example.of(team));

        return teamList.stream().map(TeamResponse::new).collect(Collectors.toList());
    }

    private Integer mapAllstarTeamValue(final Boolean allstarTeam) {
        if (Objects.nonNull(allstarTeam)) {
            return TRUE.equals(allstarTeam) ? 1 : 0;
        }

        return null;
    }
}
