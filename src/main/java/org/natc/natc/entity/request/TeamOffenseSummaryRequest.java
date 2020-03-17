package org.natc.natc.entity.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamOffenseSummaryRequest {
    private String year;
    private Integer teamId;
}
