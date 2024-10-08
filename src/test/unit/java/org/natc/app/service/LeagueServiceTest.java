package org.natc.app.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.ManagerStyle;
import org.natc.app.entity.domain.Player;
import org.natc.app.entity.domain.Team;
import org.natc.app.exception.LeagueProcessingException;
import org.natc.app.exception.NATCException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeagueServiceTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private TeamService teamService;

    @Mock
    private ManagerService managerService;

    @Mock
    private LeagueConfiguration leagueConfiguration;

    @InjectMocks
    private LeagueService leagueService;

    @Nested
    class GenerateNewLeague {

        @Test
        void shouldCallTeamServiceToGenerateTeams() throws NATCException {
            leagueService.generateNewLeague();

            verify(teamService).generateTeams(any());
        }

        @Test
        void shouldCallTeamServiceToGenerateTeamsForFirstSeason() throws NATCException {
            final String expectedYear = "2112";

            when(leagueConfiguration.getFirstSeason()).thenReturn(expectedYear);

            leagueService.generateNewLeague();

            verify(teamService).generateTeams(expectedYear);
        }

        @Test
        void shouldCallManagerServiceToGenerateManagers() throws NATCException {
            leagueService.generateNewLeague();

            verify(managerService).generateManagers(any(), any());
        }

        @Test
        void shouldCallManagerServiceToGenerateTheInitialNumberOfManagers() throws NATCException {
            when(leagueConfiguration.getInitialManagers()).thenReturn(25);

            leagueService.generateNewLeague();

            verify(managerService).generateManagers(any(), eq(25));
        }

        @Test
        void shouldCallManagerServiceToGenerateManagersForTheFirstSeason() throws NATCException {
            when(leagueConfiguration.getFirstSeason()).thenReturn("1992");

            leagueService.generateNewLeague();

            verify(managerService).generateManagers(eq("1992"), any());
        }

        @Test
        void shouldCallPlayerServiceToGeneratePlayers() throws NATCException {
            leagueService.generateNewLeague();

            verify(playerService).generatePlayers(any(), any());
        }

        @Test
        void shouldCallPlayerServiceToGenerateTheInitialNumberOfPlayers() throws NATCException {
            when(leagueConfiguration.getInitialPlayers()).thenReturn(100);

            leagueService.generateNewLeague();

            verify(playerService).generatePlayers(any(), eq(100));
        }

        @Test
        void shouldCallPlayerServiceToGeneratePlayersForTheFirstSeason() throws NATCException {
            when(leagueConfiguration.getFirstSeason()).thenReturn("1992");

            leagueService.generateNewLeague();

            verify(playerService).generatePlayers(eq("1992"), any());
        }

        @Test
        void shouldUpdateAManagerReturnedByManagerServiceWithATeamReturnedByTeamService() throws NATCException {
            final Team team = Team.builder().teamId(123).allstarTeam(0).build();
            final Manager manager = Manager.builder().managerId(321).build();

            when(teamService.generateTeams(any())).thenReturn(Collections.singletonList(team));
            when(managerService.generateManagers(any(), any())).thenReturn(Collections.singletonList(manager));

            leagueService.generateNewLeague();

            verify(managerService).updateManager(manager);

            assertEquals(team.getTeamId(), manager.getTeamId());
        }

        @Test
        void shouldMarkTheManagerAsANewHireWhenUpdatingWithATeam() throws NATCException {
            final Team team = Team.builder().teamId(123).allstarTeam(0).build();
            final Manager manager = Manager.builder().managerId(321).build();

            when(teamService.generateTeams(any())).thenReturn(Collections.singletonList(team));
            when(managerService.generateManagers(any(), any())).thenReturn(Collections.singletonList(manager));

            leagueService.generateNewLeague();

            assertEquals(1, manager.getNewHire());
        }

        @Test
        void shouldUpdateAUniqueManagerForAllTeamsReturnedByTeamService() throws NATCException {
            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).allstarTeam(0).build(),
                    Team.builder().teamId(2).allstarTeam(0).build(),
                    Team.builder().teamId(3).allstarTeam(0).build(),
                    Team.builder().teamId(4).allstarTeam(0).build(),
                    Team.builder().teamId(5).allstarTeam(0).build()
            );

            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(101).build(),
                    Manager.builder().managerId(102).build(),
                    Manager.builder().managerId(103).build(),
                    Manager.builder().managerId(104).build(),
                    Manager.builder().managerId(105).build()
            );

            when(teamService.generateTeams(any())).thenReturn(teamList);
            when(managerService.generateManagers(any(), any())).thenReturn(managerList);

            leagueService.generateNewLeague();

            verify(managerService, times(teamList.size())).updateManager(any(Manager.class));

            final List<Integer> expectedTeamIds = teamList.stream().map(Team::getTeamId).sorted().toList();
            final List<Integer> actualTeamIds = managerList.stream().map(Manager::getTeamId).sorted().toList();

            assertEquals(expectedTeamIds, actualTeamIds);
        }

        @Test
        void shouldNotAssignAManagerToAnAllstarTeam() throws NATCException {
            final Team team = Team.builder().teamId(123).allstarTeam(1).build();
            final Manager manager = Manager.builder().managerId(321).build();

            when(teamService.generateTeams(any())).thenReturn(Collections.singletonList(team));
            when(managerService.generateManagers(any(), any())).thenReturn(Collections.singletonList(manager));

            leagueService.generateNewLeague();

            verify(managerService, never()).updateManager(manager);
        }

        @Test
        void shouldOnlyAssignAsManyManagersAsThereAreTeamsGivenMoreManagersThanTeams() throws NATCException {
            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).allstarTeam(0).build(),
                    Team.builder().teamId(2).allstarTeam(0).build(),
                    Team.builder().teamId(3).allstarTeam(0).build()
            );

            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(101).build(),
                    Manager.builder().managerId(102).build(),
                    Manager.builder().managerId(103).build(),
                    Manager.builder().managerId(104).build(),
                    Manager.builder().managerId(105).build()
            );

            when(teamService.generateTeams(any())).thenReturn(teamList);
            when(managerService.generateManagers(any(), any())).thenReturn(managerList);

            leagueService.generateNewLeague();

            verify(managerService, times(teamList.size())).updateManager(any(Manager.class));

            assertEquals(teamList.size(), managerList.stream().filter(manager -> Objects.nonNull(manager.getTeamId())).count());
        }

        @Test
        void shouldThrowLeagueProcessingExceptionIfThereAreMoreTeamsThanManagers() throws NATCException {
            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).allstarTeam(0).build(),
                    Team.builder().teamId(2).allstarTeam(0).build(),
                    Team.builder().teamId(3).allstarTeam(0).build(),
                    Team.builder().teamId(4).allstarTeam(0).build(),
                    Team.builder().teamId(5).allstarTeam(0).build()
            );

            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(101).build(),
                    Manager.builder().managerId(102).build(),
                    Manager.builder().managerId(103).build()
            );

            when(teamService.generateTeams(any())).thenReturn(teamList);
            when(managerService.generateManagers(any(), any())).thenReturn(managerList);

            assertThrows(LeagueProcessingException.class, () -> leagueService.generateNewLeague());
        }

        @Test
        void shouldSelectHighestRatedManager() throws NATCException {
            final Team team = Team.builder().teamId(123).allstarTeam(0).build();
            final Manager highRatedManager = Manager.builder().managerId(321).offense(0.8).defense(0.5).intangible(0.6).penalties(0.6).build();
            final Manager lowRatedManager = Manager.builder().managerId(322).offense(0.5).defense(0.5).intangible(0.3).penalties(0.2).build();

            when(teamService.generateTeams(any())).thenReturn(Collections.singletonList(team));
            when(managerService.generateManagers(any(), any())).thenReturn(List.of(lowRatedManager, highRatedManager));

            leagueService.generateNewLeague();

            verify(managerService).updateManager(highRatedManager);
        }

        @Test
        void shouldAssignManagersInOrderFromHighestRatedToLowestRated() throws NATCException {
            final ArgumentCaptor<Manager> captor = ArgumentCaptor.forClass(Manager.class);
            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).allstarTeam(0).build(),
                    Team.builder().teamId(2).allstarTeam(0).build(),
                    Team.builder().teamId(3).allstarTeam(0).build(),
                    Team.builder().teamId(4).allstarTeam(0).build(),
                    Team.builder().teamId(5).allstarTeam(0).build()
            );

            final List<Manager> managerList = Arrays.asList(
                    Manager.builder().managerId(101).offense(1.0).defense(1.0).intangible(1.0).penalties(1.0).build(),
                    Manager.builder().managerId(102).offense(0.8).defense(0.8).intangible(0.8).penalties(0.8).build(),
                    Manager.builder().managerId(103).offense(0.6).defense(0.6).intangible(0.6).penalties(0.6).build(),
                    Manager.builder().managerId(104).offense(0.4).defense(0.4).intangible(0.4).penalties(0.4).build(),
                    Manager.builder().managerId(105).offense(0.2).defense(0.2).intangible(0.2).penalties(0.2).build()
            );

            Collections.shuffle(managerList);

            when(teamService.generateTeams(any())).thenReturn(teamList);
            when(managerService.generateManagers(any(), any())).thenReturn(managerList);

            leagueService.generateNewLeague();

            verify(managerService, times(5)).updateManager(captor.capture());

            assertEquals(101, captor.getAllValues().get(0).getManagerId());
            assertEquals(102, captor.getAllValues().get(1).getManagerId());
            assertEquals(103, captor.getAllValues().get(2).getManagerId());
            assertEquals(104, captor.getAllValues().get(3).getManagerId());
            assertEquals(105, captor.getAllValues().get(4).getManagerId());
        }

        @Test
        void shouldRandomizeTeamOrderWhenAssigningManagers() throws NATCException {
            final ArgumentCaptor<Manager> captor = ArgumentCaptor.forClass(Manager.class);
            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).allstarTeam(0).build(),
                    Team.builder().teamId(2).allstarTeam(0).build(),
                    Team.builder().teamId(3).allstarTeam(0).build(),
                    Team.builder().teamId(4).allstarTeam(0).build(),
                    Team.builder().teamId(5).allstarTeam(0).build(),
                    Team.builder().teamId(6).allstarTeam(0).build(),
                    Team.builder().teamId(7).allstarTeam(0).build(),
                    Team.builder().teamId(8).allstarTeam(0).build(),
                    Team.builder().teamId(9).allstarTeam(0).build(),
                    Team.builder().teamId(10).allstarTeam(0).build()
            );

            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(101).build(),
                    Manager.builder().managerId(102).build(),
                    Manager.builder().managerId(103).build(),
                    Manager.builder().managerId(104).build(),
                    Manager.builder().managerId(105).build(),
                    Manager.builder().managerId(106).build(),
                    Manager.builder().managerId(107).build(),
                    Manager.builder().managerId(108).build(),
                    Manager.builder().managerId(109).build(),
                    Manager.builder().managerId(110).build()
            );

            when(teamService.generateTeams(any())).thenReturn(teamList);
            when(managerService.generateManagers(any(), any())).thenReturn(managerList);

            final List<Integer> startingTeamIds = teamList.stream().map(Team::getTeamId).toList();

            leagueService.generateNewLeague();

            verify(managerService, times(10)).updateManager(captor.capture());

            final List<Integer> actualTeamIds = captor.getAllValues().stream().map(Manager::getTeamId).toList();

            assertNotEquals(startingTeamIds, actualTeamIds);
        }

        @Test
        void shouldUpdateAPlayerReturnedByPlayerServiceWithATeamReturnedByTeamService() throws NATCException {
            final Team team = Team.builder().teamId(123).allstarTeam(0).build();
            final Manager manager = Manager.builder().managerId(321).style(ManagerStyle.BALANCED.getValue()).build();
            final Player player = Player.builder().playerId(555).build();

            when(teamService.generateTeams(any())).thenReturn(Collections.singletonList(team));
            when(managerService.generateManagers(any(), any())).thenReturn(Collections.singletonList(manager));
            when(playerService.generatePlayers(any(), any())).thenReturn(Collections.singletonList(player));
            when(leagueConfiguration.getPlayersPerTeam()).thenReturn(1);

            leagueService.generateNewLeague();

            verify(playerService).updatePlayer(player);

            assertEquals(team.getTeamId(), player.getTeamId());
        }

        @Test
        void shouldUpdateAsManyUniquePlayersWithSameTeamAsPlayersPerTeamConfiguration() throws NATCException {
            final ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);
            final Team team = Team.builder().teamId(123).allstarTeam(0).build();
            final Manager manager = Manager.builder().managerId(321).style(ManagerStyle.BALANCED.getValue()).build();
            final List<Player> playerList = List.of(
                    Player.builder().playerId(201).build(),
                    Player.builder().playerId(202).build(),
                    Player.builder().playerId(203).build(),
                    Player.builder().playerId(204).build(),
                    Player.builder().playerId(205).build()
            );

            when(teamService.generateTeams(any())).thenReturn(Collections.singletonList(team));
            when(managerService.generateManagers(any(), any())).thenReturn(Collections.singletonList(manager));
            when(playerService.generatePlayers(any(), any())).thenReturn(playerList);
            when(leagueConfiguration.getPlayersPerTeam()).thenReturn(5);

            leagueService.generateNewLeague();

            verify(playerService, times(5)).updatePlayer(captor.capture());

            assertEquals(5, captor.getAllValues().stream().filter(player -> player.getTeamId() == 123).count());

            final List<Integer> expectedPlayerIds = playerList.stream().map(Player::getTeamId).sorted().toList();
            final List<Integer> actualPlayerIds = captor.getAllValues().stream().map(Player::getTeamId).sorted().toList();

            assertEquals(expectedPlayerIds, actualPlayerIds);
        }

        @Test
        void shouldUpdateAUniquePlayerForAllTeamsReturnedByTeamService() throws NATCException {
            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).allstarTeam(0).build(),
                    Team.builder().teamId(2).allstarTeam(0).build(),
                    Team.builder().teamId(3).allstarTeam(0).build(),
                    Team.builder().teamId(4).allstarTeam(0).build(),
                    Team.builder().teamId(5).allstarTeam(0).build()
            );

            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(101).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(102).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(103).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(104).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(105).style(ManagerStyle.BALANCED.getValue()).build()
            );

            final List<Player> playerList = List.of(
                    Player.builder().playerId(201).build(),
                    Player.builder().playerId(202).build(),
                    Player.builder().playerId(203).build(),
                    Player.builder().playerId(204).build(),
                    Player.builder().playerId(205).build()
            );

            when(teamService.generateTeams(any())).thenReturn(teamList);
            when(managerService.generateManagers(any(), any())).thenReturn(managerList);
            when(playerService.generatePlayers(any(), any())).thenReturn(playerList);
            when(leagueConfiguration.getPlayersPerTeam()).thenReturn(1);

            leagueService.generateNewLeague();

            verify(playerService, times(teamList.size())).updatePlayer(any(Player.class));

            final List<Integer> expectedTeamIds = teamList.stream().map(Team::getTeamId).sorted().toList();
            final List<Integer> actualTeamIds = playerList.stream().map(Player::getTeamId).sorted().toList();

            assertEquals(expectedTeamIds, actualTeamIds);
        }

        @Test
        void shouldAssignAsManyPlayersAsThereAreTeamsTimesPlayersPerTeam() throws NATCException {
            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).allstarTeam(0).build(),
                    Team.builder().teamId(2).allstarTeam(0).build(),
                    Team.builder().teamId(3).allstarTeam(0).build()
            );

            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(101).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(102).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(103).style(ManagerStyle.BALANCED.getValue()).build()
            );

            final List<Player> playerList = List.of(
                    Player.builder().playerId(201).build(),
                    Player.builder().playerId(202).build(),
                    Player.builder().playerId(203).build(),
                    Player.builder().playerId(204).build(),
                    Player.builder().playerId(205).build(),
                    Player.builder().playerId(206).build(),
                    Player.builder().playerId(207).build(),
                    Player.builder().playerId(208).build(),
                    Player.builder().playerId(209).build()
            );

            when(teamService.generateTeams(any())).thenReturn(teamList);
            when(managerService.generateManagers(any(), any())).thenReturn(managerList);
            when(playerService.generatePlayers(any(), any())).thenReturn(playerList);
            when(leagueConfiguration.getPlayersPerTeam()).thenReturn(3);

            leagueService.generateNewLeague();

            verify(playerService, times(teamList.size() * 3)).updatePlayer(any(Player.class));
        }

        @Test
        void shouldNotAssignAPlayerToAnAllstarTeam() throws NATCException {
            final Team team = Team.builder().teamId(123).allstarTeam(1).build();
            final Manager manager = Manager.builder().managerId(321).style(ManagerStyle.BALANCED.getValue()).build();
            final Player player = Player.builder().playerId(555).build();

            when(teamService.generateTeams(any())).thenReturn(Collections.singletonList(team));
            when(managerService.generateManagers(any(), any())).thenReturn(Collections.singletonList(manager));
            when(playerService.generatePlayers(any(), any())).thenReturn(Collections.singletonList(player));
            when(leagueConfiguration.getPlayersPerTeam()).thenReturn(1);

            leagueService.generateNewLeague();

            verify(playerService, never()).updatePlayer(any());
        }

        @Test
        void shouldOnlyAssignAsManyPlayersAsThereAreTeamsGivenMorePlayersThanTeams() throws NATCException {
            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).allstarTeam(0).build(),
                    Team.builder().teamId(2).allstarTeam(0).build(),
                    Team.builder().teamId(3).allstarTeam(0).build()
            );

            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(101).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(102).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(103).style(ManagerStyle.BALANCED.getValue()).build()
            );

            final List<Player> playerList = List.of(
                    Player.builder().playerId(201).build(),
                    Player.builder().playerId(202).build(),
                    Player.builder().playerId(203).build(),
                    Player.builder().playerId(204).build(),
                    Player.builder().playerId(205).build()
            );

            when(teamService.generateTeams(any())).thenReturn(teamList);
            when(managerService.generateManagers(any(), any())).thenReturn(managerList);
            when(playerService.generatePlayers(any(), any())).thenReturn(playerList);
            when(leagueConfiguration.getPlayersPerTeam()).thenReturn(1);

            leagueService.generateNewLeague();

            verify(playerService, times(teamList.size())).updatePlayer(any(Player.class));

            assertEquals(teamList.size(), playerList.stream().filter(player -> Objects.nonNull(player.getTeamId())).count());
        }

        @Test
        void shouldThrowLeagueProcessingExceptionIfThereAreMoreTeamsThanPlayers() throws NATCException {
            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).allstarTeam(0).build(),
                    Team.builder().teamId(2).allstarTeam(0).build(),
                    Team.builder().teamId(3).allstarTeam(0).build(),
                    Team.builder().teamId(4).allstarTeam(0).build(),
                    Team.builder().teamId(5).allstarTeam(0).build()
            );

            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(101).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(102).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(103).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(104).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(105).style(ManagerStyle.BALANCED.getValue()).build()
            );

            final List<Player> playerList = List.of(
                    Player.builder().playerId(201).build(),
                    Player.builder().playerId(202).build(),
                    Player.builder().playerId(203).build()
            );

            when(teamService.generateTeams(any())).thenReturn(teamList);
            when(managerService.generateManagers(any(), any())).thenReturn(managerList);
            when(playerService.generatePlayers(any(), any())).thenReturn(playerList);
            when(leagueConfiguration.getPlayersPerTeam()).thenReturn(1);

            assertThrows(LeagueProcessingException.class, () -> leagueService.generateNewLeague());
        }

        @Test
        void shouldThrowLeagueProcessingExceptionIfThereAreNotEnoughPlayersConsideringPlayersPerTeam() throws NATCException {
            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).allstarTeam(0).build(),
                    Team.builder().teamId(2).allstarTeam(0).build(),
                    Team.builder().teamId(3).allstarTeam(0).build(),
                    Team.builder().teamId(4).allstarTeam(0).build(),
                    Team.builder().teamId(5).allstarTeam(0).build()
            );

            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(101).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(102).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(103).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(104).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(105).style(ManagerStyle.BALANCED.getValue()).build()
            );

            final List<Player> playerList = List.of(
                    Player.builder().playerId(201).build(),
                    Player.builder().playerId(202).build(),
                    Player.builder().playerId(203).build(),
                    Player.builder().playerId(204).build(),
                    Player.builder().playerId(205).build(),
                    Player.builder().playerId(206).build(),
                    Player.builder().playerId(207).build(),
                    Player.builder().playerId(208).build(),
                    Player.builder().playerId(209).build(),
                    Player.builder().playerId(210).build()
            );

            when(teamService.generateTeams(any())).thenReturn(teamList);
            when(managerService.generateManagers(any(), any())).thenReturn(managerList);
            when(playerService.generatePlayers(any(), any())).thenReturn(playerList);
            when(leagueConfiguration.getPlayersPerTeam()).thenReturn(3);

            assertThrows(LeagueProcessingException.class, () -> leagueService.generateNewLeague());
        }

        @Test
        void shouldReverseTheTeamOrderForEachRoundOfPlayerAssignment() throws NATCException {
            final ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);
            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).allstarTeam(0).build(),
                    Team.builder().teamId(2).allstarTeam(0).build(),
                    Team.builder().teamId(3).allstarTeam(0).build()
            );

            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(101).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(102).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(103).style(ManagerStyle.BALANCED.getValue()).build()
            );

            final List<Player> playerList = List.of(
                    Player.builder().playerId(201).build(),
                    Player.builder().playerId(202).build(),
                    Player.builder().playerId(203).build(),
                    Player.builder().playerId(204).build(),
                    Player.builder().playerId(205).build(),
                    Player.builder().playerId(206).build(),
                    Player.builder().playerId(207).build(),
                    Player.builder().playerId(208).build(),
                    Player.builder().playerId(209).build()
            );

            when(teamService.generateTeams(any())).thenReturn(teamList);
            when(managerService.generateManagers(any(), any())).thenReturn(managerList);
            when(playerService.generatePlayers(any(), any())).thenReturn(playerList);
            when(leagueConfiguration.getPlayersPerTeam()).thenReturn(3);

            leagueService.generateNewLeague();

            verify(playerService, times(9)).updatePlayer(captor.capture());

            final List<Integer> firstRound = captor.getAllValues().subList(0, 3).stream().map(Player::getTeamId).toList();
            final List<Integer> secondRound = captor.getAllValues().subList(3, 6).stream().map(Player::getTeamId).toList();
            final List<Integer> thirdRound = captor.getAllValues().subList(6, 9).stream().map(Player::getTeamId).toList();

            assertEquals(firstRound.get(0), secondRound.get(2));
            assertEquals(firstRound.get(1), secondRound.get(1));
            assertEquals(firstRound.get(2), secondRound.getFirst());

            assertEquals(firstRound.get(0), thirdRound.getFirst());
            assertEquals(firstRound.get(1), thirdRound.get(1));
            assertEquals(firstRound.get(2), thirdRound.get(2));
        }

        @Test
        void shouldSelectHighestRatedPlayer() throws NATCException {
            final Team team = Team.builder().teamId(123).allstarTeam(0).build();
            final Manager manager = Manager.builder().managerId(321).style(ManagerStyle.BALANCED.getValue()).build();
            final Player highRatedPlayer = Player.builder().playerId(101)
                    .scoring(0.8).passing(0.8).blocking(0.8)
                    .tackling(0.8).stealing(0.8).presence(0.8)
                    .discipline(0.8).endurance(0.8)
                    .build();
            final Player lowRatedPlayer = Player.builder().playerId(102)
                    .scoring(0.3).passing(0.3).blocking(0.3)
                    .tackling(0.3).stealing(0.3).presence(0.3)
                    .discipline(0.3).endurance(0.3)
                    .build();

            when(teamService.generateTeams(any())).thenReturn(Collections.singletonList(team));
            when(managerService.generateManagers(any(), any())).thenReturn(Collections.singletonList(manager));
            when(playerService.generatePlayers(any(), any())).thenReturn(List.of(lowRatedPlayer, highRatedPlayer));
            when(leagueConfiguration.getPlayersPerTeam()).thenReturn(1);

            leagueService.generateNewLeague();

            verify(playerService).updatePlayer(highRatedPlayer);
        }

        @Test
        void shouldAssignPlayersInOrderFromHighestRatedToLowestRatedAcrossTeams() throws NATCException {
            final ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);
            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).allstarTeam(0).build(),
                    Team.builder().teamId(2).allstarTeam(0).build(),
                    Team.builder().teamId(3).allstarTeam(0).build(),
                    Team.builder().teamId(4).allstarTeam(0).build(),
                    Team.builder().teamId(5).allstarTeam(0).build()
            );

            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(101).style(ManagerStyle.BALANCED.getValue()).build(),
                    Manager.builder().managerId(102).style(ManagerStyle.OFFENSIVE.getValue()).build(),
                    Manager.builder().managerId(103).style(ManagerStyle.DEFENSIVE.getValue()).build(),
                    Manager.builder().managerId(104).style(ManagerStyle.INTANGIBLE.getValue()).build(),
                    Manager.builder().managerId(105).style(ManagerStyle.PENALTIES.getValue()).build()
            );

            final List<Player> playerList = Arrays.asList(
                    Player.builder().playerId(201).scoring(1.0).passing(1.0).blocking(1.0).tackling(1.0).stealing(1.0).presence(1.0)
                            .discipline(1.0).endurance(1.0).penaltyShot(1.0).penaltyOffense(1.0).penaltyDefense(1.0).build(),
                    Player.builder().playerId(202).scoring(0.8).passing(0.8).blocking(0.8).tackling(0.8).stealing(0.8).presence(0.8)
                            .discipline(0.8).endurance(0.8).penaltyShot(0.8).penaltyOffense(0.8).penaltyDefense(0.8).build(),
                    Player.builder().playerId(203).scoring(0.6).passing(0.6).blocking(0.6).tackling(0.6).stealing(0.6).presence(0.6)
                            .discipline(0.6).endurance(0.6).penaltyShot(0.6).penaltyOffense(0.6).penaltyDefense(0.6).build(),
                    Player.builder().playerId(204).scoring(0.4).passing(0.4).blocking(0.4).tackling(0.4).stealing(0.4).presence(0.4)
                            .discipline(0.4).endurance(0.4).penaltyShot(0.4).penaltyOffense(0.4).penaltyDefense(0.4).build(),
                    Player.builder().playerId(205).scoring(0.2).passing(0.2).blocking(0.2).tackling(0.2).stealing(0.2).presence(0.2)
                            .discipline(0.2).endurance(0.2).penaltyShot(0.2).penaltyOffense(0.2).penaltyDefense(0.2).build()
            );

            Collections.shuffle(playerList);

            when(teamService.generateTeams(any())).thenReturn(teamList);
            when(managerService.generateManagers(any(), any())).thenReturn(managerList);
            when(playerService.generatePlayers(any(), any())).thenReturn(playerList);
            when(leagueConfiguration.getPlayersPerTeam()).thenReturn(1);

            leagueService.generateNewLeague();

            verify(playerService, times(teamList.size())).updatePlayer(captor.capture());

            assertEquals(201, captor.getAllValues().get(0).getPlayerId());
            assertEquals(202, captor.getAllValues().get(1).getPlayerId());
            assertEquals(203, captor.getAllValues().get(2).getPlayerId());
            assertEquals(204, captor.getAllValues().get(3).getPlayerId());
            assertEquals(205, captor.getAllValues().get(4).getPlayerId());
        }

        @Test
        void shouldAssignPlayersInOrderFromHighestRatedToLowestRatedForATeam() throws NATCException {
            final ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);
            final List<Team> teamList = Collections.singletonList(
                    Team.builder().teamId(1).allstarTeam(0).build()
            );

            final List<Manager> managerList = Collections.singletonList(
                    Manager.builder().managerId(101).style(ManagerStyle.BALANCED.getValue()).build()
            );

            final List<Player> playerList = Arrays.asList(
                    Player.builder().playerId(201).scoring(1.0).passing(1.0).blocking(1.0).tackling(1.0).stealing(1.0).presence(1.0)
                            .discipline(1.0).endurance(1.0).penaltyShot(1.0).penaltyOffense(1.0).penaltyDefense(1.0).build(),
                    Player.builder().playerId(202).scoring(0.8).passing(0.8).blocking(0.8).tackling(0.8).stealing(0.8).presence(0.8)
                            .discipline(0.8).endurance(0.8).penaltyShot(0.8).penaltyOffense(0.8).penaltyDefense(0.8).build(),
                    Player.builder().playerId(203).scoring(0.6).passing(0.6).blocking(0.6).tackling(0.6).stealing(0.6).presence(0.6)
                            .discipline(0.6).endurance(0.6).penaltyShot(0.6).penaltyOffense(0.6).penaltyDefense(0.6).build(),
                    Player.builder().playerId(204).scoring(0.4).passing(0.4).blocking(0.4).tackling(0.4).stealing(0.4).presence(0.4)
                            .discipline(0.4).endurance(0.4).penaltyShot(0.4).penaltyOffense(0.4).penaltyDefense(0.4).build(),
                    Player.builder().playerId(205).scoring(0.2).passing(0.2).blocking(0.2).tackling(0.2).stealing(0.2).presence(0.2)
                            .discipline(0.2).endurance(0.2).penaltyShot(0.2).penaltyOffense(0.2).penaltyDefense(0.2).build()
            );

            Collections.shuffle(playerList);

            when(teamService.generateTeams(any())).thenReturn(teamList);
            when(managerService.generateManagers(any(), any())).thenReturn(managerList);
            when(playerService.generatePlayers(any(), any())).thenReturn(playerList);
            when(leagueConfiguration.getPlayersPerTeam()).thenReturn(5);

            leagueService.generateNewLeague();

            verify(playerService, times(playerList.size())).updatePlayer(captor.capture());

            assertEquals(201, captor.getAllValues().get(0).getPlayerId());
            assertEquals(202, captor.getAllValues().get(1).getPlayerId());
            assertEquals(203, captor.getAllValues().get(2).getPlayerId());
            assertEquals(204, captor.getAllValues().get(3).getPlayerId());
            assertEquals(205, captor.getAllValues().get(4).getPlayerId());
        }

        @Test
        void shouldSelectHighestRatedOffensivePlayerWhenTheTeamManagerStyleIsOffensive() throws NATCException {
            final Team team = Team.builder().teamId(123).allstarTeam(0).build();
            final Manager manager = Manager.builder().managerId(321).style(ManagerStyle.OFFENSIVE.getValue()).build();
            final Player highRatedPlayer = Player.builder().playerId(101)
                    .scoring(0.5).passing(0.5).blocking(0.5).tackling(0.1).stealing(0.1).presence(0.1)
                    .discipline(0.1).endurance(0.1).penaltyShot(0.1).penaltyOffense(0.1).penaltyDefense(0.1)
                    .build();
            final Player lowRatedPlayer = Player.builder().playerId(102)
                    .scoring(0.3).passing(0.3).blocking(0.3).tackling(0.8).stealing(0.8).presence(0.8)
                    .discipline(0.8).endurance(0.8).penaltyShot(0.8).penaltyOffense(0.8).penaltyDefense(0.8)
                    .build();

            when(teamService.generateTeams(any())).thenReturn(Collections.singletonList(team));
            when(managerService.generateManagers(any(), any())).thenReturn(Collections.singletonList(manager));
            when(playerService.generatePlayers(any(), any())).thenReturn(List.of(lowRatedPlayer, highRatedPlayer));
            when(leagueConfiguration.getPlayersPerTeam()).thenReturn(1);

            leagueService.generateNewLeague();

            verify(playerService).updatePlayer(highRatedPlayer);
        }

        @Test
        void shouldSelectHighestRatedDefensivePlayerWhenTheTeamManagerStyleIsDefensive() throws NATCException {
            final Team team = Team.builder().teamId(123).allstarTeam(0).build();
            final Manager manager = Manager.builder().managerId(321).style(ManagerStyle.DEFENSIVE.getValue()).build();
            final Player highRatedPlayer = Player.builder().playerId(101)
                    .scoring(0.1).passing(0.1).blocking(0.1).tackling(0.5).stealing(0.5).presence(0.5)
                    .discipline(0.1).endurance(0.1).penaltyShot(0.1).penaltyOffense(0.1).penaltyDefense(0.1)
                    .build();
            final Player lowRatedPlayer = Player.builder().playerId(102)
                    .scoring(0.8).passing(0.8).blocking(0.8).tackling(0.3).stealing(0.3).presence(0.3)
                    .discipline(0.8).endurance(0.8).penaltyShot(0.8).penaltyOffense(0.8).penaltyDefense(0.8)
                    .build();

            when(teamService.generateTeams(any())).thenReturn(Collections.singletonList(team));
            when(managerService.generateManagers(any(), any())).thenReturn(Collections.singletonList(manager));
            when(playerService.generatePlayers(any(), any())).thenReturn(List.of(lowRatedPlayer, highRatedPlayer));
            when(leagueConfiguration.getPlayersPerTeam()).thenReturn(1);

            leagueService.generateNewLeague();

            verify(playerService).updatePlayer(highRatedPlayer);
        }

        @Test
        void shouldSelectHighestRatedIntangiblePlayerWhenTheTeamManagerStyleIsIntangible() throws NATCException {
            final Team team = Team.builder().teamId(123).allstarTeam(0).build();
            final Manager manager = Manager.builder().managerId(321).style(ManagerStyle.INTANGIBLE.getValue()).build();
            final Player highRatedPlayer = Player.builder().playerId(101)
                    .scoring(0.1).passing(0.1).blocking(0.5).tackling(0.1).stealing(0.1).presence(0.5)
                    .discipline(0.5).endurance(0.5).penaltyShot(0.1).penaltyOffense(0.1).penaltyDefense(0.1)
                    .build();
            final Player lowRatedPlayer = Player.builder().playerId(102)
                    .scoring(0.8).passing(0.8).blocking(0.3).tackling(0.8).stealing(0.8).presence(0.3)
                    .discipline(0.3).endurance(0.3).penaltyShot(0.8).penaltyOffense(0.8).penaltyDefense(0.8)
                    .build();

            when(teamService.generateTeams(any())).thenReturn(Collections.singletonList(team));
            when(managerService.generateManagers(any(), any())).thenReturn(Collections.singletonList(manager));
            when(playerService.generatePlayers(any(), any())).thenReturn(List.of(lowRatedPlayer, highRatedPlayer));
            when(leagueConfiguration.getPlayersPerTeam()).thenReturn(1);

            leagueService.generateNewLeague();

            verify(playerService).updatePlayer(highRatedPlayer);
        }

        @Test
        void shouldSelectHighestRatedPenaltiesPlayerWhenTheTeamManagerStyleIsPenalties() throws NATCException {
            final Team team = Team.builder().teamId(123).allstarTeam(0).build();
            final Manager manager = Manager.builder().managerId(321).style(ManagerStyle.PENALTIES.getValue()).build();
            final Player highRatedPlayer = Player.builder().playerId(101)
                    .scoring(0.1).passing(0.1).blocking(0.5).tackling(0.1).stealing(0.1).presence(0.1)
                    .discipline(0.1).endurance(0.1).penaltyShot(0.5).penaltyOffense(0.5).penaltyDefense(0.5)
                    .build();
            final Player lowRatedPlayer = Player.builder().playerId(102)
                    .scoring(0.8).passing(0.8).blocking(0.8).tackling(0.8).stealing(0.8).presence(0.8)
                    .discipline(0.8).endurance(0.8).penaltyShot(0.3).penaltyOffense(0.3).penaltyDefense(0.3)
                    .build();

            when(teamService.generateTeams(any())).thenReturn(Collections.singletonList(team));
            when(managerService.generateManagers(any(), any())).thenReturn(Collections.singletonList(manager));
            when(playerService.generatePlayers(any(), any())).thenReturn(List.of(lowRatedPlayer, highRatedPlayer));
            when(leagueConfiguration.getPlayersPerTeam()).thenReturn(1);

            leagueService.generateNewLeague();

            verify(playerService).updatePlayer(highRatedPlayer);
        }

        @Test
        void shouldSelectHighestRatedPlayerWhenTheTeamManagerStyleIsBalanced() throws NATCException {
            final Team team = Team.builder().teamId(123).allstarTeam(0).build();
            final Manager manager = Manager.builder().managerId(321).style(ManagerStyle.BALANCED.getValue()).build();
            final Player highRatedPlayer = Player.builder().playerId(101)
                    .scoring(0.5).passing(0.5).blocking(0.5).tackling(0.5).stealing(0.5).presence(0.5)
                    .discipline(0.5).endurance(0.5).penaltyShot(0.1).penaltyOffense(0.1).penaltyDefense(0.1)
                    .build();
            final Player lowRatedPlayer = Player.builder().playerId(102)
                    .scoring(0.4).passing(0.5).blocking(0.4).tackling(0.5).stealing(0.4).presence(0.5)
                    .discipline(0.4).endurance(0.5).penaltyShot(1.0).penaltyOffense(1.0).penaltyDefense(1.0)
                    .build();

            when(teamService.generateTeams(any())).thenReturn(Collections.singletonList(team));
            when(managerService.generateManagers(any(), any())).thenReturn(Collections.singletonList(manager));
            when(playerService.generatePlayers(any(), any())).thenReturn(List.of(lowRatedPlayer, highRatedPlayer));
            when(leagueConfiguration.getPlayersPerTeam()).thenReturn(1);

            leagueService.generateNewLeague();

            verify(playerService).updatePlayer(highRatedPlayer);
        }
    }

    @Nested
    class UpdateLeagueForNewSeason {

        @Test
        void shouldCallTeamServiceToUpdateTeamsForNewSeason() {
            leagueService.updateLeagueForNewSeason(null, null);

            verify(teamService).updateTeamsForNewSeason(any(), any());
        }

        @Test
        void shouldPassTheGivenPreviousAndNewYearsToTheTeamService() {
            leagueService.updateLeagueForNewSeason("2001", "2002");

            verify(teamService).updateTeamsForNewSeason("2001", "2002");
        }

        @Test
        void shouldCallManagerServiceToUpdateManagersForNewSeason() {
            leagueService.updateLeagueForNewSeason(null, null);

            verify(managerService).updateManagersForNewSeason(any(), any());
        }

        @Test
        void shouldPassTheGivenPreviousAndNewYearsToTheManagerService() {
            leagueService.updateLeagueForNewSeason("2001", "2002");

            verify(managerService).updateManagersForNewSeason("2001", "2002");
        }

        @Test
        void shouldCallPlayerServiceToUpdatePlayersForNewSeason() {
            leagueService.updateLeagueForNewSeason(null, null);

            verify(playerService).updatePlayersForNewSeason(any(), any());
        }

        @Test
        void shouldPassTheGivenPreviousAndNewYearsToThePlayerService() {
            leagueService.updateLeagueForNewSeason("2001", "2002");

            verify(playerService).updatePlayersForNewSeason("2001", "2002");
        }
    }
}