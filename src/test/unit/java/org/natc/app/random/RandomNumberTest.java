package org.natc.app.random;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RandomNumberTest {

    @InjectMocks
    private RandomNumber randomNumber;

    @Nested
    class GetRandomDouble {

        @Test
        void shouldReturnADoubleBetweenZeroAndOne() {
            final Double result = randomNumber.getRandomDouble();

            assertTrue(result >= 0.0);
            assertTrue(result <= 1.0);
        }

        @Test
        void shouldReturnADifferentNumberEachTime() {
            final Double firstResult = randomNumber.getRandomDouble();
            final Double secondResult = randomNumber.getRandomDouble();

            assertNotEquals(firstResult, secondResult);
        }
    }
}