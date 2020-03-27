package org.natc.natc.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerStatsSummary {
    private String year;
    private Integer type;
    private Integer playerId;
    private Integer games;
    private Integer gamesStarted;
    private Integer playingTime;
    private Integer attempts;
    private Integer goals;
    private Integer assists;
    private Integer turnovers;
    private Integer stops;
    private Integer steals;
    private Integer penalties;
    private Integer offensivePenalties;
    private Integer penaltyShotsAttempted;
    private Integer penaltyShotsMade;
    private Integer overtimePenaltyShotsAttempted;
    private Integer overtimePenaltyShotsMade;
    private Integer teamId;
}
