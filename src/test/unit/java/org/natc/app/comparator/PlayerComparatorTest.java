package org.natc.app.comparator;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.Player;
import org.natc.app.entity.domain.PlayerRatingAdjustment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.natc.app.comparator.PlayerComparator.PlayerComparatorMode.*;
import static org.natc.app.entity.domain.PlayerRatingAdjustment.*;

@ExtendWith(MockitoExtension.class)
class PlayerComparatorTest {

    @Nested
    class Compare {

        @Nested
        class OffensiveMode {
            @Test
            void shouldGetAdjustedOffensiveRatingsWhenInOffensiveMode() {
                final PlayerComparator playerComparator = new PlayerComparator(OFFENSIVE);
                final Player player1 = mock(Player.class);
                final Player player2 = mock(Player.class);

                playerComparator.compare(player1, player2);

                verify(player1).getAdjustedOffensiveRating(any(PlayerRatingAdjustment[].class));
                verify(player2).getAdjustedOffensiveRating(any(PlayerRatingAdjustment[].class));
            }

            @Test
            void shouldPassTheAdjustmentsToTheRatingsMethodWhenInOffensiveMode() {
                final PlayerRatingAdjustment[] adjustments = {APPLY_AGE, APPLY_CONFIDENCE};
                final PlayerComparator playerComparator = new PlayerComparator(OFFENSIVE, adjustments);
                final Player player1 = mock(Player.class);
                final Player player2 = mock(Player.class);

                playerComparator.compare(player1, player2);

                verify(player1).getAdjustedOffensiveRating(adjustments);
                verify(player2).getAdjustedOffensiveRating(adjustments);
            }

            @Test
            void shouldReturnGreaterThanZeroWhenPlayerOneHasHigherOffensiveRatingsInOffensiveMode() {
                final PlayerComparator playerComparator = new PlayerComparator(OFFENSIVE);
                final Player player1 = Player.builder()
                        .scoring(0.9)
                        .passing(0.9)
                        .blocking(0.9)
                        .tackling(0.1)
                        .stealing(0.1)
                        .presence(0.1)
                        .discipline(0.1)
                        .endurance(0.1)
                        .penaltyShot(0.1)
                        .penaltyOffense(0.1)
                        .penaltyDefense(0.1)
                        .build();
                final Player player2 = Player.builder()
                        .scoring(0.1)
                        .passing(0.1)
                        .blocking(0.1)
                        .tackling(0.9)
                        .stealing(0.9)
                        .presence(0.9)
                        .discipline(0.9)
                        .endurance(0.9)
                        .penaltyShot(0.9)
                        .penaltyOffense(0.9)
                        .penaltyDefense(0.9)
                        .build();

                assertTrue(playerComparator.compare(player1, player2) > 0);
            }

            @Test
            void shouldReturnLessThanZeroWhenPlayerOneHasLowerOffensiveRatingsInOffensiveMode() {
                final PlayerComparator playerComparator = new PlayerComparator(OFFENSIVE);
                final Player player1 = Player.builder()
                        .scoring(0.1)
                        .passing(0.1)
                        .blocking(0.1)
                        .tackling(0.9)
                        .stealing(0.9)
                        .presence(0.9)
                        .discipline(0.9)
                        .endurance(0.9)
                        .penaltyShot(0.9)
                        .penaltyOffense(0.9)
                        .penaltyDefense(0.9)
                        .build();
                final Player player2 = Player.builder()
                        .scoring(0.9)
                        .passing(0.9)
                        .blocking(0.9)
                        .tackling(0.1)
                        .stealing(0.1)
                        .presence(0.1)
                        .discipline(0.1)
                        .endurance(0.1)
                        .penaltyShot(0.1)
                        .penaltyOffense(0.1)
                        .penaltyDefense(0.1)
                        .build();

                assertTrue(playerComparator.compare(player1, player2) < 0);
            }

            @Test
            void shouldReturnZeroWhenPlayersHaveSameOffensiveRatingsInOffensiveMode() {
                final PlayerComparator playerComparator = new PlayerComparator(OFFENSIVE);
                final Player player1 = Player.builder()
                        .scoring(0.3)
                        .passing(0.3)
                        .blocking(0.3)
                        .tackling(0.9)
                        .stealing(0.9)
                        .presence(0.9)
                        .discipline(0.9)
                        .endurance(0.9)
                        .penaltyShot(0.9)
                        .penaltyOffense(0.9)
                        .penaltyDefense(0.9)
                        .build();
                final Player player2 = Player.builder()
                        .scoring(0.3)
                        .passing(0.3)
                        .blocking(0.3)
                        .tackling(0.1)
                        .stealing(0.1)
                        .presence(0.1)
                        .discipline(0.1)
                        .endurance(0.1)
                        .penaltyShot(0.1)
                        .penaltyOffense(0.1)
                        .penaltyDefense(0.1)
                        .build();

                assertEquals(0, playerComparator.compare(player1, player2));
            }
        }

