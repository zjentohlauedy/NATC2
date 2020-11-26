package org.natc.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.FullName;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.ManagerStyle;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.ManagerRepository;
import org.natc.app.service.analysis.ManagerAnalyzer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManagerServiceTest {

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private NameService nameService;

    @Mock
    private ManagerAnalyzer managerAnalyzer;

    @InjectMocks
    private ManagerService managerService;

    @BeforeEach
    public void setup() throws NATCException {
        when(nameService.generateName()).thenReturn(FullName.builder().build());
    }

    @Test
    public void generateManagers_ShouldReturnAListOfManagersGenerated() throws NATCException {
        final List<Manager> managerList = managerService.generateManagers(null, 1);

        assertFalse(managerList.isEmpty());
    }

    @Test
    public void generateManagers_ShouldCreateTheGivenNumberOfManagers() throws NATCException {
        final List<Manager> managerList = managerService.generateManagers(null, 10);

        assertEquals(10, managerList.size());
    }

    @Test
    public void generateManagers_ShouldCreateManagersForTheGivenYear() throws NATCException {
        final List<Manager> managerList = managerService.generateManagers("2017", 10);

        final Set<String> years = managerList.stream().map(Manager::getYear).collect(Collectors.toSet());

        assertEquals(1, years.size());
        assertEquals("2017", years.stream().findFirst().orElse(null));
    }

    @Test
    public void generateManagers_ShouldCallManagerRepositoryToGetTheLastManagerId() throws NATCException {
        managerService.generateManagers("2001", 1);

        verify(managerRepository).findMaxManagerId();
    }

    @Test
    public void generateManagers_ShouldSetTheManagerIdToTheNumberAfterTheMaxValueReturnedByRepository() throws NATCException {
        when(managerRepository.findMaxManagerId()).thenReturn(Optional.of(27));

        final List<Manager> managerList = managerService.generateManagers("2017", 1);

        assertEquals(1, managerList.size());
        assertEquals(28, managerList.get(0).getManagerId());
    }

    @Test
    public void generateManagers_ShouldSetTheManagerIdToOneIfNoValueIsFoundInTheRepository() throws NATCException {
        when(managerRepository.findMaxManagerId()).thenReturn(Optional.empty());

        final List<Manager> managerList = managerService.generateManagers("2017", 1);

        assertEquals(1, managerList.size());
        assertEquals(1, managerList.get(0).getManagerId());
    }

    @Test
    public void generateManagers_ShouldOnlyGetTheMaxManagerIdOnceEvenWhenGeneratingMultipleManagers() throws NATCException {
        managerService.generateManagers("2001", 10);

        verify(managerRepository, atMostOnce()).findMaxManagerId();
    }

    @Test
    public void generateManagers_ShouldGenerateEveryManagerWithAManagerId() throws NATCException {
        final List<Manager> managerList = managerService.generateManagers("2017", 10);

        assertEquals(10, managerList.size());
        assertEquals(0, managerList.stream().filter(manager -> manager.getManagerId() == null).count());
    }

    @Test
    public void generateManagers_ShouldGenerateManagersWithUniqueManagerIdsIncrementingFromMaxValueInRepository() throws NATCException {
        when(managerRepository.findMaxManagerId()).thenReturn(Optional.of(27));

        final List<Manager> managerList = managerService.generateManagers("2017", 5);

        final List<Integer> expectedManagerIds = Arrays.asList(28, 29, 30, 31, 32);

        assertEquals(5, managerList.size());
        assertEquals(expectedManagerIds, managerList.stream().map(Manager::getManagerId).collect(Collectors.toList()));
    }

    @Test
    public void generateManagers_ShouldCallNameServiceToGenerateANameForTheManager() throws NATCException {
        managerService.generateManagers("2001", 1);

        verify(nameService).generateName();
    }

    @Test
    public void generateManagers_ShouldGenerateANameForEveryManager() throws NATCException {
        managerService.generateManagers("2001", 25);

        verify(nameService, times(25)).generateName();
    }

    @Test
    public void generateManagers_ShouldSetTheNamesFromNameServiceOnTheGeneratedManagers() throws NATCException {
        reset(nameService);

        when(nameService.generateName())
                .thenReturn(FullName.builder().firstName("James").lastName("Smith").build())
                .thenReturn(FullName.builder().firstName("John").lastName("Johnson").build())
                .thenReturn(FullName.builder().firstName("Robert").lastName("Williams").build());

        final List<Manager> managerList = managerService.generateManagers("2001", 3);

        assertEquals(3, managerList.size());

        assertEquals("James", managerList.get(0).getFirstName());
        assertEquals("Smith", managerList.get(0).getLastName());

        assertEquals("John", managerList.get(1).getFirstName());
        assertEquals("Johnson", managerList.get(1).getLastName());

        assertEquals("Robert", managerList.get(2).getFirstName());
        assertEquals("Williams", managerList.get(2).getLastName());
    }

    @Test
    public void generateManagers_ShouldGenerateManagersBetweenFortyAndFiftyYearsOld() throws NATCException {
        final List<Manager> managerList = managerService.generateManagers("2001", 100);

        assertEquals(100, managerList.size());
        assertEquals(100, managerList.stream()
                .filter(manager -> manager.getAge() >= 40 && manager.getAge() <= 50).count());
    }

    @Test
    public void generateManagers_ShouldGenerateManagersWithARandomAge() throws NATCException {
        final List<Manager> managerList = managerService.generateManagers("2001", 100);

        assertEquals(100, managerList.size());

        final Set<Integer> ages = managerList.stream().map(Manager::getAge).collect(Collectors.toSet());

        assertEquals(10, ages.size());
    }

    @Test
    public void generateManagers_ShouldInitializeNewHireReleasedAndRetiredFlags() throws NATCException {
        final List<Manager> managerList = managerService.generateManagers("2001", 1);

        assertEquals(1, managerList.size());

        final Manager manager = managerList.get(0);

        assertEquals(0, manager.getNewHire());
        assertEquals(0, manager.getReleased());
        assertEquals(0, manager.getRetired());
    }

    @Test
    public void generateManagers_ShouldInitializeSeasonsCountersToZero() throws NATCException {
        final List<Manager> managerList = managerService.generateManagers("2001", 1);

        assertEquals(1, managerList.size());

        final Manager manager = managerList.get(0);

        assertEquals(0, manager.getSeasons());
        assertEquals(0, manager.getTotalSeasons());
    }

    @Test
    public void generateManagers_ShouldInitializeScoreAccumulatorsToZero() throws NATCException {
        final List<Manager> managerList = managerService.generateManagers("2001", 1);

        assertEquals(1, managerList.size());

        final Manager manager = managerList.get(0);

        assertEquals(0, manager.getScore());
        assertEquals(0, manager.getTotalScore());
    }

    @Test
    public void generateManagers_ShouldSetManagerRatingsToRandomValueBetweenZeroAndOne() throws NATCException {
        final List<Manager> managerList = managerService.generateManagers("2001", 1);

        assertEquals(1, managerList.size());

        final Manager manager = managerList.get(0);

        assertTrue(manager.getOffense() >= 0.0 && manager.getOffense() <= 1.0);
        assertTrue(manager.getDefense() >= 0.0 && manager.getDefense() <= 1.0);
        assertTrue(manager.getIntangible() >= 0.0 && manager.getIntangible() <= 1.0);
        assertTrue(manager.getPenalties() >= 0.0 && manager.getPenalties() <= 1.0);
        assertTrue(manager.getVitality() >= 0.0 && manager.getVitality() <= 1.0);
    }

    @Test
    public void generateManagers_ShouldCallManagerAnalyzerToGetTheAppropriateManagerStyle() throws NATCException {
        managerService.generateManagers("2001", 1);

        verify(managerAnalyzer).determineManagerStyle(any(Manager.class));
    }

    @Test
    public void generateManagers_ShouldPassTheGeneratedManagerToTheManagerAnalyzer() throws NATCException {
        final ArgumentCaptor<Manager> captor = ArgumentCaptor.forClass(Manager.class);

        final List<Manager> managerList = managerService.generateManagers("2001", 1);

        verify(managerAnalyzer).determineManagerStyle(captor.capture());

        assertEquals(1, managerList.size());

        final Manager expectedManager = managerList.get(0);
        final Manager actualManager = captor.getValue();

        assertEquals(expectedManager, actualManager);
    }

    @Test
    public void generateManagers_ShouldSetTheManagerStyleWithTheValueReturnedFromTheManagerAnalyzer() throws NATCException {
        when(managerAnalyzer.determineManagerStyle(any())).thenReturn(ManagerStyle.DEFENSIVE);

        final List<Manager> managerList = managerService.generateManagers("2001", 1);

        assertEquals(1, managerList.size());

        assertEquals(ManagerStyle.DEFENSIVE.getValue(), managerList.get(0).getStyle());
    }

    @Test
    public void generateManagers_ShouldCallManagerAnalyzerForEveryManagerGenerated() throws NATCException {
        when(managerAnalyzer.determineManagerStyle(any()))
                .thenReturn(ManagerStyle.OFFENSIVE)
                .thenReturn(ManagerStyle.DEFENSIVE)
                .thenReturn(ManagerStyle.INTANGIBLE)
                .thenReturn(ManagerStyle.PENALTIES)
                .thenReturn(ManagerStyle.BALANCED);

        final List<Manager> managerList = managerService.generateManagers("2001", 5);

        assertEquals(5, managerList.size());

        verify(managerAnalyzer, times(5)).determineManagerStyle(any());

        final Set<Integer> styles = managerList.stream().map(Manager::getStyle).collect(Collectors.toSet());

        assertEquals(5, styles.size());
    }

    @Test
    public void generateManagers_ShouldCallManagerRepositoryToPersistGeneratedManager() throws NATCException {
        managerService.generateManagers("2001", 1);

        verify(managerRepository).save(any(Manager.class));
    }

    @Test
    public void generateManagers_ShouldCallManagerRepositoryWithGeneratedManager() throws NATCException {
        final ArgumentCaptor<Manager> captor = ArgumentCaptor.forClass(Manager.class);

        final List<Manager> managerList = managerService.generateManagers("2001", 1);

        verify(managerRepository).save(captor.capture());

        assertEquals(1, managerList.size());

        final Manager expectedManager = managerList.get(0);
        final Manager actualManager = captor.getValue();

        assertEquals(expectedManager, actualManager);
    }

    @Test
    public void generateManagers_ShouldCallManagerRepositoryForEveryManagerGenerated() throws NATCException {
        managerService.generateManagers("2001", 10);

        verify(managerRepository, times(10)).save(any(Manager.class));
    }

    @Test
    public void updateManager_ShouldCallTheManagerRepositoryToSaveTheGivenManager() {
        final Manager manager = Manager.builder().managerId(1234).build();

        reset(nameService);

        managerService.updateManager(manager);

        managerRepository.save(manager);
    }
    
    @Test
    public void updateManagersForNewSeason_ShouldCallTheRepositoryToCopyManagersForNewYear() {
        reset(nameService);

        managerService.updateManagersForNewSeason(null, null);

        verify(managerRepository).copyManagersForNewYear(any(), any());
    }

    @Test
    public void updateTeamsForNewSeason_ShouldCallPassThePreviousYearAndNewYearToTheRepository() {
        reset(nameService);

        managerService.updateManagersForNewSeason("2001", "2010");

        verify(managerRepository).copyManagersForNewYear("2001", "2010");
    }
}