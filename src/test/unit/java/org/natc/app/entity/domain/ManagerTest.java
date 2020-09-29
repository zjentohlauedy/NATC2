package org.natc.app.entity.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}