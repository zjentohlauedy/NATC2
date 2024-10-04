package org.natc.app.processor;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.*;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.ManagerRepository;
import org.natc.app.repository.PlayerRepository;
import org.natc.app.repository.ScheduleRepository;
import org.natc.app.repository.TeamRepository;
import org.natc.app.service.NATCServiceIntegrationTest;
import org.natc.app.util.TestHelpers;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RookieDraftScheduleProcessorIntegrationTest extends NATCServiceIntegrationTest {
    @Autowired
    private LeagueConfiguration leagueConfiguration;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TestHelpers testHelpers;

    @Autowired
    private RookieDraftScheduleProcessor processor;

    @Nested
    class Process {
        @Test
        void shouldUpdateTheGivenScheduleStatusToCompletedForRoundOne() throws NATCException {
            testHelpers.seedFirstAndLastNames();

            final Schedule schedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            scheduleRepository.save(schedule);

            processor.process(schedule);

            final List<Schedule> scheduleList = scheduleRepository.findAll();

            assertEquals(1, scheduleList.size());
            assertEquals(ScheduleStatus.COMPLETED.getValue(), scheduleList.getFirst().getStatus());
        }

        @Test
        void shouldUpdateTheGivenScheduleStatusToCompletedForRoundTwo() throws NATCException {
            testHelpers.seedFirstAndLastNames();

            final Schedule schedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.ROOKIE_DRAFT_ROUND_2.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            scheduleRepository.save(schedule);

            processor.process(schedule);

            final List<Schedule> scheduleList = scheduleRepository.findAll();

            assertEquals(1, scheduleList.size());
            assertEquals(ScheduleStatus.COMPLETED.getValue(), scheduleList.getFirst().getStatus());
        }

        @Test
        void shouldCreateRookiePlayersInRoundOne() throws NATCException {
            testHelpers.seedFirstAndLastNames();

            final Schedule schedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            scheduleRepository.save(schedule);

            processor.process(schedule);

            final List<Player> playerList = playerRepository.findAll();

            assertEquals(leagueConfiguration.getNewPlayersPerSeason(), playerList.size());
            assertEquals(leagueConfiguration.getNewPlayersPerSeason().longValue(),
                    playerList.stream().filter(player -> player.getAge().equals(Player.STARTING_AGE) && player.getRookie().equals(1)).count()
            );
        }

        @Test
        void shouldNotCreateNewRookiePlayersInRoundTwo() throws NATCException {
            final Schedule schedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.ROOKIE_DRAFT_ROUND_2.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            scheduleRepository.save(schedule);

            processor.process(schedule);

            final List<Player> playerList = playerRepository.findAll();

            assertTrue(playerList.isEmpty());
        }

        @Test
        void shouldDraftRookiesFromBestToWorst() throws NATCException {
            final Schedule schedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.ROOKIE_DRAFT_ROUND_2.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            scheduleRepository.save(schedule);

            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).year("2005").allstarTeam(0).build(),
                    Team.builder().teamId(2).year("2005").allstarTeam(0).build(),
                    Team.builder().teamId(3).year("2005").allstarTeam(0).build(),
                    Team.builder().teamId(4).year("2005").allstarTeam(0).build(),
                    Team.builder().teamId(5).year("2005").allstarTeam(0).build()
            );

            teamRepository.saveAll(teamList);

            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(1).teamId(1).year("2005").style(ManagerStyle.BALANCED.getValue()).retired(0).build(),
                    Manager.builder().managerId(2).teamId(2).year("2005").style(ManagerStyle.BALANCED.getValue()).retired(0).build(),
                    Manager.builder().managerId(3).teamId(3).year("2005").style(ManagerStyle.BALANCED.getValue()).retired(0).build(),
                    Manager.builder().managerId(4).teamId(4).year("2005").style(ManagerStyle.BALANCED.getValue()).retired(0).build(),
                    Manager.builder().managerId(5).teamId(5).year("2005").style(ManagerStyle.BALANCED.getValue()).retired(0).build()
            );

            managerRepository.saveAll(managerList);

            final List<Player> playerList = List.of(
                    generatePlayer(1, "2005", 0.7, ManagerStyle.BALANCED),
                    generatePlayer(2, "2005", 0.9, ManagerStyle.BALANCED),
                    generatePlayer(3, "2005", 0.4, ManagerStyle.BALANCED),
                    generatePlayer(4, "2005", 0.8, ManagerStyle.BALANCED),
                    generatePlayer(5, "2005", 0.6, ManagerStyle.BALANCED),
                    generatePlayer(6, "2005", 0.2, ManagerStyle.BALANCED),
                    generatePlayer(7, "2005", 0.5, ManagerStyle.BALANCED),
                    generatePlayer(8, "2005", 0.3, ManagerStyle.BALANCED)
            );

            playerRepository.saveAll(playerList);

            processor.process(schedule);

            final List<Player> postDraftPlayers = playerRepository.findAll();

            assertEquals(playerList.size(), postDraftPlayers.size());

            final List<Player> draftedPlayers = postDraftPlayers.stream().filter(player -> Objects.nonNull(player.getTeamId())).toList();

            assertEquals(teamList.size(), draftedPlayers.size());

            final List<Integer> draftOrder = draftedPlayers.stream()
                    .sorted(Comparator.comparing(Player::getDraftPick))
                    .map(Player::getPlayerId)
                    .toList();
            final List<Integer> ratingOrder = draftedPlayers.stream().sorted(Comparator.comparing(Player::getScoring).reversed())
                    .map(Player::getPlayerId)
                    .toList();

            assertEquals(ratingOrder, draftOrder);
        }

        @Test
        void shouldHaveTeamsDraftFromWorstToBestWhenThereWasAPreviousSeason() throws NATCException {
            testHelpers.seedFirstAndLastNames();

            final Schedule schedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            scheduleRepository.save(schedule);

            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).year("2004").games(100).wins(75).allstarTeam(0).build(),
                    Team.builder().teamId(2).year("2004").games(100).wins(25).allstarTeam(0).build(),
                    Team.builder().teamId(3).year("2004").games(100).wins(50).allstarTeam(0).build(),
                    Team.builder().teamId(4).year("2004").games(100).wins(66).allstarTeam(0).build(),
                    Team.builder().teamId(5).year("2004").games(100).wins(33).allstarTeam(0).build()
            );

            teamRepository.saveAll(teamList);

            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(1).teamId(1).year("2005").style(ManagerStyle.BALANCED.getValue()).retired(0).build(),
                    Manager.builder().managerId(2).teamId(2).year("2005").style(ManagerStyle.BALANCED.getValue()).retired(0).build(),
                    Manager.builder().managerId(3).teamId(3).year("2005").style(ManagerStyle.BALANCED.getValue()).retired(0).build(),
                    Manager.builder().managerId(4).teamId(4).year("2005").style(ManagerStyle.BALANCED.getValue()).retired(0).build(),
                    Manager.builder().managerId(5).teamId(5).year("2005").style(ManagerStyle.BALANCED.getValue()).retired(0).build()
            );

            managerRepository.saveAll(managerList);

            processor.process(schedule);

            final List<Player> postDraftPlayers = playerRepository.findAll();

            final List<Player> draftedPlayers = postDraftPlayers.stream().filter(player -> Objects.nonNull(player.getTeamId())).toList();

            assertEquals(teamList.size(), draftedPlayers.size());

            final List<Integer> draftOrder = draftedPlayers.stream()
                    .sorted(Comparator.comparing(Player::getDraftPick))
                    .map(Player::getTeamId)
                    .toList();

            final List<Integer> teamOrderByWins = teamList.stream()
                    .sorted(Comparator.comparing(Team::getWins))
                    .map(Team::getTeamId)
                    .toList();

            assertEquals(teamOrderByWins, draftOrder);
        }

        @Test
        void shouldDraftRookiesBasedOnManagerStyleForTeam() throws NATCException {
            final Schedule schedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.ROOKIE_DRAFT_ROUND_2.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            scheduleRepository.save(schedule);

            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).year("2005").allstarTeam(0).build(),
                    Team.builder().teamId(2).year("2005").allstarTeam(0).build(),
                    Team.builder().teamId(3).year("2005").allstarTeam(0).build(),
                    Team.builder().teamId(4).year("2005").allstarTeam(0).build(),
                    Team.builder().teamId(5).year("2005").allstarTeam(0).build()
            );

            teamRepository.saveAll(teamList);

            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(1).teamId(1).year("2005").style(ManagerStyle.OFFENSIVE.getValue()).retired(0).build(),
                    Manager.builder().managerId(2).teamId(2).year("2005").style(ManagerStyle.DEFENSIVE.getValue()).retired(0).build(),
                    Manager.builder().managerId(3).teamId(3).year("2005").style(ManagerStyle.INTANGIBLE.getValue()).retired(0).build(),
                    Manager.builder().managerId(4).teamId(4).year("2005").style(ManagerStyle.PENALTIES.getValue()).retired(0).build(),
                    Manager.builder().managerId(5).teamId(5).year("2005").style(ManagerStyle.BALANCED.getValue()).retired(0).build()
            );

            managerRepository.saveAll(managerList);

            final List<Player> playerList = List.of(
                    generatePlayer(1, "2005", 0.7, ManagerStyle.OFFENSIVE),
                    generatePlayer(2, "2005", 0.7, ManagerStyle.DEFENSIVE),
                    generatePlayer(3, "2005", 0.7, ManagerStyle.INTANGIBLE),
                    generatePlayer(4, "2005", 0.7, ManagerStyle.PENALTIES),
                    generatePlayer(5, "2005", 0.6, ManagerStyle.BALANCED)
            );

            playerRepository.saveAll(playerList);

            processor.process(schedule);

            final List<Player> postDraftPlayers = playerRepository.findAll();

            final List<Player> draftedPlayers = postDraftPlayers.stream().filter(player -> Objects.nonNull(player.getTeamId())).toList();

            assertEquals(teamList.size(), draftedPlayers.size());
            assertEquals(teamList.size(), draftedPlayers.stream().map(Player::getTeamId).distinct().count());

            for (final Player player : draftedPlayers) {
                assertEquals(player.getPlayerId(), player.getTeamId());
            }
        }

        @Test
        void shouldContinueRoundOneDraftInRoundTwo() throws NATCException {
            testHelpers.seedFirstAndLastNames();

            final Schedule roundOneSchedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            final Schedule roundTwoSchedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.ROOKIE_DRAFT_ROUND_2.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            scheduleRepository.save(roundOneSchedule);
            scheduleRepository.save(roundTwoSchedule);

            final List<Team> teamList = List.of(
                    Team.builder().teamId(1).year("2004").games(100).wins(75).allstarTeam(0).build(),
                    Team.builder().teamId(2).year("2004").games(100).wins(25).allstarTeam(0).build(),
                    Team.builder().teamId(3).year("2004").games(100).wins(50).allstarTeam(0).build(),
                    Team.builder().teamId(4).year("2004").games(100).wins(66).allstarTeam(0).build(),
                    Team.builder().teamId(5).year("2004").games(100).wins(33).allstarTeam(0).build()
            );

            teamRepository.saveAll(teamList);

            final List<Manager> managerList = List.of(
                    Manager.builder().managerId(1).teamId(1).year("2005").style(ManagerStyle.OFFENSIVE.getValue()).retired(0).build(),
                    Manager.builder().managerId(2).teamId(2).year("2005").style(ManagerStyle.DEFENSIVE.getValue()).retired(0).build(),
                    Manager.builder().managerId(3).teamId(3).year("2005").style(ManagerStyle.INTANGIBLE.getValue()).retired(0).build(),
                    Manager.builder().managerId(4).teamId(4).year("2005").style(ManagerStyle.PENALTIES.getValue()).retired(0).build(),
                    Manager.builder().managerId(5).teamId(5).year("2005").style(ManagerStyle.BALANCED.getValue()).retired(0).build()
            );

            managerRepository.saveAll(managerList);

            processor.process(roundOneSchedule);
            processor.process(roundTwoSchedule);

            final List<Player> postDraftPlayers = playerRepository.findAll();

            final List<Player> draftedPlayers = postDraftPlayers.stream().filter(player -> Objects.nonNull(player.getTeamId())).toList();

            assertEquals(teamList.size() * 2, draftedPlayers.size());
            assertEquals(teamList.size() * 2L, draftedPlayers.stream().map(Player::getDraftPick).distinct().count());
            assertEquals(teamList.size(), draftedPlayers.stream().map(Player::getTeamId).distinct().count());

            for (final Team team : teamList) {
                assertEquals(2, draftedPlayers.stream().filter(player -> player.getTeamId().equals(team.getTeamId())).count());
            }
        }
    }

    private Player generatePlayer(final int playerId, final String year, final double rating, final ManagerStyle managerStyle) {
        return switch (managerStyle) {
            case ManagerStyle.OFFENSIVE -> Player.builder()
                    .playerId(playerId)
                    .year(year)
                    .age(Player.STARTING_AGE)
                    .scoring(rating)
                    .passing(rating)
                    .blocking(rating)
                    .tackling(0.01)
                    .stealing(0.01)
                    .presence(0.01)
                    .discipline(0.01)
                    .endurance(0.01)
                    .penaltyShot(0.01)
                    .penaltyOffense(0.01)
                    .penaltyDefense(0.01)
                    .rookie(1)
                    .build();
            case ManagerStyle.DEFENSIVE -> Player.builder()
                    .playerId(playerId)
                    .year(year)
                    .age(Player.STARTING_AGE)
                    .scoring(0.01)
                    .passing(0.01)
                    .blocking(0.01)
                    .tackling(rating)
                    .stealing(rating)
                    .presence(rating)
                    .discipline(0.01)
                    .endurance(0.01)
                    .penaltyShot(0.01)
                    .penaltyOffense(0.01)
                    .penaltyDefense(0.01)
                    .rookie(1)
                    .build();
            case ManagerStyle.INTANGIBLE -> Player.builder()
                    .playerId(playerId)
                    .year(year)
                    .age(Player.STARTING_AGE)
                    .scoring(0.01)
                    .passing(0.01)
                    .blocking(rating)
                    .tackling(0.01)
                    .stealing(0.01)
                    .presence(rating)
                    .discipline(rating)
                    .endurance(rating)
                    .penaltyShot(0.01)
                    .penaltyOffense(0.01)
                    .penaltyDefense(0.01)
                    .rookie(1)
                    .build();
            case ManagerStyle.PENALTIES -> Player.builder()
                    .playerId(playerId)
                    .year(year)
                    .age(Player.STARTING_AGE)
                    .scoring(0.01)
                    .passing(0.01)
                    .blocking(0.01)
                    .tackling(0.01)
                    .stealing(0.01)
                    .presence(0.01)
                    .discipline(0.01)
                    .endurance(0.01)
                    .penaltyShot(rating)
                    .penaltyOffense(rating)
                    .penaltyDefense(rating)
                    .rookie(1)
                    .build();
            case ManagerStyle.BALANCED -> Player.builder()
                    .playerId(playerId)
                    .year(year)
                    .age(Player.STARTING_AGE)
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
                    .rookie(1)
                    .build();
        };
    }
}