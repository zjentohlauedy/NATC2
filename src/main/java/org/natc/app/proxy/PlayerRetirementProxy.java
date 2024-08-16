package org.natc.app.proxy;

import org.natc.app.entity.domain.Player;
import org.natc.app.random.RandomNumber;

import static org.natc.app.entity.domain.Player.MAX_FACTOR;

public class PlayerRetirementProxy {

    private final RandomNumber randomNumber;

    public PlayerRetirementProxy(RandomNumber randomNumber) {
        this.randomNumber = randomNumber;
    }

    public Boolean readyToRetire(final Player player) {
        return player.getAgeFactor() < randomNumber.getRandomDouble();
    }

    public Boolean shouldRetire(final Player player) {
        return player.getAgeFactor() < MAX_FACTOR;
    }
}
