package org.natc.app.service;

import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.TeamGame;
import org.natc.app.entity.request.TeamGameSearchRequest;
import org.natc.app.entity.response.TeamGameResponse;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class TeamGameSearchService implements NATCService<TeamGameResponse, TeamGameSearchRequest> {

    private final JpaRepository repository;

    public TeamGameSearchService(final JpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TeamGameResponse> fetchAll(final TeamGameSearchRequest request) {
        final TeamGame teamGame = TeamGame.builder()
                .gameId(request.getGameId())
                .year(request.getYear())
                .datestamp(request.getDatestamp())
                .type(GameType.getValueFor(request.getType()))
                .teamId(request.getTeamId())
                .opponent(request.getOpponent())
                .build();

        final List<TeamGame> teamGameList = repository.findAll(Example.of(teamGame));

        return teamGameList.stream().map(TeamGameResponse::new).collect(Collectors.toList());
    }
}
