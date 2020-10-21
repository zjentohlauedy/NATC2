package org.natc.app.entity.domain;

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

    @Test
    public void getOverallRating_ShouldReturnAverageOfOffenseDefenseIntangibleAndPenaltiesRatings() {
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
    public void getOverallRating_ShouldReturnZeroIfAnyRatingIsNull() {
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

    @Test
    public void generate_ShouldReturnAManager() {
        final Manager manager = Manager.generate(null, null, null, null);

        assertNotNull(manager);
    }

    @Test
    public void generate_ShouldReturnAManagerWithTheGivenManagerId() {
        final Manager manager = Manager.generate(123, null, null, null);

        assertEquals(123, manager.getManagerId());
    }

    @Test
    public void generate_ShouldReturnAManagerWithTheGivenYear() {
        final Manager manager = Manager.generate(123, "1999", null, null);

        assertEquals("1999", manager.getYear());
    }

    @Test
    public void generate_ShouldReturnAManagerWithTheGivenName() {
        final Manager manager = Manager.generate(123, null, "Stan", "Powell");

        assertEquals("Stan", manager.getFirstName());
        assertEquals("Powell", manager.getLastName());
    }

    @Test
    public void generate_ShouldReturnAManagerWithAnAgeBetweenFortyAndFifty() {
        final Manager manager = Manager.generate(null, null, null, null);

        assertTrue(manager.getAge() >= 40 && manager.getAge() <= 50);
    }

    @Test
    public void generate_ShouldChooseTheManagerAgeRandomly() {
        final List<Manager> managers = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            managers.add(Manager.generate(null, null, null, null));
        }

        final Set<Integer> ages = managers.stream().map(Manager::getAge).collect(Collectors.toSet());

        assertEquals(10, ages.size());
    }

    @Test
    public void generate_ShouldReturnAManagerWithAnOffensiveRatingBetweenZeroAndOne() {
        final Manager manager = Manager.generate(null, null, null, null);

        assertTrue(manager.getOffense() >= 0.0 && manager.getOffense() <= 1.0);
    }

    @Test
    public void generate_ShouldChooseTheOffenseRatingRandomly() {
        final List<Manager> managers = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            managers.add(Manager.generate(null, null, null, null));
        }

        final Set<Double> uniqueValues = managers.stream().map(Manager::getOffense).collect(Collectors.toSet());

        assertTrue(uniqueValues.size() >= 85);
    }

    @Test
    public void generate_ShouldReturnAManagerWithADefensiveRatingBetweenZeroAndOne() {
        final Manager manager = Manager.generate(null, null, null, null);

        assertTrue(manager.getDefense() >= 0.0 && manager.getDefense() <= 1.0);
    }

    @Test
    public void generate_ShouldChooseTheDefenseRatingRandomly() {
        final List<Manager> managers = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            managers.add(Manager.generate(null, null, null, null));
        }

        final Set<Double> uniqueValues = managers.stream().map(Manager::getDefense).collect(Collectors.toSet());

        assertTrue(uniqueValues.size() >= 85);
    }

    @Test
    public void generate_ShouldReturnAManagerWithAnIntangibleRatingBetweenZeroAndOne() {
        final Manager manager = Manager.generate(null, null, null, null);

        assertTrue(manager.getIntangible() >= 0.0 && manager.getIntangible() <= 1.0);
    }

    @Test
    public void generate_ShouldChooseTheIntangibleRatingRandomly() {
        final List<Manager> managers = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            managers.add(Manager.generate(null, null, null, null));
        }

        final Set<Double> uniqueValues = managers.stream().map(Manager::getIntangible).collect(Collectors.toSet());

        assertTrue(uniqueValues.size() >= 85);
    }

    @Test
    public void generate_ShouldReturnAManagerWithAPenaltiesRatingBetweenZeroAndOne() {
        final Manager manager = Manager.generate(null, null, null, null);

        assertTrue(manager.getPenalties() >= 0.0 && manager.getPenalties() <= 1.0);
    }

    @Test
    public void generate_ShouldChooseThePenaltiesRatingRandomly() {
        final List<Manager> managers = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            managers.add(Manager.generate(null, null, null, null));
        }

        final Set<Double> uniqueValues = managers.stream().map(Manager::getPenalties).collect(Collectors.toSet());

        assertTrue(uniqueValues.size() >= 85);
    }

    @Test
    public void generate_ShouldReturnAManagerWithAVitalityRatingBetweenZeroAndOne() {
        final Manager manager = Manager.generate(null, null, null, null);

        assertTrue(manager.getVitality() >= 0.0 && manager.getVitality() <= 1.0);
    }

    @Test
    public void generate_ShouldChooseTheVitalityRatingRandomly() {
        final List<Manager> managers = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            managers.add(Manager.generate(null, null, null, null));
        }

        final Set<Double> uniqueValues = managers.stream().map(Manager::getVitality).collect(Collectors.toSet());

        assertTrue(uniqueValues.size() >= 85);
    }

    @Test
    public void generate_ShouldReturnAManagerThatIsNotANewHire() {
        final Manager manager = Manager.generate(null, null, null, null);

        assertFalse(valueOf(manager.getNewHire()));
    }

    @Test
    public void generate_ShouldReturnAManagerThatIsNotReleased() {
        final Manager manager = Manager.generate(null, null, null, null);

        assertFalse(valueOf(manager.getReleased()));
    }

    @Test
    public void generate_ShouldReturnAManagerThatIsNotRetired() {
        final Manager manager = Manager.generate(null, null, null, null);

        assertFalse(valueOf(manager.getRetired()));
    }

    @Test
    public void generate_ShouldReturnAManagerThatHasZeroSeasons() {
        final Manager manager = Manager.generate(null, null, null, null);

        assertEquals(0, manager.getSeasons());
    }

    @Test
    public void generate_ShouldReturnAManagerWithAScoreOfZero() {
        final Manager manager = Manager.generate(null, null, null, null);

        assertEquals(0, manager.getScore());
    }

    @Test
    public void generate_ShouldReturnAManagerThatHasZeroTotalSeasons() {
        final Manager manager = Manager.generate(null, null, null, null);

        assertEquals(0, manager.getTotalSeasons());
    }

    @Test
    public void generate_ShouldReturnAManagerWithATotalScoreOfZero() {
        final Manager manager = Manager.generate(null, null, null, null);

        assertEquals(0, manager.getTotalScore());
    }
}