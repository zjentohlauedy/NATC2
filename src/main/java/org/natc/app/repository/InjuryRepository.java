package org.natc.app.repository;

import org.natc.app.entity.domain.Injury;
import org.natc.app.entity.domain.InjuryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InjuryRepository extends JpaRepository<Injury, InjuryId> {
}
