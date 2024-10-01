package org.natc.app.generator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegularSeasonScheduleDataGeneratorTest {

    @Mock
    private LeagueConfiguration leagueConfiguration;

    @InjectMocks
    private RegularSeasonScheduleDataGenerator generator;

    @Nested
    class Generate {

        @BeforeEach
        void setup() {
            when(leagueConfiguration.getNumberOfTeams()).thenReturn(40);
            when(leagueConfiguration.getGamesPerDay()).thenReturn(20);
            when(leagueConfiguration.getTeamsPerConference()).thenReturn(20);
            when(leagueConfiguration.getTeamsPerDivision()).thenReturn(10);
            when(leagueConfiguration.getOutOfConferenceGames()).thenReturn(7);
        }

        @Test
        void shouldReturnAListOfScheduleDataObjects() {
            final List<ScheduleData> scheduleDataList = generator.generate();

            assertFalse(scheduleDataList.isEmpty());
        }

        @Test
        void shouldReturnAsManyScheduleDataObjectsAsThereAreDaysInRegularSeason() {
            when(leagueConfiguration.getDaysInRegularSeason()).thenReturn(100);

            final List<ScheduleData> scheduleDataList = generator.generate();

            assertEquals(leagueConfiguration.getDaysInRegularSeason(), scheduleDataList.size());
        }

        @Test
        void shouldReturnScheduleDataWhereEveryDayIsComprisedOfEveryTeam() {
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
        void shouldReturnScheduleDataWhereEveryDayHasAConfiguredNumberOfGames() {
            final List<ScheduleData> scheduleDataList = generator.generate();

            assertFalse(scheduleDataList.isEmpty());

            for (final ScheduleData data : scheduleDataList) {
                assertEquals(leagueConfiguration.getGamesPerDay(), data.getMatches().size());
            }
        }

        @Test
        void shouldReturnScheduleDataWhereEveryTeamPlaysAConfiguredNumberOfGames() {
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
        void shouldReturnScheduleDataWhereEveryTeamPlaysHalfTheGamesAtHome() {
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
        void shouldReturnScheduleDataWhereEveryTeamPlaysHalfTheGamesOnTheRoad() {
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
        void shouldReturnScheduleDataWhereEveryTeamPlaysOtherTeamsInOwnDivisionTeamsSevenTimes() {
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
        void shouldReturnScheduleDataWhereEveryTeamPlaysThreeOrFourHomeAndRoadGamesWithinDivision() {
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
                        } else {
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
        void shouldReturnScheduleDataWhereEveryTeamPlaysOtherTeamsInRivalDivisionTeamsThreeTimes() {
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
        void shouldReturnScheduleDataWhereEveryTeamPlaysOneOrTwoHomeAndRoadGamesAgainstRivalDivision() {
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
                        } else {
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
        void shouldReturnScheduleDataWhereEveryTeamPlaysSevenGamesAgainstCrossConferenceTeams() {
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
        void shouldReturnScheduleDataWhereEveryTeamPlaysThreeOrFourHomeAndRoadGamesAgainstCrossConferenceTeams() {
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
                } else {
                    assertEquals(3, scheduleDataList.stream()
                            .flatMap(scheduleData -> scheduleData.getMatches().stream())
                            .filter(match -> match.getRoadTeam().equals(team) && !sameConference(match.getHomeTeam(), match.getRoadTeam()))
                            .count());
                }
            }
        }

        @Test
        void shouldReturnADifferentResultEveryTime() {
            final List<ScheduleData> firstRun = generator.generate();
            final List<ScheduleData> secondRun = generator.generate();

            assertNotEquals(
                    firstRun.stream().flatMap(scheduleData -> scheduleData.getMatches().stream())
                            .map(match -> match.getRoadTeam().toString() + match.getHomeTeam().toString())
                            .toList(),
                    secondRun.stream().flatMap(scheduleData -> scheduleData.getMatches().stream())
                            .map(match -> match.getRoadTeam().toString() + match.getHomeTeam().toString())
                            .toList()
            );
        }

        private Boolean sameDivision(final Integer team, final Integer opponent) {
            if (team >= 1 && team <= 10) return (opponent >= 1 && opponent <= 10);
            if (team >= 11 && team <= 20) return (opponent >= 11 && opponent <= 20);
            if (team >= 21 && team <= 30) return (opponent >= 21 && opponent <= 30);
            if (team >= 31 && team <= 40) return (opponent >= 31 && opponent <= 40);

            return false;
        }

        private Boolean sameConference(final Integer team, final Integer opponent) {
            if (team >= 1 && team <= 20) return (opponent >= 1 && opponent <= 20);
            if (team >= 21 && team <= 40) return (opponent >= 21 && opponent <= 40);

            return false;
        }
    }
}