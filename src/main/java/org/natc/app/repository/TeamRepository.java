package org.natc.app.repository;

import org.natc.app.entity.domain.Team;
import org.natc.app.entity.domain.TeamId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, TeamId> {
}
