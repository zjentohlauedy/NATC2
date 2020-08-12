package org.natc.app.service;

import org.natc.app.entity.domain.Injury;
import org.natc.app.entity.request.InjurySearchRequest;
import org.natc.app.entity.response.InjuryResponse;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class InjurySearchService {

    private final JpaRepository repository;

    public InjurySearchService(final JpaRepository repository) {
        this.repository = repository;
    }

    public List<InjuryResponse> fetchAll(final InjurySearchRequest request) {
        final Injury injury = Injury.builder()
                .gameId(request.getGameId())
                .playerId(request.getPlayerId())
                .teamId(request.getTeamId())
                .build();

        final List<Injury> injuryList = repository.findAll(Example.of(injury));

        return injuryList.stream().map(InjuryResponse::new).collect(Collectors.toList());
    }
}
