package org.natc.app.proxy;

import org.natc.app.entity.domain.Player;
import org.natc.app.random.RandomNumber;

public class PlayerRetirementProxy {

    private final RandomNumber randomNumber;

    public PlayerRetirementProxy(RandomNumber randomNumber) {
        this.randomNumber = randomNumber;
    }

    public Boolean readyToRetire(final Player player) {
        final double retirementChance = getRetirementChance(player);

        return retirementChance < randomNumber.getRandomDouble();
    }

    public Boolean shouldRetire(final Player player) {
        final double retirementChance = getRetirementChance(player);

        return retirementChance < 1.0;
    }

    private double getRetirementChance(final Player player) {
        final Integer cutoffAge = (int)Math.ceil(20.0 + (15.0 * player.getVitality()));

        if (player.getAge() <= cutoffAge) return 1.0;

        // TODO: with current logic low vitality players with age out at a slower rate than higher vitality players
        //  although the retirement changes start earlier. the final part of the calculation should be:
        //  (0.05 *(1.0 - player.getVitality()))
        return (1.0 - ((double)(player.getAge() - cutoffAge) * (0.05 + (0.05 * player.getVitality()))));
    }
}
