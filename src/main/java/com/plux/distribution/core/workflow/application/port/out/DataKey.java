package com.plux.distribution.core.workflow.application.port.out;

import org.jetbrains.annotations.NotNull;

public record DataKey<T>(
        @NotNull String id,
        @NotNull Class<T> type,
        @NotNull DataSerializer<T> serializer
) {

}
