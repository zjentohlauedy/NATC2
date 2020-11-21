package org.natc.app.entity.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ScheduleData {
    public int games;
    public int[] home_teams;
    public int[] road_teams;

    @Getter
    public static class Match {
        private final Integer homeTeam;
        private final Integer roadTeam;

        public Match(final Integer homeTeam, final Integer roadTeam) {
            this.homeTeam = homeTeam;
            this.roadTeam = roadTeam;
        }
    }

    private final List<Match> matches;

    public ScheduleData() {
        this.games = 0;
        this.home_teams = new int[20];
        this.road_teams = new int[20];

        matches = new ArrayList<>();
    }

    public String toString() {
        final char[] array = new char[41];
        int idx = 0;

        array[idx] = (char) this.games;
        array[idx] += '0';

        idx++;

        for (int i = 0; i < this.games; ++i) {
            array[idx] = (char) this.road_teams[i];
            array[idx] += '0';

            idx++;

            array[idx] = (char) this.home_teams[i];
            array[idx] += '0';

            idx++;
        }

        return new String(array);
    }
}
