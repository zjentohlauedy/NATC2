package org.natc.app.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.*;
import org.natc.app.exception.NATCException;
import org.natc.app.exception.NameGenerationException;
import org.natc.app.repository.FirstNameRepository;
import org.natc.app.repository.LastNameRepository;
import org.natc.app.repository.ManagerRepository;
import org.natc.app.repository.PlayerRepository;
import org.springframework.data.domain.Example;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NameServiceTest {

    @Captor
    private ArgumentCaptor<Example<Manager>> managerCaptor;

    @Captor
    private ArgumentCaptor<Example<Player>> playerCaptor;

    @Mock
    private FirstNameRepository firstNameRepository;

    @Mock
    private LastNameRepository lastNameRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private NameService nameService;

    @Nested
    class GenerateName {

        @Test
        void shouldCallFirstNameRepositoryToGetRandomFirstName() throws NATCException {
            when(firstNameRepository.findRandomName()).thenReturn(Optional.of(FirstName.builder().build()));
            when(lastNameRepository.findRandomName()).thenReturn(Optional.of(LastName.builder().build()));

            nameService.generateName();

            verify(firstNameRepository).findRandomName();
        }

        @Test
        void shouldCallLastNameRepositoryToGetRandomLastName() throws NATCException {
            when(firstNameRepository.findRandomName()).thenReturn(Optional.of(FirstName.builder().build()));
            when(lastNameRepository.findRandomName()).thenReturn(Optional.of(LastName.builder().build()));

            nameService.generateName();

            verify(lastNameRepository).findRandomName();
        }

        @Test
        void shouldReturnAFullName() throws NATCException {
            final FirstName expectedFirstName = FirstName.builder().name("Isaac").build();
            final LastName expectedLastName = LastName.builder().name("Jacobs").build();

            when(firstNameRepository.findRandomName()).thenReturn(Optional.of(expectedFirstName));
            when(lastNameRepository.findRandomName()).thenReturn(Optional.of(expectedLastName));

            final FullName fullName = nameService.generateName();

            assertEquals(expectedFirstName.getName(), fullName.getFirstName());
            assertEquals(expectedLastName.getName(), fullName.getLastName());
        }

        @Test
        void shouldThrowNameGenerationExceptionIfFirstNameCannotBeFound() {
            when(firstNameRepository.findRandomName()).thenReturn(Optional.empty());

            assertThrows(NameGenerationException.class, () -> nameService.generateName());
        }

        @Test
        void shouldThrowNameGenerationExceptionIfLastNameCannotBeFound() {
            when(firstNameRepository.findRandomName()).thenReturn(Optional.of(FirstName.builder().build()));
            when(lastNameRepository.findRandomName()).thenReturn(Optional.empty());

            assertThrows(NameGenerationException.class, () -> nameService.generateName());
        }

        @Test
        void shouldCallManagerRepositoryToFindExistingManagerWithSameNameGenerated() throws NATCException {
            final FirstName expectedFirstName = FirstName.builder().name("Isaac").build();
            final LastName expectedLastName = LastName.builder().name("Jacobs").build();

            when(firstNameRepository.findRandomName()).thenReturn(Optional.of(expectedFirstName));
            when(lastNameRepository.findRandomName()).thenReturn(Optional.of(expectedLastName));

            nameService.generateName();

            verify(managerRepository).findAll(managerCaptor.capture());

            final Manager manager = managerCaptor.getValue().getProbe();

            assertEquals(expectedFirstName.getName(), manager.getFirstName());
            assertEquals(expectedLastName.getName(), manager.getLastName());
        }

        @Test
        void shouldCallPlayerRepositoryToFindExistingPlayerWithSameNameGenerated() throws NATCException {
            final FirstName expectedFirstName = FirstName.builder().name("Isaac").build();
            final LastName expectedLastName = LastName.builder().name("Jacobs").build();

            when(firstNameRepository.findRandomName()).thenReturn(Optional.of(expectedFirstName));
            when(lastNameRepository.findRandomName()).thenReturn(Optional.of(expectedLastName));

            nameService.generateName();

            verify(playerRepository).findAll(playerCaptor.capture());

            final Player player = playerCaptor.getValue().getProbe();

            assertEquals(expectedFirstName.getName(), player.getFirstName());
            assertEquals(expectedLastName.getName(), player.getLastName());
        }

        @Test
        void shouldRegenerateNameIfManagerIsFoundWithTheSameName() throws NATCException {
            final FirstName expectedFirstName = FirstName.builder().name("Isaac").build();
            final LastName expectedLastName = LastName.builder().name("Jacobs").build();

            when(firstNameRepository.findRandomName()).thenReturn(Optional.of(expectedFirstName));
            when(lastNameRepository.findRandomName()).thenReturn(Optional.of(expectedLastName));

            when(managerRepository.findAll(ArgumentMatchers.<Example<Manager>>any()))
                    .thenReturn(Collections.singletonList(Manager.builder().build()))
                    .thenReturn(Collections.emptyList());

            nameService.generateName();

            verify(firstNameRepository, times(2)).findRandomName();
            verify(lastNameRepository, times(2)).findRandomName();
        }

        @Test
        void shouldRegenerateNameIfPlayerIsFoundWithTheSameName() throws NATCException {
            final FirstName expectedFirstName = FirstName.builder().name("Isaac").build();
            final LastName expectedLastName = LastName.builder().name("Jacobs").build();

            when(firstNameRepository.findRandomName()).thenReturn(Optional.of(expectedFirstName));
            when(lastNameRepository.findRandomName()).thenReturn(Optional.of(expectedLastName));

            when(playerRepository.findAll(ArgumentMatchers.<Example<Player>>any()))
                    .thenReturn(Collections.singletonList(Player.builder().build()))
                    .thenReturn(Collections.emptyList());

            nameService.generateName();

            verify(firstNameRepository, times(2)).findRandomName();
            verify(lastNameRepository, times(2)).findRandomName();
        }
    }
}