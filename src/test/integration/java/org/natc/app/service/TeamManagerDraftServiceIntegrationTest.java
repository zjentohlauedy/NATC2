package org.natc.app.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.Team;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamManagerDraftServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamManagerDraftService teamManagerDraftService;

    @Nested
    class AssignManagersToTeams {

        @Test
        void shouldAssignManagersToTeamsInOrderOfPreviousSeasonsStats() throws NATCException {
            final List<Team> previousYearTeams = List.of(
                    Team.builder().teamId(1).year("1999").games(100).wins(55).build(),
                    Team.builder().teamId(2).year("1999").games(100).wins(40).build(),
                    Team.builder().teamId(3).year("1999").games(100).wins(35).build(),
                    Team.builder().teamId(4).year("1999").games(100).wins(50).build(),
                    Team.builder().teamId(5).year("1999").games(100).wins(45).build()
            );

            teamRepository.saveAll(previousYearTeams);

            final List<Team> teams = List.of(
                    Team.builder().teamId(1).year("2000").build(),
                    Team.builder().teamId(2).year("2000").build(),
                    Team.builder().teamId(3).year("2000").build(),
                    Team.builder().teamId(4).year("2000").build(),
                    Team.builder().teamId(5).year("2000").build()
            );

            final List<Manager> managers = List.of(
                    Manager.builder().managerId(101).year("2000").seasons(0).offense(1.0).defense(1.0).intangible(1.0).penalties(1.0).build(),
                    Manager.builder().managerId(102).year("2000").seasons(0).offense(0.8).defense(0.8).intangible(0.8).penalties(0.8).build(),
                    Manager.builder().managerId(103).year("2000").seasons(0).offense(0.6).defense(0.6).intangible(0.6).penalties(0.6).build(),
                    Manager.builder().managerId(104).year("2000").seasons(0).offense(0.4).defense(0.4).intangible(0.4).penalties(0.4).build(),
                    Manager.builder().managerId(105).year("2000").seasons(0).offense(0.2).defense(0.2).intangible(0.2).penalties(0.2).build()
            );

            teamManagerDraftService.assignManagersToTeams(teams, managers);

            assertEquals(
                    previousYearTeams.stream().sorted(Comparator.comparing(Team::getWins)).map(Team::getTeamId).toList(),
                    managers.stream().sorted(Comparator.comparing(Manager::getManagerId)).map(Manager::getTeamId).toList()
            );
        }

        @Test
        void shouldOnlyConsiderPreviousSeasonTeamRecords() throws NATCException {
            final List<Team> previousYearTeams = List.of(
                    Team.builder().teamId(1).year("1997").games(100).wins(55).build(),
                    Team.builder().teamId(1).year("1998").games(100).wins(40).build(),
                    Team.builder().teamId(1).year("1999").games(100).wins(35).build(),
                    Team.builder().teamId(1).year("2000").games(100).wins(50).build(),
                    Team.builder().teamId(1).year("2001").games(100).wins(45).build(),

                    Team.builder().teamId(2).year("1997").games(100).wins(36).build(),
                    Team.builder().teamId(2).year("1998").games(100).wins(36).build(),
                    Team.builder().teamId(2).year("1999").games(100).wins(36).build(),
                    Team.builder().teamId(2).year("2000").games(100).wins(36).build(),
                    Team.builder().teamId(2).year("2001").games(100).wins(36).build()
            );

            teamRepository.saveAll(previousYearTeams);

            final List<Team> teams = List.of(
                    Team.builder().teamId(1).year("2000").build(),
                    Team.builder().teamId(2).year("2000").build()
            );

            final Manager highestRatedManager = Manager.builder().managerId(101).year("2000").seasons(0).offense(1.0).defense(1.0).intangible(1.0).penalties(1.0).build();
            final Manager lowestRatedManager = Manager.builder().managerId(105).year("2000").seasons(0).offense(0.2).defense(0.2).intangible(0.2).penalties(0.2).build();

            final List<Manager> managers = List.of(lowestRatedManager, highestRatedManager);

            teamManagerDraftService.assignManagersToTeams(teams, managers);

            assertEquals(1, highestRatedManager.getTeamId());
            assertEquals(2, lowestRatedManager.getTeamId());
        }

        @Test
        void shouldOnlyConsiderRecordsForTheGivenTeams() throws NATCException {
            final List<Team> previousYearTeams = List.of(
                    Team.builder().teamId(1).year("1999").games(100).wins(55).build(),
                    Team.builder().teamId(2).year("1999").games(100).wins(35).build(),
                    Team.builder().teamId(3).year("1999").games(100).wins(40).build(),
                    Team.builder().teamId(4).year("1999").games(100).wins(50).build(),
                    Team.builder().teamId(5).year("1999").games(100).wins(45).build(),
                    Team.builder().teamId(6).year("1999").games(100).wins(60).build(),
                    Team.builder().teamId(7).year("1999").games(100).wins(65).build()
            );

            teamRepository.saveAll(previousYearTeams);

            final List<Team> teams = List.of(
                    Team.builder().teamId(1).year("2000").build(),
                    Team.builder().teamId(2).year("2000").build()
            );

            final Manager highestRatedManager = Manager.builder().managerId(101).year("2000").seasons(0).offense(1.0).defense(1.0).intangible(1.0).penalties(1.0).build();
            final Manager lowestRatedManager = Manager.builder().managerId(105).year("2000").seasons(0).offense(0.2).defense(0.2).intangible(0.2).penalties(0.2).build();

            final List<Manager> managers = List.of(lowestRatedManager, highestRatedManager);

            teamManagerDraftService.assignManagersToTeams(teams, managers);

            assertEquals(2, highestRatedManager.getTeamId());
            assertEquals(1, lowestRatedManager.getTeamId());
        }
    }
}