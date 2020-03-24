package org.natc.natc.entity.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.natc.natc.entity.domain.GameType;
import org.natc.natc.entity.domain.TeamDefenseSummary;

@Getter
@NoArgsConstructor
public class TeamDefenseSummaryResponse {
    private String year;
    private GameType type;
    private Integer teamId;
    private Integer games;
    private Integer possessions;
    private Integer possessionTime;
    private Integer attempts;
    private Integer goals;
    private Integer turnovers;
    private Integer steals;
    private Integer penalties;
    private Integer offensivePenalties;
    private Integer penaltyShotsAttempted;
    private Integer penaltyShotsMade;
    private Integer overtimePenaltyShotsAttempted;
    private Integer overtimePenaltyShotsMade;
    private Integer score;
    
    public TeamDefenseSummaryResponse(final TeamDefenseSummary teamDefenseSummary) {
        year = teamDefenseSummary.getYear();
        type = GameType.getByValue(teamDefenseSummary.getType());
        teamId = teamDefenseSummary.getTeamId();
        games = teamDefenseSummary.getGames();
        possessions = teamDefenseSummary.getPossessions();
        possessionTime = teamDefenseSummary.getPossessionTime();
        attempts = teamDefenseSummary.getAttempts();
        goals = teamDefenseSummary.getGoals();
        turnovers = teamDefenseSummary.getTurnovers();
        steals = teamDefenseSummary.getSteals();
        penalties = teamDefenseSummary.getPenalties();
        offensivePenalties = teamDefenseSummary.getOffensivePenalties();
        penaltyShotsAttempted = teamDefenseSummary.getPenaltyShotsAttempted();
        penaltyShotsMade = teamDefenseSummary.getPenaltyShotsMade();
        overtimePenaltyShotsAttempted = teamDefenseSummary.getOvertimePenaltyShotsAttempted();
        overtimePenaltyShotsMade = teamDefenseSummary.getOvertimePenaltyShotsMade();
        score = teamDefenseSummary.getScore();
    }
}
