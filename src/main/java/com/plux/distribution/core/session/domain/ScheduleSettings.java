package com.plux.distribution.core.session.domain;

import org.jetbrains.annotations.NotNull;

public record ScheduleSettings(@NotNull HoursRange hoursRange) {

    public record HoursRange(int from, int to) {
        public HoursRange {
            if (from > to) {
                throw new IllegalArgumentException("from > to");
            }
        }
    }
}
