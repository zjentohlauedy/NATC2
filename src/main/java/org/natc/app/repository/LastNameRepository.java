package org.natc.app.repository;

import org.natc.app.entity.domain.LastName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LastNameRepository extends JpaRepository<LastName, String> {

    @Query(value = "SELECT name FROM #{#entityName} ORDER BY -LOG(1 - random())/frequency LIMIT 1", nativeQuery = true)
    Optional<LastName> findRandomName();
}
