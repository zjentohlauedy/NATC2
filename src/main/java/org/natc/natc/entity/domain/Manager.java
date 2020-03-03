package org.natc.natc.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = "managers_t")
@IdClass(ManagerId.class)
@Getter
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
}
