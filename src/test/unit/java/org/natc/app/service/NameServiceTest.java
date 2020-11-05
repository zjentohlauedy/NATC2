package org.natc.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.domain.FirstName;
import org.natc.app.entity.domain.FullName;
import org.natc.app.entity.domain.LastName;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.Player;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    public void generateName_ShouldCallFirstNameRepositoryToGetRandomFirstName() throws NATCException {
        when(firstNameRepository.findRandomName()).thenReturn(Optional.of(FirstName.builder().build()));
        when(lastNameRepository.findRandomName()).thenReturn(Optional.of(LastName.builder().build()));

        nameService.generateName();

        verify(firstNameRepository).findRandomName();
    }

    @Test
    public void generateName_ShouldCallLastNameRepositoryToGetRandomLastName() throws NATCException {
        when(firstNameRepository.findRandomName()).thenReturn(Optional.of(FirstName.builder().build()));
        when(lastNameRepository.findRandomName()).thenReturn(Optional.of(LastName.builder().build()));

        nameService.generateName();

        verify(lastNameRepository).findRandomName();
    }

    @Test
    public void generateName_ShouldReturnAFullName() throws NATCException {
        final FirstName expectedFirstName = FirstName.builder().name("Isaac").build();
        final LastName expectedLastName = LastName.builder().name("Jacobs").build();

        when(firstNameRepository.findRandomName()).thenReturn(Optional.of(expectedFirstName));
        when(lastNameRepository.findRandomName()).thenReturn(Optional.of(expectedLastName));

        final FullName fullName = nameService.generateName();

        assertEquals(expectedFirstName.getName(), fullName.getFirstName());
        assertEquals(expectedLastName.getName(), fullName.getLastName());
    }

    @Test
    public void generateName_ShouldThrowNameGenerationExceptionIfFirstNameCannotBeFound() {
        when(firstNameRepository.findRandomName()).thenReturn(Optional.empty());

        assertThrows(NameGenerationException.class, () -> nameService.generateName());
    }

    @Test
    public void generateName_ShouldThrowNameGenerationExceptionIfLastNameCannotBeFound() {
        when(firstNameRepository.findRandomName()).thenReturn(Optional.of(FirstName.builder().build()));
        when(lastNameRepository.findRandomName()).thenReturn(Optional.empty());

        assertThrows(NameGenerationException.class, () -> nameService.generateName());
    }

    @Test
    public void generateName_ShouldCallManagerRepositoryToFindExistingManagerWithSameNameGenerated() throws NATCException {
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
    public void generateName_ShouldCallPlayerRepositoryToFindExistingPlayerWithSameNameGenerated() throws NATCException {
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
    public void generateName_ShouldRegenerateNameIfManagerIsFoundWithTheSameName() throws NATCException {
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
    public void generateName_ShouldRegenerateNameIfPlayerIsFoundWithTheSameName() throws NATCException {
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