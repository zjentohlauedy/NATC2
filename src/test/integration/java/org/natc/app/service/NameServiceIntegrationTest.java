package org.natc.app.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.FirstName;
import org.natc.app.entity.domain.FullName;
import org.natc.app.entity.domain.LastName;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.Player;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.FirstNameRepository;
import org.natc.app.repository.LastNameRepository;
import org.natc.app.repository.ManagerRepository;
import org.natc.app.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NameServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private FirstNameRepository firstNameRepository;

    @Autowired
    private LastNameRepository lastNameRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private NameService nameService;

    @Nested
    class GenerateName {

        @Test
        void shouldBuildNameFromDataInFirstNameAndLastNameTables() throws NATCException {
            firstNameRepository.save(FirstName.builder().name("Joe").frequency(1.0).build());
            lastNameRepository.save(LastName.builder().name("Blow").frequency(1.0).build());

            final FullName generatedName = nameService.generateName();

            assertEquals("Joe", generatedName.getFirstName());
            assertEquals("Blow", generatedName.getLastName());
        }

        @Test
        void shouldChooseFirstNamesRandomly() throws NATCException {
            final List<FirstName> firstNameList = Arrays.asList(
                    FirstName.builder().name("James").frequency(1.0).build(),
                    FirstName.builder().name("John").frequency(1.0).build(),
                    FirstName.builder().name("Robert").frequency(1.0).build(),
                    FirstName.builder().name("Michael").frequency(1.0).build(),
                    FirstName.builder().name("William").frequency(1.0).build(),
                    FirstName.builder().name("David").frequency(1.0).build(),
                    FirstName.builder().name("Richard").frequency(1.0).build(),
                    FirstName.builder().name("Charles").frequency(1.0).build(),
                    FirstName.builder().name("Joseph").frequency(1.0).build(),
                    FirstName.builder().name("Thomas").frequency(1.0).build()
            );

            firstNameRepository.saveAll(firstNameList);
            lastNameRepository.save(LastName.builder().name("Blow").frequency(1.0).build());

            final List<FullName> fullNames = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                fullNames.add(nameService.generateName());
            }

            assertEquals(10, fullNames.stream().map(FullName::getFirstName).distinct().count());
        }

        @Test
        void shouldChooseLastNamesRandomly() throws NATCException {
            final List<LastName> lastNameList = Arrays.asList(
                    LastName.builder().name("Smith").frequency(1.0).build(),
                    LastName.builder().name("Johnson").frequency(1.0).build(),
                    LastName.builder().name("Williams").frequency(1.0).build(),
                    LastName.builder().name("Jones").frequency(1.0).build(),
                    LastName.builder().name("Brown").frequency(1.0).build(),
                    LastName.builder().name("Davis").frequency(1.0).build(),
                    LastName.builder().name("Miller").frequency(1.0).build(),
                    LastName.builder().name("Wilson").frequency(1.0).build(),
                    LastName.builder().name("Moore").frequency(1.0).build(),
                    LastName.builder().name("Taylor").frequency(1.0).build()
            );

            firstNameRepository.save(FirstName.builder().name("Joe").frequency(1.0).build());
            lastNameRepository.saveAll(lastNameList);

            final List<FullName> fullNames = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                fullNames.add(nameService.generateName());
            }

            assertEquals(10, fullNames.stream().map(FullName::getLastName).distinct().count());
        }

        @Test
        void shouldChooseHighFrequencyFirstNamesMoreOftenThanLowFrequencyFirstNames() throws NATCException {
            final List<FirstName> firstNameList = Arrays.asList(
                    FirstName.builder().name("James").frequency(1.0).build(),
                    FirstName.builder().name("Thomas").frequency(0.001).build()
            );

            final List<LastName> lastNameList = Arrays.asList(
                    LastName.builder().name("Smith").frequency(1.0).build(),
                    LastName.builder().name("Taylor").frequency(1.0).build()
            );

            firstNameRepository.saveAll(firstNameList);
            lastNameRepository.saveAll(lastNameList);

            final List<FullName> fullNames = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                fullNames.add(nameService.generateName());
            }

            final long jamesCount = fullNames.stream().filter(fullName -> fullName.getFirstName().equals("James")).count();
            final long thomasCount = fullNames.stream().filter(fullName -> fullName.getFirstName().equals("Thomas")).count();

            assertTrue(jamesCount > thomasCount);
        }

        @Test
        void shouldChooseHighFrequencyLastNamesMoreOftenThanLowFrequencyLastNames() throws NATCException {
            final List<FirstName> firstNameList = Arrays.asList(
                    FirstName.builder().name("James").frequency(1.0).build(),
                    FirstName.builder().name("Thomas").frequency(1.0).build()
            );

            final List<LastName> lastNameList = Arrays.asList(
                    LastName.builder().name("Smith").frequency(0.001).build(),
                    LastName.builder().name("Taylor").frequency(1.0).build()
            );

            firstNameRepository.saveAll(firstNameList);
            lastNameRepository.saveAll(lastNameList);

            final List<FullName> fullNames = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                fullNames.add(nameService.generateName());
            }

            final long smithCount = fullNames.stream().filter(fullName -> fullName.getLastName().equals("Smith")).count();
            final long taylorCount = fullNames.stream().filter(fullName -> fullName.getLastName().equals("Taylor")).count();

            assertTrue(taylorCount > smithCount);
        }

        @Test
        void shouldNotGenerateANameThatAlreadyExistsAsAManager() throws NATCException {
            final List<FirstName> firstNameList = Arrays.asList(
                    FirstName.builder().name("James").frequency(1.0).build(),
                    FirstName.builder().name("Thomas").frequency(1.0).build()
            );

            final List<LastName> lastNameList = Arrays.asList(
                    LastName.builder().name("Smith").frequency(1.0).build(),
                    LastName.builder().name("Taylor").frequency(1.0).build()
            );

            final List<Manager> managerList = Arrays.asList(
                    Manager.builder().managerId(1).year("2000").firstName("James").lastName("Smith").build(),
                    Manager.builder().managerId(2).year("2000").firstName("James").lastName("Taylor").build(),
                    Manager.builder().managerId(3).year("2000").firstName("Thomas").lastName("Smith").build()
            );

            firstNameRepository.saveAll(firstNameList);
            lastNameRepository.saveAll(lastNameList);
            managerRepository.saveAll(managerList);

            final FullName generatedName = nameService.generateName();

            assertEquals("Thomas", generatedName.getFirstName());
            assertEquals("Taylor", generatedName.getLastName());
        }

        @Test
        void shouldNotGenerateANameThatAlreadyExistsAsAPlayer() throws NATCException {
            final List<FirstName> firstNameList = Arrays.asList(
                    FirstName.builder().name("James").frequency(1.0).build(),
                    FirstName.builder().name("Thomas").frequency(1.0).build()
            );

            final List<LastName> lastNameList = Arrays.asList(
                    LastName.builder().name("Smith").frequency(1.0).build(),
                    LastName.builder().name("Taylor").frequency(1.0).build()
            );

            final List<Player> playerList = Arrays.asList(
                    Player.builder().playerId(1).year("2000").firstName("James").lastName("Smith").build(),
                    Player.builder().playerId(2).year("2000").firstName("James").lastName("Taylor").build(),
                    Player.builder().playerId(3).year("2000").firstName("Thomas").lastName("Smith").build()
            );

            firstNameRepository.saveAll(firstNameList);
            lastNameRepository.saveAll(lastNameList);
            playerRepository.saveAll(playerList);

            final FullName generatedName = nameService.generateName();

            assertEquals("Thomas", generatedName.getFirstName());
            assertEquals("Taylor", generatedName.getLastName());
        }
    }
}
