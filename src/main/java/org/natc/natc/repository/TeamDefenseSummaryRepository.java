package org.natc.natc.repository;

import org.natc.natc.entity.domain.TeamDefenseSummary;
import org.natc.natc.entity.domain.TeamDefenseSummaryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamDefenseSummaryRepository extends JpaRepository<TeamDefenseSummary, TeamDefenseSummaryId> {
}
