package org.natc.app.service.search;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.Injury;
import org.natc.app.entity.request.InjurySearchRequest;
import org.natc.app.entity.response.InjuryResponse;
import org.natc.app.repository.InjuryRepository;
import org.natc.app.service.NATCServiceIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InjurySearchServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private InjuryRepository injuryRepository;

    @Autowired
    private InjurySearchService injurySearchService;

    @Nested
    class FetchAll {

        @Test
        void shouldReturnAnInjuryFromTheDatabaseMappedToAResponse() {
            final Injury injury = Injury.builder()
                    .gameId(123)
                    .playerId(4321)
                    .build();

            injuryRepository.save(injury);

            final List<InjuryResponse> result = injurySearchService.fetchAll(new InjurySearchRequest());

            assertEquals(1, result.size());

            final InjuryResponse response = result.getFirst();

            assertEquals(injury.getGameId(), response.getGameId());
            assertEquals(injury.getPlayerId(), response.getPlayerId());
        }

        @Test
        void shouldMapAllScheduleFieldsToTheInjuryResponse() {
            final Injury injury = Injury.builder()
                    .gameId(123)
                    .playerId(4321)
                    .teamId(56)
                    .duration(32)
                    .build();

            injuryRepository.save(injury);

            final List<InjuryResponse> result = injurySearchService.fetchAll(new InjurySearchRequest());

            assertEquals(1, result.size());

            final InjuryResponse response = result.getFirst();

            assertEquals(injury.getGameId(), response.getGameId());
            assertEquals(injury.getPlayerId(), response.getPlayerId());
            assertEquals(injury.getTeamId(), response.getTeamId());
            assertEquals(injury.getDuration(), response.getDuration());
        }

        @Test
        void shouldReturnNoEntriesWhenSearchingGivenNoDataInTheDatabase() {
            final List<InjuryResponse> result = injurySearchService.fetchAll(new InjurySearchRequest());

            assertEquals(0, result.size());
        }

        @Nested
        class WithOneSearchParameter {

            @Test
            void shouldReturnAllEntriesForGameWhenSearchingByGameId() {
                final List<Injury> injuryList = Arrays.asList(
                        Injury.builder().gameId(1).playerId(1).teamId(1).build(),
                        Injury.builder().gameId(1).playerId(2).teamId(2).build(),
                        Injury.builder().gameId(1).playerId(3).teamId(3).build()
                );

                injuryRepository.saveAll(injuryList);

                final InjurySearchRequest request = InjurySearchRequest.builder()
                        .gameId(1)
                        .build();

                final List<InjuryResponse> result = injurySearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForGameWhenSearchingByGameId() {
                final List<Injury> injuryList = Arrays.asList(
                        Injury.builder().gameId(1).playerId(1).teamId(1).build(),
                        Injury.builder().gameId(2).playerId(2).teamId(1).build(),
                        Injury.builder().gameId(1).playerId(3).teamId(2).build(),
                        Injury.builder().gameId(3).playerId(4).teamId(2).build(),
                        Injury.builder().gameId(1).playerId(5).teamId(3).build()
                );

                injuryRepository.saveAll(injuryList);

                final InjurySearchRequest request = InjurySearchRequest.builder()
                        .gameId(1)
                        .build();

                final List<InjuryResponse> result = injurySearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t -> t.getGameId().equals(1)).count());
            }

            @Test
            void shouldReturnAllEntriesForPlayerWhenSearchingByPlayerId() {
                final List<Injury> injuryList = Arrays.asList(
                        Injury.builder().gameId(1).playerId(1).teamId(1).build(),
                        Injury.builder().gameId(2).playerId(1).teamId(2).build(),
                        Injury.builder().gameId(3).playerId(1).teamId(3).build()
                );

                injuryRepository.saveAll(injuryList);

                final InjurySearchRequest request = InjurySearchRequest.builder()
                        .playerId(1)
                        .build();

                final List<InjuryResponse> result = injurySearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForPlayerWhenSearchingByPlayerId() {
                final List<Injury> injuryList = Arrays.asList(
                        Injury.builder().gameId(1).playerId(1).teamId(1).build(),
                        Injury.builder().gameId(2).playerId(2).teamId(1).build(),
                        Injury.builder().gameId(3).playerId(1).teamId(2).build(),
                        Injury.builder().gameId(4).playerId(3).teamId(2).build(),
                        Injury.builder().gameId(5).playerId(1).teamId(3).build()
                );

                injuryRepository.saveAll(injuryList);

                final InjurySearchRequest request = InjurySearchRequest.builder()
                        .playerId(1)
                        .build();

                final List<InjuryResponse> result = injurySearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t -> t.getPlayerId().equals(1)).count());
            }

            @Test
            void shouldReturnAllEntriesForTeamWhenSearchingByTeamId() {
                final List<Injury> injuryList = Arrays.asList(
                        Injury.builder().gameId(1).playerId(1).teamId(1).build(),
                        Injury.builder().gameId(2).playerId(2).teamId(1).build(),
                        Injury.builder().gameId(3).playerId(3).teamId(1).build()
                );

                injuryRepository.saveAll(injuryList);

                final InjurySearchRequest request = InjurySearchRequest.builder()
                        .teamId(1)
                        .build();

                final List<InjuryResponse> result = injurySearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForTeamWhenSearchingByTeamId() {
                final List<Injury> injuryList = Arrays.asList(
                        Injury.builder().gameId(1).playerId(1).teamId(1).build(),
                        Injury.builder().gameId(2).playerId(1).teamId(2).build(),
                        Injury.builder().gameId(3).playerId(2).teamId(1).build(),
                        Injury.builder().gameId(4).playerId(2).teamId(3).build(),
                        Injury.builder().gameId(5).playerId(3).teamId(1).build()
                );

                injuryRepository.saveAll(injuryList);

                final InjurySearchRequest request = InjurySearchRequest.builder()
                        .teamId(1)
                        .build();

                final List<InjuryResponse> result = injurySearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t -> t.getTeamId().equals(1)).count());
            }
        }

        @Nested
        class WithTwoSearchParameters {

            @Test
            void shouldReturnAllEntriesForGameAndPlayerWhenSearchingByGameIdAndPlayerId() {
                final List<Injury> injuryList = Collections.singletonList(
                        // GameId & PlayerId are the key fields, so only one record is possible
                        Injury.builder().gameId(1).playerId(1).teamId(1).build()
                );

                injuryRepository.saveAll(injuryList);

                final InjurySearchRequest request = InjurySearchRequest.builder()
                        .gameId(1)
                        .playerId(1)
                        .build();

                final List<InjuryResponse> result = injurySearchService.fetchAll(request);

                assertEquals(1, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForGameAndPlayerWhenSearchingByGameIdAndPlayerId() {
                final List<Injury> injuryList = Arrays.asList(
                        Injury.builder().gameId(1).playerId(1).teamId(1).build(),
                        Injury.builder().gameId(2).playerId(1).teamId(1).build(),
                        Injury.builder().gameId(1).playerId(2).teamId(1).build(),
                        Injury.builder().gameId(2).playerId(2).teamId(1).build()
                );

                injuryRepository.saveAll(injuryList);

                final InjurySearchRequest request = InjurySearchRequest.builder()
                        .gameId(1)
                        .playerId(1)
                        .build();

                final List<InjuryResponse> result = injurySearchService.fetchAll(request);

                assertEquals(1, result.size());
                assertEquals(1, result.stream().filter(t ->
                        t.getGameId().equals(1) && t.getPlayerId().equals(1)
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForGameAndTeamWhenSearchingByGameIdAndTeamId() {
                final List<Injury> injuryList = Arrays.asList(
                        Injury.builder().gameId(1).playerId(1).teamId(1).build(),
                        Injury.builder().gameId(1).playerId(2).teamId(1).build(),
                        Injury.builder().gameId(1).playerId(3).teamId(1).build()
                );

                injuryRepository.saveAll(injuryList);

                final InjurySearchRequest request = InjurySearchRequest.builder()
                        .gameId(1)
                        .teamId(1)
                        .build();

                final List<InjuryResponse> result = injurySearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForGameAndTeamWhenSearchingByGameIdAndTeamId() {
                final List<Injury> injuryList = Arrays.asList(
                        Injury.builder().gameId(1).playerId(1).teamId(1).build(),
                        Injury.builder().gameId(2).playerId(2).teamId(1).build(),
                        Injury.builder().gameId(1).playerId(3).teamId(2).build(),
                        Injury.builder().gameId(1).playerId(4).teamId(1).build(),
                        Injury.builder().gameId(3).playerId(5).teamId(1).build(),
                        Injury.builder().gameId(1).playerId(6).teamId(3).build(),
                        Injury.builder().gameId(1).playerId(7).teamId(1).build()
                );

                injuryRepository.saveAll(injuryList);

                final InjurySearchRequest request = InjurySearchRequest.builder()
                        .gameId(1)
                        .teamId(1)
                        .build();

                final List<InjuryResponse> result = injurySearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getGameId().equals(1) && t.getTeamId().equals(1)
                ).count());
            }

            @Test
            void shouldReturnAllEntriesForPlayerAndTeamWhenSearchingByPlayerIdAndTeamId() {
                final List<Injury> injuryList = Arrays.asList(
                        Injury.builder().gameId(1).playerId(1).teamId(1).build(),
                        Injury.builder().gameId(2).playerId(1).teamId(1).build(),
                        Injury.builder().gameId(3).playerId(1).teamId(1).build()
                );

                injuryRepository.saveAll(injuryList);

                final InjurySearchRequest request = InjurySearchRequest.builder()
                        .playerId(1)
                        .teamId(1)
                        .build();

                final List<InjuryResponse> result = injurySearchService.fetchAll(request);

                assertEquals(3, result.size());
            }

            @Test
            void shouldOnlyReturnEntriesForPlayerAndTeamWhenSearchingByPlayerIdAndTeamId() {
                final List<Injury> injuryList = Arrays.asList(
                        Injury.builder().gameId(1).playerId(1).teamId(1).build(),
                        Injury.builder().gameId(2).playerId(2).teamId(1).build(),
                        Injury.builder().gameId(3).playerId(1).teamId(2).build(),
                        Injury.builder().gameId(4).playerId(1).teamId(1).build(),
                        Injury.builder().gameId(5).playerId(3).teamId(1).build(),
                        Injury.builder().gameId(6).playerId(1).teamId(3).build(),
                        Injury.builder().gameId(7).playerId(1).teamId(1).build()
                );

                injuryRepository.saveAll(injuryList);

                final InjurySearchRequest request = InjurySearchRequest.builder()
                        .playerId(1)
                        .teamId(1)
                        .build();

                final List<InjuryResponse> result = injurySearchService.fetchAll(request);

                assertEquals(3, result.size());
                assertEquals(3, result.stream().filter(t ->
                        t.getPlayerId().equals(1) && t.getTeamId().equals(1)
                ).count());
            }
        }

        @Test
        void shouldReturnMatchingEntryWhenSearchingByAllParameters() {
            final List<Injury> injuryList = Collections.singletonList(
                    // GameId & PlayerId are the key fields, so only one record is possible
                    Injury.builder().gameId(1).playerId(1).teamId(1).build()
            );

            injuryRepository.saveAll(injuryList);

            final InjurySearchRequest request = InjurySearchRequest.builder()
                    .gameId(1)
                    .playerId(1)
                    .teamId(1)
                    .build();

            final List<InjuryResponse> result = injurySearchService.fetchAll(request);

            assertEquals(1, result.size());
        }

        @Test
        void shouldOnlyReturnMatchingEntryWhenSearchingByAllParameters() {
            final List<Injury> injuryList = Arrays.asList(
                    Injury.builder().gameId(1).playerId(1).teamId(1).build(),
                    Injury.builder().gameId(2).playerId(1).teamId(1).build(),
                    Injury.builder().gameId(1).playerId(2).teamId(1).build(),
                    Injury.builder().gameId(2).playerId(2).teamId(1).build()
            );

            injuryRepository.saveAll(injuryList);

            final InjurySearchRequest request = InjurySearchRequest.builder()
                    .gameId(1)
                    .playerId(1)
                    .teamId(1)
                    .build();

            final List<InjuryResponse> result = injurySearchService.fetchAll(request);

            assertEquals(1, result.size());
            assertEquals(1, result.stream().filter(t ->
                    t.getGameId().equals(1) && t.getPlayerId().equals(1) && t.getTeamId().equals(1)
            ).count());
        }
    }
}