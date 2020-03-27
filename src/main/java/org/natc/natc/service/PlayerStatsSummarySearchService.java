package org.natc.natc.service;

import org.natc.natc.entity.domain.GameType;
import org.natc.natc.entity.domain.PlayerStatsSummary;
import org.natc.natc.entity.request.PlayerStatsSummarySearchRequest;
import org.natc.natc.entity.response.PlayerStatsSummaryResponse;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerStatsSummarySearchService implements NATCService<PlayerStatsSummaryResponse, PlayerStatsSummarySearchRequest> {

    private JpaRepository repository;

    public PlayerStatsSummarySearchService(final JpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PlayerStatsSummaryResponse> fetchAll(final PlayerStatsSummarySearchRequest request) {
        final PlayerStatsSummary playerStatsSummary = PlayerStatsSummary.builder()
                .year(request.getYear())
                .type(GameType.getValueFor(request.getType()))
                .playerId(request.getPlayerId())
                .teamId(request.getTeamId())
                .build();

        final List<PlayerStatsSummary> playerStatsSummaryList = repository.findAll(Example.of(playerStatsSummary));

        return playerStatsSummaryList.stream().map(PlayerStatsSummaryResponse::new).collect(Collectors.toList());
    }
}
