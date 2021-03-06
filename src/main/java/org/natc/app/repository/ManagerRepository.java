package org.natc.app.repository;

import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.ManagerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, ManagerId> {

    @Query("select max(managerId) from #{#entityName}")
    Optional<Integer> findMaxManagerId();

    @Modifying
    @Query(value = "INSERT INTO managers_t (manager_id, team_id, player_id, year, first_name, last_name, age, offense, defense, intangible, penalties, vitality, style, new_hire, released, retired, seasons, score, total_seasons, total_score) " +
    "SELECT manager_id, team_id, player_id, :destYear, first_name, last_name, age, offense, defense, intangible, penalties, vitality, style, new_hire, released, retired, seasons, score, total_seasons, total_score FROM managers_t " +
    "WHERE year = :sourceYear", nativeQuery = true)
    void copyManagersForNewYear(String sourceYear, String destYear);
}
