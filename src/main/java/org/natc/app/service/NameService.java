package org.natc.app.service;

import org.natc.app.entity.domain.*;
import org.natc.app.exception.NATCException;
import org.natc.app.exception.NameGenerationException;
import org.natc.app.repository.FirstNameRepository;
import org.natc.app.repository.LastNameRepository;
import org.natc.app.repository.ManagerRepository;
import org.natc.app.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class NameService {

    private final FirstNameRepository firstNameRepository;
    private final LastNameRepository lastNameRepository;
    private final ManagerRepository managerRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public NameService(
            final FirstNameRepository firstNameRepository,
            final LastNameRepository lastNameRepository,
            final ManagerRepository managerRepository,
            final PlayerRepository playerRepository) {
        this.firstNameRepository = firstNameRepository;
        this.lastNameRepository = lastNameRepository;
        this.managerRepository = managerRepository;
        this.playerRepository = playerRepository;
    }

    public FullName generateName() throws NATCException {
        FullName fullName = null;

        while (true) {

            final FirstName firstName = firstNameRepository.findRandomName().orElseThrow(NameGenerationException::new);
            final LastName lastName = lastNameRepository.findRandomName().orElseThrow(NameGenerationException::new);

            final Example<Manager> managerExample = Example.of(
                    Manager.builder().firstName(firstName.getName()).lastName(lastName.getName()).build()
            );

            if (managerRepository.findAll(managerExample).size() > 0) continue;

            final Example<Player> playerExample = Example.of(
                    Player.builder().firstName(firstName.getName()).lastName(lastName.getName()).build()
            );

            if (playerRepository.findAll(playerExample).size() > 0) continue;

            fullName = FullName.builder().firstName(firstName.getName()).lastName(lastName.getName()).build();

            break;
        }

        return fullName;
    }
}
