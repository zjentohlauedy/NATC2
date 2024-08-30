package org.natc.app.entity.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.natc.app.entity.domain.Player.*;
import static org.natc.app.entity.domain.PlayerRatingAdjustment.*;
import static org.natc.app.util.BooleanHelper.valueOf;

class PlayerTest {

    @Nested
    class GetOffensiveRating {

        @Test
        void shouldReturnAverageOfScoringPassingAndBlockingRatings() {
            assertEquals(1.0,
                    Player.builder().scoring(1.0).passing(1.0).blocking(1.0).build().getOffensiveRating(),
                    0.0001
            );
            assertEquals(0.6,
                    Player.builder().scoring(0.8).passing(0.6).blocking(0.4).build().getOffensiveRating(),
                    0.0001
            );
            assertEquals(0.4,
                    Player.builder().scoring(0.7).passing(0.2).blocking(0.3).build().getOffensiveRating(),
                    0.0001
            );
            assertEquals(0.5666,
                    Player.builder().scoring(0.1).passing(0.8).blocking(0.8).build().getOffensiveRating(),
                    0.0001
            );
            assertEquals(0.333,
                    Player.builder().scoring(0.123).passing(0.321).blocking(0.555).build().getOffensiveRating(),
                    0.0001
            );
        }

        @Test
        void shouldReturnZeroIfAnyOffensiveRatingIsNull() {
            assertEquals(0, Player.builder().scoring(1.0).passing(1.0).blocking(null).build().getOffensiveRating());
            assertEquals(0, Player.builder().scoring(1.0).passing(null).blocking(1.0).build().getOffensiveRating());
            assertEquals(0, Player.builder().scoring(1.0).passing(null).blocking(null).build().getOffensiveRating());
            assertEquals(0, Player.builder().scoring(null).passing(1.0).blocking(1.0).build().getOffensiveRating());
            assertEquals(0, Player.builder().scoring(null).passing(1.0).blocking(null).build().getOffensiveRating());
            assertEquals(0, Player.builder().scoring(null).passing(null).blocking(1.0).build().getOffensiveRating());
            assertEquals(0, Player.builder().scoring(null).passing(null).blocking(null).build().getOffensiveRating());
        }
    }

    @Nested
    class GetAdjustedOffensiveRating {
        @Test
        void shouldReturnBaseOffensiveRatingGivenNoAdjustments() {
            final Player player = Player.builder().scoring(0.5).passing(0.5).blocking(0.5).build();

            assertEquals(player.getOffensiveRating(), player.getAdjustedOffensiveRating());
        }

        @Test
        void shouldReturnReducedValueAsPlayerGetsOlderGivenAgeAdjustment() {
            Player.PlayerBuilder player = Player.builder().scoring(0.5).passing(0.5).blocking(0.5).vitality(0.5);

            assertTrue(player.age(35).build().getAdjustedOffensiveRating(APPLY_AGE) >
                    player.age(36).build().getAdjustedOffensiveRating(APPLY_AGE));
            assertTrue(player.age(36).build().getAdjustedOffensiveRating(APPLY_AGE) >
                    player.age(37).build().getAdjustedOffensiveRating(APPLY_AGE));
            assertTrue(player.age(38).build().getAdjustedOffensiveRating(APPLY_AGE) >
                    player.age(39).build().getAdjustedOffensiveRating(APPLY_AGE));
            assertTrue(player.age(40).build().getAdjustedOffensiveRating(APPLY_AGE) >
                    player.age(41).build().getAdjustedOffensiveRating(APPLY_AGE));
            assertTrue(player.age(41).build().getAdjustedOffensiveRating(APPLY_AGE) >
                    player.age(42).build().getAdjustedOffensiveRating(APPLY_AGE));
        }

