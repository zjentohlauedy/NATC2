package org.natc.app.repository;

import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.ManagerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, ManagerId> {
}