        @Nested
        class DefensiveMode {
            @Test
            void shouldGetAdjustedDefensiveRatingsWhenInDefensiveMode() {
                final PlayerComparator playerComparator = new PlayerComparator(DEFENSIVE);
                final Player player1 = mock(Player.class);
                final Player player2 = mock(Player.class);

                playerComparator.compare(player1, player2);

                verify(player1).getAdjustedDefensiveRating(any(PlayerRatingAdjustment[].class));
                verify(player2).getAdjustedDefensiveRating(any(PlayerRatingAdjustment[].class));
            }

            @Test
            void shouldPassTheAdjustmentsToTheRatingsMethodWhenInDefensiveMode() {
                final PlayerRatingAdjustment[] adjustments = {APPLY_CONFIDENCE, APPLY_FATIGUE};
                final PlayerComparator playerComparator = new PlayerComparator(DEFENSIVE, adjustments);
                final Player player1 = mock(Player.class);
                final Player player2 = mock(Player.class);

                playerComparator.compare(player1, player2);

                verify(player1).getAdjustedDefensiveRating(adjustments);
                verify(player2).getAdjustedDefensiveRating(adjustments);
            }

            @Test
            void shouldReturnGreaterThanZeroWhenPlayerOneHasHigherDefensiveRatingsInDefensiveMode() {
                final PlayerComparator playerComparator = new PlayerComparator(DEFENSIVE);
                final Player player1 = Player.builder()
                        .scoring(0.1)
                        .passing(0.1)
                        .blocking(0.1)
                        .tackling(0.9)
                        .stealing(0.9)
                        .presence(0.9)
                        .discipline(0.1)
                        .endurance(0.1)
                        .penaltyShot(0.1)
                        .penaltyOffense(0.1)
                        .penaltyDefense(0.1)
                        .build();
                final Player player2 = Player.builder()
                        .scoring(0.9)
                        .passing(0.9)
                        .blocking(0.9)
                        .tackling(0.1)
                        .stealing(0.1)
                        .presence(0.1)
                        .discipline(0.9)
                        .endurance(0.9)
                        .penaltyShot(0.9)
                        .penaltyOffense(0.9)
                        .penaltyDefense(0.9)
                        .build();

                assertTrue(playerComparator.compare(player1, player2) > 0);
            }

            @Test
            void shouldReturnLessThanZeroWhenPlayerOneHasLowerDefensiveRatingsInDefensiveMode() {
                final PlayerComparator playerComparator = new PlayerComparator(DEFENSIVE);
                final Player player1 = Player.builder()
                        .scoring(0.9)
                        .passing(0.9)
                        .blocking(0.9)
                        .tackling(0.1)
                        .stealing(0.1)
                        .presence(0.1)
                        .discipline(0.9)
                        .endurance(0.9)
                        .penaltyShot(0.9)
                        .penaltyOffense(0.9)
                        .penaltyDefense(0.9)
                        .build();
                final Player player2 = Player.builder()
                        .scoring(0.1)
                        .passing(0.1)
                        .blocking(0.1)
                        .tackling(0.9)
                        .stealing(0.9)
                        .presence(0.9)
                        .discipline(0.1)
                        .endurance(0.1)
                        .penaltyShot(0.1)
                        .penaltyOffense(0.1)
                        .penaltyDefense(0.1)
                        .build();

                assertTrue(playerComparator.compare(player1, player2) < 0);
            }

            @Test
            void shouldReturnZeroWhenPlayersHaveSameDefensiveRatingsInDefensiveMode() {
                final PlayerComparator playerComparator = new PlayerComparator(DEFENSIVE);
                final Player player1 = Player.builder()
                        .scoring(0.9)
                        .passing(0.9)
                        .blocking(0.9)
                        .tackling(0.3)
                        .stealing(0.3)
                        .presence(0.3)
                        .discipline(0.9)
                        .endurance(0.9)
                        .penaltyShot(0.9)
                        .penaltyOffense(0.9)
                        .penaltyDefense(0.9)
                        .build();
                final Player player2 = Player.builder()
                        .scoring(0.1)
                        .passing(0.1)
                        .blocking(0.1)
                        .tackling(0.3)
                        .stealing(0.3)
                        .presence(0.3)
                        .discipline(0.1)
                        .endurance(0.1)
                        .penaltyShot(0.1)
                        .penaltyOffense(0.1)
                        .penaltyDefense(0.1)
                        .build();

                assertEquals(0, playerComparator.compare(player1, player2));
            }
        }

