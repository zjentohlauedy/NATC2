package org.natc.app.repository;

import org.natc.app.entity.domain.FirstName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FirstNameRepository extends JpaRepository<FirstName, String> {

    @Query(value = "SELECT * FROM #{#entityName} ORDER BY -LOG(1 - random())/frequency LIMIT 1", nativeQuery = true)
    Optional<FirstName> findRandomName();
}
