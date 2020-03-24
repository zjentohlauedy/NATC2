package org.natc.natc.service;

import org.natc.natc.entity.domain.GameType;
import org.natc.natc.entity.domain.TeamDefenseSummary;
import org.natc.natc.entity.request.TeamDefenseSummaryRequest;
import org.natc.natc.entity.response.TeamDefenseSummaryResponse;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class TeamDefenseSummarySearchService implements NATCService<TeamDefenseSummaryResponse, TeamDefenseSummaryRequest> {

    private final JpaRepository repository;

    public TeamDefenseSummarySearchService(final JpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TeamDefenseSummaryResponse> fetchAll(final TeamDefenseSummaryRequest request) {
        final TeamDefenseSummary teamDefenseSummary = TeamDefenseSummary.builder()
                .year(request.getYear())
                .type(GameType.getValueFor(request.getType()))
                .teamId(request.getTeamId())
                .build();

        final List<TeamDefenseSummary> teamDefenseSummaryList = repository.findAll(Example.of(teamDefenseSummary));

        return teamDefenseSummaryList.stream().map(TeamDefenseSummaryResponse::new).collect(Collectors.toList());
    }
}
