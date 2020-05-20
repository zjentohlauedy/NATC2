package org.natc.app.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.natc.app.entity.domain.GameType;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerGameSearchRequest {
    private Integer gameId;
    private String year;
    private LocalDate datestamp;
    private GameType type;
    private Integer playerId;
    private Integer teamId;
}
