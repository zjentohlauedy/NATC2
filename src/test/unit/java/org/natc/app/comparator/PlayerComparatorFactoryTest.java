package org.natc.app.comparator;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.ManagerStyle;
import org.natc.app.entity.domain.PlayerRatingAdjustment;

import static org.junit.jupiter.api.Assertions.*;
import static org.natc.app.comparator.PlayerComparator.PlayerComparatorMode.*;
import static org.natc.app.entity.domain.PlayerRatingAdjustment.*;

@ExtendWith(MockitoExtension.class)
class PlayerComparatorFactoryTest {

    @Nested
    class GetPlayerComparatorForManager {

        @Test
        void shouldReturnAPlayerComparator() {
            final PlayerComparatorFactory factory = new PlayerComparatorFactory();

            final PlayerComparator playerComparator = factory.getPlayerComparatorForManager(ManagerStyle.OFFENSIVE);

            assertNotNull(playerComparator);
        }

        @Test
        void shouldSetPlayerComparatorModeBasedOnGivenManagerStyle() {
            final PlayerComparatorFactory factory = new PlayerComparatorFactory();

            assertEquals(OFFENSIVE, factory.getPlayerComparatorForManager(ManagerStyle.OFFENSIVE).getMode());
            assertEquals(DEFENSIVE, factory.getPlayerComparatorForManager(ManagerStyle.DEFENSIVE).getMode());
            assertEquals(INTANGIBLE, factory.getPlayerComparatorForManager(ManagerStyle.INTANGIBLE).getMode());
            assertEquals(PENALTY, factory.getPlayerComparatorForManager(ManagerStyle.PENALTIES).getMode());
            assertEquals(BALANCED, factory.getPlayerComparatorForManager(ManagerStyle.BALANCED).getMode());
        }

        @Test
        void shouldSetPlayerComparatorAdjustmentsFromGivenAdjustments() {
            final PlayerComparatorFactory factory = new PlayerComparatorFactory();
            final PlayerRatingAdjustment[] adjustments = {APPLY_AGE, APPLY_CONFIDENCE, APPLY_FATIGUE};

            final PlayerComparator playerComparator = factory.getPlayerComparatorForManager(ManagerStyle.OFFENSIVE, adjustments);

            assertEquals(adjustments, playerComparator.getAdjustments());
        }
    }
}