package com.plux.distribution.application.service.workflow;

import com.plux.distribution.domain.workflow.Frame;
import org.jetbrains.annotations.NotNull;

public interface FrameRegistry {

    @NotNull Frame get(@NotNull String id);

    @NotNull String idByType(@NotNull Class<? extends Frame> type);
}