        @Nested
        class IntangibleMode {
            @Test
            void shouldGetAdjustedIntangibleRatingsWhenInIntangibleMode() {
                final PlayerComparator playerComparator = new PlayerComparator(INTANGIBLE);
                final Player player1 = mock(Player.class);
                final Player player2 = mock(Player.class);

                playerComparator.compare(player1, player2);

                verify(player1).getAdjustedIntangibleRating(any(PlayerRatingAdjustment[].class));
                verify(player2).getAdjustedIntangibleRating(any(PlayerRatingAdjustment[].class));
            }

            @Test
            void shouldPassTheAdjustmentsToTheRatingsMethodWhenInIntangibleMode() {
                final PlayerRatingAdjustment[] adjustments = {APPLY_AGE, APPLY_FATIGUE};
                final PlayerComparator playerComparator = new PlayerComparator(INTANGIBLE, adjustments);
                final Player player1 = mock(Player.class);
                final Player player2 = mock(Player.class);

                playerComparator.compare(player1, player2);

                verify(player1).getAdjustedIntangibleRating(adjustments);
                verify(player2).getAdjustedIntangibleRating(adjustments);
            }

            @Test
            void shouldReturnGreaterThanZeroWhenPlayerOneHasHigherIntangibleRatingsInIntangibleMode() {
                final PlayerComparator playerComparator = new PlayerComparator(INTANGIBLE);
                final Player player1 = Player.builder()
                        .scoring(0.1)
                        .passing(0.1)
                        .blocking(0.9)
                        .tackling(0.1)
                        .stealing(0.1)
                        .presence(0.9)
                        .discipline(0.9)
                        .endurance(0.9)
                        .penaltyShot(0.1)
                        .penaltyOffense(0.1)
                        .penaltyDefense(0.1)
                        .build();
                final Player player2 = Player.builder()
                        .scoring(0.9)
                        .passing(0.9)
                        .blocking(0.1)
                        .tackling(0.9)
                        .stealing(0.9)
                        .presence(0.1)
                        .discipline(0.1)
                        .endurance(0.1)
                        .penaltyShot(0.9)
                        .penaltyOffense(0.9)
                        .penaltyDefense(0.9)
                        .build();

                assertTrue(playerComparator.compare(player1, player2) > 0);
            }

            @Test
            void shouldReturnLessThanZeroWhenPlayerOneHasLowerIntangibleRatingsInIntangibleMode() {
                final PlayerComparator playerComparator = new PlayerComparator(INTANGIBLE);
                final Player player1 = Player.builder()
                        .scoring(0.9)
                        .passing(0.9)
                        .blocking(0.1)
                        .tackling(0.9)
                        .stealing(0.9)
                        .presence(0.1)
                        .discipline(0.1)
                        .endurance(0.1)
                        .penaltyShot(0.9)
                        .penaltyOffense(0.9)
                        .penaltyDefense(0.9)
                        .build();
                final Player player2 = Player.builder()
                        .scoring(0.1)
                        .passing(0.1)
                        .blocking(0.9)
                        .tackling(0.1)
                        .stealing(0.1)
                        .presence(0.9)
                        .discipline(0.9)
                        .endurance(0.9)
                        .penaltyShot(0.1)
                        .penaltyOffense(0.1)
                        .penaltyDefense(0.1)
                        .build();

                assertTrue(playerComparator.compare(player1, player2) < 0);
            }

            @Test
            void shouldReturnZeroWhenPlayersHaveSameIntangibleRatingsInIntangibleMode() {
                final PlayerComparator playerComparator = new PlayerComparator(INTANGIBLE);
                final Player player1 = Player.builder()
                        .scoring(0.9)
                        .passing(0.9)
                        .blocking(0.3)
                        .tackling(0.9)
                        .stealing(0.9)
                        .presence(0.3)
                        .discipline(0.3)
                        .endurance(0.3)
                        .penaltyShot(0.9)
                        .penaltyOffense(0.9)
                        .penaltyDefense(0.9)
                        .build();
                final Player player2 = Player.builder()
                        .scoring(0.1)
                        .passing(0.1)
                        .blocking(0.3)
                        .tackling(0.1)
                        .stealing(0.1)
                        .presence(0.3)
                        .discipline(0.3)
                        .endurance(0.3)
                        .penaltyShot(0.1)
                        .penaltyOffense(0.1)
                        .penaltyDefense(0.1)
                        .build();

                assertEquals(0, playerComparator.compare(player1, player2));
            }
        }

