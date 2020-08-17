package org.natc.app.repository;

import org.natc.app.entity.domain.GameState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameStateRepository extends JpaRepository<GameState, Integer> {
}
