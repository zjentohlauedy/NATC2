package org.natc.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.Team;
import org.natc.app.repository.TeamRepository;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Captor
    private ArgumentCaptor<Example<Team>> captor;

    @Mock
    private TeamRepository teamRepository;
    
    @InjectMocks
    private TeamService teamService;
    
    @Test
    public void generateTeams_ShouldReturnAListOfTeamsGenerated() {
        final List<Team> teamList = teamService.generateTeams(null);

        assertFalse(teamList.isEmpty());
    }

    @Test
    public void generateTeams_ShouldGenerateFourtyFourTeams() {
        final List<Team> teamList = teamService.generateTeams(null);

        assertEquals(44, teamList.size());
    }

    @Test
    public void generateTeams_ShouldGenerateFourtyRegularTeams() {
        final List<Team> teamList = teamService.generateTeams(null);

        assertEquals(40, teamList.stream().filter(team -> team.getAllstarTeam().equals(0)).count());
    }

    @Test
    public void generateTeams_ShouldGenerateFourAllstarTeams() {
        final List<Team> teamList = teamService.generateTeams(null);

        assertEquals(4, teamList.stream().filter(team -> team.getAllstarTeam().equals(1)).count());
    }

    @Test
    public void generateTeams_ShouldGenerateTwentyTwoTeamsPerConference() {
        final List<Team> teamList = teamService.generateTeams(null);

        assertEquals(22, teamList.stream().filter(team -> team.getConference().equals(0)).count());
        assertEquals(22, teamList.stream().filter(team -> team.getConference().equals(1)).count());
    }

    @Test
    public void generateTeams_ShouldGenerateElevenTeamsPerDivision() {
        final List<Team> teamList = teamService.generateTeams(null);

        assertEquals(11, teamList.stream().filter(team -> team.getDivision().equals(0)).count());
        assertEquals(11, teamList.stream().filter(team -> team.getDivision().equals(1)).count());
        assertEquals(11, teamList.stream().filter(team -> team.getDivision().equals(2)).count());
        assertEquals(11, teamList.stream().filter(team -> team.getDivision().equals(3)).count());
    }

    @Test
    public void generateTeams_ShouldGenerateTeamsWithUniqueTeamIdsFromOneToFourtyFour() {
        final List<Team> teamList = teamService.generateTeams(null);

        final Set<Integer> teamIds = teamList.stream().map(Team::getTeamId).collect(Collectors.toSet());

        assertEquals(44, teamIds.size());
        assertEquals(1, teamIds.stream().mapToInt(v -> v).min().orElse(0));
        assertEquals(44, teamIds.stream().mapToInt(v -> v).max().orElse(0));
    }

    @Test
    public void generateTeams_ShouldGenerateTeamsForTheGivenYear() {
        final String expectedYear = "2020";

        final List<Team> teamList = teamService.generateTeams(expectedYear);

        assertEquals(44, teamList.stream().filter(team -> team.getYear().equals(expectedYear)).count());
    }

    @Test
    public void generateTeams_ShouldGenerateRegularTeamsAllWithDifferentLocations() {
        final List<Team> teamList = teamService.generateTeams(null);

        final Set<String> locations = teamList.stream()
                .filter(team -> team.getAllstarTeam().equals(0))
                .map(Team::getLocation)
                .collect(Collectors.toSet());

        assertEquals(40, locations.size());
    }

    @Test
    public void generateTeams_ShouldGenerateRegularTeamsAllWithDifferentNames() {
        final List<Team> teamList = teamService.generateTeams(null);

        final Set<String> names = teamList.stream()
                .filter(team -> team.getAllstarTeam().equals(0))
                .map(Team::getName)
                .collect(Collectors.toSet());

        assertEquals(40, names.size());
    }

    @Test
    public void generateTeams_ShouldGenerateRegularTeamsAllWithDifferentAbbreviations() {
        final List<Team> teamList = teamService.generateTeams(null);

        final Set<String> abbreviations = teamList.stream()
                .filter(team -> team.getAllstarTeam().equals(0))
                .map(Team::getAbbreviation)
                .collect(Collectors.toSet());

        assertEquals(40, abbreviations.size());
    }

    @Test
    public void generateTeams_ShouldGenerateRegularTeamsAllWithATimeZone() {
        final List<Team> teamList = teamService.generateTeams(null);

        assertEquals(40, teamList.stream()
                .filter(team -> team.getAllstarTeam().equals(0) && !team.getTimeZone().isBlank()).count());
    }

    @Test
    public void generateTeams_ShouldGenerateRegularTeamsAllWithAGameTimeOf965() {
        final List<Team> teamList = teamService.generateTeams(null);

        assertEquals(40, teamList.stream()
                .filter(team -> team.getAllstarTeam().equals(0) && team.getGameTime().equals(965)).count());
    }

    @Test
    public void generateTeams_ShouldGenerateAllstarTeamsWithLocationAsDivisionNames() {
        final List<Team> teamList = teamService.generateTeams(null);

        final Set<String> locations = teamList.stream()
                .filter(team -> team.getAllstarTeam().equals(1))
                .map(Team::getLocation)
                .collect(Collectors.toSet());

        final List<String> expectedLocations = Stream.of("Greene", "Davis", "Smith", "Lawrence")
                .sorted().collect(Collectors.toList());

        assertEquals(4, locations.size());
        assertEquals(expectedLocations, locations.stream().sorted().collect(Collectors.toList()));
    }

    @Test
    public void generateTeams_ShouldGenerateAllstarTeamsWithNameAsAllStars() {
        final List<Team> teamList = teamService.generateTeams(null);

        final Set<String> name = teamList.stream()
                .filter(team -> team.getAllstarTeam().equals(1))
                .map(Team::getName)
                .collect(Collectors.toSet());

        assertEquals(1, name.size());
        assertEquals("All Stars", name.toArray()[0]);
    }

    @Test
    public void generateTeams_ShouldGenerateAllstarTeamsWithAbbreviationsBasedOnDivisionNames() {
        final List<Team> teamList = teamService.generateTeams(null);

        final Set<String> abbreviations = teamList.stream()
                .filter(team -> team.getAllstarTeam().equals(1))
                .map(Team::getAbbreviation)
                .collect(Collectors.toSet());

        final List<String> expectedLocations = Stream.of("GRN.", "DVS.", "SMI.", "LAW.")
                .sorted().collect(Collectors.toList());

        assertEquals(4, abbreviations.size());
        assertEquals(expectedLocations, abbreviations.stream().sorted().collect(Collectors.toList()));
    }

    @Test
    public void generateTeams_ShouldGenerateAllstarTeamsWithTimeZoneAsNewYork() {
        final List<Team> teamList = teamService.generateTeams(null);

        final Set<String> timeZones = teamList.stream()
                .filter(team -> team.getAllstarTeam().equals(1))
                .map(Team::getTimeZone)
                .collect(Collectors.toSet());

        assertEquals(1, timeZones.size());
        assertEquals("America/New_York", timeZones.toArray()[0]);
    }

    @Test
    public void generateTeams_ShouldGenerateAllstarTeamsAllWithAGameTimeOf965() {
        final List<Team> teamList = teamService.generateTeams(null);

        assertEquals(4, teamList.stream()
                .filter(team -> team.getAllstarTeam().equals(1) && team.getGameTime().equals(965)).count());
    }
    
    @Test
    public void generateTeams_ShouldCallTeamRepositoryToSaveTheTeams() {
        final List<Team> teamList = teamService.generateTeams(null);

        verify(teamRepository).saveAll(teamList);
    }

    @Test
    public void updateTeamsForNewSeason_ShouldCallTheRepositoryToCopyTeamsForNewYear() {
        teamService.updateTeamsForNewSeason(null, null);

        verify(teamRepository).copyTeamsForNewYear(any(), any());
    }

    @Test
    public void updateTeamsForNewSeason_ShouldPassThePreviousYearAndNewYearToTheRepository() {
        teamService.updateTeamsForNewSeason("2008", "2009");

        verify(teamRepository).copyTeamsForNewYear("2008", "2009");
    }

    @Test
    public void willTeamReleaseManager_ShouldReturnFalseIfManagerHasLessThanThreeSeasons() {
        final Manager manager = Manager.builder().managerId(1).year("2002").seasons(0).build();

        assertFalse(teamService.willTeamReleaseManager(manager));
    }

    @Test
    public void willTeamReleaseManager_ShouldCallTeamRepositoryToGetManagersTeamsTwoPreviousYearRecords() {
        final Manager manager = Manager.builder().managerId(1).year("2002").teamId(5).seasons(4).build();

        teamService.willTeamReleaseManager(manager);

        verify(teamRepository, times(2)).findOne(captor.capture());

        final Team firstTeam = captor.getAllValues().get(0).getProbe();
        final Team secondTeam = captor.getAllValues().get(1).getProbe();

        assertEquals(manager.getTeamId(), firstTeam.getTeamId());
        assertEquals("2001", firstTeam.getYear());

        assertEquals(manager.getTeamId(), secondTeam.getTeamId());
        assertEquals("2000", secondTeam.getYear());
    }

    @Test
    public void willTeamReleaseManager_ShouldNotCallRepositoryIfManagerHasLessThanThreeSeasons() {
        final Manager manager = Manager.builder().managerId(1).year("2002").seasons(2).build();

        teamService.willTeamReleaseManager(manager);

        verify(teamRepository, never()).findOne(any());
    }

    @Test
    public void willTeamReleaseManager_ShouldReturnFalseIfPreviousYearsTeamRecordIsNotFound() {
        final Manager manager = Manager.builder().managerId(1).year("2002").teamId(5).seasons(4).build();

        when(teamRepository.findOne(any())).thenReturn(Optional.empty());

        assertFalse(teamService.willTeamReleaseManager(manager));
    }

    @Test
    public void willTeamReleaseManager_ShouldReturnFalseIfTwoYearsBackTeamRecordIsNotFound() {
        final Manager manager = Manager.builder().managerId(1).year("2002").teamId(5).seasons(4).build();

        when(teamRepository.findOne(any()))
                .thenReturn(Optional.of(Team.builder().build()))
                .thenReturn(Optional.empty());

        assertFalse(teamService.willTeamReleaseManager(manager));
    }

    @Test
    public void willTeamReleaseManager_ShouldReturnFalseIfPlayoffRankImprovedBetweenYears() {
        final Manager manager = Manager.builder().managerId(1).year("2002").teamId(5).seasons(4).build();

        when(teamRepository.findOne(any()))
                .thenReturn(Optional.of(Team.builder().playoffRank(3).build()))
                .thenReturn(Optional.of(Team.builder().playoffRank(2).build()));

        assertFalse(teamService.willTeamReleaseManager(manager));
    }

    @Test
    public void willTeamReleaseManager_ShouldReturnFalseIfWinsImprovedBetweenYears() {
        final Manager manager = Manager.builder().managerId(1).year("2002").teamId(5).seasons(4).build();

        when(teamRepository.findOne(any()))
                .thenReturn(Optional.of(Team.builder().wins(65).playoffRank(0).build()))
                .thenReturn(Optional.of(Team.builder().wins(55).playoffRank(0).build()));

        assertFalse(teamService.willTeamReleaseManager(manager));
    }

    @Test
    public void willTeamReleaseManager_ShouldReturnFalseIfManagerPerformanceRatingIsGreaterThanExpectation() {
        final Manager manager = Manager.builder()
                .managerId(1)
                .year("2002")
                .teamId(5)
                .seasons(4)
                .score(4)
                .totalSeasons(4)
                .totalScore(4)
                .build();

        when(teamRepository.findOne(any()))
                .thenReturn(Optional.of(Team.builder().wins(55).playoffRank(0).expectation(0.95).build()))
                .thenReturn(Optional.of(Team.builder().wins(55).playoffRank(0).expectation(0.95).build()));

        assertFalse(teamService.willTeamReleaseManager(manager));
    }

    @Test
    public void willTeamReleaseManager_ShouldReturnTrueIfManagerHasNotImprovedAndExpectationIsNotMet() {
        final Manager manager = Manager.builder()
                .managerId(1)
                .year("2002")
                .teamId(5)
                .seasons(4)
                .score(4)
                .totalSeasons(4)
                .totalScore(4)
                .build();

        when(teamRepository.findOne(any()))
                .thenReturn(Optional.of(Team.builder().wins(55).playoffRank(0).expectation(1.05).build()))
                .thenReturn(Optional.of(Team.builder().wins(55).playoffRank(0).expectation(1.05).build()));

        assertTrue(teamService.willTeamReleaseManager(manager));
    }
}