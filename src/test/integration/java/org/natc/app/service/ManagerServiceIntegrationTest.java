package org.natc.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.ManagerAward;
import org.natc.app.entity.domain.ManagerStyle;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.ManagerRepository;
import org.natc.app.util.TestHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ManagerServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private TestHelpers testHelpers;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ManagerService managerService;

    @BeforeEach
    public void setup() {
        testHelpers.seedFirstAndLastNames();
    }

    @Test
    public void generateManagers_ShouldCreateTheGivenNumberOfManagersInTheDatabase() throws NATCException {
        managerService.generateManagers("1234", 100);

        final List<Manager> managers = managerRepository.findAll();

        assertEquals(100, managers.size());
    }

    @Test
    public void generateManagers_ShouldCreateAllManagersForTheGivenYear() throws NATCException {
        managerService.generateManagers("1234", 100);

        final List<Manager> managers = managerRepository.findAll();

        assertEquals(100, managers.stream().filter(manager -> manager.getYear().equals("1234")).count());
    }

    @Test
    public void generateManagers_ShouldCreateEveryManagerWithAUniqueName() throws NATCException {
        managerService.generateManagers("1234", 1000);

        final List<Manager> managers = managerRepository.findAll();

        final Set<String> names = managers.stream().map(manager -> manager.getFirstName() + manager.getLastName()).collect(Collectors.toSet());

        assertEquals(1000, names.size());
    }

    @Test
    public void generateManagers_ShouldCreateEveryManagerWithAUniqueManagerId() throws NATCException {
        managerService.generateManagers("1234", 100);

        final List<Manager> managers = managerRepository.findAll();

        final Set<Integer> managerIds = managers.stream().map(Manager::getManagerId).collect(Collectors.toSet());

        assertEquals(100, managerIds.size());
    }

    @Test
    public void generateManagers_ShouldCreateEveryManagerWithAnAgeBetweenFortyAndFifty() throws NATCException {
        managerService.generateManagers("1234", 100);

        final List<Manager> managers = managerRepository.findAll();

        assertEquals(100, managers.stream().filter(manager -> manager.getAge() >= 40 && manager.getAge() <= 50).count());
    }

    @Test
    public void generateManagers_ShouldCreateEveryManagerWithRatingsBetweenZeroAndOne() throws NATCException {
        managerService.generateManagers("1234", 100);

        final List<Manager> managers = managerRepository.findAll();

        assertEquals(100, managers.stream().filter(manager -> manager.getOffense() >= 0.0 && manager.getOffense() <= 1.0).count());
        assertEquals(100, managers.stream().filter(manager -> manager.getDefense() >= 0.0 && manager.getDefense() <= 1.0).count());
        assertEquals(100, managers.stream().filter(manager -> manager.getIntangible() >= 0.0 && manager.getIntangible() <= 1.0).count());
        assertEquals(100, managers.stream().filter(manager -> manager.getPenalties() >= 0.0 && manager.getPenalties() <= 1.0).count());
        assertEquals(100, managers.stream().filter(manager -> manager.getVitality() >= 0.0 && manager.getVitality() <= 1.0).count());
    }

    @Test
    public void generateManagers_ShouldCreateEveryManagerWithNewHireReleasedAndRetiredAsZero() throws NATCException {
        managerService.generateManagers("1234", 100);

        final List<Manager> managers = managerRepository.findAll();

        assertEquals(100, managers.stream().filter(manager -> manager.getNewHire() == 0).count());
        assertEquals(100, managers.stream().filter(manager -> manager.getReleased() == 0).count());
        assertEquals(100, managers.stream().filter(manager -> manager.getRetired() == 0).count());
    }

    @Test
    public void generateManagers_ShouldCreateEveryManagerWithScoresAndSeasonsAsZero() throws NATCException {
        managerService.generateManagers("1234", 100);

        final List<Manager> managers = managerRepository.findAll();

        assertEquals(100, managers.stream().filter(manager -> manager.getSeasons() == 0).count());
        assertEquals(100, managers.stream().filter(manager -> manager.getTotalSeasons() == 0).count());
        assertEquals(100, managers.stream().filter(manager -> manager.getScore() == 0).count());
        assertEquals(100, managers.stream().filter(manager -> manager.getTotalScore() == 0).count());
    }

    @Test
    public void generateManagers_ShouldCreateEveryManagerWithAStyle() throws NATCException {
        final List<Integer> managerStyles = Arrays.asList(
                ManagerStyle.OFFENSIVE.getValue(),
                ManagerStyle.DEFENSIVE.getValue(),
                ManagerStyle.INTANGIBLE.getValue(),
                ManagerStyle.PENALTIES.getValue(),
                ManagerStyle.BALANCED.getValue()
        );

        managerService.generateManagers("1234", 100);

        final List<Manager> managers = managerRepository.findAll();

        assertEquals(100, managers.stream().filter(manager -> managerStyles.contains(manager.getStyle())).count());
    }

    @Test
    public void generateManagers_ShouldContinueToIncrementManagerIdsOnSubsequentCalls() throws NATCException {
        managerService.generateManagers("1234", 5);
        managerService.generateManagers("1234", 5);
        managerService.generateManagers("1234", 5);
        managerService.generateManagers("1234", 5);
        managerService.generateManagers("1234", 5);

        final List<Manager> managers = managerRepository.findAll();

        assertEquals(25, managers.size());
        assertEquals(25, managers.stream().filter(manager -> manager.getManagerId() >= 1 && manager.getManagerId() <= 25).count());
    }

    @Test
    public void updateManager_ShouldModifyAnExistingManagerRecordInTheDatabase() {
        final Manager originalManager = Manager.builder().managerId(123).year("2001").build();

        managerRepository.save(originalManager);

        final Manager updatedManager = Manager.builder().managerId(123).year("2001").firstName("Jake").lastName("Roberts").build();

        managerService.updateManager(updatedManager);

        final Manager persistedManager = managerRepository.findOne(Example.of(originalManager)).orElse(null);

        assertNotNull(persistedManager);

        assertEquals(updatedManager.getManagerId(), persistedManager.getManagerId());
        assertEquals(updatedManager.getYear(), persistedManager.getYear());
        assertEquals(updatedManager.getFirstName(), persistedManager.getFirstName());
        assertEquals(updatedManager.getLastName(), persistedManager.getLastName());
    }
    
    @Test
    public void updateManagersForNewSeason_ShouldCopyManagerRecordsFromOneYearToAnother() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2001").build(),
                Manager.builder().managerId(2).year("2001").build(),
                Manager.builder().managerId(3).year("2001").build()
        );

        managerRepository.saveAll(managerList);

        managerService.updateManagersForNewSeason("2001", "2002");

        final Example<Manager> queryCriteria = Example.of(Manager.builder().year("2002").build());
        final List<Manager> copiedManagers = managerRepository.findAll(queryCriteria);

        assertEquals(managerList.size(), copiedManagers.size());
    }
    
    @Test
    public void updateManagersForNewSeason_ShouldOnlyCopyManagerRecordsFromPreviousYear() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2001").build(),
                Manager.builder().managerId(2).year("2002").build(),
                Manager.builder().managerId(3).year("2003").build()
        );

        managerRepository.saveAll(managerList);

        managerService.updateManagersForNewSeason("2003", "2004");

        final Example<Manager> queryCriteria = Example.of(Manager.builder().year("2004").build());
        final List<Manager> copiedManagers = managerRepository.findAll(queryCriteria);

        assertEquals(1, copiedManagers.size());
        assertEquals(3, copiedManagers.get(0).getManagerId());
    }
    
    @Test
    public void updateManagersForNewSeason_ShouldOnlyCopyNecessaryFieldsToNewYear() {
        final Manager originalManager = Manager.builder()
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
                .style(ManagerStyle.BALANCED.getValue())
                .newHire(1)
                .released(0)
                .retired(0)
                .formerTeamId(111)
                .allstarTeamId(222)
                .award(ManagerAward.NONE.getValue())
                .seasons(12)
                .score(33)
                .totalSeasons(18)
                .totalScore(77)
                .build();

        managerRepository.save(originalManager);

        managerService.updateManagersForNewSeason("1991", "2002");

        final Example<Manager> queryCriteria = Example.of(Manager.builder().year("2002").build());
        final List<Manager> copiedManagers = managerRepository.findAll(queryCriteria);

        assertEquals(1, copiedManagers.size());

        final Manager copiedManager = copiedManagers.get(0);

        assertEquals(originalManager.getManagerId(), copiedManager.getManagerId());
        assertEquals(originalManager.getTeamId(), copiedManager.getTeamId());
        assertEquals(originalManager.getPlayerId(), copiedManager.getPlayerId());
        assertEquals(originalManager.getFirstName(), copiedManager.getFirstName());
        assertEquals(originalManager.getLastName(), copiedManager.getLastName());
        assertEquals(originalManager.getAge(), copiedManager.getAge());
        assertEquals(originalManager.getOffense(), copiedManager.getOffense());
        assertEquals(originalManager.getDefense(), copiedManager.getDefense());
        assertEquals(originalManager.getIntangible(), copiedManager.getIntangible());
        assertEquals(originalManager.getPenalties(), copiedManager.getPenalties());
        assertEquals(originalManager.getVitality(), copiedManager.getVitality());
        assertEquals(originalManager.getStyle(), copiedManager.getStyle());
        assertEquals(originalManager.getNewHire(), copiedManager.getNewHire());
        assertEquals(originalManager.getReleased(), copiedManager.getReleased());
        assertEquals(originalManager.getRetired(), copiedManager.getRetired());
        assertEquals(originalManager.getSeasons(), copiedManager.getSeasons());
        assertEquals(originalManager.getScore(), copiedManager.getScore());
        assertEquals(originalManager.getTotalSeasons(), copiedManager.getTotalSeasons());
        assertEquals(originalManager.getTotalScore(), copiedManager.getTotalScore());

        assertNull(copiedManager.getFormerTeamId());
        assertNull(copiedManager.getAllstarTeamId());
        assertNull(copiedManager.getAward());
    }
}