package org.natc.app.comparator;

import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.Team;
import org.natc.app.entity.domain.TeamDefenseSummary;
import org.natc.app.entity.domain.TeamOffenseSummary;
import org.natc.app.repository.TeamDefenseSummaryRepository;
import org.natc.app.repository.TeamGameRepository;
import org.natc.app.repository.TeamOffenseSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Objects;

@Component
public class TeamComparator implements Comparator<Team> {

    private final TeamGameRepository teamGameRepository;
    private final TeamOffenseSummaryRepository teamOffenseSummaryRepository;
    private final TeamDefenseSummaryRepository teamDefenseSummaryRepository;

    @Autowired
    public TeamComparator(final TeamGameRepository teamGameRepository, final TeamOffenseSummaryRepository teamOffenseSummaryRepository, final TeamDefenseSummaryRepository teamDefenseSummaryRepository) {
        this.teamGameRepository = teamGameRepository;
        this.teamOffenseSummaryRepository = teamOffenseSummaryRepository;
        this.teamDefenseSummaryRepository = teamDefenseSummaryRepository;
    }

    @Override
    public int compare(final Team t1, final Team t2) {

        // objects
        if (t1.equals(t2)) return 0;

        // games
        if (valueOrZero(t1.getGames()).equals(0) && valueOrZero(t2.getGames()).equals(0)) return 0;

        // playoff rank
        if (!Objects.equals(t1.getPlayoffRank(), t2.getPlayoffRank())) {
            return valueOrZero(t1.getPlayoffRank()) - valueOrZero(t2.getPlayoffRank());
        }

        // wins
        if (!Objects.equals(t1.getWins(), t2.getWins())) {
            return valueOrZero(t1.getWins()) - valueOrZero(t2.getWins());
        }
        
        //same division win percentage
        if (Objects.equals(t1.getDivision(), t2.getDivision())) {
            final double t1WinPct = calcWinPercentageForTeam(t1);
            final double t2WinPct = calcWinPercentageForTeam(t2);

            if (t1WinPct > t2WinPct) return 1;
            if (t2WinPct > t1WinPct) return -1;
        }

        // head to head
        final Integer t1Wins = teamGameRepository.countByYearAndTypeAndTeamIdAndOpponentAndWin(t1.getYear(), GameType.REGULAR_SEASON.getValue(), t1.getTeamId(), t2.getTeamId(), 1);
        final Integer t2Wins = teamGameRepository.countByYearAndTypeAndTeamIdAndOpponentAndWin(t2.getYear(), GameType.REGULAR_SEASON.getValue(), t2.getTeamId(), t1.getTeamId(), 1);

        if (!t1Wins.equals(t2Wins)) return t1Wins - t2Wins;

        // scoring differential
        final Integer t1ScoringDifferential = fetchScoringDifferentialForTeam(t1);
        final Integer t2ScoringDifferential = fetchScoringDifferentialForTeam(t2);
        
        return t1ScoringDifferential - t2ScoringDifferential;
    }

    private Double calcWinPercentageForTeam(final Team team) {
        final Integer divisionWins = valueOrZero(team.getDivisionWins());
        final Integer divisionLosses = valueOrZero(team.getDivisionLosses());

        return safeDivide(divisionWins, divisionWins + divisionLosses);
    }

    private Integer fetchScoringDifferentialForTeam(final Team team) {
        final TeamOffenseSummary teamOffenseSummary = fetchTeamOffenseSummaryForTeam(team);
        final TeamDefenseSummary teamDefenseSummary = fetchTeamDefenseSummaryForTeam(team);

        return teamOffenseSummary.getScore() - teamDefenseSummary.getScore();
    }

    private TeamOffenseSummary fetchTeamOffenseSummaryForTeam(final Team team) {
        return teamOffenseSummaryRepository.findOne(
                Example.of(
                        TeamOffenseSummary.builder()
                                .year(team.getYear())
                                .type(GameType.REGULAR_SEASON.getValue())
                                .teamId(team.getTeamId())
                                .build()
                )
        ).orElse(TeamOffenseSummary.builder().score(0).build());
    }
    
    private TeamDefenseSummary fetchTeamDefenseSummaryForTeam(final Team team) {
        return teamDefenseSummaryRepository.findOne(
                Example.of(
                        TeamDefenseSummary.builder()
                                .year(team.getYear())
                                .type(GameType.REGULAR_SEASON.getValue())
                                .teamId(team.getTeamId())
                                .build()
                )
        ).orElse(TeamDefenseSummary.builder().score(0).build());
    }

    private Integer valueOrZero(final Integer value) {
        return Objects.isNull(value) ? 0 : value;
    }

    private Double safeDivide(final Integer dividend, final Integer divisor) {
        return divisor == 0 ? 0.0 : (double) dividend / divisor;
    }
}
