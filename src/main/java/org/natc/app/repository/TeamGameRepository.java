package org.natc.app.repository;

import org.natc.app.entity.domain.TeamGame;
import org.natc.app.entity.domain.TeamGameId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamGameRepository extends JpaRepository<TeamGame, TeamGameId> {

    Integer countByYearAndTypeAndTeamIdAndOpponentAndWin(String year, Integer type, Integer teamId, Integer opponent, Integer win);
}
