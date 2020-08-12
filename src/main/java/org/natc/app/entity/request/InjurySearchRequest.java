package org.natc.app.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InjurySearchRequest {
    private Integer gameId;
    private Integer playerId;
    private Integer teamId;
}
