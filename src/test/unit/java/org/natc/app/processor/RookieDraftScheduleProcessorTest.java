package org.natc.app.processor;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.comparator.PlayerComparator;
import org.natc.app.comparator.PlayerComparatorFactory;
import org.natc.app.comparator.TeamComparator;
import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.*;
import org.natc.app.exception.NATCException;
import org.natc.app.exception.ScheduleProcessingException;
import org.natc.app.service.ManagerService;
import org.natc.app.service.PlayerService;
import org.natc.app.service.ScheduleService;
import org.natc.app.service.TeamService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RookieDraftScheduleProcessorTest {

    @Captor
    private ArgumentCaptor<List<Player>> captor;

    @Mock
    private LeagueConfiguration leagueConfiguration;

    @Mock
    private ManagerService managerService;

    @Mock
    private PlayerComparatorFactory playerComparatorFactory;

    @Mock
    private PlayerService playerService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private TeamComparator teamComparator;

    @Mock
    private TeamService teamService;

    @InjectMocks
    private RookieDraftScheduleProcessor processor;

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
            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()).year("2019").build());

            verify(scheduleService).updateScheduleEntry(any());
        }

        @Test
        void shouldUpdateTheScheduleEntryStatusToCompleted() throws NATCException {
            final Schedule schedule = Schedule.builder()
                    .type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue())
                    .year("2022")
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
        void shouldCallTeamServiceToGetTeamsForThePreviousYear() throws NATCException {
            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()).year("2019").build());

            verify(teamService).getRegularTeamsByYear("2018");
        }

        @Test
        void shouldCallTeamServiceToGetTeamsForTheCurrentYearWhenPreviousYearCallReturnsNoTeams() throws NATCException {
            when(teamService.getRegularTeamsByYear("2018")).thenReturn(Collections.emptyList());

            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()).year("2019").build());

            verify(teamService).getRegularTeamsByYear("2019");
        }

        @Test
        void shouldUseTeamComparatorToOrderTeamsFromPreviousYear() throws NATCException {
            final List<Player> playerList = List.of(
                    generatePlayer(1, 0.7),
                    generatePlayer(2, 0.5),
                    generatePlayer(3, 0.3)
            );

            when(teamService.getRegularTeamsByYear("2018")).thenReturn(List.of(
                    Team.builder().teamId(1).wins(75).build(),
                    Team.builder().teamId(2).wins(50).build(),
                    Team.builder().teamId(3).wins(25).build()
            ));
            when(managerService.getActiveManagersForYear(anyString())).thenReturn(List.of(
                    Manager.builder().managerId(1).teamId(1).style(ManagerStyle.OFFENSIVE.getValue()).build(),
                    Manager.builder().managerId(2).teamId(2).style(ManagerStyle.DEFENSIVE.getValue()).build(),
                    Manager.builder().managerId(3).teamId(3).style(ManagerStyle.PENALTIES.getValue()).build()
            ));
            when(playerService.generatePlayers(anyString(), anyInt())).thenReturn(playerList);
            when(playerComparatorFactory.getPlayerComparatorForManager(any())).thenCallRealMethod();
            when(teamComparator.compare(any(), any())).thenAnswer(invocation -> {
               final Team team1 = invocation.getArgument(0);
               final Team team2 = invocation.getArgument(1);

               return team1.getWins().compareTo(team2.getWins());
            });

            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()).year("2019").build());

            verify(teamComparator, atLeastOnce()).compare(any(Team.class), any(Team.class));

            final Integer[] playerIdsOrderedByTeamId = playerList.stream()
                    .sorted(Comparator.comparing(Player::getTeamId))
                    .map(Player::getPlayerId)
                    .toArray(Integer[]::new);

            assertArrayEquals(new Integer[]{3, 2, 1}, playerIdsOrderedByTeamId);
        }

        @Test
        void shouldShuffleTeamsFromCurrentYearWhenPreviousYearCallReturnsNoTeams() throws NATCException {
            final List<Player> playerList = List.of(
                    generatePlayer(1, 0.9),
                    generatePlayer(2, 0.7),
                    generatePlayer(3, 0.5),
                    generatePlayer(5, 0.3),
                    generatePlayer(5, 0.1)
            );

            when(teamService.getRegularTeamsByYear("2018")).thenReturn(Collections.emptyList());
            when(teamService.getRegularTeamsByYear("2019")).thenReturn(Arrays.asList(
                    Team.builder().teamId(1).build(),
                    Team.builder().teamId(2).build(),
                    Team.builder().teamId(3).build(),
                    Team.builder().teamId(4).build(),
                    Team.builder().teamId(5).build()
            ));
            when(managerService.getActiveManagersForYear(anyString())).thenReturn(List.of(
                    Manager.builder().managerId(1).teamId(1).style(ManagerStyle.OFFENSIVE.getValue()).build(),
                    Manager.builder().managerId(2).teamId(2).style(ManagerStyle.DEFENSIVE.getValue()).build(),
                    Manager.builder().managerId(3).teamId(3).style(ManagerStyle.PENALTIES.getValue()).build(),
                    Manager.builder().managerId(4).teamId(4).style(ManagerStyle.INTANGIBLE.getValue()).build(),
                    Manager.builder().managerId(5).teamId(5).style(ManagerStyle.BALANCED.getValue()).build()
            ));
            when(playerService.generatePlayers(anyString(), anyInt())).thenReturn(playerList);
            when(playerComparatorFactory.getPlayerComparatorForManager(any())).thenCallRealMethod();

            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()).year("2019").build());

            final Integer[] playerIdsOrderedByPlayerId = playerList.stream()
                    .sorted(Comparator.comparing(Player::getPlayerId))
                    .map(Player::getPlayerId)
                    .toArray(Integer[]::new);

            final Integer[] playerIdsOrderedByTeamId = playerList.stream()
                    .sorted(Comparator.comparing(Player::getTeamId))
                    .map(Player::getPlayerId)
                    .toArray(Integer[]::new);

            assertFalse(Arrays.equals(playerIdsOrderedByPlayerId, playerIdsOrderedByTeamId));
        }

        @Test
        void shouldCallPlayerServiceToCreateNewPlayersBasedOnConfigurationWhenRoundOne() throws NATCException {
            when(leagueConfiguration.getNewPlayersPerSeason()).thenReturn(10);

            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()).year("2019").build());

            verify(playerService).generatePlayers("2019", 10);
        }

        @Test
        void shouldCallPlayerServiceToUpdateNewPlayersWhenRoundOne() throws NATCException {
            final List<Player> generatedPlayers = List.of(
                    Player.builder().playerId(1).build(),
                    Player.builder().playerId(2).build(),
                    Player.builder().playerId(3).build(),
                    Player.builder().playerId(4).build(),
                    Player.builder().playerId(5).build()
            );

            when(playerService.generatePlayers(anyString(), any())).thenReturn(generatedPlayers);

            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()).year("2019").build());

            verify(playerService).updatePlayers(generatedPlayers);
        }

        @Test
        void shouldUpdateNewPlayersToBeRookiesOfStartingAgeWhenRoundOne() throws NATCException {
            final List<Player> generatedPlayers = List.of(
                    Player.builder().playerId(1).build(),
                    Player.builder().playerId(2).build(),
                    Player.builder().playerId(3).build(),
                    Player.builder().playerId(4).build(),
                    Player.builder().playerId(5).build()
            );

            when(playerService.generatePlayers(anyString(), any())).thenReturn(generatedPlayers);

            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()).year("2019").build());

            verify(playerService).updatePlayers(captor.capture());

            final List<Player> updatedPlayers = captor.getValue();

            assertEquals(generatedPlayers.size(), updatedPlayers.size());
            assertEquals(generatedPlayers.size(), updatedPlayers.stream()
                    .filter(player -> Objects.equals(player.getRookie(), 1)).count());
            assertEquals(generatedPlayers.size(), updatedPlayers.stream()
                    .filter(player -> Objects.equals(player.getAge(), Player.STARTING_AGE)).count());
        }

        @Test
        void shouldCallPlayerServiceToGetUndraftedRookiesForCurrentYearWhenRoundTwo() throws NATCException {
            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_2.getValue()).year("2003").build());

            verify(playerService).getUndraftedRookiesForYear("2003");
        }

        @Test
        void shouldCallPlayerServiceToUpdateUndraftedRookiePlayersWhenRoundTwo() throws NATCException {
            final List<Player> undraftedRookies = List.of(
                    Player.builder().playerId(1).build(),
                    Player.builder().playerId(2).build(),
                    Player.builder().playerId(3).build(),
                    Player.builder().playerId(4).build(),
                    Player.builder().playerId(5).build()
            );

            when(playerService.getUndraftedRookiesForYear(anyString())).thenReturn(undraftedRookies);

            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_2.getValue()).year("2003").build());

            verify(playerService).updatePlayers(undraftedRookies);
        }

        @Test
        void shouldCallManagerServiceToGetActiveManagersForCurrentYear() throws NATCException {
            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()).year("2019").build());

            verify(managerService).getActiveManagersForYear("2019");
        }

        @Test
        void shouldCallPlayerComparatorFactoryToGetPlayerComparator() throws NATCException {
            when(teamService.getRegularTeamsByYear(anyString())).thenReturn(Collections.singletonList(Team.builder().teamId(1).build()));
            when(managerService.getActiveManagersForYear(anyString())).thenReturn(Collections.singletonList(Manager.builder()
                    .managerId(1)
                    .teamId(1)
                    .style(ManagerStyle.DEFENSIVE.getValue())
                    .build()));
            when(playerService.generatePlayers(anyString(), anyInt())).thenReturn(Collections.singletonList(Player.builder().build()));
            when(playerComparatorFactory.getPlayerComparatorForManager(any())).thenReturn(mock(PlayerComparator.class));

            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()).year("2019").build());

            verify(playerComparatorFactory).getPlayerComparatorForManager(any());
        }

        @Test
        void shouldGetPlayerComparatorForEachTeamWithCorrespondingManagersStyle() throws NATCException {
            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).build(),
                    Team.builder().teamId(2).build(),
                    Team.builder().teamId(3).build()
            );
            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(1).teamId(1).style(ManagerStyle.OFFENSIVE.getValue()).build(),
                    Manager.builder().managerId(2).teamId(2).style(ManagerStyle.DEFENSIVE.getValue()).build(),
                    Manager.builder().managerId(3).teamId(3).style(ManagerStyle.BALANCED.getValue()).build()
            );
            final List<Player> playerList = List.of(
                    generatePlayer(1, 5.0),
                    generatePlayer(2, 5.0),
                    generatePlayer(3, 5.0)
            );


            when(teamService.getRegularTeamsByYear(anyString())).thenReturn(teamList);
            when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
            when(playerService.generatePlayers(anyString(), anyInt())).thenReturn(playerList);
            when(playerComparatorFactory.getPlayerComparatorForManager(any())).thenReturn(mock(PlayerComparator.class));

            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()).year("2022").build());

            verify(playerComparatorFactory, times(1)).getPlayerComparatorForManager(ManagerStyle.OFFENSIVE);
            verify(playerComparatorFactory, times(1)).getPlayerComparatorForManager(ManagerStyle.DEFENSIVE);
            verify(playerComparatorFactory, times(1)).getPlayerComparatorForManager(ManagerStyle.BALANCED);
        }

        @Test
        void shouldNotRetrievePlayerComparatorWithAnyAdjustments() throws NATCException {
            final PlayerRatingAdjustment[] adjustments = {};

            when(teamService.getRegularTeamsByYear(anyString())).thenReturn(Collections.singletonList(Team.builder().teamId(1).build()));
            when(managerService.getActiveManagersForYear(anyString())).thenReturn(Collections.singletonList(Manager.builder()
                    .managerId(1)
                    .teamId(1)
                    .style(ManagerStyle.DEFENSIVE.getValue())
                    .build()));
            when(playerService.generatePlayers(anyString(), anyInt())).thenReturn(Collections.singletonList(Player.builder().build()));
            when(playerComparatorFactory.getPlayerComparatorForManager(any())).thenReturn(mock(PlayerComparator.class));

            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()).year("2019").build());

            verify(playerComparatorFactory).getPlayerComparatorForManager(eq(ManagerStyle.DEFENSIVE), eq(adjustments));
        }

        @Test
        void shouldUseThePlayerComparatorReturnedFromThePlayerComparatorFactory() throws NATCException {
            final PlayerComparator playerComparator = mock(PlayerComparator.class);

            when(teamService.getRegularTeamsByYear(anyString())).thenReturn(Collections.singletonList(Team.builder().teamId(1).build()));
            when(managerService.getActiveManagersForYear(anyString())).thenReturn(Collections.singletonList(Manager.builder()
                    .managerId(1)
                    .teamId(1)
                    .style(ManagerStyle.DEFENSIVE.getValue())
                    .build()));
            when(playerService.generatePlayers(anyString(), anyInt())).thenReturn(List.of(
                    Player.builder().playerId(1).build(),
                    Player.builder().playerId(2).build()
            ));
            when(playerComparatorFactory.getPlayerComparatorForManager(any())).thenReturn(playerComparator);

            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()).year("2019").build());

            verify(playerComparator, atLeastOnce()).compare(any(Player.class), any(Player.class));
        }

        @Test
        void shouldPickBestRookieAndAssignTeamId() throws NATCException {
            final Player betterPlayer = generatePlayer(2, 0.7);
            final Player worsePlayer = generatePlayer(1, 0.3);

            final List<Player> rookies = List.of(worsePlayer, betterPlayer);

            when(teamService.getRegularTeamsByYear(anyString())).thenReturn(Collections.singletonList(Team.builder().teamId(1).build()));
            when(managerService.getActiveManagersForYear(anyString())).thenReturn(Collections.singletonList(Manager.builder()
                    .managerId(1)
                    .teamId(1)
                    .style(ManagerStyle.DEFENSIVE.getValue())
                    .build()));
            when(playerService.generatePlayers(anyString(), anyInt())).thenReturn(rookies);
            when(playerComparatorFactory.getPlayerComparatorForManager(any())).thenCallRealMethod();

            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()).year("2019").build());

            assertNull(worsePlayer.getTeamId());
            assertEquals(1, betterPlayer.getTeamId());
        }

        @Test
        void shouldHaveDifferentTeamsPickDifferentRookiePlayers() throws NATCException {
            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).build(),
                    Team.builder().teamId(2).build(),
                    Team.builder().teamId(3).build()
            );
            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(1).teamId(1).style(ManagerStyle.OFFENSIVE.getValue()).build(),
                    Manager.builder().managerId(2).teamId(2).style(ManagerStyle.DEFENSIVE.getValue()).build(),
                    Manager.builder().managerId(3).teamId(3).style(ManagerStyle.BALANCED.getValue()).build()
            );
            final List<Player> playerList = List.of(
                    generatePlayer(1, 4.0),
                    generatePlayer(2, 5.0),
                    generatePlayer(3, 6.0)
            );

            when(teamService.getRegularTeamsByYear(anyString())).thenReturn(teamList);
            when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
            when(playerService.generatePlayers(anyString(), anyInt())).thenReturn(playerList);
            when(playerComparatorFactory.getPlayerComparatorForManager(any())).thenCallRealMethod();

            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()).year("2022").build());

            assertEquals(playerList.size(), playerList.stream().map(Player::getTeamId).distinct().count());
        }

        @Test
        void shouldSetPickOnDraftedRookieStartingAtOneWhenRoundOne() throws NATCException {
            final Player rookie = generatePlayer(1, 5.0);

            when(teamService.getRegularTeamsByYear(anyString())).thenReturn(Collections.singletonList(Team.builder().teamId(1).build()));
            when(managerService.getActiveManagersForYear(anyString())).thenReturn(Collections.singletonList(Manager.builder()
                    .managerId(1)
                    .teamId(1)
                    .style(ManagerStyle.DEFENSIVE.getValue())
                    .build()));
            when(playerService.generatePlayers(anyString(), anyInt())).thenReturn(Collections.singletonList(rookie));
            when(playerComparatorFactory.getPlayerComparatorForManager(any())).thenCallRealMethod();

            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()).year("2019").build());

            assertEquals(1, rookie.getTeamId());
            assertEquals(1, rookie.getDraftPick());
        }

        @Test
        void shouldSetPickOnDraftedRookieStartingAtNumberOfTeamsPlusOneWhenRoundTwo() throws NATCException {
            final Player rookie = generatePlayer(1, 5.0);

            when(teamService.getRegularTeamsByYear(anyString())).thenReturn(Collections.singletonList(Team.builder().teamId(1).build()));
            when(managerService.getActiveManagersForYear(anyString())).thenReturn(Collections.singletonList(Manager.builder()
                    .managerId(1)
                    .teamId(1)
                    .style(ManagerStyle.DEFENSIVE.getValue())
                    .build()));
            when(playerService.getUndraftedRookiesForYear(anyString())).thenReturn(Collections.singletonList(rookie));
            when(playerComparatorFactory.getPlayerComparatorForManager(any())).thenCallRealMethod();
            when(leagueConfiguration.getNumberOfTeams()).thenReturn(28);

            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_2.getValue()).year("2019").build());

            assertEquals(1, rookie.getTeamId());
            assertEquals(29, rookie.getDraftPick());
        }

        @Test
        void shouldIncrementDraftPickValueForEachPlayerPickedWhenRoundOne() throws NATCException {
            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).build(),
                    Team.builder().teamId(2).build(),
                    Team.builder().teamId(3).build()
            );
            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(1).teamId(1).style(ManagerStyle.OFFENSIVE.getValue()).build(),
                    Manager.builder().managerId(2).teamId(2).style(ManagerStyle.DEFENSIVE.getValue()).build(),
                    Manager.builder().managerId(3).teamId(3).style(ManagerStyle.BALANCED.getValue()).build()
            );
            final List<Player> playerList = List.of(
                    generatePlayer(1, 4.0),
                    generatePlayer(2, 5.0),
                    generatePlayer(3, 6.0)
            );

            when(teamService.getRegularTeamsByYear(anyString())).thenReturn(teamList);
            when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
            when(playerService.generatePlayers(anyString(), anyInt())).thenReturn(playerList);
            when(playerComparatorFactory.getPlayerComparatorForManager(any())).thenCallRealMethod();

            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()).year("2022").build());

            assertEquals(3, playerList.get(0).getDraftPick());
            assertEquals(2, playerList.get(1).getDraftPick());
            assertEquals(1, playerList.get(2).getDraftPick());
        }

        @Test
        void shouldIncrementDraftPickValueForEachPlayerPickedWhenRoundTwo() throws NATCException {
            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).build(),
                    Team.builder().teamId(2).build(),
                    Team.builder().teamId(3).build()
            );
            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(1).teamId(1).style(ManagerStyle.OFFENSIVE.getValue()).build(),
                    Manager.builder().managerId(2).teamId(2).style(ManagerStyle.DEFENSIVE.getValue()).build(),
                    Manager.builder().managerId(3).teamId(3).style(ManagerStyle.BALANCED.getValue()).build()
            );
            final List<Player> playerList = List.of(
                    generatePlayer(1, 4.0),
                    generatePlayer(2, 5.0),
                    generatePlayer(3, 6.0)
            );

            when(teamService.getRegularTeamsByYear(anyString())).thenReturn(teamList);
            when(managerService.getActiveManagersForYear(anyString())).thenReturn(managerList);
            when(playerService.getUndraftedRookiesForYear(anyString())).thenReturn(playerList);
            when(playerComparatorFactory.getPlayerComparatorForManager(any())).thenCallRealMethod();
            when(leagueConfiguration.getNumberOfTeams()).thenReturn(40);

            processor.process(Schedule.builder().type(ScheduleType.ROOKIE_DRAFT_ROUND_2.getValue()).year("2022").build());

            assertEquals(43, playerList.get(0).getDraftPick());
            assertEquals(42, playerList.get(1).getDraftPick());
            assertEquals(41, playerList.get(2).getDraftPick());
        }
    }

    private Player generatePlayer(final int playerId, final double rating) {
        return Player.builder()
                .playerId(playerId)
                .year("2020")
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