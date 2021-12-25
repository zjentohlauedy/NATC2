package org.natc.app.processor;

import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.exception.NATCException;
import org.natc.app.service.ScheduleService;

public class PlayerChangesScheduleProcessor implements ScheduleProcessor {

    private final ScheduleService scheduleService;

    public PlayerChangesScheduleProcessor(final ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Override
    public void process(final Schedule schedule) throws NATCException {
        // load all players for scheduled year
        // load all managers for scheduled year that have a team id

        // iterate over players:
        // - skip if not on a team
        // - check for retirement
        //   - mark player retired

        // free agency:
        // - team list is configured team ids
        // - free agents is players list filtered by team_id is null and not retired
        // - team players is players list filtered by team_id is id from list and not retired
        // - each time a team is processed check of the manager is in the team-managers hash and pull that manager
        //   - if not, load the manager and add it
        // - same algorithm as old code for selecting players

        // after free agency:
        // - iterate over players without a team
        // - check for retirement
        //   - mark player retired

        schedule.setStatus(ScheduleStatus.COMPLETED.getValue());

        scheduleService.updateScheduleEntry(schedule);
    }
}
