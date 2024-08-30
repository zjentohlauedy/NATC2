package org.natc.app.manager;

import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.exception.NATCException;
import org.natc.app.exception.ScheduleProcessingException;
import org.natc.app.processor.ScheduleProcessor;
import org.natc.app.service.LeagueService;
import org.natc.app.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@Component
public class SeasonManager {

    private final ScheduleService scheduleService;
    private final ScheduleProcessorManager scheduleProcessorManager;
    private final LeagueService leagueService;
    private final LeagueConfiguration leagueConfiguration;

    @Autowired
    public SeasonManager(final ScheduleService scheduleService, final ScheduleProcessorManager scheduleProcessorManager, final LeagueService leagueService, final LeagueConfiguration leagueConfiguration) {
        this.scheduleService = scheduleService;
        this.scheduleProcessorManager = scheduleProcessorManager;
        this.leagueService = leagueService;
        this.leagueConfiguration = leagueConfiguration;
    }

    public void processScheduledEvent() throws NATCException {
        if (scheduleService.getCurrentScheduleEntry() != null) return;

        final Schedule lastScheduleEntry = scheduleService.getLastScheduleEntry();

        if (Objects.isNull(lastScheduleEntry)) {
            leagueService.generateNewLeague();
            scheduleService.generateSchedule(leagueConfiguration.getFirstSeason());
        }
        else if (lastScheduleEntry.getType().equals(ScheduleType.END_OF_SEASON.getValue())) {
            final String nextYear = String.valueOf(Integer.parseInt(lastScheduleEntry.getYear()) + 1);

            leagueService.updateLeagueForNewSeason(lastScheduleEntry.getYear(), nextYear);
            scheduleService.generateSchedule(nextYear);
        }

        final Schedule nextScheduleEntry = scheduleService.getNextScheduleEntry(lastScheduleEntry);

        if (Objects.isNull(nextScheduleEntry)) throw new ScheduleProcessingException();

        if (nextScheduleEntry.getScheduled().isAfter(LocalDate.now())) return;

        nextScheduleEntry.setStatus(ScheduleStatus.IN_PROGRESS.getValue());

        scheduleService.updateScheduleEntry(nextScheduleEntry);

        final ScheduleProcessor scheduleProcessor = scheduleProcessorManager
                .getProcessorFor(ScheduleType.getByValue(nextScheduleEntry.getType()));

        scheduleProcessor.process(nextScheduleEntry);
    }
}
