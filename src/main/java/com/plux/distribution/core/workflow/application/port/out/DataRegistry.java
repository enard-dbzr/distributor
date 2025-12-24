package com.plux.distribution.core.workflow.application.port.out;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DataRegistry {

    <T> @NotNull DataKey<T> keyByType(Class<T> type);

    @Nullable DataKey<?> keyById(String id);
}
