package org.natc.natc.entity.response;

public class TeamOffenseSummaryResponse {
    private String year;
    private Integer type; // TODO: enum
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
