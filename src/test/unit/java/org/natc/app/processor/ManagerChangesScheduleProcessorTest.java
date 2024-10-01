package org.natc.app.processor;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.*;
import org.natc.app.exception.NATCException;
import org.natc.app.exception.ScheduleProcessingException;
import org.natc.app.service.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagerChangesScheduleProcessorTest {

    @Captor
    private ArgumentCaptor<List<Manager>> managerCaptor;

    @Captor
    private ArgumentCaptor<List<Team>> teamCaptor;

    @Mock
    private LeagueConfiguration leagueConfiguration;

    @Mock
    private TeamManagerDraftService teamManagerDraftService;

    @Mock
    private PlayerService playerService;

    @Mock
    private ManagerService managerService;

    @Mock
    private TeamService teamService;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ManagerChangesScheduleProcessor processor;

    @Nested
    class Process {
        @Test
        void shouldThrowExceptionWhenScheduleTypeIsMissing() {
            final Schedule schedule = Schedule.builder().year("2019").build();

            assertThrows(ScheduleProcessingException.class, () -> processor.process(schedule));
        }

        @Test
        void shouldThrowExceptionWhenGivenIncorrectScheduleEventType() throws NATCException {
            final Schedule schedule = Schedule.builder()
                    .type(ScheduleType.BEGINNING_OF_SEASON.getValue())
                    .year("2019")
                    .build();

            assertThrows(ScheduleProcessingException.class, () -> processor.process(schedule));
        }

        @Test
        void shouldCallTheScheduleServiceToUpdateTheScheduleEntry() throws NATCException {
            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(scheduleService).updateScheduleEntry(any());
        }

        @Test
        void shouldUpdateTheScheduleEntryStatusToCompleted() throws NATCException {
            final Schedule schedule = Schedule.builder()
                    .type(ScheduleType.MANAGER_CHANGES.getValue())
                    .year("2001")
                    .sequence(1)
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .build();
            final ArgumentCaptor<Schedule> captor = ArgumentCaptor.forClass(Schedule.class);

            processor.process(schedule);

            verify(scheduleService).updateScheduleEntry(captor.capture());

            assertSame(schedule, captor.getValue());
            assertEquals(ScheduleStatus.COMPLETED.getValue(), captor.getValue().getStatus());
        }

        @Test
        void shouldCallManagerServiceToGetActiveManagersForSameYearAsEvent() throws NATCException {
            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(managerService).getActiveManagersForYear("2005");
        }

        @Test
        void shouldCallManagerServiceToUpdateTheManagersFoundForTheGivenYear() throws NATCException {
            final List<Manager> managerList = Arrays.asList(
                    Manager.builder().managerId(1).year("2005").age(50).vitality(0.5).build(),
                    Manager.builder().managerId(2).year("2005").age(50).vitality(0.5).build(),
                    Manager.builder().managerId(3).year("2005").age(50).vitality(0.5).build()
            );

            when(managerService.getActiveManagersForYear(any())).thenReturn(managerList);

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(managerService).updateManagers(managerList);
        }

        @Test
        void shouldIncrementTheAgeOfEachManagerFoundForTheGivenYear() throws NATCException {
            final List<Manager> managerList = Arrays.asList(
                    Manager.builder().managerId(1).year("2005").age(45).vitality(0.5).build(),
                    Manager.builder().managerId(2).year("2005").age(37).vitality(0.5).build(),
                    Manager.builder().managerId(3).year("2005").age(62).vitality(0.5).build()
            );

            when(managerService.getActiveManagersForYear(any())).thenReturn(managerList);

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            assertEquals(46, managerList.get(0).getAge());
            assertEquals(38, managerList.get(1).getAge());
            assertEquals(63, managerList.get(2).getAge());
        }

        @Test
        void shouldMarkRetiredAnyFreeManagersThatAreNowReadyToRetire() throws NATCException {
            final List<Manager> managerList = Arrays.asList(
                    Manager.builder().managerId(1).year("2005").age(55).vitality(0.2).retired(0).build(),
                    Manager.builder().managerId(2).year("2005").age(47).vitality(0.5).retired(0).build(),
                    Manager.builder().managerId(3).year("2005").age(62).vitality(0.5).retired(0).build()
            );

            when(managerService.getActiveManagersForYear(any())).thenReturn(managerList);

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            assertEquals(1, managerList.get(0).getRetired());
            assertEquals(0, managerList.get(1).getRetired());
            assertEquals(1, managerList.get(2).getRetired());
        }

        @Test
        void shouldMarkRetiredAnyTeamManagersThatAreNowReadyToRetire() throws NATCException {
            final List<Manager> managerList = Arrays.asList(
                    Manager.builder().managerId(1).year("2005").teamId(4).age(55).vitality(0.2).retired(0).build(),
                    Manager.builder().managerId(2).year("2005").teamId(5).age(47).vitality(0.5).retired(0).build(),
                    Manager.builder().managerId(3).year("2005").teamId(6).age(62).vitality(0.5).retired(0).build()
            );

            when(managerService.getActiveManagersForYear(any())).thenReturn(managerList);

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            assertEquals(1, managerList.get(0).getRetired());
            assertEquals(0, managerList.get(1).getRetired());
            assertEquals(1, managerList.get(2).getRetired());
        }

        @Test
        void shouldSetFormerTeamIdToTeamIdAndClearTeamIdForTeamManagersThatRetire() throws NATCException {
            final Manager manager = Manager.builder()
                    .managerId(1)
                    .year("2005")
                    .teamId(4)
                    .age(55)
                    .vitality(0.2)
                    .retired(0)
                    .build();

            when(managerService.getActiveManagersForYear(any())).thenReturn(Collections.singletonList(manager));

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            assertEquals(1, manager.getRetired());
            assertEquals(4, manager.getFormerTeamId());
            assertNull(manager.getTeamId());
        }

        @Test
        void shouldCallTeamServiceForManagerToDetermineIfManagerShouldBeReleased() throws NATCException {
            final Manager manager = Manager.builder()
                    .managerId(1)
                    .year("2005")
                    .teamId(4)
                    .age(45)
                    .vitality(1.0)
                    .build();

            when(managerService.getActiveManagersForYear(any())).thenReturn(Collections.singletonList(manager));

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(teamService).willTeamReleaseManager(manager);
        }

        @Test
        void shouldCallTeamServiceForEveryApplicableManagerToDetermineIfManagerShouldBeReleased() throws NATCException {
            final List<Manager> managerList = Arrays.asList(
                    Manager.builder().managerId(1).year("2005").teamId(4).age(45).vitality(0.2).retired(0).build(),
                    Manager.builder().managerId(2).year("2005").teamId(5).age(45).vitality(0.5).retired(0).build(),
                    Manager.builder().managerId(3).year("2005").teamId(6).age(45).vitality(0.5).retired(0).build()
            );

            when(managerService.getActiveManagersForYear(any())).thenReturn(managerList);

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(teamService, times(3)).willTeamReleaseManager(any(Manager.class));
        }

        @Test
        void shouldNotCallTeamServiceForManagerIfManagerDoesNotHaveATeam() throws NATCException {
            final Manager manager = Manager.builder()
                    .managerId(1)
                    .year("2005")
                    .age(45)
                    .vitality(1.0)
                    .build();

            when(managerService.getActiveManagersForYear(any())).thenReturn(Collections.singletonList(manager));

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(teamService, never()).willTeamReleaseManager(manager);
        }

        @Test
        void shouldNotCallTeamServiceForManagerIfManagerRetires() throws NATCException {
            final Manager manager = Manager.builder()
                    .managerId(1)
                    .year("2005")
                    .teamId(4)
                    .age(55)
                    .vitality(0.2)
                    .retired(0)
                    .build();

            when(managerService.getActiveManagersForYear(any())).thenReturn(Collections.singletonList(manager));

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(teamService, never()).willTeamReleaseManager(manager);
        }

        @Test
        void shouldMarkManagerAsReleasedIfTeamWillReleaseTheManager() throws NATCException {
            final Manager manager = Manager.builder()
                    .managerId(1)
                    .year("2005")
                    .teamId(4)
                    .age(45)
                    .vitality(1.0)
                    .released(0)
                    .build();

            when(managerService.getActiveManagersForYear(any())).thenReturn(Collections.singletonList(manager));
            when(teamService.willTeamReleaseManager(manager)).thenReturn(true);

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            assertEquals(1, manager.getReleased());
        }

        @Test
        void shouldNotMarkManagerAsReleasedIfTeamWillNotReleaseTheManager() throws NATCException {
            final Manager manager = Manager.builder()
                    .managerId(1)
                    .year("2005")
                    .teamId(4)
                    .age(45)
                    .vitality(1.0)
                    .released(0)
                    .build();

            when(managerService.getActiveManagersForYear(any())).thenReturn(Collections.singletonList(manager));
            when(teamService.willTeamReleaseManager(manager)).thenReturn(false);

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            assertEquals(0, manager.getReleased());
        }

        @Test
        void shouldSetFormerTeamIdAndClearTeamIdForManagersThatAreReleased() throws NATCException {
            final Manager manager = Manager.builder()
                    .managerId(1)
                    .year("2005")
                    .teamId(4)
                    .age(45)
                    .vitality(1.0)
                    .released(0)
                    .build();

            when(managerService.getActiveManagersForYear(any())).thenReturn(Collections.singletonList(manager));
            when(teamService.willTeamReleaseManager(manager)).thenReturn(true);

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            assertEquals(1, manager.getReleased());
            assertEquals(4, manager.getFormerTeamId());
            assertNull(manager.getTeamId());
        }

        @Test
        void shouldCallLeagueConfigurationToGetNumberOfNewManagersToCreate() throws NATCException {
            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(leagueConfiguration).getNewManagersPerSeason();
        }

        @Test
        void shouldGenerateTheConfiguredNumberOfNewManagers() throws NATCException {
            when(leagueConfiguration.getNewManagersPerSeason()).thenReturn(8);
            when(managerService.getActiveManagersForYear(any())).thenReturn(Collections.emptyList());

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(managerService).generateManagers("2005", 8);
        }

        @Test
        void shouldCallLeagueConfigurationToGetTheNewManagerStartingAge() throws NATCException {
            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(leagueConfiguration).getNewManagerStartingAge();
        }

        @Test
        void shouldSetTheManagersAgeToTheConfiguredStartingManagerAge() throws NATCException {
            final Manager manager = Manager.builder().build();

            when(leagueConfiguration.getNewManagerStartingAge()).thenReturn(44);
            when(managerService.getActiveManagersForYear(any())).thenReturn(Collections.emptyList());
            when(managerService.generateManagers(any(), any())).thenReturn(Collections.singletonList(manager));

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(managerService).updateManagers(managerCaptor.capture());

            assertEquals(1, managerCaptor.getValue().size());
            assertEquals(44, managerCaptor.getValue().get(0).getAge());
        }

        @Test
        void shouldSaveTheNewGeneratedManagers() throws NATCException {
            final List<Manager> newManagers = Arrays.asList(
                    Manager.builder().build(),
                    Manager.builder().build(),
                    Manager.builder().build(),
                    Manager.builder().build(),
                    Manager.builder().build()
            );

            when(managerService.getActiveManagersForYear(any())).thenReturn(Collections.emptyList());
            when(managerService.generateManagers(any(), any())).thenReturn(newManagers);

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(managerService).updateManagers(managerCaptor.capture());

            assertEquals(newManagers.size(), managerCaptor.getValue().size());
        }

        @Test
        void shouldCallPlayerServiceToCheckForManagerialCandidatesInFormerPlayers() throws NATCException {
            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(playerService).getManagerialCandidates(any());
        }

        @Test
        void shouldCallLeagueConfigurationToGetNumberOfYearsAPlayerMustSpendRetiredBeforeBecomingAManager() throws NATCException {
            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(leagueConfiguration).getPlayerManagerYearsRetired();
        }

        @Test
        void shouldSearchForCandidatePlayersThatRetiredTheConfiguredNumberOfYearsInThePast() throws NATCException {
            when(leagueConfiguration.getPlayerManagerYearsRetired()).thenReturn(10);

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(playerService).getManagerialCandidates("1995");
        }

        @Test
        void shouldCallManagerServiceToGenerateAManagerFromACandidatePlayer() throws NATCException {
            final Player player = Player.builder().build();

            when(playerService.getManagerialCandidates(any())).thenReturn(Collections.singletonList(player));
            when(managerService.generateManagerFromPlayer(any(), any())).thenReturn(Manager.builder().build());

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(managerService).generateManagerFromPlayer(anyString(), eq(player));
        }

        @Test
        void shouldCallManagerServiceToGenerateAManagerFromACandidatePlayerWithScheduleYear() throws NATCException {
            when(playerService.getManagerialCandidates(any())).thenReturn(Collections.singletonList(Player.builder().build()));
            when(managerService.generateManagerFromPlayer(any(), any())).thenReturn(Manager.builder().build());

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(managerService).generateManagerFromPlayer(eq("2005"), any());
        }

        @Test
        void shouldGenerateManagersFromAllCandidatePlayers() throws NATCException {
            final List<Player> playerList = Arrays.asList(
                    Player.builder().build(),
                    Player.builder().build(),
                    Player.builder().build(),
                    Player.builder().build(),
                    Player.builder().build()
            );

            when(playerService.getManagerialCandidates(any())).thenReturn(playerList);
            when(managerService.generateManagerFromPlayer(any(), any())).thenReturn(Manager.builder().build());

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(managerService, times(playerList.size())).generateManagerFromPlayer(any(), any());
        }

        @Test
        void shouldSaveTheManagerGeneratedFromAPlayerCandidate() throws NATCException {
            final Player player = Player.builder().playerId(123).build();
            final Manager managerFromPlayer = Manager.builder().playerId(player.getPlayerId()).build();

            when(managerService.getActiveManagersForYear(any())).thenReturn(Collections.emptyList());
            when(managerService.generateManagers(any(), any())).thenReturn(Collections.emptyList());
            when(playerService.getManagerialCandidates(any())).thenReturn(Collections.singletonList(player));
            when(managerService.generateManagerFromPlayer(any(), eq(player))).thenReturn(managerFromPlayer);

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(managerService).updateManagers(managerCaptor.capture());

            assertEquals(1, managerCaptor.getValue().size());
            assertEquals(managerFromPlayer, managerCaptor.getValue().get(0));
        }

        @Test
        void shouldOnlyGenerateConfiguredNumberOfNewManagersIncludingPlayerCandidateManagers() throws NATCException {
            final List<Player> playerList = Arrays.asList(
                    Player.builder().build(),
                    Player.builder().build()
            );

            when(leagueConfiguration.getNewManagersPerSeason()).thenReturn(5);
            when(playerService.getManagerialCandidates(any())).thenReturn(playerList);
            when(managerService.generateManagerFromPlayer(any(), any())).thenReturn(Manager.builder().build());

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(managerService).generateManagers(any(), eq(5 - playerList.size()));
        }

        @Test
        void shouldCallTeamServiceToRetrieveTeamRecordsForTeamsThatHadManagersRetire() throws NATCException {
            final Integer teamId = 123;
            final Manager manager = Manager.builder().managerId(2).year("2005").teamId(teamId).age(55).vitality(0.2).retired(0).build();

            when(managerService.getActiveManagersForYear(any())).thenReturn(Collections.singletonList(manager));

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(teamService).getTeamByTeamIdAndYear(teamId, manager.getYear());
        }

        @Test
        void shouldCallTeamServiceToRetrieveTeamRecordsForTeamsThatReleasedManagers() throws NATCException {
            final Integer teamId = 123;
            final Manager manager = Manager.builder().managerId(1).year("2005").teamId(teamId).age(45).vitality(1.0).released(0).build();

            when(managerService.getActiveManagersForYear(any())).thenReturn(Collections.singletonList(manager));
            when(teamService.willTeamReleaseManager(manager)).thenReturn(true);

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(teamService).getTeamByTeamIdAndYear(teamId, manager.getYear());
        }

        @Test
        void shouldOnlyCallTeamServiceToRetrieveTeamRecordsForReleasedOrRetiredManagersWithFormerTeams() throws NATCException {
            final ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
            final Integer retiredManagerTeamId = 25;
            final Integer releasedManagerTeamId = 18;
            final List<Manager> managerList = Arrays.asList(
                    Manager.builder().managerId(1).year("2005").teamId(retiredManagerTeamId).age(55).vitality(0.2).retired(0).build(),
                    Manager.builder().managerId(2).year("2005").age(47).vitality(0.5).retired(0).build(),
                    Manager.builder().managerId(3).year("2005").age(62).vitality(0.5).retired(0).build(),
                    Manager.builder().managerId(4).year("2005").teamId(17).age(46).vitality(0.5).retired(0).build(),
                    Manager.builder().managerId(5).year("2005").teamId(releasedManagerTeamId).age(44).vitality(0.5).retired(0).build(),
                    Manager.builder().managerId(6).year("2005").teamId(19).age(51).vitality(0.5).retired(0).build()
            );

            final Manager managerToRelease = managerList.get(4);

            when(managerService.getActiveManagersForYear(any())).thenReturn(managerList);
            when(teamService.willTeamReleaseManager(any())).thenReturn(false);
            when(teamService.willTeamReleaseManager(managerToRelease)).thenReturn(true);

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(teamService, times(2)).getTeamByTeamIdAndYear(captor.capture(), any());

            assertTrue(captor.getAllValues().contains(retiredManagerTeamId));
            assertTrue(captor.getAllValues().contains(releasedManagerTeamId));
        }

        @Test
        void shouldCallTeamManagerDraftServiceToAssignManagersToTeamsWhenManagersRetireFromTeams() throws NATCException {
            final Manager manager = Manager.builder().managerId(2).year("2005").teamId(123).age(55).vitality(0.2).retired(0).build();

            when(managerService.getActiveManagersForYear(any())).thenReturn(Collections.singletonList(manager));

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(teamManagerDraftService).assignManagersToTeams(any(), any());
        }

        @Test
        void shouldCallTeamManagerDraftServiceToAssignManagersToTeamsWhenManagersAreReleasedFromTeams() throws NATCException {
            final Manager manager = Manager.builder().managerId(2).year("2005").teamId(123).age(45).vitality(0.2).retired(0).build();

            when(managerService.getActiveManagersForYear(any())).thenReturn(Collections.singletonList(manager));
            when(teamService.willTeamReleaseManager(manager)).thenReturn(true);

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(teamManagerDraftService).assignManagersToTeams(any(), any());
        }

        @Test
        void shouldNotCallTeamManagerDraftServiceIfNoManagersAreReleasedOrRetireFromTeams() throws NATCException {
            final Manager manager = Manager.builder().managerId(2).year("2005").teamId(123).age(45).vitality(0.2).retired(0).build();

            when(managerService.getActiveManagersForYear(any())).thenReturn(Collections.singletonList(manager));

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(teamManagerDraftService, never()).assignManagersToTeams(any(), any());
        }

        @Test
        void shouldPassTheTeamsReturnedByTheTeamServiceToTheTeamManagerDraftService() throws NATCException {
            final Integer retiredManagerTeamId = 25;
            final Integer releasedManagerTeamId = 18;
            final List<Manager> managerList = Arrays.asList(
                    Manager.builder().managerId(1).year("2005").teamId(retiredManagerTeamId).age(55).vitality(0.2).retired(0).build(),
                    Manager.builder().managerId(2).year("2005").age(47).vitality(0.5).retired(0).build(),
                    Manager.builder().managerId(3).year("2005").age(62).vitality(0.5).retired(0).build(),
                    Manager.builder().managerId(4).year("2005").teamId(17).age(46).vitality(0.5).retired(0).build(),
                    Manager.builder().managerId(5).year("2005").teamId(releasedManagerTeamId).age(44).vitality(0.5).retired(0).build(),
                    Manager.builder().managerId(6).year("2005").teamId(19).age(51).vitality(0.5).retired(0).build()
            );

            final Manager managerToRelease = managerList.get(4);

            when(managerService.getActiveManagersForYear(any())).thenReturn(managerList);
            when(teamService.willTeamReleaseManager(any())).thenReturn(false);
            when(teamService.willTeamReleaseManager(managerToRelease)).thenReturn(true);
            when(teamService.getTeamByTeamIdAndYear(retiredManagerTeamId, "2005"))
                    .thenReturn(Team.builder().teamId(retiredManagerTeamId).year("2005").build());
            when(teamService.getTeamByTeamIdAndYear(releasedManagerTeamId, "2005"))
                    .thenReturn(Team.builder().teamId(releasedManagerTeamId).year("2005").build());

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(teamManagerDraftService).assignManagersToTeams(teamCaptor.capture(), any());

            assertEquals(2, teamCaptor.getValue().size());

            final List<Integer> teamIds = teamCaptor.getValue().stream().map(Team::getTeamId).toList();

            assertTrue(teamIds.contains(retiredManagerTeamId));
            assertTrue(teamIds.contains(releasedManagerTeamId));
        }

        @Test
        void shouldPassOnlyTheManagersWithoutTeamsThatAreNotRetiredToTheTeamManagerDraftService() throws NATCException {
            final Manager managerRetiredFromTeam = Manager.builder().managerId(1).year("2005").teamId(25).age(55).vitality(0.2).retired(0).build();
            final Manager managerRetired = Manager.builder().managerId(2).year("2005").age(62).vitality(0.5).retired(0).build();
            final Manager managerOnTeam1 = Manager.builder().managerId(3).year("2005").teamId(17).age(46).vitality(0.5).retired(0).build();
            final Manager managerOnTeam2 = Manager.builder().managerId(4).year("2005").teamId(19).age(51).vitality(0.5).retired(0).build();

            final Manager managerReleasedFromTeam = Manager.builder().managerId(5).year("2005").teamId(18).age(44).vitality(0.5).retired(0).build();
            final Manager availableManager = Manager.builder().managerId(6).year("2005").age(47).vitality(0.5).retired(0).build();

            final List<Manager> newManagers = Arrays.asList(
                    Manager.builder().managerId(7).year("2005").build(),
                    Manager.builder().managerId(8).year("2005").build(),
                    Manager.builder().managerId(9).year("2005").build(),
                    Manager.builder().managerId(10).year("2005").build(),
                    Manager.builder().managerId(11).year("2005").build()
            );

            final List<Manager> managerList = Arrays.asList(
                    managerRetiredFromTeam,
                    managerRetired,
                    managerReleasedFromTeam,
                    managerOnTeam1,
                    managerOnTeam2,
                    availableManager
            );

            when(managerService.getActiveManagersForYear(any())).thenReturn(managerList);
            when(teamService.willTeamReleaseManager(any())).thenReturn(false);
            when(teamService.willTeamReleaseManager(managerReleasedFromTeam)).thenReturn(true);
            when(managerService.generateManagers(any(), any())).thenReturn(newManagers);

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            verify(teamManagerDraftService).assignManagersToTeams(any(), managerCaptor.capture());

            assertEquals(7, managerCaptor.getValue().size());

            final List<Integer> managerIds = managerCaptor.getValue().stream().map(Manager::getManagerId).toList();

            assertTrue(managerIds.contains(managerReleasedFromTeam.getManagerId()));
            assertTrue(managerIds.contains(availableManager.getManagerId()));
            assertTrue(managerIds.contains(newManagers.get(0).getManagerId()));
            assertTrue(managerIds.contains(newManagers.get(1).getManagerId()));
            assertTrue(managerIds.contains(newManagers.get(2).getManagerId()));
            assertTrue(managerIds.contains(newManagers.get(3).getManagerId()));
            assertTrue(managerIds.contains(newManagers.get(4).getManagerId()));

            assertFalse(managerIds.contains(managerRetiredFromTeam.getManagerId()));
            assertFalse(managerIds.contains(managerRetired.getManagerId()));
            assertFalse(managerIds.contains(managerOnTeam1.getManagerId()));
            assertFalse(managerIds.contains(managerOnTeam2.getManagerId()));
        }

        @Test
        void shouldCallTeamManagerDraftServiceAfterCallingTeamServiceToReleaseAManagerAndBeforeUpdatingManagers() throws NATCException {
            final Manager manager = Manager.builder().managerId(2).year("2005").teamId(123).age(45).vitality(0.2).retired(0).build();

            when(managerService.getActiveManagersForYear(any())).thenReturn(Collections.singletonList(manager));
            when(teamService.willTeamReleaseManager(manager)).thenReturn(true);

            final InOrder inOrder = inOrder(teamService, teamManagerDraftService, managerService);

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year("2005").build());

            inOrder.verify(teamService).willTeamReleaseManager(manager);
            inOrder.verify(teamManagerDraftService).assignManagersToTeams(any(), any());
            inOrder.verify(managerService).updateManagers(any());
        }

        @Test
        void shouldNotUpdateManagersIfScheduleYearIsFirstSeasonYear() throws NATCException {
            final String firstSeason = "1989";

            when(leagueConfiguration.getFirstSeason()).thenReturn(firstSeason);

            processor.process(Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year(firstSeason).build());

            verify(managerService, never()).updateManagers(any());
        }

        @Test
        public void shouldUpdateScheduleToCompletedIfFirstSeasonYear() throws NATCException {
            final String firstSeason = "1989";
            final Schedule schedule = Schedule.builder().type(ScheduleType.MANAGER_CHANGES.getValue()).year(firstSeason).sequence(1).status(ScheduleStatus.IN_PROGRESS.getValue()).build();
            final ArgumentCaptor<Schedule> captor = ArgumentCaptor.forClass(Schedule.class);

            when(leagueConfiguration.getFirstSeason()).thenReturn(firstSeason);

            processor.process(schedule);

            verify(scheduleService).updateScheduleEntry(captor.capture());

            assertSame(schedule, captor.getValue());
            assertEquals(ScheduleStatus.COMPLETED.getValue(), captor.getValue().getStatus());
        }
    }
}