package com.plux.distribution.core.workflow.application.port.out;

import org.jetbrains.annotations.NotNull;

public interface DataRegistry {

    <T> @NotNull DataKey<T> keyByType(Class<T> type);

    @NotNull DataKey<?> keyById(String id);
}
