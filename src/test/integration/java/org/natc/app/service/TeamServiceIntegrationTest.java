package org.natc.app.service;

import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.Team;
import org.natc.app.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TeamServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamService teamService;

    @Test
    public void generateTeams_ShouldPersistFourDivisionsOfTenTeamsEachEvenlySplitBetweenTwoConferences() {
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
    public void generateTeams_ShouldPersistFourAllstarTeamsWithOneForEachDivision() {
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
    public void generateTeams_ShouldCreateTheIndianapolisTitansForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Indianapolis").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheCincinnatiWhirlwindForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Cincinnati").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheKansasCityFlamesForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Kansas City").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheDallasRustlersForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Dallas").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheWashingtonOlympiansForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Washington").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheMinneapolisMaraudersForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Minneapolis").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheNewOrleansTigersharksForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("New Orleans").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheOaklandAcesForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Oakland").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheVancouverCometsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Vancouver").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheSaltLakeCityLightningForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Salt Lake City").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheBostonBlacksForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Boston").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateThePittsburghPirahnasForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Pittsburgh").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheSanDiegoStingraysForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("San Diego").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateThePhiladelphiaPhotonsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Philadelphia").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheDetroitThunderForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Detroit").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheAtlantaRenegadesForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Atlanta").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheBaltimoreCrabbersForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Baltimore").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheStLouisJuggernautsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("St. Louis").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheOrlandoHurricanesForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Orlando").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheLasVegasVampiresForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Las Vegas").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheMiamiVoyagersForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Miami").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheHoustonHammersForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Houston").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheLosAngelesLegendsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Los Angeles").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheNewYorkKnightsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("New York").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheChicagoGoblinsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Chicago").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheTampaBayTerrorForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Tampa Bay").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheMontrealDragonsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Montreal").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheNewJerseyPhantomsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("New Jersey").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheMexicoCityAztecsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Mexico City").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheBuffaloIcersForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Buffalo").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheClevelandScorpionsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Cleveland").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheDenverNukesForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Denver").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheSeattlePsychoticsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Seattle").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateThePhoenixEclipseForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Phoenix").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheMilwaukeeWarriorsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Milwaukee").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheKingstonOutlawsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Kingston").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheTorontoOverlordsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Toronto").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheCharlotteSerpentsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Charlotte").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateThePortlandRhinosForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Portland").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheGreeneDivisionAllstarsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Greene").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheDavisDivisionAllstarsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Davis").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

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
    public void generateTeams_ShouldCreateTheLawrenceDivisionAllstarsForTheGivenYear() {
        teamService.generateTeams("2001");

        final Example<Team> queryCriteria = Example.of(Team.builder().location("Lawrence").build());
        final List<Team> teamList = teamRepository.findAll(queryCriteria);

        assertEquals(1, teamList.size());

        final Team team = teamList.get(0);

        assertEquals("Lawrence", team.getLocation());
        assertEquals("All Stars", team.getName());
        assertEquals("LAW.", team.getAbbreviation());
        assertEquals("America/New_York", team.getTimeZone());
        assertEquals("2001", team.getYear());
        assertEquals(1, team.getConference());
        assertEquals(3, team.getDivision());
        assertEquals(1, team.getAllstarTeam());
    }

    @Test
    public void updateTeamsForNewSeason_ShouldCopyTeamRecordsFromOneYearToAnother() {
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
    public void updateTeamsForNewSeason_ShouldOnlyCopyTeamRecordsFromPreviousYear() {
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
        assertEquals(2, copiedTeams.get(0).getTeamId());
    }

    @Test
    public void updateTeamsForNewSeason_ShouldOnlyCopyNecessaryFieldsToNewYear() {
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

        final Team copiedTeam = copiedTeams.get(0);

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