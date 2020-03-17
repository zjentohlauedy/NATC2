package org.natc.natc.service;

import org.natc.natc.entity.domain.Manager;
import org.natc.natc.entity.domain.ManagerId;
import org.natc.natc.entity.request.ManagerSearchRequest;
import org.natc.natc.entity.response.ManagerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerSearchService implements NATCService<ManagerResponse, ManagerSearchRequest> {

    private final JpaRepository<Manager, ManagerId> repository;

    @Autowired
    public ManagerSearchService(final JpaRepository<Manager, ManagerId> repository) {
        this.repository = repository;
    }

    @Override
    public List<ManagerResponse> fetchAll(final ManagerSearchRequest request) {
        final Manager manager = Manager.builder()
                .managerId(request.getManagerId())
                .teamId(request.getTeamId())
                .playerId(request.getPlayerId())
                .year(request.getYear())
                .build();

        final List<Manager> managerList = repository.findAll(Example.of(manager));

        return managerList.stream().map(ManagerResponse::new).collect(Collectors.toList());
    }
}
