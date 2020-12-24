package org.natc.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.FullName;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.ManagerStyle;
import org.natc.app.entity.domain.Player;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.ManagerRepository;
import org.natc.app.service.analysis.ManagerAnalyzer;
import org.springframework.data.domain.Example;

import java.util.Arrays;
import java.util.Collections;
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

    @Captor
    private ArgumentCaptor<Example<Manager>> captor;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private NameService nameService;

    @Mock
    private ManagerAnalyzer managerAnalyzer;

    @InjectMocks
    private ManagerService managerService;

    @Nested
    class GenerateManagers {

        @BeforeEach
        public void setup() throws NATCException {
            when(nameService.generateName()).thenReturn(FullName.builder().build());
        }

        @Test
        public void shouldReturnAListOfManagersGenerated() throws NATCException {
            final List<Manager> managerList = managerService.generateManagers(null, 1);

            assertFalse(managerList.isEmpty());
        }

        @Test
        public void shouldCreateTheGivenNumberOfManagers() throws NATCException {
            final List<Manager> managerList = managerService.generateManagers(null, 10);

            assertEquals(10, managerList.size());
        }

        @Test
        public void shouldCreateManagersForTheGivenYear() throws NATCException {
            final List<Manager> managerList = managerService.generateManagers("2017", 10);

            final Set<String> years = managerList.stream().map(Manager::getYear).collect(Collectors.toSet());

            assertEquals(1, years.size());
            assertEquals("2017", years.stream().findFirst().orElse(null));
        }

        @Test
        public void shouldCallManagerRepositoryToGetTheLastManagerId() throws NATCException {
            managerService.generateManagers("2001", 1);

            verify(managerRepository).findMaxManagerId();
        }

        @Test
        public void shouldSetTheManagerIdToTheNumberAfterTheMaxValueReturnedByRepository() throws NATCException {
            when(managerRepository.findMaxManagerId()).thenReturn(Optional.of(27));

            final List<Manager> managerList = managerService.generateManagers("2017", 1);

            assertEquals(1, managerList.size());
            assertEquals(28, managerList.get(0).getManagerId());
        }

        @Test
        public void shouldSetTheManagerIdToOneIfNoValueIsFoundInTheRepository() throws NATCException {
            when(managerRepository.findMaxManagerId()).thenReturn(Optional.empty());

            final List<Manager> managerList = managerService.generateManagers("2017", 1);

            assertEquals(1, managerList.size());
            assertEquals(1, managerList.get(0).getManagerId());
        }

        @Test
        public void shouldOnlyGetTheMaxManagerIdOnceEvenWhenGeneratingMultipleManagers() throws NATCException {
            managerService.generateManagers("2001", 10);

            verify(managerRepository, atMostOnce()).findMaxManagerId();
        }

        @Test
        public void shouldGenerateEveryManagerWithAManagerId() throws NATCException {
            final List<Manager> managerList = managerService.generateManagers("2017", 10);

            assertEquals(10, managerList.size());
            assertEquals(0, managerList.stream().filter(manager -> manager.getManagerId() == null).count());
        }

        @Test
        public void shouldGenerateManagersWithUniqueManagerIdsIncrementingFromMaxValueInRepository() throws NATCException {
            when(managerRepository.findMaxManagerId()).thenReturn(Optional.of(27));

            final List<Manager> managerList = managerService.generateManagers("2017", 5);

            final List<Integer> expectedManagerIds = Arrays.asList(28, 29, 30, 31, 32);

            assertEquals(5, managerList.size());
            assertEquals(expectedManagerIds, managerList.stream().map(Manager::getManagerId).collect(Collectors.toList()));
        }

        @Test
        public void shouldCallNameServiceToGenerateANameForTheManager() throws NATCException {
            managerService.generateManagers("2001", 1);

            verify(nameService).generateName();
        }

        @Test
        public void shouldGenerateANameForEveryManager() throws NATCException {
            managerService.generateManagers("2001", 25);

            verify(nameService, times(25)).generateName();
        }

        @Test
        public void shouldSetTheNamesFromNameServiceOnTheGeneratedManagers() throws NATCException {
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
        public void shouldGenerateManagersBetweenFortyAndFiftyYearsOld() throws NATCException {
            final List<Manager> managerList = managerService.generateManagers("2001", 100);

            assertEquals(100, managerList.size());
            assertEquals(100, managerList.stream()
                    .filter(manager -> manager.getAge() >= 40 && manager.getAge() <= 50).count());
        }

        @Test
        public void shouldGenerateManagersWithARandomAge() throws NATCException {
            final List<Manager> managerList = managerService.generateManagers("2001", 100);

            assertEquals(100, managerList.size());

            final Set<Integer> ages = managerList.stream().map(Manager::getAge).collect(Collectors.toSet());

            assertEquals(10, ages.size());
        }

        @Test
        public void shouldInitializeNewHireReleasedAndRetiredFlags() throws NATCException {
            final List<Manager> managerList = managerService.generateManagers("2001", 1);

            assertEquals(1, managerList.size());

            final Manager manager = managerList.get(0);

            assertEquals(0, manager.getNewHire());
            assertEquals(0, manager.getReleased());
            assertEquals(0, manager.getRetired());
        }

        @Test
        public void shouldInitializeSeasonsCountersToZero() throws NATCException {
            final List<Manager> managerList = managerService.generateManagers("2001", 1);

            assertEquals(1, managerList.size());

            final Manager manager = managerList.get(0);

            assertEquals(0, manager.getSeasons());
            assertEquals(0, manager.getTotalSeasons());
        }

        @Test
        public void shouldInitializeScoreAccumulatorsToZero() throws NATCException {
            final List<Manager> managerList = managerService.generateManagers("2001", 1);

            assertEquals(1, managerList.size());

            final Manager manager = managerList.get(0);

            assertEquals(0, manager.getScore());
            assertEquals(0, manager.getTotalScore());
        }

        @Test
        public void shouldSetManagerRatingsToRandomValueBetweenZeroAndOne() throws NATCException {
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
        public void shouldCallManagerAnalyzerToGetTheAppropriateManagerStyle() throws NATCException {
            managerService.generateManagers("2001", 1);

            verify(managerAnalyzer).determineManagerStyle(any(Manager.class));
        }

        @Test
        public void shouldPassTheGeneratedManagerToTheManagerAnalyzer() throws NATCException {
            final ArgumentCaptor<Manager> captor = ArgumentCaptor.forClass(Manager.class);

            final List<Manager> managerList = managerService.generateManagers("2001", 1);

            verify(managerAnalyzer).determineManagerStyle(captor.capture());

            assertEquals(1, managerList.size());

            final Manager expectedManager = managerList.get(0);
            final Manager actualManager = captor.getValue();

            assertEquals(expectedManager, actualManager);
        }

        @Test
        public void shouldSetTheManagerStyleWithTheValueReturnedFromTheManagerAnalyzer() throws NATCException {
            when(managerAnalyzer.determineManagerStyle(any())).thenReturn(ManagerStyle.DEFENSIVE);

            final List<Manager> managerList = managerService.generateManagers("2001", 1);

            assertEquals(1, managerList.size());

            assertEquals(ManagerStyle.DEFENSIVE.getValue(), managerList.get(0).getStyle());
        }

        @Test
        public void shouldCallManagerAnalyzerForEveryManagerGenerated() throws NATCException {
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
        public void shouldCallManagerRepositoryToPersistGeneratedManager() throws NATCException {
            managerService.generateManagers("2001", 1);

            verify(managerRepository).save(any(Manager.class));
        }

        @Test
        public void shouldCallManagerRepositoryWithGeneratedManager() throws NATCException {
            final ArgumentCaptor<Manager> captor = ArgumentCaptor.forClass(Manager.class);

            final List<Manager> managerList = managerService.generateManagers("2001", 1);

            verify(managerRepository).save(captor.capture());

            assertEquals(1, managerList.size());

            final Manager expectedManager = managerList.get(0);
            final Manager actualManager = captor.getValue();

            assertEquals(expectedManager, actualManager);
        }

        @Test
        public void shouldCallManagerRepositoryForEveryManagerGenerated() throws NATCException {
            managerService.generateManagers("2001", 10);

            verify(managerRepository, times(10)).save(any(Manager.class));
        }
    }

    @Nested
    class GenerateManagerFromPlayer {

        @Test
        public void shouldGenerateAManagerForTheGivenYear() {
            final Player player = Player.builder().playerId(1).year("2006").age(35).build();

            final Manager manager = managerService.generateManagerFromPlayer("2016", player);

            assertEquals("2016", manager.getYear());
        }

        @Test
        public void shouldReturnAManagerWithTheSameNameAsThePlayer() {
            final Player player = Player.builder().playerId(1).year("2006").age(35).firstName("Ralph").lastName("Johnson").build();

            final Manager manager = managerService.generateManagerFromPlayer("2016", player);

            assertEquals(player.getFirstName(), manager.getFirstName());
            assertEquals(player.getLastName(), manager.getLastName());
        }
        
        @Test
        public void shouldReturnAManagerWithTheSameAgeAsThePlayerWouldBeInCurrentYear() {
            final Player player = Player.builder().playerId(1).year("2006").age(32).build();

            final Manager manager = managerService.generateManagerFromPlayer("2016", player);

            assertEquals(42, manager.getAge());
        }

        @Test
        public void shouldReturnAManagerWithRatingsBasedOnThePlayerRatings() {
            final Player player = Player.builder()
                    .playerId(1)
                    .year("2006")
                    .age(32)
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
                    .build();

            final Manager manager = managerService.generateManagerFromPlayer("2016", player);

            assertEquals(player.getOffensiveRating(), manager.getOffense());
            assertEquals(player.getDefensiveRating(), manager.getDefense());
            assertEquals(player.getIntangibleRating(), manager.getIntangible());
            assertEquals(player.getPenaltyRating(), manager.getPenalties());
            assertEquals(player.getVitality(), manager.getVitality());
        }

        @Test
        public void shouldReturnAManagerWithPlayerIdOfGivenPlayer() {
            final Player player = Player.builder().playerId(15243).year("2006").age(32).build();

            final Manager manager = managerService.generateManagerFromPlayer("2016", player);

            assertEquals(player.getPlayerId(), manager.getPlayerId());
        }

        @Test
        public void shouldCallManagerRepositoryToGetTheLastManagerId() throws NATCException {
            final Player player = Player.builder().playerId(1).year("2006").age(32).build();

            managerService.generateManagerFromPlayer("2016", player);

            verify(managerRepository).findMaxManagerId();
        }

        @Test
        public void shouldSetTheManagerIdToTheNumberAfterTheMaxValueReturnedByRepository() throws NATCException {
            when(managerRepository.findMaxManagerId()).thenReturn(Optional.of(27));

            final Player player = Player.builder().playerId(1).year("2006").age(32).build();

            final Manager manager = managerService.generateManagerFromPlayer("2016", player);

            assertEquals(28, manager.getManagerId());
        }

        @Test
        public void shouldSetTheManagerIdToOneIfNoValueIsFoundInTheRepository() throws NATCException {
            when(managerRepository.findMaxManagerId()).thenReturn(Optional.empty());

            final Player player = Player.builder().playerId(1).year("2006").age(32).build();

            final Manager manager = managerService.generateManagerFromPlayer("2016", player);

            assertEquals(1, manager.getManagerId());
        }

        @Test
        public void shouldCallManagerAnalyzerToGetTheAppropriateManagerStyle() throws NATCException {
            final Player player = Player.builder().playerId(1).year("2006").age(32).build();

            managerService.generateManagerFromPlayer("2016", player);

            verify(managerAnalyzer).determineManagerStyle(any(Manager.class));
        }

        @Test
        public void shouldPassTheGeneratedManagerToTheManagerAnalyzer() throws NATCException {
            final ArgumentCaptor<Manager> captor = ArgumentCaptor.forClass(Manager.class);

            final Player player = Player.builder().playerId(1).year("2006").age(32).build();

            final Manager expectedManager = managerService.generateManagerFromPlayer("2016", player);

            verify(managerAnalyzer).determineManagerStyle(captor.capture());

            final Manager actualManager = captor.getValue();

            assertEquals(expectedManager, actualManager);
        }

        @Test
        public void shouldSetTheManagerStyleWithTheValueReturnedFromTheManagerAnalyzer() throws NATCException {
            when(managerAnalyzer.determineManagerStyle(any())).thenReturn(ManagerStyle.DEFENSIVE);

            final Player player = Player.builder().playerId(1).year("2006").age(32).build();

            final Manager manager = managerService.generateManagerFromPlayer("2016", player);

            assertEquals(ManagerStyle.DEFENSIVE.getValue(), manager.getStyle());
        }

        @Test
        public void shouldCallManagerRepositoryToPersistGeneratedManager() throws NATCException {
            final Player player = Player.builder().playerId(1).year("2006").age(32).build();

            managerService.generateManagerFromPlayer("2016", player);

            verify(managerRepository).save(any(Manager.class));
        }

        @Test
        public void shouldCallManagerRepositoryWithGeneratedManager() throws NATCException {
            final ArgumentCaptor<Manager> captor = ArgumentCaptor.forClass(Manager.class);

            final Player player = Player.builder().playerId(1).year("2006").age(32).build();

            final Manager expectedManager = managerService.generateManagerFromPlayer("2016", player);

            verify(managerRepository).save(captor.capture());

            final Manager actualManager = captor.getValue();

            assertEquals(expectedManager, actualManager);
        }
    }

    @Nested
    class UpdateManager {

        @Test
        public void shouldCallTheManagerRepositoryToSaveTheGivenManager() {
            final Manager manager = Manager.builder().managerId(1234).build();

            managerService.updateManager(manager);

            managerRepository.save(manager);
        }
    }

    @Nested
    class UpdateManagersForNewSeason {

        @Test
        public void shouldCallTheRepositoryToCopyManagersForNewYear() {
            managerService.updateManagersForNewSeason(null, null);

            verify(managerRepository).copyManagersForNewYear(any(), any());
        }

        @Test
        public void shouldCallPassThePreviousYearAndNewYearToTheRepository() {
            managerService.updateManagersForNewSeason("2001", "2010");

            verify(managerRepository).copyManagersForNewYear("2001", "2010");
        }
    }

    @Nested
    class GetActiveManagersForYear {

        @Test
        public void shouldCallRepositoryToRetrieveManagersForGivenYearThatAreNotRetired() {
            managerService.getActiveManagersForYear("1999");

            verify(managerRepository).findAll(captor.capture());

            final Manager manager = captor.getValue().getProbe();

            assertEquals("1999", manager.getYear());
            assertEquals(0, manager.getRetired());
        }

        @Test
        public void shouldReturnTheManagersReturnedByTheRepository() {
            final List<Manager> expectedManagers = Collections.singletonList(Manager.builder().managerId(1).build());

            when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any())).thenReturn(expectedManagers);

            final List<Manager> actualManagers = managerService.getActiveManagersForYear("1999");

            assertEquals(expectedManagers, actualManagers);
        }
    }

    @Nested
    class UpdateManagers {

        @Test
        public void shouldCallTheManagerRepositoryToSaveTheGivenManagerList() {
            final List<Manager> managerList = Collections.singletonList(Manager.builder().managerId(1).year("2000").build());

            managerService.updateManagers(managerList);

            verify(managerRepository).saveAll(managerList);
        }
    }
}