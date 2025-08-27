package com.plux.distribution.infrastructure.notifier.view.session;

import org.jetbrains.annotations.NotNull;

public record SessionEvent(
        @NotNull SessionEventType sessionEventType,
        @NotNull SessionView session
) {

}
