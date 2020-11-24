package org.natc.app.generator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.ScheduleData;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PreseasonScheduleDataGeneratorTest {

    @Mock
    private LeagueConfiguration leagueConfiguration;

    @InjectMocks
    private PreseasonScheduleDataGenerator generator;

    @BeforeEach
    public void setup() {
        when(leagueConfiguration.getDaysInPreseason()).thenReturn(10);
        when(leagueConfiguration.getNumberOfTeams()).thenReturn(40);
        when(leagueConfiguration.getGamesPerDay()).thenReturn(20);
    }

    @Test
    public void generate_ShouldReturnAListOfScheduleDataObjects() {
        final List<ScheduleData> scheduleDataList = generator.generate();

        assertFalse(scheduleDataList.isEmpty());
    }

    @Test
    public void generate_ShouldReturnAsManyScheduleDataObjectsAsThereAreDaysInPreseason() {
        final List<ScheduleData> scheduleDataList = generator.generate();

        assertEquals(leagueConfiguration.getDaysInPreseason(), scheduleDataList.size());
    }

    @Test
    public void generate_ShouldReturnScheduleDataWhereEveryDayIsComprisedOfEveryTeam() {
        final List<ScheduleData> scheduleDataList = generator.generate();

        assertFalse(scheduleDataList.isEmpty());

        for (final ScheduleData data : scheduleDataList) {
            final Set<Integer> teams = new TreeSet<>();

            for (final ScheduleData.Match match : data.getMatches()) {
                teams.add(match.getHomeTeam());
                teams.add(match.getRoadTeam());
            }

            assertEquals(leagueConfiguration.getNumberOfTeams(), teams.size());
        }
    }

    @Test
    public void generate_ShouldReturnScheduleDataWhereEveryDayHasAConfiguredNumberOfGames() {
        final List<ScheduleData> scheduleDataList = generator.generate();

        assertFalse(scheduleDataList.isEmpty());

        for (final ScheduleData data : scheduleDataList) {
            assertEquals(leagueConfiguration.getGamesPerDay(), data.getMatches().size());
        }
    }

    @Test
    public void generate_ShouldReturnScheduleDataWhereEveryTeamPlaysAConfiguredNumberOfGames() {
        final List<ScheduleData> scheduleDataList = generator.generate();

        assertFalse(scheduleDataList.isEmpty());

        for (int i = 1; i <= leagueConfiguration.getNumberOfTeams(); ++i) {
            final int team = i;
            assertEquals(leagueConfiguration.getDaysInPreseason().longValue(),
            scheduleDataList.stream().flatMap(scheduleData -> scheduleData.getMatches().stream()).filter(match ->
                    match.getHomeTeam().equals(team) || match.getRoadTeam().equals(team)).count());
        }
    }

    @Test
    public void generate_ShouldReturnScheduleDataWhereEveryTeamPlaysHalfTheGamesAtHome() {
        final List<ScheduleData> scheduleDataList = generator.generate();

        assertFalse(scheduleDataList.isEmpty());

        for (int i = 1; i <= leagueConfiguration.getNumberOfTeams(); ++i) {
            final int team = i;
            assertEquals(leagueConfiguration.getDaysInPreseason().longValue() / 2,
                    scheduleDataList.stream().flatMap(scheduleData -> scheduleData.getMatches().stream()).filter(match ->
                            match.getHomeTeam().equals(team)).count());
        }
    }

    @Test
    public void generate_ShouldReturnScheduleDataWhereEveryTeamPlaysHalfTheGamesOnTheRoad() {
        final List<ScheduleData> scheduleDataList = generator.generate();

        assertFalse(scheduleDataList.isEmpty());

        for (int i = 1; i <= leagueConfiguration.getNumberOfTeams(); ++i) {
            final int team = i;
            assertEquals(leagueConfiguration.getDaysInPreseason().longValue() / 2,
                    scheduleDataList.stream().flatMap(scheduleData -> scheduleData.getMatches().stream()).filter(match ->
                            match.getRoadTeam().equals(team)).count());
        }
    }

    @Test
    public void generate_ShouldReturnSchedleDateWhereEveryTeamPlaysADifferentTeamEveryGame() {
        final List<ScheduleData> scheduleDataList = generator.generate();

        assertFalse(scheduleDataList.isEmpty());

        for (int i = 1; i <= leagueConfiguration.getNumberOfTeams(); ++i) {
            final int team = i;
            assertEquals(leagueConfiguration.getDaysInPreseason().longValue(),
                    scheduleDataList.stream().flatMap(scheduleData -> scheduleData.getMatches().stream())
                            .filter(match -> match.getHomeTeam().equals(team) || match.getRoadTeam().equals(team))
                            .map(match -> match.getHomeTeam().equals(team) ? match.getRoadTeam() : match.getHomeTeam())
                            .collect(Collectors.toSet())
                            .size());
        }
    }

    @Test
    public void generate_ShouldReturnADifferentResultEveryTime() {
        final List<ScheduleData> firstRun = generator.generate();
        final List<ScheduleData> secondRun = generator.generate();

        assertNotEquals(
                firstRun.stream().flatMap(scheduleData -> scheduleData.getMatches().stream())
                        .map(match -> match.getRoadTeam().toString() + match.getHomeTeam().toString())
                        .collect(Collectors.toList()),
                secondRun.stream().flatMap(scheduleData -> scheduleData.getMatches().stream())
                        .map(match -> match.getRoadTeam().toString() + match.getHomeTeam().toString())
                        .collect(Collectors.toList())
        );
    }
}