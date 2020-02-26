package org.natc.natc.repository;

import org.natc.natc.entity.domain.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeamRepository extends CrudRepository<Team, Integer> {
    List<Team> findAll();
}
