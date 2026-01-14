package com.plux.distribution.core.aggregator.application.port.in;

import org.jetbrains.annotations.NotNull;

public interface AddGroupPartUseCase<T> {

    void addPart(@NotNull String groupKey, @NotNull T part);
}
