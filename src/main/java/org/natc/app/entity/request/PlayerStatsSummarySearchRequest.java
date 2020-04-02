package org.natc.app.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.natc.app.entity.domain.GameType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerStatsSummarySearchRequest {
    private String year;
    private GameType type;
    private Integer playerId;
    private Integer teamId;
}
