package org.natc.natc.repository;

import org.natc.natc.entity.domain.PlayerStatsSummary;
import org.natc.natc.entity.domain.PlayerStatsSummaryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerStatsSummaryRepository extends JpaRepository<PlayerStatsSummary, PlayerStatsSummaryId> {
}
