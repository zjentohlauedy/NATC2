package org.natc.app.random;

import org.springframework.stereotype.Component;

@Component
public class RandomNumber {
    public Double getRandomDouble() {
        return Math.random();
    }
}
