package org.natc.app.entity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
