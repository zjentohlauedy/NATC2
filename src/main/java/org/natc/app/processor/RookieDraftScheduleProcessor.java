package org.natc.app.processor;

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
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component("rookie-draft-schedule-processor")
public class RookieDraftScheduleProcessor implements ScheduleProcessor {

    private final LeagueConfiguration leagueConfiguration;
    private final ManagerService managerService;
    private final PlayerComparatorFactory playerComparatorFactory;
    private final PlayerService playerService;
    private final ScheduleService scheduleService;
    private final TeamComparator teamComparator;
    private final TeamService teamService;

    private final List<ScheduleType> validScheduleTypes = Arrays.asList(
            ScheduleType.ROOKIE_DRAFT_ROUND_1,
            ScheduleType.ROOKIE_DRAFT_ROUND_2
    );

    public RookieDraftScheduleProcessor(
            final LeagueConfiguration leagueConfiguration,
            final ManagerService managerService,
            final PlayerComparatorFactory playerComparatorFactory,
            final PlayerService playerService,
            final ScheduleService scheduleService,
            final TeamComparator teamComparator,
            final TeamService teamService) {
        this.leagueConfiguration = leagueConfiguration;
        this.managerService = managerService;
        this.playerComparatorFactory = playerComparatorFactory;
        this.playerService = playerService;
        this.scheduleService = scheduleService;
        this.teamComparator = teamComparator;
        this.teamService = teamService;
    }

    @Override
    public void process(final Schedule schedule) throws NATCException {
        validateScheduleEntry(schedule);

        final List<Team> teams = retrieveTeams(schedule);
        final Map<Integer, ManagerStyle> managerStyleMap = getManagerStyleMap(schedule);
        final List<Player> rookies = getRookiePlayers(schedule);

        int draftPickNumber = Objects.equals(schedule.getType(), ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue()) ? 1 : leagueConfiguration.getNumberOfTeams() + 1;

        for (final Team team : teams) {
            final PlayerComparator playerComparator = playerComparatorFactory.getPlayerComparatorForManager(managerStyleMap.get(team.getTeamId()));

            final Player bestRookie = rookies.stream()
                    .filter(player -> Objects.isNull(player.getTeamId()))
                    .max(playerComparator)
                    .orElseThrow();

            bestRookie.setTeamId(team.getTeamId());
            bestRookie.setDraftPick(draftPickNumber++);
        }

        playerService.updatePlayers(rookies);

        schedule.setStatus(ScheduleStatus.COMPLETED.getValue());

        scheduleService.updateScheduleEntry(schedule);
    }

    private void validateScheduleEntry(final Schedule schedule) throws ScheduleProcessingException {
        final ScheduleType scheduleType = ScheduleType.getByValue(schedule.getType());

        if (Objects.isNull(scheduleType)) {
            throw new ScheduleProcessingException("Schedule type is required");
        }

        if (!validScheduleTypes.contains(scheduleType)) {
            throw new ScheduleProcessingException(
                    String.format("Invalid Schedule Type [%s] for this processor, valid types: %s",
                            scheduleType.name(),
                            validScheduleTypes
                    )
            );
        }
    }

    private List<Team> retrieveTeams(final Schedule schedule) {
        final String previousYear = String.valueOf(Integer.parseInt(schedule.getYear()) - 1);
        List<Team> teams = teamService.getRegularTeamsByYear(previousYear);

        if (teams.isEmpty()) {
            teams = teamService.getRegularTeamsByYear(schedule.getYear());
            Collections.shuffle(teams);
        }
        else {
            teams = teams.stream().sorted(teamComparator).toList();
        }

        return teams;
    }

    private Map<Integer, ManagerStyle> getManagerStyleMap(final Schedule schedule) {
        final List<Manager> managers = managerService.getActiveManagersForYear(schedule.getYear());

        return managers.stream()
                .filter(manager -> Objects.nonNull(manager.getTeamId()))
                .collect(Collectors.toMap(
                        Manager::getTeamId,
                        manager -> Optional.ofNullable(ManagerStyle.getByValue(manager.getStyle())).orElseThrow()
                ));
    }

    private List<Player> getRookiePlayers(final Schedule schedule) throws NATCException {
        if (Objects.equals(schedule.getType(), ScheduleType.ROOKIE_DRAFT_ROUND_2.getValue())) {
            return playerService.getUndraftedRookiesForYear(schedule.getYear());
        }

        final List<Player> rookies = playerService.generatePlayers(schedule.getYear(), leagueConfiguration.getNewPlayersPerSeason());

        for (final Player player : rookies) {
            player.setRookie(1);
            player.setAge(Player.STARTING_AGE);
        }

        return rookies;
    }
}
