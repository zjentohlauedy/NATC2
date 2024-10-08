package org.natc.app.service.search;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.ManagerAward;
import org.natc.app.entity.domain.ManagerStyle;
import org.natc.app.entity.request.ManagerSearchRequest;
import org.natc.app.entity.response.ManagerResponse;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManagerSearchServiceTest {

    @Captor
    private ArgumentCaptor<Example<Manager>> captor;

    @Mock
    private JpaRepository managerRepository;

    @InjectMocks
    private ManagerSearchService managerSearchService;

    @Nested
    class FetchAll {

        @Test
        void shouldReturnAListOfManagerResponses() {
            final List<ManagerResponse> managerList = managerSearchService.fetchAll(new ManagerSearchRequest());

            assertEquals(0, managerList.size());
        }

        @Test
        void shouldCallTheManagerRepositoryWithAnExampleManager() {
            managerSearchService.fetchAll(new ManagerSearchRequest());

            verify(managerRepository).findAll(ArgumentMatchers.<Example<Manager>>any());
        }

        @Test
        void shouldCallManagerRepositoryWithExampleManagerBasedOnRequest() {
            final ManagerSearchRequest request = ManagerSearchRequest.builder()
                    .managerId(123)
                    .teamId(321)
                    .playerId(555)
                    .year("1991")
                    .build();

            managerSearchService.fetchAll(request);

            verify(managerRepository).findAll(captor.capture());

            final Manager manager = captor.getValue().getProbe();

            assertEquals(request.getManagerId(), manager.getManagerId());
            assertEquals(request.getTeamId(), manager.getTeamId());
            assertEquals(request.getPlayerId(), manager.getPlayerId());
            assertEquals(request.getYear(), manager.getYear());
        }

        @Test
        void shouldReturnManagerResponsesMappedFromTheManagersReturnedByRepository() {
            final Manager manager = generateManager(ManagerStyle.PENALTIES, ManagerAward.MANAGER_OF_THE_YEAR);

            when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

            final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

            assertEquals(1, result.size());

            final ManagerResponse response = result.getFirst();

            assertEquals(manager.getManagerId(), response.getManagerId());
            assertEquals(manager.getTeamId(), response.getTeamId());
            assertEquals(manager.getPlayerId(), response.getPlayerId());
            assertEquals(manager.getYear(), response.getYear());
            assertEquals(manager.getFirstName(), response.getFirstName());
            assertEquals(manager.getLastName(), response.getLastName());
            assertEquals(manager.getAge(), response.getAge());
            assertEquals(manager.getOffense(), response.getOffense());
            assertEquals(manager.getDefense(), response.getDefense());
            assertEquals(manager.getIntangible(), response.getIntangible());
            assertEquals(manager.getPenalties(), response.getPenalties());
            assertEquals(manager.getVitality(), response.getVitality());
            assertEquals(ManagerStyle.PENALTIES, response.getStyle());
            assertEquals(manager.getFormerTeamId(), response.getFormerTeamId());
            assertEquals(manager.getAllstarTeamId(), response.getAllstarTeamId());
            assertEquals(ManagerAward.MANAGER_OF_THE_YEAR, response.getAward());
            assertEquals(manager.getSeasons(), response.getSeasons());
            assertEquals(manager.getScore(), response.getScore());
            assertEquals(manager.getTotalSeasons(), response.getTotalSeasons());
            assertEquals(manager.getTotalScore(), response.getTotalScore());

            assertNotNull(response.getNewHire());
            assertNotNull(response.getReleased());
            assertNotNull(response.getRetired());
        }

        @Test
        void shouldMapNewHireValueFromIntegerToBooleanInReponseWhenFalse() {
            final Manager manager = Manager.builder().newHire(0).build();

            when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

            final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

            assertEquals(1, result.size());

            final ManagerResponse response = result.getFirst();

            assertFalse(response.getNewHire());
        }

        @Test
        void shouldMapNewHireValueFromIntegerToBooleanInReponseWhenTrue() {
            final Manager manager = Manager.builder().newHire(1).build();

            when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

            final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

            assertEquals(1, result.size());

            final ManagerResponse response = result.getFirst();

            assertTrue(response.getNewHire());
        }

        @Test
        void shouldMapNewHireValueFromIntegerToBooleanInReponseWhenNull() {
            final Manager manager = new Manager();

            when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

            final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

            assertEquals(1, result.size());

            final ManagerResponse response = result.getFirst();

            assertNull(response.getNewHire());
        }

        @Test
        void shouldMapReleasedValueFromIntegerToBooleanInReponseWhenFalse() {
            final Manager manager = Manager.builder().released(0).build();

            when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

            final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

            assertEquals(1, result.size());

            final ManagerResponse response = result.getFirst();

            assertFalse(response.getReleased());
        }

        @Test
        void shouldMapReleasedValueFromIntegerToBooleanInReponseWhenTrue() {
            final Manager manager = Manager.builder().released(1).build();

            when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

            final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

            assertEquals(1, result.size());

            final ManagerResponse response = result.getFirst();

            assertTrue(response.getReleased());
        }

        @Test
        void shouldMapReleasedValueFromIntegerToBooleanInReponseWhenNull() {
            final Manager manager = new Manager();

            when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

            final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

            assertEquals(1, result.size());

            final ManagerResponse response = result.getFirst();

            assertNull(response.getReleased());
        }

        @Test
        void shouldMapRetiredValueFromIntegerToBooleanInReponseWhenFalse() {
            final Manager manager = Manager.builder().retired(0).build();

            when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

            final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

            assertEquals(1, result.size());

            final ManagerResponse response = result.getFirst();

            assertFalse(response.getRetired());
        }

        @Test
        void shouldMapRetiredValueFromIntegerToBooleanInReponseWhenTrue() {
            final Manager manager = Manager.builder().retired(1).build();

            when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

            final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

            assertEquals(1, result.size());

            final ManagerResponse response = result.getFirst();

            assertTrue(response.getRetired());
        }

        @Test
        void shouldMapRetiredValueFromIntegerToBooleanInReponseWhenNull() {
            final Manager manager = new Manager();

            when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

            final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

            assertEquals(1, result.size());

            final ManagerResponse response = result.getFirst();

            assertNull(response.getRetired());
        }

        @Test
        void shouldReturnSameNumberOfResponsesAsManagersReturnedByRepository() {
            final List<Manager> managerList = List.of(new Manager(), new Manager(), new Manager(), new Manager());

            when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(managerList);

            final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

            assertEquals(managerList.size(), result.size());
        }

        private Manager generateManager(final ManagerStyle style, final ManagerAward award) {
            return Manager.builder()
                    .managerId(123)
                    .teamId(321)
                    .playerId(555)
                    .year("1991")
                    .firstName("John")
                    .lastName("Doe")
                    .age(49)
                    .offense(0.111)
                    .defense(0.222)
                    .intangible(0.333)
                    .penalties(0.444)
                    .vitality(0.555)
                    .style(style.getValue())
                    .newHire(1)
                    .released(0)
                    .retired(0)
                    .formerTeamId(111)
                    .allstarTeamId(222)
                    .award(award.getValue())
                    .seasons(12)
                    .score(33)
                    .totalSeasons(18)
                    .totalScore(77)
                    .build();
        }
    }
}