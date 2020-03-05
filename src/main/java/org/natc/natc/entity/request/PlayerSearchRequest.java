package org.natc.natc.entity.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlayerSearchRequest {
    private final Integer playerId;
    private final Integer teamId;
    private final String year;
}
