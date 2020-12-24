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

class ManagerTest {

    @Nested
    class Generate {

        @Test
        void ShouldReturnAManager() {
            final Manager manager = Manager.generate(null, null, null, null);

            assertNotNull(manager);
        }

        @Test
        void ShouldReturnAManagerWithTheGivenManagerId() {
            final Manager manager = Manager.generate(123, null, null, null);

            assertEquals(123, manager.getManagerId());
        }

        @Test
        void ShouldReturnAManagerWithTheGivenYear() {
            final Manager manager = Manager.generate(123, "1999", null, null);

            assertEquals("1999", manager.getYear());
        }

        @Test
        void ShouldReturnAManagerWithTheGivenName() {
            final Manager manager = Manager.generate(123, null, "Stan", "Powell");

            assertEquals("Stan", manager.getFirstName());
            assertEquals("Powell", manager.getLastName());
        }

        @Test
        void ShouldReturnAManagerWithAnAgeBetweenFortyAndFifty() {
            final Manager manager = Manager.generate(null, null, null, null);

            assertTrue(manager.getAge() >= 40 && manager.getAge() <= 50);
        }

        @Test
        void ShouldChooseTheManagerAgeRandomly() {
            final List<Manager> managers = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                managers.add(Manager.generate(null, null, null, null));
            }

            final Set<Integer> ages = managers.stream().map(Manager::getAge).collect(Collectors.toSet());

            assertEquals(10, ages.size());
        }

        @Test
        void ShouldReturnAManagerWithAnOffensiveRatingBetweenZeroAndOne() {
            final Manager manager = Manager.generate(null, null, null, null);

            assertTrue(manager.getOffense() >= 0.0 && manager.getOffense() <= 1.0);
        }

        @Test
        void ShouldChooseTheOffenseRatingRandomly() {
            final List<Manager> managers = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                managers.add(Manager.generate(null, null, null, null));
            }

