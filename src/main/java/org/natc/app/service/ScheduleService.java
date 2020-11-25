package org.natc.app.service;

import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleData;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.generator.ScheduleDataGenerator;
import org.natc.app.manager.ScheduleDataGeneratorManager;
import org.natc.app.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ScheduleService {

    private final ScheduleRepository repository;
    private final LeagueConfiguration leagueConfiguration;
    private final ScheduleDataGeneratorManager scheduleDataGeneratorManager;

    @Autowired
    public ScheduleService(
            final ScheduleRepository repository,
            final LeagueConfiguration leagueConfiguration,
            final ScheduleDataGeneratorManager scheduleDataGeneratorManager
    ) {
        this.repository = repository;
        this.leagueConfiguration = leagueConfiguration;
        this.scheduleDataGeneratorManager = scheduleDataGeneratorManager;
    }

    public void generateSchedule(final String year) {
        final List<Schedule> scheduleList = new ArrayList<>();
        final int yearNum = Integer.parseInt(year);

        int sequence = 1;
        LocalDate scheduledDate = LocalDate.of(yearNum, Month.JANUARY, 1);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.BEGINNING_OF_SEASON.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
                .build());

        sequence++;
        scheduledDate = getDayOfWeekOnOrAfter(LocalDate.of(yearNum, Month.FEBRUARY, 1), DayOfWeek.MONDAY);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.MANAGER_CHANGES.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
                .build());

        sequence++;
        scheduledDate = LocalDate.of(yearNum, Month.FEBRUARY, 28);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.PLAYER_CHANGES.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
                .build());

        sequence++;
        scheduledDate = getDayOfWeekOnOrAfter(LocalDate.of(yearNum, Month.MARCH, 1), DayOfWeek.MONDAY);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.ROOKIE_DRAFT_ROUND_1.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
                .build());

        sequence++;
        scheduledDate = scheduledDate.plusDays(1);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.ROOKIE_DRAFT_ROUND_2.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
                .build());

        sequence++;
        scheduledDate = scheduledDate.plusDays(13);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.TRAINING_CAMP.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
                .build());

        sequence++;
        scheduledDate = getDayOfWeekOnOrAfter(LocalDate.of(yearNum, Month.APRIL, 1), DayOfWeek.MONDAY);
        scheduleList.addAll(generateScheduleDataBasedSchedule(sequence, scheduledDate, year, ScheduleType.PRESEASON));

        sequence = scheduleList.get(scheduleList.size() - 1).getSequence() + 1;
        scheduledDate = scheduleList.get(scheduleList.size() - 1).getScheduled().plusDays(1);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.END_OF_PRESEASON.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
                .build());

        sequence++;
        scheduledDate = LocalDate.of(yearNum, Month.APRIL, 30);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.ROSTER_CUT.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
                .build());

        sequence++;
        scheduledDate = getDayOfWeekOnOrAfter(LocalDate.of(yearNum, Month.MAY, 1), DayOfWeek.MONDAY);
        scheduleList.addAll(generateScheduleDataBasedSchedule(sequence, scheduledDate, year, ScheduleType.REGULAR_SEASON));

        sequence = scheduleList.get(scheduleList.size() - 1).getSequence() + 1;
        scheduledDate = scheduleList.get(scheduleList.size() - 1).getScheduled().plusDays(1);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.END_OF_REGULAR_SEASON.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
                .build());

        sequence++;
        scheduledDate = getDayOfWeekOnOrAfter(scheduledDate.plusDays(1), DayOfWeek.MONDAY);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.AWARDS.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
                .build());

        sequence++;
        scheduledDate = getDayOfWeekOnOrAfter(scheduledDate.plusDays(1), DayOfWeek.FRIDAY);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.POSTSEASON.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
                .build());

        sequence++;
        scheduledDate = getDayOfWeekOnOrAfter(scheduledDate.plusDays(1), DayOfWeek.SUNDAY);
        for (int i = 0; i < leagueConfiguration.getPlayoffGamesRoundOne(); ++i) {
            scheduleList.add(Schedule.builder()
                    .year(year)
                    .sequence(sequence)
                    .type(ScheduleType.DIVISION_PLAYOFF.getValue())
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .scheduled(scheduledDate)
                    .build());

            sequence++;
            scheduledDate = scheduledDate.plusDays(1);
        }

        scheduledDate = getDayOfWeekOnOrAfter(scheduledDate.plusDays(1), DayOfWeek.SUNDAY);
        for (int i = 0; i < leagueConfiguration.getPlayoffGamesRoundTwo(); ++i) {
            scheduleList.add(Schedule.builder()
                    .year(year)
                    .sequence(sequence)
                    .type(ScheduleType.DIVISION_CHAMPIONSHIP.getValue())
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .scheduled(scheduledDate)
                    .build());

            sequence++;
            scheduledDate = scheduledDate.plusDays(1);
        }

        scheduledDate = getDayOfWeekOnOrAfter(scheduledDate.plusDays(1), DayOfWeek.SUNDAY);
        for (int i = 0; i < leagueConfiguration.getPlayoffGamesRoundThree(); ++i) {
            scheduleList.add(Schedule.builder()
                    .year(year)
                    .sequence(sequence)
                    .type(ScheduleType.CONFERENCE_CHAMPIONSHIP.getValue())
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .scheduled(scheduledDate)
                    .build());

            sequence++;
            scheduledDate = scheduledDate.plusDays(1);
        }

        scheduledDate = getDayOfWeekOnOrAfter(scheduledDate.plusDays(1), DayOfWeek.SUNDAY);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.NATC_CHAMPIONSHIP.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
                .build());

        sequence++;
        scheduledDate = scheduledDate.plusDays(1);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.END_OF_POSTSEASON.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
                .build());

        sequence++;
        scheduledDate = scheduledDate.plusDays(1);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.ALL_STARS.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
                .build());

        sequence++;
        scheduledDate = getDayOfWeekOnOrAfter(scheduledDate.plusDays(1), DayOfWeek.SATURDAY);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.ALL_STAR_DAY_1.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
                .build());

        sequence++;
        scheduledDate = scheduledDate.plusDays(1);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.ALL_STAR_DAY_2.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
                .build());

        sequence++;
        scheduledDate = scheduledDate.plusDays(1);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.END_OF_ALLSTAR_GAMES.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
                .build());

        sequence++;
        scheduledDate = getDayOfWeekOnOrAfter(LocalDate.of(yearNum, Month.NOVEMBER, 1), DayOfWeek.MONDAY);
        scheduleList.add(Schedule.builder()
                .year(year)
                .sequence(sequence)
                .type(ScheduleType.END_OF_SEASON.getValue())
                .status(ScheduleStatus.SCHEDULED.getValue())
                .scheduled(scheduledDate)
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

    private List<Schedule> generateScheduleDataBasedSchedule(
            final int startSequence,
            final LocalDate startDate,
            final String year,
            final ScheduleType scheduleType
    ) {
        int sequence = startSequence;
        LocalDate scheduledDate = startDate;
        final List<Schedule> scheduleList = new ArrayList<>();

        final ScheduleDataGenerator regularSeasonGenerator = scheduleDataGeneratorManager.getGeneratorFor(scheduleType);
        final List<ScheduleData> regularSeasonScheduleData = regularSeasonGenerator.generate();

        for (final ScheduleData scheduleData : regularSeasonScheduleData) {
            if (scheduledDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) scheduledDate = scheduledDate.plusDays(2);

            scheduleList.add(Schedule.builder()
                    .year(year)
                    .sequence(sequence)
                    .type(scheduleType.getValue())
                    .status(ScheduleStatus.SCHEDULED.getValue())
                    .data(scheduleData.toString())
                    .scheduled(scheduledDate)
                    .build());

            sequence++;
            scheduledDate = scheduledDate.plusDays(1);
        }

        return scheduleList;
    }
}
