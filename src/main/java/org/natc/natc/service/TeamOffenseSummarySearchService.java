package org.natc.natc.service;

import org.natc.natc.entity.domain.GameType;
import org.natc.natc.entity.domain.TeamOffenseSummary;
import org.natc.natc.entity.domain.TeamOffenseSummaryId;
import org.natc.natc.entity.request.TeamOffenseSummaryRequest;
import org.natc.natc.entity.response.TeamOffenseSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamOffenseSummarySearchService implements NATCService<TeamOffenseSummaryResponse, TeamOffenseSummaryRequest> {

    private final JpaRepository<TeamOffenseSummary, TeamOffenseSummaryId> repository;

    @Autowired
    public TeamOffenseSummarySearchService(final JpaRepository<TeamOffenseSummary, TeamOffenseSummaryId> repository) {
        this.repository = repository;
    }

    @Override
    public List<TeamOffenseSummaryResponse> fetchAll(final TeamOffenseSummaryRequest request) {
        final TeamOffenseSummary teamOffenseSummary = TeamOffenseSummary.builder()
                .year(request.getYear())
                .type(GameType.getValueFor(request.getType()))
                .teamId(request.getTeamId())
                .build();

        final List<TeamOffenseSummary> teamOffenseSummaryList = repository.findAll(Example.of(teamOffenseSummary));

        return teamOffenseSummaryList.stream().map(TeamOffenseSummaryResponse::new).collect(Collectors.toList());
    }
}
