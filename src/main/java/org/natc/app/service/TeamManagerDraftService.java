package org.natc.app.service;

import org.natc.app.comparator.TeamComparator;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.Team;
import org.natc.app.exception.NATCException;
import org.natc.app.exception.TeamManagerDraftException;
import org.natc.app.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeamManagerDraftService {

    private final TeamRepository teamRepository;
    private final TeamComparator teamComparator;

    @Autowired
    public TeamManagerDraftService(final TeamRepository teamRepository, final TeamComparator teamComparator) {
        this.teamRepository = teamRepository;
        this.teamComparator = teamComparator;
    }

    public void assignManagersToTeams(final List<Team> teams, final List<Manager> managers) throws NATCException {

        final List<Team> draftTeams = new ArrayList<>();

        for (final Team team : teams) {
            final String previousYear = String.valueOf(Integer.parseInt(team.getYear()) - 1);
            final Example<Team> teamExample = Example.of(Team.builder().teamId(team.getTeamId()).year(previousYear).build());

            teamRepository.findOne(teamExample).ifPresent(draftTeams::add);
        }

        if (draftTeams.isEmpty()) {
            draftTeams.addAll(teams);
            Collections.shuffle(draftTeams);
        }
        else if (draftTeams.size() == teams.size()) {
            draftTeams.sort(teamComparator);
        }
        else {
            throw new TeamManagerDraftException();
        }

        for (final Team team : draftTeams) {
            final Manager manager = managers.stream()
                    .filter(m -> Objects.isNull(m.getTeamId()) && !team.getTeamId().equals(m.getFormerTeamId()))
                    .max(Comparator.comparing(Manager::getPerformanceRating).thenComparing(Manager::getOverallRating))
                    .orElseThrow(TeamManagerDraftException::new);

            manager.setTeamId(team.getTeamId());
            manager.setNewHire(1);
            manager.setScore(0);
            manager.setSeasons(0);
        }
    }
}
