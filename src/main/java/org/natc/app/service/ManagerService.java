package org.natc.app.service;

import org.natc.app.entity.domain.FullName;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.ManagerStyle;
import org.natc.app.entity.domain.Player;
import org.natc.app.exception.NATCException;
import org.natc.app.repository.ManagerRepository;
import org.natc.app.service.analysis.ManagerAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final NameService nameService;
    private final ManagerAnalyzer managerAnalyzer;

    @Autowired
    public ManagerService(final ManagerRepository managerRepository, final NameService nameService, final ManagerAnalyzer managerAnalyzer) {
        this.managerRepository = managerRepository;
        this.nameService = nameService;
        this.managerAnalyzer = managerAnalyzer;
    }

    public List<Manager> generateManagers(final String year, final Integer count) throws NATCException {
        final List<Manager> managerList = new ArrayList<>();

        int lastManagerId = managerRepository.findMaxManagerId().orElse(0);

        for (int i = 0; i < count; i++) {
            final FullName name = nameService.generateName();
            final Manager manager = Manager.generate(++lastManagerId, year, name.getFirstName(), name.getLastName());
            final ManagerStyle style = managerAnalyzer.determineManagerStyle(manager);
            manager.setStyle(Objects.isNull(style) ? null : style.getValue());

            managerRepository.save(manager);

            managerList.add(manager);
        }

        return managerList;
    }

    public Manager generateManagerFromPlayer(final String year, final Player player) {
        final Manager manager = Manager.generate(0, year, player.getFirstName(), player.getLastName());
        final int differenceInYears = Integer.parseInt(year) - Integer.parseInt(player.getYear());

        final int lastManagerId = managerRepository.findMaxManagerId().orElse(0);

        manager.setManagerId(lastManagerId + 1);
        manager.setPlayerId(player.getPlayerId());
        manager.setAge(player.getAge() + differenceInYears);
        manager.setOffense(player.getOffensiveRating());
        manager.setDefense(player.getDefensiveRating());
        manager.setIntangible(player.getIntangibleRating());
        manager.setPenalties(player.getPenaltyRating());
        manager.setVitality(player.getVitality());

        final ManagerStyle style = managerAnalyzer.determineManagerStyle(manager);
        manager.setStyle(Objects.isNull(style) ? null : style.getValue());

        managerRepository.save(manager);

        return manager;
    }

    public void updateManager(final Manager manager) {
        managerRepository.save(manager);
    }

    public void updateManagers(final List<Manager> managerList) {
        managerRepository.saveAll(managerList);
    }

    public void updateManagersForNewSeason(final String previousYear, final String newYear) {
        managerRepository.copyManagersForNewYear(previousYear, newYear);
    }

    public List<Manager> getActiveManagersForYear(final String year) {
        return managerRepository.findAll(Example.of(Manager.builder().year(year).retired(0).build()));
    }
}
