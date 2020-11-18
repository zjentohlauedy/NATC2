package org.natc.app.service;

import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ScheduleService {

    private final ScheduleRepository repository;
    private final LeagueConfiguration leagueConfiguration;

    @Autowired
    public ScheduleService(final ScheduleRepository repository, final LeagueConfiguration leagueConfiguration) {
        this.repository = repository;
        this.leagueConfiguration = leagueConfiguration;
    }

    public void generateSchedule(final String year) {
        final List<Schedule> scheduleList = new ArrayList<>();
        final int yearNum = Integer.parseInt(year);

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(1)
                .type(ScheduleType.BEGINNING_OF_SEASON.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(LocalDate.of(yearNum, Month.JANUARY, 1))
                .build());

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(2)
                .type(ScheduleType.MANAGER_CHANGES.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(getDayOfWeekOnOrAfter(LocalDate.of(yearNum, Month.FEBRUARY, 1), DayOfWeek.MONDAY))
                .build());

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(3)
                .type(ScheduleType.PLAYER_CHANGES.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(LocalDate.of(yearNum, Month.FEBRUARY, 28))
                .build());

        LocalDate previousDate = getDayOfWeekOnOrAfter(LocalDate.of(yearNum, Month.MARCH, 1), DayOfWeek.MONDAY);

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(4)
                .type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(previousDate)
                .build());

        previousDate = previousDate.plusDays(1);

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(5)
                .type(ScheduleType.ROOKIE_DRAFT_ROUND_2.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(previousDate)
                .build());

        previousDate = previousDate.plusDays(13);

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(6)
                .type(ScheduleType.TRAINING_CAMP.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(previousDate)
                .build());

        previousDate = getDayOfWeekOnOrAfter(LocalDate.of(yearNum, Month.APRIL, 1), DayOfWeek.MONDAY);

        final List<String> preseasonMatches = generatePreseasonSchedule();
        int preseasonSequence = 7;

        for (final String match : preseasonMatches) {
            if (previousDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) previousDate = previousDate.plusDays(2);

            scheduleList.add(Schedule.builder()
                    .year(year)
                    .sequence(preseasonSequence++)
                    .type(ScheduleType.PRESEASON.getValue())
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .data(match)
                    .scheduled(previousDate)
                    .build());

            previousDate = previousDate.plusDays(1);
        }

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(preseasonSequence++)
                .type(ScheduleType.END_OF_PRESEASON.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(previousDate)
                .build());

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(preseasonSequence)
                .type(ScheduleType.ROSTER_CUT.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(LocalDate.of(yearNum, Month.APRIL, 30))
                .build());

        previousDate = getDayOfWeekOnOrAfter(LocalDate.of(yearNum, Month.MAY, 1), DayOfWeek.MONDAY);

        final List<String> regularSeasonMatches = generateRegularSeasonSchedule();
        int regularSeasonSequence = preseasonSequence + 1;

        for (final String match : regularSeasonMatches) {
            if (previousDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) previousDate = previousDate.plusDays(2);

            scheduleList.add(Schedule.builder()
                    .year(year)
                    .sequence(regularSeasonSequence++)
                    .type(ScheduleType.REGULAR_SEASON.getValue())
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .data(match)
                    .scheduled(previousDate)
                    .build());

            previousDate = previousDate.plusDays(1);
        }

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(regularSeasonSequence++)
                .type(ScheduleType.END_OF_REGULAR_SEASON.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(previousDate)
                .build());

        previousDate = getDayOfWeekOnOrAfter(previousDate.plusDays(1), DayOfWeek.MONDAY);

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(regularSeasonSequence++)
                .type(ScheduleType.AWARDS.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(previousDate)
                .build());

        previousDate = getDayOfWeekOnOrAfter(previousDate.plusDays(1), DayOfWeek.FRIDAY);

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(regularSeasonSequence++)
                .type(ScheduleType.POSTSEASON.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(previousDate)
                .build());

        previousDate = getDayOfWeekOnOrAfter(previousDate.plusDays(1), DayOfWeek.SUNDAY);

        for (int i = 0; i < leagueConfiguration.getPlayoffGamesRoundOne(); ++i) {
            scheduleList.add(Schedule.builder()
                    .year(year)
                    .sequence(regularSeasonSequence++)
                    .type(ScheduleType.DIVISION_PLAYOFF.getValue())
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .scheduled(previousDate)
                    .build());

            previousDate = previousDate.plusDays(1);
        }

        previousDate = getDayOfWeekOnOrAfter(previousDate.plusDays(1), DayOfWeek.SUNDAY);

        for (int i = 0; i < leagueConfiguration.getPlayoffGamesRoundTwo(); ++i) {
            scheduleList.add(Schedule.builder()
                    .year(year)
                    .sequence(regularSeasonSequence++)
                    .type(ScheduleType.DIVISION_CHAMPIONSHIP.getValue())
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .scheduled(previousDate)
                    .build());

            previousDate = previousDate.plusDays(1);
        }

        previousDate = getDayOfWeekOnOrAfter(previousDate.plusDays(1), DayOfWeek.SUNDAY);

        for (int i = 0; i < leagueConfiguration.getPlayoffGamesRoundThree(); ++i) {
            scheduleList.add(Schedule.builder()
                    .year(year)
                    .sequence(regularSeasonSequence++)
                    .type(ScheduleType.CONFERENCE_CHAMPIONSHIP.getValue())
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .scheduled(previousDate)
                    .build());

            previousDate = previousDate.plusDays(1);
        }

        previousDate = getDayOfWeekOnOrAfter(previousDate.plusDays(1), DayOfWeek.SUNDAY);

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(regularSeasonSequence++)
                .type(ScheduleType.NATC_CHAMPIONSHIP.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(previousDate)
                .build());

        previousDate = previousDate.plusDays(1);

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(regularSeasonSequence++)
                .type(ScheduleType.END_OF_POSTSEASON.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(previousDate)
                .build());

        previousDate = previousDate.plusDays(1);

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(regularSeasonSequence++)
                .type(ScheduleType.ALL_STARS.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(previousDate)
                .build());

        previousDate = getDayOfWeekOnOrAfter(previousDate.plusDays(1), DayOfWeek.SATURDAY);

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(regularSeasonSequence++)
                .type(ScheduleType.ALL_STAR_DAY_1.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(previousDate)
                .build());

        previousDate = previousDate.plusDays(1);

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(regularSeasonSequence++)
                .type(ScheduleType.ALL_STAR_DAY_2.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(previousDate)
                .build());

        previousDate = previousDate.plusDays(1);

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(regularSeasonSequence++)
                .type(ScheduleType.END_OF_ALLSTAR_GAMES.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(previousDate)
                .build());

        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(regularSeasonSequence)
                .type(ScheduleType.END_OF_SEASON.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(getDayOfWeekOnOrAfter(LocalDate.of(yearNum, Month.NOVEMBER, 1), DayOfWeek.MONDAY))
                .build());

        repository.saveAll(scheduleList);
    }

    public Schedule getCurrentScheduleEntry() {

        return repository.findFirstByStatusOrderByScheduledDesc(ScheduleStatus.IN_PROGRESS.getValue()).orElse(null);
    }

    public Schedule getLastScheduleEntry() {

        return repository.findFirstByStatusOrderByScheduledDesc(ScheduleStatus.COMPLETED.getValue()).orElse(null);
    }

    public Schedule getNextScheduleEntry(final Schedule lastEntry) {
        final String year;
        final Integer sequence;

        if (isNull(lastEntry)) {
            year = leagueConfiguration.getFirstSeason();
            sequence = Schedule.FIRST_SEQUENCE;
        }
        else if (ScheduleType.END_OF_SEASON.getValue().equals(lastEntry.getType())) {
            year = String.valueOf(Integer.parseInt(lastEntry.getYear()) + 1);
            sequence = Schedule.FIRST_SEQUENCE;
        }
        else {
            year = lastEntry.getYear();
            sequence = lastEntry.getSequence() + 1;
        }

        return repository.findByYearAndSequence(year, sequence).orElse(null);
    }

    public void updateScheduleEntry(final Schedule schedule) {
        repository.save(schedule);
    }

    private LocalDate getDayOfWeekOnOrAfter(final LocalDate startDate, final DayOfWeek dayOfWeek) {
        LocalDate finalDate = startDate;

        while (!finalDate.getDayOfWeek().equals(dayOfWeek)) {
            finalDate = finalDate.plusDays(1);
        }

        return finalDate;
    }

    private List<String> generatePreseasonSchedule() {
        final List<String> schedule = new ArrayList<>();
        final ScheduleData[] data = new ScheduleData[leagueConfiguration.getDaysInPreseason()];
        final int[] teams = new int[leagueConfiguration.getNumberOfTeams()];

        for (int i = 0; i < leagueConfiguration.getNumberOfTeams(); ++i) teams[i] = i + 1;

        shuffle(teams, 0, leagueConfiguration.getNumberOfTeams());

        for (int day = 0; day < leagueConfiguration.getDaysInPreseason(); ++day) {

            data[day] = new ScheduleData();

            data[day].games = leagueConfiguration.getGamesPerDay();

            for (int match = 0; match < leagueConfiguration.getGamesPerDay(); ++match) {

                if ((day % 2) == 0) {

                    data[day].road_teams[match] = teams[match + leagueConfiguration.getTeamsPerConference()];
                    data[day].home_teams[match] = teams[match];
                } else {

                    data[day].road_teams[match] = teams[match];
                    data[day].home_teams[match] = teams[match + leagueConfiguration.getTeamsPerConference()];
                }
            }

            rotate(teams, leagueConfiguration.getTeamsPerConference(), leagueConfiguration.getTeamsPerConference());
        }

        for (int i = 0; i < leagueConfiguration.getDaysInPreseason(); ++i) {

            schedule.add(data[i].toString());
        }

        Collections.shuffle(schedule);

        return schedule;
    }

    // TODO: bad idea
    private Boolean flip;
    private Boolean invert;

    private List<String> generateRegularSeasonSchedule() {

        final List<String>   schedule = new ArrayList<>();
        final ScheduleData[] data     = new ScheduleData[leagueConfiguration.getDaysInRegularSeason()];
        final int[]          teams    = new int[leagueConfiguration.getNumberOfTeams()];


        for ( int i = 0; i < leagueConfiguration.getNumberOfTeams(); ++i ) teams[i] = i + 1;

        int day = 0;

        // Conference Series
        shuffle( teams,  0, leagueConfiguration.getTeamsPerConference() );
        shuffle( teams, 20, leagueConfiguration.getTeamsPerConference() );

        this.flip = false;

        for ( int series = 0; series < leagueConfiguration.getTeamsPerConference() - 1; ++series ) {

            this.invert = false;

            for ( int round = 0; round < 3; ++round ) {

                data[day] = new ScheduleData();

                data[day].games = leagueConfiguration.getGamesPerDay();

                scheduleConferenceGames( data[day], teams );

                day++;

                this.invert = ! this.invert;
            }

            rotate( teams,  1, leagueConfiguration.getTeamsPerConference() - 1 );
            rotate( teams, 21, leagueConfiguration.getTeamsPerConference() - 1 );
        }


        // After conference schedule team list has the form with road/home bias:
        //  Div: 1111111111 2222222222 3333333333 4444444444
        // Conf: 1111111111 1111111111 2222222222 2222222222
        // Bias: rhrhrhrhrh rhrhrhrhrh rhrhrhrhrh rhrhrhrhrh

        // Out of Conference Series
        for ( int round = 0; round < leagueConfiguration.getOutOfConferenceGames(); ++round ) {

            data[day] = new ScheduleData();

            data[day].games = leagueConfiguration.getGamesPerDay();

            for ( int match = 0; match < leagueConfiguration.getGamesPerDay(); ++match ) {

                if ( (round % 2) != 0 ) {

                    data[day].road_teams[match] = teams[match];
                    data[day].home_teams[match] = teams[39 - match];
                }
                else {

                    if ( round == 0  &&  (match % 2) != 0 ) {

                        data[day].road_teams[match] = teams[match];
                        data[day].home_teams[match] = teams[39 - match];
                    }
                    else {

                        data[day].road_teams[match] = teams[39 - match];
                        data[day].home_teams[match] = teams[match];
                    }
                }
            }

            day ++;

            rotate( teams,  20, leagueConfiguration.getTeamsPerConference() );
        }

        // Division Series
        for ( int i = 0; i < leagueConfiguration.getNumberOfTeams(); ++i ) teams[i] = i + 1;

        shuffle( teams,  0, leagueConfiguration.getTeamsPerDivision() );
        shuffle( teams, 10, leagueConfiguration.getTeamsPerDivision() );
        shuffle( teams, 20, leagueConfiguration.getTeamsPerDivision() );
        shuffle( teams, 30, leagueConfiguration.getTeamsPerDivision() );

        this.flip = false;

        for ( int series = 0; series < leagueConfiguration.getTeamsPerDivision() - 1; ++series ) {

            for ( int round = 0; round < 4; ++round ) {

                data[day] = new ScheduleData();

                data[day].games = leagueConfiguration.getGamesPerDay();

                scheduleDivisionGames( data, day, teams );

                day++;
            }

            rotate( teams,  1, leagueConfiguration.getTeamsPerDivision() - 1 );
            rotate( teams, 11, leagueConfiguration.getTeamsPerDivision() - 1 );
            rotate( teams, 21, leagueConfiguration.getTeamsPerDivision() - 1 );
            rotate( teams, 31, leagueConfiguration.getTeamsPerDivision() - 1 );
        }


        for ( int i = 0; i < leagueConfiguration.getDaysInRegularSeason(); ++i ) {

            schedule.add(data[i].toString());
        }

        Collections.shuffle( schedule );

        return schedule;
    }

    private void scheduleConferenceGames(final ScheduleData data, final int[] teams ) {

        for ( int i = 0; i < leagueConfiguration.getNumberOfTeams(); i += leagueConfiguration.getTeamsPerConference() )
        {
            for ( int j = 0; j < 10; ++j )
            {
                final int match = (i / 2) + j;
                int road  = i + j;
                int home  = i + 19 - j;

                // Swap road and home for first team (since it doesn't rotate)
                if ( j == 0 && this.flip ) {

                    final int temp;

                    temp = road;
                    road = home;
                    home = temp;
                }

                // Every other pass swap home and road teams (so some teams don't play every game at home)
                if ( j != 0 && this.invert ) {

                    final int temp;

                    temp = road;
                    road = home;
                    home = temp;
                }

                data.road_teams[match] = teams[road];
                data.home_teams[match] = teams[home];
            }
        }

        this.flip = ! this.flip;
    }

    private void scheduleDivisionGames(final ScheduleData[] data, final int day, final int[] teams ) {

        for ( int i = 0; i < leagueConfiguration.getNumberOfTeams(); i += leagueConfiguration.getTeamsPerDivision() )
        {
            for ( int j = 0; j < 5; ++j )
            {
                final int match = (i / 2) + j;
                int road  = i + j;
                int home  = i + 9 - j;

                boolean done = false;

                for ( int previous_day = day - 1; previous_day > 0; --previous_day ) {

                    for ( int game = 0; game < data[previous_day].games; ++game ) {

                        if (    data[previous_day].road_teams[game] == teams[home] &&
                                data[previous_day].home_teams[game] == teams[road]    ) {

                            done = true;

                            break;
                        }

                        if (    data[previous_day].road_teams[game] == teams[road] &&
                                data[previous_day].home_teams[game] == teams[home]    ) {

                            final int temp;

                            temp = road;
                            road = home;
                            home = temp;

                            done = true;

                            break;
                        }
                    }

                    if ( done ) break;
                }

                data[day].road_teams[match] = teams[road];
                data[day].home_teams[match] = teams[home];
            }
        }

        this.flip = ! this.flip;
    }

    private static class ScheduleData {
        public int games;
        public int[] home_teams;
        public int[] road_teams;

        public ScheduleData() {
            this.games = 0;
            this.home_teams = new int[20];
            this.road_teams = new int[20];
        }

        public String toString() {
            final char[] array = new char[41];
            int idx = 0;

            array[idx] = (char) this.games;
            array[idx] += '0';

            idx++;

            for (int i = 0; i < this.games; ++i) {
                array[idx] = (char) this.road_teams[i];
                array[idx] += '0';

                idx++;

                array[idx] = (char) this.home_teams[i];
                array[idx] += '0';

                idx++;
            }

            return new String(array);
        }
    }

    private void rotate(final int[] array, final int offset, final int length) {
        int i;
        final int x = array[(offset + length) - 1];

        for (i = (offset + length) - 1; i > offset; --i) {
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
}
