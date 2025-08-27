package com.plux.distribution.infrastructure.notifier.view;

import org.jetbrains.annotations.NotNull;

public record EventView(
        @NotNull EventType eventType,
        Object data
) {

}
