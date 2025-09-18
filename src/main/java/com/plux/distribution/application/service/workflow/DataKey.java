package com.plux.distribution.application.service.workflow;

import org.jetbrains.annotations.NotNull;

public record DataKey<T>(
        @NotNull String id,
        @NotNull Class<T> type,
        @NotNull DataSerializer<T> serializer
) {

}
