package org.natc.app.proxy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.Player;
import org.natc.app.random.RandomNumber;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.natc.app.entity.domain.Player.MAX_RATING;
import static org.natc.app.entity.domain.Player.MIN_RATING;

@ExtendWith(MockitoExtension.class)
class PlayerRetirementProxyTest {

    @Mock
    private RandomNumber randomNumber;

    @InjectMocks
    private PlayerRetirementProxy proxy;

    @Nested
    class ReadyToRetire {

        @Test
        void shouldCallRandomNumberGenerator() {
            proxy.readyToRetire(Player.builder().age(99).vitality(0.5).build());

            verify(randomNumber).getRandomDouble();
        }

        // min vitality

        @Test
        void shouldReturnFalseIfPlayerAgeIsUnderTwentyOneAndHasMinimumVitalityWithMaxRandomReturned() {
            lenient().when(randomNumber.getRandomDouble()).thenReturn(1.0);

            assertFalse(proxy.readyToRetire(Player.builder().age(18).vitality(MIN_RATING).build()));
            assertFalse(proxy.readyToRetire(Player.builder().age(19).vitality(MIN_RATING).build()));
            assertFalse(proxy.readyToRetire(Player.builder().age(20).vitality(MIN_RATING).build()));
        }

        @Test
        void shouldReturnTrueIfPlayerIsTwentyOneAndHasMinimumVitalityWithMaxRandomReturned() {
            when(randomNumber.getRandomDouble()).thenReturn(1.0);

            assertTrue(proxy.readyToRetire(Player.builder().age(21).vitality(MIN_RATING).build()));
        }

        @Test
        void shouldReturnFalseIfPlayerIsTwentyOneAndHasMinimumVitalityWithNearMaxRandomReturned() {
            when(randomNumber.getRandomDouble()).thenReturn(0.95);

            assertFalse(proxy.readyToRetire(Player.builder().age(21).vitality(MIN_RATING).build()));
        }

        @Test
        void shouldReturnFalseIfPlayerIsFortyAndHasMinimumVitalityWithMinRandomReturned() {
            when(randomNumber.getRandomDouble()).thenReturn(0.0);

            assertFalse(proxy.readyToRetire(Player.builder().age(40).vitality(MIN_RATING).build()));
        }

        @Test
        void shouldReturnTrueIfPlayerIsFortyAndHasMinimumVitalityWithNearMinRandomReturned() {
            when(randomNumber.getRandomDouble()).thenReturn(0.05);

            assertTrue(proxy.readyToRetire(Player.builder().age(40).vitality(MIN_RATING).build()));
        }

        @Test
        void shouldReturnTrueIfPlayerAgeIsOverFortyAndHasMinimumVitalityWithMinRandomReturned() {
            when(randomNumber.getRandomDouble()).thenReturn(0.0);

            assertTrue(proxy.readyToRetire(Player.builder().age(41).vitality(MIN_RATING).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(42).vitality(MIN_RATING).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(43).vitality(MIN_RATING).build()));
        }

        // max vitality

        @Test
        void shouldReturnFalseIfPlayerAgeIsUnderThirtySixAndHasMaximumVitalityWithMaxRandomReturned() {
            lenient().when(randomNumber.getRandomDouble()).thenReturn(1.0);

            assertFalse(proxy.readyToRetire(Player.builder().age(33).vitality(MAX_RATING).build()));
            assertFalse(proxy.readyToRetire(Player.builder().age(34).vitality(MAX_RATING).build()));
            assertFalse(proxy.readyToRetire(Player.builder().age(35).vitality(MAX_RATING).build()));
        }

        @Test
        void shouldReturnTrueIfPlayerIsThirtySixAndHasMaximumVitalityWithMaxRandomReturned() {
            when(randomNumber.getRandomDouble()).thenReturn(1.0);

            assertTrue(proxy.readyToRetire(Player.builder().age(36).vitality(MAX_RATING).build()));
        }

        @Test
        void shouldReturnFalseIfPlayerIsThirtySixAndHasMaximumVitalityWithNearMaxRandomReturned() {
            when(randomNumber.getRandomDouble()).thenReturn(0.9);

            assertFalse(proxy.readyToRetire(Player.builder().age(36).vitality(MAX_RATING).build()));
        }

        @Test
        void shouldReturnFalseIfPlayerIsFortyFiveAndHasMaximumVitalityWithMinRandomReturned() {
            when(randomNumber.getRandomDouble()).thenReturn(0.0);

            assertFalse(proxy.readyToRetire(Player.builder().age(45).vitality(MAX_RATING).build()));
        }

        @Test
        void shouldReturnTrueIfPlayerIsFortyFiveAndHasMaximumVitalityWithNearMinRandomReturned() {
            when(randomNumber.getRandomDouble()).thenReturn(0.1);

            assertTrue(proxy.readyToRetire(Player.builder().age(45).vitality(MAX_RATING).build()));
        }

        @Test
        void shouldReturnTrueIfPlayerAgeIsOverFortyFiveAndHasMaximumVitalityWithMinRandomReturned() {
            when(randomNumber.getRandomDouble()).thenReturn(0.0);

            assertTrue(proxy.readyToRetire(Player.builder().age(46).vitality(MAX_RATING).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(47).vitality(MAX_RATING).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(48).vitality(MAX_RATING).build()));
        }

