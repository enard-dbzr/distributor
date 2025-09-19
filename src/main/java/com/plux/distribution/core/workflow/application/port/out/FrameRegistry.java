package com.plux.distribution.core.workflow.application.port.out;

import com.plux.distribution.core.workflow.domain.Frame;
import org.jetbrains.annotations.NotNull;

public interface FrameRegistry {

    @NotNull Frame get(@NotNull String id);

    @NotNull String idByType(@NotNull Class<? extends Frame> type);
}
