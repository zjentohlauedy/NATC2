package org.natc.app.service.search;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.Injury;
import org.natc.app.entity.request.InjurySearchRequest;
import org.natc.app.entity.response.InjuryResponse;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InjurySearchServiceTest {

    @Captor
    private ArgumentCaptor<Example<Injury>> captor;

    @Mock
    private JpaRepository repository;

    @InjectMocks
    private InjurySearchService service;

    @Nested
    class FetchAll {

        @Test
        void shouldReturnAListOfInjuryResponses() {
            final List<InjuryResponse> result = service.fetchAll(new InjurySearchRequest());

            assertEquals(0, result.size());
        }

        @Test
        void shouldCallTheInjuryRepositoryWithAnExampleInjury() {
            service.fetchAll(new InjurySearchRequest());

            verify(repository).findAll(ArgumentMatchers.<Example<Injury>>any());
        }

        @Test
        void shouldCallInjuryRepositoryWithExampleInjuryBasedOnRequest() {
            final InjurySearchRequest request = InjurySearchRequest.builder()
                    .gameId(123)
                    .playerId(4321)
                    .teamId(56)
                    .build();

            service.fetchAll(request);

            verify(repository).findAll(captor.capture());

            final Injury injury = captor.getValue().getProbe();

            assertEquals(request.getGameId(), injury.getGameId());
            assertEquals(request.getPlayerId(), injury.getPlayerId());
            assertEquals(request.getTeamId(), injury.getTeamId());
        }

        @Test
        void shouldReturnInjuryResponsesMappedFromTheInjuriesReturnedByRepository() {
            final Injury injury = generateInjury();

            when(repository.findAll(ArgumentMatchers.<Example<Injury>>any())).thenReturn(Collections.singletonList(injury));

            final List<InjuryResponse> result = service.fetchAll(new InjurySearchRequest());

            assertEquals(1, result.size());

            final InjuryResponse response = result.getFirst();

            assertEquals(injury.getGameId(), response.getGameId());
            assertEquals(injury.getPlayerId(), response.getPlayerId());
            assertEquals(injury.getTeamId(), response.getTeamId());
            assertEquals(injury.getDuration(), response.getDuration());
        }

        private Injury generateInjury() {
            return Injury.builder()
                    .gameId(123)
                    .playerId(4321)
                    .teamId(56)
                    .duration(7)
                    .build();
        }
    }
}