package org.natc.app.proxy;

import org.natc.app.entity.domain.Player;
import org.natc.app.random.RandomNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.natc.app.entity.domain.Player.MAX_FACTOR;

@Component
public class PlayerRetirementProxy {

    private final RandomNumber randomNumber;

    @Autowired
    public PlayerRetirementProxy(final RandomNumber randomNumber) {
        this.randomNumber = randomNumber;
    }

    public Boolean readyToRetire(final Player player) {
        return player.getAgeFactor() < randomNumber.getRandomDouble();
    }

    public Boolean shouldRetire(final Player player) {
        return player.getAgeFactor() < MAX_FACTOR;
    }
}
