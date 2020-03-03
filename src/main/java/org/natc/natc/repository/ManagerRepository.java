package org.natc.natc.repository;

import org.natc.natc.entity.domain.Manager;
import org.natc.natc.entity.domain.ManagerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, ManagerId> {
}
