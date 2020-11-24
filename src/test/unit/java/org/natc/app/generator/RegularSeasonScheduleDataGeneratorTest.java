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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegularSeasonScheduleDataGeneratorTest {

    @Mock
    private LeagueConfiguration leagueConfiguration;

    @InjectMocks
    private RegularSeasonScheduleDataGenerator generator;

    @BeforeEach
    public void setup() {
        when(leagueConfiguration.getNumberOfTeams()).thenReturn(40);
        when(leagueConfiguration.getGamesPerDay()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerConference()).thenReturn(20);
        when(leagueConfiguration.getTeamsPerDivision()).thenReturn(10);
        when(leagueConfiguration.getOutOfConferenceGames()).thenReturn(7);
    }

    @Test
    public void generate_ShouldReturnAListOfScheduleDataObjects() {
        final List<ScheduleData> scheduleDataList = generator.generate();

        assertFalse(scheduleDataList.isEmpty());
    }

    @Test
    public void generate_ShouldReturnAsManyScheduleDataObjectsAsThereAreDaysInRegularSeason() {
        when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);

        final List<ScheduleData> scheduleDataList = generator.generate();

        assertEquals(leagueConfiguration.getDaysInRegularSeason(), scheduleDataList.size());
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
        when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);

        final List<ScheduleData> scheduleDataList = generator.generate();

        assertFalse(scheduleDataList.isEmpty());

        for (int i = 1; i <= leagueConfiguration.getNumberOfTeams(); ++i) {
            final int team = i;
            assertEquals(leagueConfiguration.getDaysInRegularSeason().longValue(),
                    scheduleDataList.stream().flatMap(scheduleData -> scheduleData.getMatches().stream()).filter(match ->
                            match.getHomeTeam().equals(team) || match.getRoadTeam().equals(team)).count());
        }
    }

    @Test
    public void generate_ShouldReturnScheduleDataWhereEveryTeamPlaysHalfTheGamesAtHome() {
        when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);

        final List<ScheduleData> scheduleDataList = generator.generate();

        assertFalse(scheduleDataList.isEmpty());

        for (int i = 1; i <= leagueConfiguration.getNumberOfTeams(); ++i) {
            final int team = i;
            assertEquals(leagueConfiguration.getDaysInRegularSeason().longValue() / 2,
                    scheduleDataList.stream().flatMap(scheduleData -> scheduleData.getMatches().stream()).filter(match ->
                            match.getHomeTeam().equals(team)).count());
        }
    }

    @Test
    public void generate_ShouldReturnScheduleDataWhereEveryTeamPlaysHalfTheGamesOnTheRoad() {
        when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);

        final List<ScheduleData> scheduleDataList = generator.generate();

        assertFalse(scheduleDataList.isEmpty());

        for (int i = 1; i <= leagueConfiguration.getNumberOfTeams(); ++i) {
            final int team = i;
            assertEquals(leagueConfiguration.getDaysInRegularSeason().longValue() / 2,
                    scheduleDataList.stream().flatMap(scheduleData -> scheduleData.getMatches().stream()).filter(match ->
                            match.getRoadTeam().equals(team)).count());
        }
    }

    @Test
    public void generate_ShouldReturnScheduleDataWhereEveryTeamPlaysOtherTeamsInOwnDivisionTeamsSevenTimes() {
        final List<ScheduleData> scheduleDataList = generator.generate();

        assertFalse(scheduleDataList.isEmpty());

        for (int i = 1; i <= leagueConfiguration.getNumberOfTeams(); ++i) {
            final int team = i;

            for (int j = 1; j <= leagueConfiguration.getNumberOfTeams(); ++j) {
                final int opponent = j;

                if (team == opponent) continue;

                if (sameDivision(team, opponent)) {
                    assertEquals(7,
                            scheduleDataList.stream().flatMap(scheduleData -> scheduleData.getMatches().stream()).filter(match ->
                                    (match.getHomeTeam().equals(team) && match.getRoadTeam().equals(opponent)) ||
                                            (match.getRoadTeam().equals(team) && match.getHomeTeam().equals(opponent))
                            ).count());
                }
            }
        }
    }

    @Test
    public void generate_ShouldReturnScheduleDataWhereEveryTeamPlaysThreeOrFourHomeAndRoadGamesWithinDivision() {
        final List<ScheduleData> scheduleDataList = generator.generate();

        assertFalse(scheduleDataList.isEmpty());

        for (int i = 1; i <= leagueConfiguration.getNumberOfTeams(); ++i) {
            final int team = i;

            for (int j = 1; j <= leagueConfiguration.getNumberOfTeams(); ++j) {
                final int opponent = j;

                if (team == opponent) continue;

                if (sameDivision(team, opponent)) {
                    final long homeGames = scheduleDataList.stream()
                            .flatMap(scheduleData -> scheduleData.getMatches().stream())
                            .filter(match -> match.getHomeTeam().equals(team) && match.getRoadTeam().equals(opponent))
                            .count();

                    assertTrue(3 <= homeGames && homeGames <= 4);

                    if (homeGames == 3) {
                        assertEquals(4,
                                scheduleDataList.stream()
                                        .flatMap(scheduleData -> scheduleData.getMatches().stream())
                                        .filter(match -> match.getRoadTeam().equals(team) && match.getHomeTeam().equals(opponent))
                                        .count());
                    }
                    else {
                        assertEquals(3,
                                scheduleDataList.stream()
                                        .flatMap(scheduleData -> scheduleData.getMatches().stream())
                                        .filter(match -> match.getRoadTeam().equals(team) && match.getHomeTeam().equals(opponent))
                                        .count());
                    }
                }
            }
        }
    }

    @Test
    public void generate_ShouldReturnScheduleDataWhereEveryTeamPlaysOtherTeamsInRivalDivisionTeamsThreeTimes() {
        final List<ScheduleData> scheduleDataList = generator.generate();

        assertFalse(scheduleDataList.isEmpty());

        for (int i = 1; i <= leagueConfiguration.getNumberOfTeams(); ++i) {
            final int team = i;

            for (int j = 1; j <= leagueConfiguration.getNumberOfTeams(); ++j) {
                final int opponent = j;

                if (team == opponent) continue;

                if (sameConference(team, opponent) && !sameDivision(team, opponent)) {
                    assertEquals(3,
                            scheduleDataList.stream().flatMap(scheduleData -> scheduleData.getMatches().stream()).filter(match ->
                                    (match.getHomeTeam().equals(team) && match.getRoadTeam().equals(opponent)) ||
                                            (match.getRoadTeam().equals(team) && match.getHomeTeam().equals(opponent))
                            ).count());
                }
            }
        }
    }

    @Test
    public void generate_ShouldReturnScheduleDataWhereEveryTeamPlaysOneOrTwoHomeAndRoadGamesAgainstRivalDivision() {
        final List<ScheduleData> scheduleDataList = generator.generate();

        assertFalse(scheduleDataList.isEmpty());

        for (int i = 1; i <= leagueConfiguration.getNumberOfTeams(); ++i) {
            final int team = i;

            for (int j = 1; j <= leagueConfiguration.getNumberOfTeams(); ++j) {
                final int opponent = j;

                if (team == opponent) continue;

                if (sameConference(team, opponent) && !sameDivision(team, opponent)) {
                    final long homeGames = scheduleDataList.stream()
                            .flatMap(scheduleData -> scheduleData.getMatches().stream())
                            .filter(match -> match.getHomeTeam().equals(team) && match.getRoadTeam().equals(opponent))
                            .count();

                    assertTrue(1 <= homeGames && homeGames <= 2);

                    if (homeGames == 1) {
                        assertEquals(2,
                                scheduleDataList.stream()
                                        .flatMap(scheduleData -> scheduleData.getMatches().stream())
                                        .filter(match -> match.getRoadTeam().equals(team) && match.getHomeTeam().equals(opponent))
                                        .count());
                    }
                    else {
                        assertEquals(1,
                                scheduleDataList.stream()
                                        .flatMap(scheduleData -> scheduleData.getMatches().stream())
                                        .filter(match -> match.getRoadTeam().equals(team) && match.getHomeTeam().equals(opponent))
                                        .count());
                    }
                }
            }
        }
    }

    @Test
    public void generate_ShouldReturnScheduleDataWhereEveryTeamPlaysSevenGamesAgainstCrossConferenceTeams() {
        final List<ScheduleData> scheduleDataList = generator.generate();

        assertFalse(scheduleDataList.isEmpty());

        for (int i = 1; i <= leagueConfiguration.getNumberOfTeams(); ++i) {
            final int team = i;

            assertEquals(7,
                    scheduleDataList.stream().flatMap(scheduleData -> scheduleData.getMatches().stream()).filter(match ->
                            (match.getHomeTeam().equals(team) || match.getRoadTeam().equals(team)) &&
                            !sameConference(match.getHomeTeam(), match.getRoadTeam())
                    ).count());
        }
    }

    @Test
    public void generate_ShouldReturnScheduleDataWhereEveryTeamPlaysThreeOrFourHomeAndRoadGamesAgainstCrossConferenceTeams() {
        final List<ScheduleData> scheduleDataList = generator.generate();

        assertFalse(scheduleDataList.isEmpty());

        for (int i = 1; i <= leagueConfiguration.getNumberOfTeams(); ++i) {
            final int team = i;

            final long homeGames = scheduleDataList.stream()
                    .flatMap(scheduleData -> scheduleData.getMatches().stream())
                    .filter(match -> match.getHomeTeam().equals(team) && !sameConference(match.getHomeTeam(), match.getRoadTeam()))
                    .count();

            assertTrue(3 <= homeGames && homeGames <= 4);

            if (homeGames == 3) {
                assertEquals(4, scheduleDataList.stream()
                        .flatMap(scheduleData -> scheduleData.getMatches().stream())
                        .filter(match -> match.getRoadTeam().equals(team) && !sameConference(match.getHomeTeam(), match.getRoadTeam()))
                        .count());
            }
            else {
                assertEquals(3, scheduleDataList.stream()
                        .flatMap(scheduleData -> scheduleData.getMatches().stream())
                        .filter(match -> match.getRoadTeam().equals(team) && !sameConference(match.getHomeTeam(), match.getRoadTeam()))
                        .count());
            }
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

    private Boolean sameDivision(final Integer team, final Integer opponent) {
        if (team >=  1 && team <= 10) return (opponent >=  1 && opponent <= 10);
        if (team >= 11 && team <= 20) return (opponent >= 11 && opponent <= 20);
        if (team >= 21 && team <= 30) return (opponent >= 21 && opponent <= 30);
        if (team >= 31 && team <= 40) return (opponent >= 31 && opponent <= 40);

        return false;
    }

    private Boolean sameConference(final Integer team, final Integer opponent) {
        if (team >=  1 && team <= 20) return (opponent >=  1 && opponent <= 20);
        if (team >= 21 && team <= 40) return (opponent >= 21 && opponent <= 40);

        return false;
    }
}