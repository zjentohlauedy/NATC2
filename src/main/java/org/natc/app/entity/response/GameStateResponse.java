package org.natc.app.entity.response;

import lombok.Getter;
import org.natc.app.entity.domain.GameState;
import org.natc.app.entity.domain.Period;
import org.natc.app.entity.domain.PossessionType;

import static org.natc.app.util.BooleanHelper.valueOf;

@Getter
public class GameStateResponse {
    private Integer gameId;
    private Boolean started;
    private Integer startTime;
    private Integer sequence;
    private Period period;
    private Boolean overtime;
    private Integer timeRemaining;
    private Boolean clockStopped;
    private PossessionType possession;
    private String lastEvent;

    public GameStateResponse(final GameState gameState) {
        gameId = gameState.getGameId();
        started = valueOf(gameState.getStarted());
        startTime = gameState.getStartTime();
        sequence = gameState.getSequence();
        period = Period.getByValue(gameState.getPeriod());
        overtime = valueOf(gameState.getOvertime());
        timeRemaining = gameState.getTimeRemaining();
        clockStopped = valueOf(gameState.getClockStopped());
        possession = PossessionType.getByValue(gameState.getPossession());
        lastEvent = gameState.getLastEvent();
    }
}
