package org.natc.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Test
    public void fetchAll_ShouldReturnAListOfManagerResponses() {
        final List<ManagerResponse> managerList = managerSearchService.fetchAll(new ManagerSearchRequest());

        assertEquals(0, managerList.size());
    }

    @Test
    public void fetchAll_ShouldCallTheManagerRepositoryWithAnExampleManager() {
        managerSearchService.fetchAll(new ManagerSearchRequest());

        verify(managerRepository).findAll(ArgumentMatchers.<Example<Manager>>any());
    }

    @Test
    public void fetchAll_ShouldCallManagerRepositoryWithExampleManagerBasedOnRequest() {
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
    public void fetchAll_ShouldReturnManagerResponsesMappedFromTheManagersReturnedByRepository() {
        final Manager manager = generateManager(ManagerStyle.PENALTIES, ManagerAward.MANAGER_OF_THE_YEAR);

        when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

        final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

        assertEquals(1, result.size());

        final ManagerResponse response = result.get(0);

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
    public void fetchAll_ShouldMapNewHireValueFromIntegerToBooleanInReponseWhenFalse() {
        final Manager manager = Manager.builder().newHire(0).build();

        when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

        final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

        assertEquals(1, result.size());

        final ManagerResponse response = result.get(0);

        assertFalse(response.getNewHire());
    }
    
    @Test
    public void fetchAll_ShouldMapNewHireValueFromIntegerToBooleanInReponseWhenTrue() {
        final Manager manager = Manager.builder().newHire(1).build();

        when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

        final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

        assertEquals(1, result.size());

        final ManagerResponse response = result.get(0);

        assertTrue(response.getNewHire());
    }
    
    @Test
    public void fetchAll_ShouldMapNewHireValueFromIntegerToBooleanInReponseWhenNull() {
        final Manager manager = new Manager();

        when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

        final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

        assertEquals(1, result.size());

        final ManagerResponse response = result.get(0);

        assertNull(response.getNewHire());
    }

    @Test
    public void fetchAll_ShouldMapReleasedValueFromIntegerToBooleanInReponseWhenFalse() {
        final Manager manager = Manager.builder().released(0).build();

        when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

        final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

        assertEquals(1, result.size());

        final ManagerResponse response = result.get(0);

        assertFalse(response.getReleased());
    }
    
    @Test
    public void fetchAll_ShouldMapReleasedValueFromIntegerToBooleanInReponseWhenTrue() {
        final Manager manager = Manager.builder().released(1).build();

        when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

        final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

        assertEquals(1, result.size());

        final ManagerResponse response = result.get(0);

        assertTrue(response.getReleased());
    }
    
    @Test
    public void fetchAll_ShouldMapReleasedValueFromIntegerToBooleanInReponseWhenNull() {
        final Manager manager = new Manager();

        when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

        final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

        assertEquals(1, result.size());

        final ManagerResponse response = result.get(0);

        assertNull(response.getReleased());
    }

    @Test
    public void fetchAll_ShouldMapRetiredValueFromIntegerToBooleanInReponseWhenFalse() {
        final Manager manager = Manager.builder().retired(0).build();

        when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

        final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

        assertEquals(1, result.size());

        final ManagerResponse response = result.get(0);

        assertFalse(response.getRetired());
    }
    
    @Test
    public void fetchAll_ShouldMapRetiredValueFromIntegerToBooleanInReponseWhenTrue() {
        final Manager manager = Manager.builder().retired(1).build();

        when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

        final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

        assertEquals(1, result.size());

        final ManagerResponse response = result.get(0);

        assertTrue(response.getRetired());
    }
    
    @Test
    public void fetchAll_ShouldMapRetiredValueFromIntegerToBooleanInReponseWhenNull() {
        final Manager manager = new Manager();

        when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(Collections.singletonList(manager));

        final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

        assertEquals(1, result.size());

        final ManagerResponse response = result.get(0);

        assertNull(response.getRetired());
    }

    @Test
    public void fetchAll_ShouldReturnSameNumberOfResponsesAsManagersReturnedByRepository() {
        final List<Manager> managerList = Arrays.asList(new Manager(), new Manager(), new Manager(), new Manager());

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