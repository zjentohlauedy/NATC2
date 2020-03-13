package org.natc.natc.repository;

import org.natc.natc.entity.domain.Player;
import org.natc.natc.entity.domain.PlayerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, PlayerId> {
}
