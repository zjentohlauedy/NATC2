package org.natc.natc.repository;

import org.natc.natc.entity.domain.Team;
import org.natc.natc.entity.domain.TeamId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, TeamId> {
}
