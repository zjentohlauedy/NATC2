package org.natc.app.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "natc.league")
public class LeagueConfiguration {
    private Integer teamsPerDivision;
    private Integer teamsPerConference;
    private Integer numberOfTeams;
    private Integer playersPerTeam;
    private String firstSeason;
    private Integer initialManagers;
    private Integer initialPlayers;
    private Integer daysInPreseason;
    private Integer daysInRegularSeason;
    private Integer gamesPerDay;
    private Integer outOfConferenceGames;
    private Integer playoffGamesRoundOne;
    private Integer playoffGamesRoundTwo;
    private Integer playoffGamesRoundThree;
    private Integer maxPlayerManagersPerSeason;
}
