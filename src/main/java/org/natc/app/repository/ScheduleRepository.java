package org.natc.app.repository;

import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, ScheduleId> {
}
