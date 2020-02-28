package org.natc.natc.entity.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.natc.natc.entity.domain.Team;

@Getter
@NoArgsConstructor
public class TeamResponse {
    private Integer teamId;
    private String year;
    private String location;
    private String name;
    private String abbreviation;
    private String timeZone;
    private Integer gameTime;
    private Integer conferenceId;
    private Integer divisionId;
    private Boolean allstarTeam;
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

    public TeamResponse(final Team team) {
        if (team.getAllstarTeam() != null) {
            allstarTeam = team.getAllstarTeam() == 1;
        }

        teamId = team.getTeamId();
        year = team.getYear();
        location = team.getLocation();
        name = team.getName();
        abbreviation = team.getAbbreviation();
        timeZone = team.getTimeZone();
        gameTime = team.getGameTime();
        conferenceId = team.getConference();
        divisionId = team.getDivision();
        preseasonGames = team.getPreseasonGames();
        preseasonWins = team.getPreseasonWins();
        preseasonLosses = team.getPreseasonLosses();
        games = team.getGames();
        wins = team.getWins();
        losses = team.getLosses();
        divisionWins = team.getDivisionWins();
        divisionLosses = team.getDivisionLosses();
        outOfConferenceWins = team.getOutOfConferenceWins();
        outOfConferenceLosses = team.getOutOfConferenceLosses();
        overtimeWins = team.getOvertimeWins();
        overtimeLosses = team.getOvertimeLosses();
        roadWins = team.getRoadWins();
        roadLosses = team.getRoadLosses();
        homeWins = team.getHomeWins();
        homeLosses = team.getHomeLosses();
        divisionRank = team.getDivisionRank();
        playoffRank = team.getPlayoffRank();
        playoffGames = team.getPlayoffGames();
        round1Wins = team.getRound1Wins();
        round2Wins = team.getRound2Wins();
        round3Wins = team.getRound3Wins();
        expectation = team.getExpectation();
        drought = team.getDrought();
    }
}
