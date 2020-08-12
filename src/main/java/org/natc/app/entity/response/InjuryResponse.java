package org.natc.app.entity.response;

import lombok.Getter;
import org.natc.app.entity.domain.Injury;

@Getter
public class InjuryResponse {
    private Integer gameId;
    private Integer playerId;
    private Integer teamId;
    private Integer duration;

    public InjuryResponse(final Injury injury) {
        gameId = injury.getGameId();
        playerId = injury.getPlayerId();
        teamId = injury.getTeamId();
        duration = injury.getDuration();
    }
}
