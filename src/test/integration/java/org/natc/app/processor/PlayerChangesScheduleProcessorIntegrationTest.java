package org.natc.app.processor;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.*;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.ManagerRepository;
import org.natc.app.repository.PlayerRepository;
import org.natc.app.repository.ScheduleRepository;
import org.natc.app.service.NATCServiceIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PlayerChangesScheduleProcessorIntegrationTest extends NATCServiceIntegrationTest {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerChangesScheduleProcessor processor;

    @Nested
    class Process {

        @Test
        void shouldUpdateTheGivenScheduleStatusToCompleted() throws NATCException {
            final Schedule schedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.PLAYER_CHANGES.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            scheduleRepository.save(schedule);

            processor.process(schedule);

            final List<Schedule> scheduleList = scheduleRepository.findAll();

            assertEquals(1, scheduleList.size());
            assertEquals(ScheduleStatus.COMPLETED.getValue(), scheduleList.get(0).getStatus());
        }

        @Test
        void shouldLetPlayersRetireAndReplaceThemOnTheTeam() throws NATCException {
            final Schedule schedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.PLAYER_CHANGES.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            scheduleRepository.save(schedule);

            final List<Manager> managerList = Arrays.asList(
                    Manager.builder().managerId(1).year("2005").teamId(1).style(ManagerStyle.OFFENSIVE.getValue()).retired(0).build(),
                    Manager.builder().managerId(2).year("2005").teamId(2).style(ManagerStyle.DEFENSIVE.getValue()).retired(0).build(),
                    Manager.builder().managerId(3).year("2005").teamId(3).style(ManagerStyle.BALANCED.getValue()).retired(0).build()
            );

            managerRepository.saveAll(managerList);

            final List<Player> playerList = new ArrayList<>();

            // Each team has 13 players increasing in age from 28 to 40 and high ratings
            for (int i = 1; i <= 3; i++) {
                for (int j = 1; j <= 13; j++) {
                    playerList.add(generatePlayer((100 * i) + j, 27 + j, i, "2005", 0.9));
                }
            }

            // All free agents are 25 with low ratings
            for (int i = 401; i <= 450; i++) {
                playerList.add(generatePlayer(i, 25, null, "2005", 0.1));
            }

            playerRepository.saveAll(playerList);

            processor.process(schedule);

            final List<Player> finalPlayerList = playerRepository.findAll();

            assertEquals(13, finalPlayerList.stream().filter(player -> Objects.equals(player.getTeamId(), 1)).count());
            assertEquals(13, finalPlayerList.stream().filter(player -> Objects.equals(player.getTeamId(), 2)).count());
            assertEquals(13, finalPlayerList.stream().filter(player -> Objects.equals(player.getTeamId(), 3)).count());

            final List<Player> retiredPlayers = finalPlayerList.stream()
                    .filter(player -> Objects.equals(player.getRetired(), 1))
                    .toList();

            assertFalse(retiredPlayers.isEmpty());

            for (Player player : retiredPlayers) {
                assertNull(player.getTeamId());
                assertNotNull(player.getFormerTeamId());
            }

            final List<Player> signedFeeAgents = finalPlayerList.stream()
                    .filter(player -> player.getPlayerId() > 400 && Objects.nonNull(player.getTeamId()))
                    .toList();

            assertFalse(signedFeeAgents.isEmpty());
            assertEquals(retiredPlayers.size(), signedFeeAgents.size());

            for (Player player : signedFeeAgents) {
                assertEquals(1, player.getSigned());
                assertEquals(0, player.getFreeAgent());
            }
        }

        @Test
        void shouldRetireOlderFreeAgentsAndTeamPlayersAfterBeingReleased() throws NATCException {
            final Schedule schedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.PLAYER_CHANGES.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            scheduleRepository.save(schedule);

            final List<Manager> managerList = Arrays.asList(
                    Manager.builder().managerId(1).year("2005").teamId(1).style(ManagerStyle.OFFENSIVE.getValue()).retired(0).build(),
                    Manager.builder().managerId(2).year("2005").teamId(2).style(ManagerStyle.DEFENSIVE.getValue()).retired(0).build(),
                    Manager.builder().managerId(3).year("2005").teamId(3).style(ManagerStyle.BALANCED.getValue()).retired(0).build()
            );

            managerRepository.saveAll(managerList);

            final List<Player> playerList = new ArrayList<>();

            // Each team has 13 players at 30 with some lower rated than free agents
            for (int i = 1; i <= 3; i++) {
                for (int j = 1; j <= 13; j++) {
                    playerList.add(generatePlayer((100 * i) + j, 30, i, "2005", j > 8 ? 0.5 : 0.7));
                }
            }

            // Free agents that are 25 with mid range ratings
            for (int i = 401; i <= 450; i++) {
                playerList.add(generatePlayer(i, 25, null, "2005", 0.5));
            }

            // Free agents that are 29 with low ratings
            for (int i = 501; i <= 510; i++) {
                playerList.add(generatePlayer(i, 29, null, "2005", 0.3));
            }

            playerRepository.saveAll(playerList);

            processor.process(schedule);

            final List<Player> finalPlayerList = playerRepository.findAll();

            final List<Player> retiredReleasedPlayers = finalPlayerList.stream()
                    .filter(player -> Objects.equals(player.getRetired(), 1) && Objects.equals(player.getFreeAgent(), 1))
                    .toList();

            assertFalse(retiredReleasedPlayers.isEmpty());

            for (Player player : retiredReleasedPlayers) {
                assertNull(player.getTeamId());
                assertNotNull(player.getFormerTeamId());
                assertEquals(player.getReleased(), 0);
                assertEquals(player.getSigned(), 0);
            }

            final List<Player> retiredFreeAgents = finalPlayerList.stream()
                    .filter(player -> Objects.equals(player.getRetired(), 1) && player.getPlayerId() > 500)
                    .toList();

            assertEquals(10, retiredFreeAgents.size());

            for (Player player : retiredFreeAgents) {
                assertNull(player.getTeamId());
                assertNull(player.getFormerTeamId());
                assertEquals(player.getReleased(), 0);
            }
        }

        @Test
        void shouldMovePlayersBetweenTeamsAndFreeAgency() throws NATCException {
            final Schedule schedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.PLAYER_CHANGES.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            scheduleRepository.save(schedule);

            final List<Manager> managerList = Arrays.asList(
                    Manager.builder().managerId(1).year("2005").teamId(1).style(ManagerStyle.OFFENSIVE.getValue()).retired(0).build(),
                    Manager.builder().managerId(2).year("2005").teamId(2).style(ManagerStyle.DEFENSIVE.getValue()).retired(0).build(),
                    Manager.builder().managerId(3).year("2005").teamId(3).style(ManagerStyle.BALANCED.getValue()).retired(0).build()
            );

            managerRepository.saveAll(managerList);

            final List<Player> playerList = new ArrayList<>();

            playerList.add(generatePlayer(101, 20, 1, "2005", 0.9));
            playerList.add(generatePlayer(102, 20, 1, "2005", 0.9));
            playerList.add(generatePlayer(103, 20, 1, "2005", 0.9));
            playerList.add(generatePlayer(104, 20, 1, "2005", 0.7));
            playerList.add(generatePlayer(105, 20, 1, "2005", 0.7));
            playerList.add(generatePlayer(106, 20, 1, "2005", 0.7));
            playerList.add(generatePlayer(107, 20, 1, "2005", 0.7));
            playerList.add(generatePlayer(108, 20, 1, "2005", 0.7));
            playerList.add(generatePlayer(109, 20, 1, "2005", 0.5));
            playerList.add(generatePlayer(110, 20, 1, "2005", 0.5));
            playerList.add(generatePlayer(111, 20, 1, "2005", 0.5));
            playerList.add(generatePlayer(112, 20, 1, "2005", 0.5));
            playerList.add(generatePlayer(113, 20, 1, "2005", 0.5));

            playerList.add(generatePlayer(201, 20, 2, "2005", 0.7));
            playerList.add(generatePlayer(202, 20, 2, "2005", 0.7));
            playerList.add(generatePlayer(203, 20, 2, "2005", 0.7));
            playerList.add(generatePlayer(204, 20, 2, "2005", 0.7));
            playerList.add(generatePlayer(205, 20, 2, "2005", 0.7));
            playerList.add(generatePlayer(206, 20, 2, "2005", 0.5));
            playerList.add(generatePlayer(207, 20, 2, "2005", 0.5));
            playerList.add(generatePlayer(208, 20, 2, "2005", 0.5));
            playerList.add(generatePlayer(209, 20, 2, "2005", 0.3));
            playerList.add(generatePlayer(210, 20, 2, "2005", 0.3));
            playerList.add(generatePlayer(211, 20, 2, "2005", 0.3));
            playerList.add(generatePlayer(212, 20, 2, "2005", 0.3));
            playerList.add(generatePlayer(213, 20, 2, "2005", 0.3));

            playerList.add(generatePlayer(301, 20, 3, "2005", 0.9));
            playerList.add(generatePlayer(302, 20, 3, "2005", 0.9));
            playerList.add(generatePlayer(303, 20, 3, "2005", 0.9));
            playerList.add(generatePlayer(304, 20, 3, "2005", 0.7));
            playerList.add(generatePlayer(305, 20, 3, "2005", 0.7));
            playerList.add(generatePlayer(306, 20, 3, "2005", 0.7));
            playerList.add(generatePlayer(307, 20, 3, "2005", 0.5));
            playerList.add(generatePlayer(308, 20, 3, "2005", 0.5));
            playerList.add(generatePlayer(309, 20, 3, "2005", 0.5));
            playerList.add(generatePlayer(310, 20, 3, "2005", 0.3));
            playerList.add(generatePlayer(311, 20, 3, "2005", 0.3));
            playerList.add(generatePlayer(312, 20, 3, "2005", 0.3));
            playerList.add(generatePlayer(313, 20, 3, "2005", 0.1));

            playerList.add(generatePlayer(401, 20, null, "2005", 0.8));
            playerList.add(generatePlayer(402, 20, null, "2005", 0.8));
            playerList.add(generatePlayer(403, 20, null, "2005", 0.8));
            playerList.add(generatePlayer(404, 20, null, "2005", 0.8));
            playerList.add(generatePlayer(405, 20, null, "2005", 0.8));
            playerList.add(generatePlayer(406, 20, null, "2005", 0.6));
            playerList.add(generatePlayer(407, 20, null, "2005", 0.6));
            playerList.add(generatePlayer(408, 20, null, "2005", 0.6));
            playerList.add(generatePlayer(409, 20, null, "2005", 0.4));
            playerList.add(generatePlayer(410, 20, null, "2005", 0.4));
            playerList.add(generatePlayer(411, 20, null, "2005", 0.4));
            playerList.add(generatePlayer(412, 20, null, "2005", 0.4));
            playerList.add(generatePlayer(413, 20, null, "2005", 0.4));
            playerList.add(generatePlayer(414, 20, null, "2005", 0.2));
            playerList.add(generatePlayer(415, 20, null, "2005", 0.2));

            playerRepository.saveAll(playerList);

            processor.process(schedule);

            final List<Player> finalPlayerList = playerRepository.findAll();

            assertEquals(13, finalPlayerList.stream().filter(player -> Objects.equals(player.getTeamId(), 1)).count());
            assertEquals(13, finalPlayerList.stream().filter(player -> Objects.equals(player.getTeamId(), 2)).count());
            assertEquals(13, finalPlayerList.stream().filter(player -> Objects.equals(player.getTeamId(), 3)).count());

            final List<Player> keptPlayers = finalPlayerList.stream()
                    .filter(player -> Objects.nonNull(player.getTeamId()) &&
                            Objects.isNull(player.getFormerTeamId()) &&
                            player.getPlayerId() < 400)
                    .toList();

            final List<Player> releasedPlayers = finalPlayerList.stream()
                    .filter(player -> Objects.isNull(player.getTeamId()) &&
                            Objects.nonNull(player.getFormerTeamId()) &&
                            player.getPlayerId() < 400)
                    .toList();

            final List<Player> signedFreeAgents = finalPlayerList.stream()
                    .filter(player -> Objects.nonNull(player.getTeamId()) &&
                            Objects.isNull(player.getFormerTeamId()) &&
                            player.getPlayerId() > 400)
                    .toList();

            final List<Player> unsignedFreeAgents = finalPlayerList.stream()
                    .filter(player -> Objects.isNull(player.getTeamId()) &&
                            Objects.isNull(player.getFormerTeamId()) &&
                            player.getPlayerId() > 400)
                    .toList();

            final List<Player> resignedPlayers = finalPlayerList.stream()
                    .filter(player -> Objects.nonNull(player.getTeamId()) &&
                            Objects.nonNull(player.getFormerTeamId()) &&
                            player.getPlayerId() < 400)
                    .toList();

            assertFalse(keptPlayers.isEmpty());
            assertFalse(releasedPlayers.isEmpty());
            assertFalse(signedFreeAgents.isEmpty());
            assertFalse(unsignedFreeAgents.isEmpty());
            assertFalse(resignedPlayers.isEmpty());
        }
    }

    private Player generatePlayer(final int playerId, final int age, final Integer teamId, final String year, final double rating) {
        return Player.builder()
                .playerId(playerId)
                .teamId(teamId)
                .year(year)
                .age(age)
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
                .retired(0)
                .build();
    }
}