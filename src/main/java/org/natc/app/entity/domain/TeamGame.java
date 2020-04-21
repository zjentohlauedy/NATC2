package org.natc.app.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamGame {
    private Integer gameId;
    private String year;
    private LocalDate datestamp;
    private Integer type;
    private Integer playoffRound;
    private Integer teamId;
    private Integer opponent;
    private Integer road;
    private Integer overtime;
    private Integer win;
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
    private Integer period1Score;
    private Integer period2Score;
    private Integer period3Score;
    private Integer period4Score;
    private Integer period5Score;
    private Integer overtimeScore;
    private Integer totalScore;
}
