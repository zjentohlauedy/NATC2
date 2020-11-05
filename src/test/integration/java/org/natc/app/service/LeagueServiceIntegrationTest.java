package org.natc.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.Player;
import org.natc.app.entity.domain.Team;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.ManagerRepository;
import org.natc.app.repository.PlayerRepository;
import org.natc.app.repository.TeamRepository;
import org.natc.app.util.TestHelpers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeagueServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private TestHelpers testHelpers;

    @Autowired
    private LeagueConfiguration leagueConfiguration;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private LeagueService leagueService;

    @BeforeEach
    public void setup() {
        testHelpers.seedFirstAndLastNames();
    }

    @Test
    public void generateNewLeague_ShouldCreateAllTeamsForTheFirstSeason() throws NATCException {
        leagueService.generateNewLeague();

        final List<Team> teams = teamRepository.findAll();

        assertEquals(44, teams.size());
        assertEquals(44, teams.stream().filter(team -> team.getYear().equals(leagueConfiguration.getFirstSeason())).count());
    }

    @Test
    public void generateNewLeague_ShouldCreateInitialManagersForTheFirstSeason() throws NATCException {
        leagueService.generateNewLeague();

        final List<Manager> managers = managerRepository.findAll();

        assertEquals(leagueConfiguration.getInitialManagers(), managers.size());
        assertEquals(leagueConfiguration.getInitialManagers().longValue(), managers.stream()
                .filter(manager -> manager.getYear().equals(leagueConfiguration.getFirstSeason())).count());
    }

    @Test
    public void generateNewLeague_ShouldCreateInitialPlayersForTheFirstSeason() throws NATCException {
        leagueService.generateNewLeague();

        final List<Player> players = playerRepository.findAll();

        assertEquals(leagueConfiguration.getInitialPlayers(), players.size());
        assertEquals(leagueConfiguration.getInitialPlayers().longValue(), players.stream()
                .filter(player -> player.getYear().equals(leagueConfiguration.getFirstSeason())).count());
    }

    @Test
    public void generateNewLeague_ShouldAssignEveryTeamAManager() throws NATCException {
        leagueService.generateNewLeague();

        final List<Team> regularTeams = teamRepository.findAll().stream().filter(team -> team.getAllstarTeam() == 0).collect(Collectors.toList());

        final Integer totalTeams = regularTeams.size();
        final Integer firstTeam = regularTeams.stream().min(Comparator.comparing(Team::getTeamId)).map(Team::getTeamId).orElseThrow();
        final Integer lastTeam = regularTeams.stream().max(Comparator.comparing(Team::getTeamId)).map(Team::getTeamId).orElseThrow();

        final List<Manager> managers = managerRepository.findAll();

        final List<Manager> signedManagers = managers.stream().filter(manager -> manager.getTeamId() != null).collect(Collectors.toList());

        assertEquals(totalTeams, signedManagers.size());
        assertEquals(firstTeam, signedManagers.stream()
                .min(Comparator.comparing(Manager::getTeamId))
                .map(Manager::getTeamId).orElse(0));
        assertEquals(lastTeam, signedManagers.stream()
                .max(Comparator.comparing(Manager::getTeamId))
                .map(Manager::getTeamId).orElse(0));

        final Set<Integer> managerIds = signedManagers.stream().map(Manager::getTeamId).collect(Collectors.toSet());

        assertEquals(totalTeams, managerIds.size());
    }

    @Test
    public void generateNewLeague_ShouldAssignEveryTeamAPlayersPerTeamNumberOfPlayers() throws NATCException {
        leagueService.generateNewLeague();

        final List<Team> regularTeams = teamRepository.findAll().stream().filter(team -> team.getAllstarTeam() == 0).collect(Collectors.toList());

        final Integer totalTeams = regularTeams.size();
        final Integer firstTeam = regularTeams.stream().min(Comparator.comparing(Team::getTeamId)).map(Team::getTeamId).orElseThrow();
        final Integer lastTeam = regularTeams.stream().max(Comparator.comparing(Team::getTeamId)).map(Team::getTeamId).orElseThrow();

        final List<Player> players = playerRepository.findAll();

        final List<Player> signedPlayers = players.stream().filter(player -> player.getTeamId() != null).collect(Collectors.toList());

        assertEquals(totalTeams * leagueConfiguration.getPlayersPerTeam(), signedPlayers.size());

        final Map<Integer, Long> teamPlayerCounts = signedPlayers.stream().collect(Collectors.groupingBy(Player::getTeamId, Collectors.counting()));

        assertEquals(totalTeams, teamPlayerCounts.size());
        assertEquals(firstTeam, teamPlayerCounts.keySet().stream().min(Comparator.comparing(Function.identity())).orElse(0));
        assertEquals(lastTeam, teamPlayerCounts.keySet().stream().max(Comparator.comparing(Function.identity())).orElse(0));

        final Set<Long> counts = new HashSet<>(teamPlayerCounts.values());

        assertEquals(1, counts.size());
        assertEquals(leagueConfiguration.getPlayersPerTeam().longValue(), counts.stream().findFirst().orElse(0L));
    }
}