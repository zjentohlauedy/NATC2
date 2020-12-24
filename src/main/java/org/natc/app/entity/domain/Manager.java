package org.natc.app.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = "managers_t")
@IdClass(ManagerId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Manager {
    @Id
    private Integer managerId;
    private Integer teamId;
    private Integer playerId;
    @Id
    private String year;
    private String firstName;
    private String lastName;
    private Integer age;
    private Double offense;
    private Double defense;
    private Double intangible;
    private Double penalties;
    private Double vitality;
    private Integer style;
    private Integer newHire;
    private Integer released;
    private Integer retired;
    private Integer formerTeamId;
    private Integer allstarTeamId;
    private Integer award;
    private Integer seasons;
    private Integer score;
    private Integer totalSeasons;
    private Integer totalScore;

    public static Manager generate(
            final Integer managerId,
            final String year,
            final String firstName,
            final String lastName) {
        return Manager.builder()
                .managerId(managerId)
                .year(year)
                .firstName(firstName)
                .lastName(lastName)
                .age((int)Math.floor((Math.random() * 10.0) + 40.0))
                .offense(Math.random())
                .defense(Math.random())
                .intangible(Math.random())
                .penalties(Math.random())
                .vitality(Math.random())
                .newHire(0)
                .released(0)
                .retired(0)
                .seasons(0)
                .score(0)
                .totalSeasons(0)
                .totalScore(0)
                .build();
    }

    public Double getOverallRating() {
        if (offense == null) return 0.0;
        if (defense == null) return 0.0;
        if (intangible == null) return 0.0;
        if (penalties == null) return 0.0;

        return (offense + defense + intangible + penalties) / 4.0;
    }

    public Double getPerformanceRating() {
        if (seasons == 0) return 1.0;

        return Math.max(((double)score / seasons), ((double)totalScore / totalSeasons));
    }
}
