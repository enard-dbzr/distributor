package com.plux.distribution.domain.workflow;

import org.jetbrains.annotations.NotNull;

public interface FrameFactory {

    @NotNull Frame get(@NotNull String key);
}