        @Test
        void shouldRetirePlayersWithLowerVitalityBeforeHigherVitalityGivenSameAgeAndSameRandomNumber() {
            when(randomNumber.getRandomDouble()).thenReturn(0.5);

            assertFalse(proxy.readyToRetire(Player.builder().age(35).vitality(1.0).build()));
            assertFalse(proxy.readyToRetire(Player.builder().age(35).vitality(0.9).build()));
            assertFalse(proxy.readyToRetire(Player.builder().age(35).vitality(0.8).build()));
            assertFalse(proxy.readyToRetire(Player.builder().age(35).vitality(0.7).build()));
            assertFalse(proxy.readyToRetire(Player.builder().age(35).vitality(0.6).build()));

            assertTrue(proxy.readyToRetire(Player.builder().age(35).vitality(0.5).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(35).vitality(0.4).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(35).vitality(0.3).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(35).vitality(0.2).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(35).vitality(0.1).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(35).vitality(0.0).build()));
        }

        @Test
        void shouldRetirePlayersWithHigherAgeBeforeLowerAgeGivenSameVitalityAndSameRandomNumber() {
            when(randomNumber.getRandomDouble()).thenReturn(0.5);

            assertFalse(proxy.readyToRetire(Player.builder().age(30).vitality(0.5).build()));
            assertFalse(proxy.readyToRetire(Player.builder().age(31).vitality(0.5).build()));
            assertFalse(proxy.readyToRetire(Player.builder().age(32).vitality(0.5).build()));
            assertFalse(proxy.readyToRetire(Player.builder().age(33).vitality(0.5).build()));
            assertFalse(proxy.readyToRetire(Player.builder().age(34).vitality(0.5).build()));

            assertTrue(proxy.readyToRetire(Player.builder().age(35).vitality(0.5).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(36).vitality(0.5).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(37).vitality(0.5).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(38).vitality(0.5).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(39).vitality(0.5).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(40).vitality(0.5).build()));
        }

        @Test
        void shouldRetirePlayersWhenRandomNumberIsHigherGivenSameAgeAndVitality() {
            when(randomNumber.getRandomDouble())
                    .thenReturn(0.0)
                    .thenReturn(0.1)
                    .thenReturn(0.2)
                    .thenReturn(0.3)
                    .thenReturn(0.4)
                    .thenReturn(0.5)
                    .thenReturn(0.6)
                    .thenReturn(0.7)
                    .thenReturn(0.8)
                    .thenReturn(0.9)
                    .thenReturn(1.0);

            assertFalse(proxy.readyToRetire(Player.builder().age(35).vitality(0.5).build()));
            assertFalse(proxy.readyToRetire(Player.builder().age(35).vitality(0.5).build()));
            assertFalse(proxy.readyToRetire(Player.builder().age(35).vitality(0.5).build()));
            assertFalse(proxy.readyToRetire(Player.builder().age(35).vitality(0.5).build()));
            assertFalse(proxy.readyToRetire(Player.builder().age(35).vitality(0.5).build()));

            assertTrue(proxy.readyToRetire(Player.builder().age(35).vitality(0.5).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(35).vitality(0.5).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(35).vitality(0.5).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(35).vitality(0.5).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(35).vitality(0.5).build()));
            assertTrue(proxy.readyToRetire(Player.builder().age(45).vitality(0.5).build()));

            verify(randomNumber, times(11)).getRandomDouble();
        }
    }
    
    @Nested
    class ShouldRetire {
        // min vitality

        @Test
        void shouldReturnFalseIfPlayerAgeIsUnderTwentyOneAndHasMinimumVitality() {
            assertFalse(proxy.shouldRetire(Player.builder().age(18).vitality(MIN_RATING).build()));
            assertFalse(proxy.shouldRetire(Player.builder().age(19).vitality(MIN_RATING).build()));
            assertFalse(proxy.shouldRetire(Player.builder().age(20).vitality(MIN_RATING).build()));
        }

        @Test
        void shouldReturnTrueIfPlayerAgeIsOverTwentyAndHasMinimumVitality() {
            assertTrue(proxy.shouldRetire(Player.builder().age(21).vitality(MIN_RATING).build()));
            assertTrue(proxy.shouldRetire(Player.builder().age(22).vitality(MIN_RATING).build()));
            assertTrue(proxy.shouldRetire(Player.builder().age(23).vitality(MIN_RATING).build()));
        }

        // max vitality

        @Test
        void shouldReturnFalseIfPlayerAgeIsUnderThirtySixAndHasMaximumVitality() {
            assertFalse(proxy.shouldRetire(Player.builder().age(33).vitality(MAX_RATING).build()));
            assertFalse(proxy.shouldRetire(Player.builder().age(34).vitality(MAX_RATING).build()));
            assertFalse(proxy.shouldRetire(Player.builder().age(35).vitality(MAX_RATING).build()));
        }

        @Test
        void shouldReturnTrueIfPlayerAgeIsOverThirtyFiveAndHasMaximumVitality() {
            assertTrue(proxy.shouldRetire(Player.builder().age(36).vitality(MAX_RATING).build()));
            assertTrue(proxy.shouldRetire(Player.builder().age(37).vitality(MAX_RATING).build()));
            assertTrue(proxy.shouldRetire(Player.builder().age(38).vitality(MAX_RATING).build()));
        }
    }
}