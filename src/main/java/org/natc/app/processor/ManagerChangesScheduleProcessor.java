package org.natc.app.processor;

import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.*;
import org.natc.app.exception.NATCException;
import org.natc.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.natc.app.processor.ScheduleValidator.validateScheduleEntry;

@Component("manager-changes-schedule-processor")
public class ManagerChangesScheduleProcessor implements ScheduleProcessor {

    private final LeagueConfiguration leagueConfiguration;
    private final PlayerService playerService;
    private final ManagerService managerService;
    private final ScheduleService scheduleService;
    private final TeamService teamService;
    private final TeamManagerDraftService teamManagerDraftService;

    private final List<ScheduleType> validScheduleTypes = Collections.singletonList(ScheduleType.MANAGER_CHANGES);

    @Autowired
    public ManagerChangesScheduleProcessor(
            final LeagueConfiguration leagueConfiguration,
            final PlayerService playerService,
            final ManagerService managerService,
            final ScheduleService scheduleService,
            final TeamService teamService,
            final TeamManagerDraftService teamManagerDraftService) {
        this.leagueConfiguration = leagueConfiguration;
        this.playerService = playerService;
        this.managerService = managerService;
        this.scheduleService = scheduleService;
        this.teamService = teamService;
        this.teamManagerDraftService = teamManagerDraftService;
    }

    @Override
    public void process(final Schedule schedule) throws NATCException {
        validateScheduleEntry(schedule, validScheduleTypes);

        if (schedule.getYear().equals(leagueConfiguration.getFirstSeason())) {
            completeSchedule(schedule);
            return;
        }

        final List<Manager> managerList = new ArrayList<>(managerService.getActiveManagersForYear(schedule.getYear()));

        for (final Manager manager : managerList) {
            manager.setAge(manager.getAge() + 1);

            if (manager.readyToRetire()) {
                manager.setRetired(1);
                manager.setFormerTeamId(manager.getTeamId());
                manager.setTeamId(null);
            }

            if (Objects.nonNull(manager.getTeamId()) && teamService.willTeamReleaseManager(manager)) {
                manager.setReleased(1);
                manager.setFormerTeamId(manager.getTeamId());
                manager.setTeamId(null);
            }
        }

        managerList.addAll(generateNewManagers(schedule.getYear()));

        final List<Team> teamsWithoutManagers = new ArrayList<>();

        for (final Manager manager : managerList) {
            if (Objects.nonNull(manager.getFormerTeamId())) {
                teamsWithoutManagers.add(teamService.getTeamByTeamIdAndYear(manager.getFormerTeamId(), manager.getYear()));
            }
        }

        if (!teamsWithoutManagers.isEmpty()) {
            final List<Manager> availableManagers = managerList.stream()
                    .filter(manager -> !Objects.equals(manager.getRetired(), 1) && Objects.isNull(manager.getTeamId()))
                    .collect(Collectors.toList());

            teamManagerDraftService.assignManagersToTeams(teamsWithoutManagers, availableManagers);
        }

        managerService.updateManagers(managerList);

        completeSchedule(schedule);
    }

    private List<Manager> generateNewManagers(final String year) throws NATCException {
        final Integer newManagerStartingAge = leagueConfiguration.getNewManagerStartingAge();
        final List<Manager> managerList = new ArrayList<>();
        final String playerManagerEligibleSeason = String.valueOf(Integer.parseInt(year) - leagueConfiguration.getPlayerManagerYearsRetired());

        int managersToGenerate = leagueConfiguration.getNewManagersPerSeason();

        for (final Player candidate : playerService.getManagerialCandidates(playerManagerEligibleSeason)) {
            managerList.add(managerService.generateManagerFromPlayer(year, candidate));

            managersToGenerate--;
        }

        for (final Manager manager : managerService.generateManagers(year, managersToGenerate)) {
            manager.setAge(newManagerStartingAge);
            managerList.add(manager);
        }

        return managerList;
    }

    private void completeSchedule(final Schedule schedule) {
        schedule.setStatus(ScheduleStatus.COMPLETED.getValue());

        scheduleService.updateScheduleEntry(schedule);
    }
}
