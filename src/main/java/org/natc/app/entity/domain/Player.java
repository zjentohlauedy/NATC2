package org.natc.app.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.time.LocalDate;

@Entity(name = "players_t")
@IdClass(PlayerId.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {
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
    private Double durability;
    private Integer rookie;
    private Integer injured;
    private LocalDate returnDate;
    private Integer freeAgent;
    private Integer signed;
    private Integer released;
    private Integer retired;
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
                .age((int)Math.floor((Math.random() * 12.0) + 18.0))
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
        if (scoring == null) return 0.0;
        if (passing == null) return 0.0;
        if (blocking == null) return 0.0;

        return (scoring + passing + blocking) / 3.0;
    }

    public Double getDefensiveRating() {
        if (tackling == null) return 0.0;
        if (stealing == null) return 0.0;
        if (presence == null) return 0.0;

        return (tackling + stealing + presence) / 3.0;
    }

    public Double getIntangibleRating() {
        if (blocking == null) return 0.0;
        if (presence == null) return 0.0;
        if (discipline == null) return 0.0;
        if (endurance == null) return 0.0;

        return (blocking + presence + discipline + endurance) / 4.0;
    }

    public Double getPenaltyRating() {
        if (penaltyShot == null) return 0.0;
        if (penaltyOffense == null) return 0.0;
        if (penaltyDefense == null) return 0.0;

        return (penaltyShot + penaltyOffense + penaltyDefense) / 3.0;
    }

    public Double getPerformanceRating() {
        if (scoring == null) return 0.0;
        if (passing == null) return 0.0;
        if (blocking == null) return 0.0;
        if (tackling == null) return 0.0;
        if (stealing == null) return 0.0;
        if (presence == null) return 0.0;
        if (discipline == null) return 0.0;
        if (endurance == null) return 0.0;

        return (scoring + passing + blocking +
                tackling + stealing + presence +
                discipline + endurance) / 8.0;
    }
}
