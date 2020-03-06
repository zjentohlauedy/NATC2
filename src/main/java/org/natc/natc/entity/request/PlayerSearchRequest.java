package org.natc.natc.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerSearchRequest {
    private Integer playerId;
    private Integer teamId;
    private String year;
}
