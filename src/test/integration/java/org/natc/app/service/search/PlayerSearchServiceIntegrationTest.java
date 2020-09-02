package org.natc.app.service.search;

import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.Player;
import org.natc.app.entity.domain.PlayerAward;
import org.natc.app.entity.request.PlayerSearchRequest;
import org.natc.app.entity.response.PlayerResponse;
import org.natc.app.repository.PlayerRepository;
import org.natc.app.service.NATCServiceIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlayerSearchServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerSearchService playerSearchService;

    @Test
    public void shouldReturnAPlayerFromTheDatabaseMappedToAResponse() {
        final Player player = Player.builder()
                .playerId(1)
                .year("2020")
                .firstName("John")
                .lastName("Doe")
                .age(23)
                .build();

        playerRepository.save(player);

        final List<PlayerResponse> result = playerSearchService.fetchAll(new PlayerSearchRequest());

        assertEquals(1, result.size());

        final PlayerResponse response = result.get(0);

        assertEquals(player.getPlayerId(), response.getPlayerId());
        assertEquals(player.getYear(), response.getYear());
        assertEquals(player.getFirstName(), response.getFirstName());
        assertEquals(player.getLastName(), response.getLastName());
        assertEquals(player.getAge(), response.getAge());
    }

    @Test
    public void shouldMapAllPlayerFieldsToThePlayerResponse() {
        final Player player = Player.builder()
                .playerId(123)
                .teamId(321)
                .year("1991")
                .firstName("John")
                .lastName("Doe")
                .age(26)
                .scoring(0.101)
                .passing(0.202)
                .blocking(0.303)
                .tackling(0.404)
                .stealing(0.505)
                .presence(0.606)
                .discipline(0.707)
                .penaltyShot(0.808)
                .penaltyOffense(0.909)
                .penaltyDefense(1.010)
                .endurance(1.111)
                .confidence(1.212)
                .vitality(1.313)
                .durability(1.414)
                .rookie(1)
                .injured(0)
                .returnDate(LocalDate.now())
                .freeAgent(1)
                .signed(0)
                .released(1)
                .retired(0)
                .formerTeamId(111)
                .allstarAlternate(222)
                .award(PlayerAward.GOLD.getValue())
                .draftPick(444)
                .seasonsPlayed(555)
                .allstarTeamId(1)
                .build();

        playerRepository.save(player);

        final List<PlayerResponse> result = playerSearchService.fetchAll(new PlayerSearchRequest());

        assertEquals(1, result.size());

        final PlayerResponse response = result.get(0);

        assertNotNull(response.getPlayerId());
        assertNotNull(response.getTeamId());
        assertNotNull(response.getYear());
        assertNotNull(response.getFirstName());
        assertNotNull(response.getLastName());
        assertNotNull(response.getAge());
        assertNotNull(response.getScoring());
        assertNotNull(response.getPassing());
        assertNotNull(response.getBlocking());
        assertNotNull(response.getTackling());
        assertNotNull(response.getStealing());
        assertNotNull(response.getPresence());
        assertNotNull(response.getDiscipline());
        assertNotNull(response.getPenaltyShot());
        assertNotNull(response.getPenaltyOffense());
        assertNotNull(response.getPenaltyDefense());
        assertNotNull(response.getEndurance());
        assertNotNull(response.getConfidence());
        assertNotNull(response.getVitality());
        assertNotNull(response.getDurability());
        assertNotNull(response.getRookie());
        assertNotNull(response.getInjured());
        assertNotNull(response.getReturnDate());
        assertNotNull(response.getFreeAgent());
        assertNotNull(response.getSigned());
        assertNotNull(response.getReleased());
        assertNotNull(response.getRetired());
        assertNotNull(response.getFormerTeamId());
        assertNotNull(response.getAllstarAlternate());
        assertNotNull(response.getAllstarTeamId());
        assertNotNull(response.getAward());
        assertNotNull(response.getDraftPick());
        assertNotNull(response.getSeasonsPlayed());
    }

    @Test
    public void shouldReturnAllEntriesWhenSearchingWithoutValues() {
        final List<Player> playerList = Arrays.asList(
                Player.builder().playerId(1).year("2000").teamId(1).build(),
                Player.builder().playerId(2).year("2001").teamId(2).build(),
                Player.builder().playerId(3).year("2002").teamId(3).build()
        );

        playerRepository.saveAll(playerList);

        final PlayerSearchRequest request = PlayerSearchRequest.builder().build();

        final List<PlayerResponse> result = playerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnNoEntriesWhenSearchingGivenNoDataInTheDatabase() {
        final PlayerSearchRequest request = PlayerSearchRequest.builder().build();

        final List<PlayerResponse> result = playerSearchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForPlayerWhenSearchingByPlayerId() {
        final List<Player> playerList = Arrays.asList(
                Player.builder().playerId(1).year("2000").teamId(1).build(),
                Player.builder().playerId(1).year("2001").teamId(2).build(),
                Player.builder().playerId(1).year("2002").teamId(3).build()
        );

        playerRepository.saveAll(playerList);

        final PlayerSearchRequest request = PlayerSearchRequest.builder()
                .playerId(1)
                .build();

        final List<PlayerResponse> result = playerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForPlayerWhenSearchingByPlayerId() {
        final List<Player> playerList = Arrays.asList(
                Player.builder().playerId(1).year("2000").teamId(1).build(),
                Player.builder().playerId(2).year("2000").teamId(1).build(),
                Player.builder().playerId(1).year("2001").teamId(2).build(),
                Player.builder().playerId(3).year("2001").teamId(2).build(),
                Player.builder().playerId(1).year("2002").teamId(3).build()
        );

        playerRepository.saveAll(playerList);

        final PlayerSearchRequest request = PlayerSearchRequest.builder()
                .playerId(1)
                .build();

        final List<PlayerResponse> result = playerSearchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getPlayerId().equals(1)).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearWhenSearchingByYear() {
        final List<Player> playerList = Arrays.asList(
                Player.builder().playerId(1).year("2000").teamId(1).build(),
                Player.builder().playerId(2).year("2000").teamId(2).build(),
                Player.builder().playerId(3).year("2000").teamId(3).build()
        );

        playerRepository.saveAll(playerList);

        final PlayerSearchRequest request = PlayerSearchRequest.builder()
                .year("2000")
                .build();

        final List<PlayerResponse> result = playerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearWhenSearchingByYear() {
        final List<Player> playerList = Arrays.asList(
                Player.builder().playerId(1).year("2000").teamId(1).build(),
                Player.builder().playerId(1).year("2001").teamId(1).build(),
                Player.builder().playerId(2).year("2000").teamId(2).build(),
                Player.builder().playerId(2).year("2002").teamId(2).build(),
                Player.builder().playerId(3).year("2000").teamId(3).build()
        );

        playerRepository.saveAll(playerList);

        final PlayerSearchRequest request = PlayerSearchRequest.builder()
                .year("2000")
                .build();

        final List<PlayerResponse> result = playerSearchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getYear().equals("2000")).count());
    }

    @Test
    public void shouldReturnAllEntriesForTeamWhenSearchingByTeamId() {
        final List<Player> playerList = Arrays.asList(
                Player.builder().playerId(1).year("2000").teamId(1).build(),
                Player.builder().playerId(2).year("2001").teamId(1).build(),
                Player.builder().playerId(3).year("2002").teamId(1).build()
        );

        playerRepository.saveAll(playerList);

        final PlayerSearchRequest request = PlayerSearchRequest.builder()
                .teamId(1)
                .build();

        final List<PlayerResponse> result = playerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForTeamWhenSearchingByTeamId() {
        final List<Player> playerList = Arrays.asList(
                Player.builder().playerId(1).year("2000").teamId(1).build(),
                Player.builder().playerId(1).year("2001").teamId(2).build(),
                Player.builder().playerId(2).year("2001").teamId(1).build(),
                Player.builder().playerId(3).year("2001").teamId(3).build(),
                Player.builder().playerId(3).year("2002").teamId(1).build()
        );

        playerRepository.saveAll(playerList);

        final PlayerSearchRequest request = PlayerSearchRequest.builder()
                .teamId(1)
                .build();

        final List<PlayerResponse> result = playerSearchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getTeamId().equals(1)).count());
    }

    @Test
    public void shouldReturnAllEntriesForPlayerAndYearWhenSearchingByPlayerIdAndYear() {
        final List<Player> playerList = Collections.singletonList(
                // Player ID & Year are the key fields, so only one record is possible
                Player.builder().playerId(1).year("2000").teamId(2).build()
        );

        playerRepository.saveAll(playerList);

        final PlayerSearchRequest request = PlayerSearchRequest.builder()
                .playerId(1)
                .year("2000")
                .build();

        final List<PlayerResponse> result = playerSearchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldReturnOnlyEntriesForPlayerAndYearWhenSearchingByPlayerIdAndYear() {
        final List<Player> playerList = Arrays.asList(
                Player.builder().playerId(1).year("2000").teamId(2).build(),
                Player.builder().playerId(2).year("2000").teamId(2).build(),
                Player.builder().playerId(1).year("2001").teamId(2).build()
        );

        playerRepository.saveAll(playerList);

        final PlayerSearchRequest request = PlayerSearchRequest.builder()
                .playerId(1)
                .year("2000")
                .build();

        final List<PlayerResponse> result = playerSearchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getPlayerId().equals(1) && t.getYear().equals("2000")
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForPlayerAndTeamWhenSearchingByPlayerIdAndTeamId() {
        final List<Player> playerList = Arrays.asList(
                Player.builder().playerId(1).year("2000").teamId(1).build(),
                Player.builder().playerId(1).year("2001").teamId(1).build(),
                Player.builder().playerId(1).year("2002").teamId(1).build()
        );

        playerRepository.saveAll(playerList);

        final PlayerSearchRequest request = PlayerSearchRequest.builder()
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerResponse> result = playerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForPlayerAndTeamWhenSearchingByPlayerIdAndTeamId() {
        final List<Player> playerList = Arrays.asList(
                Player.builder().playerId(1).year("2000").teamId(1).build(),
                Player.builder().playerId(2).year("2001").teamId(1).build(),
                Player.builder().playerId(1).year("2002").teamId(2).build(),
                Player.builder().playerId(1).year("2003").teamId(1).build(),
                Player.builder().playerId(3).year("2004").teamId(1).build(),
                Player.builder().playerId(1).year("2005").teamId(3).build(),
                Player.builder().playerId(1).year("2006").teamId(1).build()
        );

        playerRepository.saveAll(playerList);

        final PlayerSearchRequest request = PlayerSearchRequest.builder()
                .playerId(1)
                .teamId(1)
                .build();

        final List<PlayerResponse> result = playerSearchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getPlayerId().equals(1) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
        final List<Player> playerList = Arrays.asList(
                Player.builder().playerId(1).year("2000").teamId(1).build(),
                Player.builder().playerId(2).year("2000").teamId(1).build(),
                Player.builder().playerId(3).year("2000").teamId(1).build()
        );

        playerRepository.saveAll(playerList);

        final PlayerSearchRequest request = PlayerSearchRequest.builder()
                .year("2000")
                .teamId(1)
                .build();

        final List<PlayerResponse> result = playerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
        final List<Player> playerList = Arrays.asList(
                Player.builder().playerId(1).year("2000").teamId(1).build(),
                Player.builder().playerId(2).year("2001").teamId(1).build(),
                Player.builder().playerId(3).year("2000").teamId(2).build(),
                Player.builder().playerId(4).year("2000").teamId(1).build(),
                Player.builder().playerId(5).year("2002").teamId(1).build(),
                Player.builder().playerId(6).year("2000").teamId(3).build(),
                Player.builder().playerId(7).year("2000").teamId(1).build()
        );

        playerRepository.saveAll(playerList);

        final PlayerSearchRequest request = PlayerSearchRequest.builder()
                .year("2000")
                .teamId(1)
                .build();

        final List<PlayerResponse> result = playerSearchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnMatchingEntryWhenSearchingByAllParameters() {
        final List<Player> playerList = Collections.singletonList(
                Player.builder().playerId(1).year("2000").teamId(2).build()
        );

        playerRepository.saveAll(playerList);

        final PlayerSearchRequest request = PlayerSearchRequest.builder()
                .playerId(1)
                .year("2000")
                .teamId(2)
                .build();

        final List<PlayerResponse> result = playerSearchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnMatchingEntryWhenSearchingByAllParameters() {
        final List<Player> playerList = Arrays.asList(
                Player.builder().playerId(1).year("2000").teamId(3).build(),
                Player.builder().playerId(2).year("2000").teamId(3).build(),
                Player.builder().playerId(2).year("2001").teamId(3).build(),
                Player.builder().playerId(1).year("2001").teamId(4).build()
        );

        playerRepository.saveAll(playerList);

        final PlayerSearchRequest request = PlayerSearchRequest.builder()
                .playerId(1)
                .year("2000")
                .teamId(3)
                .build();

        final List<PlayerResponse> result = playerSearchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getPlayerId().equals(1) && t.getYear().equals("2000") && t.getTeamId().equals(3)
        ).count());
    }
}