        @Nested
        class PenaltyMode {
            @Test
            void shouldGetAdjustedPenaltyRatingsWhenInPenaltyMode() {
                final PlayerComparator playerComparator = new PlayerComparator(PENALTIES);
                final Player player1 = mock(Player.class);
                final Player player2 = mock(Player.class);

                playerComparator.compare(player1, player2);

                verify(player1).getAdjustedPenaltyRating(any(PlayerRatingAdjustment[].class));
                verify(player2).getAdjustedPenaltyRating(any(PlayerRatingAdjustment[].class));
            }

            @Test
            void shouldPassTheAdjustmentsToTheRatingsMethodWhenInPenaltyMode() {
                final PlayerRatingAdjustment[] adjustments = {APPLY_AGE, APPLY_CONFIDENCE, APPLY_FATIGUE};
                final PlayerComparator playerComparator = new PlayerComparator(PENALTIES, adjustments);
                final Player player1 = mock(Player.class);
                final Player player2 = mock(Player.class);

                playerComparator.compare(player1, player2);

                verify(player1).getAdjustedPenaltyRating(adjustments);
                verify(player2).getAdjustedPenaltyRating(adjustments);
            }

            @Test
            void shouldReturnGreaterThanZeroWhenPlayerOneHasHigherPenaltyRatingsInPenaltyMode() {
                final PlayerComparator playerComparator = new PlayerComparator(PENALTIES);
                final Player player1 = Player.builder()
                        .scoring(0.1)
                        .passing(0.1)
                        .blocking(0.1)
                        .tackling(0.1)
                        .stealing(0.1)
                        .presence(0.1)
                        .discipline(0.1)
                        .endurance(0.1)
                        .penaltyShot(0.9)
                        .penaltyOffense(0.9)
                        .penaltyDefense(0.9)
                        .build();
                final Player player2 = Player.builder()
                        .scoring(0.9)
                        .passing(0.9)
                        .blocking(0.9)
                        .tackling(0.9)
                        .stealing(0.9)
                        .presence(0.9)
                        .discipline(0.9)
                        .endurance(0.9)
                        .penaltyShot(0.1)
                        .penaltyOffense(0.1)
                        .penaltyDefense(0.1)
                        .build();

                assertTrue(playerComparator.compare(player1, player2) > 0);
            }

            @Test
            void shouldReturnLessThanZeroWhenPlayerOneHasLowerPenaltyRatingsInPenaltyMode() {
                final PlayerComparator playerComparator = new PlayerComparator(PENALTIES);
                final Player player1 = Player.builder()
                        .scoring(0.9)
                        .passing(0.9)
                        .blocking(0.9)
                        .tackling(0.9)
                        .stealing(0.9)
                        .presence(0.9)
                        .discipline(0.9)
                        .endurance(0.9)
                        .penaltyShot(0.1)
                        .penaltyOffense(0.1)
                        .penaltyDefense(0.1)
                        .build();
                final Player player2 = Player.builder()
                        .scoring(0.1)
                        .passing(0.1)
                        .blocking(0.1)
                        .tackling(0.1)
                        .stealing(0.1)
                        .presence(0.1)
                        .discipline(0.1)
                        .endurance(0.1)
                        .penaltyShot(0.9)
                        .penaltyOffense(0.9)
                        .penaltyDefense(0.9)
                        .build();

                assertTrue(playerComparator.compare(player1, player2) < 0);
            }

            @Test
            void shouldZeroWhenPlayersHaveSamePenaltyRatingsInPenaltyMode() {
                final PlayerComparator playerComparator = new PlayerComparator(PENALTIES);
                final Player player1 = Player.builder()
                        .scoring(0.9)
                        .passing(0.9)
                        .blocking(0.9)
                        .tackling(0.9)
                        .stealing(0.9)
                        .presence(0.9)
                        .discipline(0.9)
                        .endurance(0.9)
                        .penaltyShot(0.3)
                        .penaltyOffense(0.3)
                        .penaltyDefense(0.3)
                        .build();
                final Player player2 = Player.builder()
                        .scoring(0.1)
                        .passing(0.1)
                        .blocking(0.1)
                        .tackling(0.1)
                        .stealing(0.1)
                        .presence(0.1)
                        .discipline(0.1)
                        .endurance(0.1)
                        .penaltyShot(0.3)
                        .penaltyOffense(0.3)
                        .penaltyDefense(0.3)
                        .build();

                assertEquals(0, playerComparator.compare(player1, player2));
            }
        }

