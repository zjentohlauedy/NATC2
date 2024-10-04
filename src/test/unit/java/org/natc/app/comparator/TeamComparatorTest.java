package org.natc.app.comparator;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.GameType;
import org.natc.app.entity.domain.Team;
import org.natc.app.entity.domain.TeamDefenseSummary;
import org.natc.app.entity.domain.TeamOffenseSummary;
import org.natc.app.repository.TeamDefenseSummaryRepository;
import org.natc.app.repository.TeamGameRepository;
import org.natc.app.repository.TeamOffenseSummaryRepository;
import org.springframework.data.domain.Example;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamComparatorTest {

    @Captor
    private ArgumentCaptor<Example<TeamOffenseSummary>> teamOffenseSummaryCaptor;

    @Captor
    private ArgumentCaptor<Example<TeamDefenseSummary>> teamDefenseSummaryCaptor;

    @Mock
    private TeamGameRepository teamGameRepository;

    @Mock
    private TeamOffenseSummaryRepository teamOffenseSummaryRepository;

    @Mock
    private TeamDefenseSummaryRepository teamDefenseSummaryRepository;

    @InjectMocks
    private TeamComparator teamComparator;

    @Nested
    class Compare {

        @Test
        void shouldReturnZeroIfWhenBothTeamsAreTheSameTeam() {
            final Team team = Team.builder().teamId(1).year("2000").build();

            assertEquals(0, teamComparator.compare(team, team));
        }

        @Test
        void shouldReturnZeroWhenBothTeamsHaveNotPlayedAnyGames() {
            final Team team1 = Team.builder().teamId(1).year("2000").games(0).build();
            final Team team2 = Team.builder().teamId(2).year("2000").games(0).build();

            assertEquals(0, teamComparator.compare(team1, team2));
        }

        @Test
        void shouldReturnZeroWhenBothTeamsHaveNullGames() {
            final Team team1 = Team.builder().teamId(1).year("2000").build();
            final Team team2 = Team.builder().teamId(2).year("2000").build();

            assertEquals(0, teamComparator.compare(team1, team2));
        }

        @Test
        void shouldReturnZeroWhenOneTeamHasZeroGamesAndTheOtherHasNullGames() {
            final Team team1 = Team.builder().teamId(1).year("2000").games(0).build();
            final Team team2 = Team.builder().teamId(2).year("2000").build();
            final Team team3 = Team.builder().teamId(1).year("2000").build();
            final Team team4 = Team.builder().teamId(2).year("2000").games(0).build();

            assertEquals(0, teamComparator.compare(team1, team2));
            assertEquals(0, teamComparator.compare(team3, team4));
        }

        // vvv both teams have non-null games and at least one is non-zero vvv

        @Test
        void shouldReturnDifferenceBetweenPlayoffRanksWhenTeam1HasAHigherPlayoffRankThanTeam2() {
            final Team team1 = Team.builder().teamId(1).year("2000").games(100).playoffRank(1).build();
            final Team team2 = Team.builder().teamId(2).year("2000").games(100).playoffRank(0).build();

            assertEquals(team1.getPlayoffRank() - team2.getPlayoffRank(), teamComparator.compare(team1, team2));
        }

        @Test
        void shouldReturnNegativeDifferenceBetweenPlayoffRanksWhenTeam2HasAHigherPlayoffRankThanTeam1() {
            final Team team1 = Team.builder().teamId(1).year("2000").games(100).playoffRank(1).build();
            final Team team2 = Team.builder().teamId(2).year("2000").games(100).playoffRank(4).build();

            assertEquals(team1.getPlayoffRank() - team2.getPlayoffRank(), teamComparator.compare(team1, team2));
        }

        @Test
        void shouldReturnTeam1PlayoffRankWhenTeam1HasAPlayoffRankAndTeam2PlayoffRankIsNull() {
            final Team team1 = Team.builder().teamId(1).year("2000").games(100).playoffRank(2).build();
            final Team team2 = Team.builder().teamId(2).year("2000").games(100).build();

            assertEquals(team1.getPlayoffRank(), teamComparator.compare(team1, team2));
        }

        @Test
        void shouldReturnNegativeTeam2PlayoffRankWhenTeam2HasAPlayoffRankAndTeam1PlayoffRankIsNull() {
            final Team team1 = Team.builder().teamId(1).year("2000").games(100).build();
            final Team team2 = Team.builder().teamId(2).year("2000").games(100).playoffRank(5).build();

            assertEquals(-team2.getPlayoffRank(), teamComparator.compare(team1, team2));
        }

        // vvv playoff ranks are the same vvv

        @Test
        void shouldReturnDifferenceBetweenWinsWhenTeam1HasHigherWinsThanTeam2() {
            final Team team1 = Team.builder().teamId(1).year("2000").games(100).playoffRank(0).wins(63).build();
            final Team team2 = Team.builder().teamId(2).year("2000").games(100).playoffRank(0).wins(55).build();

            assertEquals(team1.getWins() - team2.getWins(), teamComparator.compare(team1, team2));
        }

        @Test
        void shouldReturnNegativeDifferenceBetweenWinsWhenTeam2HasHigherWinsThanTeam1() {
            final Team team1 = Team.builder().teamId(1).year("2000").games(100).playoffRank(0).wins(46).build();
            final Team team2 = Team.builder().teamId(2).year("2000").games(100).playoffRank(0).wins(72).build();

            assertEquals(team1.getWins() - team2.getWins(), teamComparator.compare(team1, team2));
        }

        @Test
        void shouldReturnTeam1WinsWhenTeam1HasWinsAndTeam2WinsIsNull() {
            final Team team1 = Team.builder().teamId(1).year("2000").games(100).playoffRank(0).wins(46).build();
            final Team team2 = Team.builder().teamId(2).year("2000").games(100).playoffRank(0).build();

            assertEquals(team1.getWins(), teamComparator.compare(team1, team2));
        }

        @Test
        void shouldReturnNegativeTeam2WinsWhenTeam2HasWinsAndTeam1WinsIsNull() {
            final Team team1 = Team.builder().teamId(1).year("2000").games(100).playoffRank(0).build();
            final Team team2 = Team.builder().teamId(2).year("2000").games(100).playoffRank(0).wins(37).build();

            assertEquals(-team2.getWins(), teamComparator.compare(team1, team2));
        }

        // vvv wins are the same vvv

        @Nested
        class WhenSameDivision {

            @Test
            void shouldReturnOneWhenTeam1HasABetterDivisionRecordThanTeam2() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).divisionWins(10).divisionLosses(6).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(1).games(100).playoffRank(0).wins(46).divisionWins(12).divisionLosses(9).build();

                assertEquals(1, teamComparator.compare(team1, team2));
            }

            @Test
            void shouldReturnNegativeOneWhenTeam2HasABetterDivisionRecordThanTeam1() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).divisionWins(10).divisionLosses(8).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(1).games(100).playoffRank(0).wins(46).divisionWins(12).divisionLosses(9).build();

                assertEquals(-1, teamComparator.compare(team1, team2));
            }

            @Test
            void shouldTreatZeroDivisionWinsAndLossesAsHavingARecordOfZero() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).divisionWins(1).divisionLosses(0).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(1).games(100).playoffRank(0).wins(46).divisionWins(0).divisionLosses(0).build();
                final Team team3 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).divisionWins(0).divisionLosses(0).build();
                final Team team4 = Team.builder().teamId(2).year("2000").division(1).games(100).playoffRank(0).wins(46).divisionWins(1).divisionLosses(0).build();

                assertEquals(1, teamComparator.compare(team1, team2));
                assertEquals(-1, teamComparator.compare(team3, team4));
            }

            @Test
            void shouldTreatAnyNullDivisionWinsOrLossesAsHavingARecordOfZero() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).divisionWins(1).divisionLosses(0).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(1).games(100).playoffRank(0).wins(46).divisionWins(null).divisionLosses(0).build();
                final Team team3 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).divisionWins(0).divisionLosses(null).build();
                final Team team4 = Team.builder().teamId(2).year("2000").division(1).games(100).playoffRank(0).wins(46).divisionWins(1).divisionLosses(0).build();

                assertEquals(1, teamComparator.compare(team1, team2));
                assertEquals(-1, teamComparator.compare(team3, team4));
            }
        }

        // vvv different divisions or division records equal vvv

        @Nested
        class CompareHeadToHead {

            @Test
            void shouldCallTeamGameRepositoryToGetTeam1WinsVsTeam2WhenDifferentDivisions() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();

                teamComparator.compare(team1, team2);

                verify(teamGameRepository).countByYearAndTypeAndTeamIdAndOpponentAndWin(
                        team1.getYear(),
                        GameType.REGULAR_SEASON.getValue(),
                        team1.getTeamId(),
                        team2.getTeamId(),
                        1
                );
            }

            @Test
            void shouldCallTeamGameRepositoryToGetTeam1WinsVsTeam2WhenSameDivisionsAndSameDivisionRecord() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(2).games(100).playoffRank(0).wins(46).divisionWins(14).divisionLosses(8).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).divisionWins(14).divisionLosses(8).build();

                teamComparator.compare(team1, team2);

                verify(teamGameRepository).countByYearAndTypeAndTeamIdAndOpponentAndWin(
                        team1.getYear(),
                        GameType.REGULAR_SEASON.getValue(),
                        team1.getTeamId(),
                        team2.getTeamId(),
                        1
                );
            }

            @Test
            void shouldCallTeamGameRepositoryToGetTeam2WinsVsTeam1WhenDifferentDivisions() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();

                teamComparator.compare(team1, team2);

                verify(teamGameRepository).countByYearAndTypeAndTeamIdAndOpponentAndWin(
                        team2.getYear(),
                        GameType.REGULAR_SEASON.getValue(),
                        team2.getTeamId(),
                        team1.getTeamId(),
                        1
                );
            }

            @Test
            void shouldCallTeamGameRepositoryToGetTeam2WinsVsTeam1WhenSameDivisionsAndSameDivisionRecord() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(2).games(100).playoffRank(0).wins(46).divisionWins(14).divisionLosses(8).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).divisionWins(14).divisionLosses(8).build();

                teamComparator.compare(team1, team2);

                verify(teamGameRepository).countByYearAndTypeAndTeamIdAndOpponentAndWin(
                        team2.getYear(),
                        GameType.REGULAR_SEASON.getValue(),
                        team2.getTeamId(),
                        team1.getTeamId(),
                        1
                );
            }

            @Test
            void shouldReturnDifferenceBetweenHeadToHeadWinsWhenTeam1HasHigherWinsThanTeam2() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();

                when(teamGameRepository.countByYearAndTypeAndTeamIdAndOpponentAndWin(
                        team1.getYear(),
                        GameType.REGULAR_SEASON.getValue(),
                        team1.getTeamId(),
                        team2.getTeamId(),
                        1
                )).thenReturn(4);
                when(teamGameRepository.countByYearAndTypeAndTeamIdAndOpponentAndWin(
                        team2.getYear(),
                        GameType.REGULAR_SEASON.getValue(),
                        team2.getTeamId(),
                        team1.getTeamId(),
                        1
                )).thenReturn(1);

                assertEquals(3, teamComparator.compare(team1, team2));
            }

            @Test
            void shouldReturnNegativeDifferenceBetweenHeadToHeadWinsWhenTeam2HasHigherWinsThanTeam1() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();

                when(teamGameRepository.countByYearAndTypeAndTeamIdAndOpponentAndWin(
                        team1.getYear(),
                        GameType.REGULAR_SEASON.getValue(),
                        team1.getTeamId(),
                        team2.getTeamId(),
                        1
                )).thenReturn(0);
                when(teamGameRepository.countByYearAndTypeAndTeamIdAndOpponentAndWin(
                        team2.getYear(),
                        GameType.REGULAR_SEASON.getValue(),
                        team2.getTeamId(),
                        team1.getTeamId(),
                        1
                )).thenReturn(5);

                assertEquals(-5, teamComparator.compare(team1, team2));
            }
        }

        // vvv head to head wins are equal vvv

        @Nested
        class CompareScoringDifferential {

            @Test
            void shouldCallTeamOffenseSummaryToGetOffenseStatsForTeam1ThenTeam2() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();

                when(teamGameRepository.countByYearAndTypeAndTeamIdAndOpponentAndWin(any(), any(), any(), any(), any())).thenReturn(0);

                teamComparator.compare(team1, team2);

                verify(teamOffenseSummaryRepository, times(2)).findOne(teamOffenseSummaryCaptor.capture());
                
                final TeamOffenseSummary team1OffenseSummary = teamOffenseSummaryCaptor.getAllValues().get(0).getProbe();
                final TeamOffenseSummary team2OffenseSummary = teamOffenseSummaryCaptor.getAllValues().get(1).getProbe();

                assertEquals(team1.getYear(), team1OffenseSummary.getYear());
                assertEquals(GameType.REGULAR_SEASON.getValue(), team1OffenseSummary.getType());
                assertEquals(team1.getTeamId(), team1OffenseSummary.getTeamId());

                assertEquals(team2.getYear(), team2OffenseSummary.getYear());
                assertEquals(GameType.REGULAR_SEASON.getValue(), team2OffenseSummary.getType());
                assertEquals(team2.getTeamId(), team2OffenseSummary.getTeamId());
            }

            @Test
            void shouldCallTeamDefenseSummaryToGetDefenseStatsForTeam1ThenTeam2() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();

                when(teamGameRepository.countByYearAndTypeAndTeamIdAndOpponentAndWin(any(), any(), any(), any(), any())).thenReturn(0);

                teamComparator.compare(team1, team2);

                verify(teamDefenseSummaryRepository, times(2)).findOne(teamDefenseSummaryCaptor.capture());
                
                final TeamDefenseSummary team1DefenseSummary = teamDefenseSummaryCaptor.getAllValues().get(0).getProbe();
                final TeamDefenseSummary team2DefenseSummary = teamDefenseSummaryCaptor.getAllValues().get(1).getProbe();

                assertEquals(team1.getYear(), team1DefenseSummary.getYear());
                assertEquals(GameType.REGULAR_SEASON.getValue(), team1DefenseSummary.getType());
                assertEquals(team1.getTeamId(), team1DefenseSummary.getTeamId());

                assertEquals(team2.getYear(), team2DefenseSummary.getYear());
                assertEquals(GameType.REGULAR_SEASON.getValue(), team2DefenseSummary.getType());
                assertEquals(team2.getTeamId(), team2DefenseSummary.getTeamId());
            }
            
            @Test
            void shouldReturnTheDifferenceBetweenScoringDifferentialsWhenTeam1HasHigherDifferentialThanTeam2() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();
                final TeamOffenseSummary team1OffenseSummary = TeamOffenseSummary.builder().score(555).build();
                final TeamDefenseSummary team1DefenseSummary = TeamDefenseSummary.builder().score(316).build();
                final TeamOffenseSummary team2OffenseSummary = TeamOffenseSummary.builder().score(602).build();
                final TeamDefenseSummary team2DefenseSummary = TeamDefenseSummary.builder().score(537).build();

                when(teamGameRepository.countByYearAndTypeAndTeamIdAndOpponentAndWin(any(), any(), any(), any(), any())).thenReturn(0);
                when(teamOffenseSummaryRepository.findOne(any()))
                        .thenReturn(Optional.of(team1OffenseSummary))
                        .thenReturn(Optional.of(team2OffenseSummary));
                when(teamDefenseSummaryRepository.findOne(any()))
                        .thenReturn(Optional.of(team1DefenseSummary))
                        .thenReturn(Optional.of(team2DefenseSummary));

                assertEquals(174, teamComparator.compare(team1, team2));
            }

            @Test
            void shouldReturnTheNegativeDifferenceBetweenScoringDifferentialsWhenTeam2HasHigherDifferentialThanTeam1() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();
                final TeamOffenseSummary team1OffenseSummary = TeamOffenseSummary.builder().score(420).build();
                final TeamDefenseSummary team1DefenseSummary = TeamDefenseSummary.builder().score(369).build();
                final TeamOffenseSummary team2OffenseSummary = TeamOffenseSummary.builder().score(666).build();
                final TeamDefenseSummary team2DefenseSummary = TeamDefenseSummary.builder().score(404).build();

                when(teamGameRepository.countByYearAndTypeAndTeamIdAndOpponentAndWin(any(), any(), any(), any(), any())).thenReturn(0);
                when(teamOffenseSummaryRepository.findOne(any()))
                        .thenReturn(Optional.of(team1OffenseSummary))
                        .thenReturn(Optional.of(team2OffenseSummary));
                when(teamDefenseSummaryRepository.findOne(any()))
                        .thenReturn(Optional.of(team1DefenseSummary))
                        .thenReturn(Optional.of(team2DefenseSummary));

                assertEquals(-211, teamComparator.compare(team1, team2));
            }

            @Test
            void shouldReturnTheDifferenceBetweenScoringDifferentialsWhenTeam1HasHigherDifferentialThanTeam2WhenDifferentialsAreNegative() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();
                final TeamOffenseSummary team1OffenseSummary = TeamOffenseSummary.builder().score(537).build();
                final TeamDefenseSummary team1DefenseSummary = TeamDefenseSummary.builder().score(602).build();
                final TeamOffenseSummary team2OffenseSummary = TeamOffenseSummary.builder().score(316).build();
                final TeamDefenseSummary team2DefenseSummary = TeamDefenseSummary.builder().score(555).build();

                when(teamGameRepository.countByYearAndTypeAndTeamIdAndOpponentAndWin(any(), any(), any(), any(), any())).thenReturn(0);
                when(teamOffenseSummaryRepository.findOne(any()))
                        .thenReturn(Optional.of(team1OffenseSummary))
                        .thenReturn(Optional.of(team2OffenseSummary));
                when(teamDefenseSummaryRepository.findOne(any()))
                        .thenReturn(Optional.of(team1DefenseSummary))
                        .thenReturn(Optional.of(team2DefenseSummary));

                assertEquals(174, teamComparator.compare(team1, team2));
            }

            @Test
            void shouldReturnTheNegativeDifferenceBetweenScoringDifferentialsWhenTeam2HasHigherDifferentialThanTeam1WhenDifferentialsAreNegative() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();
                final TeamOffenseSummary team1OffenseSummary = TeamOffenseSummary.builder().score(404).build();
                final TeamDefenseSummary team1DefenseSummary = TeamDefenseSummary.builder().score(666).build();
                final TeamOffenseSummary team2OffenseSummary = TeamOffenseSummary.builder().score(369).build();
                final TeamDefenseSummary team2DefenseSummary = TeamDefenseSummary.builder().score(420).build();

                when(teamGameRepository.countByYearAndTypeAndTeamIdAndOpponentAndWin(any(), any(), any(), any(), any())).thenReturn(0);
                when(teamOffenseSummaryRepository.findOne(any()))
                        .thenReturn(Optional.of(team1OffenseSummary))
                        .thenReturn(Optional.of(team2OffenseSummary));
                when(teamDefenseSummaryRepository.findOne(any()))
                        .thenReturn(Optional.of(team1DefenseSummary))
                        .thenReturn(Optional.of(team2DefenseSummary));

                assertEquals(-211, teamComparator.compare(team1, team2));
            }

            @Test
            void shouldReturnZeroWhenBothTeamsHaveTheSameDifferential() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();
                final TeamOffenseSummary team1OffenseSummary = TeamOffenseSummary.builder().score(500).build();
                final TeamDefenseSummary team1DefenseSummary = TeamDefenseSummary.builder().score(400).build();
                final TeamOffenseSummary team2OffenseSummary = TeamOffenseSummary.builder().score(450).build();
                final TeamDefenseSummary team2DefenseSummary = TeamDefenseSummary.builder().score(350).build();

                when(teamGameRepository.countByYearAndTypeAndTeamIdAndOpponentAndWin(any(), any(), any(), any(), any())).thenReturn(0);
                when(teamOffenseSummaryRepository.findOne(any()))
                        .thenReturn(Optional.of(team1OffenseSummary))
                        .thenReturn(Optional.of(team2OffenseSummary));
                when(teamDefenseSummaryRepository.findOne(any()))
                        .thenReturn(Optional.of(team1DefenseSummary))
                        .thenReturn(Optional.of(team2DefenseSummary));

                assertEquals(0, teamComparator.compare(team1, team2));
            }

            @Test
            void shouldTreatMissingDefenseSummaryRecordsAsAScoreOfZero() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();
                final TeamOffenseSummary team1OffenseSummary = TeamOffenseSummary.builder().score(500).build();
                final TeamOffenseSummary team2OffenseSummary = TeamOffenseSummary.builder().score(450).build();

                when(teamGameRepository.countByYearAndTypeAndTeamIdAndOpponentAndWin(any(), any(), any(), any(), any())).thenReturn(0);
                when(teamOffenseSummaryRepository.findOne(any()))
                        .thenReturn(Optional.of(team1OffenseSummary))
                        .thenReturn(Optional.of(team2OffenseSummary));

                assertEquals(50, teamComparator.compare(team1, team2));
            }

            @Test
            void shouldTreatMissingOffenseSummaryRecordsAsAScoreOfZero() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();
                final TeamDefenseSummary team1DefenseSummary = TeamDefenseSummary.builder().score(400).build();
                final TeamDefenseSummary team2DefenseSummary = TeamDefenseSummary.builder().score(350).build();

                when(teamGameRepository.countByYearAndTypeAndTeamIdAndOpponentAndWin(any(), any(), any(), any(), any())).thenReturn(0);
                when(teamDefenseSummaryRepository.findOne(any()))
                        .thenReturn(Optional.of(team1DefenseSummary))
                        .thenReturn(Optional.of(team2DefenseSummary));

                assertEquals(-50, teamComparator.compare(team1, team2));
            }
        }
    }
}