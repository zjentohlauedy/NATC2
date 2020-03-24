package org.natc.natc.entity.response;

import org.natc.natc.entity.domain.GameType;

public class TeamDefenseSummaryResponse {
    private String year;
    private GameType type;
    private Integer teamId;
    private Integer games;
    private Integer possessions;
    private Integer possessionTime;
    private Integer attempts;
    private Integer goals;
    private Integer turnovers;
    private Integer steals;
    private Integer penalties;
    private Integer offensivePenalties;
    private Integer penaltyShotsAttempted;
    private Integer penaltyShotsMade;
    private Integer overtimePenaltyShotsAttempted;
    private Integer overtimePenaltyShotsMade;
    private Integer score;
}
