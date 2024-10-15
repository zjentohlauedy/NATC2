package org.natc.app.controller.testing;

import org.natc.app.exception.NATCException;
import org.natc.app.manager.SeasonManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Profile("test")
@RequestMapping("/api/__testing")
public class ScheduleEventController {

    private final SeasonManager seasonManager;

    @Autowired
    public ScheduleEventController(final SeasonManager seasonManager) {
        this.seasonManager = seasonManager;
    }

    @RequestMapping("/schedule-event")
    public ResponseEntity<String> execute() throws NATCException {
        seasonManager.processScheduledEvent();

        return ResponseEntity.ok("Event scheduled successfully!");
    }
}
