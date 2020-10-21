package org.natc.app.service.analysis;

import lombok.Getter;
import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.ManagerStyle;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Component
public class ManagerAnalyzer {

    @Getter
    private static class ManagerStyleValue {
        private final ManagerStyle managerStyle;
        private final Double rating;
        private final Integer priority;

        public ManagerStyleValue(final ManagerStyle managerStyle, final Double rating, final Integer priority) {
            this.managerStyle = managerStyle;
            this.rating = rating;
            this.priority = priority;
        }
    }

    public ManagerStyle determineManagerStyle(final Manager manager) {

        final List<ManagerStyleValue> styleValues = Arrays.asList(
                new ManagerStyleValue(ManagerStyle.OFFENSIVE, manager.getOffense(), 3),
                new ManagerStyleValue(ManagerStyle.DEFENSIVE, manager.getDefense(), 3),
                new ManagerStyleValue(ManagerStyle.INTANGIBLE, manager.getIntangible(), 1),
                new ManagerStyleValue(ManagerStyle.PENALTIES, manager.getPenalties(), 2)
        );

        final ManagerStyleValue bestRating = styleValues.stream()
                .max(Comparator.comparing(ManagerStyleValue::getRating)).orElseThrow();

        final ManagerStyleValue secondBestRating = styleValues.stream()
                .filter(t -> !t.getManagerStyle().equals(bestRating.getManagerStyle()))
                .max(Comparator.comparing(ManagerStyleValue::getRating)).orElseThrow();

        if (bestRating.getRating() - secondBestRating.getRating() >= 0.1) {
            return bestRating.getManagerStyle();
        }

        final ManagerStyleValue thirdBestRating = styleValues.stream()
                .filter(t -> !t.getManagerStyle().equals(bestRating.getManagerStyle()) && !t.getManagerStyle().equals(secondBestRating.getManagerStyle()))
                .max(Comparator.comparing(ManagerStyleValue::getRating)).orElseThrow();

        if (secondBestRating.getRating() - thirdBestRating.getRating() < 0.1) {
            return ManagerStyle.BALANCED;
        }

        if (bestRating.getPriority() > secondBestRating.getPriority()) {
            return bestRating.getManagerStyle();
        }

        if (bestRating.getPriority() < secondBestRating.getPriority()) {
            return secondBestRating.getManagerStyle();
        }

        return ManagerStyle.BALANCED;
    }
}
