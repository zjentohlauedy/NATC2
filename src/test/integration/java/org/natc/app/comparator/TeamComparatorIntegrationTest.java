package org.natc.app.comparator;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.*;
import org.natc.app.repository.TeamDefenseSummaryRepository;
import org.natc.app.repository.TeamGameRepository;
import org.natc.app.repository.TeamOffenseSummaryRepository;
import org.natc.app.service.NATCServiceIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamComparatorIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private TeamComparator teamComparator;

    @Autowired
    private TeamGameRepository teamGameRepository;

    @Autowired
    private TeamOffenseSummaryRepository teamOffenseSummaryRepository;

    @Autowired
    private TeamDefenseSummaryRepository teamDefenseSummaryRepository;

    @Nested
    class Compare {

        @Nested
        class HeadToHead {

            @Test
            void shouldDetermineHeadToHeadResultsFromTeamGameRecords() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();

                final List<TeamGame> team1WinningGames = List.of(
                        TeamGame.builder().gameId(1).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(2).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(3).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(4).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(5).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build()
                );

                final List<TeamGame> team2WinningGames = List.of(
                        TeamGame.builder().gameId(6).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(7).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(1).build()
                );

                teamGameRepository.saveAll(team1WinningGames);
                teamGameRepository.saveAll(team2WinningGames);

                assertEquals(3, teamComparator.compare(team1, team2));
            }

            @Test
            void shouldOnlyConsiderTeamGamesForTheSameYearAsTheGivenTeams() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();

                final List<TeamGame> team1WinningGames = List.of(
                        TeamGame.builder().gameId(1).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(2).year("2001").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(3).year("2002").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(4).year("2003").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(5).year("2004").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build()
                );

                final List<TeamGame> team2WinningGames = List.of(
                        TeamGame.builder().gameId(6).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(7).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(8).year("2005").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(9).year("2006").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(10).year("2007").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(1).build()
                );

                teamGameRepository.saveAll(team1WinningGames);
                teamGameRepository.saveAll(team2WinningGames);

                assertEquals(-1, teamComparator.compare(team1, team2));
            }

            @Test
            void shouldOnlyConsiderTeamGamesThatAreBetweenTheGivenTeams() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();

                final List<TeamGame> team1WinningGames = List.of(
                        TeamGame.builder().gameId(1).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(2).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(3).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(4).win(1).build(),
                        TeamGame.builder().gameId(4).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(5).win(1).build(),
                        TeamGame.builder().gameId(5).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(6).win(1).build(),
                        TeamGame.builder().gameId(6).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(3).opponent(team2.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(7).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(4).opponent(team2.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(8).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(5).opponent(team2.getTeamId()).win(1).build()
                );

                final List<TeamGame> team2WinningGames = List.of(
                        TeamGame.builder().gameId(9).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(3).win(1).build(),
                        TeamGame.builder().gameId(10).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(11).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(7).win(1).build(),
                        TeamGame.builder().gameId(12).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(8).win(1).build(),
                        TeamGame.builder().gameId(13).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(9).win(1).build(),
                        TeamGame.builder().gameId(14).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(6).opponent(team1.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(15).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(7).opponent(team1.getTeamId()).win(1).build()
                );

                teamGameRepository.saveAll(team1WinningGames);
                teamGameRepository.saveAll(team2WinningGames);

                assertEquals(1, teamComparator.compare(team1, team2));
            }

            @Test
            void shouldOnlyConsiderWins() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();

                final List<TeamGame> team1WinningGames = List.of(
                        TeamGame.builder().gameId(1).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(2).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(0).build(),
                        TeamGame.builder().gameId(3).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(0).build(),
                        TeamGame.builder().gameId(4).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(0).build(),
                        TeamGame.builder().gameId(5).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(0).build()
                );

                final List<TeamGame> team2WinningGames = List.of(
                        TeamGame.builder().gameId(6).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(7).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(8).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(9).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(10).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(0).build()
                );

                teamGameRepository.saveAll(team1WinningGames);
                teamGameRepository.saveAll(team2WinningGames);

                assertEquals(-3, teamComparator.compare(team1, team2));
            }

            @Test
            void shouldOnlyConsiderRegularSeasonGames() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();

                final List<TeamGame> team1WinningGames = List.of(
                        TeamGame.builder().gameId(1).year("2000").type(GameType.PRESEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(2).year("2000").type(GameType.PRESEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(3).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(4).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(5).year("2000").type(GameType.POSTSEASON.getValue()).teamId(team1.getTeamId()).opponent(team2.getTeamId()).win(1).build()
                );

                final List<TeamGame> team2WinningGames = List.of(
                        TeamGame.builder().gameId(6).year("2000").type(GameType.PRESEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(7).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(8).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(9).year("2000").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(1).build(),
                        TeamGame.builder().gameId(10).year("2000").type(GameType.POSTSEASON.getValue()).teamId(team2.getTeamId()).opponent(team1.getTeamId()).win(1).build()
                );

                teamGameRepository.saveAll(team1WinningGames);
                teamGameRepository.saveAll(team2WinningGames);

                assertEquals(-1, teamComparator.compare(team1, team2));
            }
        }
        
        @Nested
        class ScoringDifferential {
            
            @Test
            void shouldDetermineScoringDifferentialDifferenceFromTeamOffenseAndTeamDefenseSummaryRecords() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();
                
                final List<TeamOffenseSummary> teamOffenseSummaryList = List.of(
                        TeamOffenseSummary.builder().year(team1.getYear()).type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).score(123).build(),
                        TeamOffenseSummary.builder().year(team2.getYear()).type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).score(456).build()
                );
                
                final List<TeamDefenseSummary> teamDefenseSummaryList = List.of(
                        TeamDefenseSummary.builder().year(team1.getYear()).type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).score(456).build(),
                        TeamDefenseSummary.builder().year(team2.getYear()).type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).score(123).build()
                );

                teamOffenseSummaryRepository.saveAll(teamOffenseSummaryList);
                teamDefenseSummaryRepository.saveAll(teamDefenseSummaryList);

                assertEquals(-666, teamComparator.compare(team1, team2));
            }

            @Test
            void shouldOnlyConsiderRecordsForTheSameYearAsTheGivenTeam() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();

                final List<TeamOffenseSummary> teamOffenseSummaryList = List.of(
                        TeamOffenseSummary.builder().year(team1.getYear()).type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).score(123).build(),
                        TeamOffenseSummary.builder().year("1999").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).score(456).build()
                );

                final List<TeamDefenseSummary> teamDefenseSummaryList = List.of(
                        TeamDefenseSummary.builder().year(team1.getYear()).type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).score(456).build(),
                        TeamDefenseSummary.builder().year("1999").type(GameType.REGULAR_SEASON.getValue()).teamId(team2.getTeamId()).score(123).build()
                );

                teamOffenseSummaryRepository.saveAll(teamOffenseSummaryList);
                teamDefenseSummaryRepository.saveAll(teamDefenseSummaryList);

                assertEquals(-333, teamComparator.compare(team1, team2));
            }

            @Test
            void shouldOnlyConsiderRegularSeasonRecords() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();

                final List<TeamOffenseSummary> teamOffenseSummaryList = List.of(
                        TeamOffenseSummary.builder().year(team1.getYear()).type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).score(222).build(),
                        TeamOffenseSummary.builder().year(team2.getYear()).type(GameType.PRESEASON.getValue()).teamId(team2.getTeamId()).score(456).build()
                );

                final List<TeamDefenseSummary> teamDefenseSummaryList = List.of(
                        TeamDefenseSummary.builder().year(team1.getYear()).type(GameType.REGULAR_SEASON.getValue()).teamId(team1.getTeamId()).score(111).build(),
                        TeamDefenseSummary.builder().year(team2.getYear()).type(GameType.POSTSEASON.getValue()).teamId(team2.getTeamId()).score(123).build()
                );

                teamOffenseSummaryRepository.saveAll(teamOffenseSummaryList);
                teamDefenseSummaryRepository.saveAll(teamDefenseSummaryList);

                assertEquals(111, teamComparator.compare(team1, team2));
            }

            @Test
            void shouldOnlyConsiderRecordsForTheGivenTeams() {
                final Team team1 = Team.builder().teamId(1).year("2000").division(1).games(100).playoffRank(0).wins(46).build();
                final Team team2 = Team.builder().teamId(2).year("2000").division(2).games(100).playoffRank(0).wins(46).build();

                final List<TeamOffenseSummary> teamOffenseSummaryList = List.of(
                        TeamOffenseSummary.builder().year(team1.getYear()).type(GameType.REGULAR_SEASON.getValue()).teamId(3).score(123).build(),
                        TeamOffenseSummary.builder().year(team2.getYear()).type(GameType.REGULAR_SEASON.getValue()).teamId(4).score(456).build()
                );

                final List<TeamDefenseSummary> teamDefenseSummaryList = List.of(
                        TeamDefenseSummary.builder().year(team1.getYear()).type(GameType.REGULAR_SEASON.getValue()).teamId(5).score(456).build(),
                        TeamDefenseSummary.builder().year(team2.getYear()).type(GameType.REGULAR_SEASON.getValue()).teamId(6).score(123).build()
                );

                teamOffenseSummaryRepository.saveAll(teamOffenseSummaryList);
                teamDefenseSummaryRepository.saveAll(teamDefenseSummaryList);

                assertEquals(0, teamComparator.compare(team1, team2));
            }
        }
    }
}