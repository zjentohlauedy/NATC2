package org.natc.app.repository;

import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.ManagerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, ManagerId> {

    @Query("select max(managerId) from #{#entityName}")
    Optional<Integer> findMaxManagerId();
}
