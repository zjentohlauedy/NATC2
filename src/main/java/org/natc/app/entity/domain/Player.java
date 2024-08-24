package org.natc.app.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.Arrays;

import static org.natc.app.entity.domain.PlayerRatingAdjustment.*;

@Entity(name = "players_t")
@IdClass(PlayerId.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {
    public static final int STARTING_AGE = 17;
    public static final int AGE_VARIATION = 12;
    public static final double MIN_CUTOFF_AGE = 20.0;
    public static final double MAX_VITAL_AGE_EXTENSION = 15.0;
    public static final double AGING_RATE = 0.05;
    public static final double MIN_RATING = 0.0;
    public static final double MAX_RATING = 1.0;
    public static final double MAX_FACTOR = 1.0;
    public static final double MIN_FACTOR = 0.0;
    public static final double FATIGUE_TIRED_POINT = 1.0;

    @Id
    private Integer playerId;
    @Setter
    private Integer teamId;
    @Id
    private String year;
    private String firstName;
    private String lastName;
    private Integer age;
    private Double scoring;
    private Double passing;
    private Double blocking;
    private Double tackling;
    private Double stealing;
    private Double presence;
    private Double discipline;
    private Double penaltyShot;
    private Double penaltyOffense;
    private Double penaltyDefense;
    private Double endurance;
    private Double confidence;
    private Double vitality;
    @Transient
    private Double fatigue;
    private Double durability;
    private Integer rookie;
    private Integer injured;
    private LocalDate returnDate;
    @Setter
    private Integer freeAgent;
    @Setter
    private Integer signed;
    @Setter
    private Integer released;
    @Setter
    private Integer retired;
    @Setter
    private Integer formerTeamId;
    private Integer allstarTeamId;
    private Integer award;
    private Integer draftPick;
    private Integer seasonsPlayed;
    private Integer allstarAlternate;

    public static Player generate(final Integer playerId, final String year, final String firstName, final String lastName) {
        return Player.builder()
                .playerId(playerId)
                .year(year)
                .firstName(firstName)
                .lastName(lastName)
                .age((int)Math.floor((Math.random() * AGE_VARIATION) + STARTING_AGE + 1))
                .scoring(Math.random())
                .passing(Math.random())
                .blocking(Math.random())
                .tackling(Math.random())
                .stealing(Math.random())
                .presence(Math.random())
                .discipline(Math.random())
                .penaltyShot(Math.random())
                .penaltyOffense(Math.random())
                .penaltyDefense(Math.random())
                .endurance(Math.random())
                .confidence(Math.random())
                .vitality(Math.random())
                .durability(Math.random())
                .rookie(0)
                .injured(0)
                .freeAgent(0)
                .signed(0)
                .released(0)
                .retired(0)
                .allstarAlternate(0)
                .seasonsPlayed(0)
                .build();
    }

    public Double getOffensiveRating() {
        if (scoring == null) return MIN_RATING;
        if (passing == null) return MIN_RATING;
        if (blocking == null) return MIN_RATING;

        return (scoring + passing + blocking) / 3.0;
    }

    public Double getAdjustedOffensiveRating(PlayerRatingAdjustment... adjustments) {
        return adjustRating(getOffensiveRating(), adjustments);
    }

    public Double getDefensiveRating() {
        if (tackling == null) return MIN_RATING;
        if (stealing == null) return MIN_RATING;
        if (presence == null) return MIN_RATING;

        return (tackling + stealing + presence) / 3.0;
    }

    public Double getAdjustedDefensiveRating(PlayerRatingAdjustment... adjustments) {
        return adjustRating(getDefensiveRating(), adjustments);
    }

    public Double getIntangibleRating() {
        if (blocking == null) return MIN_RATING;
        if (presence == null) return MIN_RATING;
        if (discipline == null) return MIN_RATING;
        if (endurance == null) return MIN_RATING;

        return (blocking + presence + discipline + endurance) / 4.0;
    }

    public Double getAdjustedIntangibleRating(PlayerRatingAdjustment... adjustments) {
        return adjustRating(getIntangibleRating(), adjustments);
    }

    public Double getPenaltyRating() {
        if (penaltyShot == null) return MIN_RATING;
        if (penaltyOffense == null) return MIN_RATING;
        if (penaltyDefense == null) return MIN_RATING;

        return (penaltyShot + penaltyOffense + penaltyDefense) / 3.0;
    }

    public Double getAdjustedPenaltyRating(PlayerRatingAdjustment... adjustments) {
        return adjustRating(getPenaltyRating(), adjustments);
    }

    public Double getPerformanceRating() {
        if (scoring == null) return MIN_RATING;
        if (passing == null) return MIN_RATING;
        if (blocking == null) return MIN_RATING;
        if (tackling == null) return MIN_RATING;
        if (stealing == null) return MIN_RATING;
        if (presence == null) return MIN_RATING;
        if (discipline == null) return MIN_RATING;
        if (endurance == null) return MIN_RATING;

        return (scoring + passing + blocking +
                tackling + stealing + presence +
                discipline + endurance) / 8.0;
    }

    public Double getAdjustedPerformanceRating(PlayerRatingAdjustment... adjustments) {
        return adjustRating(getPerformanceRating(), adjustments);
    }

    public Double getAgeFactor() {
        final Integer cutoffAge = (int)Math.ceil(MIN_CUTOFF_AGE + (MAX_VITAL_AGE_EXTENSION * vitality));

        if (age <= cutoffAge) return MAX_FACTOR;

        // TODO: with current logic low vitality players with age out at a slower rate than higher vitality players
        //  although the retirement changes start earlier. the final part of the calculation should be:
        //  (0.05 *(1.0 - player.getVitality()))
        return (MAX_FACTOR - ((double)(age - cutoffAge) * (AGING_RATE + (AGING_RATE * vitality))));
    }

    protected Double getConfidenceFactor() {
        final int yearsPlayed = age - STARTING_AGE;

        return MAX_FACTOR - ((MAX_RATING - confidence) / (1.0 + (yearsPlayed * yearsPlayed)));
    }

    protected Double getFatigueFactor() {
        if (fatigue <= FATIGUE_TIRED_POINT) {
            return MAX_FACTOR;
        }

        // TODO: this code will incorrectly set the factor to 0 when fatigue reaches the tired point
        //  then increase as fatigue increases. Instead the factor should slowly be reduced as fatigue
        //  increases past the tired point.
        final Double factor = MAX_FACTOR - (fatigue - FATIGUE_TIRED_POINT);

        return factor > MIN_FACTOR ? factor : MIN_FACTOR;
    }

    private Double adjustRating(Double rating, PlayerRatingAdjustment... adjustments) {
        if (Arrays.stream(adjustments).anyMatch(adj -> adj == APPLY_AGE)) {
            rating *= getAgeFactor();
        }

        if (Arrays.stream(adjustments).anyMatch(adj -> adj == APPLY_CONFIDENCE)) {
            rating *= getConfidenceFactor();
        }

        if (Arrays.stream(adjustments).anyMatch(adj -> adj == APPLY_FATIGUE)) {
            rating *= getFatigueFactor();
        }

        return rating;
    }
}
