package org.natc.app.service.search;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.Team;
import org.natc.app.entity.request.TeamSearchRequest;
import org.natc.app.entity.response.TeamResponse;
import org.natc.app.repository.TeamRepository;
import org.natc.app.service.NATCServiceIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TeamSearchServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private TeamSearchService teamSearchService;

    @Nested
    class FetchAll {

        @Test
        void shouldReturnATeamFromTheDatabaseMappedToAResponse() {
            final Team team = Team.builder()
                    .teamId(1)
                    .year("1994")
                    .location("San Francisco")
                    .name("Warriors")
                    .abbreviation("S.F.")
                    .build();

            teamRepository.save(team);

            final List<TeamResponse> result = teamSearchService.fetchAll(new TeamSearchRequest());

            assertEquals(1, result.size());

            final TeamResponse response = result.getFirst();

            assertEquals(team.getTeamId(), response.getTeamId());
            assertEquals(team.getYear(), response.getYear());
            assertEquals(team.getLocation(), response.getLocation());
            assertEquals(team.getName(), response.getName());
            assertEquals(team.getAbbreviation(), response.getAbbreviation());
        }

        @Test
        void shouldMapAllTeamFieldsToTheTeamResponse() {
            final Team team = Team.builder()
                    .teamId(123)
                    .year("1999")
                    .location("Here")
                    .name("Them")
                    .abbreviation("ABC.")
                    .timeZone("Americas/Los_Angeles")
                    .gameTime(999)
                    .conference(111)
                    .division(222)
                    .allstarTeam(1)
                    .preseasonGames(12)
                    .preseasonWins(7)
                    .preseasonLosses(5)
                    .games(99)
                    .wins(55)
                    .losses(44)
                    .divisionWins(33)
                    .divisionLosses(22)
                    .outOfConferenceWins(66)
                    .outOfConferenceLosses(11)
                    .overtimeWins(9)
                    .overtimeLosses(3)
                    .roadWins(88)
                    .roadLosses(77)
                    .homeWins(35)
                    .homeLosses(46)
                    .divisionRank(6)
                    .playoffRank(15)
                    .playoffGames(14)
                    .round1Wins(13)
                    .round2Wins(6)
                    .round3Wins(4)
                    .expectation(0.432)
                    .drought(17)
                    .build();

            teamRepository.save(team);

            final List<TeamResponse> result = teamSearchService.fetchAll(new TeamSearchRequest());

            assertEquals(1, result.size());

            final TeamResponse response = result.getFirst();

            assertNotNull(response.getTeamId());
            assertNotNull(response.getYear());
            assertNotNull(response.getLocation());
            assertNotNull(response.getName());
            assertNotNull(response.getAbbreviation());
            assertNotNull(response.getTimeZone());
            assertNotNull(response.getGameTime());
            assertNotNull(response.getConferenceId());
            assertNotNull(response.getDivisionId());
            assertNotNull(response.getAllstarTeam());
            assertNotNull(response.getPreseasonGames());
            assertNotNull(response.getPreseasonWins());
            assertNotNull(response.getPreseasonLosses());
            assertNotNull(response.getGames());
            assertNotNull(response.getWins());
            assertNotNull(response.getLosses());
            assertNotNull(response.getDivisionWins());
            assertNotNull(response.getDivisionLosses());
            assertNotNull(response.getOutOfConferenceWins());
            assertNotNull(response.getOutOfConferenceLosses());
            assertNotNull(response.getOvertimeWins());
            assertNotNull(response.getOvertimeLosses());
            assertNotNull(response.getRoadWins());
            assertNotNull(response.getRoadLosses());
            assertNotNull(response.getHomeWins());
            assertNotNull(response.getHomeLosses());
            assertNotNull(response.getDivisionRank());
            assertNotNull(response.getPlayoffRank());
            assertNotNull(response.getPlayoffGames());
            assertNotNull(response.getRound1Wins());
            assertNotNull(response.getRound2Wins());
            assertNotNull(response.getRound3Wins());
            assertNotNull(response.getExpectation());
            assertNotNull(response.getDrought());
        }

        @Test
        void shouldReturnAllEntriesWhenSearchingWithoutValues() {
            final List<Team> teamList = Arrays.asList(
                    Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                    Team.builder().teamId(2).year("2001").division(2).conference(2).allstarTeam(1).build(),
                    Team.builder().teamId(3).year("2002").division(3).conference(3).allstarTeam(1).build()
            );

            teamRepository.saveAll(teamList);

            final TeamSearchRequest request = TeamSearchRequest.builder().build();

            final List<TeamResponse> result = teamSearchService.fetchAll(request);

            assertEquals(3, result.size());
        }

        @Test
        void shouldReturnNoEntriesWhenSearchingGivenNoDataInTheDatabase() {
            final TeamSearchRequest request = TeamSearchRequest.builder().build();

            final List<TeamResponse> result = teamSearchService.fetchAll(request);

            assertEquals(0, result.size());
        }

        @Nested
        class WithOneSearchParameter {

            @Test
            void shouldReturnAllEntriesForTeamWhenSearchingByTeamId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2002").division(3).conference(3).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTeamWhenSearchingByTeamId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2001").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2002").division(3).conference(3).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t -> t.getTeamId().equals(1)).count());
            }

            @Test
            void shouldReturnAllEntriesForYearWhenSearchingByYear() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2000").division(3).conference(3).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .year("2000")
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForYearWhenSearchingByYear() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(2).year("2002").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2000").division(3).conference(3).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .year("2000")
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t -> t.getYear().equals("2000")).count());
            }

            @Test
            void shouldReturnAllEntriesForConferenceWhenSearchingByConferenceId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2002").division(2).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2003").division(3).conference(1).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .conferenceId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForConferenceWhenSearchingByConferenceId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2002").division(2).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(2).year("2003").division(2).conference(3).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2004").division(1).conference(1).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .conferenceId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t -> t.getConferenceId().equals(1)).count());
            }

            @Test
            void shouldReturnAllEntriesForDivisionWhenSearchingByDivisionId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2002").division(1).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2003").division(1).conference(3).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForDivisionWhenSearchingByDivisionId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2002").division(1).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(2).year("2003").division(3).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2004").division(1).conference(3).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t -> t.getDivisionId().equals(1)).count());
            }

            @Test
            void shouldReturnAllEntriesForAllstarTeamWhenSearchingByAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2002").division(2).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2003").division(3).conference(3).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForAllstarTeamWhenSearchingByAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(2).year("2002").division(2).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2003").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2004").division(1).conference(3).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t -> !t.getAllstarTeam()).count());
            }
        }

        @Nested
        class WithTwoSearchParameters {

            @Test
            void shouldReturnAllEntriesForTeamAndYearWhenSearchingByTeamIdAndYear() {
                final List<Team> teamList = Collections.singletonList(
                        // Team ID & Year are the key fields, so only one record is possible
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(1, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTeamAndYearWhenSearchingByTeamIdAndYear() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(1, result.size());
                assertEquals(1, result.stream().filter(t ->
                        t.getTeamId().equals(1) && t.getYear().equals("2000")
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForTeamAndConferenceWhenSearchingByTeamIdAndConferenceId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2002").division(3).conference(1).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .conferenceId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTeamAndConferenceWhenSearchingByTeamIdAndConferenceId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2002").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2003").division(2).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2004").division(2).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2005").division(2).conference(3).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2006").division(3).conference(1).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .conferenceId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getTeamId().equals(1) && t.getConferenceId().equals(1)
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForTeamAndDivisionWhenSearchingByTeamIdAndDivisionId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2002").division(1).conference(3).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTeamAndDivisionWhenSearchingByTeamIdAndDivisionId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2002").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2003").division(1).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2004").division(1).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2005").division(3).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2006").division(1).conference(3).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getTeamId().equals(1) && t.getDivisionId().equals(1)
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForTeamAndAllstarTeamWhenSearchingByTeamIdAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2002").division(3).conference(3).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTeamAndAllstarTeamWhenSearchingByTeamIdAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2002").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2003").division(2).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2004").division(2).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2005").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2006").division(3).conference(3).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getTeamId().equals(1) && !t.getAllstarTeam()
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForYearAndConferenceWhenSearchingByYearAndConferenceId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2000").division(3).conference(1).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .year("2000")
                        .conferenceId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForYearAndConferenceWhenSearchingByYearAndConferenceId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2000").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(4).year("2000").division(2).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(5).year("2002").division(2).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(6).year("2000").division(2).conference(3).allstarTeam(1).build(),
                        Team.builder().teamId(7).year("2000").division(3).conference(1).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .year("2000")
                        .conferenceId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getYear().equals("2000") && t.getConferenceId().equals(1)
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForYearAndDivisionWhenSearchingByYearAndDivisionId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2000").division(1).conference(3).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .year("2000")
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForYearAndDivisionWhenSearchingByYearAndDivisionId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2000").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(4).year("2000").division(1).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(5).year("2002").division(1).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(6).year("2000").division(3).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(7).year("2000").division(1).conference(3).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .year("2000")
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getYear().equals("2000") && t.getDivisionId().equals(1)
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForYearAndAllstarTeamWhenSearchingByYearAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2000").division(3).conference(3).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .year("2000")
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForYearAndAllstarTeamWhenSearchingByYearAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2000").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(4).year("2000").division(2).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(5).year("2002").division(2).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(6).year("2000").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(7).year("2000").division(3).conference(3).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .year("2000")
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getYear().equals("2000") && !t.getAllstarTeam()
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForConferenceAndDivisionWhenSearchingByConferenceIdAndDivisionId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2001").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2002").division(1).conference(1).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .conferenceId(1)
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForConferenceAndDivisionWhenSearchingByConferenceIdAndDivisionId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2002").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2003").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(2).year("2004").division(3).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(2).year("2005").division(1).conference(3).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2006").division(1).conference(1).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .conferenceId(1)
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getConferenceId().equals(1) && t.getDivisionId().equals(1)
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForConferenceAndAllstarTeamWhenSearchingByConferenceIdAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2001").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2002").division(3).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .conferenceId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForConferenceAndAllstarTeamWhenSearchingByConferenceIdAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2002").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(2).year("2003").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2004").division(2).conference(3).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2005").division(2).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2006").division(3).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .conferenceId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getConferenceId().equals(1) && !t.getAllstarTeam()
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForDivisionAndAllstarTeamWhenSearchingByDivisionIdAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2001").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2002").division(1).conference(3).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .divisionId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForDivisionAndAllstarTeamWhenSearchingByDivisionIdAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2002").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(2).year("2003").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2004").division(3).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2005").division(1).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2006").division(1).conference(3).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .divisionId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getDivisionId().equals(1) && !t.getAllstarTeam()
                ).count());
            }
        }

        @Nested
        class WithThreeSearchParameters {

            @Test
            void shouldReturnAllEntriesForTeamYearAndConferenceWhenSearchingByTeamIdYearAndConferenceId() {
                final List<Team> teamList = Collections.singletonList(
                        // Team ID & Year are the key fields, so only one record is possible
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .conferenceId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(1, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTeamYearAndConferenceWhenSearchingByTeamIdYearAndConferenceId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .conferenceId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(1, result.size());
                assertEquals(1, result.stream().filter(t ->
                        t.getTeamId().equals(1) && t.getYear().equals("2000") && t.getConferenceId().equals(1)
                ).count());
            }

            @Test
            void shouldReturnNoEntriesForTeamYearAndConferenceWhenSearchingByTeamIdYearAndConferenceIdAndConferenceIsDifferent() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .conferenceId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(0, result.size());
            }

            @Test
            void shouldReturnAllEntriesForTeamYearAndDivisionWhenSearchingByTeamIdYearAndDivisionId() {
                final List<Team> teamList = Collections.singletonList(
                        // Team ID & Year are the key fields, so only one record is possible
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(1, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTeamYearAndDivisionWhenSearchingByTeamIdYearAndDivisionId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(1, result.size());
                assertEquals(1, result.stream().filter(t ->
                        t.getTeamId().equals(1) && t.getYear().equals("2000") && t.getDivisionId().equals(1)
                ).count());
            }

            @Test
            void shouldReturnNoEntriesForTeamYearAndDivisionWhenSearchingByTeamIdYearAndDivisionIdAndDivisionIsDifferent() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(0, result.size());
            }

            @Test
            void shouldReturnAllEntriesForTeamYearAndAllstarTeamWhenSearchingByTeamIdYearAndAllstarTeam() {
                final List<Team> teamList = Collections.singletonList(
                        // Team ID & Year are the key fields, so only one record is possible
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(1, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTeamYearAndAllstarTeamWhenSearchingByTeamIdYearAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(1, result.size());
                assertEquals(1, result.stream().filter(t ->
                        t.getTeamId().equals(1) && t.getYear().equals("2000") && !t.getAllstarTeam()
                ).count());
            }

            @Test
            void shouldReturnNoEntriesForTeamYearAndAllstarTeamWhenSearchingByTeamIdYearAndAllstarTeamIdAndAllstarTeamIsDifferent() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(0, result.size());
            }

            @Test
            void shouldReturnAllEntriesForTeamConferenceAndDivisionWhenSearchingByTeamIdConferenceIdAndDivisionId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2002").division(1).conference(1).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .conferenceId(1)
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTeamConferenceAndDivisionWhenSearchingByTeamIdConferenceIdAndDivisionId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2002").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2003").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2004").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2005").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2006").division(3).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2007").division(1).conference(3).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2008").division(1).conference(1).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .conferenceId(1)
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getTeamId().equals(1) && t.getConferenceId().equals(1) && t.getDivisionId().equals(1)
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForTeamConferenceAndAllstarTeamWhenSearchingByTeamIdConferenceIdAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2002").division(3).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .conferenceId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTeamConferenceAndAllstarTeamWhenSearchingByTeamIdConferenceIdAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2002").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2003").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2004").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2005").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2006").division(2).conference(3).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2007").division(2).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2008").division(3).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .conferenceId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getTeamId().equals(1) && t.getConferenceId().equals(1) && !t.getAllstarTeam()
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForTeamDivisionAndAllstarTeamWhenSearchingByTeamIdDivisionIdAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2002").division(1).conference(3).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .divisionId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTeamDivisionAndAllstarTeamWhenSearchingByTeamIdDivisionIdAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2002").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2003").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2004").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2005").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2006").division(3).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2007").division(1).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2008").division(1).conference(3).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .divisionId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getTeamId().equals(1) && t.getDivisionId().equals(1) && !t.getAllstarTeam()
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForYearConferenceAndDivisionWhenSearchingByYearConferenceIdAndDivisionId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(3).year("2000").division(1).conference(1).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .year("2000")
                        .conferenceId(1)
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForYearConferenceAndDivisionWhenSearchingByYearConferenceIdAndDivisionId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2002").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2000").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(4).year("2000").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(5).year("2000").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(6).year("2003").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(7).year("2000").division(3).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(8).year("2000").division(1).conference(3).allstarTeam(1).build(),
                        Team.builder().teamId(9).year("2000").division(1).conference(1).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .year("2000")
                        .conferenceId(1)
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getYear().equals("2000") && t.getConferenceId().equals(1) && t.getDivisionId().equals(1)
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForYearConferenceAndAllstarTeamWhenSearchingByYearConferenceIdAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2000").division(3).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .year("2000")
                        .conferenceId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForYearConferenceAndAllstarTeamWhenSearchingByYearConferenceIdAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2002").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2000").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(4).year("2000").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(5).year("2000").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(6).year("2003").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(7).year("2000").division(2).conference(3).allstarTeam(0).build(),
                        Team.builder().teamId(8).year("2000").division(2).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(9).year("2000").division(3).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .year("2000")
                        .conferenceId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getYear().equals("2000") && t.getConferenceId().equals(1) && !t.getAllstarTeam()
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForYearDivisionAndAllstarTeamWhenSearchingByYearDivisionIdAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2000").division(1).conference(3).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .year("2000")
                        .divisionId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForYearDivisionAndAllstarTeamWhenSearchingByYearDivisionIdAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2002").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2000").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(4).year("2000").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(5).year("2000").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(6).year("2003").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(7).year("2000").division(3).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(8).year("2000").division(1).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(9).year("2000").division(1).conference(3).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .year("2000")
                        .divisionId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getYear().equals("2000") && t.getDivisionId().equals(1) && !t.getAllstarTeam()
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForConferenceDivisionAndAllstarTeamWhenSearchingByConferenceIdDivisionIdAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2002").division(1).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .conferenceId(1)
                        .divisionId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForConferenceDivisionAndAllstarTeamWhenSearchingByConferenceIdDivisionIdAndAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2000").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(4).year("2000").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(5).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(6).year("2001").division(3).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(7).year("2001").division(1).conference(3).allstarTeam(0).build(),
                        Team.builder().teamId(8).year("2001").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(9).year("2002").division(1).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .conferenceId(1)
                        .divisionId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getConferenceId().equals(1) && t.getDivisionId().equals(1) && !t.getAllstarTeam()
                ).count());
            }
        }

        @Nested
        class WithFourSearchParameters {

            @Test
            void shouldReturnAllEntriesForTeamYearConferenceAndDivisionWhenSearchingByTeamIdYearConferenceIdAndDivisionId() {
                final List<Team> teamList = Collections.singletonList(
                        // Team ID & Year are the key fields, so only one record is possible
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .conferenceId(1)
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(1, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTeamYearConferenceAndDivisionWhenSearchingByTeamIdYearConferenceIdAndDivisionId() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .conferenceId(1)
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(1, result.size());
                assertEquals(1, result.stream().filter(t ->
                        t.getTeamId().equals(1) &&
                                t.getYear().equals("2000") &&
                                t.getConferenceId().equals(1) &&
                                t.getDivisionId().equals(1)
                ).count());
            }

            @Test
            void shouldReturnNoEntriesForTeamYearConferenceAndDivisionWhenSearchingByTeamIdYearConferenceIdAndDivisionIdAndConferenceIsDifferent() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .conferenceId(1)
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(0, result.size());
            }

            @Test
            void shouldReturnNoEntriesForTeamYearConferenceAndDivisionWhenSearchingByTeamIdYearConferenceIdAndDivisionIdAndDivisionIsDifferent() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .conferenceId(1)
                        .divisionId(1)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(0, result.size());
            }

            @Test
            void shouldReturnAllEntriesForTeamYearConferenceAndAllstarTeamWhenSearchingByTeamIdYearConferenceIdAllstarTeam() {
                final List<Team> teamList = Collections.singletonList(
                        // Team ID & Year are the key fields, so only one record is possible
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .conferenceId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(1, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTeamYearConferenceAndAllstarTeamWhenSearchingByTeamIdYearConferenceIdAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .conferenceId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(1, result.size());
                assertEquals(1, result.stream().filter(t ->
                        t.getTeamId().equals(1) &&
                                t.getYear().equals("2000") &&
                                t.getConferenceId().equals(1) &&
                                !t.getAllstarTeam()
                ).count());
            }

            @Test
            void shouldReturnNoEntriesForTeamYearConferenceAndAllstarTeamWhenSearchingByTeamIdYearConferenceIdAllstarTeamAndConferenceIsDifferent() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .conferenceId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(0, result.size());
            }

            @Test
            void shouldReturnNoEntriesForTeamYearConferenceAndAllstarTeamWhenSearchingByTeamIdYearConferenceIdAllstarTeamAndAllstarTeamIsDifferent() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .conferenceId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(0, result.size());
            }

            @Test
            void shouldReturnAllEntriesForTeamYearDivisionAndAllstarTeamWhenSearchingByTeamIdYearDivisionIdAllstarTeam() {
                final List<Team> teamList = Collections.singletonList(
                        // Team ID & Year are the key fields, so only one record is possible
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .divisionId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(1, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTeamYearDivisionAndAllstarTeamWhenSearchingByTeamIdYearDivisionIdAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .divisionId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(1, result.size());
                assertEquals(1, result.stream().filter(t ->
                        t.getTeamId().equals(1) &&
                                t.getYear().equals("2000") &&
                                t.getDivisionId().equals(1) &&
                                !t.getAllstarTeam()
                ).count());
            }

            @Test
            void shouldReturnNoEntriesForTeamYearDivisionAndAllstarTeamWhenSearchingByTeamIdYearDivisionIdAllstarTeamAndDivisionIsDifferent() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .divisionId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(0, result.size());
            }

            @Test
            void shouldReturnNoEntriesForTeamDivisionAndAllstarTeamWhenSearchingByTeamIdYearDivisionIdAllstarTeamAndAllstarTeamIsDifferent() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(2).year("2000").division(2).conference(2).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2001").division(2).conference(2).allstarTeam(1).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .year("2000")
                        .divisionId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(0, result.size());
            }

            @Test
            void shouldReturnAllEntriesForTeamConferenceDivisionAndAllstarTeamWhenSearchingByTeamIdConferenceIdDivisionIdAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2002").division(1).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .conferenceId(1)
                        .divisionId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTeamConferenceDivisionAndAllstarTeamWhenSearchingByTeamIdConferenceIdDivisionIdAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2001").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2002").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2003").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2004").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2005").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2006").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2007").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2008").division(3).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2009").division(1).conference(3).allstarTeam(0).build(),
                        Team.builder().teamId(1).year("2010").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(1).year("2011").division(1).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .teamId(1)
                        .conferenceId(1)
                        .divisionId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getTeamId().equals(1) &&
                                t.getConferenceId().equals(1) &&
                                t.getDivisionId().equals(1) &&
                                !t.getAllstarTeam()
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForYearConferenceDivisionAndAllstarTeamWhenSearchingByYearConferenceIdDivisionIdAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2000").division(1).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .year("2000")
                        .conferenceId(1)
                        .divisionId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForYearConferenceDivisionAndAllstarTeamWhenSearchingByYearConferenceIdDivisionIdAllstarTeam() {
                final List<Team> teamList = Arrays.asList(
                        Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(2).year("2002").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(3).year("2000").division(2).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(4).year("2000").division(1).conference(2).allstarTeam(0).build(),
                        Team.builder().teamId(5).year("2000").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(6).year("2000").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(7).year("2003").division(1).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(8).year("2000").division(3).conference(1).allstarTeam(0).build(),
                        Team.builder().teamId(9).year("2000").division(1).conference(3).allstarTeam(0).build(),
                        Team.builder().teamId(10).year("2000").division(1).conference(1).allstarTeam(1).build(),
                        Team.builder().teamId(11).year("2000").division(1).conference(1).allstarTeam(0).build()
                );

                teamRepository.saveAll(teamList);

                final TeamSearchRequest request = TeamSearchRequest.builder()
                        .year("2000")
                        .conferenceId(1)
                        .divisionId(1)
                        .allstarTeam(false)
                        .build();

                final List<TeamResponse> result = teamSearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getYear().equals("2000") &&
                                t.getConferenceId().equals(1) &&
                                t.getDivisionId().equals(1) &&
                                !t.getAllstarTeam()
                ).count());
            }
        }

        @Test
        void shouldReturnMatchingEntryWhenSearchingByAllParameters() {
            final List<Team> teamList = Collections.singletonList(
                    Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build()
            );

            teamRepository.saveAll(teamList);

            final TeamSearchRequest request = TeamSearchRequest.builder()
                    .teamId(1)
                    .year("2000")
                    .conferenceId(1)
                    .divisionId(1)
                    .allstarTeam(false)
                    .build();

            final List<TeamResponse> result = teamSearchService.fetchAll(request);

            assertEquals(1, result.size());
        }

        @Test
        void shouldOnlyReturnMatchingEntryWhenSearchingByAllParameters() {
            final List<Team> teamList = Arrays.asList(
                    Team.builder().teamId(1).year("2000").division(1).conference(1).allstarTeam(0).build(),
                    Team.builder().teamId(2).year("2000").division(1).conference(1).allstarTeam(0).build(),
                    Team.builder().teamId(1).year("2001").division(1).conference(1).allstarTeam(0).build(),
                    Team.builder().teamId(2).year("2001").division(2).conference(1).allstarTeam(0).build(),
                    Team.builder().teamId(2).year("2002").division(1).conference(2).allstarTeam(0).build(),
                    Team.builder().teamId(3).year("2000").division(1).conference(1).allstarTeam(1).build()
            );

            teamRepository.saveAll(teamList);

            final TeamSearchRequest request = TeamSearchRequest.builder()
                    .teamId(1)
                    .year("2000")
                    .conferenceId(1)
                    .divisionId(1)
                    .allstarTeam(false)
                    .build();

            final List<TeamResponse> result = teamSearchService.fetchAll(request);

            assertEquals(1, result.size());
            assertEquals(1, result.stream().filter(t ->
                    t.getTeamId().equals(1) &&
                            t.getYear().equals("2000") &&
                            t.getConferenceId().equals(1) &&
                            t.getDivisionId().equals(1) &&
                            !t.getAllstarTeam()
            ).count());
        }
    }
}