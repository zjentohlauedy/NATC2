package org.natc.app.service.search;

import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.PlayerStatsSummary;
import org.natc.app.entity.domain.PlayerStatsSummaryId;
import org.natc.app.entity.request.PlayerStatsSummarySearchRequest;
import org.natc.app.entity.response.PlayerStatsSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerStatsSummarySearchService implements NATCSearchService<PlayerStatsSummaryResponse, PlayerStatsSummarySearchRequest> {

    private JpaRepository<PlayerStatsSummary, PlayerStatsSummaryId> repository;

    @Autowired
    public PlayerStatsSummarySearchService(final JpaRepository<PlayerStatsSummary, PlayerStatsSummaryId> repository) {
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
