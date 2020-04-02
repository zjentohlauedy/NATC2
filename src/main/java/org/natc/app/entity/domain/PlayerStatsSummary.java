package org.natc.app.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = "player_stats_sum_t")
@IdClass(PlayerStatsSummaryId.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerStatsSummary {
    @Id
    private String year;
    @Id
    private Integer type;
    @Id
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
    @Column(name = "psa")
    private Integer penaltyShotsAttempted;
    @Column(name = "psm")
    private Integer penaltyShotsMade;
    @Column(name = "ot_psa")
    private Integer overtimePenaltyShotsAttempted;
    @Column(name = "ot_psm")
    private Integer overtimePenaltyShotsMade;
    private Integer teamId;
}
