package org.natc.natc.service;

import org.junit.jupiter.api.Test;
import org.natc.natc.entity.domain.Manager;
import org.natc.natc.entity.domain.ManagerAward;
import org.natc.natc.entity.domain.ManagerStyle;
import org.natc.natc.entity.request.ManagerSearchRequest;
import org.natc.natc.entity.response.ManagerResponse;
import org.natc.natc.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

class ManagerSearchServiceIntegrationTest extends NATCServiceIntegrationTest {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ManagerSearchService managerSearchService;

    @Test
    public void shouldReturnAManagerFromTheDatabaseMappedToAResponse() {
        final Manager manager = Manager.builder()
                .managerId(1)
                .year("1998")
                .firstName("John")
                .lastName("Doe")
                .age(46)
                .build();

        managerRepository.save(manager);

        final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

        assertEquals(1, result.size());

        final ManagerResponse response = result.get(0);

        assertEquals(manager.getManagerId(), response.getManagerId());
        assertEquals(manager.getYear(), response.getYear());
        assertEquals(manager.getFirstName(), response.getFirstName());
        assertEquals(manager.getLastName(), response.getLastName());
        assertEquals(manager.getAge(), response.getAge());
    }

    @Test
    public void shouldMapAllManagerFieldsToTheManagerResponse() {
        final Manager manager = Manager.builder()
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

        managerRepository.save(manager);

        final List<ManagerResponse> result = managerSearchService.fetchAll(new ManagerSearchRequest());

        assertEquals(1, result.size());

        final ManagerResponse response = result.get(0);

        assertNotNull(response.getManagerId());
        assertNotNull(response.getTeamId());
        assertNotNull(response.getPlayerId());
        assertNotNull(response.getYear());
        assertNotNull(response.getFirstName());
        assertNotNull(response.getLastName());
        assertNotNull(response.getAge());
        assertNotNull(response.getOffense());
        assertNotNull(response.getDefense());
        assertNotNull(response.getIntangible());
        assertNotNull(response.getPenalties());
        assertNotNull(response.getVitality());
        assertNotNull(response.getStyle());
        assertNotNull(response.getNewHire());
        assertNotNull(response.getReleased());
        assertNotNull(response.getRetired());
        assertNotNull(response.getFormerTeamId());
        assertNotNull(response.getAllstarTeamId());
        assertNotNull(response.getAward());
        assertNotNull(response.getSeasons());
        assertNotNull(response.getScore());
        assertNotNull(response.getTotalSeasons());
        assertNotNull(response.getTotalScore());
    }

    @Test
    public void shouldReturnAllEntriesWhenSearchingWithoutValues() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(2).playerId(3).build(),
                Manager.builder().managerId(2).year("2001").teamId(3).playerId(4).build(),
                Manager.builder().managerId(3).year("2002").teamId(4).playerId(5).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder().build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnNoEntriesWhenSearchingGivenNoDataInTheDatabase() {
        final ManagerSearchRequest request = ManagerSearchRequest.builder().build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(0, result.size());
    }
}