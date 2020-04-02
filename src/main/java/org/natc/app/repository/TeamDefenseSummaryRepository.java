package org.natc.app.repository;

import org.natc.app.entity.domain.TeamDefenseSummary;
import org.natc.app.entity.domain.TeamDefenseSummaryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamDefenseSummaryRepository extends JpaRepository<TeamDefenseSummary, TeamDefenseSummaryId> {
}
