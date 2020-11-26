package org.natc.app.repository;

import org.natc.app.entity.domain.Team;
import org.natc.app.entity.domain.TeamId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, TeamId> {

    @Modifying
    @Query(value = "INSERT INTO teams_t (team_id, year, location, name, abbrev, time_zone, game_time, conference, division, allstar_team, expectation, drought) " +
            "SELECT team_id, :destYear, location, name, abbrev, time_zone, game_time, conference, division, allstar_team, expectation, drought FROM teams_t " +
            "WHERE year = :sourceYear", nativeQuery = true)
    void copyTeamsForNewYear(String sourceYear, String destYear);
}
