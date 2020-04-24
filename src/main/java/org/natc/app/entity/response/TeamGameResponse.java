package org.natc.app.entity.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.TeamGame;

import java.time.LocalDate;

import static org.natc.app.util.BooleanHelper.valueOf;

@Getter
@NoArgsConstructor
public class TeamGameResponse {
    private Integer gameId;
    private String year;
    private LocalDate datestamp;
    private GameType type;
    private Integer playoffRound;
    private Integer teamId;
    private Integer opponent;
    private Boolean road;
    private Boolean overtime;
    private Boolean win;
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
    private Integer period1Score;
    private Integer period2Score;
    private Integer period3Score;
    private Integer period4Score;
    private Integer period5Score;
    private Integer overtimeScore;
    private Integer totalScore;

    public TeamGameResponse(final TeamGame teamGame) {
        gameId = teamGame.getGameId();
        year = teamGame.getYear();
        datestamp = teamGame.getDatestamp();
        type = GameType.getByValue(teamGame.getType());
        playoffRound = teamGame.getPlayoffRound();
        teamId = teamGame.getTeamId();
        opponent = teamGame.getOpponent();
        road = valueOf(teamGame.getRoad());
        overtime = valueOf(teamGame.getOvertime());
        win = valueOf(teamGame.getWin());
        possessions = teamGame.getPossessions();
        possessionTime = teamGame.getPossessionTime();
        attempts = teamGame.getAttempts();
        goals = teamGame.getGoals();
        turnovers = teamGame.getTurnovers();
        steals = teamGame.getSteals();
        penalties = teamGame.getPenalties();
        offensivePenalties = teamGame.getOffensivePenalties();
        penaltyShotsAttempted = teamGame.getPenaltyShotsAttempted();
        penaltyShotsMade = teamGame.getPenaltyShotsMade();
        overtimePenaltyShotsAttempted = teamGame.getOvertimePenaltyShotsAttempted();
        overtimePenaltyShotsMade = teamGame.getOvertimePenaltyShotsMade();
        period1Score = teamGame.getPeriod1Score();
        period2Score = teamGame.getPeriod2Score();
        period3Score = teamGame.getPeriod3Score();
        period4Score = teamGame.getPeriod4Score();
        period5Score = teamGame.getPeriod5Score();
        overtimeScore = teamGame.getOvertimeScore();
        totalScore = teamGame.getTotalScore();
    }
}
