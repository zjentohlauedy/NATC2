package org.natc.app.repository;

import org.natc.app.entity.domain.Player;
import org.natc.app.entity.domain.PlayerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, PlayerId> {
}
