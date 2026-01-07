package com.plux.distribution.core.session.domain;

import java.time.ZoneId;
import org.jetbrains.annotations.NotNull;

public record ScheduleSettings(
        @NotNull HoursRange hoursRange,
        @NotNull ZoneId timezone,
        @NotNull Integer sessionsPerDay
) {

    public ScheduleSettings {
        checkSessionsPerDateValid(sessionsPerDay);
    }

    public static void checkSessionsPerDateValid(@NotNull Integer sessionsPerDay) throws IllegalArgumentException {
        if (sessionsPerDay < 0) {
            throw new IllegalArgumentException("sessionsPerDate must be not negative");
        }
    }

    public record HoursRange(int from, int to) {

        public HoursRange {
            if (from >= to) {
                throw new IllegalArgumentException("from >= to");
            }
        }
    }
}
