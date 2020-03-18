package org.natc.natc.entity.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.natc.natc.entity.domain.GameType;
import org.natc.natc.entity.domain.TeamOffenseSummary;

@Getter
@NoArgsConstructor
public class TeamOffenseSummaryResponse {
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

    public TeamOffenseSummaryResponse(final TeamOffenseSummary teamOffenseSummary) {
        year = teamOffenseSummary.getYear();
        type = GameType.getByValue(teamOffenseSummary.getType());
        teamId = teamOffenseSummary.getTeamId();
        games = teamOffenseSummary.getGames();
        possessions = teamOffenseSummary.getPossessions();
        possessionTime = teamOffenseSummary.getPossessionTime();
        attempts = teamOffenseSummary.getAttempts();
        goals = teamOffenseSummary.getGoals();
        turnovers = teamOffenseSummary.getTurnovers();
        steals = teamOffenseSummary.getSteals();
        penalties = teamOffenseSummary.getPenalties();
        offensivePenalties = teamOffenseSummary.getOffensivePenalties();
        penaltyShotsAttempted = teamOffenseSummary.getPenaltyShotsAttempted();
        penaltyShotsMade = teamOffenseSummary.getPenaltyShotsMade();
        overtimePenaltyShotsAttempted = teamOffenseSummary.getOvertimePenaltyShotsAttempted();
        overtimePenaltyShotsMade = teamOffenseSummary.getOvertimePenaltyShotsMade();
        score = teamOffenseSummary.getScore();
    }
}
