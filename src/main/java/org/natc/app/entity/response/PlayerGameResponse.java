package org.natc.app.entity.response;

import lombok.Getter;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.PlayerGame;

import java.time.LocalDate;

import static org.natc.app.util.BooleanHelper.valueOf;

@Getter
public class PlayerGameResponse {
    private Integer gameId;
    private String year;
    private LocalDate datestamp;
    private GameType type;
    private Integer playerId;
    private Integer teamId;
    private Boolean injured;
    private Boolean started;
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
    private Integer offense;
    private Integer points;

    public PlayerGameResponse(final PlayerGame playerGame) {
        gameId = playerGame.getGameId();
        year = playerGame.getYear();
        datestamp = playerGame.getDatestamp();
        type = GameType.getByValue(playerGame.getType());
        playerId = playerGame.getPlayerId();
        teamId = playerGame.getTeamId();
        injured = valueOf(playerGame.getInjured());
        started = valueOf(playerGame.getStarted());
        playingTime = playerGame.getPlayingTime();
        attempts = playerGame.getAttempts();
        goals = playerGame.getGoals();
        assists = playerGame.getAssists();
        turnovers = playerGame.getTurnovers();
        stops = playerGame.getStops();
        steals = playerGame.getSteals();
        penalties = playerGame.getPenalties();
        offensivePenalties = playerGame.getOffensivePenalties();
        penaltyShotsAttempted = playerGame.getPenaltyShotsAttempted();
        penaltyShotsMade = playerGame.getPenaltyShotsMade();
        overtimePenaltyShotsAttempted = playerGame.getOvertimePenaltyShotsAttempted();
        overtimePenaltyShotsMade = playerGame.getOvertimePenaltyShotsMade();
        offense = playerGame.getOffense();
        points = playerGame.getPoints();
    }
}
