package com.plux.distribution.application.workflow.core;

import org.jetbrains.annotations.NotNull;

public interface FrameFactory {

    @NotNull Frame get(@NotNull String key);
}
