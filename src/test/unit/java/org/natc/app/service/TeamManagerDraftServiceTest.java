package org.natc.app.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.comparator.TeamComparator;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.Team;
import org.natc.app.exception.NATCException;
import org.natc.app.exception.TeamManagerDraftException;
import org.natc.app.repository.TeamRepository;
import org.springframework.data.domain.Example;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamManagerDraftServiceTest {

    @Captor
    private ArgumentCaptor<Example<Team>> teamCaptor;

    @Mock
    private TeamComparator teamComparator;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamManagerDraftService teamManagerDraftService;

    @Nested
    class AssignManagersToTeams {

        @Test
        void shouldCallTeamRepositoryToRetrieveTeamRecordsForPreviousSeason() throws NATCException {
            final Team team = Team.builder().teamId(123).year("2011").build();
            final Manager manager = Manager.builder().managerId(1).year("2011").build();
            final List<Team> teams = Collections.singletonList(team);
            final List<Manager> managers = Collections.singletonList(manager);

            teamManagerDraftService.assignManagersToTeams(teams, managers);

            verify(teamRepository).findOne(teamCaptor.capture());

            final Team exampleTeam = teamCaptor.getValue().getProbe();

            assertEquals(team.getTeamId(), exampleTeam.getTeamId());
            assertEquals("2010", exampleTeam.getYear());
        }

        @Test
        void shouldCallTeamRepositoryForEveryTeamGiven() throws NATCException {
            final List<Team> teams = Arrays.asList(
                    Team.builder().teamId(1).year("2000").build(),
                    Team.builder().teamId(2).year("2000").build(),
                    Team.builder().teamId(3).year("2000").build(),
                    Team.builder().teamId(4).year("2000").build(),
                    Team.builder().teamId(5).year("2000").build()
            );

            final List<Manager> managers = Arrays.asList(
                    Manager.builder().managerId(101).year("2000").seasons(0).build(),
                    Manager.builder().managerId(102).year("2000").seasons(0).build(),
                    Manager.builder().managerId(103).year("2000").seasons(0).build(),
                    Manager.builder().managerId(104).year("2000").seasons(0).build(),
                    Manager.builder().managerId(105).year("2000").seasons(0).build()
            );

            teamManagerDraftService.assignManagersToTeams(teams, managers);

            verify(teamRepository, times(teams.size())).findOne(teamCaptor.capture());

            assertEquals(teams.size(), teamCaptor.getAllValues().stream().map(teamExample -> teamExample.getProbe().getTeamId()).distinct().count());
        }

        @Test
        void shouldAssignAManagerToATeamBySettingTheTeamIdOnTheManagerToTheTeamIdOfTheTeam() throws NATCException {
            final Team team = Team.builder().teamId(123).year("2011").build();
            final Manager manager = Manager.builder().managerId(1).year("2011").build();
            final List<Team> teams = Collections.singletonList(team);
            final List<Manager> managers = Collections.singletonList(manager);

            teamManagerDraftService.assignManagersToTeams(teams, managers);

            assertEquals(team.getTeamId(), manager.getTeamId());
        }

        @Test
        void shouldSetNewHireToTrueWhenAssigningAManagerToATeam() throws NATCException {
            final Team team = Team.builder().teamId(123).year("2011").build();
            final Manager manager = Manager.builder().managerId(1).year("2011").build();
            final List<Team> teams = Collections.singletonList(team);
            final List<Manager> managers = Collections.singletonList(manager);

            teamManagerDraftService.assignManagersToTeams(teams, managers);

            assertEquals(team.getTeamId(), manager.getTeamId());
            assertEquals(1, manager.getNewHire());
        }

        @Test
        void shouldResetScoreAndSeasonsToZeroWhenAssigningAManagerToATeam() throws NATCException {
            final Team team = Team.builder().teamId(123).year("2011").build();
            final Manager manager = Manager.builder().managerId(1).year("2011").build();
            final List<Team> teams = Collections.singletonList(team);
            final List<Manager> managers = Collections.singletonList(manager);

            teamManagerDraftService.assignManagersToTeams(teams, managers);

            assertEquals(team.getTeamId(), manager.getTeamId());
            assertEquals(0, manager.getScore());
            assertEquals(0, manager.getSeasons());
        }

        @Test
        void shouldAssignAManagerToEveryTeam() throws NATCException {
            final List<Team> teams = Arrays.asList(
                    Team.builder().teamId(1).year("2000").build(),
                    Team.builder().teamId(2).year("2000").build(),
                    Team.builder().teamId(3).year("2000").build(),
                    Team.builder().teamId(4).year("2000").build(),
                    Team.builder().teamId(5).year("2000").build()
            );

            final List<Manager> managers = Arrays.asList(
                    Manager.builder().managerId(101).year("2000").seasons(0).build(),
                    Manager.builder().managerId(102).year("2000").seasons(0).build(),
                    Manager.builder().managerId(103).year("2000").seasons(0).build(),
                    Manager.builder().managerId(104).year("2000").seasons(0).build(),
                    Manager.builder().managerId(105).year("2000").seasons(0).build()
            );

            teamManagerDraftService.assignManagersToTeams(teams, managers);

            assertEquals(teams.size(), managers.stream().filter(manager -> Objects.nonNull(manager.getTeamId())).count());
            assertEquals(
                    teams.stream().map(Team::getTeamId).distinct().sorted().toList(),
                    managers.stream().map(Manager::getTeamId).distinct().sorted().toList()
            );
        }

        @Test
        void shouldOnlyAssignOneManagerPerTeamIfThereAreMoreManagersThanTeams() throws NATCException {
            final List<Team> teams = Arrays.asList(
                    Team.builder().teamId(1).year("2000").build(),
                    Team.builder().teamId(4).year("2000").build(),
                    Team.builder().teamId(5).year("2000").build()
            );

            final List<Manager> managers = Arrays.asList(
                    Manager.builder().managerId(101).year("2000").seasons(0).build(),
                    Manager.builder().managerId(102).year("2000").seasons(0).build(),
                    Manager.builder().managerId(103).year("2000").seasons(0).build(),
                    Manager.builder().managerId(104).year("2000").seasons(0).build(),
                    Manager.builder().managerId(105).year("2000").seasons(0).build()
            );

            teamManagerDraftService.assignManagersToTeams(teams, managers);

            assertEquals(teams.size(), managers.stream().filter(manager -> Objects.nonNull(manager.getTeamId())).count());
        }

        @Test
        void shouldThrowATeamManagerDraftExceptionIfThereAreNotEnoughManagersForEveryTeam() {
            final List<Team> teams = Arrays.asList(
                    Team.builder().teamId(1).year("2000").build(),
                    Team.builder().teamId(2).year("2000").build(),
                    Team.builder().teamId(3).year("2000").build(),
                    Team.builder().teamId(4).year("2000").build(),
                    Team.builder().teamId(5).year("2000").build()
            );

            final List<Manager> managers = Arrays.asList(
                    Manager.builder().managerId(101).year("2000").seasons(0).build(),
                    Manager.builder().managerId(104).year("2000").seasons(0).build(),
                    Manager.builder().managerId(105).year("2000").seasons(0).build()
            );

            assertThrows(TeamManagerDraftException.class, () -> teamManagerDraftService.assignManagersToTeams(teams, managers));
        }

        @Test
        void shouldAssignAManagerWithTheHighestPerformanceRatingFirst() throws NATCException {
            final Manager highestRatedManager = Manager.builder().managerId(1).year("2000").score(10).seasons(3).totalScore(10).totalSeasons(3).build();
            final Manager lowestRatedManager = Manager.builder().managerId(2).year("2000").score(5).seasons(3).totalScore(5).totalSeasons(3).build();
            final Team team = Team.builder().teamId(123).year("2011").build();
            final List<Team> teams = Collections.singletonList(team);
            final List<Manager> managers = Arrays.asList(lowestRatedManager, highestRatedManager);

            teamManagerDraftService.assignManagersToTeams(teams, managers);

            assertEquals(team.getTeamId(), highestRatedManager.getTeamId());
        }
        
        @Test
        void shouldAssignAManagerWithTheHighestOverallRatingIfPerformanceRatingsAreTheSame() throws NATCException {
            final Manager highestRatedManager = Manager.builder().managerId(1).year("2000").seasons(0)
                    .offense(0.8).defense(0.8).intangible(0.8).penalties(0.8)
                    .build();
            final Manager lowestRatedManager = Manager.builder().managerId(2).year("2000").seasons(0)
                    .offense(0.2).defense(0.2).intangible(0.2).penalties(0.2)
                    .build();
            final Team team = Team.builder().teamId(123).year("2011").build();
            final List<Team> teams = Collections.singletonList(team);
            final List<Manager> managers = Arrays.asList(lowestRatedManager, highestRatedManager);

            teamManagerDraftService.assignManagersToTeams(teams, managers);

            assertEquals(team.getTeamId(), highestRatedManager.getTeamId());
        }

        @Test
        void shouldNotAssignAManagerToATeamThatReleasedTheManager() throws NATCException {
            final Team team = Team.builder().teamId(123).year("2011").build();
            final Manager highestRatedManager = Manager.builder().managerId(1).year("2000").seasons(0)
                    .offense(0.8).defense(0.8).intangible(0.8).penalties(0.8).formerTeamId(team.getTeamId())
                    .build();
            final Manager lowestRatedManager = Manager.builder().managerId(2).year("2000").seasons(0)
                    .offense(0.2).defense(0.2).intangible(0.2).penalties(0.2)
                    .build();
            final List<Team> teams = Collections.singletonList(team);
            final List<Manager> managers = Arrays.asList(lowestRatedManager, highestRatedManager);

            teamManagerDraftService.assignManagersToTeams(teams, managers);

            assertEquals(team.getTeamId(), lowestRatedManager.getTeamId());
        }

        @Test
        void shouldAssignManagersToTeamsInARandomOrderGivenNoPreviousSeasonTeamRecordsExist() throws NATCException {
            final List<Team> teams = Arrays.asList(
                    Team.builder().teamId(1).year("2000").build(),
                    Team.builder().teamId(2).year("2000").build(),
                    Team.builder().teamId(3).year("2000").build(),
                    Team.builder().teamId(4).year("2000").build(),
                    Team.builder().teamId(5).year("2000").build()
            );

            final List<Manager> managers = Arrays.asList(
                    Manager.builder().managerId(101).year("2000").seasons(0).offense(1.0).defense(1.0).intangible(1.0).penalties(1.0).build(),
                    Manager.builder().managerId(102).year("2000").seasons(0).offense(0.8).defense(0.8).intangible(0.8).penalties(0.8).build(),
                    Manager.builder().managerId(103).year("2000").seasons(0).offense(0.6).defense(0.6).intangible(0.6).penalties(0.6).build(),
                    Manager.builder().managerId(104).year("2000").seasons(0).offense(0.4).defense(0.4).intangible(0.4).penalties(0.4).build(),
                    Manager.builder().managerId(105).year("2000").seasons(0).offense(0.2).defense(0.2).intangible(0.2).penalties(0.2).build()
            );

            final List<Integer> expectedTeamIdList = teams.stream().map(Team::getTeamId).toList();

            teamManagerDraftService.assignManagersToTeams(teams, managers);

            final List<Integer> actualTeamIdList = managers.stream().sorted(Comparator.comparing(Manager::getManagerId)).map(Manager::getTeamId).toList();

            assertNotEquals(expectedTeamIdList, actualTeamIdList);
        }

        @Test
        void shouldThrowATeamManagerDraftExceptionIfOnlySomeOfThePreviousSeasonTeamRecordsExist() {
            final List<Team> teams = Arrays.asList(
                    Team.builder().teamId(1).year("2000").build(),
                    Team.builder().teamId(2).year("2000").build(),
                    Team.builder().teamId(3).year("2000").build(),
                    Team.builder().teamId(4).year("2000").build(),
                    Team.builder().teamId(5).year("2000").build()
            );

            final List<Team> previousYearTeams = Arrays.asList(
                    Team.builder().teamId(1).year("1999").build(),
                    Team.builder().teamId(2).year("1999").build(),
                    Team.builder().teamId(3).year("1999").build(),
                    Team.builder().teamId(4).year("1999").build()
            );

            final List<Manager> managers = Arrays.asList(
                    Manager.builder().managerId(101).year("2000").seasons(0).offense(1.0).defense(1.0).intangible(1.0).penalties(1.0).build(),
                    Manager.builder().managerId(102).year("2000").seasons(0).offense(0.8).defense(0.8).intangible(0.8).penalties(0.8).build(),
                    Manager.builder().managerId(103).year("2000").seasons(0).offense(0.6).defense(0.6).intangible(0.6).penalties(0.6).build(),
                    Manager.builder().managerId(104).year("2000").seasons(0).offense(0.4).defense(0.4).intangible(0.4).penalties(0.4).build(),
                    Manager.builder().managerId(105).year("2000").seasons(0).offense(0.2).defense(0.2).intangible(0.2).penalties(0.2).build()
            );

            when(teamRepository.findOne(any()))
                    .thenReturn(Optional.of(previousYearTeams.getFirst()))
                    .thenReturn(Optional.of(previousYearTeams.get(1)))
                    .thenReturn(Optional.of(previousYearTeams.get(2)))
                    .thenReturn(Optional.of(previousYearTeams.get(3)))
                    .thenReturn(Optional.empty());

            assertThrows(TeamManagerDraftException.class, () -> teamManagerDraftService.assignManagersToTeams(teams, managers));
        }

        @Nested
        class WhenPreviousSeasonTeamRecordsExist {

            @Test
            void shouldSortTeamsUsingTeamComparator() throws NATCException {
                final List<Team> teams = Arrays.asList(
                        Team.builder().teamId(1).year("2000").build(),
                        Team.builder().teamId(2).year("2000").build(),
                        Team.builder().teamId(3).year("2000").build(),
                        Team.builder().teamId(4).year("2000").build(),
                        Team.builder().teamId(5).year("2000").build()
                );

                final List<Team> previousYearTeams = Arrays.asList(
                        Team.builder().teamId(1).year("1999").build(),
                        Team.builder().teamId(2).year("1999").build(),
                        Team.builder().teamId(3).year("1999").build(),
                        Team.builder().teamId(4).year("1999").build(),
                        Team.builder().teamId(5).year("1999").build()
                );

                final List<Manager> managers = Arrays.asList(
                        Manager.builder().managerId(101).year("2000").seasons(0).offense(1.0).defense(1.0).intangible(1.0).penalties(1.0).build(),
                        Manager.builder().managerId(102).year("2000").seasons(0).offense(0.8).defense(0.8).intangible(0.8).penalties(0.8).build(),
                        Manager.builder().managerId(103).year("2000").seasons(0).offense(0.6).defense(0.6).intangible(0.6).penalties(0.6).build(),
                        Manager.builder().managerId(104).year("2000").seasons(0).offense(0.4).defense(0.4).intangible(0.4).penalties(0.4).build(),
                        Manager.builder().managerId(105).year("2000").seasons(0).offense(0.2).defense(0.2).intangible(0.2).penalties(0.2).build()
                );

                when(teamRepository.findOne(any()))
                        .thenReturn(Optional.of(previousYearTeams.getFirst()))
                        .thenReturn(Optional.of(previousYearTeams.get(1)))
                        .thenReturn(Optional.of(previousYearTeams.get(2)))
                        .thenReturn(Optional.of(previousYearTeams.get(3)))
                        .thenReturn(Optional.of(previousYearTeams.get(4)));

                teamManagerDraftService.assignManagersToTeams(teams, managers);

                verify(teamComparator, atLeastOnce()).compare(any(), any());
            }

            @Test
            void shouldSelectManagersForTeamsInTheSortedOrder() throws NATCException {
                final List<Team> teams = Arrays.asList(
                        Team.builder().teamId(1).year("2000").build(),
                        Team.builder().teamId(2).year("2000").build()
                );

                final List<Team> previousYearTeams = Arrays.asList(
                        Team.builder().teamId(1).year("1999").build(),
                        Team.builder().teamId(2).year("1999").build()
                );

                final List<Manager> managers = Arrays.asList(
                        Manager.builder().managerId(101).year("2000").seasons(0).offense(1.0).defense(1.0).intangible(1.0).penalties(1.0).build(),
                        Manager.builder().managerId(102).year("2000").seasons(0).offense(0.8).defense(0.8).intangible(0.8).penalties(0.8).build()
                );

                when(teamRepository.findOne(any()))
                        .thenReturn(Optional.of(previousYearTeams.getFirst()))
                        .thenReturn(Optional.of(previousYearTeams.get(1)));

                // Team 2 should be sorted to first position
                when(teamComparator.compare(previousYearTeams.get(1), previousYearTeams.getFirst())).thenReturn(-1);

                final Manager bestManager = managers.getFirst();

                teamManagerDraftService.assignManagersToTeams(teams, managers);

                assertEquals(2, bestManager.getTeamId());
            }

            @Test
            void shouldSelectManagersForTeamsInTheSortedOrderWhenSortingOpposite() throws NATCException {
                final List<Team> teams = Arrays.asList(
                        Team.builder().teamId(1).year("2000").build(),
                        Team.builder().teamId(2).year("2000").build()
                );

                final List<Team> previousYearTeams = Arrays.asList(
                        Team.builder().teamId(1).year("1999").build(),
                        Team.builder().teamId(2).year("1999").build()
                );

                final List<Manager> managers = Arrays.asList(
                        Manager.builder().managerId(101).year("2000").seasons(0).offense(1.0).defense(1.0).intangible(1.0).penalties(1.0).build(),
                        Manager.builder().managerId(102).year("2000").seasons(0).offense(0.8).defense(0.8).intangible(0.8).penalties(0.8).build()
                );

                when(teamRepository.findOne(any()))
                        .thenReturn(Optional.of(previousYearTeams.getFirst()))
                        .thenReturn(Optional.of(previousYearTeams.get(1)));

                // Team 2 should be sorted to first position
                when(teamComparator.compare(previousYearTeams.get(1), previousYearTeams.getFirst())).thenReturn(1);

                final Manager bestManager = managers.getFirst();

                teamManagerDraftService.assignManagersToTeams(teams, managers);

                assertEquals(1, bestManager.getTeamId());
            }
        }
    }
}