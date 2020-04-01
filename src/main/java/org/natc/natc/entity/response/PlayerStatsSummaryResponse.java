package org.natc.natc.entity.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.natc.natc.entity.domain.GameType;
import org.natc.natc.entity.domain.PlayerStatsSummary;

@Getter
@NoArgsConstructor
public class PlayerStatsSummaryResponse {
    private String year;
    private GameType type;
    private Integer playerId;
    private Integer games;
    private Integer gamesStarted;
    private Integer playingTime;
    private Integer attempts;
    private Integer goals;
    private Integer assists;
    private Integer turnovers;
    private Integer stops;
    private Integer steals;
    private Integer penalties;
    private Integer offensivePenalties;
    private Integer penaltyShotsAttempted;
    private Integer penaltyShotsMade;
    private Integer overtimePenaltyShotsAttempted;
    private Integer overtimePenaltyShotsMade;
    private Integer teamId;

    public PlayerStatsSummaryResponse(final PlayerStatsSummary playerStatsSummary) {
        year = playerStatsSummary.getYear();
        type = GameType.getByValue(playerStatsSummary.getType());
        playerId = playerStatsSummary.getPlayerId();
        games = playerStatsSummary.getGames();
        gamesStarted = playerStatsSummary.getGamesStarted();
        playingTime = playerStatsSummary.getPlayingTime();
        attempts = playerStatsSummary.getAttempts();
        goals = playerStatsSummary.getGoals();
        assists = playerStatsSummary.getAssists();
        turnovers = playerStatsSummary.getTurnovers();
        stops = playerStatsSummary.getStops();
        steals = playerStatsSummary.getSteals();
        penalties = playerStatsSummary.getPenalties();
        offensivePenalties = playerStatsSummary.getOffensivePenalties();
        penaltyShotsAttempted = playerStatsSummary.getPenaltyShotsAttempted();
        penaltyShotsMade = playerStatsSummary.getPenaltyShotsMade();
        overtimePenaltyShotsAttempted = playerStatsSummary.getOvertimePenaltyShotsAttempted();
        overtimePenaltyShotsMade = playerStatsSummary.getOvertimePenaltyShotsMade();
        teamId = playerStatsSummary.getTeamId();
    }
}
