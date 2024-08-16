package org.natc.app.comparator;

import lombok.AccessLevel;
import lombok.Getter;
import org.natc.app.entity.domain.Player;
import org.natc.app.entity.domain.PlayerRatingAdjustment;

import java.util.Comparator;

@Getter(AccessLevel.PROTECTED)
public class PlayerComparator implements Comparator<Player> {
    protected enum PlayerComparatorMode {
        OFFENSIVE,
        DEFENSIVE,
        INTANGIBLE,
        PENALTY,
        BALANCED
    }

    private final PlayerComparatorMode mode;
    private final PlayerRatingAdjustment[] adjustments;

    protected PlayerComparator(PlayerComparatorMode mode, PlayerRatingAdjustment... adjustments) {
        this.mode = mode;
        this.adjustments = adjustments;
    }

    @Override
    public int compare(final Player p1, final Player p2) {
        switch (mode) {
            case OFFENSIVE: return Double.compare(p1.getAdjustedOffensiveRating(adjustments), p2.getAdjustedOffensiveRating(adjustments));
            case DEFENSIVE: return Double.compare(p1.getAdjustedDefensiveRating(adjustments), p2.getAdjustedDefensiveRating(adjustments));
            case INTANGIBLE: return Double.compare(p1.getAdjustedIntangibleRating(adjustments), p2.getAdjustedIntangibleRating(adjustments));
            case PENALTY: return Double.compare(p1.getAdjustedPenaltyRating(adjustments), p2.getAdjustedPenaltyRating(adjustments));
            case BALANCED: return Double.compare(p1.getAdjustedPerformanceRating(adjustments), p2.getAdjustedPerformanceRating(adjustments));
        }

        return 0;
    }
}
