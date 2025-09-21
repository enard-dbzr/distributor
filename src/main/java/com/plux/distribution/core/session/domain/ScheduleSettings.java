package com.plux.distribution.core.session.domain;

import org.jetbrains.annotations.NotNull;

public record ScheduleSettings(@NotNull HoursRange hoursRange, @NotNull String timezone) {

    public ScheduleSettings {
        if (!timezone.matches("^UTC[+-]\\d{1,2}$")) {
            throw new IllegalArgumentException("Invalid time zone");
        }
    }

    public record HoursRange(int from, int to) {
        public HoursRange {
            if (from > to) {
                throw new IllegalArgumentException("from > to");
            }
        }
    }
}
