package org.natc.app.repository;

import org.natc.app.entity.domain.PlayerGame;
import org.natc.app.entity.domain.PlayerGameId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerGameRepository extends JpaRepository<PlayerGame, PlayerGameId> {
}
