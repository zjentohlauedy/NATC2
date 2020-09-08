package org.natc.app.repository;

import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, ScheduleId> {
    Optional<Schedule> findFirstByStatusOrderByScheduledDesc(Integer status);
    Optional<Schedule> findByYearAndSequence(String year, Integer sequence);
}
