package org.natc.natc.service;

import org.natc.natc.entity.domain.Team;
import org.natc.natc.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamSearchService {

    private final TeamRepository teamRepository;

    @Autowired
    public TeamSearchService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> execute(Integer teamId, String year) {
        final Team team = Team.builder().teamId(teamId).year(year).build();

        return teamRepository.findAll(Example.of(team));
    }
}
