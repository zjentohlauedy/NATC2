package org.natc.app.service;

import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.ManagerStyle;
import org.natc.app.entity.domain.Player;
import org.natc.app.entity.domain.Team;
import org.natc.app.exception.LeagueProcessingException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeagueService {

    private final TeamService teamService;
    private final ManagerService managerService;
    private final PlayerService playerService;
    private final LeagueConfiguration leagueConfiguration;
    private final Map<ManagerStyle, Comparator<Player>> managerStyleComparatorMap;

    public LeagueService(
            final TeamService teamService,
            final ManagerService managerService,
            final PlayerService playerService,
            final LeagueConfiguration leagueConfiguration
    ) {
        this.teamService = teamService;
        this.managerService = managerService;
        this.playerService = playerService;
        this.leagueConfiguration = leagueConfiguration;

        this.managerStyleComparatorMap = Map.of(
                ManagerStyle.OFFENSIVE, Comparator.comparing(Player::getOffensiveRating),
                ManagerStyle.DEFENSIVE, Comparator.comparing(Player::getDefensiveRating),
                ManagerStyle.INTANGIBLE, Comparator.comparing(Player::getIntangibleRating),
                ManagerStyle.PENALTIES, Comparator.comparing(Player::getPenaltyRating),
                ManagerStyle.BALANCED, Comparator.comparing(Player::getPerformanceRating)
        );
    }

    public void generateNewLeague() throws LeagueProcessingException {
        final List<Team> teams = teamService.generateTeams();
        final List<Manager> managers = managerService.generateManagers();
        final List<Player> players = playerService.generatePlayers();

        final List<Team> regularTeams = teams.stream().filter(team -> team.getAllstarTeam().equals(0)).collect(Collectors.toList());

        Collections.shuffle(regularTeams);

        assignManagersToTeams(regularTeams, managers);

        for (int i = 0; i < leagueConfiguration.getPlayersPerTeam(); ++i) {
            Collections.reverse(regularTeams);

            assignPlayersToTeams(regularTeams, managers, players);
        }
    }

    private void assignManagersToTeams(final List<Team> teams, final List<Manager> managers) throws LeagueProcessingException {
        for (final Team team : teams) {
            final Optional<Manager> managerOpt = managers.stream()
                    .filter(manager -> manager.getTeamId() == null)
                    .max(Comparator.comparing(Manager::getOverallRating));

            if (managerOpt.isEmpty()) {
                throw new LeagueProcessingException();
            }

            final Manager manager = managerOpt.get();

            manager.setTeamId(team.getTeamId());
            manager.setNewHire(1);

            managerService.updateManager(manager);
        }
    }

    private void assignPlayersToTeams(final List<Team> teams, final List<Manager> managers, final List<Player> players) throws LeagueProcessingException {
        for (final Team team : teams) {
            final Manager teamManager = managers.stream()
                    .filter(manager -> team.getTeamId().equals(manager.getTeamId()))
                    .findFirst().orElseThrow(LeagueProcessingException::new);

            final Optional<Player> playerOpt = players.stream()
                    .filter(player -> player.getTeamId() == null)
                    .max(managerStyleComparatorMap.get(ManagerStyle.getByValue(teamManager.getStyle())));

            if (playerOpt.isEmpty()) {
                throw new LeagueProcessingException();
            }

            final Player player = playerOpt.get();

            player.setTeamId(team.getTeamId());

            playerService.updatePlayer(player);
        }
    }

    public void updateLeagueForNewSeason() {
        throw new UnsupportedOperationException("Method Not Implemented Yet");
    }
}
