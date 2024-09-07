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

@Entity(name = "playergames_t")
@IdClass(PlayerGameId.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerGame {
    @Id
    private Integer gameId;
    private String year;
    private LocalDate datestamp;
    private Integer type;
    @Id
    private Integer playerId;
    private Integer teamId;
    private Integer injured;
    private Integer started;
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
    private Integer offense;
    private Integer points;
}
