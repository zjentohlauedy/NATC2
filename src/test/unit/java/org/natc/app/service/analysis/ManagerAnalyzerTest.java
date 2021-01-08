package org.natc.app.service.analysis;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.ManagerStyle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ManagerAnalyzerTest {

    @InjectMocks
    private ManagerAnalyzer managerAnalyzer;

    @Nested
    class DetermineManagerStyle {

        @Test
        void shouldReturnAManagerStyle() {
            final Manager manager = Manager.builder()
                    .offense(1.0)
                    .defense(1.0)
                    .intangible(1.0)
                    .penalties(1.0)
                    .build();

            final ManagerStyle managerStyle = managerAnalyzer.determineManagerStyle(manager);

            assertNotNull(managerStyle);
        }

        @Test
        void shouldReturnOffensiveWhenOffenseIsTheHighestRatingByAtLeastOneTenth() {
            final Manager manager = Manager.builder()
                    .offense(0.8)
                    .defense(0.7)
                    .intangible(0.7)
                    .penalties(0.7)
                    .build();

            final ManagerStyle managerStyle = managerAnalyzer.determineManagerStyle(manager);

            assertEquals(ManagerStyle.OFFENSIVE, managerStyle);
        }

        @Test
        void shouldReturnDefensiveWhenDefenseIsTheHighestRatingByAtLeastOneTenth() {
            final Manager manager = Manager.builder()
                    .offense(0.3)
                    .defense(0.4)
                    .intangible(0.3)
                    .penalties(0.3)
                    .build();

            final ManagerStyle managerStyle = managerAnalyzer.determineManagerStyle(manager);

            assertEquals(ManagerStyle.DEFENSIVE, managerStyle);
        }

        @Test
        void shouldReturnIntangibleWhenIntangibleIsTheHighestRatingByAtLeastOneTenth() {
            final Manager manager = Manager.builder()
                    .offense(0.7)
                    .defense(0.7)
                    .intangible(0.8)
                    .penalties(0.7)
                    .build();

            final ManagerStyle managerStyle = managerAnalyzer.determineManagerStyle(manager);

            assertEquals(ManagerStyle.INTANGIBLE, managerStyle);
        }

        @Test
        void shouldReturnPenaltiesWhenPenaltiesIsTheHighestRatingByAtLeastOneTenth() {
            final Manager manager = Manager.builder()
                    .offense(0.1)
                    .defense(0.1)
                    .intangible(0.1)
                    .penalties(0.2)
                    .build();

            final ManagerStyle managerStyle = managerAnalyzer.determineManagerStyle(manager);

            assertEquals(ManagerStyle.PENALTIES, managerStyle);
        }

        @Test
        void shouldReturnBalancedWhenOffenseAndDefenseAreTopTwoLessThanOneTenthApart() {
            final Manager manager = Manager.builder()
                    .offense(0.8)
                    .defense(0.75)
                    .intangible(0.7)
                    .penalties(0.7)
                    .build();

            final ManagerStyle managerStyle = managerAnalyzer.determineManagerStyle(manager);

            assertEquals(ManagerStyle.BALANCED, managerStyle);
        }

        @Test
        void shouldReturnBalancedWhenTopTwoAreLessThanOneTenthApartAndThirdIsLessThanOneTenthBehindThem() {
            final Manager manager = Manager.builder()
                    .offense(0.8)
                    .defense(0.4)
                    .intangible(0.75)
                    .penalties(0.7)
                    .build();

            final ManagerStyle managerStyle = managerAnalyzer.determineManagerStyle(manager);

            assertEquals(ManagerStyle.BALANCED, managerStyle);
        }

        @Test
        void shouldReturnOffenseWhenOffenseAndIntangibleAreTopTwoAndOffenseIsHigher() {
            final Manager manager = Manager.builder()
                    .offense(0.8)
                    .defense(0.4)
                    .intangible(0.75)
                    .penalties(0.6)
                    .build();

            final ManagerStyle managerStyle = managerAnalyzer.determineManagerStyle(manager);

            assertEquals(ManagerStyle.OFFENSIVE, managerStyle);
        }

        @Test
        void shouldReturnOffenseWhenOffenseAndIntangibleAreTopTwoAndIntangibleIsHigher() {
            final Manager manager = Manager.builder()
                    .offense(0.75)
                    .defense(0.4)
                    .intangible(0.8)
                    .penalties(0.6)
                    .build();

            final ManagerStyle managerStyle = managerAnalyzer.determineManagerStyle(manager);

            assertEquals(ManagerStyle.OFFENSIVE, managerStyle);
        }

        @Test
        void shouldReturnDefenseWhenDefenseAndIntangibleAreTopTwoAndDefenseIsHigher() {
            final Manager manager = Manager.builder()
                    .offense(0.4)
                    .defense(0.8)
                    .intangible(0.75)
                    .penalties(0.6)
                    .build();

            final ManagerStyle managerStyle = managerAnalyzer.determineManagerStyle(manager);

            assertEquals(ManagerStyle.DEFENSIVE, managerStyle);
        }

        @Test
        void shouldReturnDefenseWhenDefenseAndIntangibleAreTopTwoAndIntangibleIsHigher() {
            final Manager manager = Manager.builder()
                    .offense(0.4)
                    .defense(0.75)
                    .intangible(0.8)
                    .penalties(0.6)
                    .build();

            final ManagerStyle managerStyle = managerAnalyzer.determineManagerStyle(manager);

            assertEquals(ManagerStyle.DEFENSIVE, managerStyle);
        }

        @Test
        void shouldReturnPenaltiesWhenPenaltiesAndIntangibleAreTopTwoAndPenaltiesIsHigher() {
            final Manager manager = Manager.builder()
                    .offense(0.4)
                    .defense(0.6)
                    .intangible(0.75)
                    .penalties(0.8)
                    .build();

            final ManagerStyle managerStyle = managerAnalyzer.determineManagerStyle(manager);

            assertEquals(ManagerStyle.PENALTIES, managerStyle);
        }

        @Test
        void shouldReturnPenaltiesWhenPenaltiesAndIntangibleAreTopTwoAndIntangibleIsHigher() {
            final Manager manager = Manager.builder()
                    .offense(0.4)
                    .defense(0.6)
                    .intangible(0.8)
                    .penalties(0.75)
                    .build();

            final ManagerStyle managerStyle = managerAnalyzer.determineManagerStyle(manager);

            assertEquals(ManagerStyle.PENALTIES, managerStyle);
        }

        @Test
        void shouldReturnOffenseWhenOffenseAndPenaltiesAreTopTwoAndOffenseIsHigher() {
            final Manager manager = Manager.builder()
                    .offense(0.8)
                    .defense(0.4)
                    .intangible(0.6)
                    .penalties(0.75)
                    .build();

            final ManagerStyle managerStyle = managerAnalyzer.determineManagerStyle(manager);

            assertEquals(ManagerStyle.OFFENSIVE, managerStyle);
        }

        @Test
        void shouldReturnOffenseWhenOffenseAndPenaltiesAreTopTwoAndPenaltiesIsHigher() {
            final Manager manager = Manager.builder()
                    .offense(0.75)
                    .defense(0.4)
                    .intangible(0.6)
                    .penalties(0.8)
                    .build();

            final ManagerStyle managerStyle = managerAnalyzer.determineManagerStyle(manager);

            assertEquals(ManagerStyle.OFFENSIVE, managerStyle);
        }

        @Test
        void shouldReturnDefenseWhenDefenseAndPenaltiesAreTopTwoAndDefenseIsHigher() {
            final Manager manager = Manager.builder()
                    .offense(0.4)
                    .defense(0.8)
                    .intangible(0.6)
                    .penalties(0.75)
                    .build();

            final ManagerStyle managerStyle = managerAnalyzer.determineManagerStyle(manager);

            assertEquals(ManagerStyle.DEFENSIVE, managerStyle);
        }

        @Test
        void shouldReturnDefenseWhenDefenseAndPenaltiesAreTopTwoAndPenaltiesIsHigher() {
            final Manager manager = Manager.builder()
                    .offense(0.4)
                    .defense(0.75)
                    .intangible(0.6)
                    .penalties(0.8)
                    .build();

            final ManagerStyle managerStyle = managerAnalyzer.determineManagerStyle(manager);

            assertEquals(ManagerStyle.DEFENSIVE, managerStyle);
        }
    }
}