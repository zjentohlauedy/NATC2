package org.natc.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
import org.springframework.data.domain.Example;

import java.util.*;
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
    void setup() {
        testHelpers.seedFirstAndLastNames();
    }

    @Nested
    class GenerateNewLeague {

        @Test
        void shouldCreateAllTeamsForTheFirstSeason() throws NATCException {
            leagueService.generateNewLeague();

            final List<Team> teams = teamRepository.findAll();

            assertEquals(44, teams.size());
            assertEquals(44, teams.stream().filter(team -> team.getYear().equals(leagueConfiguration.getFirstSeason())).count());
        }

        @Test
        void shouldCreateInitialManagersForTheFirstSeason() throws NATCException {
            leagueService.generateNewLeague();

            final List<Manager> managers = managerRepository.findAll();

            assertEquals(leagueConfiguration.getInitialManagers(), managers.size());
            assertEquals(leagueConfiguration.getInitialManagers().longValue(), managers.stream()
                    .filter(manager -> manager.getYear().equals(leagueConfiguration.getFirstSeason())).count());
        }

        @Test
        void shouldCreateInitialPlayersForTheFirstSeason() throws NATCException {
            leagueService.generateNewLeague();

            final List<Player> players = playerRepository.findAll();

            assertEquals(leagueConfiguration.getInitialPlayers(), players.size());
            assertEquals(leagueConfiguration.getInitialPlayers().longValue(), players.stream()
                    .filter(player -> player.getYear().equals(leagueConfiguration.getFirstSeason())).count());
        }

        @Test
        void shouldAssignEveryTeamAManager() throws NATCException {
            leagueService.generateNewLeague();

            final List<Team> regularTeams = teamRepository.findAll().stream().filter(team -> team.getAllstarTeam() == 0).toList();

            final Integer totalTeams = regularTeams.size();
            final Integer firstTeam = regularTeams.stream().min(Comparator.comparing(Team::getTeamId)).map(Team::getTeamId).orElseThrow();
            final Integer lastTeam = regularTeams.stream().max(Comparator.comparing(Team::getTeamId)).map(Team::getTeamId).orElseThrow();

            final List<Manager> managers = managerRepository.findAll();

            final List<Manager> signedManagers = managers.stream().filter(manager -> Objects.nonNull(manager.getTeamId())).toList();

            assertEquals(totalTeams, signedManagers.size());
            assertEquals(firstTeam, signedManagers.stream()
                    .min(Comparator.comparing(Manager::getTeamId))
                    .map(Manager::getTeamId).orElse(0));
            assertEquals(lastTeam, signedManagers.stream()
                    .max(Comparator.comparing(Manager::getTeamId))
                    .map(Manager::getTeamId).orElse(0));

            assertEquals(totalTeams.longValue(), signedManagers.stream().map(Manager::getTeamId).distinct().count());
        }

        @Test
        void shouldAssignEveryTeamAPlayersPerTeamNumberOfPlayers() throws NATCException {
            leagueService.generateNewLeague();

            final List<Team> regularTeams = teamRepository.findAll().stream().filter(team -> team.getAllstarTeam() == 0).toList();

            final Integer totalTeams = regularTeams.size();
            final Integer firstTeam = regularTeams.stream().min(Comparator.comparing(Team::getTeamId)).map(Team::getTeamId).orElseThrow();
            final Integer lastTeam = regularTeams.stream().max(Comparator.comparing(Team::getTeamId)).map(Team::getTeamId).orElseThrow();

            final List<Player> players = playerRepository.findAll();

            final List<Player> signedPlayers = players.stream().filter(player -> Objects.nonNull(player.getTeamId())).toList();

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

    @Nested
    class UpdateLeagueForNewSeason {

        @Test
        void shouldDuplicateTheLeagueStructureFromPreviousYearToNewYear() throws NATCException {
            leagueService.generateNewLeague();

            final List<Team> previousYearTeams = teamRepository.findAll();
            final List<Manager> previousYearManagers = managerRepository.findAll();
            final List<Player> previousYearPlayers = playerRepository.findAll();

            final String newYear = String.valueOf(Integer.parseInt(leagueConfiguration.getFirstSeason()) + 1);

            leagueService.updateLeagueForNewSeason(leagueConfiguration.getFirstSeason(), newYear);

            final List<Team> newTeams = teamRepository.findAll(Example.of(Team.builder().year(newYear).build()));
            final List<Manager> newManagers = managerRepository.findAll(Example.of(Manager.builder().year(newYear).build()));
            final List<Player> newPlayers = playerRepository.findAll(Example.of(Player.builder().year(newYear).build()));

            assertEquals(previousYearTeams.size(), newTeams.size());
            assertEquals(previousYearManagers.size(), newManagers.size());
            assertEquals(previousYearPlayers.size(), newPlayers.size());

            previousYearManagers.sort(Comparator.comparing(Manager::getManagerId));
            newManagers.sort(Comparator.comparing(Manager::getManagerId));

            for (int i = 0; i < previousYearManagers.size(); ++i) {
                assertEquals(previousYearManagers.get(i).getTeamId(), newManagers.get(i).getTeamId());
            }

            previousYearPlayers.sort(Comparator.comparing(Player::getPlayerId));
            newPlayers.sort(Comparator.comparing(Player::getPlayerId));

            for (int i = 0; i < previousYearPlayers.size(); ++i) {
                assertEquals(previousYearPlayers.get(i).getTeamId(), newPlayers.get(i).getTeamId());
            }
        }
    }
}