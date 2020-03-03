package org.natc.natc.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerSearchRequest {
    private Integer managerId;
    private Integer teamId;
    private Integer playerId;
    private String year;
}
