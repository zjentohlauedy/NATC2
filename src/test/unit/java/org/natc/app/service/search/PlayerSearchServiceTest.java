package org.natc.app.service.search;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.Player;
import org.natc.app.entity.domain.PlayerAward;
import org.natc.app.entity.request.PlayerSearchRequest;
import org.natc.app.entity.response.PlayerResponse;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerSearchServiceTest {

    @Captor
    private ArgumentCaptor<Example<Player>> captor;

    @Mock
    private JpaRepository playerRepository;

    @InjectMocks
    private PlayerSearchService playerSearchService;

    @Nested
    class FetchAll {

        @Test
        void shouldReturnAListOfPlayerResponses() {
            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(0, playerList.size());
        }

        @Test
        void shouldCallThePlayerRepositoryWithAnExamplePlayer() {
            playerSearchService.fetchAll(new PlayerSearchRequest());

            verify(playerRepository).findAll(ArgumentMatchers.<Example<Player>>any());
        }

        @Test
        void shouldCallPlayerRepositoryWithExamplePlayerBasedOnRequest() {
            final PlayerSearchRequest request = PlayerSearchRequest.builder()
                    .playerId(123)
                    .teamId(321)
                    .year("1991")
                    .build();

            playerSearchService.fetchAll(request);

            verify(playerRepository).findAll(captor.capture());

            final Player player = captor.getValue().getProbe();

            assertEquals(request.getPlayerId(), player.getPlayerId());
            assertEquals(request.getTeamId(), player.getTeamId());
            assertEquals(request.getYear(), player.getYear());
        }

        @Test
        void shouldReturnPlayerResponsesMappedFromThePlayersReturnedByRepository() {
            final Player player = generatePlayer(PlayerAward.GOLD);

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertEquals(player.getPlayerId(), response.getPlayerId());
            assertEquals(player.getTeamId(), response.getTeamId());
            assertEquals(player.getYear(), response.getYear());
            assertEquals(player.getFirstName(), response.getFirstName());
            assertEquals(player.getLastName(), response.getLastName());
            assertEquals(player.getAge(), response.getAge());
            assertEquals(player.getScoring(), response.getScoring());
            assertEquals(player.getPassing(), response.getPassing());
            assertEquals(player.getBlocking(), response.getBlocking());
            assertEquals(player.getTackling(), response.getTackling());
            assertEquals(player.getStealing(), response.getStealing());
            assertEquals(player.getPresence(), response.getPresence());
            assertEquals(player.getDiscipline(), response.getDiscipline());
            assertEquals(player.getPenaltyShot(), response.getPenaltyShot());
            assertEquals(player.getPenaltyOffense(), response.getPenaltyOffense());
            assertEquals(player.getPenaltyDefense(), response.getPenaltyDefense());
            assertEquals(player.getEndurance(), response.getEndurance());
            assertEquals(player.getConfidence(), response.getConfidence());
            assertEquals(player.getVitality(), response.getVitality());
            assertEquals(player.getDurability(), response.getDurability());
            assertEquals(player.getReturnDate(), response.getReturnDate());
            assertEquals(player.getFormerTeamId(), response.getFormerTeamId());
            assertEquals(player.getAllstarTeamId(), response.getAllstarTeamId());
            assertEquals(PlayerAward.GOLD, response.getAward());
            assertEquals(player.getDraftPick(), response.getDraftPick());
            assertEquals(player.getSeasonsPlayed(), response.getSeasonsPlayed());

            assertNotNull(response.getRookie());
            assertNotNull(response.getInjured());
            assertNotNull(response.getFreeAgent());
            assertNotNull(response.getSigned());
            assertNotNull(response.getReleased());
            assertNotNull(response.getRetired());
            assertNotNull(response.getAllstarAlternate());
        }

        @Test
        void shouldMapRookieValueFromIntegerToBooleanInReponseWhenFalse() {
            final Player player = Player.builder().rookie(0).build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertFalse(response.getRookie());
        }

        @Test
        void shouldMapRookieValueFromIntegerToBooleanInReponseWhenTrue() {
            final Player player = Player.builder().rookie(1).build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertTrue(response.getRookie());

        }

        @Test
        void shouldMapRookieValueFromIntegerToBooleanInReponseWhenNull() {
            final Player player = Player.builder().build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertNull(response.getRookie());
        }

        @Test
        void shouldMapInjuredValueFromIntegerToBooleanInReponseWhenFalse() {
            final Player player = Player.builder().injured(0).build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertFalse(response.getInjured());
        }

        @Test
        void shouldMapInjuredValueFromIntegerToBooleanInReponseWhenTrue() {
            final Player player = Player.builder().injured(1).build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertTrue(response.getInjured());
        }

        @Test
        void shouldMapInjuredValueFromIntegerToBooleanInReponseWhenNull() {
            final Player player = Player.builder().build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertNull(response.getInjured());
        }

        @Test
        void shouldMapFreeAgentValueFromIntegerToBooleanInReponseWhenFalse() {
            final Player player = Player.builder().freeAgent(0).build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertFalse(response.getFreeAgent());
        }

        @Test
        void shouldMapFreeAgentValueFromIntegerToBooleanInReponseWhenTrue() {
            final Player player = Player.builder().freeAgent(1).build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertTrue(response.getFreeAgent());
        }

        @Test
        void shouldMapFreeAgentValueFromIntegerToBooleanInReponseWhenNull() {
            final Player player = Player.builder().build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertNull(response.getFreeAgent());
        }

        @Test
        void shouldMapSignedValueFromIntegerToBooleanInReponseWhenFalse() {
            final Player player = Player.builder().signed(0).build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertFalse(response.getSigned());
        }

        @Test
        void shouldMapSignedValueFromIntegerToBooleanInReponseWhenTrue() {
            final Player player = Player.builder().signed(1).build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertTrue(response.getSigned());
        }

        @Test
        void shouldMapSignedValueFromIntegerToBooleanInReponseWhenNull() {
            final Player player = Player.builder().build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertNull(response.getSigned());
        }

        @Test
        void shouldMapReleasedValueFromIntegerToBooleanInReponseWhenFalse() {
            final Player player = Player.builder().released(0).build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertFalse(response.getReleased());
        }

        @Test
        void shouldMapReleasedValueFromIntegerToBooleanInReponseWhenTrue() {
            final Player player = Player.builder().released(1).build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertTrue(response.getReleased());
        }

        @Test
        void shouldMapReleasedValueFromIntegerToBooleanInReponseWhenNull() {
            final Player player = Player.builder().build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertNull(response.getReleased());
        }

        @Test
        void shouldMapRetiredValueFromIntegerToBooleanInReponseWhenFalse() {
            final Player player = Player.builder().retired(0).build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertFalse(response.getRetired());
        }

        @Test
        void shouldMapRetiredValueFromIntegerToBooleanInReponseWhenTrue() {
            final Player player = Player.builder().retired(1).build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertTrue(response.getRetired());
        }

        @Test
        void shouldMapRetiredValueFromIntegerToBooleanInReponseWhenNull() {
            final Player player = Player.builder().build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertNull(response.getRetired());
        }

        @Test
        void shouldMapAllstarAlternateValueFromIntegerToBooleanInReponseWhenFalse() {
            final Player player = Player.builder().allstarAlternate(0).build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertFalse(response.getAllstarAlternate());
        }

        @Test
        void shouldMapAllstarAlternateValueFromIntegerToBooleanInReponseWhenTrue() {
            final Player player = Player.builder().allstarAlternate(1).build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertTrue(response.getAllstarAlternate());
        }

        @Test
        void shouldMapAllstarAlternateValueFromIntegerToBooleanInReponseWhenNull() {
            final Player player = Player.builder().build();

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(Collections.singletonList(player));

            final List<PlayerResponse> playerList = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(1, playerList.size());

            final PlayerResponse response = playerList.getFirst();

            assertNull(response.getAllstarAlternate());
        }

        @Test
        void shouldReturnSameNumberOfResponsesAsPlayersReturnedByRepository() {
            final List<Player> playerList = Arrays.asList(new Player(), new Player(), new Player(), new Player());

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any())).thenReturn(playerList);

            final List<PlayerResponse> result = playerSearchService.fetchAll(new PlayerSearchRequest());

            assertEquals(playerList.size(), result.size());
        }

        private Player generatePlayer(final PlayerAward award) {
            return Player.builder()
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
                    .award(award.getValue())
                    .draftPick(444)
                    .seasonsPlayed(555)
                    .allstarTeamId(1)
                    .build();
        }
    }
}