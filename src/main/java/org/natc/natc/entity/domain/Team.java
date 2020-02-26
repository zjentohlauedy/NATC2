package org.natc.natc.entity.domain;

import lombok.Getter;

@Getter
public class Team {
    private Integer teamId;
    private String year;
    private String location;
    private String name;
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
    private Integer divisionWins;
    private Integer divisionLosses;
    private Integer outOfConferenceWins;
    private Integer outOfConferenceLosses;
    private Integer overtimeWins;
    private Integer overtimeLosses;
    private Integer roadWins;
    private Integer roadLosses;
    private Integer homeWins;
    private Integer homeLosses;
    private Integer divisionRank;
    private Integer playoffRank;
    private Integer playoffGames;
    private Integer round1Wins;
    private Integer round2Wins;
    private Integer round3Wins;
    private Double expectation;
    private Integer drought;
}
