package org.natc.app.generator;

import org.natc.app.configuration.LeagueConfiguration;
import org.natc.app.entity.domain.ScheduleData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PreseasonScheduleDataGenerator {

    private final LeagueConfiguration leagueConfiguration;

    public PreseasonScheduleDataGenerator(final LeagueConfiguration leagueConfiguration) {
        this.leagueConfiguration = leagueConfiguration;
    }

    public List<ScheduleData> generate() {
        final List<ScheduleData> scheduleDataList = new ArrayList<>();
        final List<Integer> teamIds = IntStream.rangeClosed(1, leagueConfiguration.getNumberOfTeams()).boxed().collect(Collectors.toList());

        Collections.shuffle(teamIds);

        final List<Integer> staticTeams = teamIds.subList(0, leagueConfiguration.getGamesPerDay());
        final List<Integer> floatingTeams = teamIds.subList(leagueConfiguration.getGamesPerDay(), teamIds.size());

        for (int day = 0; day < leagueConfiguration.getDaysInPreseason(); ++day) {
            final ScheduleData scheduleData = new ScheduleData();

            scheduleData.games = leagueConfiguration.getGamesPerDay();

            for (int match = 0; match < leagueConfiguration.getGamesPerDay(); ++match) {
                if ((day % 2) == 0) {
                    // static teams are home
                    scheduleData.getMatches().add(new ScheduleData.Match(staticTeams.get(match), floatingTeams.get(match)));
                } else {
                    // floating teams are home
                    scheduleData.getMatches().add(new ScheduleData.Match(floatingTeams.get(match), staticTeams.get(match)));
                }
            }

            scheduleDataList.add(scheduleData);

            Collections.rotate(floatingTeams, 1);
        }

        Collections.shuffle(scheduleDataList);

        return scheduleDataList;
    }
}
