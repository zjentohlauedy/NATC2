package org.natc.natc.service;

import org.natc.natc.entity.domain.TeamOffenseSummary;
import org.natc.natc.entity.request.TeamOffenseSummaryRequest;
import org.natc.natc.entity.response.TeamOffenseSummaryResponse;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class TeamOffenseSummarySearchService implements NATCService<TeamOffenseSummaryResponse, TeamOffenseSummaryRequest> {

    private final JpaRepository repository;

    public TeamOffenseSummarySearchService(final JpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TeamOffenseSummaryResponse> fetchAll(final TeamOffenseSummaryRequest request) {
        final TeamOffenseSummary teamOffenseSummary = TeamOffenseSummary.builder()
                .year(request.getYear())
                .teamId(request.getTeamId())
                .build();

        final List<TeamOffenseSummary> teamOffenseSummaryList = repository.findAll(Example.of(teamOffenseSummary));

        return teamOffenseSummaryList.stream().map(TeamOffenseSummaryResponse::new).collect(Collectors.toList());
    }
}
