package org.natc.natc.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.natc.natc.entity.domain.GameType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDefenseSummarySearchRequest {
    private String year;
    private GameType type;
    private Integer teamId;
}