        @Nested
        class BalancedMode {
            @Test
            void shouldGetAdjustedPerformanceRatingsWhenInBalancedMode() {
                final PlayerComparator playerComparator = new PlayerComparator(BALANCED);
                final Player player1 = mock(Player.class);
                final Player player2 = mock(Player.class);

                playerComparator.compare(player1, player2);

                verify(player1).getAdjustedPerformanceRating(any(PlayerRatingAdjustment[].class));
                verify(player2).getAdjustedPerformanceRating(any(PlayerRatingAdjustment[].class));
            }

            @Test
            void shouldPassTheAdjustmentsToTheRatingsMethodWhenInBalancedMode() {
                final PlayerRatingAdjustment[] adjustments = {APPLY_AGE};
                final PlayerComparator playerComparator = new PlayerComparator(BALANCED, adjustments);
                final Player player1 = mock(Player.class);
                final Player player2 = mock(Player.class);

                playerComparator.compare(player1, player2);

                verify(player1).getAdjustedPerformanceRating(adjustments);
                verify(player2).getAdjustedPerformanceRating(adjustments);
            }

            @Test
            void shouldReturnGreaterThanZeroWhenPlayerOneHasHigherPerformanceRatingsInBalancedMode() {
                final PlayerComparator playerComparator = new PlayerComparator(BALANCED);
                final Player player1 = Player.builder()
                        .scoring(0.9)
                        .passing(0.9)
                        .blocking(0.9)
                        .tackling(0.9)
                        .stealing(0.9)
                        .presence(0.9)
                        .discipline(0.9)
                        .endurance(0.9)
                        .penaltyShot(0.1)
                        .penaltyOffense(0.1)
                        .penaltyDefense(0.1)
                        .build();
                final Player player2 = Player.builder()
                        .scoring(0.1)
                        .passing(0.1)
                        .blocking(0.1)
                        .tackling(0.1)
                        .stealing(0.1)
                        .presence(0.1)
                        .discipline(0.1)
                        .endurance(0.1)
                        .penaltyShot(0.9)
                        .penaltyOffense(0.9)
                        .penaltyDefense(0.9)
                        .build();

                assertTrue(playerComparator.compare(player1, player2) > 0);
            }

            @Test
            void shouldReturnLessThanZeroWhenPlayerOneHasLowerPerformanceRatingsInBalancedMode() {
                final PlayerComparator playerComparator = new PlayerComparator(BALANCED);
                final Player player1 = Player.builder()
                        .scoring(0.1)
                        .passing(0.1)
                        .blocking(0.1)
                        .tackling(0.1)
                        .stealing(0.1)
                        .presence(0.1)
                        .discipline(0.1)
                        .endurance(0.1)
                        .penaltyShot(0.9)
                        .penaltyOffense(0.9)
                        .penaltyDefense(0.9)
                        .build();
                final Player player2 = Player.builder()
                        .scoring(0.9)
                        .passing(0.9)
                        .blocking(0.9)
                        .tackling(0.9)
                        .stealing(0.9)
                        .presence(0.9)
                        .discipline(0.9)
                        .endurance(0.9)
                        .penaltyShot(0.1)
                        .penaltyOffense(0.1)
                        .penaltyDefense(0.1)
                        .build();

                assertTrue(playerComparator.compare(player1, player2) < 0);
            }

            @Test
            void shouldReturnZeroWhenPlayersHaveSamePerformanceRatingsInBalancedMode() {
                final PlayerComparator playerComparator = new PlayerComparator(BALANCED);
                final Player player1 = Player.builder()
                        .scoring(0.3)
                        .passing(0.3)
                        .blocking(0.3)
                        .tackling(0.3)
                        .stealing(0.3)
                        .presence(0.3)
                        .discipline(0.3)
                        .endurance(0.3)
                        .penaltyShot(0.9)
                        .penaltyOffense(0.9)
                        .penaltyDefense(0.9)
                        .build();
                final Player player2 = Player.builder()
                        .scoring(0.3)
                        .passing(0.3)
                        .blocking(0.3)
                        .tackling(0.3)
                        .stealing(0.3)
                        .presence(0.3)
                        .discipline(0.3)
                        .endurance(0.3)
                        .penaltyShot(0.1)
                        .penaltyOffense(0.1)
                        .penaltyDefense(0.1)
                        .build();

                assertEquals(0, playerComparator.compare(player1, player2));
            }
        }
    }
}