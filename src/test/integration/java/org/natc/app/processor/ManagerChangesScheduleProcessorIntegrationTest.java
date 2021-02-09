package org.natc.app.processor;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.entity.domain.Team;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.ManagerRepository;
import org.natc.app.repository.ScheduleRepository;
import org.natc.app.repository.TeamRepository;
import org.natc.app.service.NATCServiceIntegrationTest;
import org.natc.app.util.TestHelpers;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ManagerChangesScheduleProcessorIntegrationTest extends NATCServiceIntegrationTest {

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
    private ManagerChangesScheduleProcessor processor;

    @Nested
    class Process {

        @Test
        void shouldUpdateTheGivenScheduleStatusToCompleted() throws NATCException {
            testHelpers.seedFirstAndLastNames();

            final Schedule schedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.MANAGER_CHANGES.getValue())
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
        public void shouldCreateNewManagers() throws NATCException {
            testHelpers.seedFirstAndLastNames();

            final Schedule schedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.MANAGER_CHANGES.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            scheduleRepository.save(schedule);

            final List<Team> teamList = Arrays.asList(
                    Team.builder().teamId(1).year("2005").build(),
                    Team.builder().teamId(2).year("2005").build(),
                    Team.builder().teamId(3).year("2005").build()
            );

            teamRepository.saveAll(teamList);

            final List<Manager> managerList = Arrays.asList(
                    Manager.builder().managerId(101).year("2005").teamId(1).seasons(1).build(),
                    Manager.builder().managerId(102).year("2005").teamId(2).seasons(1).build(),
                    Manager.builder().managerId(103).year("2005").teamId(3).seasons(1).build()
            );

            managerRepository.saveAll(managerList);

            processor.process(schedule);

            final List<Manager> finalManagerList = managerRepository.findAll();

            assertEquals(managerList.size() + leagueConfiguration.getNewManagersPerSeason(), finalManagerList.size());
        }

        @Test
        void shouldReplaceReleasedAndRetiredManagers() throws NATCException {
            testHelpers.seedFirstAndLastNames();

            final Schedule schedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.MANAGER_CHANGES.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            scheduleRepository.save(schedule);

            final List<Team> teamList = Arrays.asList(
                    Team.builder().teamId(1).year("2003").playoffRank(0).build(),
                    Team.builder().teamId(1).year("2004").playoffRank(1).build(),
                    Team.builder().teamId(1).year("2005").build(),
                    Team.builder().teamId(2).year("2003").playoffRank(0).wins(45).build(),
                    Team.builder().teamId(2).year("2004").playoffRank(0).wins(43).expectation(0.5).build(),
                    Team.builder().teamId(2).year("2005").build(),
                    Team.builder().teamId(3).year("2003").playoffRank(0).build(),
                    Team.builder().teamId(3).year("2004").playoffRank(1).build(),
                    Team.builder().teamId(3).year("2005").build()
            );

            teamRepository.saveAll(teamList);

            final List<Manager> managerList = Arrays.asList(
                    Manager.builder().managerId(101).year("2005").teamId(1).seasons(4).age(55).vitality(0.2).retired(0).build(),
                    Manager.builder().managerId(102).year("2005").teamId(2).seasons(4).age(52).vitality(0.7).retired(0).score(1).totalSeasons(4).totalScore(1).build(),
                    Manager.builder().managerId(103).year("2005").teamId(3).seasons(4).age(48).vitality(0.8).retired(0).build()
            );

            managerRepository.saveAll(managerList);

            processor.process(schedule);

            final List<Manager> finalManagerList = managerRepository.findAll();

            final Manager retiringManager = finalManagerList.stream().filter(manager -> manager.getManagerId().equals(101)).findFirst().orElseThrow();

            assertEquals(1, retiringManager.getRetired());
            assertNull(retiringManager.getTeamId());
            assertEquals(1, retiringManager.getFormerTeamId());

            final Manager releasedManager = finalManagerList.stream().filter(manager -> manager.getManagerId().equals(102)).findFirst().orElseThrow();

            assertEquals(1, releasedManager.getReleased());
            assertNull(releasedManager.getTeamId());
            assertEquals(2, releasedManager.getFormerTeamId());

            final Manager stayingManager = finalManagerList.stream().filter(manager -> manager.getManagerId().equals(103)).findFirst().orElseThrow();

            assertEquals(3, stayingManager.getTeamId());

            final List<Manager> team1Managers = finalManagerList.stream().filter(manager -> Objects.equals(manager.getTeamId(), 1)).collect(Collectors.toList());

            assertEquals(1, team1Managers.size());
            assertNotEquals(retiringManager.getManagerId(), team1Managers.get(0).getManagerId());

            final List<Manager> team2Managers = finalManagerList.stream().filter(manager -> Objects.equals(manager.getTeamId(), 2)).collect(Collectors.toList());

            assertEquals(1, team2Managers.size());
            assertNotEquals(releasedManager.getManagerId(), team2Managers.get(0).getManagerId());

            final List<Manager> team3Managers = finalManagerList.stream().filter(manager -> Objects.equals(manager.getTeamId(), 3)).collect(Collectors.toList());

            assertEquals(1, team3Managers.size());
            assertEquals(stayingManager.getManagerId(), team3Managers.get(0).getManagerId());
        }
    }
}