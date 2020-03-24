package org.natc.natc.service;

import org.natc.natc.entity.domain.GameType;
import org.natc.natc.entity.domain.TeamDefenseSummary;
import org.natc.natc.entity.domain.TeamDefenseSummaryId;
import org.natc.natc.entity.request.TeamDefenseSummaryRequest;
import org.natc.natc.entity.response.TeamDefenseSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamDefenseSummarySearchService implements NATCService<TeamDefenseSummaryResponse, TeamDefenseSummaryRequest> {

    private final JpaRepository<TeamDefenseSummary, TeamDefenseSummaryId> repository;

    @Autowired
    public TeamDefenseSummarySearchService(final JpaRepository<TeamDefenseSummary, TeamDefenseSummaryId> repository) {
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
