package org.natc.app.processor;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.Player;
import org.natc.app.entity.domain.Schedule;
import org.natc.app.entity.domain.ScheduleStatus;
import org.natc.app.entity.domain.ScheduleType;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.PlayerRepository;
import org.natc.app.repository.ScheduleRepository;
import org.natc.app.service.NATCServiceIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainingCampScheduleProcessorIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TrainingCampScheduleProcessor processor;

    @Nested
    class Process {
        @Test
        void shouldUpdateTheGivenScheduleStatusToCompleted() throws NATCException {
            final Schedule schedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.TRAINING_CAMP.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            scheduleRepository.save(schedule);

            processor.process(schedule);

            final List<Schedule> scheduleList = scheduleRepository.findAll();

            assertEquals(1, scheduleList.size());
            assertEquals(ScheduleStatus.COMPLETED.getValue(), scheduleList.getFirst().getStatus());
        }

        @Test
        void shouldIncreasePlayerAgesByOne() throws NATCException {
            final Schedule schedule = Schedule.builder()
                    .year("2005")
                    .sequence(1)
                    .type(ScheduleType.TRAINING_CAMP.getValue())
                    .status(ScheduleStatus.IN_PROGRESS.getValue())
                    .scheduled(LocalDate.now())
                    .build();

            scheduleRepository.save(schedule);

            final List<Player> playersBefore = List.of(
                    Player.builder().playerId(1).year("1997").age(25).build(),
                    Player.builder().playerId(1).year("1997").age(25).build(),
                    Player.builder().playerId(1).year("1997").age(25).build(),
                    Player.builder().playerId(1).year("2005").age(25).build(),
                    Player.builder().playerId(1).year("2005").age(25).build(),
                    Player.builder().playerId(1).year("2005").age(25).build(),
                    Player.builder().playerId(1).year("2005").age(25).build(),
                    Player.builder().playerId(1).year("2011").age(25).build(),
                    Player.builder().playerId(1).year("2011").age(25).build(),
                    Player.builder().playerId(1).year("2011").age(25).build()
            );

            playerRepository.saveAll(playersBefore);

            processor.process(schedule);

            final List<Player> playersAfter = playerRepository.findAll();

            for (final Player player : playersAfter) {
                if ("2005".equals(player.getYear())) {
                    assertEquals(26, player.getAge());
                }
                else {
                    assertEquals(25, player.getAge());
                }
            }
        }
    }
}