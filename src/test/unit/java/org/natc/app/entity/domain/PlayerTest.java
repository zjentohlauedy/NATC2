package org.natc.app.entity.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {

    @Test
    public void getOffensiveRating_ShouldReturnAverageOfScoringPassingAndBlockingRatings() {
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
    public void getOffensiveRating_ShouldReturnZeroIfAnyOffensiveRatingIsNull() {
        assertEquals(0, Player.builder().scoring(1.0).passing(1.0).blocking(null).build().getOffensiveRating());
        assertEquals(0, Player.builder().scoring(1.0).passing(null).blocking(1.0).build().getOffensiveRating());
        assertEquals(0, Player.builder().scoring(1.0).passing(null).blocking(null).build().getOffensiveRating());
        assertEquals(0, Player.builder().scoring(null).passing(1.0).blocking(1.0).build().getOffensiveRating());
        assertEquals(0, Player.builder().scoring(null).passing(1.0).blocking(null).build().getOffensiveRating());
        assertEquals(0, Player.builder().scoring(null).passing(null).blocking(1.0).build().getOffensiveRating());
        assertEquals(0, Player.builder().scoring(null).passing(null).blocking(null).build().getOffensiveRating());
    }

    @Test
    public void getDefensiveRating_ShouldReturnAverageOfTacklingStealingAndPresenceRatings() {
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
    public void getDefensiveRating_ShouldReturnZeroIfAnyDefensiveRatingIsNull() {
        assertEquals(0.0, Player.builder().tackling(1.0).stealing(1.0).presence(null).build().getDefensiveRating());
        assertEquals(0.0, Player.builder().tackling(1.0).stealing(null).presence(1.0).build().getDefensiveRating());
        assertEquals(0.0, Player.builder().tackling(1.0).stealing(null).presence(null).build().getDefensiveRating());
        assertEquals(0.0, Player.builder().tackling(null).stealing(1.0).presence(1.0).build().getDefensiveRating());
        assertEquals(0.0, Player.builder().tackling(null).stealing(1.0).presence(null).build().getDefensiveRating());
        assertEquals(0.0, Player.builder().tackling(null).stealing(null).presence(1.0).build().getDefensiveRating());
        assertEquals(0.0, Player.builder().tackling(null).stealing(null).presence(null).build().getDefensiveRating());
    }

    @Test
    public void getIntangibleRating_ShouldReturnAverageOfBlockingPresenceDisciplineAndEndurance() {
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
    public void getIntangibleRating_ShouldReturnZeroIfAnyIntangibleRatingIsNull() {
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

    @Test
    public void getPenaltyRating_ShouldReturnAverageOfPenaltyShotPenaltyOffenseAndPenaltyDefense() {
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
    public void getPenaltyRating_ShouldReturnZeroIfAnyPenaltyRatingIsNull() {
        assertEquals(0.0, Player.builder().penaltyShot(1.0).penaltyOffense(1.0).penaltyDefense(null).build().getPenaltyRating());
        assertEquals(0.0, Player.builder().penaltyShot(1.0).penaltyOffense(null).penaltyDefense(1.0).build().getPenaltyRating());
        assertEquals(0.0, Player.builder().penaltyShot(1.0).penaltyOffense(null).penaltyDefense(null).build().getPenaltyRating());
        assertEquals(0.0, Player.builder().penaltyShot(null).penaltyOffense(1.0).penaltyDefense(1.0).build().getPenaltyRating());
        assertEquals(0.0, Player.builder().penaltyShot(null).penaltyOffense(1.0).penaltyDefense(null).build().getPenaltyRating());
        assertEquals(0.0, Player.builder().penaltyShot(null).penaltyOffense(null).penaltyDefense(1.0).build().getPenaltyRating());
        assertEquals(0.0, Player.builder().penaltyShot(null).penaltyOffense(null).penaltyDefense(null).build().getPenaltyRating());
    }

    @Test
    public void getPerformanceRating_ShouldReturnAverageOfScoringPassingBlockingTacklingStealingPresenceDisciplineAndEndurance() {
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
    public void getPerformanceRating_ShouldReturnZeroIfAnyRatingIsNull() {
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