package org.natc.app.entity.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

            final Set<Integer> ages = players.stream().map(Player::getAge).collect(Collectors.toSet());

            assertEquals(12, ages.size());
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

            final Set<Double> uniqueValues = players.stream().map(Player::getScoring).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
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

            final Set<Double> uniqueValues = players.stream().map(Player::getPassing).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
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

            final Set<Double> uniqueValues = players.stream().map(Player::getBlocking).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
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

            final Set<Double> uniqueValues = players.stream().map(Player::getTackling).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
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

            final Set<Double> uniqueValues = players.stream().map(Player::getStealing).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
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

            final Set<Double> uniqueValues = players.stream().map(Player::getPresence).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
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

            final Set<Double> uniqueValues = players.stream().map(Player::getDiscipline).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
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

            final Set<Double> uniqueValues = players.stream().map(Player::getPenaltyShot).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
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

            final Set<Double> uniqueValues = players.stream().map(Player::getPenaltyOffense).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
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

            final Set<Double> uniqueValues = players.stream().map(Player::getPenaltyDefense).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
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

            final Set<Double> uniqueValues = players.stream().map(Player::getEndurance).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
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

            final Set<Double> uniqueValues = players.stream().map(Player::getConfidence).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
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

            final Set<Double> uniqueValues = players.stream().map(Player::getVitality).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
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

            final Set<Double> uniqueValues = players.stream().map(Player::getDurability).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
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