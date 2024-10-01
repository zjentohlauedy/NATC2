package org.natc.app.processor;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.comparator.PlayerComparator;
import org.natc.app.comparator.PlayerComparatorFactory;
import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.*;
import org.natc.app.exception.NATCException;
import org.natc.app.exception.ScheduleProcessingException;
import org.natc.app.proxy.PlayerRetirementProxy;
import org.natc.app.service.ManagerService;
import org.natc.app.service.PlayerService;
import org.natc.app.service.ScheduleService;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.natc.app.entity.domain.PlayerRatingAdjustment.APPLY_AGE;

@ExtendWith(MockitoExtension.class)
class PlayerChangesScheduleProcessorTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private ManagerService managerService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PlayerRetirementProxy playerRetirementProxy;

    @Mock
    private PlayerComparatorFactory playerComparatorFactory;

    @Mock
    private LeagueConfiguration leagueConfiguration;

    @InjectMocks
    private PlayerChangesScheduleProcessor processor;

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
            processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2005").build());

            verify(scheduleService).updateScheduleEntry(any());
        }

        @Test
        void shouldUpdateTheScheduleEntryStatusToCompleted() throws NATCException {
            final Schedule schedule = Schedule.builder()
                    .type(ScheduleType.PLAYER_CHANGES.getValue())
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
        void shouldCallPlayerServiceToGetActivePlayersForSameYearAsScheduledEvent() throws NATCException {
            processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2012").build());

            verify(playerService).getActivePlayersForYear("2012");
        }

        @Test
        void shouldCallManagerServiceToGetActiveManagersForSameYearAsScheduledEvent() throws NATCException {
            processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2012").build());

            verify(managerService).getActiveManagersForYear("2012");
        }

        @Nested
        class ReadyToRetire {
            @Test
            void shouldCallPlayerRetirementProxyToSeeIfAPlayerIsReadyToRetire() throws NATCException {
                final Player player = Player.builder().playerId(1).teamId(1).year("2020").build();
                final List<Player> playerList = Collections.singletonList(player);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2012").build());

                verify(playerRetirementProxy).readyToRetire(player);
            }

            @Test
            void shouldCallPlayerRetirementProxyReadyToRetireForEachPlayer() throws NATCException {
                final List<Player> playerList = Arrays.asList(
                        Player.builder().playerId(1).teamId(1).year("2020").build(),
                        Player.builder().playerId(2).teamId(1).year("2020").build(),
                        Player.builder().playerId(3).teamId(1).year("2020").build()
                );

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2012").build());

                verify(playerRetirementProxy, times(playerList.size())).readyToRetire(any());
            }

            @Test
            void shouldOnlyCallPlayerRetirementProxyReadyToRetireForPlayersOnATeam() throws NATCException {
                final List<Player> playerList = Arrays.asList(
                        Player.builder().playerId(1).teamId(1).year("2020").build(),
                        Player.builder().playerId(2).teamId(1).year("2020").build(),
                        Player.builder().playerId(3).year("2020").build()
                );

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2012").build());

                verify(playerRetirementProxy, times(2)).readyToRetire(any());
            }

            @Test
            void shouldMarkPlayerRetiredIfAPlayerIsReadyToRetire() throws NATCException {
                final Player player = Player.builder().playerId(1).teamId(1).year("2020").build();
                final List<Player> playerList = Collections.singletonList(player);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(playerRetirementProxy.readyToRetire(any())).thenReturn(true);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2012").build());

                assertEquals(1, player.getRetired());
            }

            @Test
            void shouldMoveTheTeamIdToFormerTeamIdIfAPlayerIsReadyToRetire() throws NATCException {
                final int teamId = 7;
                final Player player = Player.builder().playerId(1).teamId(teamId).year("2020").build();
                final List<Player> playerList = Collections.singletonList(player);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(playerRetirementProxy.readyToRetire(any())).thenReturn(true);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2012").build());

                assertEquals(teamId, player.getFormerTeamId());
                assertNull(player.getTeamId());
            }
        }

        @Nested
        class FreeAgency {
            @Test
            void shouldCallPlayerComparatorFactoryToRetrievePlayerComparator() throws NATCException {
                final List<Manager> managerList = Collections.singletonList(
                        Manager.builder().managerId(1).teamId(1).style(ManagerStyle.BALANCED.getValue()).build()
                );

                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                verify(playerComparatorFactory, atLeastOnce()).getPlayerComparatorForManager(any(), any());
            }

            @Test
            void shouldRetrievePlayerComparatorForEachManagerBasedOnManagerStyle() throws NATCException {
                final List<Manager> managerList = Arrays.asList(
                        Manager.builder().managerId(1).teamId(1).style(ManagerStyle.OFFENSIVE.getValue()).build(),
                        Manager.builder().managerId(2).teamId(2).style(ManagerStyle.DEFENSIVE.getValue()).build(),
                        Manager.builder().managerId(3).teamId(3).style(ManagerStyle.INTANGIBLE.getValue()).build(),
                        Manager.builder().managerId(4).teamId(4).style(ManagerStyle.PENALTIES.getValue()).build(),
                        Manager.builder().managerId(5).teamId(5).style(ManagerStyle.BALANCED.getValue()).build()
                );

                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                verify(playerComparatorFactory, atLeastOnce()).getPlayerComparatorForManager(eq(ManagerStyle.OFFENSIVE), any());
                verify(playerComparatorFactory, atLeastOnce()).getPlayerComparatorForManager(eq(ManagerStyle.DEFENSIVE), any());
                verify(playerComparatorFactory, atLeastOnce()).getPlayerComparatorForManager(eq(ManagerStyle.INTANGIBLE), any());
                verify(playerComparatorFactory, atLeastOnce()).getPlayerComparatorForManager(eq(ManagerStyle.PENALTIES), any());
                verify(playerComparatorFactory, atLeastOnce()).getPlayerComparatorForManager(eq(ManagerStyle.BALANCED), any());
            }

            @Test
            void shouldOnlyRetrievePlayerComparatorForManagersOnATeam() throws NATCException {
                final List<Manager> managerList = Arrays.asList(
                        Manager.builder().managerId(1).teamId(1).style(ManagerStyle.OFFENSIVE.getValue()).build(),
                        Manager.builder().managerId(2).style(ManagerStyle.BALANCED.getValue()).build()
                );

                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                verify(playerComparatorFactory, atLeastOnce()).getPlayerComparatorForManager(eq(ManagerStyle.OFFENSIVE), any());
                verify(playerComparatorFactory, never()).getPlayerComparatorForManager(eq(ManagerStyle.BALANCED), any());
            }

            @Test
            void shouldConfigurePlayerComparatorsWithUseAgeAdjustment() throws NATCException {
                final List<Manager> managerList = Collections.singletonList(
                        Manager.builder().managerId(1).teamId(1).style(ManagerStyle.OFFENSIVE.getValue()).build()
                );

                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                verify(playerComparatorFactory, atLeastOnce()).getPlayerComparatorForManager(ManagerStyle.OFFENSIVE, APPLY_AGE);
            }

            @Test
            void shouldUseThePlayerComparatorReturnedFromPlayerComparatorFactory() throws NATCException {
                final Manager manager = Manager.builder().managerId(1).teamId(1).style(ManagerStyle.BALANCED.getValue()).build();
                final Player teamPlayer = generatePlayer(1, manager.getTeamId(), "2020", 0.3);
                final Player freeAgent = generatePlayer(2, null, "2020", 0.7);
                final List<Player> playerList = Arrays.asList(teamPlayer, freeAgent);
                final List<Manager> managerList = Collections.singletonList(manager);
                final PlayerComparator playerComparator = mock(PlayerComparator.class);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenReturn(playerComparator);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                verify(playerComparator, atLeastOnce()).compare(any(Player.class), any(Player.class));
            }

            @Test
            void shouldReplacePlayerOnTeamWithBetterFreeAgentPlayer() throws NATCException {
                final Manager manager = Manager.builder().managerId(1).teamId(1).style(ManagerStyle.BALANCED.getValue()).build();
                final Player teamPlayer = generatePlayer(1, manager.getTeamId(), "2020", 0.3);
                final Player freeAgent = generatePlayer(2, null, "2020", 0.7);
                final List<Player> playerList = Arrays.asList(teamPlayer, freeAgent);
                final List<Manager> managerList = Collections.singletonList(manager);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                assertNull(teamPlayer.getTeamId());
                assertEquals(manager.getTeamId(), freeAgent.getTeamId());
            }

            @Test
            void shouldPickBestFreeAgentWhenTeamPlayerCountUnderConfiguredAmount() throws NATCException {
                final Manager manager = Manager.builder().managerId(1).teamId(1).style(ManagerStyle.BALANCED.getValue()).build();
                final Player teamPlayer = generatePlayer(1, manager.getTeamId(), "2020", 0.7);
                final Player freeAgent = generatePlayer(2, null, "2020", 0.3);
                final List<Player> playerList = Arrays.asList(teamPlayer, freeAgent);
                final List<Manager> managerList = Collections.singletonList(manager);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();
                when(leagueConfiguration.getPlayersPerTeam()).thenReturn(2);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                assertEquals(manager.getTeamId(), teamPlayer.getTeamId());
                assertEquals(manager.getTeamId(), freeAgent.getTeamId());
            }

            @Test
            void shouldMarkFreeAgentAsSignedAndNoLongerAFreeAgentWhenReplacingTeamPlayer() throws NATCException {
                final Manager manager = Manager.builder().managerId(1).teamId(1).style(ManagerStyle.BALANCED.getValue()).build();
                final Player teamPlayer = generatePlayer(1, manager.getTeamId(), "2020", 0.3);
                final Player freeAgent = generatePlayer(2, null, "2020", 0.7);
                final List<Player> playerList = Arrays.asList(teamPlayer, freeAgent);
                final List<Manager> managerList = Collections.singletonList(manager);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                assertEquals(1, freeAgent.getSigned());
                assertEquals(0, freeAgent.getFreeAgent());
            }

            @Test
            void shouldMarkFreeAgentAsSignedAndNoLongerAFreeAgentWhenAddingToTeam() throws NATCException {
                final Manager manager = Manager.builder().managerId(1).teamId(1).style(ManagerStyle.BALANCED.getValue()).build();
                final Player teamPlayer = generatePlayer(1, manager.getTeamId(), "2020", 0.7);
                final Player freeAgent = generatePlayer(2, null, "2020", 0.3);
                final List<Player> playerList = Arrays.asList(teamPlayer, freeAgent);
                final List<Manager> managerList = Collections.singletonList(manager);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();
                when(leagueConfiguration.getPlayersPerTeam()).thenReturn(2);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                assertEquals(1, freeAgent.getSigned());
                assertEquals(0, freeAgent.getFreeAgent());
            }

            @Test
            void shouldMarkTeamPlayerAsReleasedUnsignedAndFreeAgentWhenReplacingTeamPlayer() throws NATCException {
                final Manager manager = Manager.builder().managerId(1).teamId(1).style(ManagerStyle.BALANCED.getValue()).build();
                final Player teamPlayer = generatePlayer(1, manager.getTeamId(), "2020", 0.3);
                final Player freeAgent = generatePlayer(2, null, "2020", 0.7);
                final List<Player> playerList = Arrays.asList(teamPlayer, freeAgent);
                final List<Manager> managerList = Collections.singletonList(manager);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                assertEquals(1, teamPlayer.getReleased());
                assertEquals(1, teamPlayer.getFreeAgent());
                assertEquals(0, teamPlayer.getSigned());
            }

            @Test
            void shouldSetFormerTeamIdOnTeamPlayerToManagerTeamIdWhenReplacingTeamPlayer() throws NATCException {
                final Manager manager = Manager.builder().managerId(1).teamId(1).style(ManagerStyle.BALANCED.getValue()).build();
                final Player teamPlayer = generatePlayer(1, manager.getTeamId(), "2020", 0.3);
                final Player freeAgent = generatePlayer(2, null, "2020", 0.7);
                final List<Player> playerList = Arrays.asList(teamPlayer, freeAgent);
                final List<Manager> managerList = Collections.singletonList(manager);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                assertNull(teamPlayer.getTeamId());
                assertEquals(manager.getTeamId(), teamPlayer.getFormerTeamId());
            }

            @Test
            void shouldNotChangeExistingFormerTeamIdOnTeamPlayerWhenReplacingTeamPlayer() throws NATCException {
                final Manager manager = Manager.builder().managerId(1).teamId(1).style(ManagerStyle.BALANCED.getValue()).build();
                final Player teamPlayer = generatePlayer(1, manager.getTeamId(), "2020", 0.3);
                final Player freeAgent = generatePlayer(2, null, "2020", 0.7);
                final List<Player> playerList = Arrays.asList(teamPlayer, freeAgent);
                final List<Manager> managerList = Collections.singletonList(manager);
                teamPlayer.setFormerTeamId(25);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                assertNull(teamPlayer.getTeamId());
                assertEquals(25, teamPlayer.getFormerTeamId());
            }

            @Test
            void shouldReplaceWorstTeamPlayerWithBestFreeAgentWhenThereAreManyOfEach() throws NATCException {
                final Manager manager = Manager.builder().managerId(1).teamId(1).style(ManagerStyle.BALANCED.getValue()).build();
                final Player teamPlayer1 = generatePlayer(1, manager.getTeamId(), "2020", 0.5);
                final Player teamPlayer2 = generatePlayer(2, manager.getTeamId(), "2020", 0.3);
                final Player teamPlayer3 = generatePlayer(3, manager.getTeamId(), "2020", 0.7);
                final Player freeAgent1 = generatePlayer(4, null, "2020", 0.3);
                final Player freeAgent2 = generatePlayer(5, null, "2020", 0.4);
                final Player freeAgent3 = generatePlayer(6, null, "2020", 0.2);
                final List<Player> playerList = Arrays.asList(
                        teamPlayer1,
                        freeAgent1,
                        teamPlayer2,
                        freeAgent2,
                        teamPlayer3,
                        freeAgent3
                );
                final List<Manager> managerList = Collections.singletonList(manager);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                assertNull(teamPlayer2.getTeamId());
                assertEquals(manager.getTeamId(), freeAgent2.getTeamId());
            }

            @Test
            void shouldOnlyConsiderPlayersOnSameTeamAsManagerAndFreeAgents() throws NATCException {
                final Manager manager = Manager.builder().managerId(1).teamId(1).style(ManagerStyle.BALANCED.getValue()).build();
                final Player teamPlayer1 = generatePlayer(1, manager.getTeamId(), "2020", 0.5);
                final Player teamPlayer2 = generatePlayer(2, 10, "2020", 0.3);
                final Player teamPlayer3 = generatePlayer(3, 11, "2020", 0.2);
                final Player teamPlayer4 = generatePlayer(4, 12, "2020", 0.1);
                final Player teamPlayer5 = generatePlayer(5, 13, "2020", 0.4);
                final Player freeAgent = generatePlayer(6, null, "2020", 0.6);
                final List<Player> playerList = Arrays.asList(
                        teamPlayer1,
                        teamPlayer2,
                        teamPlayer3,
                        teamPlayer4,
                        teamPlayer5,
                        freeAgent
                );
                final List<Manager> managerList = Collections.singletonList(manager);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                assertNull(teamPlayer1.getTeamId());
                assertEquals(manager.getTeamId(), freeAgent.getTeamId());
                assertEquals(10, teamPlayer2.getTeamId());
                assertEquals(11, teamPlayer3.getTeamId());
                assertEquals(12, teamPlayer4.getTeamId());
                assertEquals(13, teamPlayer5.getTeamId());
            }

            @Test
            void shouldNotMakeAnyChangesIfThereAreNoFreeAgents() throws NATCException {
                final Manager manager = Manager.builder().managerId(1).teamId(1).style(ManagerStyle.BALANCED.getValue()).build();
                final Player teamPlayer = generatePlayer(1, manager.getTeamId(), "2020", 0.7);
                final Player otherTeamPlayer = generatePlayer(2, 2, "2020", 0.3);
                final Player anotherTeamPlayer = generatePlayer(3, 3, "2020", 0.3);
                final List<Player> playerList = Arrays.asList(teamPlayer, otherTeamPlayer, anotherTeamPlayer);
                final List<Manager> managerList = Collections.singletonList(manager);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                assertEquals(manager.getTeamId(), teamPlayer.getTeamId());
                assertEquals(2, otherTeamPlayer.getTeamId());
                assertEquals(3, anotherTeamPlayer.getTeamId());

                assertNull(teamPlayer.getReleased());
                assertNull(otherTeamPlayer.getReleased());
                assertNull(anotherTeamPlayer.getReleased());

                assertNull(teamPlayer.getFreeAgent());
                assertNull(otherTeamPlayer.getFreeAgent());
                assertNull(anotherTeamPlayer.getFreeAgent());

                assertNull(teamPlayer.getSigned());
                assertNull(otherTeamPlayer.getSigned());
                assertNull(anotherTeamPlayer.getSigned());

                assertNull(teamPlayer.getFormerTeamId());
                assertNull(otherTeamPlayer.getFormerTeamId());
                assertNull(anotherTeamPlayer.getFormerTeamId());
            }

            @Test
            void shouldConsiderEveryManagerWhenCheckingForPlayerChanges() throws NATCException {
                final Manager manager1 = Manager.builder().managerId(1).teamId(1).style(ManagerStyle.BALANCED.getValue()).build();
                final Manager manager2 = Manager.builder().managerId(2).teamId(2).style(ManagerStyle.BALANCED.getValue()).build();
                final Manager manager3 = Manager.builder().managerId(3).teamId(3).style(ManagerStyle.BALANCED.getValue()).build();
                final Player teamPlayer1 = generatePlayer(1, manager1.getTeamId(), "2020", 0.3);
                final Player teamPlayer2 = generatePlayer(2, manager2.getTeamId(), "2020", 0.3);
                final Player teamPlayer3 = generatePlayer(3, manager3.getTeamId(), "2020", 0.3);
                final Player freeAgent1 = generatePlayer(4, null, "2020", 0.7);
                final Player freeAgent2 = generatePlayer(5, null, "2020", 0.7);
                final Player freeAgent3 = generatePlayer(6, null, "2020", 0.7);
                final List<Player> playerList = Arrays.asList(
                        teamPlayer1,
                        freeAgent1,
                        teamPlayer2,
                        freeAgent2,
                        teamPlayer3,
                        freeAgent3
                );
                final List<Manager> managerList = Arrays.asList(manager1, manager2, manager3);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                assertNull(teamPlayer1.getTeamId());
                assertNull(teamPlayer2.getTeamId());
                assertNull(teamPlayer3.getTeamId());

                assertEquals(1, teamPlayer1.getReleased());
                assertEquals(1, teamPlayer2.getReleased());
                assertEquals(1, teamPlayer3.getReleased());

                assertNotNull(freeAgent1.getTeamId());
                assertNotNull(freeAgent2.getTeamId());
                assertNotNull(freeAgent3.getTeamId());

                assertEquals(1, freeAgent1.getSigned());
                assertEquals(1, freeAgent2.getSigned());
                assertEquals(1, freeAgent3.getSigned());

                assertEquals(managerList.size(), playerList.stream()
                        .filter(player -> Objects.equals(player.getSigned(), 1))
                        .map(Player::getTeamId)
                        .distinct()
                        .count());
            }

            @Test
            void shouldContinueToGoThroughManagerListUntilThereAreNoMoreChangesToMake() throws NATCException {
                final Manager manager1 = Manager.builder().managerId(1).teamId(1).style(ManagerStyle.OFFENSIVE.getValue()).build();
                final Manager manager2 = Manager.builder().managerId(2).teamId(2).style(ManagerStyle.DEFENSIVE.getValue()).build();
                final Manager manager3 = Manager.builder().managerId(3).teamId(3).style(ManagerStyle.BALANCED.getValue()).build();
                final List<Manager> managerList = Arrays.asList(manager1, manager2, manager3);
                final List<Player> playerList = Arrays.asList(
                        generatePlayer(1, null, "2020", 0.5),
                        generatePlayer(2, null, "2020", 0.5),
                        generatePlayer(3, null, "2020", 0.5),
                        generatePlayer(4, null, "2020", 0.5),
                        generatePlayer(5, null, "2020", 0.5),
                        generatePlayer(6, null, "2020", 0.5)
                );

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();
                when(leagueConfiguration.getPlayersPerTeam()).thenReturn(5);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                assertEquals(playerList.size(), playerList.stream()
                        .filter(player -> Objects.equals(player.getSigned(), 1))
                        .count());
                assertEquals(managerList.size(), playerList.stream()
                        .filter(player -> Objects.equals(player.getSigned(), 1))
                        .map(Player::getTeamId)
                        .distinct()
                        .count());
                assertEquals(2, playerList.stream()
                        .filter(player -> Objects.equals(manager1.getTeamId(), player.getTeamId()))
                        .count());
                assertEquals(2, playerList.stream()
                        .filter(player -> Objects.equals(manager2.getTeamId(), player.getTeamId()))
                        .count());
                assertEquals(2, playerList.stream()
                        .filter(player -> Objects.equals(manager3.getTeamId(), player.getTeamId()))
                        .count());
            }

            @Test
            void shouldMakeChangesBasedOnTeamNeedRatherThanEvenDistribution() throws NATCException {
                final Manager manager1 = Manager.builder().managerId(1).teamId(1).style(ManagerStyle.OFFENSIVE.getValue()).build();
                final Manager manager2 = Manager.builder().managerId(2).teamId(2).style(ManagerStyle.DEFENSIVE.getValue()).build();
                final List<Manager> managerList = Arrays.asList(manager1, manager2);
                final List<Player> playerList = Arrays.asList(
                        generatePlayer(1, manager1.getTeamId(), "2020", 0.9),
                        generatePlayer(2, manager1.getTeamId(), "2020", 0.9),
                        generatePlayer(3, manager1.getTeamId(), "2020", 0.9),
                        generatePlayer(4, manager1.getTeamId(), "2020", 0.2),
                        generatePlayer(5, manager2.getTeamId(), "2020", 0.5),
                        generatePlayer(6, manager2.getTeamId(), "2020", 0.5),
                        generatePlayer(7, manager2.getTeamId(), "2020", 0.5),
                        generatePlayer(8, manager2.getTeamId(), "2020", 0.5),
                        generatePlayer(9, null, "2020", 0.7),
                        generatePlayer(10, null, "2020", 0.7),
                        generatePlayer(11, null, "2020", 0.7),
                        generatePlayer(12, null, "2020", 0.7)
                );

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();
                when(leagueConfiguration.getPlayersPerTeam()).thenReturn(4);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                assertEquals(4, playerList.stream()
                        .filter(player -> Objects.equals(player.getSigned(), 1))
                        .count());
                assertEquals(1, playerList.stream()
                        .filter(player -> Objects.equals(manager1.getTeamId(), player.getTeamId()) && Objects.equals(player.getSigned(), 1))
                        .count());
                assertEquals(3, playerList.stream()
                        .filter(player -> Objects.equals(manager2.getTeamId(), player.getTeamId()) && Objects.equals(player.getSigned(), 1))
                        .count());

                assertEquals(4, playerList.stream()
                        .filter(player -> Objects.equals(player.getReleased(), 1))
                        .count());
                assertEquals(1, playerList.stream()
                        .filter(player -> Objects.equals(manager1.getTeamId(), player.getFormerTeamId()))
                        .count());
                assertEquals(3, playerList.stream()
                        .filter(player -> Objects.equals(manager2.getTeamId(), player.getFormerTeamId()))
                        .count());
            }

            @Test
            void shouldConsiderReleasedPlayersAsFreeAgents() throws NATCException {
                final Manager manager1 = Manager.builder().managerId(1).teamId(1).style(ManagerStyle.OFFENSIVE.getValue()).build();
                final Manager manager2 = Manager.builder().managerId(2).teamId(2).style(ManagerStyle.DEFENSIVE.getValue()).build();
                final List<Manager> managerList = Arrays.asList(manager1, manager2);
                final List<Player> playerList = Arrays.asList(
                        generatePlayer(1, manager1.getTeamId(), "2020", 0.9),
                        generatePlayer(2, manager1.getTeamId(), "2020", 0.9),
                        generatePlayer(3, manager1.getTeamId(), "2020", 0.9),
                        generatePlayer(4, manager1.getTeamId(), "2020", 0.5),
                        generatePlayer(5, manager2.getTeamId(), "2020", 0.3),
                        generatePlayer(6, manager2.getTeamId(), "2020", 0.3),
                        generatePlayer(7, manager2.getTeamId(), "2020", 0.3),
                        generatePlayer(8, manager2.getTeamId(), "2020", 0.3),
                        generatePlayer(9, null, "2020", 0.7),
                        generatePlayer(10, null, "2020", 0.7),
                        generatePlayer(11, null, "2020", 0.7),
                        generatePlayer(12, null, "2020", 0.7)
                );

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();
                when(leagueConfiguration.getPlayersPerTeam()).thenReturn(4);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                final Player resignedPlayer = playerList.stream().filter(p -> p.getPlayerId() == 4).findFirst().orElse(null);

                assertNotNull(resignedPlayer);
                assertEquals(manager1.getTeamId(), resignedPlayer.getFormerTeamId());
                assertEquals(manager2.getTeamId(), resignedPlayer.getTeamId());
            }

            @Test
            void shouldProcessTeamsInRandomOrderThatChangesEachTimeThrough() throws NATCException {
                final ArgumentCaptor<ManagerStyle> captor = ArgumentCaptor.forClass(ManagerStyle.class);
                final List<Manager> managerList = Arrays.asList(
                        Manager.builder().managerId(1).teamId(1).style(ManagerStyle.OFFENSIVE.getValue()).build(),
                        Manager.builder().managerId(2).teamId(2).style(ManagerStyle.DEFENSIVE.getValue()).build(),
                        Manager.builder().managerId(3).teamId(3).style(ManagerStyle.INTANGIBLE.getValue()).build(),
                        Manager.builder().managerId(4).teamId(4).style(ManagerStyle.PENALTIES.getValue()).build(),
                        Manager.builder().managerId(5).teamId(5).style(ManagerStyle.BALANCED.getValue()).build()
                );
                final List<Player> freeAgents = new ArrayList<>();

                for (int i = 1; i <= 25; i++) {
                    freeAgents.add(generatePlayer(i, null, "2020", 0.5));
                }

                List<ManagerStyle> previousOrder = managerList.stream()
                        .map(manager -> ManagerStyle.getByValue(manager.getStyle()))
                        .toList();

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(freeAgents);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();
                when(leagueConfiguration.getPlayersPerTeam()).thenReturn(10);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                verify(playerComparatorFactory, atLeastOnce()).getPlayerComparatorForManager(captor.capture(), any());

                final List<ManagerStyle> managerStyleList = captor.getAllValues();

                assertEquals(30, managerStyleList.size());

                for (final List<ManagerStyle> currentOrder : partitionList(managerStyleList, managerList.size())) {
                    assertEquals(previousOrder.stream().distinct().count(), currentOrder.stream().distinct().count());
                    assertNotEquals(previousOrder, currentOrder);

                    previousOrder = currentOrder;
                }
            }

            @Test
            void shouldNotConsiderPlayersThatHaveRetired() throws NATCException {
                final Manager manager = Manager.builder().managerId(1).teamId(1).style(ManagerStyle.BALANCED.getValue()).build();
                final Player teamPlayer = generatePlayer(1, manager.getTeamId(), "2020", 0.3);
                final Player oldTeamPlayer = generatePlayer(2, manager.getTeamId(), "2020", 0.7);
                final Player retiredPlayer = generatePlayer(3, null, "2020", 0.7);
                final List<Player> playerList = Arrays.asList(teamPlayer, oldTeamPlayer, retiredPlayer);
                final List<Manager> managerList = Collections.singletonList(manager);

                retiredPlayer.setRetired(1);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerRetirementProxy.readyToRetire(any())).then(invocation -> invocation.getArgument(0) == oldTeamPlayer);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                assertNull(retiredPlayer.getTeamId());
                assertNull(oldTeamPlayer.getTeamId());
                assertEquals(manager.getTeamId(), teamPlayer.getTeamId());
            }
        }

        @Nested
        class ShouldRetire {
            @Test
            void shouldCallPlayerRetirementProxyToSeeIfAPlayerShouldRetire() throws NATCException {
                final Player player = Player.builder().playerId(1).year("2020").build();
                final List<Player> playerList = Collections.singletonList(player);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2012").build());

                verify(playerRetirementProxy).shouldRetire(player);
            }

            @Test
            void shouldCallPlayerRetirementProxyShouldRetireForEachPlayer() throws NATCException {
                final List<Player> playerList = Arrays.asList(
                        Player.builder().playerId(1).year("2020").build(),
                        Player.builder().playerId(2).year("2020").build(),
                        Player.builder().playerId(3).year("2020").build()
                );

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2012").build());

                verify(playerRetirementProxy, times(playerList.size())).shouldRetire(any());
            }

            @Test
            void shouldOnlyCallPlayerRetirementProxyShouldRetireForPlayersNotOnATeam() throws NATCException {
                final List<Player> playerList = Arrays.asList(
                        Player.builder().playerId(1).teamId(1).year("2020").build(),
                        Player.builder().playerId(2).teamId(1).year("2020").build(),
                        Player.builder().playerId(3).year("2020").build()
                );

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2012").build());

                verify(playerRetirementProxy, times(1)).shouldRetire(any());
            }

            @Test
            void shouldOnlyCallPlayerRetirementProxyShouldRetireForPlayersNotAlreadyRetired() throws NATCException {
                final List<Player> playerList = Arrays.asList(
                        Player.builder().playerId(1).retired(1).year("2020").build(),
                        Player.builder().playerId(2).retired(1).year("2020").build(),
                        Player.builder().playerId(3).year("2020").build()
                );

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2012").build());

                verify(playerRetirementProxy, times(1)).shouldRetire(any());
            }

            @Test
            void shouldMarkPlayerRetiredIfAPlayerShouldRetire() throws NATCException {
                final Player player = Player.builder().playerId(1).year("2020").build();
                final List<Player> playerList = Collections.singletonList(player);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(playerRetirementProxy.shouldRetire(any())).thenReturn(true);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2012").build());

                assertEquals(1, player.getRetired());
            }

            @Test
            void shouldMarkPlayerNotReleasedIfAPlayerShouldRetire() throws NATCException {
                final Player player = Player.builder().playerId(1).released(1).year("2020").build();
                final List<Player> playerList = Collections.singletonList(player);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(playerRetirementProxy.shouldRetire(any())).thenReturn(true);

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2012").build());

                assertEquals(0, player.getReleased());
            }

            @Test
            void shouldCheckReleasePlayersForRetirementTwice() throws NATCException {
                final Manager manager = Manager.builder().managerId(1).teamId(1).style(ManagerStyle.BALANCED.getValue()).build();
                final Player teamPlayer = generatePlayer(1, manager.getTeamId(), "2020", 0.3);
                final Player freeAgent = generatePlayer(2, null, "2020", 0.7);
                final List<Player> playerList = Arrays.asList(teamPlayer, freeAgent);
                final List<Manager> managerList = Collections.singletonList(manager);

                when(playerService.getActivePlayersForYear(anyString())).thenReturn(playerList);
                when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
                when(playerComparatorFactory.getPlayerComparatorForManager(any(), any())).thenCallRealMethod();

                processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

                verify(playerRetirementProxy, times(1)).readyToRetire(teamPlayer);
                verify(playerRetirementProxy, times(1)).shouldRetire(teamPlayer);
            }
        }

        @Test
        void shouldCallPlayerServiceToUpdatePlayersRetrievedForScheduledYear() throws NATCException {
            final List<Player> playerList = Arrays.asList(
                    Player.builder().playerId(1).year("2020").build(),
                    Player.builder().playerId(2).year("2020").build(),
                    Player.builder().playerId(3).year("2020").build()
            );

            when(playerService.getActivePlayersForYear("2020")).thenReturn(playerList);

            processor.process(Schedule.builder().type(ScheduleType.PLAYER_CHANGES.getValue()).year("2020").build());

            verify(playerService).updatePlayers(playerList);
        }
    }

    private List<List<ManagerStyle>> partitionList(final List<ManagerStyle> managerStyleList, final int chunkSize) {
        final AtomicInteger counter = new AtomicInteger(0);

        return new ArrayList<>(managerStyleList.stream()
                .collect(Collectors.groupingBy(style -> counter.getAndDecrement() / chunkSize))
                .values());
    }

    private Player generatePlayer(final int playerId, final Integer teamId, final String year, final double rating) {
        return Player.builder()
                .playerId(playerId)
                .teamId(teamId)
                .year(year)
                .age(25)
                .vitality(0.5)
                .scoring(rating)
                .passing(rating)
                .blocking(rating)
                .tackling(rating)
                .stealing(rating)
                .presence(rating)
                .discipline(rating)
                .endurance(rating)
                .penaltyShot(rating)
                .penaltyOffense(rating)
                .penaltyDefense(rating)
                .build();
    }
}