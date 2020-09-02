package org.natc.app.service;

import org.junit.jupiter.api.Test;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.ManagerAward;
import org.natc.app.entity.domain.ManagerStyle;
import org.natc.app.entity.request.ManagerSearchRequest;
import org.natc.app.entity.response.ManagerResponse;
import org.natc.app.repository.ManagerRepository;
import org.natc.app.service.search.ManagerSearchService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


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

    @Test
    public void shouldReturnAllEntriesForManagerWhenSearchingByManagerId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(1).year("2001").teamId(2).playerId(2).build(),
                Manager.builder().managerId(1).year("2002").teamId(3).playerId(3).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForManagerWhenSearchingByManagerId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(1).year("2001").teamId(2).playerId(2).build(),
                Manager.builder().managerId(3).year("2001").teamId(2).playerId(2).build(),
                Manager.builder().managerId(1).year("2002").teamId(3).playerId(3).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getManagerId().equals(1)).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearWhenSearchingByYear() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2000").teamId(2).playerId(2).build(),
                Manager.builder().managerId(3).year("2000").teamId(3).playerId(3).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .year("2000")
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearWhenSearchingByYear() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(1).year("2001").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2000").teamId(2).playerId(2).build(),
                Manager.builder().managerId(2).year("2002").teamId(2).playerId(2).build(),
                Manager.builder().managerId(3).year("2000").teamId(3).playerId(3).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .year("2000")
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getYear().equals("2000")).count());
    }

    @Test
    public void shouldReturnAllEntriesForTeamWhenSearchingByTeamId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2001").teamId(1).playerId(2).build(),
                Manager.builder().managerId(3).year("2002").teamId(1).playerId(3).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .teamId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForTeamWhenSearchingByTeamId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2000").teamId(2).playerId(1).build(),
                Manager.builder().managerId(3).year("2001").teamId(1).playerId(2).build(),
                Manager.builder().managerId(4).year("2001").teamId(3).playerId(2).build(),
                Manager.builder().managerId(5).year("2002").teamId(1).playerId(3).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .teamId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getTeamId().equals(1)).count());
    }

    @Test
    public void shouldReturnAllEntriesForPlayerWhenSearchingByPlayerId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2001").teamId(2).playerId(1).build(),
                Manager.builder().managerId(3).year("2002").teamId(3).playerId(1).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .playerId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForPlayerWhenSearchingByPlayerId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2000").teamId(1).playerId(2).build(),
                Manager.builder().managerId(3).year("2001").teamId(2).playerId(1).build(),
                Manager.builder().managerId(4).year("2001").teamId(2).playerId(3).build(),
                Manager.builder().managerId(5).year("2002").teamId(3).playerId(1).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .playerId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t -> t.getPlayerId().equals(1)).count());
    }

    @Test
    public void shouldReturnAllEntriesForManagerAndYearWhenSearchingByManagerIdAndYear() {
        final List<Manager> managerList = Collections.singletonList(
                // Manager ID & Year are the key fields, so only one record is possible
                Manager.builder().managerId(1).year("2000").teamId(2).playerId(3).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .year("2000")
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForManagerAndYearWhenSearchingByManagerIdAndYear() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(3).playerId(4).build(),
                Manager.builder().managerId(2).year("2001").teamId(3).playerId(4).build(),
                Manager.builder().managerId(3).year("2002").teamId(3).playerId(4).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .year("2000")
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getManagerId().equals(1) && t.getYear().equals("2000")
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForManagerAndTeamWhenSearchingByManagerIdAndTeamId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(1).year("2001").teamId(1).playerId(2).build(),
                Manager.builder().managerId(1).year("2002").teamId(1).playerId(3).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .teamId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForManagerAndTeamWhenSearchingByManagerIdAndTeamId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2001").teamId(1).playerId(1).build(),
                Manager.builder().managerId(1).year("2002").teamId(2).playerId(1).build(),
                Manager.builder().managerId(1).year("2003").teamId(1).playerId(2).build(),
                Manager.builder().managerId(3).year("2004").teamId(1).playerId(2).build(),
                Manager.builder().managerId(1).year("2005").teamId(3).playerId(2).build(),
                Manager.builder().managerId(1).year("2006").teamId(1).playerId(3).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .teamId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getManagerId().equals(1) && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForManagerAndPlayerWhenSearchingByManagerIdAndPlayerId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(1).year("2001").teamId(2).playerId(1).build(),
                Manager.builder().managerId(1).year("2002").teamId(3).playerId(1).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .playerId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForManagerAndPlayerWhenSearchingByManagerIdAndPlayerId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2001").teamId(1).playerId(1).build(),
                Manager.builder().managerId(1).year("2002").teamId(1).playerId(2).build(),
                Manager.builder().managerId(1).year("2003").teamId(2).playerId(1).build(),
                Manager.builder().managerId(3).year("2004").teamId(2).playerId(1).build(),
                Manager.builder().managerId(1).year("2005").teamId(2).playerId(3).build(),
                Manager.builder().managerId(1).year("2006").teamId(3).playerId(1).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .playerId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getManagerId().equals(1) && t.getPlayerId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2000").teamId(1).playerId(2).build(),
                Manager.builder().managerId(3).year("2000").teamId(1).playerId(3).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .year("2000")
                .teamId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearAndTeamWhenSearchingByYearAndTeamId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2001").teamId(1).playerId(1).build(),
                Manager.builder().managerId(3).year("2000").teamId(2).playerId(1).build(),
                Manager.builder().managerId(4).year("2000").teamId(1).playerId(2).build(),
                Manager.builder().managerId(5).year("2002").teamId(1).playerId(2).build(),
                Manager.builder().managerId(6).year("2000").teamId(3).playerId(2).build(),
                Manager.builder().managerId(7).year("2000").teamId(1).playerId(3).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .year("2000")
                .teamId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getTeamId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearAndPlayerWhenSearchingByYearAndPlayerId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2000").teamId(2).playerId(1).build(),
                Manager.builder().managerId(3).year("2000").teamId(3).playerId(1).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .year("2000")
                .playerId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearAndPlayerWhenSearchingByYearAndPlayerId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2001").teamId(1).playerId(1).build(),
                Manager.builder().managerId(3).year("2000").teamId(1).playerId(2).build(),
                Manager.builder().managerId(4).year("2000").teamId(2).playerId(1).build(),
                Manager.builder().managerId(5).year("2002").teamId(2).playerId(1).build(),
                Manager.builder().managerId(6).year("2000").teamId(2).playerId(3).build(),
                Manager.builder().managerId(7).year("2000").teamId(3).playerId(1).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .year("2000")
                .playerId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getPlayerId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForTeamAndPlayerWhenSearchingByTeamIdAndPlayerId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2001").teamId(1).playerId(1).build(),
                Manager.builder().managerId(3).year("2002").teamId(1).playerId(1).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .teamId(1)
                .playerId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForTeamAndPlayerWhenSearchingByTeamIdAndPlayerId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2000").teamId(2).playerId(1).build(),
                Manager.builder().managerId(3).year("2000").teamId(1).playerId(2).build(),
                Manager.builder().managerId(4).year("2001").teamId(1).playerId(1).build(),
                Manager.builder().managerId(5).year("2001").teamId(3).playerId(1).build(),
                Manager.builder().managerId(6).year("2001").teamId(1).playerId(3).build(),
                Manager.builder().managerId(7).year("2002").teamId(1).playerId(1).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .teamId(1)
                .playerId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getTeamId().equals(1) && t.getPlayerId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForManagerYearAndTeamWhenSearchingByManagerIdYearAndTeamId() {
        final List<Manager> managerList = Collections.singletonList(
                // Manager ID & Year are the key fields, so only one record is possible
                Manager.builder().managerId(1).year("2000").teamId(2).playerId(3).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .year("2000")
                .teamId(2)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForManagerYearAndTeamWhenSearchingByManagerIdYearAndTeamId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(3).playerId(4).build(),
                Manager.builder().managerId(2).year("2001").teamId(4).playerId(5).build(),
                Manager.builder().managerId(3).year("2002").teamId(5).playerId(6).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .year("2000")
                .teamId(3)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getManagerId().equals(1) && t.getYear().equals("2000") && t.getTeamId().equals(3)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForManagerYearAndTeamWhenSearchingByManagerIdYearAndTeamIdAndTeamIdIsDifferent() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(3).playerId(4).build(),
                Manager.builder().managerId(2).year("2001").teamId(4).playerId(5).build(),
                Manager.builder().managerId(3).year("2002").teamId(5).playerId(6).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .year("2000")
                .teamId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForManagerYearAndPlayerWhenSearchingByManagerIdYearAndPlayerId() {
        final List<Manager> managerList = Collections.singletonList(
                // Manager ID & Year are the key fields, so only one record is possible
                Manager.builder().managerId(1).year("2000").teamId(2).playerId(3).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .year("2000")
                .playerId(3)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForManagerYearAndPlayerWhenSearchingByManagerIdYearAndPlayerId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(3).playerId(4).build(),
                Manager.builder().managerId(2).year("2001").teamId(4).playerId(5).build(),
                Manager.builder().managerId(3).year("2002").teamId(5).playerId(6).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .year("2000")
                .playerId(4)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getManagerId().equals(1) && t.getYear().equals("2000") && t.getPlayerId().equals(4)
        ).count());
    }

    @Test
    public void shouldReturnNoEntriesForManagerYearAndPlayerWhenSearchingByManagerIdYearAndPlayerIdAndPlayerIdIsDifferent() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(3).playerId(4).build(),
                Manager.builder().managerId(2).year("2001").teamId(4).playerId(5).build(),
                Manager.builder().managerId(3).year("2002").teamId(5).playerId(6).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .year("2000")
                .playerId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnAllEntriesForManagerTeamAndPlayerWhenSearchingByManagerIdTeamIdAndPlayerId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(1).year("2001").teamId(1).playerId(1).build(),
                Manager.builder().managerId(1).year("2002").teamId(1).playerId(1).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .teamId(1)
                .playerId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForManagerTeamAndPlayerWhenSearchingByManagerIdTeamIdAndPlayerId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2001").teamId(1).playerId(1).build(),
                Manager.builder().managerId(1).year("2002").teamId(2).playerId(1).build(),
                Manager.builder().managerId(1).year("2003").teamId(1).playerId(2).build(),
                Manager.builder().managerId(1).year("2004").teamId(1).playerId(1).build(),
                Manager.builder().managerId(3).year("2005").teamId(1).playerId(1).build(),
                Manager.builder().managerId(1).year("2006").teamId(3).playerId(1).build(),
                Manager.builder().managerId(1).year("2007").teamId(1).playerId(3).build(),
                Manager.builder().managerId(1).year("2008").teamId(1).playerId(1).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .teamId(1)
                .playerId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getManagerId().equals(1) && t.getTeamId().equals(1) && t.getPlayerId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnAllEntriesForYearTeamAndPlayerWhenSearchingByYearTeamIdAndPlayerId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(3).year("2000").teamId(1).playerId(1).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .year("2000")
                .teamId(1)
                .playerId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
    }

    @Test
    public void shouldOnlyReturnEntriesForYearTeamAndPlayerWhenSearchingByYearTeamIdAndPlayerId() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2001").teamId(1).playerId(1).build(),
                Manager.builder().managerId(3).year("2000").teamId(2).playerId(1).build(),
                Manager.builder().managerId(4).year("2000").teamId(1).playerId(2).build(),
                Manager.builder().managerId(5).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(6).year("2002").teamId(1).playerId(1).build(),
                Manager.builder().managerId(7).year("2000").teamId(3).playerId(1).build(),
                Manager.builder().managerId(8).year("2000").teamId(1).playerId(3).build(),
                Manager.builder().managerId(9).year("2000").teamId(1).playerId(1).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .year("2000")
                .teamId(1)
                .playerId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(3, result.size());
        assertEquals(3, result.stream().filter(t ->
                t.getYear().equals("2000") && t.getTeamId().equals(1) && t.getPlayerId().equals(1)
        ).count());
    }

    @Test
    public void shouldReturnMatchingEntryWhenSearchingByAllParameters() {
        final List<Manager> managerList = Collections.singletonList(
                Manager.builder().managerId(1).year("2000").teamId(2).playerId(3).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .year("2000")
                .teamId(2)
                .playerId(3)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(1, result.size());
    }

    @Test
    public void shouldOnlyReturnMatchingEntryWhenSearchingByAllParameters() {
        final List<Manager> managerList = Arrays.asList(
                Manager.builder().managerId(1).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(2).year("2000").teamId(1).playerId(1).build(),
                Manager.builder().managerId(3).year("2001").teamId(1).playerId(1).build(),
                Manager.builder().managerId(4).year("2000").teamId(2).playerId(1).build(),
                Manager.builder().managerId(5).year("2000").teamId(1).playerId(3).build()
        );

        managerRepository.saveAll(managerList);

        final ManagerSearchRequest request = ManagerSearchRequest.builder()
                .managerId(1)
                .year("2000")
                .teamId(1)
                .playerId(1)
                .build();

        final List<ManagerResponse> result = managerSearchService.fetchAll(request);

        assertEquals(1, result.size());
        assertEquals(1, result.stream().filter(t ->
                t.getManagerId().equals(1) &&
                        t.getYear().equals("2000") &&
                        t.getTeamId().equals(1) &&
                        t.getPlayerId().equals(1)
        ).count());
    }
}