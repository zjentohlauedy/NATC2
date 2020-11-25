package org.natc.app.generator;

import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.ScheduleData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component("regular-season-schedule-data-generator")
public class RegularSeasonScheduleDataGenerator implements ScheduleDataGenerator {

    public static final int CONFERENCE_ROUNDS = 3;
    public static final int DIVISION_ROUNDS = 4;

    private final LeagueConfiguration leagueConfiguration;

    public RegularSeasonScheduleDataGenerator(final LeagueConfiguration leagueConfiguration) {
        this.leagueConfiguration = leagueConfiguration;
    }

    public List<ScheduleData> generate() {
        final Integer numberOfTeams = leagueConfiguration.getNumberOfTeams();
        final Integer teamsPerDivision = leagueConfiguration.getTeamsPerDivision();
        final Integer teamsPerConference = leagueConfiguration.getTeamsPerConference();
        final Integer gamesPerDay = leagueConfiguration.getGamesPerDay();
        final Integer outOfConferenceGames = leagueConfiguration.getOutOfConferenceGames();

        final List<ScheduleData> scheduleDataList = new ArrayList<>();
        final int[] teams = new int[numberOfTeams];

        for ( int i = 0; i < numberOfTeams; ++i ) teams[i] = i + 1;

        // Division Series
        generateRoundRobin(teams, scheduleDataList, teamsPerDivision, DIVISION_ROUNDS);

        // Conference Series
        generateRoundRobin(teams, scheduleDataList, teamsPerConference, CONFERENCE_ROUNDS);

        // Out of Conference Series
        final int lastIndex = teams.length - 1;

        for ( int round = 0; round < outOfConferenceGames; ++round ) {
            final ScheduleData scheduleData = new ScheduleData();

            for (int match = 0; match < gamesPerDay; ++match) {

                // After conference schedule team list has the form with road/home bias:
                //  Div: 1111111111 2222222222 3333333333 4444444444
                // Conf: 1111111111 1111111111 2222222222 2222222222
                // Bias: rhrhrhrhrh rhrhrhrhrh rhrhrhrhrh rhrhrhrhrh
                // Since an odd number of games are being generated, make sure to even out
                // this bias.
                if ((round == 0 && isOdd(match)) || isOdd(round)) {
                    scheduleData.getMatches().add(new ScheduleData.Match(
                            teams[lastIndex - match],
                            teams[match]
                    ));
                } else {
                    scheduleData.getMatches().add(new ScheduleData.Match(
                            teams[match],
                            teams[lastIndex - match]
                    ));
                }
            }

            scheduleDataList.add(scheduleData);

            rotate( teams, teamsPerConference, teamsPerConference );
        }

        Collections.shuffle( scheduleDataList );

        return scheduleDataList;
    }

    private void generateRoundRobin(
            final int[] teams, final List<ScheduleData> scheduleDataList, final int partitionSize, final int rounds
    ) {
        final int partitions = teams.length / partitionSize;

        for (int i = 0; i < partitions; ++i) {
            shuffle(teams, (i * partitionSize), partitionSize);
        }

        boolean flip = false;

        for ( int series = 0; series < partitionSize - 1; ++series ) {
            boolean invert = false;

            for ( int round = 0; round < rounds; ++round ) {
                final ScheduleData scheduleData = new ScheduleData();

                generateMatches( scheduleData, teams, partitionSize, invert, flip );

                scheduleDataList.add(scheduleData);

                invert = !invert;
                flip = !flip;
            }

            for (int i = 0; i < partitions; ++i) {
                rotate(teams, (i * partitionSize) + 1, partitionSize - 1);
            }
        }

    }

    private void generateMatches(final ScheduleData data, final int[] teams, final int partitionSize, final boolean invert, final boolean flip ) {

        for ( int i = 0; i < teams.length; i += partitionSize )
        {
            for ( int j = 0; j < (partitionSize / 2); ++j )
            {
                int road = i + j;
                int home = i + (partitionSize - 1) - j;

                // Swap road and home for first team (since it doesn't rotate)
                if (j == 0 && flip) {
                    final int temp = road;
                    road = home;
                    home = temp;
                }

                // Every other pass swap home and road teams (so some teams don't play every game at home)
                if (j != 0 && invert) {
                    final int temp = road;
                    road = home;
                    home = temp;
                }

                data.getMatches().add(new ScheduleData.Match(teams[home], teams[road]));
            }
        }
    }

    private void rotate(final int[] array, final int offset, final int length) {
        final int x = array[(offset + length) - 1];

        for (int i = (offset + length) - 1; i > offset; --i) {
            array[i] = array[i - 1];
        }

        array[offset] = x;
    }

    private void shuffle(final int[] array, final int offset, final int length) {
        for (int i = length; i > 1; --i) {
            final int n = offset + (int) Math.floor(Math.random() * (float) (i - 1));
            final int m = offset + i - 1;
            final int x;

            x = array[n];
            array[n] = array[m];
            array[m] = x;
        }
    }

    private boolean isOdd(final int value) {
        return (value % 2) != 0;
    }
}
