package org.natc.natc.entity.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.natc.natc.entity.domain.Player;
import org.natc.natc.entity.domain.PlayerAward;

import java.time.LocalDate;

import static org.natc.natc.util.BooleanHelper.valueOf;

@Getter
@NoArgsConstructor
public class PlayerResponse {
    private Integer playerId;
    private Integer teamId;
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
    private Boolean rookie;
    private Boolean injured;
    private LocalDate returnDate;
    private Boolean freeAgent;
    private Boolean signed;
    private Boolean released;
    private Boolean retired;
    private Integer formerTeamId;
    private Integer allstarTeamId;
    private PlayerAward award;
    private Integer draftPick;
    private Integer seasonsPlayed;
    private Boolean allstarAlternate;

    public PlayerResponse(final Player player) {
        playerId = player.getPlayerId();
        teamId = player.getTeamId();
        year = player.getYear();
        firstName = player.getFirstName();
        lastName = player.getLastName();
        age = player.getAge();
        scoring = player.getScoring();
        passing = player.getPassing();
        blocking = player.getBlocking();
        tackling = player.getTackling();
        stealing = player.getStealing();
        presence = player.getPresence();
        discipline = player.getDiscipline();
        penaltyShot = player.getPenaltyShot();
        penaltyOffense = player.getPenaltyOffense();
        penaltyDefense = player.getPenaltyDefense();
        endurance = player.getEndurance();
        confidence = player.getConfidence();
        vitality = player.getVitality();
        durability = player.getDurability();
        rookie = valueOf(player.getRookie());
        injured = valueOf(player.getInjured());
        returnDate = player.getReturnDate();
        freeAgent = valueOf(player.getFreeAgent());
        signed = valueOf(player.getSigned());
        released = valueOf(player.getReleased());
        retired = valueOf(player.getRetired());
        formerTeamId = player.getFormerTeamId();
        allstarTeamId = player.getAllstarTeamId();
        award = PlayerAward.getByValue(player.getAward());
        draftPick = player.getDraftPick();
        seasonsPlayed = player.getSeasonsPlayed();
        allstarAlternate = valueOf(player.getAllstarAlternate());
    }
}
