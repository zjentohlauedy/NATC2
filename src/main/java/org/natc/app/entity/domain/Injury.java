package org.natc.app.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Injury {
    private Integer gameId;
    private Integer playerId;
    private Integer teamId;
    private Integer duration;
}
