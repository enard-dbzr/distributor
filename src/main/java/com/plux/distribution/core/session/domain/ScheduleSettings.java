package com.plux.distribution.core.session.domain;

import org.jetbrains.annotations.NotNull;

public record ScheduleSettings(
        @NotNull HoursRange hoursRange,
        @NotNull String timezone,  // TODO: make ZoneId typed
        @NotNull Integer sessionsPerDay
) {

    public ScheduleSettings {
        checkTimezoneValid(timezone);
        checkSessionsPerDateValid(sessionsPerDay);
    }

    public static void checkTimezoneValid(String timezone) throws IllegalArgumentException {
        if (!timezone.matches("^UTC[+-]\\d{1,2}$")) {
            throw new IllegalArgumentException("Invalid time zone");
        }
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
