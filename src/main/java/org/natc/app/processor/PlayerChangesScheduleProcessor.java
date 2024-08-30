package org.natc.app.processor;

import org.natc.app.comparator.PlayerComparator;
import org.natc.app.comparator.PlayerComparatorFactory;
import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.*;
import org.natc.app.exception.NATCException;
import org.natc.app.proxy.PlayerRetirementProxy;
import org.natc.app.service.ManagerService;
import org.natc.app.service.PlayerService;
import org.natc.app.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.natc.app.entity.domain.PlayerRatingAdjustment.APPLY_AGE;

@Component("player-changes-schedule-processor")
public class PlayerChangesScheduleProcessor implements ScheduleProcessor {

    private final PlayerService playerService;
    private final ManagerService managerService;
    private final ScheduleService scheduleService;
    private final PlayerRetirementProxy playerRetirementProxy;
    private final PlayerComparatorFactory playerComparatorFactory;
    private final LeagueConfiguration leagueConfiguration;

    @Autowired
    public PlayerChangesScheduleProcessor(
            final PlayerService playerService,
            final ManagerService managerService,
            final ScheduleService scheduleService,
            final PlayerRetirementProxy playerRetirementProxy,
            final PlayerComparatorFactory playerComparatorFactory,
            final LeagueConfiguration leagueConfiguration) {
        this.playerService = playerService;
        this.managerService = managerService;
        this.scheduleService = scheduleService;
        this.playerRetirementProxy = playerRetirementProxy;
        this.playerComparatorFactory = playerComparatorFactory;
        this.leagueConfiguration = leagueConfiguration;
    }

    @Override
    public void process(final Schedule schedule) throws NATCException {
        final List<Player> players = playerService.getActivePlayersForYear(schedule.getYear());
        final List<Manager> managers = managerService.getActiveManagersForYear(schedule.getYear());

        for (final Player player : players) {
            if (Objects.isNull(player.getTeamId())) continue;

            if (playerRetirementProxy.readyToRetire(player)) {
                player.setRetired(1);
                player.setFormerTeamId(player.getTeamId());
                player.setTeamId(null);
            }
        }

        final List<Manager> teamManagers = managers.stream()
                .filter(manager -> !Objects.isNull(manager.getTeamId()))
                .collect(Collectors.toList());

        while (checkEachManagerForPlayerChanges(teamManagers, players) > 0);

        for (final Player player : players) {
            if (Objects.nonNull(player.getTeamId()) || Objects.equals(player.getRetired(), 1)) continue;

            if (playerRetirementProxy.shouldRetire(player)) {
                player.setRetired(1);
                player.setReleased(0);
            }
        }

        playerService.updatePlayers(players);

        schedule.setStatus(ScheduleStatus.COMPLETED.getValue());

        scheduleService.updateScheduleEntry(schedule);
    }

    private int checkEachManagerForPlayerChanges(List<Manager> managers, List<Player> players) {
        int playerChangesMade = 0;

        Collections.shuffle(managers);

        for (Manager manager : managers) {
            if (checkForPlayerChange(manager, players)) {
                playerChangesMade++;
            }
        }

        return playerChangesMade;
    }

    private boolean checkForPlayerChange(Manager manager, List<Player> players) {
        final ManagerStyle managerStyle = ManagerStyle.getByValue(manager.getStyle());

        final PlayerComparator playerComparator = playerComparatorFactory.getPlayerComparatorForManager(managerStyle, APPLY_AGE);

        final List<Player> freeAgents = players.stream()
                .filter(player -> Objects.isNull(player.getTeamId()) && !Objects.equals(player.getRetired(), 1))
                .collect(Collectors.toList());

        if (freeAgents.isEmpty()) return false;

        final List<Player> teamPlayers = players.stream()
                .filter(player -> Objects.equals(manager.getTeamId(), player.getTeamId()))
                .collect(Collectors.toList());

        final Player bestFreeAgent = freeAgents.stream()
                .max(playerComparator)
                .orElseThrow();

        if (teamPlayers.size() < leagueConfiguration.getPlayersPerTeam()) {
            signFreeAgentToTeam(bestFreeAgent, manager.getTeamId());

            return true;
        }

        final Player worstTeamPlayer = teamPlayers.stream()
                .min(playerComparator)
                .orElseThrow();

        if (playerComparator.compare(bestFreeAgent, worstTeamPlayer) > 0) {
            signFreeAgentToTeam(bestFreeAgent, manager.getTeamId());
            releasePlayerFromTeam(worstTeamPlayer, manager.getTeamId());

            return true;
        }

        return false;
    }
    
    private void signFreeAgentToTeam(Player player, Integer teamId) {
        player.setTeamId(teamId);
        player.setSigned(1);
        player.setFreeAgent(0);
    }
    
    private void releasePlayerFromTeam(Player player, Integer teamId) {
        player.setTeamId(null);
        player.setReleased(1);
        player.setFreeAgent(1);
        player.setSigned(0);

        if (Objects.isNull(player.getFormerTeamId())) {
            player.setFormerTeamId(teamId);
        }
    }
}
