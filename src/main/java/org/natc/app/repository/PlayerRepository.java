package org.natc.app.repository;

import org.natc.app.entity.domain.Player;
import org.natc.app.entity.domain.PlayerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, PlayerId> {

    @Query("select max(playerId) from #{#entityName}")
    Optional<Integer> findMaxPlayerId();
}
