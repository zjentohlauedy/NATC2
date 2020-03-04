package org.natc.natc.entity.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.natc.natc.entity.domain.Manager;
import org.natc.natc.entity.domain.ManagerAward;
import org.natc.natc.entity.domain.ManagerStyle;

import static org.natc.natc.util.BooleanHelper.valueOf;

@Getter
@NoArgsConstructor
public class ManagerResponse {
    private Integer managerId;
    private Integer teamId;
    private Integer playerId;
    private String year;
    private String firstName;
    private String lastName;
    private Integer age;
    private Double offense;
    private Double defense;
    private Double intangible;
    private Double penalties;
    private Double vitality;
    private ManagerStyle style;
    private Boolean newHire;
    private Boolean released;
    private Boolean retired;
    private Integer formerTeamId;
    private Integer allstarTeamId;
    private ManagerAward award;
    private Integer seasons;
    private Integer score;
    private Integer totalSeasons;
    private Integer totalScore;

    public ManagerResponse(final Manager manager) {
        managerId = manager.getManagerId();
        teamId = manager.getTeamId();
        playerId = manager.getPlayerId();
        year = manager.getYear();
        firstName = manager.getFirstName();
        lastName = manager.getLastName();
        age = manager.getAge();
        offense = manager.getOffense();
        defense = manager.getDefense();
        intangible = manager.getIntangible();
        penalties = manager.getPenalties();
        vitality = manager.getVitality();
        style = ManagerStyle.getByValue(manager.getStyle());
        newHire = valueOf(manager.getNewHire());
        released = valueOf(manager.getReleased());
        retired = valueOf(manager.getRetired());
        formerTeamId = manager.getFormerTeamId();
        allstarTeamId = manager.getAllstarTeamId();
        award = ManagerAward.getByValue(manager.getAward());
        seasons = manager.getSeasons();
        score = manager.getScore();
        totalSeasons = manager.getTotalSeasons();
        totalScore = manager.getTotalScore();
    }
}