            final Set<Double> uniqueValues = managers.stream().map(Manager::getOffense).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
        }

        @Test
        void ShouldReturnAManagerWithADefensiveRatingBetweenZeroAndOne() {
            final Manager manager = Manager.generate(null, null, null, null);

            assertTrue(manager.getDefense() >= 0.0 && manager.getDefense() <= 1.0);
        }

        @Test
        void ShouldChooseTheDefenseRatingRandomly() {
            final List<Manager> managers = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                managers.add(Manager.generate(null, null, null, null));
            }

            final Set<Double> uniqueValues = managers.stream().map(Manager::getDefense).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
        }

        @Test
        void ShouldReturnAManagerWithAnIntangibleRatingBetweenZeroAndOne() {
            final Manager manager = Manager.generate(null, null, null, null);

            assertTrue(manager.getIntangible() >= 0.0 && manager.getIntangible() <= 1.0);
        }

        @Test
        void ShouldChooseTheIntangibleRatingRandomly() {
            final List<Manager> managers = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                managers.add(Manager.generate(null, null, null, null));
            }

            final Set<Double> uniqueValues = managers.stream().map(Manager::getIntangible).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
        }

        @Test
        void ShouldReturnAManagerWithAPenaltiesRatingBetweenZeroAndOne() {
            final Manager manager = Manager.generate(null, null, null, null);

            assertTrue(manager.getPenalties() >= 0.0 && manager.getPenalties() <= 1.0);
        }

        @Test
        void ShouldChooseThePenaltiesRatingRandomly() {
            final List<Manager> managers = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                managers.add(Manager.generate(null, null, null, null));
            }

            final Set<Double> uniqueValues = managers.stream().map(Manager::getPenalties).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
        }

        @Test
        void ShouldReturnAManagerWithAVitalityRatingBetweenZeroAndOne() {
            final Manager manager = Manager.generate(null, null, null, null);

            assertTrue(manager.getVitality() >= 0.0 && manager.getVitality() <= 1.0);
        }

        @Test
        void ShouldChooseTheVitalityRatingRandomly() {
            final List<Manager> managers = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                managers.add(Manager.generate(null, null, null, null));
            }

            final Set<Double> uniqueValues = managers.stream().map(Manager::getVitality).collect(Collectors.toSet());

            assertTrue(uniqueValues.size() >= 85);
        }

        @Test
        void ShouldReturnAManagerThatIsNotANewHire() {
            final Manager manager = Manager.generate(null, null, null, null);

            assertFalse(valueOf(manager.getNewHire()));
        }

        @Test
        void ShouldReturnAManagerThatIsNotReleased() {
            final Manager manager = Manager.generate(null, null, null, null);

            assertFalse(valueOf(manager.getReleased()));
        }

        @Test
        void ShouldReturnAManagerThatIsNotRetired() {
            final Manager manager = Manager.generate(null, null, null, null);

            assertFalse(valueOf(manager.getRetired()));
        }

        @Test
        void ShouldReturnAManagerThatHasZeroSeasons() {
            final Manager manager = Manager.generate(null, null, null, null);

            assertEquals(0, manager.getSeasons());
        }

        @Test
        void ShouldReturnAManagerWithAScoreOfZero() {
            final Manager manager = Manager.generate(null, null, null, null);

            assertEquals(0, manager.getScore());
        }

        @Test
        void ShouldReturnAManagerThatHasZeroTotalSeasons() {
            final Manager manager = Manager.generate(null, null, null, null);

            assertEquals(0, manager.getTotalSeasons());
        }

        @Test
        void ShouldReturnAManagerWithATotalScoreOfZero() {
            final Manager manager = Manager.generate(null, null, null, null);

            assertEquals(0, manager.getTotalScore());
        }
    }

    @Nested
    class GetOverallRating {

        @Test
        void ShouldReturnAverageOfOffenseDefenseIntangibleAndPenaltiesRatings() {
            assertEquals(1.0,
                    Manager.builder().offense(1.0).defense(1.0).intangible(1.0).penalties(1.0).build().getOverallRating(),
                    0.0001
            );
            assertEquals(0.5,
                    Manager.builder().offense(0.8).defense(0.3).intangible(0.7).penalties(0.2).build().getOverallRating(),
                    0.0001
            );
            assertEquals(0.75,
                    Manager.builder().offense(0.6).defense(0.7).intangible(0.8).penalties(0.9).build().getOverallRating(),
                    0.0001
            );
            assertEquals(0.4,
                    Manager.builder().offense(0.1).defense(0.3).intangible(0.5).penalties(0.7).build().getOverallRating(),
                    0.0001
            );
            assertEquals(0.47175,
                    Manager.builder().offense(0.321).defense(0.123).intangible(0.456).penalties(0.987).build().getOverallRating(),
                    0.0001
            );
        }

        @Test
        void ShouldReturnZeroIfAnyRatingIsNull() {
            assertEquals(0,
                    Manager.builder().offense(1.0).defense(1.0).intangible(1.0).penalties(null).build().getOverallRating()
            );
            assertEquals(0,
                    Manager.builder().offense(1.0).defense(1.0).intangible(null).penalties(1.0).build().getOverallRating()
            );
            assertEquals(0,
                    Manager.builder().offense(1.0).defense(null).intangible(1.0).penalties(1.0).build().getOverallRating()
            );
            assertEquals(0,
                    Manager.builder().offense(1.0).defense(null).intangible(1.0).penalties(null).build().getOverallRating()
            );
            assertEquals(0,
                    Manager.builder().offense(1.0).defense(null).intangible(null).penalties(1.0).build().getOverallRating()
            );
            assertEquals(0,
                    Manager.builder().offense(1.0).defense(null).intangible(null).penalties(null).build().getOverallRating()
            );
            assertEquals(0,
                    Manager.builder().offense(null).defense(1.0).intangible(1.0).penalties(1.0).build().getOverallRating()
            );
            assertEquals(0,
                    Manager.builder().offense(null).defense(1.0).intangible(1.0).penalties(null).build().getOverallRating()
            );
            assertEquals(0,
                    Manager.builder().offense(null).defense(1.0).intangible(null).penalties(1.0).build().getOverallRating()
            );
            assertEquals(0,
                    Manager.builder().offense(null).defense(null).intangible(1.0).penalties(1.0).build().getOverallRating()
            );
            assertEquals(0,
                    Manager.builder().offense(null).defense(null).intangible(1.0).penalties(null).build().getOverallRating()
            );
            assertEquals(0,
                    Manager.builder().offense(null).defense(null).intangible(null).penalties(1.0).build().getOverallRating()
            );
            assertEquals(0,
                    Manager.builder().offense(null).defense(null).intangible(null).penalties(null).build().getOverallRating()
            );
        }
    }

    @Nested
    class GetPerformanceRating {

        @Test
        void ShouldReturnRatingOfOneIfManagerHasZeroSeasons() {
            assertEquals(1.0, Manager.builder().seasons(0).build().getPerformanceRating());
        }

        @Nested
        class WhenSeasonsAndTotalSeasonsAreTheSame {
            @Test
            public void ShouldReturnScoreDividedBySeasons() {
                assertEquals((12.0 / 5.0),
                        Manager.builder().score(12).seasons(5).totalScore(12).totalSeasons(5).build().getPerformanceRating()
                );
                assertEquals((3.0 / 7.0),
                        Manager.builder().score(3).seasons(7).totalScore(3).totalSeasons(7).build().getPerformanceRating()
                );
                assertEquals((15.0 / 11.0),
                        Manager.builder().score(15).seasons(11).totalScore(15).totalSeasons(11).build().getPerformanceRating()
                );
                assertEquals((6.0 / 3.0),
                        Manager.builder().score(6).seasons(3).totalScore(6).totalSeasons(3).build().getPerformanceRating()
                );
                assertEquals((2.0 / 8.0),
                        Manager.builder().score(2).seasons(8).totalScore(2).totalSeasons(8).build().getPerformanceRating()
                );
            }
        }

        @Test
        public void ShouldReturnTheGreaterOfSeasonsScoreRatioOrTotalSeasonsTotalScoreRatio() {
            assertEquals((12.0 / 5.0),
                    Manager.builder().score(12).seasons(5).totalScore(10).totalSeasons(8).build().getPerformanceRating()
            );
            assertEquals((3.0 / 7.0),
                    Manager.builder().score(3).seasons(7).totalScore(1).totalSeasons(10).build().getPerformanceRating()
            );
            assertEquals((15.0 / 11.0),
                    Manager.builder().score(5).seasons(4).totalScore(15).totalSeasons(11).build().getPerformanceRating()
            );
            assertEquals((2.0 / 8.0),
                    Manager.builder().score(0).seasons(3).totalScore(2).totalSeasons(8).build().getPerformanceRating()
            );
        }
    }
}