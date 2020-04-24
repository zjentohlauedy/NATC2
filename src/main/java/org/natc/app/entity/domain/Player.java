package org.natc.app.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}