package com.plux.distribution.core.workflow.domain.frame;

import org.jetbrains.annotations.NotNull;

public interface TextProvider {

    @NotNull String getString(@NotNull String key);
}
