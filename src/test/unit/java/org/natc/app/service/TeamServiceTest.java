package org.natc.app.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.Team;
import org.natc.app.exception.TeamNotFoundException;
import org.natc.app.repository.TeamRepository;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Nested
    class GenerateTeams {

        @Test
        public void shouldReturnAListOfTeamsGenerated() {
            final List<Team> teamList = teamService.generateTeams(null);

            assertFalse(teamList.isEmpty());
        }

        @Test
        public void shouldGenerateFourtyFourTeams() {
            final List<Team> teamList = teamService.generateTeams(null);

            assertEquals(44, teamList.size());
        }

        @Test
        public void shouldGenerateFourtyRegularTeams() {
            final List<Team> teamList = teamService.generateTeams(null);

            assertEquals(40, teamList.stream().filter(team -> team.getAllstarTeam().equals(0)).count());
        }

        @Test
        public void shouldGenerateFourAllstarTeams() {
            final List<Team> teamList = teamService.generateTeams(null);

            assertEquals(4, teamList.stream().filter(team -> team.getAllstarTeam().equals(1)).count());
        }

        @Test
        public void shouldGenerateTwentyTwoTeamsPerConference() {
            final List<Team> teamList = teamService.generateTeams(null);

            assertEquals(22, teamList.stream().filter(team -> team.getConference().equals(0)).count());
            assertEquals(22, teamList.stream().filter(team -> team.getConference().equals(1)).count());
        }

        @Test
        public void shouldGenerateElevenTeamsPerDivision() {
            final List<Team> teamList = teamService.generateTeams(null);

            assertEquals(11, teamList.stream().filter(team -> team.getDivision().equals(0)).count());
            assertEquals(11, teamList.stream().filter(team -> team.getDivision().equals(1)).count());
            assertEquals(11, teamList.stream().filter(team -> team.getDivision().equals(2)).count());
            assertEquals(11, teamList.stream().filter(team -> team.getDivision().equals(3)).count());
        }

        @Test
        public void shouldGenerateTeamsWithUniqueTeamIdsFromOneToFourtyFour() {
            final List<Team> teamList = teamService.generateTeams(null);

            final Set<Integer> teamIds = teamList.stream().map(Team::getTeamId).collect(Collectors.toSet());

            assertEquals(44, teamIds.size());
            assertEquals(1, teamIds.stream().mapToInt(v -> v).min().orElse(0));
            assertEquals(44, teamIds.stream().mapToInt(v -> v).max().orElse(0));
        }

        @Test
        public void shouldGenerateTeamsForTheGivenYear() {
            final String expectedYear = "2020";

            final List<Team> teamList = teamService.generateTeams(expectedYear);

            assertEquals(44, teamList.stream().filter(team -> team.getYear().equals(expectedYear)).count());
        }

        @Test
        public void shouldGenerateRegularTeamsAllWithDifferentLocations() {
            final List<Team> teamList = teamService.generateTeams(null);

            final Set<String> locations = teamList.stream()
                    .filter(team -> team.getAllstarTeam().equals(0))
                    .map(Team::getLocation)
                    .collect(Collectors.toSet());

            assertEquals(40, locations.size());
        }

        @Test
        public void shouldGenerateRegularTeamsAllWithDifferentNames() {
            final List<Team> teamList = teamService.generateTeams(null);

            final Set<String> names = teamList.stream()
                    .filter(team -> team.getAllstarTeam().equals(0))
                    .map(Team::getName)
                    .collect(Collectors.toSet());

            assertEquals(40, names.size());
        }

        @Test
        public void shouldGenerateRegularTeamsAllWithDifferentAbbreviations() {
            final List<Team> teamList = teamService.generateTeams(null);

            final Set<String> abbreviations = teamList.stream()
                    .filter(team -> team.getAllstarTeam().equals(0))
                    .map(Team::getAbbreviation)
                    .collect(Collectors.toSet());

            assertEquals(40, abbreviations.size());
        }

        @Test
        public void shouldGenerateRegularTeamsAllWithATimeZone() {
            final List<Team> teamList = teamService.generateTeams(null);

            assertEquals(40, teamList.stream()
                    .filter(team -> team.getAllstarTeam().equals(0) && !team.getTimeZone().isBlank()).count());
        }

        @Test
        public void shouldGenerateRegularTeamsAllWithAGameTimeOf965() {
            final List<Team> teamList = teamService.generateTeams(null);

            assertEquals(40, teamList.stream()
                    .filter(team -> team.getAllstarTeam().equals(0) && team.getGameTime().equals(965)).count());
        }

        @Test
        public void shouldGenerateAllstarTeamsWithLocationAsDivisionNames() {
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
        public void shouldGenerateAllstarTeamsWithNameAsAllStars() {
            final List<Team> teamList = teamService.generateTeams(null);

            final Set<String> name = teamList.stream()
                    .filter(team -> team.getAllstarTeam().equals(1))
                    .map(Team::getName)
                    .collect(Collectors.toSet());

            assertEquals(1, name.size());
            assertEquals("All Stars", name.toArray()[0]);
        }

        @Test
        public void shouldGenerateAllstarTeamsWithAbbreviationsBasedOnDivisionNames() {
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
        public void shouldGenerateAllstarTeamsWithTimeZoneAsNewYork() {
            final List<Team> teamList = teamService.generateTeams(null);

            final Set<String> timeZones = teamList.stream()
                    .filter(team -> team.getAllstarTeam().equals(1))
                    .map(Team::getTimeZone)
                    .collect(Collectors.toSet());

            assertEquals(1, timeZones.size());
            assertEquals("America/New_York", timeZones.toArray()[0]);
        }

        @Test
        public void shouldGenerateAllstarTeamsAllWithAGameTimeOf965() {
            final List<Team> teamList = teamService.generateTeams(null);

            assertEquals(4, teamList.stream()
                    .filter(team -> team.getAllstarTeam().equals(1) && team.getGameTime().equals(965)).count());
        }

        @Test
        public void shouldCallTeamRepositoryToSaveTheTeams() {
            final List<Team> teamList = teamService.generateTeams(null);

            verify(teamRepository).saveAll(teamList);
        }
    }

    @Nested
    class UpdateTeamsForNewSeason {

        @Test
        public void shouldCallTheRepositoryToCopyTeamsForNewYear() {
            teamService.updateTeamsForNewSeason(null, null);

            verify(teamRepository).copyTeamsForNewYear(any(), any());
        }

        @Test
        public void shouldPassThePreviousYearAndNewYearToTheRepository() {
            teamService.updateTeamsForNewSeason("2008", "2009");

            verify(teamRepository).copyTeamsForNewYear("2008", "2009");
        }
    }

    @Nested
    class WillTeamReleaseManager {

        @Test
        public void shouldReturnFalseIfManagerHasLessThanThreeSeasons() {
            final Manager manager = Manager.builder().managerId(1).year("2002").seasons(0).build();

            assertFalse(teamService.willTeamReleaseManager(manager));
        }

        @Test
        public void shouldCallTeamRepositoryToGetManagersTeamsTwoPreviousYearRecords() {
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
        public void shouldNotCallRepositoryIfManagerHasLessThanThreeSeasons() {
            final Manager manager = Manager.builder().managerId(1).year("2002").seasons(2).build();

            teamService.willTeamReleaseManager(manager);

            verify(teamRepository, never()).findOne(any());
        }

        @Test
        public void shouldReturnFalseIfPreviousYearsTeamRecordIsNotFound() {
            final Manager manager = Manager.builder().managerId(1).year("2002").teamId(5).seasons(4).build();

            when(teamRepository.findOne(any())).thenReturn(Optional.empty());

            assertFalse(teamService.willTeamReleaseManager(manager));
        }

        @Test
        public void shouldReturnFalseIfTwoYearsBackTeamRecordIsNotFound() {
            final Manager manager = Manager.builder().managerId(1).year("2002").teamId(5).seasons(4).build();

            when(teamRepository.findOne(any()))
                    .thenReturn(Optional.of(Team.builder().build()))
                    .thenReturn(Optional.empty());

            assertFalse(teamService.willTeamReleaseManager(manager));
        }

        @Test
        public void shouldReturnFalseIfPlayoffRankImprovedBetweenYears() {
            final Manager manager = Manager.builder().managerId(1).year("2002").teamId(5).seasons(4).build();

            when(teamRepository.findOne(any()))
                    .thenReturn(Optional.of(Team.builder().playoffRank(3).build()))
                    .thenReturn(Optional.of(Team.builder().playoffRank(2).build()));

            assertFalse(teamService.willTeamReleaseManager(manager));
        }

        @Test
        public void shouldReturnFalseIfWinsImprovedBetweenYears() {
            final Manager manager = Manager.builder().managerId(1).year("2002").teamId(5).seasons(4).build();

            when(teamRepository.findOne(any()))
                    .thenReturn(Optional.of(Team.builder().wins(65).playoffRank(0).build()))
                    .thenReturn(Optional.of(Team.builder().wins(55).playoffRank(0).build()));

            assertFalse(teamService.willTeamReleaseManager(manager));
        }

        @Test
        public void shouldReturnFalseIfManagerPerformanceRatingIsGreaterThanExpectation() {
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
        public void shouldReturnTrueIfManagerHasNotImprovedAndExpectationIsNotMet() {
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

    @Nested
    class GetTeamByTeamIdAndYear {

        @Test
        public void shouldCallTeamRepositoryToFindTeam() throws TeamNotFoundException {
            when(teamRepository.findOne(any())).thenReturn(Optional.of(Team.builder().build()));

            teamService.getTeamByTeamIdAndYear(123, "2000");

            verify(teamRepository).findOne(any());
        }

        @Test
        public void shouldPassTheGivenTeamIdAndYearToTheTeamRepository() throws TeamNotFoundException {
            when(teamRepository.findOne(any())).thenReturn(Optional.of(Team.builder().build()));

            teamService.getTeamByTeamIdAndYear(123, "2000");

            verify(teamRepository).findOne(captor.capture());

            final Team team = captor.getValue().getProbe();

            assertEquals(123, team.getTeamId());
            assertEquals("2000", team.getYear());
        }

        @Test
        public void shouldReturnTheTeamReturnedByTheTeamRepositoryIfAMatchingTeamIsFound() throws TeamNotFoundException {
            final Team expectedTeam = Team.builder().teamId(123).year("2000").build();

            when(teamRepository.findOne(any())).thenReturn(Optional.of(expectedTeam));

            final Team actualTeam = teamService.getTeamByTeamIdAndYear(123, "2000");

            assertSame(expectedTeam, actualTeam);
        }

        @Test
        public void shouldThrowTeamNotFoundExceptionIfAMatchingTeamIsNotFound() {
            when(teamRepository.findOne(any())).thenReturn(Optional.empty());

            assertThrows(TeamNotFoundException.class, () -> teamService.getTeamByTeamIdAndYear(123, "2000"));
        }
    }
}