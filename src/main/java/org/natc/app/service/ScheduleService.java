package org.natc.app.service;

import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        throw new UnsupportedOperationException("Method Not Implemented Yet");
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
}
