package org.natc.app.repository;

import org.natc.app.entity.domain.PlayerStatsSummary;
import org.natc.app.entity.domain.PlayerStatsSummaryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerStatsSummaryRepository extends JpaRepository<PlayerStatsSummary, PlayerStatsSummaryId> {
}
