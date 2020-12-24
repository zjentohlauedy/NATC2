package org.natc.app.repository;

import org.natc.app.entity.domain.Player;
import org.natc.app.entity.domain.PlayerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, PlayerId> {

    @Query("select max(playerId) from #{#entityName}")
    Optional<Integer> findMaxPlayerId();

    @Modifying
    @Query(value = "INSERT INTO players_t (player_id, team_id, year, first_name, last_name, age, scoring, passing, blocking, tackling, stealing, presence, discipline, penalty_shot, penalty_offense, penalty_defense, endurance, confidence, vitality, durability, rookie, retired, seasons_played) " +
    "SELECT player_id, team_id, :destYear, first_name, last_name, age, scoring, passing, blocking, tackling, stealing, presence, discipline, penalty_shot, penalty_offense, penalty_defense, endurance, confidence, vitality, durability, rookie, retired, seasons_played FROM players_t " +
    "WHERE year = :sourceYear", nativeQuery = true)
    void copyPlayersForNewYear(String sourceYear, String destYear);

    @Query(value = "select * from #{#entityName} where year = :year and retired = 1 order by (scoring + passing + blocking + tackling + stealing + presence + discipline + endurance + penalty_shot + penalty_offense + penalty_defense) desc limit 2", nativeQuery = true)
    List<Player> findTopTwoRetiredPlayersForYear(String year);
}
