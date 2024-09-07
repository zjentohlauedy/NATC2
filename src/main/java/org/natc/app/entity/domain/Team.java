package org.natc.app.entity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "teams_t")
@IdClass(TeamId.class)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Team {
    @Id
    private Integer teamId;
    @Id
    private String year;
    private String location;
    private String name;
    @Column(name = "abbrev")
    private String abbreviation;
    private String timeZone;
    private Integer gameTime;
    private Integer conference;
    private Integer division;
    private Integer allstarTeam;
    private Integer preseasonGames;
    private Integer preseasonWins;
    private Integer preseasonLosses;
    private Integer games;
    private Integer wins;
    private Integer losses;
    @Column(name = "div_wins")
    private Integer divisionWins;
    @Column(name = "div_losses")
    private Integer divisionLosses;
    @Column(name = "ooc_wins")
    private Integer outOfConferenceWins;
    @Column(name = "ooc_losses")
    private Integer outOfConferenceLosses;
    @Column(name = "ot_wins")
    private Integer overtimeWins;
    @Column(name = "ot_losses")
    private Integer overtimeLosses;
    private Integer roadWins;
    private Integer roadLosses;
    private Integer homeWins;
    private Integer homeLosses;
    private Integer divisionRank;
    private Integer playoffRank;
    private Integer playoffGames;
    @Column(name = "round1_wins")
    private Integer round1Wins;
    @Column(name = "round2_wins")
    private Integer round2Wins;
    @Column(name = "round3_wins")
    private Integer round3Wins;
    private Double expectation;
    private Integer drought;
}

