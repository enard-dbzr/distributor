package com.plux.distribution.core.workflow.domain;

import org.jetbrains.annotations.NotNull;

public interface TextProvider {
    @NotNull String getString(@NotNull String key);
}
