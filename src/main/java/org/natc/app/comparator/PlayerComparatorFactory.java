package org.natc.app.comparator;

import org.natc.app.entity.domain.ManagerStyle;
import org.natc.app.entity.domain.PlayerRatingAdjustment;

import java.util.Map;

import static java.util.Map.entry;

public class PlayerComparatorFactory {
    private static final Map<ManagerStyle, PlayerComparator.PlayerComparatorMode> managerStyleModeMap = Map.ofEntries(
            entry(ManagerStyle.OFFENSIVE, PlayerComparator.PlayerComparatorMode.OFFENSIVE),
            entry(ManagerStyle.DEFENSIVE, PlayerComparator.PlayerComparatorMode.DEFENSIVE),
            entry(ManagerStyle.INTANGIBLE, PlayerComparator.PlayerComparatorMode.INTANGIBLE),
            entry(ManagerStyle.PENALTIES, PlayerComparator.PlayerComparatorMode.PENALTY),
            entry(ManagerStyle.BALANCED, PlayerComparator.PlayerComparatorMode.BALANCED)
    );

    public PlayerComparator getPlayerComparatorForManager(ManagerStyle managerStyle, PlayerRatingAdjustment... adjustments) {
        return new PlayerComparator(managerStyleModeMap.get(managerStyle), adjustments);
    }
}
