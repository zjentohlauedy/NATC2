package org.natc.app.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamSearchRequest {
    private Integer teamId;
    private String year;
    private Integer conferenceId;
    private Integer divisionId;
    private Boolean allstarTeam;
}
