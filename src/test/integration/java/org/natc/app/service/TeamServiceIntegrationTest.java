package org.natc.app.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.Team;
import org.natc.app.exception.TeamNotFoundException;
import org.natc.app.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeamServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamService teamService;

    @Nested
    class GenerateTeams {

        @Test
        void shouldPersistFourDivisionsOfTenTeamsEachEvenlySplitBetweenTwoConferences() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().allstarTeam(0).build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(40, teamList.size());
            assertEquals(20, teamList.stream().filter(team -> team.getConference().equals(0)).count());
            assertEquals(20, teamList.stream().filter(team -> team.getConference().equals(1)).count());
            assertEquals(10, teamList.stream().filter(team -> team.getDivision().equals(0)).count());
            assertEquals(10, teamList.stream().filter(team -> team.getDivision().equals(1)).count());
            assertEquals(10, teamList.stream().filter(team -> team.getDivision().equals(2)).count());
            assertEquals(10, teamList.stream().filter(team -> team.getDivision().equals(3)).count());
        }

        @Test
        void shouldPersistFourAllstarTeamsWithOneForEachDivision() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().allstarTeam(1).build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(4, teamList.size());
            assertEquals(2, teamList.stream().filter(team -> team.getConference().equals(0)).count());
            assertEquals(2, teamList.stream().filter(team -> team.getConference().equals(1)).count());
            assertEquals(1, teamList.stream().filter(team -> team.getDivision().equals(0)).count());
            assertEquals(1, teamList.stream().filter(team -> team.getDivision().equals(1)).count());
            assertEquals(1, teamList.stream().filter(team -> team.getDivision().equals(2)).count());
            assertEquals(1, teamList.stream().filter(team -> team.getDivision().equals(3)).count());
        }

        @Test
        void shouldCreateTheIndianapolisTitansForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Indianapolis").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Indianapolis", team.getLocation());
            assertEquals("Titans", team.getName());
            assertEquals("IND.", team.getAbbreviation());
            assertEquals("America/Indianapolis", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(0, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheCincinnatiWhirlwindForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Cincinnati").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Cincinnati", team.getLocation());
            assertEquals("Whirlwind", team.getName());
            assertEquals("CIN.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(0, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheKansasCityFlamesForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Kansas City").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Kansas City", team.getLocation());
            assertEquals("Flames", team.getName());
            assertEquals("K.C.", team.getAbbreviation());
            assertEquals("America/Chicago", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(0, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheDallasRustlersForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Dallas").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Dallas", team.getLocation());
            assertEquals("Rustlers", team.getName());
            assertEquals("DAL.", team.getAbbreviation());
            assertEquals("America/Chicago", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(0, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheWashingtonOlympiansForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Washington").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Washington", team.getLocation());
            assertEquals("Olympians", team.getName());
            assertEquals("WAS.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(0, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheMinneapolisMaraudersForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Minneapolis").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Minneapolis", team.getLocation());
            assertEquals("Marauders", team.getName());
            assertEquals("MIN.", team.getAbbreviation());
            assertEquals("America/Chicago", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(0, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheNewOrleansTigersharksForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("New Orleans").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("New Orleans", team.getLocation());
            assertEquals("Tigersharks", team.getName());
            assertEquals("N.O.", team.getAbbreviation());
            assertEquals("America/Chicago", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(0, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheOaklandAcesForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Oakland").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Oakland", team.getLocation());
            assertEquals("Aces", team.getName());
            assertEquals("OAK.", team.getAbbreviation());
            assertEquals("America/Los_Angeles", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(0, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheVancouverCometsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Vancouver").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Vancouver", team.getLocation());
            assertEquals("Comets", team.getName());
            assertEquals("VAN.", team.getAbbreviation());
            assertEquals("America/Vancouver", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(0, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheSaltLakeCityLightningForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Salt Lake City").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Salt Lake City", team.getLocation());
            assertEquals("Lightning", team.getName());
            assertEquals("SLC.", team.getAbbreviation());
            assertEquals("America/Phoenix", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(0, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheBostonBlacksForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Boston").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Boston", team.getLocation());
            assertEquals("Blacks", team.getName());
            assertEquals("BOS.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(1, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateThePittsburghPirahnasForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Pittsburgh").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Pittsburgh", team.getLocation());
            assertEquals("Pirahnas", team.getName());
            assertEquals("PIT.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(1, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheSanDiegoStingraysForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("San Diego").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("San Diego", team.getLocation());
            assertEquals("Stingrays", team.getName());
            assertEquals("S.D.", team.getAbbreviation());
            assertEquals("America/Los_Angeles", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(1, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateThePhiladelphiaPhotonsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Philadelphia").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Philadelphia", team.getLocation());
            assertEquals("Photons", team.getName());
            assertEquals("PHI.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(1, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheDetroitThunderForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Detroit").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Detroit", team.getLocation());
            assertEquals("Thunder", team.getName());
            assertEquals("DET.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(1, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheAtlantaRenegadesForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Atlanta").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Atlanta", team.getLocation());
            assertEquals("Renegades", team.getName());
            assertEquals("ATL.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(1, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheBaltimoreCrabbersForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Baltimore").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Baltimore", team.getLocation());
            assertEquals("Crabbers", team.getName());
            assertEquals("BAL.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(1, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheStLouisJuggernautsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("St. Louis").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("St. Louis", team.getLocation());
            assertEquals("Juggernauts", team.getName());
            assertEquals("S.L.", team.getAbbreviation());
            assertEquals("America/Chicago", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(1, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheOrlandoHurricanesForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Orlando").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Orlando", team.getLocation());
            assertEquals("Hurricanes", team.getName());
            assertEquals("ORL.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(1, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheLasVegasVampiresForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Las Vegas").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Las Vegas", team.getLocation());
            assertEquals("Vampires", team.getName());
            assertEquals("L.V.", team.getAbbreviation());
            assertEquals("America/Los_Angeles", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(1, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheMiamiVoyagersForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Miami").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Miami", team.getLocation());
            assertEquals("Voyagers", team.getName());
            assertEquals("MIA.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(2, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheHoustonHammersForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Houston").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Houston", team.getLocation());
            assertEquals("Hammers", team.getName());
            assertEquals("HOU.", team.getAbbreviation());
            assertEquals("America/Chicago", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(2, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheLosAngelesLegendsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Los Angeles").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Los Angeles", team.getLocation());
            assertEquals("Legends", team.getName());
            assertEquals("L.A.", team.getAbbreviation());
            assertEquals("America/Los_Angeles", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(2, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheNewYorkKnightsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("New York").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("New York", team.getLocation());
            assertEquals("Knights", team.getName());
            assertEquals("N.Y.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(2, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheChicagoGoblinsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Chicago").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Chicago", team.getLocation());
            assertEquals("Goblins", team.getName());
            assertEquals("CHI.", team.getAbbreviation());
            assertEquals("America/Chicago", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(2, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheTampaBayTerrorForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Tampa Bay").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Tampa Bay", team.getLocation());
            assertEquals("Terror", team.getName());
            assertEquals("T.B.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(2, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheMontrealDragonsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Montreal").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Montreal", team.getLocation());
            assertEquals("Dragons", team.getName());
            assertEquals("MON.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(2, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheNewJerseyPhantomsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("New Jersey").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("New Jersey", team.getLocation());
            assertEquals("Phantoms", team.getName());
            assertEquals("N.J.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(2, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheMexicoCityAztecsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Mexico City").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Mexico City", team.getLocation());
            assertEquals("Aztecs", team.getName());
            assertEquals("MEX.", team.getAbbreviation());
            assertEquals("America/Mexico_City", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(2, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheBuffaloIcersForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Buffalo").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Buffalo", team.getLocation());
            assertEquals("Icers", team.getName());
            assertEquals("BUF.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(3, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheClevelandScorpionsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Cleveland").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Cleveland", team.getLocation());
            assertEquals("Scorpions", team.getName());
            assertEquals("CLE.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(3, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheDenverNukesForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Denver").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Denver", team.getLocation());
            assertEquals("Nukes", team.getName());
            assertEquals("DEN.", team.getAbbreviation());
            assertEquals("America/Denver", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(3, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheSeattlePsychoticsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Seattle").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Seattle", team.getLocation());
            assertEquals("Psychotics", team.getName());
            assertEquals("SEA.", team.getAbbreviation());
            assertEquals("America/Los_Angeles", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(3, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateThePhoenixEclipseForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Phoenix").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Phoenix", team.getLocation());
            assertEquals("Eclipse", team.getName());
            assertEquals("PHX.", team.getAbbreviation());
            assertEquals("America/Phoenix", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(3, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheMilwaukeeWarriorsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Milwaukee").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Milwaukee", team.getLocation());
            assertEquals("Warriors", team.getName());
            assertEquals("MIL.", team.getAbbreviation());
            assertEquals("America/Chicago", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(3, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheKingstonOutlawsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Kingston").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Kingston", team.getLocation());
            assertEquals("Outlaws", team.getName());
            assertEquals("KIN.", team.getAbbreviation());
            assertEquals("America/Jamaica", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(3, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheTorontoOverlordsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Toronto").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Toronto", team.getLocation());
            assertEquals("Overlords", team.getName());
            assertEquals("TOR.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(3, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheCharlotteSerpentsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Charlotte").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Charlotte", team.getLocation());
            assertEquals("Serpents", team.getName());
            assertEquals("CHA.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(3, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateThePortlandRhinosForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Portland").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Portland", team.getLocation());
            assertEquals("Rhinos", team.getName());
            assertEquals("POR.", team.getAbbreviation());
            assertEquals("America/Los_Angeles", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(3, team.getDivision());
            assertEquals(0, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheGreeneDivisionAllstarsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Greene").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Greene", team.getLocation());
            assertEquals("All Stars", team.getName());
            assertEquals("GRN.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(0, team.getDivision());
            assertEquals(1, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheDavisDivisionAllstarsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Davis").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Davis", team.getLocation());
            assertEquals("All Stars", team.getName());
            assertEquals("DVS.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(0, team.getConference());
            assertEquals(1, team.getDivision());
            assertEquals(1, team.getAllstarTeam());
        }

        @Test
        void shouldCreateTheLawrenceDivisionAllstarsForTheGivenYear() {
            teamService.generateTeams("2001");

            final Example<Team> queryCriteria = Example.of(Team.builder().location("Lawrence").build());
            final List<Team> teamList = teamRepository.findAll(queryCriteria);

            assertEquals(1, teamList.size());

            final Team team = teamList.getFirst();

            assertEquals("Lawrence", team.getLocation());
            assertEquals("All Stars", team.getName());
            assertEquals("LAW.", team.getAbbreviation());
            assertEquals("America/New_York", team.getTimeZone());
            assertEquals("2001", team.getYear());
            assertEquals(1, team.getConference());
            assertEquals(3, team.getDivision());
            assertEquals(1, team.getAllstarTeam());
        }
    }

    @Nested
    class UpdateTeamsForNewSeason {

        @Test
        void shouldCopyTeamRecordsFromOneYearToAnother() {
            final List<Team> teamList = Arrays.asList(
                    Team.builder().teamId(1).year("2002").build(),
                    Team.builder().teamId(2).year("2002").build(),
                    Team.builder().teamId(3).year("2002").build()
            );

            teamRepository.saveAll(teamList);

            teamService.updateTeamsForNewSeason("2002", "2020");

            final Example<Team> queryCriteria = Example.of(Team.builder().year("2020").build());
            final List<Team> copiedTeams = teamRepository.findAll(queryCriteria);

            assertEquals(teamList.size(), copiedTeams.size());
        }

        @Test
        void shouldOnlyCopyTeamRecordsFromPreviousYear() {
            final List<Team> teamList = Arrays.asList(
                    Team.builder().teamId(1).year("2001").build(),
                    Team.builder().teamId(2).year("2002").build(),
                    Team.builder().teamId(3).year("2003").build()
            );

            teamRepository.saveAll(teamList);

            teamService.updateTeamsForNewSeason("2002", "2020");

            final Example<Team> queryCriteria = Example.of(Team.builder().year("2020").build());
            final List<Team> copiedTeams = teamRepository.findAll(queryCriteria);

            assertEquals(1, copiedTeams.size());
            assertEquals(2, copiedTeams.getFirst().getTeamId());
        }

        @Test
        void shouldOnlyCopyNecessaryFieldsToNewYear() {
            final Team originalTeam = Team.builder()
                    .teamId(123)
                    .year("1999")
                    .location("Here")
                    .name("Them")
                    .abbreviation("ABC.")
                    .timeZone("Americas/Los_Angeles")
                    .gameTime(999)
                    .conference(111)
                    .division(222)
                    .allstarTeam(1)
                    .preseasonGames(12)
                    .preseasonWins(7)
                    .preseasonLosses(5)
                    .games(99)
                    .wins(55)
                    .losses(44)
                    .divisionWins(33)
                    .divisionLosses(22)
                    .outOfConferenceWins(66)
                    .outOfConferenceLosses(11)
                    .overtimeWins(9)
                    .overtimeLosses(3)
                    .roadWins(88)
                    .roadLosses(77)
                    .homeWins(35)
                    .homeLosses(46)
                    .divisionRank(6)
                    .playoffRank(15)
                    .playoffGames(14)
                    .round1Wins(13)
                    .round2Wins(6)
                    .round3Wins(4)
                    .expectation(0.432)
                    .drought(17)
                    .build();

            teamRepository.save(originalTeam);

            teamService.updateTeamsForNewSeason("1999", "2000");

            final Example<Team> queryCriteria = Example.of(Team.builder().year("2000").build());
            final List<Team> copiedTeams = teamRepository.findAll(queryCriteria);

            assertEquals(1, copiedTeams.size());

            final Team copiedTeam = copiedTeams.getFirst();

            assertEquals(originalTeam.getTeamId(), copiedTeam.getTeamId());
            assertEquals(originalTeam.getLocation(), copiedTeam.getLocation());
            assertEquals(originalTeam.getName(), copiedTeam.getName());
            assertEquals(originalTeam.getAbbreviation(), copiedTeam.getAbbreviation());
            assertEquals(originalTeam.getTimeZone(), copiedTeam.getTimeZone());
            assertEquals(originalTeam.getGameTime(), copiedTeam.getGameTime());
            assertEquals(originalTeam.getConference(), copiedTeam.getConference());
            assertEquals(originalTeam.getDivision(), copiedTeam.getDivision());
            assertEquals(originalTeam.getAllstarTeam(), copiedTeam.getAllstarTeam());
            assertEquals(originalTeam.getExpectation(), copiedTeam.getExpectation());
            assertEquals(originalTeam.getDrought(), copiedTeam.getDrought());

            assertNull(copiedTeam.getPreseasonGames());
            assertNull(copiedTeam.getPreseasonWins());
            assertNull(copiedTeam.getPreseasonLosses());
            assertNull(copiedTeam.getGames());
            assertNull(copiedTeam.getWins());
            assertNull(copiedTeam.getLosses());
            assertNull(copiedTeam.getDivisionWins());
            assertNull(copiedTeam.getDivisionLosses());
            assertNull(copiedTeam.getOutOfConferenceWins());
            assertNull(copiedTeam.getOutOfConferenceLosses());
            assertNull(copiedTeam.getOvertimeWins());
            assertNull(copiedTeam.getOvertimeLosses());
            assertNull(copiedTeam.getRoadWins());
            assertNull(copiedTeam.getRoadLosses());
            assertNull(copiedTeam.getHomeWins());
            assertNull(copiedTeam.getHomeLosses());
            assertNull(copiedTeam.getDivisionRank());
            assertNull(copiedTeam.getPlayoffRank());
            assertNull(copiedTeam.getPlayoffGames());
            assertNull(copiedTeam.getRound1Wins());
            assertNull(copiedTeam.getRound2Wins());
            assertNull(copiedTeam.getRound3Wins());
        }
    }

    @Nested
    class WillTeamReleaseManager {

        @Test
        void shouldReturnFalseIfPreviousSeasonTeamIsDoesNotExist() {
            final Team team = Team.builder().teamId(1).year("2000").build();
            final Manager manager = Manager.builder().managerId(1).year("2000").seasons(5).build();

            teamRepository.save(team);

            assertFalse(teamService.willTeamReleaseManager(manager));
        }

        @Test
        void shouldReturnFalseIfTwoYearsBackSeasonTeamIsDoesNotExist() {
            final Manager manager = Manager.builder().managerId(1).year("2000").seasons(5).build();
            final List<Team> teamList = Arrays.asList(
                    Team.builder().teamId(1).year("1999").build(),
                    Team.builder().teamId(2).year("2000").build()
            );

            teamRepository.saveAll(teamList);

            assertFalse(teamService.willTeamReleaseManager(manager));
        }

        @Test
        void shouldReturnTrueIfBothYearsTeamsExistAndTheManagerFailsOnCriteria() {
            final Manager manager = Manager.builder().managerId(1).year("2000").seasons(5).totalSeasons(5).score(0).totalScore(0).build();
            final List<Team> teamList = Arrays.asList(
                    Team.builder().teamId(1).wins(55).playoffRank(1).expectation(0.6).year("1998").build(),
                    Team.builder().teamId(2).wins(55).playoffRank(1).expectation(0.6).year("1999").build(),
                    Team.builder().teamId(3).wins(55).playoffRank(1).expectation(0.6).year("2000").build()
            );

            teamRepository.saveAll(teamList);

            assertTrue(teamService.willTeamReleaseManager(manager));
        }
    }

    @Nested
    class GetTeamByTeamIdAndYear {

        @Test
        void shouldReturnTheMatchingTeam() throws TeamNotFoundException {
            final Team expectedTeam = Team.builder().teamId(123).year("2012").build();

            teamRepository.save(expectedTeam);

            final Team actualTeam = teamService.getTeamByTeamIdAndYear(expectedTeam.getTeamId(), expectedTeam.getYear());

            assertEquals(expectedTeam.getTeamId(), actualTeam.getTeamId());
            assertEquals(expectedTeam.getYear(), actualTeam.getYear());
        }

        @Test
        void shouldReturnTheOnlyMatchingTeam() throws TeamNotFoundException {
            final List<Team> teamList = Arrays.asList(
                    Team.builder().teamId(1).year("2000").build(),
                    Team.builder().teamId(2).year("1999").build(),
                    Team.builder().teamId(2).year("2000").build(),
                    Team.builder().teamId(2).year("2001").build(),
                    Team.builder().teamId(3).year("2000").build()
            );

            teamRepository.saveAll(teamList);

            final Team actualTeam = teamService.getTeamByTeamIdAndYear(2, "2000");

            assertEquals(2, actualTeam.getTeamId());
            assertEquals("2000", actualTeam.getYear());
        }

        @Test
        void shouldThrowATeamNotFoundExceptionIfMatchingTeamDoesntExist() throws TeamNotFoundException {
            final List<Team> teamList = Arrays.asList(
                    Team.builder().teamId(1).year("2000").build(),
                    Team.builder().teamId(2).year("1999").build(),
                    Team.builder().teamId(2).year("2000").build(),
                    Team.builder().teamId(2).year("2001").build(),
                    Team.builder().teamId(3).year("2000").build()
            );

            teamRepository.saveAll(teamList);

            assertThrows(TeamNotFoundException.class, () -> teamService.getTeamByTeamIdAndYear(5, "2005"));
        }
    }

    @Nested
    class GetRegularTeamsByYear {
        @Test
        void shouldReturnRegularTeamsForTheGivenYear() {
            final List<Team> teamList = Arrays.asList(
                    Team.builder().teamId(1).year("2018").allstarTeam(0).build(),
                    Team.builder().teamId(2).year("2018").allstarTeam(0).build(),
                    Team.builder().teamId(3).year("2018").allstarTeam(0).build()
            );

            teamRepository.saveAll(teamList);

            final List<Team> actualTeamList = teamService.getRegularTeamsByYear("2018");

            assertEquals(3, actualTeamList.size());
        }

        @Test
        void shouldOnlyReturnRegularTeamsForTheGivenYear() {
            final List<Team> teamList = Arrays.asList(
                    Team.builder().teamId(1).year("2018").allstarTeam(0).build(),
                    Team.builder().teamId(2).year("2016").allstarTeam(0).build(),
                    Team.builder().teamId(3).year("2018").allstarTeam(0).build(),
                    Team.builder().teamId(4).year("2018").allstarTeam(1).build(),
                    Team.builder().teamId(5).year("2018").allstarTeam(0).build()
            );

            teamRepository.saveAll(teamList);

            final List<Team> actualTeamList = teamService.getRegularTeamsByYear("2018");

            assertEquals(3, actualTeamList.size());
            assertEquals(3, actualTeamList.stream().filter(t -> t.getYear().equals("2018")).count());
            assertEquals(3, actualTeamList.stream().filter(t -> t.getAllstarTeam().equals(0)).count());
        }

        @Test
        void shouldReturnAnEmptyListWhenNoTeamsMatch() {
            final List<Team> teamList = Arrays.asList(
                    Team.builder().teamId(2).year("2016").allstarTeam(0).build(),
                    Team.builder().teamId(4).year("2018").allstarTeam(1).build()
            );

            teamRepository.saveAll(teamList);

            final List<Team> actualTeamList = teamService.getRegularTeamsByYear("2018");

            assertEquals(0, actualTeamList.size());
            assertTrue(actualTeamList.isEmpty());
        }
    }
}