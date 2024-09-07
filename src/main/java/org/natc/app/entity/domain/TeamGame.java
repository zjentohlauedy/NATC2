package org.natc.app.entity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name = "teamgames_t")
@IdClass(TeamGameId.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamGame {
    @Id
    private Integer gameId;
    private String year;
    private LocalDate datestamp;
    private Integer type;
    private Integer playoffRound;
    @Id
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
    @Column(name = "psa")
    private Integer penaltyShotsAttempted;
    @Column(name = "psm")
    private Integer penaltyShotsMade;
    @Column(name = "ot_psa")
    private Integer overtimePenaltyShotsAttempted;
    @Column(name = "ot_psm")
    private Integer overtimePenaltyShotsMade;
    @Column(name = "period1_score")
    private Integer period1Score;
    @Column(name = "period2_score")
    private Integer period2Score;
    @Column(name = "period3_score")
    private Integer period3Score;
    @Column(name = "period4_score")
    private Integer period4Score;
    @Column(name = "period5_score")
    private Integer period5Score;
    private Integer overtimeScore;
    private Integer totalScore;
}
