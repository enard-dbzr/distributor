package com.plux.distribution.application.service.workflow;

import org.jetbrains.annotations.NotNull;

public interface DataRegistry {

    <T> @NotNull DataKey<T> keyByType(Class<T> type);

    @NotNull DataKey<?> keyById(String id);
}