        @Test
        void shouldReturnReducedValueAsPlayerConfidenceDecreasesGivenConfidenceAdjustment() {
            Player.PlayerBuilder player = Player.builder().scoring(0.5).passing(0.5).blocking(0.5).vitality(0.5).age(20);

            assertTrue(player.confidence(1.0).build().getAdjustedOffensiveRating(APPLY_CONFIDENCE) >
                    player.confidence(0.9).build().getAdjustedOffensiveRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.9).build().getAdjustedOffensiveRating(APPLY_CONFIDENCE) >
                    player.confidence(0.8).build().getAdjustedOffensiveRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.8).build().getAdjustedOffensiveRating(APPLY_CONFIDENCE) >
                    player.confidence(0.7).build().getAdjustedOffensiveRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.7).build().getAdjustedOffensiveRating(APPLY_CONFIDENCE) >
                    player.confidence(0.6).build().getAdjustedOffensiveRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.6).build().getAdjustedOffensiveRating(APPLY_CONFIDENCE) >
                    player.confidence(0.5).build().getAdjustedOffensiveRating(APPLY_CONFIDENCE));
        }

        @Test
        void shouldReturnReducedValuesAsPlayerFatigueIncreasesGivenFatigueAdjustment() {
            Player.PlayerBuilder player = Player.builder().scoring(0.5).passing(0.5).blocking(0.5);

            assertTrue(player.fatigue(1.0).build().getAdjustedOffensiveRating(APPLY_FATIGUE) >
                    player.fatigue(1.1).build().getAdjustedOffensiveRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.1).build().getAdjustedOffensiveRating(APPLY_FATIGUE) >
                    player.fatigue(1.2).build().getAdjustedOffensiveRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.2).build().getAdjustedOffensiveRating(APPLY_FATIGUE) >
                    player.fatigue(1.3).build().getAdjustedOffensiveRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.3).build().getAdjustedOffensiveRating(APPLY_FATIGUE) >
                    player.fatigue(1.4).build().getAdjustedOffensiveRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.4).build().getAdjustedOffensiveRating(APPLY_FATIGUE) >
                    player.fatigue(1.5).build().getAdjustedOffensiveRating(APPLY_FATIGUE));
        }

        @Test
        void shouldApplyAllAdjustmentsGiven() {
            Player player = Player.builder()
                    .age(21)
                    .scoring(0.5)
                    .passing(0.5)
                    .blocking(0.5)
                    .vitality(0.0)
                    .confidence(0.0)
                    .fatigue(1.5)
                    .build();

            assertTrue(player.getAdjustedOffensiveRating(APPLY_AGE) >
                    player.getAdjustedOffensiveRating(APPLY_AGE, APPLY_CONFIDENCE));
            assertTrue(player.getAdjustedOffensiveRating(APPLY_CONFIDENCE) >
                    player.getAdjustedOffensiveRating(APPLY_AGE, APPLY_CONFIDENCE));
            assertTrue(player.getAdjustedOffensiveRating(APPLY_AGE) >
                    player.getAdjustedOffensiveRating(APPLY_AGE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedOffensiveRating(APPLY_FATIGUE) >
                    player.getAdjustedOffensiveRating(APPLY_AGE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedOffensiveRating(APPLY_CONFIDENCE) >
                    player.getAdjustedOffensiveRating(APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedOffensiveRating(APPLY_FATIGUE) >
                    player.getAdjustedOffensiveRating(APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedOffensiveRating(APPLY_AGE, APPLY_CONFIDENCE) >
                    player.getAdjustedOffensiveRating(APPLY_AGE, APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedOffensiveRating(APPLY_AGE, APPLY_FATIGUE) >
                    player.getAdjustedOffensiveRating(APPLY_AGE, APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedOffensiveRating(APPLY_CONFIDENCE, APPLY_FATIGUE) >
                    player.getAdjustedOffensiveRating(APPLY_AGE, APPLY_CONFIDENCE, APPLY_FATIGUE));
        }
    }

    @Nested
    class GetDefensiveRating {

        @Test
        void shouldReturnAverageOfTacklingStealingAndPresenceRatings() {
            assertEquals(1.0,
                    Player.builder().tackling(1.0).stealing(1.0).presence(1.0).build().getDefensiveRating(),
                    0.0001
            );
            assertEquals(0.6,
                    Player.builder().tackling(0.8).stealing(0.6).presence(0.4).build().getDefensiveRating(),
                    0.0001
            );
            assertEquals(0.4,
                    Player.builder().tackling(0.7).stealing(0.2).presence(0.3).build().getDefensiveRating(),
                    0.0001
            );
            assertEquals(0.5666,
                    Player.builder().tackling(0.1).stealing(0.8).presence(0.8).build().getDefensiveRating(),
                    0.0001
            );
            assertEquals(0.333,
                    Player.builder().tackling(0.123).stealing(0.321).presence(0.555).build().getDefensiveRating(),
                    0.0001
            );
        }

        @Test
        void shouldReturnZeroIfAnyDefensiveRatingIsNull() {
            assertEquals(0.0, Player.builder().tackling(1.0).stealing(1.0).presence(null).build().getDefensiveRating());
            assertEquals(0.0, Player.builder().tackling(1.0).stealing(null).presence(1.0).build().getDefensiveRating());
            assertEquals(0.0, Player.builder().tackling(1.0).stealing(null).presence(null).build().getDefensiveRating());
            assertEquals(0.0, Player.builder().tackling(null).stealing(1.0).presence(1.0).build().getDefensiveRating());
            assertEquals(0.0, Player.builder().tackling(null).stealing(1.0).presence(null).build().getDefensiveRating());
            assertEquals(0.0, Player.builder().tackling(null).stealing(null).presence(1.0).build().getDefensiveRating());
            assertEquals(0.0, Player.builder().tackling(null).stealing(null).presence(null).build().getDefensiveRating());
        }
    }

    @Nested
    class GetAdjustedDefensiveRating {
        @Test
        void shouldReturnBaseDefensiveRatingGivenNoAdjustments() {
            final Player player = Player.builder().tackling(0.5).stealing(0.5).presence(0.5).build();

            assertEquals(player.getDefensiveRating(), player.getAdjustedDefensiveRating());
        }

        @Test
        void shouldReturnReducedValueAsPlayerGetsOlderGivenAgeAdjustment() {
            Player.PlayerBuilder player = Player.builder().tackling(0.5).stealing(0.5).presence(0.5).vitality(0.5);

            assertTrue(player.age(35).build().getAdjustedDefensiveRating(APPLY_AGE) >
                    player.age(36).build().getAdjustedDefensiveRating(APPLY_AGE));
            assertTrue(player.age(36).build().getAdjustedDefensiveRating(APPLY_AGE) >
                    player.age(37).build().getAdjustedDefensiveRating(APPLY_AGE));
            assertTrue(player.age(38).build().getAdjustedDefensiveRating(APPLY_AGE) >
                    player.age(39).build().getAdjustedDefensiveRating(APPLY_AGE));
            assertTrue(player.age(40).build().getAdjustedDefensiveRating(APPLY_AGE) >
                    player.age(41).build().getAdjustedDefensiveRating(APPLY_AGE));
            assertTrue(player.age(41).build().getAdjustedDefensiveRating(APPLY_AGE) >
                    player.age(42).build().getAdjustedDefensiveRating(APPLY_AGE));
        }

        @Test
        void shouldReturnReducedValueAsPlayerConfidenceDecreasesGivenConfidenceAdjustment() {
            Player.PlayerBuilder player = Player.builder().tackling(0.5).stealing(0.5).presence(0.5).vitality(0.5).age(20);

            assertTrue(player.confidence(1.0).build().getAdjustedDefensiveRating(APPLY_CONFIDENCE) >
                    player.confidence(0.9).build().getAdjustedDefensiveRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.9).build().getAdjustedDefensiveRating(APPLY_CONFIDENCE) >
                    player.confidence(0.8).build().getAdjustedDefensiveRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.8).build().getAdjustedDefensiveRating(APPLY_CONFIDENCE) >
                    player.confidence(0.7).build().getAdjustedDefensiveRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.7).build().getAdjustedDefensiveRating(APPLY_CONFIDENCE) >
                    player.confidence(0.6).build().getAdjustedDefensiveRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.6).build().getAdjustedDefensiveRating(APPLY_CONFIDENCE) >
                    player.confidence(0.5).build().getAdjustedDefensiveRating(APPLY_CONFIDENCE));
        }

        @Test
        void shouldReturnReducedValuesAsPlayerFatigueIncreasesGivenFatigueAdjustment() {
            Player.PlayerBuilder player = Player.builder().tackling(0.5).stealing(0.5).presence(0.5);

            assertTrue(player.fatigue(1.0).build().getAdjustedDefensiveRating(APPLY_FATIGUE) >
                    player.fatigue(1.1).build().getAdjustedDefensiveRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.1).build().getAdjustedDefensiveRating(APPLY_FATIGUE) >
                    player.fatigue(1.2).build().getAdjustedDefensiveRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.2).build().getAdjustedDefensiveRating(APPLY_FATIGUE) >
                    player.fatigue(1.3).build().getAdjustedDefensiveRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.3).build().getAdjustedDefensiveRating(APPLY_FATIGUE) >
                    player.fatigue(1.4).build().getAdjustedDefensiveRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.4).build().getAdjustedDefensiveRating(APPLY_FATIGUE) >
                    player.fatigue(1.5).build().getAdjustedDefensiveRating(APPLY_FATIGUE));
        }

        @Test
        void shouldApplyAllAdjustmentsGiven() {
            Player player = Player.builder()
                    .age(21)
                    .tackling(0.5)
                    .stealing(0.5)
                    .presence(0.5)
                    .vitality(0.0)
                    .confidence(0.0)
                    .fatigue(1.5)
                    .build();

            assertTrue(player.getAdjustedDefensiveRating(APPLY_AGE) >
                    player.getAdjustedDefensiveRating(APPLY_AGE, APPLY_CONFIDENCE));
            assertTrue(player.getAdjustedDefensiveRating(APPLY_CONFIDENCE) >
                    player.getAdjustedDefensiveRating(APPLY_AGE, APPLY_CONFIDENCE));
            assertTrue(player.getAdjustedDefensiveRating(APPLY_AGE) >
                    player.getAdjustedDefensiveRating(APPLY_AGE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedDefensiveRating(APPLY_FATIGUE) >
                    player.getAdjustedDefensiveRating(APPLY_AGE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedDefensiveRating(APPLY_CONFIDENCE) >
                    player.getAdjustedDefensiveRating(APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedDefensiveRating(APPLY_FATIGUE) >
                    player.getAdjustedDefensiveRating(APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedDefensiveRating(APPLY_AGE, APPLY_CONFIDENCE) >
                    player.getAdjustedDefensiveRating(APPLY_AGE, APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedDefensiveRating(APPLY_AGE, APPLY_FATIGUE) >
                    player.getAdjustedDefensiveRating(APPLY_AGE, APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedDefensiveRating(APPLY_CONFIDENCE, APPLY_FATIGUE) >
                    player.getAdjustedDefensiveRating(APPLY_AGE, APPLY_CONFIDENCE, APPLY_FATIGUE));
        }
    }

    @Nested
    class GetIntangibleRating {

        @Test
        void shouldReturnAverageOfBlockingPresenceDisciplineAndEndurance() {
            assertEquals(1.0,
                    Player.builder().blocking(1.0).presence(1.0).discipline(1.0).endurance(1.0).build().getIntangibleRating(),
                    0.0001
            );
            assertEquals(0.5,
                    Player.builder().blocking(0.8).presence(0.3).discipline(0.7).endurance(0.2).build().getIntangibleRating(),
                    0.0001
            );
            assertEquals(0.75,
                    Player.builder().blocking(0.6).presence(0.7).discipline(0.8).endurance(0.9).build().getIntangibleRating(),
                    0.0001
            );
            assertEquals(0.4,
                    Player.builder().blocking(0.1).presence(0.3).discipline(0.5).endurance(0.7).build().getIntangibleRating(),
                    0.0001
            );
            assertEquals(0.47175,
                    Player.builder().blocking(0.321).presence(0.123).discipline(0.456).endurance(0.987).build().getIntangibleRating(),
                    0.0001
            );
        }

        @Test
        void shouldReturnZeroIfAnyIntangibleRatingIsNull() {
            assertEquals(0,
                    Player.builder().blocking(1.0).presence(1.0).discipline(1.0).endurance(null).build().getIntangibleRating()
            );
            assertEquals(0,
                    Player.builder().blocking(1.0).presence(1.0).discipline(null).endurance(1.0).build().getIntangibleRating()
            );
            assertEquals(0,
                    Player.builder().blocking(1.0).presence(null).discipline(1.0).endurance(1.0).build().getIntangibleRating()
            );
            assertEquals(0,
                    Player.builder().blocking(1.0).presence(null).discipline(1.0).endurance(null).build().getIntangibleRating()
            );
            assertEquals(0,
                    Player.builder().blocking(1.0).presence(null).discipline(null).endurance(1.0).build().getIntangibleRating()
            );
            assertEquals(0,
                    Player.builder().blocking(1.0).presence(null).discipline(null).endurance(null).build().getIntangibleRating()
            );
            assertEquals(0,
                    Player.builder().blocking(null).presence(1.0).discipline(1.0).endurance(1.0).build().getIntangibleRating()
            );
            assertEquals(0,
                    Player.builder().blocking(null).presence(1.0).discipline(1.0).endurance(null).build().getIntangibleRating()
            );
            assertEquals(0,
                    Player.builder().blocking(null).presence(1.0).discipline(null).endurance(1.0).build().getIntangibleRating()
            );
            assertEquals(0,
                    Player.builder().blocking(null).presence(null).discipline(1.0).endurance(1.0).build().getIntangibleRating()
            );
            assertEquals(0,
                    Player.builder().blocking(null).presence(null).discipline(1.0).endurance(null).build().getIntangibleRating()
            );
            assertEquals(0,
                    Player.builder().blocking(null).presence(null).discipline(null).endurance(1.0).build().getIntangibleRating()
            );
            assertEquals(0,
                    Player.builder().blocking(null).presence(null).discipline(null).endurance(null).build().getIntangibleRating()
            );
        }
    }

    @Nested
    class GetAdjustedIntangibleRating {
        @Test
        void shouldReturnBaseIntangibleRatingGivenNoAdjustments() {
            final Player player = Player.builder().blocking(0.5).presence(0.5).discipline(0.5).endurance(0.5).build();

            assertEquals(player.getIntangibleRating(), player.getAdjustedIntangibleRating());
        }

        @Test
        void shouldReturnReducedValueAsPlayerGetsOlderGivenAgeAdjustment() {
            Player.PlayerBuilder player = Player.builder().blocking(0.5).presence(0.5).discipline(0.5).endurance(0.5).vitality(0.5);

            assertTrue(player.age(35).build().getAdjustedIntangibleRating(APPLY_AGE) >
                    player.age(36).build().getAdjustedIntangibleRating(APPLY_AGE));
            assertTrue(player.age(36).build().getAdjustedIntangibleRating(APPLY_AGE) >
                    player.age(37).build().getAdjustedIntangibleRating(APPLY_AGE));
            assertTrue(player.age(38).build().getAdjustedIntangibleRating(APPLY_AGE) >
                    player.age(39).build().getAdjustedIntangibleRating(APPLY_AGE));
            assertTrue(player.age(40).build().getAdjustedIntangibleRating(APPLY_AGE) >
                    player.age(41).build().getAdjustedIntangibleRating(APPLY_AGE));
            assertTrue(player.age(41).build().getAdjustedIntangibleRating(APPLY_AGE) >
                    player.age(42).build().getAdjustedIntangibleRating(APPLY_AGE));
        }

        @Test
        void shouldReturnReducedValueAsPlayerConfidenceDecreasesGivenConfidenceAdjustment() {
            Player.PlayerBuilder player = Player.builder().blocking(0.5).presence(0.5).discipline(0.5).endurance(0.5).vitality(0.5).age(20);

            assertTrue(player.confidence(1.0).build().getAdjustedIntangibleRating(APPLY_CONFIDENCE) >
                    player.confidence(0.9).build().getAdjustedIntangibleRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.9).build().getAdjustedIntangibleRating(APPLY_CONFIDENCE) >
                    player.confidence(0.8).build().getAdjustedIntangibleRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.8).build().getAdjustedIntangibleRating(APPLY_CONFIDENCE) >
                    player.confidence(0.7).build().getAdjustedIntangibleRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.7).build().getAdjustedIntangibleRating(APPLY_CONFIDENCE) >
                    player.confidence(0.6).build().getAdjustedIntangibleRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.6).build().getAdjustedIntangibleRating(APPLY_CONFIDENCE) >
                    player.confidence(0.5).build().getAdjustedIntangibleRating(APPLY_CONFIDENCE));
        }

        @Test
        void shouldReturnReducedValuesAsPlayerFatigueIncreasesGivenFatigueAdjustment() {
            Player.PlayerBuilder player = Player.builder().blocking(0.5).presence(0.5).discipline(0.5).endurance(0.5);

            assertTrue(player.fatigue(1.0).build().getAdjustedIntangibleRating(APPLY_FATIGUE) >
                    player.fatigue(1.1).build().getAdjustedIntangibleRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.1).build().getAdjustedIntangibleRating(APPLY_FATIGUE) >
                    player.fatigue(1.2).build().getAdjustedIntangibleRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.2).build().getAdjustedIntangibleRating(APPLY_FATIGUE) >
                    player.fatigue(1.3).build().getAdjustedIntangibleRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.3).build().getAdjustedIntangibleRating(APPLY_FATIGUE) >
                    player.fatigue(1.4).build().getAdjustedIntangibleRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.4).build().getAdjustedIntangibleRating(APPLY_FATIGUE) >
                    player.fatigue(1.5).build().getAdjustedIntangibleRating(APPLY_FATIGUE));
        }

        @Test
        void shouldApplyAllAdjustmentsGiven() {
            Player player = Player.builder()
                    .age(21)
                    .blocking(0.5)
                    .presence(0.5)
                    .discipline(0.5)
                    .endurance(0.5)
                    .vitality(0.0)
                    .confidence(0.0)
                    .fatigue(1.5)
                    .build();

            assertTrue(player.getAdjustedIntangibleRating(APPLY_AGE) >
                    player.getAdjustedIntangibleRating(APPLY_AGE, APPLY_CONFIDENCE));
            assertTrue(player.getAdjustedIntangibleRating(APPLY_CONFIDENCE) >
                    player.getAdjustedIntangibleRating(APPLY_AGE, APPLY_CONFIDENCE));
            assertTrue(player.getAdjustedIntangibleRating(APPLY_AGE) >
                    player.getAdjustedIntangibleRating(APPLY_AGE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedIntangibleRating(APPLY_FATIGUE) >
                    player.getAdjustedIntangibleRating(APPLY_AGE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedIntangibleRating(APPLY_CONFIDENCE) >
                    player.getAdjustedIntangibleRating(APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedIntangibleRating(APPLY_FATIGUE) >
                    player.getAdjustedIntangibleRating(APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedIntangibleRating(APPLY_AGE, APPLY_CONFIDENCE) >
                    player.getAdjustedIntangibleRating(APPLY_AGE, APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedIntangibleRating(APPLY_AGE, APPLY_FATIGUE) >
                    player.getAdjustedIntangibleRating(APPLY_AGE, APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedIntangibleRating(APPLY_CONFIDENCE, APPLY_FATIGUE) >
                    player.getAdjustedIntangibleRating(APPLY_AGE, APPLY_CONFIDENCE, APPLY_FATIGUE));
        }
    }

    @Nested
    class GetPenaltyRating {

        @Test
        void shouldReturnAverageOfPenaltyShotPenaltyOffenseAndPenaltyDefense() {
            assertEquals(1.0,
                    Player.builder().penaltyShot(1.0).penaltyOffense(1.0).penaltyDefense(1.0).build().getPenaltyRating(),
                    0.0001
            );
            assertEquals(0.6,
                    Player.builder().penaltyShot(0.8).penaltyOffense(0.6).penaltyDefense(0.4).build().getPenaltyRating(),
                    0.0001
            );
            assertEquals(0.4,
                    Player.builder().penaltyShot(0.7).penaltyOffense(0.2).penaltyDefense(0.3).build().getPenaltyRating(),
                    0.0001
            );
            assertEquals(0.5666,
                    Player.builder().penaltyShot(0.1).penaltyOffense(0.8).penaltyDefense(0.8).build().getPenaltyRating(),
                    0.0001
            );
            assertEquals(0.333,
                    Player.builder().penaltyShot(0.123).penaltyOffense(0.321).penaltyDefense(0.555).build().getPenaltyRating(),
                    0.0001
            );
        }

        @Test
        void shouldReturnZeroIfAnyPenaltyRatingIsNull() {
            assertEquals(0.0, Player.builder().penaltyShot(1.0).penaltyOffense(1.0).penaltyDefense(null).build().getPenaltyRating());
            assertEquals(0.0, Player.builder().penaltyShot(1.0).penaltyOffense(null).penaltyDefense(1.0).build().getPenaltyRating());
            assertEquals(0.0, Player.builder().penaltyShot(1.0).penaltyOffense(null).penaltyDefense(null).build().getPenaltyRating());
            assertEquals(0.0, Player.builder().penaltyShot(null).penaltyOffense(1.0).penaltyDefense(1.0).build().getPenaltyRating());
            assertEquals(0.0, Player.builder().penaltyShot(null).penaltyOffense(1.0).penaltyDefense(null).build().getPenaltyRating());
            assertEquals(0.0, Player.builder().penaltyShot(null).penaltyOffense(null).penaltyDefense(1.0).build().getPenaltyRating());
            assertEquals(0.0, Player.builder().penaltyShot(null).penaltyOffense(null).penaltyDefense(null).build().getPenaltyRating());
        }
    }

    @Nested
    class GetAdjustedPenaltyRating {
        @Test
        void shouldReturnBasePenaltyRatingGivenNoAdjustments() {
            final Player player = Player.builder().penaltyShot(0.5).penaltyOffense(0.5).penaltyDefense(0.5).build();

            assertEquals(player.getPenaltyRating(), player.getAdjustedPenaltyRating());
        }

        @Test
        void shouldReturnReducedValueAsPlayerGetsOlderGivenAgeAdjustment() {
            Player.PlayerBuilder player = Player.builder().penaltyShot(0.5).penaltyOffense(0.5).penaltyDefense(0.5).vitality(0.5);

            assertTrue(player.age(35).build().getAdjustedPenaltyRating(APPLY_AGE) >
                    player.age(36).build().getAdjustedPenaltyRating(APPLY_AGE));
            assertTrue(player.age(36).build().getAdjustedPenaltyRating(APPLY_AGE) >
                    player.age(37).build().getAdjustedPenaltyRating(APPLY_AGE));
            assertTrue(player.age(38).build().getAdjustedPenaltyRating(APPLY_AGE) >
                    player.age(39).build().getAdjustedPenaltyRating(APPLY_AGE));
            assertTrue(player.age(40).build().getAdjustedPenaltyRating(APPLY_AGE) >
                    player.age(41).build().getAdjustedPenaltyRating(APPLY_AGE));
            assertTrue(player.age(41).build().getAdjustedPenaltyRating(APPLY_AGE) >
                    player.age(42).build().getAdjustedPenaltyRating(APPLY_AGE));
        }

        @Test
        void shouldReturnReducedValueAsPlayerConfidenceDecreasesGivenConfidenceAdjustment() {
            Player.PlayerBuilder player = Player.builder().penaltyShot(0.5).penaltyOffense(0.5).penaltyDefense(0.5).vitality(0.5).age(20);

            assertTrue(player.confidence(1.0).build().getAdjustedPenaltyRating(APPLY_CONFIDENCE) >
                    player.confidence(0.9).build().getAdjustedPenaltyRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.9).build().getAdjustedPenaltyRating(APPLY_CONFIDENCE) >
                    player.confidence(0.8).build().getAdjustedPenaltyRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.8).build().getAdjustedPenaltyRating(APPLY_CONFIDENCE) >
                    player.confidence(0.7).build().getAdjustedPenaltyRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.7).build().getAdjustedPenaltyRating(APPLY_CONFIDENCE) >
                    player.confidence(0.6).build().getAdjustedPenaltyRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.6).build().getAdjustedPenaltyRating(APPLY_CONFIDENCE) >
                    player.confidence(0.5).build().getAdjustedPenaltyRating(APPLY_CONFIDENCE));
        }

        @Test
        void shouldReturnReducedValuesAsPlayerFatigueIncreasesGivenFatigueAdjustment() {
            Player.PlayerBuilder player = Player.builder().penaltyShot(0.5).penaltyOffense(0.5).penaltyDefense(0.5);

            assertTrue(player.fatigue(1.0).build().getAdjustedPenaltyRating(APPLY_FATIGUE) >
                    player.fatigue(1.1).build().getAdjustedPenaltyRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.1).build().getAdjustedPenaltyRating(APPLY_FATIGUE) >
                    player.fatigue(1.2).build().getAdjustedPenaltyRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.2).build().getAdjustedPenaltyRating(APPLY_FATIGUE) >
                    player.fatigue(1.3).build().getAdjustedPenaltyRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.3).build().getAdjustedPenaltyRating(APPLY_FATIGUE) >
                    player.fatigue(1.4).build().getAdjustedPenaltyRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.4).build().getAdjustedPenaltyRating(APPLY_FATIGUE) >
                    player.fatigue(1.5).build().getAdjustedPenaltyRating(APPLY_FATIGUE));
        }

        @Test
        void shouldApplyAllAdjustmentsGiven() {
            Player player = Player.builder()
                    .age(21)
                    .penaltyShot(0.5)
                    .penaltyOffense(0.5)
                    .penaltyDefense(0.5)
                    .vitality(0.0)
                    .confidence(0.0)
                    .fatigue(1.5)
                    .build();

            assertTrue(player.getAdjustedPenaltyRating(APPLY_AGE) >
                    player.getAdjustedPenaltyRating(APPLY_AGE, APPLY_CONFIDENCE));
            assertTrue(player.getAdjustedPenaltyRating(APPLY_CONFIDENCE) >
                    player.getAdjustedPenaltyRating(APPLY_AGE, APPLY_CONFIDENCE));
            assertTrue(player.getAdjustedPenaltyRating(APPLY_AGE) >
                    player.getAdjustedPenaltyRating(APPLY_AGE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedPenaltyRating(APPLY_FATIGUE) >
                    player.getAdjustedPenaltyRating(APPLY_AGE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedPenaltyRating(APPLY_CONFIDENCE) >
                    player.getAdjustedPenaltyRating(APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedPenaltyRating(APPLY_FATIGUE) >
                    player.getAdjustedPenaltyRating(APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedPenaltyRating(APPLY_AGE, APPLY_CONFIDENCE) >
                    player.getAdjustedPenaltyRating(APPLY_AGE, APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedPenaltyRating(APPLY_AGE, APPLY_FATIGUE) >
                    player.getAdjustedPenaltyRating(APPLY_AGE, APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedPenaltyRating(APPLY_CONFIDENCE, APPLY_FATIGUE) >
                    player.getAdjustedPenaltyRating(APPLY_AGE, APPLY_CONFIDENCE, APPLY_FATIGUE));
        }
    }

    @Nested
    class GetPerformanceRating {

        @Test
        void shouldReturnAverageOfScoringPassingBlockingTacklingStealingPresenceDisciplineAndEndurance() {
            assertEquals(1.0,
                    Player.builder()
                            .scoring(1.0).passing(1.0).blocking(1.0)
                            .tackling(1.0).stealing(1.0).presence(1.0)
                            .discipline(1.0).endurance(1.0)
                            .build().getPerformanceRating(),
                    0.0001
            );
            assertEquals(0.45,
                    Player.builder()
                            .scoring(0.1).passing(0.2).blocking(0.3)
                            .tackling(0.4).stealing(0.5).presence(0.6)
                            .discipline(0.7).endurance(0.8)
                            .build().getPerformanceRating(),
                    0.0001
            );
            assertEquals(0.4,
                    Player.builder()
                            .scoring(0.1).passing(0.1).blocking(0.3)
                            .tackling(0.3).stealing(0.5).presence(0.5)
                            .discipline(0.7).endurance(0.7)
                            .build().getPerformanceRating(),
                    0.0001
            );
            assertEquals(0.4875,
                    Player.builder()
                            .scoring(0.5).passing(0.5).blocking(0.5)
                            .tackling(0.4).stealing(0.4).presence(0.4)
                            .discipline(0.6).endurance(0.6)
                            .build().getPerformanceRating(),
                    0.0001
            );
            assertEquals(0.5689,
                    Player.builder()
                            .scoring(0.123).passing(0.321).blocking(0.456)
                            .tackling(0.654).stealing(0.789).presence(0.987)
                            .discipline(0.555).endurance(0.666)
                            .build().getPerformanceRating(),
                    0.0001
            );
        }

        @Test
        void shouldReturnZeroIfAnyRatingIsNull() {
            assertEquals(0.0, Player.builder()
                    .scoring(null).passing(1.0).blocking(1.0)
                    .tackling(1.0).stealing(1.0).presence(1.0)
                    .discipline(1.0).endurance(1.0)
                    .build().getPerformanceRating()
            );
            assertEquals(0.0, Player.builder()
                    .scoring(1.0).passing(null).blocking(1.0)
                    .tackling(1.0).stealing(1.0).presence(1.0)
                    .discipline(1.0).endurance(1.0)
                    .build().getPerformanceRating()
            );
            assertEquals(0.0, Player.builder()
                    .scoring(1.0).passing(1.0).blocking(null)
                    .tackling(1.0).stealing(1.0).presence(1.0)
                    .discipline(1.0).endurance(1.0)
                    .build().getPerformanceRating()
            );
            assertEquals(0.0, Player.builder()
                    .scoring(1.0).passing(1.0).blocking(1.0)
                    .tackling(null).stealing(1.0).presence(1.0)
                    .discipline(1.0).endurance(1.0)
                    .build().getPerformanceRating()
            );
            assertEquals(0.0, Player.builder()
                    .scoring(1.0).passing(1.0).blocking(1.0)
                    .tackling(1.0).stealing(null).presence(1.0)
                    .discipline(1.0).endurance(1.0)
                    .build().getPerformanceRating()
            );
            assertEquals(0.0, Player.builder()
                    .scoring(1.0).passing(1.0).blocking(1.0)
                    .tackling(1.0).stealing(1.0).presence(null)
                    .discipline(1.0).endurance(1.0)
                    .build().getPerformanceRating()
            );
            assertEquals(0.0, Player.builder()
                    .scoring(1.0).passing(1.0).blocking(1.0)
                    .tackling(1.0).stealing(1.0).presence(1.0)
                    .discipline(null).endurance(1.0)
                    .build().getPerformanceRating()
            );
            assertEquals(0.0, Player.builder()
                    .scoring(1.0).passing(1.0).blocking(1.0)
                    .tackling(1.0).stealing(1.0).presence(1.0)
                    .discipline(1.0).endurance(null)
                    .build().getPerformanceRating()
            );
        }
    }

    @Nested
    class GetAdjustedPerformanceRating {
        @Test
        void shouldReturnBasePerformanceRatingGivenNoAdjustments() {
            final Player player = Player.builder()
                    .scoring(0.5)
                    .passing(0.5)
                    .blocking(0.5)
                    .tackling(0.5)
                    .stealing(0.5)
                    .presence(0.5)
                    .discipline(0.5)
                    .endurance(0.5)
                    .build();

            assertEquals(player.getPerformanceRating(), player.getAdjustedPerformanceRating());
        }

        @Test
        void shouldReturnReducedValueAsPlayerGetsOlderGivenAgeAdjustment() {
            Player.PlayerBuilder player = Player.builder()
                    .scoring(0.5)
                    .passing(0.5)
                    .blocking(0.5)
                    .tackling(0.5)
                    .stealing(0.5)
                    .presence(0.5)
                    .discipline(0.5)
                    .endurance(0.5)
                    .vitality(0.5);

            assertTrue(player.age(35).build().getAdjustedPerformanceRating(APPLY_AGE) >
                    player.age(36).build().getAdjustedPerformanceRating(APPLY_AGE));
            assertTrue(player.age(36).build().getAdjustedPerformanceRating(APPLY_AGE) >
                    player.age(37).build().getAdjustedPerformanceRating(APPLY_AGE));
            assertTrue(player.age(38).build().getAdjustedPerformanceRating(APPLY_AGE) >
                    player.age(39).build().getAdjustedPerformanceRating(APPLY_AGE));
            assertTrue(player.age(40).build().getAdjustedPerformanceRating(APPLY_AGE) >
                    player.age(41).build().getAdjustedPerformanceRating(APPLY_AGE));
            assertTrue(player.age(41).build().getAdjustedPerformanceRating(APPLY_AGE) >
                    player.age(42).build().getAdjustedPerformanceRating(APPLY_AGE));
        }

        @Test
        void shouldReturnReducedValueAsPlayerConfidenceDecreasesGivenConfidenceAdjustment() {
            Player.PlayerBuilder player = Player.builder()
                    .scoring(0.5)
                    .passing(0.5)
                    .blocking(0.5)
                    .tackling(0.5)
                    .stealing(0.5)
                    .presence(0.5)
                    .discipline(0.5)
                    .endurance(0.5)
                    .vitality(0.5)
                    .age(20);

            assertTrue(player.confidence(1.0).build().getAdjustedPerformanceRating(APPLY_CONFIDENCE) >
                    player.confidence(0.9).build().getAdjustedPerformanceRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.9).build().getAdjustedPerformanceRating(APPLY_CONFIDENCE) >
                    player.confidence(0.8).build().getAdjustedPerformanceRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.8).build().getAdjustedPerformanceRating(APPLY_CONFIDENCE) >
                    player.confidence(0.7).build().getAdjustedPerformanceRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.7).build().getAdjustedPerformanceRating(APPLY_CONFIDENCE) >
                    player.confidence(0.6).build().getAdjustedPerformanceRating(APPLY_CONFIDENCE));
            assertTrue(player.confidence(0.6).build().getAdjustedPerformanceRating(APPLY_CONFIDENCE) >
                    player.confidence(0.5).build().getAdjustedPerformanceRating(APPLY_CONFIDENCE));
        }

        @Test
        void shouldReturnReducedValuesAsPlayerFatigueIncreasesGivenFatigueAdjustment() {
            Player.PlayerBuilder player = Player.builder()
                    .scoring(0.5)
                    .passing(0.5)
                    .blocking(0.5)
                    .tackling(0.5)
                    .stealing(0.5)
                    .presence(0.5)
                    .discipline(0.5)
                    .endurance(0.5);

            assertTrue(player.fatigue(1.0).build().getAdjustedPerformanceRating(APPLY_FATIGUE) >
                    player.fatigue(1.1).build().getAdjustedPerformanceRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.1).build().getAdjustedPerformanceRating(APPLY_FATIGUE) >
                    player.fatigue(1.2).build().getAdjustedPerformanceRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.2).build().getAdjustedPerformanceRating(APPLY_FATIGUE) >
                    player.fatigue(1.3).build().getAdjustedPerformanceRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.3).build().getAdjustedPerformanceRating(APPLY_FATIGUE) >
                    player.fatigue(1.4).build().getAdjustedPerformanceRating(APPLY_FATIGUE));
            assertTrue(player.fatigue(1.4).build().getAdjustedPerformanceRating(APPLY_FATIGUE) >
                    player.fatigue(1.5).build().getAdjustedPerformanceRating(APPLY_FATIGUE));
        }

        @Test
        void shouldApplyAllAdjustmentsGiven() {
            Player player = Player.builder()
                    .age(21)
                    .scoring(0.5)
                    .passing(0.5)
                    .blocking(0.5)
                    .tackling(0.5)
                    .stealing(0.5)
                    .presence(0.5)
                    .discipline(0.5)
                    .endurance(0.5)
                    .vitality(0.0)
                    .confidence(0.0)
                    .fatigue(1.5)
                    .build();

            assertTrue(player.getAdjustedPerformanceRating(APPLY_AGE) >
                    player.getAdjustedPerformanceRating(APPLY_AGE, APPLY_CONFIDENCE));
            assertTrue(player.getAdjustedPerformanceRating(APPLY_CONFIDENCE) >
                    player.getAdjustedPerformanceRating(APPLY_AGE, APPLY_CONFIDENCE));
            assertTrue(player.getAdjustedPerformanceRating(APPLY_AGE) >
                    player.getAdjustedPerformanceRating(APPLY_AGE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedPerformanceRating(APPLY_FATIGUE) >
                    player.getAdjustedPerformanceRating(APPLY_AGE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedPerformanceRating(APPLY_CONFIDENCE) >
                    player.getAdjustedPerformanceRating(APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedPerformanceRating(APPLY_FATIGUE) >
                    player.getAdjustedPerformanceRating(APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedPerformanceRating(APPLY_AGE, APPLY_CONFIDENCE) >
                    player.getAdjustedPerformanceRating(APPLY_AGE, APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedPerformanceRating(APPLY_AGE, APPLY_FATIGUE) >
                    player.getAdjustedPerformanceRating(APPLY_AGE, APPLY_CONFIDENCE, APPLY_FATIGUE));
            assertTrue(player.getAdjustedPerformanceRating(APPLY_CONFIDENCE, APPLY_FATIGUE) >
                    player.getAdjustedPerformanceRating(APPLY_AGE, APPLY_CONFIDENCE, APPLY_FATIGUE));
        }
    }

    @Nested
    class GetAgeFactor {
        @Test
        void shouldReturnValueBetweenZeroAndOneGivenReasonableAge() {
            final Player player = Player.builder().age(25).vitality(0.5).build();

            final Double ageFactor = player.getAgeFactor();

            assertTrue(ageFactor >= 0.0);
            assertTrue(ageFactor <= 1.0);
        }

        @Test
        void shouldReturnMaxFactorIfAgeIsUnderTwentyOneAndMinVitality() {
            assertEquals(MAX_FACTOR, Player.builder().age(18).vitality(MIN_RATING).build().getAgeFactor());
            assertEquals(MAX_FACTOR, Player.builder().age(19).vitality(MIN_RATING).build().getAgeFactor());
            assertEquals(MAX_FACTOR, Player.builder().age(20).vitality(MIN_RATING).build().getAgeFactor());
        }

        @Test
        void shouldReturnReducedValueIfAgeIsOverTwentyAndMinVitality() {
            assertTrue(MAX_FACTOR > Player.builder().age(21).vitality(MIN_RATING).build().getAgeFactor());
            assertTrue(MAX_FACTOR > Player.builder().age(22).vitality(MIN_RATING).build().getAgeFactor());
            assertTrue(MAX_FACTOR > Player.builder().age(23).vitality(MIN_RATING).build().getAgeFactor());
        }

        @Test
        void shouldReturnMaxFactorIfAgeIsUnderThirtySixAndMaxVitality() {
            assertEquals(MAX_FACTOR, Player.builder().age(33).vitality(MAX_RATING).build().getAgeFactor());
            assertEquals(MAX_FACTOR, Player.builder().age(34).vitality(MAX_RATING).build().getAgeFactor());
            assertEquals(MAX_FACTOR, Player.builder().age(35).vitality(MAX_RATING).build().getAgeFactor());
        }

        @Test
        void shouldReturnReducedValueIfAgeIsOverThirtyFiveAndMaxVitality() {
            assertTrue(MAX_FACTOR > Player.builder().age(36).vitality(MAX_RATING).build().getAgeFactor());
            assertTrue(MAX_FACTOR > Player.builder().age(37).vitality(MAX_RATING).build().getAgeFactor());
            assertTrue(MAX_FACTOR > Player.builder().age(38).vitality(MAX_RATING).build().getAgeFactor());
        }

        @Test
        void shouldReturnLowerNumberAsAgeIncreasesWhenVitalityIsTheSame() {
            assertTrue(Player.builder().age(34).vitality(0.5).build().getAgeFactor() >
                    Player.builder().age(35).vitality(0.5).build().getAgeFactor());
            assertTrue(Player.builder().age(35).vitality(0.5).build().getAgeFactor() >
                    Player.builder().age(36).vitality(0.5).build().getAgeFactor());
            assertTrue(Player.builder().age(36).vitality(0.5).build().getAgeFactor() >
                    Player.builder().age(37).vitality(0.5).build().getAgeFactor());
            assertTrue(Player.builder().age(37).vitality(0.5).build().getAgeFactor() >
                    Player.builder().age(38).vitality(0.5).build().getAgeFactor());
            assertTrue(Player.builder().age(38).vitality(0.5).build().getAgeFactor() >
                    Player.builder().age(39).vitality(0.5).build().getAgeFactor());
            assertTrue(Player.builder().age(39).vitality(0.5).build().getAgeFactor() >
                    Player.builder().age(40).vitality(0.5).build().getAgeFactor());
        }

        @Test
        void shouldReturnHigherNumberAsVitalityIncreasesWhenAgeIsTheSame() {
            assertTrue(Player.builder().age(35).vitality(0.4).build().getAgeFactor() >
                    Player.builder().age(35).vitality(0.3).build().getAgeFactor());
            assertTrue(Player.builder().age(35).vitality(0.5).build().getAgeFactor() >
                    Player.builder().age(35).vitality(0.4).build().getAgeFactor());
            assertTrue(Player.builder().age(35).vitality(0.6).build().getAgeFactor() >
                    Player.builder().age(35).vitality(0.5).build().getAgeFactor());
            assertTrue(Player.builder().age(35).vitality(0.7).build().getAgeFactor() >
                    Player.builder().age(35).vitality(0.6).build().getAgeFactor());
            assertTrue(Player.builder().age(35).vitality(0.8).build().getAgeFactor() >
                    Player.builder().age(35).vitality(0.7).build().getAgeFactor());

        }

        // TODO: This test should change when calculation is fixed (see method)
        @Test
        void shouldReturnMinFactorWhenAgeIsFortyAndMinVitality() {
            assertEquals(MIN_FACTOR, Player.builder().age(40).vitality(MIN_RATING).build().getAgeFactor());
        }

        // TODO: This test should change when calculation is fixed (see method)
        @Test
        void shouldReturnMinFactorWhenAgeIsFortyFiveAndMaxVitality() {
            assertEquals(MIN_FACTOR, Player.builder().age(45).vitality(MAX_RATING).build().getAgeFactor());
        }
    }

    @Nested
    class GetConfidenceFactor {
        @Test
        void shouldReturnMaxFactorWhenConfidenceIsMax() {
            final Player player = Player.builder().age(35).confidence(MAX_RATING).build();

            assertEquals(MAX_FACTOR, player.getConfidenceFactor());
        }

        @Test
        void shouldReturnMinFactorWhenConfidenceIsMinAndAgeIsMin() {
            final Player player = Player.builder().age(STARTING_AGE).confidence(MIN_RATING).build();

            assertEquals(MIN_FACTOR, player.getConfidenceFactor());
        }

        @Test
        void shouldIncreaseValueAsPlayerAges() {
            assertTrue(Player.builder().age(STARTING_AGE).confidence(0.0).build().getConfidenceFactor() <
                    Player.builder().age(STARTING_AGE + 1).confidence(0.0).build().getConfidenceFactor());
            assertTrue(Player.builder().age(STARTING_AGE + 1).confidence(0.0).build().getConfidenceFactor() <
                    Player.builder().age(STARTING_AGE + 2).confidence(0.0).build().getConfidenceFactor());
            assertTrue(Player.builder().age(STARTING_AGE + 2).confidence(0.0).build().getConfidenceFactor() <
                    Player.builder().age(STARTING_AGE + 3).confidence(0.0).build().getConfidenceFactor());
            assertTrue(Player.builder().age(STARTING_AGE + 3).confidence(0.0).build().getConfidenceFactor() <
                    Player.builder().age(STARTING_AGE + 4).confidence(0.0).build().getConfidenceFactor());
            assertTrue(Player.builder().age(STARTING_AGE + 4).confidence(0.0).build().getConfidenceFactor() <
                    Player.builder().age(STARTING_AGE + 5).confidence(0.0).build().getConfidenceFactor());
        }

        @Test
        void shouldIncreaseValueAsConfidenceIncreases() {
            assertTrue(Player.builder().age(STARTING_AGE).confidence(0.0).build().getConfidenceFactor() <
                    Player.builder().age(STARTING_AGE).confidence(0.1).build().getConfidenceFactor());
            assertTrue(Player.builder().age(STARTING_AGE).confidence(0.1).build().getConfidenceFactor() <
                    Player.builder().age(STARTING_AGE).confidence(0.2).build().getConfidenceFactor());
            assertTrue(Player.builder().age(STARTING_AGE).confidence(0.2).build().getConfidenceFactor() <
                    Player.builder().age(STARTING_AGE).confidence(0.3).build().getConfidenceFactor());
            assertTrue(Player.builder().age(STARTING_AGE).confidence(0.3).build().getConfidenceFactor() <
                    Player.builder().age(STARTING_AGE).confidence(0.4).build().getConfidenceFactor());
            assertTrue(Player.builder().age(STARTING_AGE).confidence(0.4).build().getConfidenceFactor() <
                    Player.builder().age(STARTING_AGE).confidence(0.5).build().getConfidenceFactor());
        }
    }

    @Nested
    class GetFatigueFactor {
        @Test
        void shouldReturnMaxFactorWhenFatigueIsUnderOne() {
            assertEquals(MAX_FACTOR, Player.builder().fatigue(0.1).build().getFatigueFactor());
            assertEquals(MAX_FACTOR, Player.builder().fatigue(0.3).build().getFatigueFactor());
            assertEquals(MAX_FACTOR, Player.builder().fatigue(0.5).build().getFatigueFactor());
            assertEquals(MAX_FACTOR, Player.builder().fatigue(0.7).build().getFatigueFactor());
            assertEquals(MAX_FACTOR, Player.builder().fatigue(0.9).build().getFatigueFactor());
        }

        @Test
        void shouldReturnLowerValuesAsFatigueIncreasesOverOne() {
            assertTrue(Player.builder().fatigue(1.0).build().getFatigueFactor() >
                    Player.builder().fatigue(1.1).build().getFatigueFactor());
            assertTrue(Player.builder().fatigue(1.1).build().getFatigueFactor() >
                    Player.builder().fatigue(1.2).build().getFatigueFactor());
            assertTrue(Player.builder().fatigue(1.2).build().getFatigueFactor() >
                    Player.builder().fatigue(1.3).build().getFatigueFactor());
            assertTrue(Player.builder().fatigue(1.3).build().getFatigueFactor() >
                    Player.builder().fatigue(1.4).build().getFatigueFactor());
            assertTrue(Player.builder().fatigue(1.4).build().getFatigueFactor() >
                    Player.builder().fatigue(1.5).build().getFatigueFactor());
        }

        @Test
        void shouldReturnMinFactorWhenFatigueIsTwoOrAbove() {
            assertEquals(MIN_FACTOR, Player.builder().fatigue(2.0).build().getFatigueFactor());
            assertEquals(MIN_FACTOR, Player.builder().fatigue(2.2).build().getFatigueFactor());
            assertEquals(MIN_FACTOR, Player.builder().fatigue(2.4).build().getFatigueFactor());
            assertEquals(MIN_FACTOR, Player.builder().fatigue(2.6).build().getFatigueFactor());
            assertEquals(MIN_FACTOR, Player.builder().fatigue(2.8).build().getFatigueFactor());
        }
    }

    @Nested
    class Generate {

        @Test
        void shouldReturnAPlayer() {
            final Player player = Player.generate(null, null, null, null);

            assertNotNull(player);
        }

        @Test
        void shouldReturnAPlayerWithTheGivenPlayerId() {
            final Player player = Player.generate(321, null, null, null);

            assertEquals(321, player.getPlayerId());
        }

        @Test
        void shouldReturnAPlayerWithTheGivenYear() {
            final Player player = Player.generate(null, "2020", null, null);

            assertEquals("2020", player.getYear());
        }

        @Test
        void shouldReturnAPlayerWithTheGivenName() {
            final Player player = Player.generate(null, null, "David", "Montgomery");

            assertEquals("David", player.getFirstName());
            assertEquals("Montgomery", player.getLastName());
        }

        @Test
        void shouldReturnAPlayerWithWithAnAgeBetweenEighteenAndThirty() {
            final Player player = Player.generate(null, null, null, null);

            assertTrue(player.getAge() >= 18 && player.getAge() <= 30);
        }

        @Test
        void shouldChooseThePlayerAgeRandomly() {
            final List<Player> players = new ArrayList<>();

            for (int i = 0; i <= 100; i++) {
                players.add(Player.generate(null, null, null, null));
            }

            assertEquals(12, players.stream().map(Player::getAge).distinct().count());
        }

        @Test
        void shouldReturnAPlayerWithAScoringRatingBetweenZeroAndOne() {
            final Player player = Player.generate(null, null, null, null);

            assertTrue(player.getScoring() >= 0.0 && player.getScoring() <= 1.0);
        }

        @Test
        void shouldChooseTheScoringRatingRandomly() {
            final List<Player> players = new ArrayList<>();

            for (int i = 0; i <= 100; i++) {
                players.add(Player.generate(null, null, null, null));
            }

            assertTrue(players.stream().map(Player::getScoring).distinct().count() >= 85);
        }

        @Test
        void shouldReturnAPlayerWithAPassingRatingBetweenZeroAndOne() {
            final Player player = Player.generate(null, null, null, null);

            assertTrue(player.getPassing() >= 0.0 && player.getPassing() <= 1.0);
        }

        @Test
        void shouldChooseThePassingRatingRandomly() {
            final List<Player> players = new ArrayList<>();

            for (int i = 0; i <= 100; i++) {
                players.add(Player.generate(null, null, null, null));
            }

            assertTrue(players.stream().map(Player::getPassing).distinct().count() >= 85);
        }

        @Test
        void shouldReturnAPlayerWithABlockingRatingBetweenZeroAndOne() {
            final Player player = Player.generate(null, null, null, null);

            assertTrue(player.getBlocking() >= 0.0 && player.getBlocking() <= 1.0);
        }

        @Test
        void shouldChooseTheBlockingRatingRandomly() {
            final List<Player> players = new ArrayList<>();

            for (int i = 0; i <= 100; i++) {
                players.add(Player.generate(null, null, null, null));
            }

            assertTrue(players.stream().map(Player::getBlocking).distinct().count() >= 85);
        }

        @Test
        void shouldReturnAPlayerWithATacklingRatingBetweenZeroAndOne() {
            final Player player = Player.generate(null, null, null, null);

            assertTrue(player.getTackling() >= 0.0 && player.getTackling() <= 1.0);
        }

        @Test
        void shouldChooseTheTacklingRatingRandomly() {
            final List<Player> players = new ArrayList<>();

            for (int i = 0; i <= 100; i++) {
                players.add(Player.generate(null, null, null, null));
            }

            assertTrue(players.stream().map(Player::getTackling).distinct().count() >= 85);
        }

        @Test
        void shouldReturnAPlayerWithAStealingRatingBetweenZeroAndOne() {
            final Player player = Player.generate(null, null, null, null);

            assertTrue(player.getStealing() >= 0.0 && player.getStealing() <= 1.0);
        }

        @Test
        void shouldChooseTheStealingRatingRandomly() {
            final List<Player> players = new ArrayList<>();

            for (int i = 0; i <= 100; i++) {
                players.add(Player.generate(null, null, null, null));
            }

            assertTrue(players.stream().map(Player::getStealing).distinct().count() >= 85);
        }

        @Test
        void shouldReturnAPlayerWithAPresenceRatingBetweenZeroAndOne() {
            final Player player = Player.generate(null, null, null, null);

            assertTrue(player.getPresence() >= 0.0 && player.getPresence() <= 1.0);
        }

        @Test
        void shouldChooseThePresenceRatingRandomly() {
            final List<Player> players = new ArrayList<>();

            for (int i = 0; i <= 100; i++) {
                players.add(Player.generate(null, null, null, null));
            }

            assertTrue(players.stream().map(Player::getPresence).distinct().count() >= 85);
        }

        @Test
        void shouldReturnAPlayerWithADisciplineRatingBetweenZeroAndOne() {
            final Player player = Player.generate(null, null, null, null);

            assertTrue(player.getDiscipline() >= 0.0 && player.getDiscipline() <= 1.0);
        }

        @Test
        void shouldChooseTheDisciplineRatingRandomly() {
            final List<Player> players = new ArrayList<>();

            for (int i = 0; i <= 100; i++) {
                players.add(Player.generate(null, null, null, null));
            }

            assertTrue(players.stream().map(Player::getDiscipline).distinct().count() >= 85);
        }

        @Test
        void shouldReturnAPlayerWithAPenaltyShotRatingBetweenZeroAndOne() {
            final Player player = Player.generate(null, null, null, null);

            assertTrue(player.getPenaltyShot() >= 0.0 && player.getPenaltyShot() <= 1.0);
        }

        @Test
        void shouldChooseThePenaltyShotRatingRandomly() {
            final List<Player> players = new ArrayList<>();

            for (int i = 0; i <= 100; i++) {
                players.add(Player.generate(null, null, null, null));
            }

            assertTrue(players.stream().map(Player::getPenaltyShot).distinct().count() >= 85);
        }

        @Test
        void shouldReturnAPlayerWithAPenaltyOffenseRatingBetweenZeroAndOne() {
            final Player player = Player.generate(null, null, null, null);

            assertTrue(player.getPenaltyOffense() >= 0.0 && player.getPenaltyOffense() <= 1.0);
        }

        @Test
        void shouldChooseThePenaltyOffenseRatingRandomly() {
            final List<Player> players = new ArrayList<>();

            for (int i = 0; i <= 100; i++) {
                players.add(Player.generate(null, null, null, null));
            }

            assertTrue(players.stream().map(Player::getPenaltyOffense).distinct().count() >= 85);
        }

        @Test
        void shouldReturnAPlayerWithAPenaltyDefenseRatingBetweenZeroAndOne() {
            final Player player = Player.generate(null, null, null, null);

            assertTrue(player.getPenaltyDefense() >= 0.0 && player.getPenaltyDefense() <= 1.0);
        }

        @Test
        void shouldChooseThePenaltyDefenseRatingRandomly() {
            final List<Player> players = new ArrayList<>();

            for (int i = 0; i <= 100; i++) {
                players.add(Player.generate(null, null, null, null));
            }

            assertTrue(players.stream().map(Player::getPenaltyDefense).distinct().count() >= 85);
        }

        @Test
        void shouldReturnAPlayerWithAnEnduranceRatingBetweenZeroAndOne() {
            final Player player = Player.generate(null, null, null, null);

            assertTrue(player.getEndurance() >= 0.0 && player.getEndurance() <= 1.0);
        }

        @Test
        void shouldChooseTheEnduranceRatingRandomly() {
            final List<Player> players = new ArrayList<>();

            for (int i = 0; i <= 100; i++) {
                players.add(Player.generate(null, null, null, null));
            }

            assertTrue(players.stream().map(Player::getEndurance).distinct().count() >= 85);
        }

        @Test
        void shouldReturnAPlayerWithAConfidenceRatingBetweenZeroAndOne() {
            final Player player = Player.generate(null, null, null, null);

            assertTrue(player.getConfidence() >= 0.0 && player.getConfidence() <= 1.0);
        }

        @Test
        void shouldChooseTheConfidenceRatingRandomly() {
            final List<Player> players = new ArrayList<>();

            for (int i = 0; i <= 100; i++) {
                players.add(Player.generate(null, null, null, null));
            }

            assertTrue(players.stream().map(Player::getConfidence).distinct().count() >= 85);
        }

        @Test
        void shouldReturnAPlayerWithAVitalityRatingBetweenZeroAndOne() {
            final Player player = Player.generate(null, null, null, null);

            assertTrue(player.getVitality() >= 0.0 && player.getVitality() <= 1.0);
        }

        @Test
        void shouldChooseTheVitalityRatingRandomly() {
            final List<Player> players = new ArrayList<>();

            for (int i = 0; i <= 100; i++) {
                players.add(Player.generate(null, null, null, null));
            }

            assertTrue(players.stream().map(Player::getVitality).distinct().count() >= 85);
        }

        @Test
        void shouldReturnAPlayerWithADurabilityRatingBetweenZeroAndOne() {
            final Player player = Player.generate(null, null, null, null);

            assertTrue(player.getDurability() >= 0.0 && player.getDurability() <= 1.0);
        }

        @Test
        void shouldChooseTheDurabilityRatingRandomly() {
            final List<Player> players = new ArrayList<>();

            for (int i = 0; i <= 100; i++) {
                players.add(Player.generate(null, null, null, null));
            }

            assertTrue(players.stream().map(Player::getDurability).distinct().count() >= 85);
        }

        @Test
        void shouldReturnAPlayerThatIsNotARookie() {
            final Player player = Player.generate(null, null, null, null);

            assertFalse(valueOf(player.getRookie()));
        }

        @Test
        void shouldReturnAPlayerThatIsNotInjured() {
            final Player player = Player.generate(null, null, null, null);

            assertFalse(valueOf(player.getInjured()));
        }

        @Test
        void shouldReturnAPlayerThatIsNotAFreeAgent() {
            final Player player = Player.generate(null, null, null, null);

            assertFalse(valueOf(player.getFreeAgent()));
        }

        @Test
        void shouldReturnAPlayerThatIsNotSignedToATeam() {
            final Player player = Player.generate(null, null, null, null);

            assertFalse(valueOf(player.getSigned()));
        }

        @Test
        void shouldReturnAPlayerThatIsNotReleased() {
            final Player player = Player.generate(null, null, null, null);

            assertFalse(valueOf(player.getReleased()));
        }

        @Test
        void shouldReturnAPlayerThatIsNotRetired() {
            final Player player = Player.generate(null, null, null, null);

            assertFalse(valueOf(player.getRetired()));
        }

        @Test
        void shouldReturnAPlayerThatIsNotAnAllstarAlternate() {
            final Player player = Player.generate(null, null, null, null);

            assertFalse(valueOf(player.getAllstarAlternate()));
        }

        @Test
        void shouldReturnAPlayerThatHasZeroSeasonsPlayed() {
            final Player player = Player.generate(null, null, null, null);

            assertEquals(0, player.getSeasonsPlayed());
        }
    }
}