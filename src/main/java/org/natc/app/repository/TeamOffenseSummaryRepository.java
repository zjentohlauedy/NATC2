package org.natc.app.repository;

import org.natc.app.entity.domain.TeamOffenseSummary;
import org.natc.app.entity.domain.TeamOffenseSummaryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamOffenseSummaryRepository extends JpaRepository<TeamOffenseSummary, TeamOffenseSummaryId> {
}
