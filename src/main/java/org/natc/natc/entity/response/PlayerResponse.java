package org.natc.natc.entity.response;

import java.time.LocalDate;

public class PlayerResponse {
    private Integer playerId;
    private Integer teamId;
    private String year;
    private String firstName;
    private String lastName;
    private Double age;
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
    private Boolean rookie;
    private Boolean injured;
    private LocalDate returnDate;
    private Boolean freeAgent;
    private Boolean signed;
    private Boolean released;
    private Boolean retired;
    private Integer formerTeamId;
    private Integer allstarTeamId;
    private Integer award; // TODO: enum
    private Integer draftPick;
    private Integer seasonsPlayed;
    private Boolean allstarAlternate;
}
