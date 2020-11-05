package org.natc.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.Manager;
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
}