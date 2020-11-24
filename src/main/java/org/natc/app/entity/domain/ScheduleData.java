package org.natc.app.entity.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ScheduleData {

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
        matches = new ArrayList<>();
    }

    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();

        buffer.append(encode(matches.size()));

        for (final Match match : matches) {
            buffer.append(encode(match.getRoadTeam()));
            buffer.append(encode(match.getHomeTeam()));
        }

        return buffer.toString();
    }

    private char encode(final int value) {
        return (char)('0' + value);
    }
}
