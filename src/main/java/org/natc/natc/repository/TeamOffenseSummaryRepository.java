package org.natc.natc.repository;

import org.natc.natc.entity.domain.TeamOffenseSummary;
import org.natc.natc.entity.domain.TeamOffenseSummaryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamOffenseSummaryRepository extends JpaRepository<TeamOffenseSummary, TeamOffenseSummaryId> {
}
