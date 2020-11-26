package org.natc.app.manager;

import org.junit.jupiter.api.Test;
import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.Player;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.entity.domain.Team;
import org.natc.app.exception.NATCException;
import org.natc.app.exception.ScheduleProcessingException;
import org.natc.app.repository.ManagerRepository;
import org.natc.app.repository.PlayerRepository;
import org.natc.app.repository.ScheduleRepository;
import org.natc.app.repository.TeamRepository;
import org.natc.app.service.NATCServiceIntegrationTest;
import org.natc.app.util.TestHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SeasonManagerIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private TestHelpers testHelpers;

    @Autowired
    private LeagueConfiguration leagueConfiguration;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SeasonManager seasonManager;

    @Test
    public void processScheduledEvent_ShouldHandleInProgressSchedule() throws NATCException {
        final List<Schedule> initialScheduleList = Arrays.asList(
                Schedule.builder().year("2001").sequence(24).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(2)).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2001").sequence(25).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(1)).status(ScheduleStatus.IN_PROGRESS.getValue()).build(),
                Schedule.builder().year("2001").sequence(26).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now()).status(ScheduleStatus.SCHEDULED.getValue()).build()
        );

        scheduleRepository.saveAll(initialScheduleList);

        seasonManager.processScheduledEvent();

        final Example<Schedule> example = Example.of(Schedule.builder().year("2001").sequence(26).build());
        final List<Schedule> afterScheduleList = scheduleRepository.findAll(example);

        assertEquals(ScheduleStatus.SCHEDULED.getValue(), afterScheduleList.get(0).getStatus());
    }

    @Test
    public void processScheduledEvent_ShouldGenerateANewScheduleWhenNoSchedulesExistAndPutBeginningOfSeasonInProgress() throws NATCException {
        testHelpers.seedFirstAndLastNames();

        seasonManager.processScheduledEvent();

        final Example<Schedule> example = Example.of(Schedule.builder().year(leagueConfiguration.getFirstSeason()).sequence(1).build());
        final List<Schedule> afterScheduleList = scheduleRepository.findAll(example);

        assertEquals(ScheduleType.BEGINNING_OF_SEASON.getValue(), afterScheduleList.get(0).getType());
        assertEquals(ScheduleStatus.IN_PROGRESS.getValue(), afterScheduleList.get(0).getStatus());
    }

    @Test
    public void processScheduledEvent_ShouldGenerateANewLeagueWhenNoSchedulesExist() throws NATCException {
        testHelpers.seedFirstAndLastNames();

        seasonManager.processScheduledEvent();

        final List<Team> teamList = teamRepository.findAll();
        final List<Manager> managerList = managerRepository.findAll();
        final List<Player> playerList = playerRepository.findAll();

        assertFalse(teamList.isEmpty());
        assertFalse(managerList.isEmpty());
        assertFalse(playerList.isEmpty());
    }

    @Test
    public void processScheduledEvent_ShouldUpdateLeagueToNextYearWhenEndOfSeason() throws NATCException {
        final List<Schedule> initialScheduleList = Arrays.asList(
                Schedule.builder().year("2001").sequence(24).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(2)).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2001").sequence(25).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(1)).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2001").sequence(26).type(ScheduleType.END_OF_SEASON.getValue())
                        .scheduled(LocalDate.now()).status(ScheduleStatus.COMPLETED.getValue()).build()
        );

        scheduleRepository.saveAll(initialScheduleList);
        teamRepository.save(Team.builder().teamId(1).year("2001").build());
        managerRepository.save(Manager.builder().managerId(1).year("2001").build());
        playerRepository.save(Player.builder().playerId(1).year("2001").build());

        seasonManager.processScheduledEvent();

        final List<Team> teamList = teamRepository.findAll(Example.of(Team.builder().year("2002").build()));
        final List<Manager> managerList = managerRepository.findAll(Example.of(Manager.builder().year("2002").build()));
        final List<Player> playerList = playerRepository.findAll(Example.of(Player.builder().year("2002").build()));

        assertFalse(teamList.isEmpty());
        assertFalse(managerList.isEmpty());
        assertFalse(playerList.isEmpty());
    }

    @Test
    public void processScheduledEvent_ShouldGenerateANewScheduleForNextYearWhenEndOfSeasonAndPutNewBeginningOfSeasonInProgress() throws NATCException {
        final List<Schedule> initialScheduleList = Arrays.asList(
                Schedule.builder().year("2001").sequence(24).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(2)).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2001").sequence(25).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(1)).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2001").sequence(26).type(ScheduleType.END_OF_SEASON.getValue())
                        .scheduled(LocalDate.now()).status(ScheduleStatus.COMPLETED.getValue()).build()
        );

        scheduleRepository.saveAll(initialScheduleList);

        seasonManager.processScheduledEvent();

        final Example<Schedule> example = Example.of(Schedule.builder().year("2002").sequence(1).build());
        final List<Schedule> afterScheduleList = scheduleRepository.findAll(example);

        assertEquals(ScheduleType.BEGINNING_OF_SEASON.getValue(), afterScheduleList.get(0).getType());
        assertEquals(ScheduleStatus.IN_PROGRESS.getValue(), afterScheduleList.get(0).getStatus());
    }

    @Test
    public void processScheduledEvent_ShouldThrowScheduleProcessingExceptionWhenNoScheduledEntriesExist() {
        final List<Schedule> initialScheduleList = Arrays.asList(
                Schedule.builder().year("2001").sequence(24).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(2)).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2001").sequence(25).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(1)).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2001").sequence(26).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now()).status(ScheduleStatus.COMPLETED.getValue()).build()
        );

        scheduleRepository.saveAll(initialScheduleList);

        assertThrows(ScheduleProcessingException.class, () -> seasonManager.processScheduledEvent());
    }

    @Test
    public void processScheduledEvent_ShouldSetScheduleStatusToInProgress() throws NATCException {
        final List<Schedule> initialScheduleList = Arrays.asList(
                Schedule.builder().year("2001").sequence(24).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(2)).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2001").sequence(25).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now().minusDays(1)).status(ScheduleStatus.COMPLETED.getValue()).build(),
                Schedule.builder().year("2001").sequence(26).type(ScheduleType.REGULAR_SEASON.getValue())
                        .scheduled(LocalDate.now()).status(ScheduleStatus.SCHEDULED.getValue()).build()
        );

        scheduleRepository.saveAll(initialScheduleList);

        seasonManager.processScheduledEvent();

        final Example<Schedule> example = Example.of(Schedule.builder().year("2001").sequence(26).build());
        final List<Schedule> afterScheduleList = scheduleRepository.findAll(example);

        assertEquals(ScheduleStatus.IN_PROGRESS.getValue(), afterScheduleList.get(0).getStatus());
    }
}