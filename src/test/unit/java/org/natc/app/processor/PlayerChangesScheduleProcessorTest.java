package org.natc.app.processor;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.Player;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.exception.NATCException;
import org.natc.app.service.ManagerService;
import org.natc.app.service.PlayerService;
import org.natc.app.service.ScheduleService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerChangesScheduleProcessorTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private ManagerService managerService;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private PlayerChangesScheduleProcessor processor;

    @Nested
    class Process {

        @Test
        void shouldCallTheScheduleServiceToUpdateTheScheduleEntry() throws NATCException {
            processor.process(Schedule.builder().year("2005").build());

            verify(scheduleService).updateScheduleEntry(any());
        }

        @Test
        void shouldUpdateTheScheduleEntryStatusToCompleted() throws NATCException {
            final Schedule schedule = Schedule.builder().year("2001").sequence(1).status(ScheduleStatus.IN_PROGRESS.getValue()).build();
            final ArgumentCaptor<Schedule> captor = ArgumentCaptor.forClass(Schedule.class);

            processor.process(schedule);

            verify(scheduleService).updateScheduleEntry(captor.capture());

            assertSame(schedule, captor.getValue());
            assertEquals(ScheduleStatus.COMPLETED.getValue(), captor.getValue().getStatus());
        }

        @Test
        void shouldCallPlayerServiceToGetActivePlayersForSameYearAsScheduledEvent() throws NATCException {
            processor.process(Schedule.builder().year("2012").build());

            verify(playerService).getActivePlayersForYear("2012");
        }

        @Test
        void shouldCallManagerServiceToGetActiveManagersForSameYearAsScheduledEvent() throws NATCException {
            processor.process(Schedule.builder().year("2012").build());

            verify(managerService).getActiveManagersForYear("2012");
        }

        // should call ready to retire

        // should check each player to see if they are ready to retire

        // only check ready to retire if the player is on a team

        //---

        // should call should retire

        // should check each player to see if they should retire

        // only check should retire if the player is not on a team

        // TODO: released players should get checked twice, ready to retire then should retire

        @Test
        void shouldCallPlayerServiceToUpdatePlayersRetrievedForScheduledYear() throws NATCException {
            final List<Player> playerList = Arrays.asList(
                    Player.builder().playerId(1).year("2020").build(),
                    Player.builder().playerId(2).year("2020").build(),
                    Player.builder().playerId(3).year("2020").build()
            );

            when(playerService.getActivePlayersForYear("2020")).thenReturn(playerList);

            processor.process(Schedule.builder().year("2020").build());

            verify(playerService).updatePlayers(playerList);
        }
    }
}