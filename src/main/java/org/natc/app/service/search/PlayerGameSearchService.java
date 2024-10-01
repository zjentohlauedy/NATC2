package org.natc.app.service.search;

import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.PlayerGame;
import org.natc.app.entity.domain.PlayerGameId;
import org.natc.app.entity.request.PlayerGameSearchRequest;
import org.natc.app.entity.response.PlayerGameResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerGameSearchService implements NATCSearchService<PlayerGameResponse, PlayerGameSearchRequest> {

    private final JpaRepository<PlayerGame, PlayerGameId> repository;

    @Autowired
    public PlayerGameSearchService(final JpaRepository<PlayerGame, PlayerGameId> repository) {
        this.repository = repository;
    }

    public List<PlayerGameResponse> fetchAll(final PlayerGameSearchRequest request) {
        final PlayerGame playerGame = PlayerGame.builder()
                .gameId(request.getGameId())
                .year(request.getYear())
                .datestamp(request.getDatestamp())
                .type(GameType.getValueFor(request.getType()))
                .playerId(request.getPlayerId())
                .teamId(request.getTeamId())
                .build();

        final List<PlayerGame> playerGameList = repository.findAll(Example.of(playerGame));

        return playerGameList.stream().map(PlayerGameResponse::new).toList();
    }
}
